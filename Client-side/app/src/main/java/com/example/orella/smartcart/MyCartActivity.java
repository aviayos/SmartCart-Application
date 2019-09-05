package com.example.orella.smartcart;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.orella.smartcart.Common.Result;
import com.example.orella.smartcart.Common.interFace.HTTPCallback;
import com.example.orella.smartcart.Common.interFace.ItemAddListener;
import com.example.orella.smartcart.Fragment.DairyFragment;
import com.example.orella.smartcart.Fragment.FetchProductsNetworkFragment;
import com.example.orella.smartcart.Fragment.FishMeatFragment;
import com.example.orella.smartcart.Fragment.FruitVegFragment;
import com.example.orella.smartcart.Fragment.OtherFragment;
import com.example.orella.smartcart.Model.Product;
import com.example.orella.smartcart.Model.User;
import com.example.orella.smartcart.Utils.APIUtils;
import com.example.orella.smartcart.ui.main.CategoriesPageAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MyCartActivity extends AppCompatActivity implements HTTPCallback, ItemAddListener, NavigationView.OnNavigationItemSelectedListener  {
    private ViewPager mViewPager;
    private FetchProductsNetworkFragment fetchProductsNetworkFragment;
    private TabLayout tabLayout;
    private NavigationView navigationView;
    private TextView txtFullName;
    private ImageView imageView;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
    private Bundle bundle;

    private CategoriesPageAdapter mCategoriesPageAdapter;
    private boolean fetching = false;
    private SharedPreferencesConfig preferencesConfig;
    private Map<String, ArrayList<Product>> productMapList;
    private ArrayList<Product> selectedProds;
    private User user;
    private static final String TAG = "MyCartActivity";

    private static final int LIST_ADDED = 1;
    private SpeechRecognizer mySpeechRecognizer;
    private TextToSpeech mytts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);
        user = getIntent().getExtras().getParcelable(APIUtils.LOGGED_USER);
        preferencesConfig = new SharedPreferencesConfig(getApplicationContext());
        mCategoriesPageAdapter = new CategoriesPageAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.category_content);
        tabLayout = findViewById(R.id.tabs);
        ArrayList<Product> prodInList = getIntent().getExtras().getParcelableArrayList(APIUtils.CART_SENT);

        if (prodInList == null)
            selectedProds = new ArrayList<>();
        else
            selectedProds = prodInList;

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        setBackgroundAndMenu();
        getMetaData();

        String str = "what would you like to buy today?";

        initializeTextToSpeech(str);
        initializeSpeechRecognizer();
    }

    private void getMetaData() {
        fetchProductsNetworkFragment = FetchProductsNetworkFragment.getInstance(getSupportFragmentManager());
        fetchProductsNetworkFragment.doFetchProducts(user.getUserID());
        fetching = true;
    }

    private void setBackgroundAndMenu() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("  SmartCart   ");

        getSupportActionBar().setLogo(R.drawable.ic_cart_logo);

        drawer = (DrawerLayout) findViewById(R.id.drawering_the_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.navigation_my_cart_view);
        navigationView.setNavigationItemSelectedListener(this);


        View headerView = navigationView.getHeaderView(0);
        txtFullName = headerView.findViewById(R.id.txtFullName);
        txtFullName.setText("Or");
    }

    private void setupViewPager(ViewPager v, Bundle bundle){
        CategoriesPageAdapter adapter = new CategoriesPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new FruitVegFragment(), "Fruit & Veg.", bundle);
        adapter.addFragment(new FishMeatFragment(), "Fish & Meat",bundle);
        adapter.addFragment(new DairyFragment(), "Dairy",bundle);
        adapter.addFragment(new OtherFragment(), "Other",bundle);
        v.setAdapter(adapter);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode)
        {
            case (LIST_ADDED) : {
                if (resultCode == Activity.RESULT_OK) {
                    ArrayList<Product> updatedList = data.getExtras().getParcelableArrayList("cart");
                    if (updatedList != null){
                        selectedProds = updatedList;
                    }
                }
                break;
            }
        }
    }

    /****************************************Implement Network*************************************/
    @Override
    public void updateFailure(String result) {

    }

    @Override
    public void updateSuccess(Result result) {
        if (!result.mapResult.isEmpty()){
            productMapList = new HashMap<>(result.mapResult);
            preferencesConfig.writeMetaStatus(true);
            bundle = new Bundle();
            bundle.putParcelableArrayList("meatList", productMapList.get(APIUtils.MEAT));
            bundle.putParcelableArrayList("dairyList", productMapList.get(APIUtils.DAIRY));
            bundle.putParcelableArrayList("fruitsAndVegsList", productMapList.get(APIUtils.FRUIT_AND_VEGS));
            bundle.putParcelableArrayList("otherList", productMapList.get(APIUtils.OTHER));

            setupViewPager(mViewPager, bundle);

            tabLayout.setupWithViewPager(mViewPager);
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

    }

    @Override
    public void finish() {
        fetching = false;
        if (fetchProductsNetworkFragment != null) {
            fetchProductsNetworkFragment.cancelFetchProducts();
        }
    }
    /*******************************************Implement Item Add Listener************************/
    @Override
    public void addProductToCart(Product product) {
        if (selectedProds.contains(product)) {
            int oldProd = selectedProds.indexOf(product);
            int newQuan = product.getQuantity() + selectedProds.get(oldProd).getQuantity();
            product.setQuantity(newQuan);
            selectedProds.remove(oldProd);
            selectedProds.add(product);

        }
        else{
            selectedProds.add(product);
        }
    }
    /*****************************Implement Navigation****************************************/
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawering_the_layout);
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
            //put search
            Intent intent = new Intent(this, SearchViewActivity.class);
            ArrayList<Product> fullList = getFullListOfProds();
            bundle.putParcelableArrayList(APIUtils.CART_SENT, this.selectedProds);
            bundle.putParcelableArrayList(APIUtils.FULL_PRODS, fullList);
            bundle.putParcelable(APIUtils.LOGGED_USER, user);
            intent.putExtras(bundle);
            startActivityForResult(intent, LIST_ADDED);
        }

        else if (id == R.id.nav_category){
            //put search

        }

        else if( id == R.id.nac_cart){
            Intent intent = new Intent(this, Cart.class);
            ArrayList<Product> fullList = getFullListOfProds();
            bundle.putParcelableArrayList(APIUtils.CART_SENT, this.selectedProds);
            bundle.putParcelableArrayList(APIUtils.FULL_PRODS, fullList);
            bundle.putParcelable(APIUtils.LOGGED_USER, user);
            intent.putExtras(bundle);
            startActivityForResult(intent, LIST_ADDED);
            //finish();
        }

        else if (id == R.id.nav_about) {

            startActivity(new Intent(this, AboutActivity.class));

        }
        else if (id == R.id.nav_sendreview) {

            startActivity(new Intent(this, SendReviewActivity.class));

        }

        else if (id == R.id.nav_myprofile) {
            bundle.putParcelable(APIUtils.LOGGED_USER, user);
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);

        }
        else if (id == R.id.nav_logout) {
            preferencesConfig.writeLoginStatus(false);
            startActivity(new Intent(this , LoginActivity.class));
            finish();

        }
        DrawerLayout drawer = findViewById(R.id.drawering_the_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private ArrayList<Product> getFullListOfProds() {
        ArrayList<Product> list = new ArrayList<>();
        list.addAll(productMapList.get(APIUtils.MEAT));
        list.addAll(productMapList.get(APIUtils.DAIRY));
        list.addAll(productMapList.get(APIUtils.OTHER));
        list.addAll(productMapList.get(APIUtils.FRUIT_AND_VEGS));
        return list;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
    /************************************/

    public void onPageScrollStateChanged(int i) {

    };

    public void showMyList(View view) {

        startActivity(new Intent(this, ListActivity.class));
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
                    Toast.makeText(MyCartActivity.this, "There is no tts", Toast.LENGTH_LONG).show();
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