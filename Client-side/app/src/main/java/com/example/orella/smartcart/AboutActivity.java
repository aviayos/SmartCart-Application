package com.example.orella.smartcart;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.widget.Toast;

import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AboutActivity extends AppCompatActivity {

    private SpeechRecognizer mySpeechRecognizer;
    private TextToSpeech mytts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        String str = "Smart Cart is a user friendly app which allows you to purchase the cheapest most compatible grocery shopping car. " +
                "soon in google play and app store";

        initializeTextToSpeech(str);
        initializeSpeechRecognizer();

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
                    Toast.makeText(AboutActivity.this, "There is no tts", Toast.LENGTH_LONG).show();
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
