package com.example.orella.smartcart.Fragment;

import android.content.Context;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.example.orella.smartcart.Common.Result;
import com.example.orella.smartcart.Common.interFace.HTTPCallback;
import com.example.orella.smartcart.Model.Filter;
import com.example.orella.smartcart.Model.Place;
import com.example.orella.smartcart.Model.Product;
import com.example.orella.smartcart.Utils.APIUtils;
import com.example.orella.smartcart.Utils.ConnectionUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilterNetworkFragment extends Fragment {

    public static final String TAG = "FilterNetworkFragment";
    private static final String URL_KEY = "UrlKey";
    private HTTPCallback<String> callback;
    private FilterTask filterTask;
    protected final static String FILTER_URL = ConnectionUtils.getInstance().getUrlHost()+"/app/api/filter";

    /**
     * Static initializer for LoginNetworkFragment that sets the URL of the host it will be downloading
     * from.
     */
    public static FilterNetworkFragment getInstance(FragmentManager fragmentManager) {
        FilterNetworkFragment filterNetworkFragment = new FilterNetworkFragment();
        Bundle args = new Bundle();
        args.putString(URL_KEY, FILTER_URL);
        filterNetworkFragment.setArguments(args);
        fragmentManager.beginTransaction().add(filterNetworkFragment, TAG).commit();
        return filterNetworkFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //urlString = getArguments().getString(URL_KEY);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Host Activity will handle callbacks from task.
        callback = (HTTPCallback<String>) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // Clear reference to host Activity to avoid memory leak.
        callback = null;
    }

    @Override
    public void onDestroy() {
        // Cancel task when Fragment is destroyed.
        cancelFilter();
        super.onDestroy();
    }

    /**
     * Start non-blocking execution of LoginTask.
     */
    public void doFilter(String userID, List<Product> userCart, Filter filters, Location userLoc) {
        cancelFilter();
        filterTask = new FilterTask(callback,userID, userCart, filters, userLoc);
        filterTask.execute(FILTER_URL);
    }

    /**
     * Cancel (and interrupt if necessary) any ongoing LoginTask execution.
     */
    public void cancelFilter() {
        if (filterTask != null) {
            filterTask.cancel(true);
        }
    }

    private class FilterTask extends AsyncTask<String, Integer, Result> {

        private HTTPCallback<String> callback;
        private List<Product> userCart;
        private Location userLoc;
        private Filter filters;
        private String userID;


        public FilterTask(HTTPCallback<String> callback, String userID, List<Product> userCart, Filter filters, Location userLoc) {
            setCallback(callback);
            this.userCart = userCart;
            this.filters = filters;
            this.userLoc = userLoc;
            this.userID = userID;

        }

        void setCallback(HTTPCallback<String> callback) {
            this.callback = callback;
        }


        /**
         * Cancel background network operation if we do not have network connectivity.
         */
        @Override
        protected void onPreExecute() {
            if (callback != null) {
                NetworkInfo networkInfo = callback.getActiveNetworkInfo();
                if (networkInfo == null || !networkInfo.isConnected() ||
                        (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                                && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
                    // If no connectivity, cancel task and update Callback with null data.
                    callback.updateFailure(null);
                    cancel(true);
                }
            }
        }

        /**
         * Defines work to perform on the background thread.
         */
        @Override
        protected Result doInBackground(String... urls) {
            Result result = null;
            if (!isCancelled() && urls != null && urls.length > 0) {
                String urlString = urls[0];
                try {
                    URL url = new URL(urlString);
                    Result resultString = filterUrl(url);
                    if (resultString != null) {
                        result = resultString;
                    } else {
                        throw new IOException("No response received.");
                    }
                } catch (Exception e) {
                    result = new Result(e);
                }
            }
            return result;
        }

        /**
         * Updates the HTTPCallback with the result.
         */
        @Override
        protected void onPostExecute(Result result) {
            if (result != null && callback != null) {
                if (result.exception != null) {
                    callback.updateFailure(result.exception.getMessage());
                } else if (result.placeArrayList != null) {
                    callback.updateSuccess(result);
                }
                callback.finish();
            }
        }

        /**
         * Override to add special behavior for cancelled AsyncTask.
         */
        @Override
        protected void onCancelled(Result result) {


        }

        private Result filterUrl(URL url) throws IOException, JSONException {
            InputStream stream = null;
            HttpURLConnection connection = null;
            ArrayList<String> result = new ArrayList<>();
            try {
                connection = (HttpURLConnection) url.openConnection();
                // Timeout for reading InputStream arbitrarily set to 3000ms.
                connection.setReadTimeout(8000);
                // Timeout for connection.connect() arbitrarily set to 3000ms.
                connection.setConnectTimeout(8000);
                // For this use case, set HTTP method to GET.
                connection.setRequestMethod(APIUtils.POST);
                connection.setRequestProperty(APIUtils.CONTENT_TYPE, APIUtils.JSON_UTF8);
                connection.setRequestProperty(APIUtils.AUTHORIZATION, userID);
                connection.setDoOutput(true);
                connection.setDoInput(true);
                Map<String,Object> args = getArgsMap();
                ConnectionUtils.getInstance().setConnectionBodyJSON(connection, args);
                // Open communications link (network traffic occurs here).
                connection.connect();
                publishProgress(HTTPCallback.Progress.CONNECT_SUCCESS);
                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_BAD_REQUEST) {
                    result.add(String.valueOf(HttpURLConnection.HTTP_BAD_REQUEST));
                    stream = connection.getErrorStream();
                    result = ConnectionUtils.getInstance().getUserErrors(stream);

                } else if (responseCode == HttpURLConnection.HTTP_OK) {
                    stream = connection.getInputStream();
                    publishProgress(HTTPCallback.Progress.GET_INPUT_STREAM_SUCCESS, 0);
                    if (stream != null) {
                        // Converts Stream to String with max length of 500.
                        ArrayList<Place> inp = ConnectionUtils.getInstance().getListOfPlaces(stream);
                        Result result1 = new Result(inp);
                        result1.codeResult = HttpURLConnection.HTTP_OK;
                        return result1;
                    }
                } else {
                    throw new IOException("HTTP error code: " + responseCode);
                }
            } finally {
                // Close Stream and disconnect HTTP connection.
                if (stream != null) {
                    stream.close();
                }
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return new Result(result);
        }



        private Map<String, Object> getArgsMap() throws JSONException {
            Map<String, Object> args = new HashMap<>();
            JSONObject loc = ConnectionUtils.getInstance().getLocationAsJson(userLoc.getLatitude(),userLoc.getLongitude());
            JSONArray prods = ConnectionUtils.getInstance().getProducts(userCart);

            args.put(APIUtils.FILTER_TYPE, filters.getCode());
            args.put(APIUtils.PRODS_LIST,prods);
            args.put(APIUtils.LOCATION,loc);

            return args;

        }
    }



}
