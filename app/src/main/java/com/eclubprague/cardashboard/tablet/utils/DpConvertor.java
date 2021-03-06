package com.eclubprague.cardashboard.tablet.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * Convertor from dp to pixels
 *
 * Created by Michael on 14. 4. 2015.
 */
public class DpConvertor {

    public static int dpToPx(DisplayMetrics displayMetrics, int dp){
        float pixels = TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP, dp, displayMetrics );
        return Math.round(pixels);
    }
}
