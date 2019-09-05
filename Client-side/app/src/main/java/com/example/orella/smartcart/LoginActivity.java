package com.example.orella.smartcart;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;
import com.example.orella.smartcart.Common.Result;
import com.example.orella.smartcart.Common.interFace.HTTPCallback;
import com.example.orella.smartcart.Fragment.LoginNetworkFragment;
import com.example.orella.smartcart.Model.User;
import com.example.orella.smartcart.Utils.APIUtils;
import java.net.HttpURLConnection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;


public class LoginActivity extends FragmentActivity implements HTTPCallback {
    private LoginNetworkFragment loginNetworkFragment;
    private boolean login = false;
    private SharedPreferencesConfig preferencesConfig;
    private TextInputLayout userPassword;
    private TextInputLayout userMail;
    private ProgressDialog dialogPr;
    private SpeechRecognizer mySpeechRecognizer;
    private TextToSpeech mytts;
    private User user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        preferencesConfig = new SharedPreferencesConfig(getApplicationContext());
        dialogPr = new ProgressDialog(this);
        dialogPr.setCanceledOnTouchOutside(false);
        userMail = findViewById(R.id.user_mail);
        userPassword = findViewById(R.id.user_password);
        loginNetworkFragment = LoginNetworkFragment.getInstance(getSupportFragmentManager());

        String str = "Welcome to Smart Cart";
        initializeTextToSpeech(str);
        initializeSpeechRecognizer();
     }

    public void loginUser(View view) {
        dialogPr.setTitle("Welcome to SmartCart");
        dialogPr.setMessage("please wait while we loading your data");
        dialogPr.show();
        String userMail = this.userMail.getEditText().getText().toString().trim();
        String userPassword = this.userPassword.getEditText().getText().toString().trim();

        if (confirmInput(view)) {
            if (!login && loginNetworkFragment != null) {
                // Execute the async Login.
                user = new User(userMail, userPassword);
                loginNetworkFragment.doLogin(userMail, userPassword);
                login = true;
            }
        }
        else{
            dialogPr.cancel();
        }
//        loginWithNoServer(userMail, userPassword);
    }

//    private void loginWithNoServer(String userMail, String userPassword) {
//        if (userMail.equals(getResources().getString(R.string.user_mail)) &&
//                userPassword.equals(getResources().getString(R.string.user_password))) {
//            startActivity(new Intent(this, Cart.class));
//            preferencesConfig.writeLoginStatus(true);
//            finish();
//
//        } else {
//            Toast.makeText(this, "Login Faild, Try Again..", Toast.LENGTH_LONG).show();
//            this.userMail.getEditText().setText(" ");
//            this.userPassword.getEditText().setText(" ");
//
//        }
//        else{
//            dialogPr.cancel();
//        }
//
//    }

    public void goRegister(View view) {
        Intent regIntent = new Intent(this, RegisterActivity.class);
        startActivity(regIntent);
    }


    public void forgetPassword(View view) {
        Intent regIntent = new Intent(this, ForgotPasswordActivity.class);
        startActivity(regIntent);
    }


    private boolean validateEmail() {
        String emailInput = userMail.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            userMail.setError("Field can't be empty");
            initializeTextToSpeech("Field can't be empty");

            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            userMail.setError("Please enter a valid email address");
            initializeTextToSpeech("Please enter a valid email address");

            return false;
        } else {
            userMail.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String passwordInput = userPassword.getEditText().getText().toString().trim();
        if (passwordInput.isEmpty()) {
            initializeTextToSpeech("Field can't be empty");
            userPassword.setError("Field can't be empty");
            return false;
        } else {
            userPassword.setError(null);
            return true;
        }
    }

    public boolean confirmInput(View v) {
        if (!validateEmail() | !validatePassword()) {
            return false;
        }
        return true;
    }


    @Override
    public void updateFailure(String result) {
        // Update your UI here based on result of download.
    }

    @Override
    public void updateSuccess(Result result) {
        if (result.resultValue.get(0).equals(String.valueOf(HttpURLConnection.HTTP_OK))) {

            String userID = result.resultValue.get(1);
            user.setUserID(userID);

            int timeout = 2000; // make the activity visible for 4 seconds
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    finish();
                    Intent intent = new Intent(loginNetworkFragment.getActivity(), MyCartActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(APIUtils.LOGGED_USER, user);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    preferencesConfig.writeLoginStatus(true);
                    dialogPr.cancel();
                }
            }, timeout);

        } else {
            for (Object err : result.resultValue) {
                String error = err.toString();
                if (!error.contains(APIUtils.STATUS_CODE)) {
                    String field = error.substring(0, error.lastIndexOf(APIUtils.SEPERATOR));
                    String description = error.substring(error.lastIndexOf(APIUtils.SEPERATOR) + 1);
                    switch (field) {
                        case APIUtils.EMAIL: {
                            userMail.setError(description);
                            initializeTextToSpeech(description);
                        }
                            break;
                        case APIUtils.PASSWORD: {
                            userPassword.setError(description);
                            initializeTextToSpeech(description);

                        }

                            break;
                        default:
                            continue;
                    }
                }
            }
            dialogPr.cancel();
        }
    }

    @Override
    public NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;
    }

    @Override
    public void onProgressUpdate(int progressCode, int percentComplete) {
        switch (progressCode) {
            // You can add UI behavior for progress updates here.
            case Progress.ERROR:
                System.out.println("connect Error");
                break;
            case Progress.CONNECT_SUCCESS:
                System.out.println("connect success!");
                break;
            case Progress.GET_INPUT_STREAM_SUCCESS:
                System.out.println("connect GET_INPUT_STREAM_SUCCESS");
                break;
            case Progress.PROCESS_INPUT_STREAM_IN_PROGRESS:
                System.out.println("connect PROCESS_INPUT_STREAM_IN_PROGRESS");
                break;
            case Progress.PROCESS_INPUT_STREAM_SUCCESS:
                System.out.println("connect PROCESS_INPUT_STREAM_SUCCESS");
                break;
        }
    }

    @Override
    public void finish() {
        login = false;
        if (loginNetworkFragment != null) {
            loginNetworkFragment.cancelLogin();
        }
    }


    private void initializeSpeechRecognizer() {
        if(SpeechRecognizer.isRecognitionAvailable(this)){
            mySpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
            mySpeechRecognizer.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle params) {

                }
                @Override
                public void onBeginningOfSpeech() {

                }
                @Override
                public void onRmsChanged(float rmsdB) {

                }
                @Override
                public void onBufferReceived(byte[] buffer) {

                }
                @Override
                public void onEndOfSpeech() {

                }
                @Override
                public void onError(int error) {

                }
                @Override
                public void onResults(Bundle bundle) {
                    List<String> results = bundle.getStringArrayList(
                            SpeechRecognizer.RESULTS_RECOGNITION
                    );
                    processResult(results.get(0));
                }
                @Override
                public void onPartialResults(Bundle partialResults) {
                }
                @Override
                public void onEvent(int eventType, Bundle params) {

                }
            });
        }
    }

    private void processResult(String command) {
        if(command.indexOf("whats")!= -1) {
            if (command.indexOf("your name") != -1)
                speak("my name is Shmuel");
        }

        if(command.indexOf("time")!= -1) {
            Date now = new Date();
            String time = DateUtils.formatDateTime(this, now.getTime(),
                    DateUtils.FORMAT_SHOW_TIME);
            speak("the time now is" + time);
        }
        else if(command.indexOf("open")!=-1){
            if(command.indexOf("browser")!=-1) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.google.com"));
                startActivity(intent);

            }
        }
    }

    private void initializeTextToSpeech(String str) {
        mytts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(mytts.getEngines().size()==0){
                    Toast.makeText(LoginActivity.this, "There is no tts", Toast.LENGTH_LONG).show();
                    finish();
                }


                else{
                    mytts.setLanguage(Locale.US);
                    speak(str);
                }
            }
        });
    }

    private void speak(String messsage) {
        if(Build.VERSION.SDK_INT >= 21){
            mytts.speak(messsage, TextToSpeech.QUEUE_FLUSH ,null,null);
        }
        else {
            mytts.speak(messsage, TextToSpeech.QUEUE_FLUSH ,null);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        mytts.shutdown();
    }
}
