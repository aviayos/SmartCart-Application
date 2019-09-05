package com.example.orella.smartcart.Fragment;

import android.annotation.SuppressLint;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.orella.smartcart.Model.Product;
import com.example.orella.smartcart.R;
import com.example.orella.smartcart.ui.main.ProductAdapter;

import java.util.ArrayList;


public class FruitVegFragment extends Fragment {

    private static final String TAG ="FruitVegFragment";
    private ArrayList<Product> products;
    private ProductAdapter adapter;
    private ListView productResultView;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        populateList(getArguments().getParcelableArrayList("fruitsAndVegsList"));

    }

    private void populateList(ArrayList<Product> fruitsAndVegsList) {
        products = new ArrayList<>(fruitsAndVegsList);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fruit_veg, container, false);
        productResultView = view.findViewById(R.id.productListTabbed);



        adapter = new ProductAdapter(this.getActivity(), products);
        productResultView.setAdapter(adapter);
        productResultView.setClickable(true);
        return view;
    }


}
