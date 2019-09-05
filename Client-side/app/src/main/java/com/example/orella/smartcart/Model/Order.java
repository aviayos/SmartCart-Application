package com.example.orella.smartcart.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;

public class Order implements Parcelable {

    private ArrayList<Product> productsList;
    private int orderPrice;
    private Place orderedPlace;
    private Date datePurchased;//consider replacing with Calendar obj

    public Order(ArrayList<Product> productsList, Place orderedPlace, int price){
        this.productsList = new ArrayList<>(productsList);
        this.orderPrice = price;
        this.orderedPlace = orderedPlace;
        this.datePurchased = new Date();
    }

    protected Order(Parcel in) {
        productsList = in.createTypedArrayList(Product.CREATOR);
        orderPrice = in.readInt();
        orderedPlace = in.readParcelable(Place.class.getClassLoader());
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

    public ArrayList<Product> getProductsList() {
        return productsList;
    }

    public void setProductsList(ArrayList<Product> productsList) {
        this.productsList = productsList;
    }

    public int getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(int orderPrice) {
        this.orderPrice = orderPrice;
    }

    public Place getOrderedPlace() {
        return orderedPlace;
    }

    public void setOrderedPlace(Place orderedPlace) {
        this.orderedPlace = orderedPlace;
    }

    public Date getDatePurchased() {
        return datePurchased;
    }

    public void setDatePurchased(Date datePurchased) {
        this.datePurchased = datePurchased;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(productsList);
        dest.writeInt(orderPrice);
        dest.writeParcelable(orderedPlace, flags);
    }
}
