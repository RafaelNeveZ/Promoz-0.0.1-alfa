package com.example.rafae.promoz_001_alfa.util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.example.rafae.promoz_001_alfa.model.Advertising;
import com.loopj.android.http.AsyncHttpResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import cz.msebera.android.httpclient.Header;

/**
 * Created by vallux on 25/02/17.
 */

public class HttpResponseHandler extends AsyncHttpResponseHandler {
    onFinishResponse callback;

    public interface onFinishResponse {
        void finished();
    }

    public HttpResponseHandler(Context context) {
        this.context = context;
    }

    public void setCallback(Activity activity){
        try {
            callback = (onFinishResponse) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement onFinishResponse");
        }
    }

    private List<Advertising> advertisings = new ArrayList<Advertising>();
    private String response;
    private String nameObj;
    private Context context;

    private void setAdvertisings() {

        this.advertisings.clear();
        Advertising advertising = new Advertising(this.context);
        try {
            JSONObject jsonObject = new JSONObject(this.response);

            JSONArray jsonArray = jsonObject.getJSONArray(this.nameObj);

            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                advertising.setId(obj.getInt("_id"));
                advertising.setIdStore(obj.getInt("id_store"));
                advertising.setQtdCoin(obj.getInt("qtdCoin"));
                advertising.setImageURL(obj.getString("imageURL"));
                advertising.setTitle(obj.getString("title"));
                advertising.setSubTitle(obj.getString("subtitle"));
                advertising.setInfo(obj.getString("info"));
                advertising.setLat(obj.getDouble("lat"));
                advertising.setLng(obj.getDouble("long"));
                advertising.setAlt(obj.getDouble("alt"));
                this.advertisings.add(advertising);
            }
            callback.finished();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public List<Advertising> getAdvertisings() {
        return advertisings;
    }

    public void clearAdvertisings(){
        advertisings.clear();
    }

    @Override
    public void onStart() {
        // called before request is started
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] response) {
        // called when response HTTP status is "200 OK"
        try {
            this.response = new String(response, "ISO-8859-1");
            this.nameObj = setArrayName(this.response);
            //  Log.e("RESPONSE", this.response+"\n\n");
            setAdvertisings();
        } catch (UnsupportedEncodingException e) {
            //  Log.e("ERRO", "onSuccess...\n\n");
            e.printStackTrace();
        }
    }

    private String setArrayName(String response) {

        String regex = "\\{\"(.*)\":\\[\\{.*";
        Pattern padrao = Pattern.compile(regex);
        Matcher matcher = padrao.matcher(response);

        if(matcher.matches()) return matcher.group(1);
        return "";
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
    }

    @Override
    public void onRetry(int retryNo) {
        // called when request is retried
    }
}
