package com.z4.segmento.listeners;

import com.z4.segmento.SegmentedProgressBar;

/**
 * Created by z4.
 */

public interface OnSegmentCountChangedListener {
    void onSegmentCountChanged(SegmentedProgressBar progressBar, int segmentCount);
}