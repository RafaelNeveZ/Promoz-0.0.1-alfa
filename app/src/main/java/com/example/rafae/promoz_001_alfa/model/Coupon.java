package com.example.rafae.promoz_001_alfa.model;

import java.io.Serializable;

/**
 * Created by vallux on 05/03/17.
 */

public class Coupon implements Serializable {

    private Integer _id;
    private Integer walletId;
    private String title;
    private String subTitle;
    //private byte[] img;
    private int img;
    private String info;
    private String dateUse;
    private String dateExp;
    private Integer price;
    private Integer valid;
    private Integer storeId;

    public Coupon(){
    }

    public Coupon(Integer _id, Integer walletId, String title, String subTitle, int img, String info, String dateUse, String dateExp, Integer price, Integer valid, Integer storeID) {
        this._id = _id;
        this.walletId = walletId;
        this.title = title;
        this.subTitle = subTitle;
        this.img = img;
        this.info = info;
        this.dateUse = dateUse;
        this.dateExp = dateExp;
        this.price = price;
        this.valid = valid;
        this.storeId = storeID;
    }

    public Integer get_id() {
        return _id;
    }

    public void set_id(Integer _id) {
        this._id = _id;
    }

    public Integer getWalletId() {
        return walletId;
    }

    public void setWalletId(Integer walletId) {
        this.walletId = walletId;
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

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getDateUse() {
        return dateUse;
    }

    public void setDateUse(String dateUse) {
        this.dateUse = dateUse;
    }

    public String getDateExp() {
        return dateExp;
    }

    public void setDateExp(String dateExp) {
        this.dateExp = dateExp;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getValid() {
        return valid;
    }

    public void setValid(Integer valid) {
        this.valid = valid;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    @Override
    public String toString() {
        return this.title;
    }
}
