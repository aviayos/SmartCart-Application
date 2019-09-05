package com.example.orella.smartcart.Common;

import com.example.orella.smartcart.Model.Place;
import com.example.orella.smartcart.Model.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * Wrapper class that serves as a union of a result value and an exception. When the download
 * task has completed, either the result value or exception can be a non-null value.
 * This allows you to pass exceptions to the UI thread that were thrown during doInBackground().
 */
public class Result {
    public List<String> resultValue;
    public Exception exception;
    public Map<String, ArrayList<Product>> mapResult;
    public ArrayList<Place> placeArrayList;
    public int codeResult;


    public Result(List<String> results) {
        this.resultValue = results;
    }

    public Result(Exception exception) {
        this.resultValue = new ArrayList<>();
        this.resultValue.add("404");
        this.resultValue.add(exception.getMessage());
        this.exception = exception;
    }
    public Result(Map<String , ArrayList<Product>> result){
        this.mapResult = new HashMap<>();
        this.mapResult = result;
    }
    public Result(ArrayList<Place> result){
        this.placeArrayList = new ArrayList<>();
        this.placeArrayList = result;
    }




}
