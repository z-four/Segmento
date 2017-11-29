package com.z4.segmento.utils;

import android.graphics.Paint;

/**
 * Created by z4
 */

public abstract class SegmentoUtils {

    public static Paint getDefaultProgressPaint(float width, int color) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(width);
        paint.setDither(true);
        paint.setFilterBitmap(true);
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);

        return paint;
    }
}