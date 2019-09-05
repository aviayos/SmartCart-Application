package com.example.orella.smartcart.ui.main;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.orella.smartcart.Common.interFace.PlaceClickAction;
import com.example.orella.smartcart.Model.Filter;
import com.example.orella.smartcart.Model.Place;
import com.example.orella.smartcart.R;

import java.util.ArrayList;

public class PlacesAdapter extends BaseAdapter {

    Activity context;
    private ArrayList<Place> places;
    private static LayoutInflater inflater = null;
    private String filter;

    public PlacesAdapter(Activity context, ArrayList<Place> places, String filterType){
        this.context = context;
        this.places = places;
        this.filter = filterType;
        inflater  = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return places.size();
    }

    @Override
    public Place getItem(int position) {
        return places.get(position);
    }

    @Override//should return the id of the place
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       View placeView;
       placeView = (convertView == null) ? inflater.inflate(R.layout.places_list_item, null) : convertView;
        TextView pName  = placeView.findViewById(R.id.place_name);
        TextView pAddress  = placeView.findViewById(R.id.place_address);
        TextView filterRes  = placeView.findViewById(R.id.filter_cat);
        ImageButton mapPlace = placeView.findViewById(R.id.mapView);
        ImageButton moreInfo = placeView.findViewById(R.id.moreInfo);
        ImageView logo = placeView.findViewById(R.id.placeLogo);

        Place selected = places.get(position);
        pName.setText(selected.getName());
        pAddress.setText(selected.getAddress());
        if (position == 0)
            logo.setImageResource(R.drawable.logo1);
        else if (position == 1)
            logo.setImageResource(R.drawable.logo2);
        else
            logo.setImageResource(R.drawable.logo3);

        String filterValue = getFilterVal(selected, filter);
        filterRes.setText(filterValue);
        moreInfo.setClickable(true);
        mapPlace.setClickable(true);


        placeView.setTag(places.get(position));

        moreInfo.setOnClickListener((view)->{
            Place selectedPlace = (Place)placeView.getTag();
            if (context instanceof PlaceClickAction) {
                ((PlaceClickAction) context).getInfo(selectedPlace);
            }
        });

        mapPlace.setOnClickListener((view)->{
            Place selectedPlace = (Place)placeView.getTag();
            if (context instanceof PlaceClickAction) {
                ((PlaceClickAction) context).getMap(selectedPlace);
            }
        });


        return placeView;
    }

    private String getFilterVal(Place place, String filter) {
        if (filter.equals(Filter.Availability.getName())){
            return place.getAvailability() + "%";
        }
        else if (filter.equals(Filter.Distance.getName())){
            return place.getDistance() + " km";
        }
        else{
            return place.getOffer() + "$";
        }
    }
}
