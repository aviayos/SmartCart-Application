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
import com.example.orella.smartcart.Utils.APIUtils;
import com.example.orella.smartcart.Utils.ConnectionUtils;
import org.json.JSONException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegisterNetworkFragment extends Fragment {
    public static final String TAG = "LoginNetworkFragment";
    private static final String URL_KEY = "UrlKey";
    private HTTPCallback<String> callback;
    private RegisterTask registerTask;
    protected final static String REGISTER_URL = ConnectionUtils.getInstance().getUrlHost() + "/app/signup";

    /**
     * Static initializer for NetworkFragment that sets the URL of the host it will be downloading
     * from.
     */
    public static RegisterNetworkFragment getInstance(FragmentManager fragmentManager) {
        RegisterNetworkFragment regNetworkFragment = new RegisterNetworkFragment();
        Bundle args = new Bundle();
        args.putString(URL_KEY, REGISTER_URL);
        regNetworkFragment.setArguments(args);
        fragmentManager.beginTransaction().add(regNetworkFragment, TAG).commit();
        return regNetworkFragment;
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
        cancelRegister();
        super.onDestroy();
    }

    /**
     * Start non-blocking execution of LoginTask.
     */
    public void doRegister(String first, String last, String email, String pass) {
        cancelRegister();
        registerTask = new RegisterTask(callback, first, last, email, pass);
        registerTask.execute(REGISTER_URL);
    }

    /**
     * Cancel (and interrupt if necessary) any ongoing LoginTask execution.
     */
    public void cancelRegister() {
        if (registerTask != null) {
            registerTask.cancel(true);
        }
    }

    private class RegisterTask extends AsyncTask<String, Integer, Result> {

        private HTTPCallback<String> callback;
        private String userLastName;
        private String userFirstName;
        private String userMail;
        private String userPass;

        RegisterTask(HTTPCallback<String> callback, String first, String last, String userMail, String userPass) {
            setCallback(callback);
            this.userFirstName = first;
            this.userLastName = last;
            this.userMail = userMail;
            this.userPass = userPass;
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
                    Result resultString = registerUrl(url);
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

        private Result registerUrl(URL url) throws IOException, JSONException {
            InputStream stream = null;
            HttpURLConnection connection = null;
            ArrayList<String> result = new ArrayList<>();
            try {
                connection = (HttpURLConnection) url.openConnection();
                // Timeout for reading InputStream arbitrarily set to 3000ms.
                connection.setReadTimeout(3000);
                // Timeout for connection.connect() arbitrarily set to 3000ms.
                connection.setConnectTimeout(3000);
                // For this use case, set HTTP method to GET.
                connection.setRequestMethod(APIUtils.POST);
                connection.setRequestProperty(APIUtils.CONTENT_TYPE, APIUtils.JSON_UTF8);
                connection.setDoOutput(true);
                connection.setDoInput(true);
                Map<String, String> args = new HashMap<>();
                args.put(APIUtils.EMAIL, this.userMail);
                args.put(APIUtils.PASSWORD, this.userPass);
                args.put(APIUtils.FNAME, this.userFirstName);
                args.put(APIUtils.LNAME, this.userLastName);
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
                    result.add(String.valueOf(HttpURLConnection.HTTP_OK));
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
