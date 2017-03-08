package com.example.rafae.promoz_001_alfa.model;

import android.content.Context;

import com.example.rafae.promoz_001_alfa.R;
import com.example.rafae.promoz_001_alfa.util.ImgHttpResponseHandler;
import com.example.rafae.promoz_001_alfa.util.Singleton;
import com.loopj.android.http.AsyncHttpClient;

import java.io.Serializable;

/**
 * Created by vallux on 07/03/17.
 */

public class TempAdvertising implements Serializable, ImgHttpResponseHandler.onFinishResponseImg {
    private Integer _id;
    private byte[] image;
    private String imageURL;
    private Integer qtdCoin;
    private double lat;
    private double lng;
    private ImgHttpResponseHandler responseHandler;
    private AsyncHttpClient client;
    private Context context;


    public TempAdvertising (Integer id, String imageURL, Integer qtdCoin, double lat, double lng) {
        this._id = id;
        this.imageURL = imageURL;
        this.qtdCoin = qtdCoin;
        this.lat = lat;
        this.lng = lng;
    }

    @Override
    public void finishedImg(byte[] img) {
        this.image = img;
    }

    public  void setImage() {
        this.responseHandler = new ImgHttpResponseHandler();
        client = new AsyncHttpClient();
        this.responseHandler.setCallback(this);

        String URL = this.context.getResources().getString(R.string.protocol) + Singleton.getServerIp(this.context.getResources().getString(R.string.server_ip), this.context.getResources().getString(R.string.pref_default_ip_address), this.context) + this.context.getResources().getString(R.string.image_directory)  + getImageURL();

        client.get(URL,this.responseHandler);
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Integer get_id() {
        return _id;
    }

    public void set_id(Integer id) {
        this._id = id;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Integer getQtdCoin() {
        return qtdCoin;
    }

    public void setQtdCoin(Integer qtdCoin) {
        this.qtdCoin = qtdCoin;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
