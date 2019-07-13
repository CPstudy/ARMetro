package com.guk2zzada.armetro;

import android.content.res.Resources;
import android.util.DisplayMetrics;

public class Units {
    public static int dp(int dp){
        final DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        int m = metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT;

        return dp * m;
    }

    public static float dp(float dp) {
        final DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        int m = metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT;

        return dp * m;
    }
}
