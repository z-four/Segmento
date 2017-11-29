package com.z4.segmento.listeners;

import com.z4.segmento.SegmentedProgressBar;

/**
 * Created by z4.
 */

public interface OnProgressChangedListener {
    void onProgressChanged(SegmentedProgressBar progressBar, float progress);
    void onStartTracking(SegmentedProgressBar progressBar);
    void onStopTracking(SegmentedProgressBar progressBar);
}