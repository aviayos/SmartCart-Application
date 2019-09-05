package com.example.orella.smartcart.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.orella.smartcart.R;

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;


    public SliderAdapter(Context context){
        this.context = context;
    }


    public int[] slide_images = {
        R.drawable.cheese,
            R.drawable.protein,
            R.drawable.fruits
    };



    public String[] slide_headings = {
            "Cheese", "Meat and Fish", "Vegtables and Fruits"
    };


    public String[] slide_descs = {
            "Lorem ipsum dolor sit amet, consectetur adipcind elit, sed do tempor indidunt ut labore dolore magna" +
                    "aliqua",
            "Lorem ipsum dolor sit amet, consectetur adipcind elit, sed do tempor indidunt ut labore dolore magna" +
                    "aliqua","Lorem ipsum dolor sit amet, consectetur adipcind elit, sed do tempor indidunt ut labore dolore magna" +
            "aliqua"
    };

    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == (RelativeLayout)o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);

        ImageView slideImageView = (ImageView) view.findViewById(R.id.slide_image);
        TextView slideHeading = (TextView) view.findViewById(R.id.slide_heading);
//        TextView slideDescription = (TextView) view.findViewById(R.id.slide_desc);

        slideImageView.setImageResource(slide_images[position]);
        slideHeading.setText(slide_headings[position]);
//        slideDescription.setText(slide_descs[position]);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)object);
    }
}
