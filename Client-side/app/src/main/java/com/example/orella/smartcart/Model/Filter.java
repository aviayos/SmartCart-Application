package com.example.orella.smartcart.Model;

import com.example.orella.smartcart.Utils.APIUtils;

public enum Filter {
    Distance("Distance", APIUtils.DISTANCE),
    Price("Price",APIUtils.PRICE),
    Availability("Availability",APIUtils.AVAIL);

    private final String code;
    private final String name;

    Filter(final String name, final String code) {
        this.name = name;
        this.code = code;
    }

    public final String getCode(){
        return this.code;
    }
    public final String getName(){ return this.name; }

}
