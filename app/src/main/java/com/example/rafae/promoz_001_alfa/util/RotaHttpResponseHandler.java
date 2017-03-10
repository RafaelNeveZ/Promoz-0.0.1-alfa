package com.example.rafae.promoz_001_alfa.util;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by vallux on 09/03/17.
 */

public class RotaHttpResponseHandler extends AsyncHttpResponseHandler {

    private String response;
 //   private PolylineOptions polylines;
    private onRotaFinishResponse callback;

    public interface onRotaFinishResponse {
        void rotaFinished(PolylineOptions polylines);
    }

    public void setCallback(Activity activity){
        try {
            callback = (onRotaFinishResponse) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement onFinishResponse");
        }
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        try {
            this.response = new String(responseBody, "ISO-8859-1");
            Log.e("JSON", this.response);
            setRoute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    protected void setPolylines(List<List<HashMap<String, String>>> result) {

        ArrayList<LatLng> points = null;
        PolylineOptions lineOptions = null;

        // Traversing through all the routes
        for(int i=0;i<result.size();i++){
            points = new ArrayList<LatLng>();
            lineOptions = new PolylineOptions();

            // Fetching i-th route
            List<HashMap<String, String>> path = result.get(i);

            // Fetching all the points in i-th route
            for(int j=0;j<path.size();j++){
                HashMap<String,String> point = path.get(j);

                double lat = Double.parseDouble(point.get("lat"));
                double lng = Double.parseDouble(point.get("lng"));
                LatLng position = new LatLng(lat, lng);

                points.add(position);
            }

            // Adding all the points in the route to LineOptions
            lineOptions.addAll(points);
            lineOptions.width(2);
            lineOptions.color(Color.argb(85, 250, 70,70));
        }
//        this.polylines = lineOptions;
        callback.rotaFinished(lineOptions);
        // Drawing polyline in the Google Map for the i-th route
     //   map.addPolyline(lineOptions);
    }

    private void setRoute() {
        try {
            JSONObject jsonObject = new JSONObject(this.response);
            DirectionsJSONParser parser = new DirectionsJSONParser();
            List<List<HashMap<String, String>>> routes = null;
            routes = parser.parse(jsonObject);
            setPolylines(routes);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

    }
}
