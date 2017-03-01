package com.example.rafae.promoz_001_alfa.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.rafae.promoz_001_alfa.R;
import com.example.rafae.promoz_001_alfa.interfaces.Coin;
import com.example.rafae.promoz_001_alfa.interfaces.Markers;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by vallux on 27/02/17.
 */

public class MessageDialogs {
    public static void msgInfo(Activity activity, String title, String msg, int iconId){
        //public static void msgInfo(Activity activity, String title, String msg){
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setTitle(title);
        alert.setMessage(msg);
        alert.setPositiveButton("OK", null);
        alert.setIcon(iconId);
        alert.show();
    }

    public static void msgErrorDB(Context context, String tag, String error, Exception ex){
        Toast.makeText(context, error, Toast.LENGTH_LONG).show();
    }

    public static void msgAddvertising(final Activity activity, int layoutId, byte[] image, int imageId, Integer timeMs, final Integer amountCoin, final Integer idCoin, final LatLng coordLoja) {

        final Dialog alert = new Dialog(activity);
        alert.setContentView(layoutId);
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TimerView timerView = (TimerView) alert.findViewById(R.id.timer);

        if(image != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
            RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(activity.getResources(), bitmap);
            drawable.setCircular(true);
            ImageView adImage = (ImageView) alert.findViewById(imageId);
            adImage.setImageDrawable(drawable);
        }

        final Button bt = (Button)alert.findViewById(R.id.bot);
        bt.setEnabled(false);
        bt.setVisibility(View.INVISIBLE);
        alert.show();

        final CountDownTimer countDownTimer = new CountDownTimer(timeMs, timeMs) {
            public void onTick(long millisUntilFinished) {}
            public void onFinish() {
                bt.setEnabled(true);
                bt.setVisibility( View.VISIBLE);
                Coin callback = (Coin) activity;
                callback.gainCoin(amountCoin, idCoin);
            }
        };
        timerView.start(timeMs/1000);
        countDownTimer.start();

        alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                countDownTimer.cancel();
                Markers callback = (Markers) activity;
                callback.resetMarker();
            }
        });

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO DESENHAR ROTA
                Markers callback = (Markers) activity;
                callback.moveCamera(coordLoja);
                alert.dismiss();
            }
        });
    }
}
