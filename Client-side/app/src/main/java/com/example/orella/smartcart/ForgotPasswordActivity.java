package com.example.orella.smartcart;

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
import android.widget.Button;
import android.widget.Toast;

import com.example.orella.smartcart.Common.Result;
import com.example.orella.smartcart.Common.interFace.HTTPCallback;
import com.example.orella.smartcart.Fragment.ForgotPassNetworkFragment;
import com.example.orella.smartcart.Utils.APIUtils;

import java.net.HttpURLConnection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;


public class ForgotPasswordActivity extends FragmentActivity implements HTTPCallback {

    private ForgotPassNetworkFragment forgotPassNetworkFragment;
    private TextInputLayout emailToSend;
    private boolean forgotPass = false;
    private SpeechRecognizer mySpeechRecognizer;
    private TextToSpeech mytts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        emailToSend = findViewById(R.id.last_name_id);
        Button buttonSend = findViewById(R.id.button_send);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendResetReq();
            }
        });
        forgotPassNetworkFragment = ForgotPassNetworkFragment.getInstance(getSupportFragmentManager());


        String str = "if you forgot your password, just write your e-mail, send it to us," +
                "and we will send you e-mail with new password";

        initializeTextToSpeech(str);
        initializeSpeechRecognizer();
    }

    private void sendResetReq() {

        if (validateEmail()) {
            if (!forgotPass && forgotPassNetworkFragment != null) {// Execute the async.
                String email = emailToSend.getEditText().getText().toString().trim();
                forgotPassNetworkFragment.doReset(email);
                forgotPass = true;
            }

        }
    }

    private boolean validateEmail() {
        String emailInput = emailToSend.getEditText().getText().toString().trim();
        emailToSend.setBackgroundColor(1);


        if (emailInput.isEmpty()) {
            emailToSend.setError("Field can't be empty");
            return false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            emailToSend.setError("Please enter a valid email address");
            return false;
        } else {
            emailToSend.setError(null);
            return true;
        }
    }

    @Override
    public void updateFailure(String result) {

    }

    @Override
    public void updateSuccess(Result result) {
        if (result.resultValue.get(0).equals(String.valueOf(HttpURLConnection.HTTP_OK))) {
            emailToSend.setHelperText(result.resultValue.get(1));
            emailToSend.setHelperTextTextAppearance(R.style.TextAppearance_Compat_Notification);
            int timeout = 5000; // make the activity visible for 4 seconds
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    finish();
                    Intent homepage = new Intent(forgotPassNetworkFragment.getActivity(), LoginActivity.class);
                    startActivity(homepage);
                }
            }, timeout);

        } else {
            for (String error : result.resultValue) {
                if (!error.contains(APIUtils.STATUS_CODE)) {
                    String description = error.substring(error.lastIndexOf(APIUtils.SEPERATOR) + 1);
                    emailToSend.setError(description);
                    }
                }
            }

    }

//    @Override
//    public void updateSuccess(ArrayList result) {
//        if (result.get(0).toString().equals(String.valueOf(HttpURLConnection.HTTP_OK))) {
//            emailToSend.setHelperText(result.get(1).toString());
//            emailToSend.setHelperTextTextAppearance(R.style.TextAppearance_Compat_Notification);
//            int timeout = 5000; // make the activity visible for 4 seconds
//            Timer timer = new Timer();
//            timer.schedule(new TimerTask() {
//                @Override
//                public void run() {
//                    finish();
//                    Intent homepage = new Intent(forgotPassNetworkFragment.getActivity(), LoginActivity.class);
//                    startActivity(homepage);
//                }
//            }, timeout);
//
//        } else {
//            for (Object err : result) {
//                String error = err.toString();
//                if (!error.contains(APIUtils.STATUS_CODE)) {
//                    String field = error.substring(0, error.lastIndexOf(APIUtils.SEPERATOR));
//                    String description = error.substring(error.lastIndexOf(APIUtils.SEPERATOR) + 1);
//                    emailToSend.setError(description);
//                    }
//                }
//            }
//        }


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
        forgotPass = false;
        if (forgotPassNetworkFragment != null) {
            forgotPassNetworkFragment.cancelReset();
        }

    }

    public void sendResetReq(View view) {
        sendResetReq();
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
                    Toast.makeText(ForgotPasswordActivity.this, "There is no tts", Toast.LENGTH_LONG).show();
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
