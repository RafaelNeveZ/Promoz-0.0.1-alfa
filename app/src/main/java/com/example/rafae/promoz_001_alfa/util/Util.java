package com.example.rafae.promoz_001_alfa.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.rafae.promoz_001_alfa.model.User;

/**
 * Created by vallux on 28/02/17.
 */

public class Util extends AppCompatActivity {
    public static class Constants {

        public static String URI_GOOGLE = "https://www.google.com/intl/pt-BR/policies/terms/";
        public static String FONT_ITCKRIST = "fonts/ITCKRIST.TTF";
        public static int ERROR_LOGIN = -1;
        public static int ERROR_SENHA = 1;
        public static int ERROR_BD = -1;
    }

    public static void setFont(AssetManager assets, TextView textView, String font){

        Typeface customFont = Typeface.createFromAsset(assets, font);
        textView.setTypeface(customFont);
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public static void showProgress(final boolean show, Resources resources, final View mFormView, final View mProgressView) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime);

            mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public static void setSharedPreferencesRegion(Context context, String key, Integer value){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static void setSharedPreferences(Context context, Integer authUserId) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(User.getChave_ID(), authUserId);
        editor.commit();
    }
}
