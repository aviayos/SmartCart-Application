package com.example.orella.smartcart.Fragment;
import com.example.orella.smartcart.Common.Result;
import com.example.orella.smartcart.Common.interFace.HTTPCallback;
import com.example.orella.smartcart.Utils.APIUtils;
import com.example.orella.smartcart.Utils.ConnectionUtils;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import org.json.JSONException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ForgotPassNetworkFragment extends Fragment {
    public static final String TAG = "ForgotPassNetworkFragment";
    private static final String URL_KEY = "UrlKey";
    private HTTPCallback<String> callback;
    private ForgotTask forgotTask;
    protected final static String FORGOT_URL = ConnectionUtils.getInstance().getUrlHost()+"/app/forgot-password";


    /**
     * Static initializer for LoginNetworkFragment that sets the URL of the host it will be downloading
     * from.
     */
    public static ForgotPassNetworkFragment getInstance(FragmentManager fragmentManager) {
        ForgotPassNetworkFragment networkFragment = new ForgotPassNetworkFragment();
        Bundle args = new Bundle();
        args.putString(URL_KEY, FORGOT_URL);//check these lines again
        networkFragment.setArguments(args);
        fragmentManager.beginTransaction().add(networkFragment, TAG).commit();
        return networkFragment;
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
        cancelReset();
        super.onDestroy();
    }

    /**
     * Start non-blocking execution of LoginTask.
     */
    public void doReset(String email) {
        cancelReset();
        forgotTask = new ForgotTask(callback, email);
        forgotTask.execute(FORGOT_URL);
    }

    /**
     * Cancel (and interrupt if necessary) any ongoing LoginTask execution.
     */
    public void cancelReset() {
        if (forgotTask != null) {
            forgotTask.cancel(true);
        }
    }

    private class ForgotTask extends AsyncTask<String, Integer, Result> {

        private HTTPCallback<String> callback;
        private String userMail;
        ForgotTask(HTTPCallback<String> callback, String userMail) {
            setCallback(callback);
            this.userMail = userMail;
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
                    Result resultString = forgotUrl(url);
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
                } else if (result.resultValue != null) {
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

        private Result forgotUrl(URL url) throws IOException, JSONException {
            InputStream stream = null;
            HttpURLConnection connection = null;
            ArrayList<String> result = new ArrayList<>();
            try {
                connection = (HttpURLConnection) url.openConnection();
                // Timeout for reading InputStream arbitrarily set to 3000ms.
                connection.setReadTimeout(3000);
                // Timeout for connection.connect() arbitrarily set to 3000ms.
                connection.setConnectTimeout(3000);
                // For this use case, set HTTP method to POST.
                connection.setRequestMethod(APIUtils.POST);
                connection.setRequestProperty(APIUtils.CONTENT_TYPE, APIUtils.JSON_UTF8);
                //connection.setRequestProperty("Accept", "application/json");
                connection.setDoOutput(true);
                connection.setDoInput(true);
                Map<String,String> args = new HashMap<>();
                args.put(APIUtils.EMAIL,this.userMail);
                ConnectionUtils.getInstance().setConnectionBody(connection, args);
                // Open communications link (network traffic occurs here).
                connection.connect();
                publishProgress(HTTPCallback.Progress.CONNECT_SUCCESS);
                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_BAD_REQUEST) {
                    result.add(String.valueOf(HttpURLConnection.HTTP_BAD_REQUEST));
                    stream = connection.getErrorStream();
                    result = ConnectionUtils.getInstance().getUserErrors(stream);

                } else if (responseCode == HttpURLConnection.HTTP_OK) {
                    result.add(String.valueOf(responseCode));
                    stream = connection.getInputStream();
                    publishProgress(HTTPCallback.Progress.GET_INPUT_STREAM_SUCCESS, 0);
                    if (stream != null) {
                        // Converts Stream to String with max length of 500.
                        String inp = ConnectionUtils.getInstance().readStream(stream, 500);
                        result.add(inp);
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
