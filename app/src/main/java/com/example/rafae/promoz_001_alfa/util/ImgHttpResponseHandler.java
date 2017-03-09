package com.example.rafae.promoz_001_alfa.util;

import com.loopj.android.http.AsyncHttpResponseHandler;
import cz.msebera.android.httpclient.Header;

/**
 * Created by vallux on 25/02/17.
 */

public class ImgHttpResponseHandler extends AsyncHttpResponseHandler {

    private onFinishResponseImg callback;
    private Integer addId;

    public interface onFinishResponseImg {
        void finishedImg(byte[] img, Integer id);
    }

    public void setCallback(Object obj) {
        try {
            callback = (onFinishResponseImg) obj;
        } catch (ClassCastException e) {
            throw new ClassCastException(obj.toString()
                    + " must implement onFinishResponseImg");
        }
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        //Log.e("SUCCESS","VEIO IMG");
        callback.finishedImg(responseBody, this.addId);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
    }
}