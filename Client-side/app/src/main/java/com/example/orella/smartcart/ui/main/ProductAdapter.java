package com.example.orella.smartcart.ui.main;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.orella.smartcart.Common.interFace.ItemAddListener;
import com.example.orella.smartcart.Model.Product;
import com.example.orella.smartcart.R;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends BaseAdapter implements Filterable {

    Activity context;
    private ArrayList<Product> products;
    private ArrayList<Product> originalProds;
    private static LayoutInflater inflater = null;



    public ProductAdapter(Activity context, ArrayList<Product> products) {
        this.context = context;
        this.products = products;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }



    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int position) {
        return products.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View productView;
        ProductHolder holder;
        if (convertView == null) {
            productView = inflater.inflate(R.layout.products_list_item, null);
            holder = new ProductHolder();
            holder.quan = productView.findViewById(R.id.productQuantity);
            holder.name = productView.findViewById(R.id.productName);
            holder.category = productView.findViewById(R.id.productCategory);
            holder.quantity = holder.quan.getNumber();
            productView.setTag(R.string.set_id_1,holder);

        } else {
            productView = convertView;
            holder = (ProductHolder) convertView.getTag(R.string.set_id_1);
        }

        Button addButton = productView.findViewById(R.id.addProduct);
        ElegantNumberButton elegantNumberButton = productView.findViewById(R.id.productQuantity);
        addButton.setClickable(true);

        Product selected = products.get(position);
        holder.name.setText(selected.getName());
        holder.category.setText(selected.getCategory());
        productView.setTag(R.string.set_id_2, products.get(position));


//        elegantNumberButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
//            @Override
//            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
//
//            }
//        });

        addButton.setOnClickListener((view) -> {
            Product selectedProd = (Product) productView.getTag(R.string.set_id_2);
            ProductHolder holderIn = (ProductHolder) productView.getTag(R.string.set_id_1);
            String productQuan = holderIn.quan.getNumber();

            if (productQuan != null &&  !productQuan.trim().isEmpty()) {
                if (context instanceof ItemAddListener && Integer.valueOf(productQuan) > 0) {
                    selectedProd.setQuantity(Integer.valueOf(productQuan));
                    ((ItemAddListener) context).addProductToCart(selectedProd);
                    holderIn.quan.setNumber("0");
                }
            }
        });

        return productView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<Product> results = new ArrayList<>();
                if (originalProds == null)
                    originalProds = products;
                if (constraint != null) {
                    if (originalProds != null && originalProds.size() > 0) {
                        for (final Product prod : originalProds) {
                            if (prod.getName().toLowerCase()
                                    .contains(constraint.toString()))
                                results.add(prod);
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                products = (ArrayList<Product>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }


    class ProductHolder {
        ElegantNumberButton quan;
        TextView name;
        TextView category;
        String quantity;
    }


}
