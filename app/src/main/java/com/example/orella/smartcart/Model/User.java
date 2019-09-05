package com.example.orella.smartcart.Model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

public class User implements Parcelable {

    private String userName;
    private String password;
    private String userID;
    private ArrayList<Order> userOrders;


    public User(String userName, String password){
        this.userName = userName;
        this.password = password;
    }

    public User() {

    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    public ArrayList<Order> getUserOrders() {
        return userOrders;
    }

    public void setUserOrders(ArrayList<Order> userOrders) {
        this.userOrders = userOrders;
    }
    protected User(Parcel in) {
        userName = in.readString();
        password = in.readString();
        userID = in.readString();
        userOrders = new ArrayList<Order>();
        in.readList(userOrders, Order.class.getClassLoader());
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userName);
        dest.writeString(password);
        dest.writeString(userID);
        dest.writeList(userOrders);
    }


}
