package com.z4.segmento;

/**
 * Created by z4
 */

public class Segment {
    private float mStartAngle;
    private float mProgressAngle;
    private float mCurrProgress;

    float getCurrProgress() {
        return mCurrProgress;
    }

    void setCurrProgress(float currProgress) {
        mCurrProgress = currProgress;
    }

    float getStartAngle() {
        return mStartAngle;
    }

    void setStartAngle(float startAngle) {
        mStartAngle = startAngle;
    }

    float getProgressAngle() {
        return mProgressAngle;
    }

    void setProgressAngle(float progressAngle) {
        mProgressAngle = progressAngle;
    }
}