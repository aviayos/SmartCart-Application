package com.example.orella.smartcart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.orella.smartcart.Model.User;
import com.example.orella.smartcart.Utils.APIUtils;

public class ProfileActivity extends AppCompatActivity {

    private User user;
    private TextView userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        user = new User();
        user = getIntent().getExtras().getParcelable(APIUtils.LOGGED_USER);
        userName = findViewById(R.id.user_name);
        if (user != null){
            userName.setText(user.getUserName());
        }
    }

    public void openMyCart(View view) {
        startActivity(new Intent(this , Cart.class));

    }
}
