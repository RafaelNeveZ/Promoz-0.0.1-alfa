package com.example.rafae.promoz_001_alfa.model;

/**
 * Created by vallux on 25/02/17.
 */

public class Advertising {
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
        return imageURL;
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

    public void setImage(byte[] image) {
        this.image = image;
    }

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
