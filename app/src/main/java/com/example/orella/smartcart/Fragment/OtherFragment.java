package com.example.orella.smartcart.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.orella.smartcart.Model.Product;
import com.example.orella.smartcart.R;
import com.example.orella.smartcart.ui.main.ProductAdapter;

import java.util.ArrayList;


public class OtherFragment extends Fragment {

    private static final String TAG ="OtherFragment";
    private ArrayList<Product> products;
    private ProductAdapter adapter;
    private ListView productResultView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        products = new ArrayList<>(getArguments().getParcelableArrayList("otherList"));
        populateList();
    }

    private void populateList() {
//        Product test = new Product("Chicken Wings", "12345", "Fish & Meat");
//        Product test1 = new Product("Tuna", "12346", "Fish & Meat");
//        Product test2 = new Product("Salmon", "12347", "Fish & Meat");
//
//        products.add(test2);
//        products.add(test);
//        products.add(test2);
//        products.add(test1);
//        products.add(test);
//        products.add(test1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_other, container, false);

        productResultView = view.findViewById(R.id.productListTabbed);
        adapter = new ProductAdapter(this.getActivity(), products);
        productResultView.setAdapter(adapter);
        productResultView.setClickable(true);


        return view;
    }

}
