package com.example.orella.smartcart.Utils;



import android.util.Pair;

import com.example.orella.smartcart.Model.Place;
import com.example.orella.smartcart.Model.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ConnectionUtils {
    private static final ConnectionUtils ourInstance = new ConnectionUtils();
    public static ConnectionUtils getInstance() {
        return ourInstance;
    }
    private ConnectionUtils() {
    }
    private static String UTF8_ENCODING = "UTF-8";
    private static String URL_HOST = "http://10.0.2.2:9000";


    public ArrayList<String> getUserErrors(InputStream stream) throws IOException, JSONException {
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
        }
        br.close();
        JSONArray obj = new JSONArray(sb.toString());
        ArrayList<String> res = new ArrayList<>();
        res.add(APIUtils.ERROR_400);
        for (int i = 0; i < obj.length(); i++) {
            String error = obj.getJSONObject(i).get(APIUtils.PARAM).toString() + APIUtils.SEPERATOR + obj.getJSONObject(i).get(APIUtils.MSG).toString();
            res.add(error);
        }
        return res;
    }


    public String readStream(InputStream stream, int maxReadSize)
            throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, UTF8_ENCODING);
        char[] rawBuffer = new char[maxReadSize];
        int readSize;
        StringBuffer buffer = new StringBuffer();
        while (((readSize = reader.read(rawBuffer)) != -1) && maxReadSize > 0) {
            if (readSize > maxReadSize) {
                readSize = maxReadSize;
            }
            buffer.append(rawBuffer, 0, readSize);
            maxReadSize -= readSize;
        }
        return buffer.toString();
    }

    public Map<String, String> readStreamWithKeys(InputStream stream) throws IOException, JSONException {
        String line;
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        Map <String , String> mappedKeys = new HashMap<>();
        JSONObject jsonObject = new JSONObject(sb.toString());
        Iterator<String> keys = jsonObject.keys();
        while(keys.hasNext()) {
            String key = keys.next();
            mappedKeys.put(key, jsonObject.get(key).toString());
        }
        return mappedKeys;
    }


    public void setConnectionBody(HttpURLConnection connection, Map<String, String> bodyArgs) throws IOException, JSONException {
        JSONObject json = new JSONObject();
        for (Map.Entry<String,String> entry : bodyArgs.entrySet()){
            json.put(entry.getKey(), entry.getValue());
        }
        OutputStream os = connection.getOutputStream();
        os.write(json.toString().getBytes(UTF8_ENCODING));
        os.close();
    }

    public void setConnectionBodyJSON(HttpURLConnection connection, Map<String, Object> bodyArgs) throws IOException, JSONException {
        JSONObject json = new JSONObject();
        for (Map.Entry<String,Object> entry : bodyArgs.entrySet()){
            json.put(entry.getKey(), entry.getValue());
        }
        OutputStream os = connection.getOutputStream();
        os.write(json.toString().getBytes(UTF8_ENCODING));
        os.close();
    }


    public Map<String, ArrayList<Product>> getListOfProducts(InputStream stream) throws IOException, JSONException {
        String line;
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        JSONObject jsonObject = new JSONObject(sb.toString());
        Map<String, ArrayList<Product>> mappedKeys = new HashMap<>();
        String meatKey = APIUtils.MEAT;
        String fruitAndVegsKey = APIUtils.FRUIT_AND_VEGS;
        String otherKey = APIUtils.OTHER;
        String dairyKey = APIUtils.DAIRY;
        mappedKeys.put(meatKey, getListByCategory(meatKey, jsonObject.getJSONArray(meatKey)));
        mappedKeys.put(dairyKey, getListByCategory(dairyKey, jsonObject.getJSONArray(dairyKey)));
        mappedKeys.put(fruitAndVegsKey, getListByCategory(fruitAndVegsKey, jsonObject.getJSONArray(fruitAndVegsKey)));
        mappedKeys.put(otherKey, getListByCategory(otherKey, jsonObject.getJSONArray(otherKey)));

        return mappedKeys;
    }

    private ArrayList<Product> getListByCategory(String category,  JSONArray jsonArray) throws JSONException {
        ArrayList<Product> products = new ArrayList<>();
        if (jsonArray != null) {
            for (int i=0 ; i<jsonArray.length() ; i++){
                JSONObject jsonInner  = jsonArray.getJSONObject(i);
                String name = jsonInner.get("product_name").toString();
                String id = jsonInner.get("productid").toString();
                Product product = new Product(name, id, category);
                products.add(product);
            }
        }
        return products;
    }

    public JSONObject getLocationAsJson(double latit, double longtit) throws JSONException {
        JSONObject json = new JSONObject();
        json.put(APIUtils.LAT, String.valueOf(latit));
        json.put(APIUtils.LNG, String.valueOf(longtit));
        return json;
    }

    public JSONArray getProducts(List<Product> products) throws JSONException {
        JSONArray jsonArr = new JSONArray();
        for (Product product: products){
            jsonArr.put(product.getName());
        }
        return jsonArr;
    }

    public String getUrlHost(){
        return URL_HOST;
    }

    public ArrayList<Place> getListOfPlaces(InputStream stream) throws IOException, JSONException {
        String line;
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        JSONArray jsonArray = new JSONArray(sb.toString());
        ArrayList<Place> places = new ArrayList<>();
        for(int i = 0 ; i < jsonArray.length() ; i++){
            JSONObject placeJson = jsonArray.getJSONObject(i);
            places.add(fetchPlace(placeJson));
        }
        br.close();
        return places;
    }

    private Place fetchPlace(JSONObject place) throws JSONException {
        String id = place.get(APIUtils.SUPER_ID).toString();
        String name = place.get(APIUtils.SUPER_NAME).toString();
        int avail = Integer.valueOf(place.get(APIUtils.AVAIL).toString());
        double offer = Double.valueOf(place.get(APIUtils.PRICE).toString());
        double distance = Double.valueOf(place.get(APIUtils.DISTANCE).toString());
        String phoneNum = place.get(APIUtils.PHONE_NUM).toString();
        String address = place.get(APIUtils.ADDRESS).toString();
        JSONObject loc = place.getJSONObject(APIUtils.LOCATION);
        double lat = (double) loc.get(APIUtils.LAT);
        double lng = (double) loc.get(APIUtils.LNG);
        Pair<Double, Double> location = new Pair<>(lat,lng);
        Place newPlace =
                Place.Builder.newInstance().id(id).name(name).availability(avail).placeOffer(offer)
                        .distance(distance).phoneNumber(phoneNum).address(address).location(location).build();
        return newPlace;

    }
}
