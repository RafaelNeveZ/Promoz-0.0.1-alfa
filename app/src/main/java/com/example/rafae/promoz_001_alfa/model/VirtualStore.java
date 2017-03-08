package com.example.rafae.promoz_001_alfa.model;

import java.io.Serializable;

/**
 * Created by vallux on 26/01/17.
 */

public class VirtualStore implements Serializable {

    private Integer _id;
    private String title;
    private String information;
    private Integer img;
    //private byte img[];
    private Integer price;
    private Integer storeId;
    private Integer valid;

    public VirtualStore(Integer _id, String title, String information, Integer img, Integer price, Integer storeId, Integer valid) {
        this._id = _id;
        this.title = title;
        this.information = information;
        this.img = img;
        this.price = price;
        this.storeId = storeId;
        this.valid = valid;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public Integer get_id() {
        return _id;
    }

    public void set_id(Integer _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public Integer getImg() {
        return img;
    }

    public void setImg(Integer img) {
        this.img = img;
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

    @Override
    public String toString() {
        return this.title;
    }
}
