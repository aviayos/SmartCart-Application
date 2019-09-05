package com.example.orella.smartcart;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.LocationListener;
import android.os.Build;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.orella.smartcart.Common.Result;
import com.example.orella.smartcart.Common.interFace.HTTPCallback;
import com.example.orella.smartcart.Fragment.FilterNetworkFragment;
import com.example.orella.smartcart.Model.Filter;
import com.example.orella.smartcart.Model.Place;
import com.example.orella.smartcart.Model.Product;
import com.example.orella.smartcart.Model.User;
import com.example.orella.smartcart.Utils.APIUtils;
import com.example.orella.smartcart.ui.main.CheeseAdapter;
import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager;
import com.google.android.gms.location.FusedLocationProviderClient;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Cart extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, HTTPCallback, LocationListener {

    private SpeechRecognizer mySpeechRecognizer;
    private TextToSpeech mytts;
    RecyclerView.LayoutManager layoutManager;
     TextView txtFullName;

    ImageView imageView;
    CheeseAdapter adapter;
    Toolbar toolbar;
    DrawerLayout drawer;
    private Button btn_compare;
    private RadioGroup filterGroup;
    private RadioButton filterChoice;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;
    View headerView;
    private SharedPreferencesConfig preferencesConfig;
    HorizontalInfiniteCycleViewPager viewPager;

    private boolean filtering = false;
    private FilterNetworkFragment filterNetworkFragment;
    ArrayList<Product> productList;

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location userLocation;
    private User user;
    private boolean accepted = false;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private Filter userFilter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        getLocationPermission();

        user = getIntent().getExtras().getParcelable(APIUtils.LOGGED_USER);
        productList = new ArrayList<>();

        initData(getIntent().getExtras().getParcelableArrayList(APIUtils.CART_SENT));
        createViewPager();
        createToolBar();
        preferencesConfig = new SharedPreferencesConfig(getApplicationContext());
        createDrawerAndNavigation();

        String str = "This is your cart";
        initializeTextToSpeech(str);
        str = "you can filter according distance, availability and price";
        initializeTextToSpeech(str);
        initializeSpeechRecognizer();
        filterNetworkFragment = FilterNetworkFragment.getInstance(getSupportFragmentManager());
    }

    private void getLocationPermission(){
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                accepted = true;
                getDeviceLocation();

            }else{
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
 }

    private void getDeviceLocation(){
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try{
            if(accepted){
                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            //found location
                            userLocation = (Location) task.getResult();

                        }else{
                            //location is null
                            Toast.makeText(Cart.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e){
            System.err.println(e);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        accepted = false;
        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0){
                    for(int i = 0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            accepted = false;
                            return;
                        }
                    }
                    accepted = true;
                    getDeviceLocation();
                }
            }
        }
    }




    public void comparePlaces(View view) {

        if (accepted) {//user permission accepted
           getDeviceLocation();
            if (filterChoice == null) {
                Toast.makeText(this, "Cannot compare without filter", Toast.LENGTH_SHORT);

            } else {
                if (userLocation != null) {
                    userFilter = getFilter(String.valueOf(filterChoice.getText()));
                    filterNetworkFragment.doFilter(user.getUserID(), productList, userFilter, userLocation);
                    filtering = true;
                } else {
                    Toast.makeText(this, "Please turn on your location services", Toast.LENGTH_SHORT);
                }

            }
//            Intent intent = new Intent(this, ResultsActivity.class);
//            startActivity(intent);

        }
    }



    private Filter getFilter(String text) {

            if (text.equals(Filter.Availability.getName())){
                return Filter.Availability;
            }
            else if (text.equals(Filter.Distance.getName())){
                return Filter.Distance;
            }
            else{
                return Filter.Price;
            }



    }


    private void createDrawerAndNavigation() {
        drawer = findViewById(R.id.cart_draw);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = findViewById(R.id.navigation_my_cart_view);
        navigationView.setNavigationItemSelectedListener(this);
        headerView = navigationView.getHeaderView(0);
        txtFullName = (TextView)headerView.findViewById(R.id.txtFullName);
        txtFullName.setText("Or Ella");
    }

    private void createToolBar() {
        filterGroup = findViewById(R.id.radio_group);
        btn_compare = findViewById(R.id.btn_compare);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("  SmartCart   ");
        getSupportActionBar().setLogo(R.drawable.ic_cart_logo);

    }

    private void createViewPager() {
        viewPager = findViewById(R.id.viewPager);
        adapter =  new CheeseAdapter(this, productList);
        viewPager.setAdapter(adapter);
    }

    private void initData(ArrayList<Product> prods) {
        if (prods != null) {
           productList = prods;

        }
   }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap bitmap = (Bitmap)data.getExtras().get("data");
        imageView.setImageBitmap(bitmap);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.cart_draw);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_search) {
            Intent intent = new Intent(this, SearchViewActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(APIUtils.CART_SENT, productList);
            ArrayList<Product> fullList = getIntent().getExtras().getParcelableArrayList(APIUtils.FULL_PRODS);
            bundle.putParcelableArrayList(APIUtils.FULL_PRODS, fullList);
            bundle.putParcelable(APIUtils.LOGGED_USER, user);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();

        }

        else if (id == R.id.nav_category){
            Intent intent = new Intent(this, MyCartActivity.class);
            Bundle newBun = new Bundle();
            newBun.putParcelableArrayList(APIUtils.CART_SENT, productList);
            newBun.putParcelable(APIUtils.LOGGED_USER, user);
            intent.putExtras(newBun);
            setResult(Activity.RESULT_OK, intent);
            startActivity(intent);
            finish();

        }

        else if( id == R.id.nac_cart){

            //startActivity(new Intent(this, ChatActivity.class));

        }
        else if (id == R.id.nav_about) {

            startActivity(new Intent(this, AboutActivity.class));

        }
        else if (id == R.id.nav_sendreview) {

            startActivity(new Intent(this, SendReviewActivity.class));

        }
        else if (id == R.id.nav_myprofile) {
            Bundle newBun = new Bundle();
            newBun.putParcelable(APIUtils.LOGGED_USER, user);
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.putExtras(newBun);
            startActivity(intent);

        }
        else if (id == R.id.nav_logout) {
            preferencesConfig.writeLoginStatus(false);
            startActivity(new Intent(this , LoginActivity.class));
            finish();

        }
        DrawerLayout drawer = findViewById(R.id.cart_draw);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onPageScrollStateChanged(int i) {

    }

    public void showMyList(View view) {
        startActivity(new Intent(this, ListActivity.class));
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
    public void checkRadioSelected(View v) {
        int radioId = filterGroup.getCheckedRadioButtonId();
        filterChoice = findViewById(radioId);
    }
    /****************************************Implement Network*********************************/
    @Override
    public void updateFailure(String result) {

    }

    @Override
    public void updateSuccess(Result result) {
        if (result.codeResult == HttpURLConnection.HTTP_OK){
            ArrayList<Place> placeRes = new ArrayList<>(result.placeArrayList);
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(APIUtils.PLACES_RESULT, placeRes);
            bundle.putString(APIUtils.FILTER_TYPE, userFilter.getName());
            Intent intent = new Intent(Cart.this, ResultsActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        else{
            Toast.makeText(this, APIUtils.ERR_MESSAGE, Toast.LENGTH_LONG);
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
    public void finish(){
        filtering = false;
        if (filterNetworkFragment != null) {
            filterNetworkFragment.cancelFilter();
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
                    // processResult(results.get(0));
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

    private void initializeTextToSpeech(String str) {
        mytts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(mytts.getEngines().size()==0){
                    Toast.makeText(Cart.this, "There is no tts", Toast.LENGTH_LONG).show();
//                    finish();
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
    public void onPause() {
        super.onPause();
        mytts.shutdown();
    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

//    @Override
//    public void onConnected(@Nullable Bundle bundle) {
//
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//
//    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//
//    }
}
