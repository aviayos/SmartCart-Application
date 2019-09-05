package com.example.orella.smartcart;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.orella.smartcart.LoginActivity;
import com.example.orella.smartcart.R;
import com.felipecsl.gifimageview.library.GifImageView;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.InflaterOutputStream;


public class SplashActivity extends AppCompatActivity {

    private GifImageView gifImageView;
    private ImageView imageView;

//    public void fade(View view){
//        ImageView cart = (ImageView)findViewById(R.id.imageView7);
//        cart.animate().alpha(0f).setDuration(1500);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);




    }
}
