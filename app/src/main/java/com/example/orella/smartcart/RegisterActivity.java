package com.example.orella.smartcart;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;


import com.example.orella.smartcart.Common.Result;
import com.example.orella.smartcart.Common.interFace.HTTPCallback;
import com.example.orella.smartcart.Fragment.RegisterNetworkFragment;
import com.example.orella.smartcart.Utils.APIUtils;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;



public class RegisterActivity extends FragmentActivity implements HTTPCallback {



    private SpeechRecognizer mySpeechRecognizer;
    private TextToSpeech mytts;

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    //"(?=.*[m 0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{4,}" +               //at least 4 characters
                    "$");


    private TextInputLayout textInputPassword;
    private TextInputLayout textInputEmail;
    private TextInputLayout firstName;
    private TextInputLayout lastName;
    private RegisterNetworkFragment registerNetworkFragment;
    private boolean register = false;
    private ProgressDialog dialogPr;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        dialogPr = new ProgressDialog(this);
        dialogPr.setCanceledOnTouchOutside(false);
        textInputEmail = findViewById(R.id.text_input_email);
        firstName = findViewById(R.id.text_input_Fname);
        lastName = findViewById(R.id.last_name_id);
        textInputPassword = findViewById(R.id.text_input_password);
        registerNetworkFragment = RegisterNetworkFragment.getInstance(getSupportFragmentManager());


        String str = "please enter your name, your e-mail address and password";

        initializeTextToSpeech(str);
        initializeSpeechRecognizer();
    }

    public void register(View v) {
        dialogPr.setTitle("Welcome");
        dialogPr.setMessage("please wait while we register your information");
        dialogPr.show();
        if (confirmInput(v)) {
            if (!register && registerNetworkFragment != null) {// Execute the async Login.
                String firstName = this.firstName.getEditText().getText().toString().trim();
                String lastName = this.lastName.getEditText().getText().toString().trim();
                String email = textInputEmail.getEditText().getText().toString().trim();
                String password = textInputPassword.getEditText().getText().toString().trim();
                registerNetworkFragment.doRegister(firstName, lastName, email, password);
                register = true;
            }
        }
        else{
            dialogPr.cancel();
        }
    }


    private boolean validateUsername() {
        String usernameInput = firstName.getEditText().getText().toString().trim();
        String userLastNameInput = lastName.getEditText().getText().toString().trim();

        if (usernameInput.isEmpty() || userLastNameInput.isEmpty()) {
            if(usernameInput.isEmpty())
                firstName.setError("Field can't be empty");
            if(userLastNameInput.isEmpty())
                lastName.setError("Field can't be empty");
            return false;
        } else if (usernameInput.length() > 15 || userLastNameInput.length() > 15) {
            firstName.setError("Username too long");
            return false;
        } else {
            firstName.setError(null);
            lastName.setError(null);
            return true;
        }
    }


    private boolean validateEmail() {
        String emailInput = textInputEmail.getEditText().getText().toString().trim();
        if (emailInput.isEmpty()) {
            textInputEmail.setError("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            textInputEmail.setError("Please enter a valid email address");
            return false;
        } else {
            textInputEmail.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String passwordInput = textInputPassword.getEditText().getText().toString().trim();

        if (passwordInput.isEmpty()) {
            textInputPassword.setError("Field can't be empty");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            textInputPassword.setError("Password too weak");
            return false;
        } else {
            textInputPassword.setError(null);
            return true;
        }
    }

    private boolean confirmInput(View v) {
        if (!validateEmail() | !validateUsername() | !validatePassword()) {
            return false;
        }
        return true;

    }

    @Override
    public void updateFailure(String result) {
        Toast.makeText(this, APIUtils.ERR_MESSAGE, Toast.LENGTH_LONG);

    }

    @Override
    public void updateSuccess(Result result) {
        if (result.resultValue.get(0).equals(String.valueOf(HttpURLConnection.HTTP_OK))) {
            finish();//added
            Intent menuIntent = new Intent(this, LoginActivity.class);
            startActivity(menuIntent);
        } else {
            for (String error : result.resultValue) {
                //String error = err.toString();
                if (!error.contains(APIUtils.STATUS_CODE)) {
                    String field = error.substring(0, error.lastIndexOf(APIUtils.SEPERATOR));
                    String description = error.substring(error.lastIndexOf(APIUtils.SEPERATOR) + 1);
                    switch (field) {
                        case APIUtils.FNAME:
                            firstName.setError(description);
                            break;
                        case APIUtils.LNAME:
                            lastName.setError(description);
                            break;
                        case APIUtils.EMAIL:
                            textInputEmail.setError(description);
                            break;
                        case APIUtils.PASSWORD:
                            textInputPassword.setError(description);
                            break;
                        default:
                            continue;
                    }
                }
            }
        }
        dialogPr.cancel();
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
                //להוסיף הערה למשתמש על בעיה בהתחברות
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
        register = false;
        if (registerNetworkFragment != null) {
            registerNetworkFragment.cancelRegister();
        }
    }


    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(this, LoginActivity.class);
        startActivity(setIntent);
        finish();
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
                    Toast.makeText(RegisterActivity.this, "There is no tts", Toast.LENGTH_LONG).show();
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
