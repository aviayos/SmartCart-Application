package com.example.orella.smartcart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.orella.smartcart.Model.Place;
import com.example.orella.smartcart.Utils.APIUtils;

public class PlaceInfoActivity extends AppCompatActivity {

    private TextView placeOffer;
    private TextView placeDistance;
    private TextView placeAvail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_info);
        placeAvail = findViewById(R.id.place_avail);
        placeDistance = findViewById(R.id.place_distance);
        placeOffer = findViewById(R.id.place_offer);
        Place placeChosed = getIntent().getExtras().getParcelable(APIUtils.PLACE_TARGET);
        setPlaceDetails(placeChosed);

    }

    private void setPlaceDetails(Place place) {
        if (place != null) {
            String offerVal = String.valueOf(place.getOffer());
            placeOffer.setText(offerVal);
            String distanceVal = String.valueOf(place.getDistance());
            placeDistance.setText(distanceVal);
            String availVal = String.valueOf(place.getAvailability());
            placeAvail.setText(availVal);
        }
    }

}
