package com.example.orella.smartcart.Model;
import android.widget.ImageView;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.orella.smartcart.R;

import java.util.HashMap;
import java.util.Map;

public class Product implements Parcelable {

    private String mName;
    private String mId;
    private String mCategory;
    private Map<String, String> convertMap;
    private int mQuantity;
    private ImageView mPic;
    private int image;
    private Map<String, Integer> test;


    public Product(String name, String id, String category) {
        test = new HashMap<>();
        test.put("Milk", R.drawable.milk);
        test.put("Apple", R.drawable.apple);
        test.put("Egg",R.drawable.egg);
        test.put("Banana", R.drawable.banana);
        test.put("Cherry",R.drawable.cherry);
        test.put("Chicken",R.drawable.chicken);
        test.put("Tuna", R.drawable.tuna);
        test.put("Pasta", R.drawable.pasta);
        test.put("Pineapple", R.drawable.pineapple);
        test.put("Beef",R.drawable.beef );
        test.put("Yogurt", R.drawable.yogurt);
        test.put("Persil", R.drawable.persil);
        test.put("Salmon", R.drawable.salmon);

        convertMap = new HashMap<>();
        convertMap.put("meat", "Meat & Fish");
        convertMap.put("dairy", "Dairy");
        convertMap.put("fruitsAndVegs", "Fruit & Veg.");
        convertMap.put("other", "Other");
        this.mName = name;
        this.mId = id;
        this.mCategory = convertMap.get(category);
        this.image = test.get(name);
    }


    public String getName() {
        return this.mName;
    }

    public String getId() {
        return this.mId;
    }

    public String getCategory() {
        return this.mCategory;
    }

    public void setQuantity(int quantity) {
        this.mQuantity = quantity;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(String code) {
        this.image = test.get(code);
    }

    public int getQuantity() {
        return mQuantity;
    }


    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };


    /***************************************Comparing Implementation******************************/

    @Override
    public boolean equals(Object v) {
        boolean retVal = false;

        if (v instanceof Product){
            Product ptr = (Product) v;
            if (this.mId.equals(ptr.getId()) && this.mName.equals(ptr.getName()))
                retVal = true;
        }

        return retVal;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + (this.mId != null ? this.mId.hashCode() : 0);
        return hash;
    }


    /***************************************Parcelable Implementation******************************/
    @Override
    public int describeContents() {
        return 0;
    }


    protected Product(Parcel in) {
        mName = in.readString();
        mId = in.readString();
        mCategory = in.readString();
        mQuantity = in.readInt();
        image = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeString(mId);
        dest.writeString(mCategory);
        dest.writeInt(mQuantity);
        dest.writeInt(image);

    }

}
