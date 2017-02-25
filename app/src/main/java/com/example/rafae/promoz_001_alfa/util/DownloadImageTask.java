package com.example.rafae.promoz_001_alfa.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.widget.ImageView;
import java.io.InputStream;
import android.content.res.Resources;

/**
 * Created by vallux on 25/02/17.
 */

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;

    public void setResource(Resources resource) {
        this.resource = resource;
    }

    Resources resource;

    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();

            mIcon11 = BitmapFactory.decodeStream(in);

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        RoundedBitmapDrawable dr =
                RoundedBitmapDrawableFactory.create(this.resource, result);
        dr.setCircular(true);
        //bmImage.setImageBitmap(result);
        bmImage.setImageDrawable(dr);
    }
}