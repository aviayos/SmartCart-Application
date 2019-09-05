package com.example.orella.smartcart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.example.orella.smartcart.Common.interFace.PlaceClickAction;
import com.example.orella.smartcart.Model.Place;
import com.example.orella.smartcart.Utils.APIUtils;
import com.example.orella.smartcart.ui.main.PlacesAdapter;
import com.google.android.gms.location.FusedLocationProviderClient;

import java.util.ArrayList;

public class ResultsActivity extends AppCompatActivity implements PlaceClickAction {

    private ListView placesResultView;
    private ArrayList<Place> places;
    private PlacesAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        places = new ArrayList<>();
        String filterType = getIntent().getExtras().getString(APIUtils.FILTER_TYPE);
        populateList(filterType);//pass the arguments from the filter activity;
    }

    private void populateList(String filterType) {
        placesResultView = findViewById(R.id.placesList);
        places = getIntent().getParcelableArrayListExtra(APIUtils.PLACES_RESULT);
        adapter = new PlacesAdapter(this, places, filterType);
        placesResultView.setAdapter(adapter);
        placesResultView.setClickable(true);

    }

    @Override
    public void getMap(Place place) {
        Intent intent = new Intent(this, MapActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(APIUtils.PLACE_TARGET, place);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void getInfo(Place place) {
        Intent intent = new Intent(this, PlaceInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(APIUtils.PLACE_TARGET, place);
        intent.putExtras(bundle);
        startActivity(intent);


    }
}
