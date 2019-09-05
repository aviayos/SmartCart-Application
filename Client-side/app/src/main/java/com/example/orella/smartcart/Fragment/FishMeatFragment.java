package com.example.orella.smartcart.Fragment;

import android.content.Context;
import android.net.Uri;
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


public class FishMeatFragment extends Fragment {
    private static final String TAG ="FishMeatFragment";
    private ArrayList<Product> products;
    private ProductAdapter adapter;
    private ListView productResultView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        products = new ArrayList<>(getArguments().getParcelableArrayList("meatList"));
    }

    private void populateList() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fish_meat, container, false);
        productResultView = view.findViewById(R.id.productListTabbed);

        adapter = new ProductAdapter(this.getActivity(), products);
        productResultView.setAdapter(adapter);
        productResultView.setClickable(true);
        return view;

    }

}
