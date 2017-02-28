package com.example.rafae.promoz_001_alfa.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.util.Log;
import java.io.ByteArrayOutputStream;

/**
 * Created by vallux on 28/02/17.
 */

public class ImageUtil {

    private static byte percentage = 100;

    public static byte[] getThumbNail(Bitmap bitmap) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, percentage, stream);
        return stream.toByteArray();
    }

    public static byte[] getThumbNailDrawable(RoundedBitmapDrawable drawable){

        Bitmap bitmap = drawable.getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, percentage, stream);
        return stream.toByteArray();
    }

    public static Bitmap reSizeImageCrop(Bitmap bt, int base) {
        float largura = (float)bt.getWidth();
        float altura = (float) bt.getHeight();
        int offsetX = 0;
        int offsetY = 0;
        Bitmap b;
        if(altura > largura) {
            altura = base*(altura/largura);
            largura = base;
            b = Bitmap.createScaledBitmap(bt, Math.round(largura), Math.round(altura), false);
            offsetY = Math.round((altura - largura)/2);
        } else {
            largura = base*(largura/altura);
            altura = base;
            b = Bitmap.createScaledBitmap(bt, Math.round(largura), Math.round(altura), false);
            offsetX = Math.round((largura - altura)/2);
        }
        return Bitmap.createBitmap(b, offsetX, offsetY, base, base);
    }

    public static Bitmap reSizeImage(Bitmap bt){
        float largura = (float)bt.getWidth();
        float altura = (float) bt.getHeight();
        int base = 1000;

        if(largura < base && altura < base){
            return bt;
        } else if(altura > largura){
            float altProp = ((largura/altura)*base);
            return Bitmap.createScaledBitmap(bt, Math.round(altProp), base, false);

        } else if(largura > altura){
            float largProp = ((altura/largura)*base);
            return Bitmap.createScaledBitmap(bt, base, Math.round(largProp), false);

        } else {
            return Bitmap.createScaledBitmap(bt, base, base, false);
        }
    }

    public static byte[] drawableToByteArray(Context context, int resourceId){

        try {
            Drawable drawable = ContextCompat.getDrawable(context,resourceId);
            Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, percentage, stream);
            return stream.toByteArray();
        } catch (Exception e) {
            Log.d("ImageManager", "Error: " + e.toString());
        }
        return null;
    }
}
