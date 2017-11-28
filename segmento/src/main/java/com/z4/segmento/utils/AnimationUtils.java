package com.z4.segmento.utils;

import android.view.View;

/**
 * Created by z4.
 */

public abstract class AnimationUtils {

    public static void scale(View view, float scaleValue, long duration) {
        view.animate().scaleY(scaleValue)
                .scaleX(scaleValue)
                .setDuration(duration);
    }
}