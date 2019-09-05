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
import java.util.List;
import java.util.Map;


public class LoginNetworkFragment extends Fragment {
    public static final String TAG = "LoginNetworkFragment";
    private static final String URL_KEY = "UrlKey";
    private HTTPCallback<String> callback;
    private LoginTask loginTask;
    protected final static String LOGIN_URL = ConnectionUtils.getInstance().getUrlHost()+"/app/login";

    /**
     * Static initializer for LoginNetworkFragment that sets the URL of the host it will be downloading
     * from.
     */
    public static LoginNetworkFragment getInstance(FragmentManager fragmentManager) {
        LoginNetworkFragment loginNetworkFragment = new LoginNetworkFragment();
        Bundle args = new Bundle();
        args.putString(URL_KEY, LOGIN_URL);
        loginNetworkFragment.setArguments(args);
        fragmentManager.beginTransaction().add(loginNetworkFragment, TAG).commit();
        return loginNetworkFragment;
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
        cancelLogin();
        super.onDestroy();
    }

    /**
     * Start non-blocking execution of LoginTask.
     */
    public void doLogin(String email, String pass) {
        cancelLogin();
        loginTask = new LoginTask(callback, email, pass);
        loginTask.execute(LOGIN_URL);
    }

    /**
     * Cancel (and interrupt if necessary) any ongoing LoginTask execution.
     */
    public void cancelLogin() {
        if (loginTask != null) {
            loginTask.cancel(true);
        }
    }

    public class LoginTask extends AsyncTask<String, Integer, Result> {

        private HTTPCallback<String> callback;
        private String userMail;
        private String userPass;



        LoginTask(HTTPCallback<String> callback, String userMail, String userPass) {
            setCallback(callback);
            this.userMail = userMail;
            this.userPass = userPass;
        }

        void setCallback(HTTPCallback<String> callback) {
            this.callback = callback;
        }

        /**
         * Wrapper class that serves as a union of a result value and an exception. When the download
         * task has completed, either the result value or exception can be a non-null value.
         * This allows you to pass exceptions to the UI thread that were thrown during doInBackground().
         */
//        public class LoginResult extends Result {
//            public ArrayList<String> resultValue = new ArrayList<>();
//            public Exception exception;
//
//
//            public LoginResult(ArrayList<String> results) {
//                this.resultValue = results;
//            }
//
//            public LoginResult(Exception exception) {
//                this.resultValue.add("404");
//                this.resultValue.add(exception.getMessage());
//                this.exception = exception;
//            }
//
//            public List<String> getResult(){return this.resultValue;}
//         }

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
            Result loginResult = null;
            if (!isCancelled() && urls != null && urls.length > 0) {
                String urlString = urls[0];
                try {
                    URL url = new URL(urlString);
                    Result result = loginUrl(url);
                    if (result != null) {
                        loginResult = result;
                    } else {
                        throw new IOException("No response received.");
                    }
                } catch (Exception e) {
                    loginResult = new Result(e);
                }
            }
            return loginResult;
        }

        /**
         * Updates the HTTPCallback with the loginResult.
         */
        @Override
        protected void onPostExecute(Result loginResult) {
            if (loginResult != null && callback != null) {
                if (loginResult.exception != null) {
                    callback.updateFailure(loginResult.exception.getMessage());
                } else if (loginResult.resultValue != null) {
                    callback.updateSuccess(loginResult);
                }
                callback.finish();
            }
        }

        /**
         * Override to add special behavior for cancelled AsyncTask.
         */
        @Override
        protected void onCancelled(Result loginResult) {


        }

        private Result loginUrl(URL url) throws IOException, JSONException {
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
                //connection.setRequestProperty("Accept", "application/json");
                connection.setDoOutput(true);
                connection.setDoInput(true);
                Map<String,String> args = new HashMap<>();
                args.put(APIUtils.EMAIL, this.userMail);
                args.put(APIUtils.PASSWORD, this.userPass);

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
                        Map<String,String> inp = ConnectionUtils.getInstance().readStreamWithKeys(stream);
                        result.add(inp.get(APIUtils.TOKEN));
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










