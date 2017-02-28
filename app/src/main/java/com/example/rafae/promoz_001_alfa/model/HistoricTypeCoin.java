package com.example.rafae.promoz_001_alfa.model;

import java.io.Serializable;

/**
 * Created by vallux on 28/02/17.
 */

public class HistoricTypeCoin implements Serializable {

    private Integer _id;
    private String description;

    public Integer get_id() {
        return _id;
    }

    public void set_id(Integer _id) {
        this._id = _id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public HistoricTypeCoin(Integer _id, String description) {
        this._id = _id;
        this.description = description;
    }

    public HistoricTypeCoin() {
    }

    @Override
    public String toString() {
        return this.description;
    }
}
