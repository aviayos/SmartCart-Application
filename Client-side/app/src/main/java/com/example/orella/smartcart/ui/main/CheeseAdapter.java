package com.example.orella.smartcart.ui.main;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.orella.smartcart.Model.Product;
import com.example.orella.smartcart.R;

import java.util.ArrayList;
import java.util.List;

public class CheeseAdapter extends PagerAdapter {

    Context context;
    ArrayList<Product> productList;


    public CheeseAdapter(Context context, ArrayList<Product>productList){
        this.context = context;
        this.productList = productList;
    }


    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view.equals(o);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
    View view = LayoutInflater.from(context).inflate(R.layout.card_product_item,container,false);
    TextView name = view.findViewById(R.id.product_title);
    TextView category = view.findViewById(R.id.product_category);
    FloatingActionButton btn_cart = view.findViewById(R.id.btn_cart);
    ImageView productImage = view.findViewById(R.id.product_image);
    TextView quantity = view.findViewById(R.id.product_quantity);

    Product product = productList.get(position);
    name.setText(product.getName());
    category.setText(product.getCategory());
    productImage.setImageResource(product.getImage());
    quantity.setText(String.valueOf(product.getQuantity()));



        view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   Toast.makeText(context, "" + productList.get(position).getName(), Toast.LENGTH_SHORT).show();
                }
            });

     btn_cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Button clicked| You Added...", Toast.LENGTH_SHORT).show();
                }
            });


          container.addView(view);
          return view;

    }




}
