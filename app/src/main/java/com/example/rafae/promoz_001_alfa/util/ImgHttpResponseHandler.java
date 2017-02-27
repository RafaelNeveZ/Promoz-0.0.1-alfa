package com.example.rafae.promoz_001_alfa.util;

import android.app.Activity;
import com.loopj.android.http.AsyncHttpResponseHandler;
import cz.msebera.android.httpclient.Header;

/**
 * Created by vallux on 25/02/17.
 */

public class ImgHttpResponseHandler extends AsyncHttpResponseHandler {

    onFinishResponseImg callback;

    public interface onFinishResponseImg {
        void finishedImg(byte[] img);
    }

    public void setCallback(Activity activity) {
        try {
            callback = (onFinishResponseImg) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement onFinishResponseImg");
        }
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        //Log.e("SUCCESS","VEIO IMG");
        callback.finishedImg(responseBody);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

    }
}