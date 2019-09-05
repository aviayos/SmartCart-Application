package com.example.orella.smartcart;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.orella.smartcart.Common.interFace.ItemAddListener;
import com.example.orella.smartcart.Model.Product;
import com.example.orella.smartcart.Model.User;
import com.example.orella.smartcart.Utils.APIUtils;
import com.example.orella.smartcart.ui.main.ProductAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SearchViewActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener, ItemAddListener {

    private TextView txtFullName;
    private ImageView imageView;

    private SearchView searchView;
    private ArrayList<Product> productsFull;
    private ArrayList<Product> selectedProds;
    private ProductAdapter adapter;
    private ListView productView;
    private android.support.v7.widget.Toolbar toolbar;
    android.widget.Filter filter;
    private SharedPreferencesConfig preferencesConfig;
    private User user;
    private SpeechRecognizer mySpeechRecognizer;
    private TextToSpeech mytts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_view);
        preferencesConfig = new SharedPreferencesConfig(this);
        searchView = findViewById(R.id.search_view);
        productView = findViewById(R.id.product_list_full);
        user = getIntent().getExtras().getParcelable(APIUtils.LOGGED_USER);

        ArrayList<Product> prfromcart = getIntent().getExtras().getParcelableArrayList(APIUtils.CART_SENT);
        selectedProds =new ArrayList<>() ;
        if (prfromcart!= null){
            selectedProds = prfromcart;



            String str = "you can search according product name";

            initializeTextToSpeech(str);
            initializeSpeechRecognizer();

        }

        productsFull = new ArrayList<>(getIntent().getExtras().getParcelableArrayList(APIUtils.FULL_PRODS));
        adapter = new ProductAdapter(this, productsFull);
        productView.setAdapter(adapter);
        productView.setTextFilterEnabled(false);
        filter = adapter.getFilter();
        setupSearchView();
        setBackgroundAndMenu();

    }
    private void setupSearchView()
    {
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint("Search any product here");
    }

    private void setBackgroundAndMenu() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("  SmartCart   ");
        getSupportActionBar().setLogo(R.drawable.ic_cart_logo);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        DrawerLayout drawer = findViewById(R.id.drawering_the_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_my_cart_view);
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
        imageView.setImageBitmap(bitmap);
    }


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
            //startActivity(new Intent(this, SearchViewActivity.class));

        } else if (id == R.id.nav_category) {
            Intent intent = new Intent(this, MyCartActivity.class);
            Bundle newBun = new Bundle();
            newBun.putParcelableArrayList(APIUtils.CART_SENT, this.selectedProds);
            newBun.putParcelable(APIUtils.LOGGED_USER, user);
            intent.putExtras(newBun);
            setResult(Activity.RESULT_OK, intent);
            finish();

        }
        else if (id == R.id.nac_cart) {
            Intent intent = new Intent(this, Cart.class);
            Bundle newBun = new Bundle();
            newBun.putParcelableArrayList(APIUtils.CART_SENT, this.selectedProds);
            newBun.putParcelableArrayList(APIUtils.FULL_PRODS, productsFull);
            newBun.putParcelable(APIUtils.LOGGED_USER, user);
            intent.putExtras(newBun);
            startActivity(intent);
            finish();
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

        } else if (id == R.id.nav_logout) {
            preferencesConfig.writeLoginStatus(false);
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawering_the_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onPageScrollStateChanged(int i) {

    }

    ;

    public void showMyList(View view) {

        startActivity(new Intent(this, ListActivity.class));
    }

    /**************************************************search view**************************************************************************/
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        filter.filter(newText);
        return true;
    }

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
                    Toast.makeText(SearchViewActivity.this, "There is no tts", Toast.LENGTH_LONG).show();
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
