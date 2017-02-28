package com.example.rafae.promoz_001_alfa.util;

import android.content.Context;
import android.util.Log;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

/**
 * Created by vallux on 26/02/17.
 */

public class Singleton {

    private static Singleton instance;
    private static String serverIp = ""; // avaliar necessidade

    public static Singleton getInstance() {
        if (instance == null) {
            // Create the instance
            instance = new Singleton();
        }
        // Return the instance
        return instance;
    }

    public static String getServerIp(String strPref, String defaultIp, Context context) {

        serverIp = getDefaultSharedPreferences(context).getString(strPref,defaultIp);

        return serverIp;
    }

    /*Constructor hidden because this is a singleton*/
    private Singleton() { }
}
