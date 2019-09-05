package com.example.orella.smartcart.Fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.example.orella.smartcart.Common.Result;
import com.example.orella.smartcart.Common.interFace.HTTPCallback;
import com.example.orella.smartcart.Model.Product;
import com.example.orella.smartcart.Utils.APIUtils;
import com.example.orella.smartcart.Utils.ConnectionUtils;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;


public class FetchProductsNetworkFragment extends Fragment {

    private static final String TAG = "FetchProductsNetworkFragment";
    private static final String URL_KEY = "UrlKey";
    private HTTPCallback<String> callback;
    private FetchProductsTask fetchProductsTask;
    protected final static String FETCH_PRODUCTS_URL = ConnectionUtils.getInstance().getUrlHost()+"/app/api/metadata";


    public static FetchProductsNetworkFragment getInstance(FragmentManager fragmentManager) {
        FetchProductsNetworkFragment fetchProductsNetworkFragment = new FetchProductsNetworkFragment();
        Bundle args = new Bundle();
        args.putString(URL_KEY, FETCH_PRODUCTS_URL);
        fetchProductsNetworkFragment.setArguments(args);
        fragmentManager.beginTransaction().add(fetchProductsNetworkFragment, TAG).commit();
        return fetchProductsNetworkFragment;
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
        fetchProductsTask.setCallback(callback);
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
        cancelFetchProducts();
        super.onDestroy();
    }

    /**
     * Start non-blocking execution of LoginTask.
     */
    public void doFetchProducts(String userId) {
        cancelFetchProducts();
        fetchProductsTask = new FetchProductsTask(callback, userId);
        fetchProductsTask.execute(FETCH_PRODUCTS_URL);
    }

    /**
     * Cancel (and interrupt if necessary) any ongoing LoginTask execution.
     */
    public void cancelFetchProducts() {
        if (fetchProductsTask != null) {
            fetchProductsTask.cancel(true);
        }
    }

    private class FetchProductsTask extends AsyncTask<String, Integer, Result> {

        private HTTPCallback<String> callback;
        private String mUserId;


        public FetchProductsTask(HTTPCallback<String> callback, String userId) {
            setCallback(callback);
            mUserId = userId;

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
                    Result resultString = fetchProductsUrl(url);
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
                } else if (result.mapResult != null) {
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

        private Result fetchProductsUrl(URL url) throws IOException, JSONException {
            InputStream stream = null;
            HttpURLConnection connection = null;
            ArrayList<String> result = new ArrayList<>();
            try {
                connection = (HttpURLConnection) url.openConnection();
                // Timeout for reading InputStream arbitrarily set to 3000ms.
                connection.setReadTimeout(5000);
                // Timeout for connection.connect() arbitrarily set to 3000ms.
                connection.setConnectTimeout(5000);
                // For this use case, set HTTP method to GET.
                connection.setRequestMethod(APIUtils.GET);
                connection.setRequestProperty(APIUtils.CONTENT_TYPE, APIUtils.JSON_UTF8);
                connection.setRequestProperty(APIUtils.AUTHORIZATION, mUserId);
                // Open communications link (network traffic occurs here).
                connection.connect();
                publishProgress(HTTPCallback.Progress.CONNECT_SUCCESS);
                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_BAD_REQUEST) {
                    result.add(String.valueOf(HttpURLConnection.HTTP_BAD_REQUEST));
                    stream = connection.getErrorStream();
                    result = ConnectionUtils.getInstance().getUserErrors(stream);
                    return new Result(result);

                } else if (responseCode == HttpURLConnection.HTTP_OK) {
                    result.add(String.valueOf(HttpURLConnection.HTTP_OK));
                    stream = connection.getInputStream();
                    publishProgress(HTTPCallback.Progress.GET_INPUT_STREAM_SUCCESS, 0);
                    if (stream != null) {
                        // Converts Stream to String with max length of 500.
                        Map<String, ArrayList<Product>> inp = ConnectionUtils.getInstance().getListOfProducts(stream);
                        return new Result(inp);
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
    }
}
