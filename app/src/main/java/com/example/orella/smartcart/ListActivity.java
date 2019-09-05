package com.example.orella.smartcart;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Slide;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;

import java.util.ArrayList;
import java.util.List;




public class ListActivity extends AppCompatActivity {




    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mToggle;


    private List<String> list;

    private SharedPreferencesConfig preferencesConfig;
    ImageView imageView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = findViewById(R.id.toolbar);


        drawerLayout = (DrawerLayout)findViewById(R.id.drawerList);
        mToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if(mToggle.onOptionsItemSelected(item)) {

            switch (item.getItemId())
            {
                case R.id.logout:
                    Intent listIntent = new Intent(this, LoginActivity.class);
                    startActivity(listIntent);

                default:
                    break;
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bar_menu, menu);
        return true;
    }




    public void userLogOut(View view) {

        preferencesConfig.writeLoginStatus(false);
        startActivity(new Intent(this , LoginActivity.class));
        finish();
    }
}
