package com.example.orella.smartcart.Model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Pair;

public final class Place implements Parcelable {

    private String address;
    private String name;
    private String placeId;
    private Pair<Double, Double> location;
    private String phoneNum;
    private double offer;
    private double distance;
    private int availability;


    public Place(Builder builder) {
        this.address = builder.address;
        this.name = builder.name;
        this.placeId = builder.id;
        this.location = builder.location;
        this.phoneNum = builder.phoneNum;
        this.offer = builder.offer;
        this.distance = builder.distance;
        this.availability = builder.availability;

    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public String getPlaceId() {
        return placeId;
    }

    public Pair<Double, Double> getLocation() {
        return location;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public double getOffer() {
        return offer;
    }

    public double getDistance() {
        return distance;
    }

    public int getAvailability() {
        return availability;
    }


    /*****************************************Parcelable Implementation*******************************************************/

    protected Place(Parcel in) {
        address = in.readString();
        name = in.readString();
        placeId = in.readString();
        phoneNum = in.readString();
        offer = in.readDouble();
        distance = in.readDouble();
        availability = in.readInt();
    }


    public static final Creator<Place> CREATOR = new Creator<Place>() {
        @Override
        public Place createFromParcel(Parcel in) {
            return new Place(in);
        }

        @Override
        public Place[] newArray(int size) {
            return new Place[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(address);
        dest.writeString(name);
        dest.writeString(placeId);
        dest.writeString(phoneNum);
        dest.writeDouble(offer);
        dest.writeDouble(distance);
        dest.writeInt(availability);
    }

    /*****************************************Builder Design*******************************************************/
    // Static class Builder
    public static class Builder {
        /// instance fields
        private String address;
        private String name;
        private String id;
        private Pair<Double, Double> location;
        private String phoneNum;
        private double offer;
        private double distance;
        private int availability;

        public static Builder newInstance() {
            return new Builder();
        }

        private Builder() {
        }

        // Setter methods
        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder address(String address) {
            this.address = address;
            return this;
        }

        public Builder location(Pair<Double, Double> location) {
            this.location = location;
            return this;
        }

        public Builder phoneNumber(String phoneNumber) {
            this.phoneNum = phoneNumber;
            return this;
        }

        public Builder placeOffer(double offer) {
            this.offer = offer;
            return this;
        }

        public Builder distance(double distance) {
            this.distance = distance;
            return this;
        }

        public Builder availability(int availability) {
            this.availability = availability;
            return this;
        }

        // build method to deal with outer class
        // to return outer instance
        public Place build() {
            return new Place(this);
        }

    }

}


