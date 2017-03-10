package com.example.rafae.promoz_001_alfa.model;

import android.content.Context;
import android.util.Log;

import com.example.rafae.promoz_001_alfa.R;
import com.example.rafae.promoz_001_alfa.util.ImgHttpResponseHandler;
import com.example.rafae.promoz_001_alfa.util.Singleton;
import com.loopj.android.http.AsyncHttpClient;

/**
 * Created by vallux on 25/02/17.
 */

public class Advertising implements ImgHttpResponseHandler.onFinishResponseImg {
    private Integer id;
    private Integer idStore;
    private byte[] image;
    private String imageURL;
    private Integer qtdCoin;
    private String title;
    private String subTitle;
    private String info;
    private Double lat;
    private Double lng;
    private Double alt;
    private Integer idArea;
    private ImgHttpResponseHandler responseHandler;
    private AsyncHttpClient client;
    private Context context;

    public Advertising(Context context) {
        this.context = context;
    }

    public  void setImage() {

        this.responseHandler = new ImgHttpResponseHandler();
        client = new AsyncHttpClient();
        this.responseHandler.setCallback(this);

        String URL = this.context.getResources().getString(R.string.protocol) + Singleton.getServerIp(this.context.getResources().getString(R.string.server_ip), this.context.getResources().getString(R.string.pref_default_ip_address), this.context) + this.context.getResources().getString(R.string.image_directory)  + getImageURL();

        client.get(URL,this.responseHandler);
    }

    @Override
    public void finishedImg(byte[] img, Integer id) {
        this.image = img;
    }

    public Integer getIdArea() {
        return idArea;
    }

    public void setIdArea(Integer idArea) {
        this.idArea = idArea;
    }

    public Double getAlt() {
        return alt;
    }

    public void setAlt(Double alt) {
        this.alt = alt;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getImageURL() {
        return this.imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdStore() {
        return idStore;
    }

    public void setIdStore(Integer idStore) {
        this.idStore = idStore;
    }

    public byte[] getImage() {
        return image;
    }

    /*public void setImage(byte[] image) {
        this.image = image;
    }*/

    public Integer getQtdCoin() {
        return qtdCoin;
    }

    public void setQtdCoin(Integer qtdCoin) {
        this.qtdCoin = qtdCoin;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
