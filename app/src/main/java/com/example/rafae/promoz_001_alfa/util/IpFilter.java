package com.example.rafae.promoz_001_alfa.util;

import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by vallux on 26/02/17.
 */

public class IpFilter implements InputFilter {
    private String exp;

    public IpFilter(String exp) {
        this.exp = exp;
    }


    @Override
    public CharSequence filter(CharSequence source, int start, int end,
                               android.text.Spanned dest, int dstart, int dend) {
        if (end > start) {
            String destTxt = dest.toString();
            String resultingTxt = destTxt.substring(0, dstart)
                    + source.subSequence(start, end)
                    + destTxt.substring(dend);

            if (!resultingTxt.matches(this.exp)) {
                return "";
            }
        }
        return null;
    }
}
