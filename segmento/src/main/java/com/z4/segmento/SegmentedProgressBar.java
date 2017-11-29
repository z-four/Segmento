package com.z4.segmento;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import java.util.Stack;

import com.z4.segmento.listeners.OnProgressChangedListener;
import com.z4.segmento.listeners.OnSegmentCountChangedListener;

import static com.z4.segmento.utils.AnimationUtils.scale;
import static com.z4.segmento.utils.SegmentoUtils.getDefaultProgressPaint;

/**
 * Created by z4
 */

public class SegmentedProgressBar extends View implements View.OnTouchListener {
    private static final int DEFAULT_ANIMATION_DURATION = 5000;
    private static final int DEFAULT_SCALE_ANIMATION_DURATION = 300;

    private static final float DEFAULT_MIN_SCALE_FACTOR = 1.0f;
    private static final float DEFAULT_SCALE_FACTOR = 1.20f;

    private static final float DEFAULT_PROGRESS_WIDTH = 10f;
    private static final float DEFAULT_SEGMENT_SIZE = 5f;

    private static final int PROGRESS_MAX_ANGLE = 360;

    private static final String PROGRESS_ANIM_PROPERTY = "progress";

    private RectF mRectF = new RectF();
    private Paint mOuterCircle;
    private Paint mSegmentCircle;
    private Paint mInnerCircle;

    private Stack<Segment> mSegmentStack;
    private ObjectAnimator mProgressAnimator;

    private OnProgressChangedListener mOnProgressChangedListener;
    private OnSegmentCountChangedListener mOnSegmentCountChangedListener;

    private float mScaleFactor;
    private long mScaleAnimDuration;

    private int mWidth, mHeight;
    private float mCurrProgress, mMaxProgress;

    private float mInnerProgressWidth, mOuterProgressWidth, mShadowWidth, mSegmentSize;
    private int mInnerProgressColor, mOuterProgressColor, mSegmentColor;
    private int mOuterProgressColors[];
    private float mOuterProgressColorsPositions[];

    private int mProgressAngle, mStartAngle = -90;

    private boolean mClockWise;

    public SegmentedProgressBar(Context context) {
        super(context);
        init();
    }

    public SegmentedProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SegmentedProgressBar(Context context, AttributeSet attrs, int i) {
        super(context, attrs, i);

        parseAttrs(attrs);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawOval(mRectF, mInnerCircle);
        canvas.drawArc(mRectF, mStartAngle, mProgressAngle, false, mOuterCircle);

        if (mSegmentStack != null && mSegmentStack.size() > 0) {
            for (Segment segment : mSegmentStack) {
                canvas.drawArc(mRectF, segment.getStartAngle(), segment.getProgressAngle(),
                        false, mSegmentCircle);
            }
        }
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        int min = Math.min(width, height);

        if (height != mHeight || width != mWidth) {
            mHeight = height > 0 ? height : mHeight;
            mWidth = width > 0 ? width : mWidth;

            upgradeInnerProgressColor();
        }

        setMeasuredDimension(min, min);
        setRadiusRect(min);
    }

    private void parseAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs,
                R.styleable.SegmentedProgressBar, 0, 0);

        try {
            mClockWise = typedArray.getBoolean(R.styleable.SegmentedProgressBar_clock_wise,
                    false);
            mInnerProgressColor = typedArray.getColor(
                    R.styleable.SegmentedProgressBar_inner_progress_color, Color.WHITE);
            mOuterProgressColor = typedArray.getColor(
                    R.styleable.SegmentedProgressBar_outer_progress_color, Color.GREEN);
            mSegmentColor = typedArray.getColor(
                    R.styleable.SegmentedProgressBar_segment_color, Color.WHITE);
            mInnerProgressWidth = typedArray.getDimension(
                    R.styleable.SegmentedProgressBar_inner_progress_width, DEFAULT_PROGRESS_WIDTH);
            mOuterProgressWidth = typedArray.getDimension(
                    R.styleable.SegmentedProgressBar_outer_progress_width, DEFAULT_PROGRESS_WIDTH);
            mShadowWidth = typedArray.getDimension(
                    R.styleable.SegmentedProgressBar_shadow_width, 0);
            mSegmentSize = typedArray.getDimension(
                    R.styleable.SegmentedProgressBar_segment_size, DEFAULT_SEGMENT_SIZE);

            mCurrProgress = typedArray.getFloat(R.styleable.SegmentedProgressBar_progress, 0);
            mMaxProgress = typedArray.getFloat(R.styleable.SegmentedProgressBar_max_progress,
                    DEFAULT_ANIMATION_DURATION);

            mScaleFactor = typedArray.getFloat(R.styleable.SegmentedProgressBar_scaleFactor,
                    DEFAULT_SCALE_FACTOR);
            mScaleAnimDuration = typedArray.getInt(R.styleable.SegmentedProgressBar_scaleAnimDuration,
                    DEFAULT_SCALE_ANIMATION_DURATION);

        } finally { typedArray.recycle(); }
    }

    private void init() {
        mSegmentCircle = getDefaultProgressPaint(mOuterProgressWidth, mSegmentColor);
        mOuterCircle = getDefaultProgressPaint(mOuterProgressWidth, mOuterProgressColor);
        mInnerCircle = getDefaultProgressPaint(mInnerProgressWidth, mInnerProgressColor);

        setLayerType(LAYER_TYPE_SOFTWARE, mInnerCircle);

        if (mShadowWidth > 0) updateShadow();

        setInnerProgressWidth(mInnerProgressWidth);
        setOuterProgressWidth(mOuterProgressWidth);
        setSegmentSize(mSegmentSize);

        setInnerProgressColor(mInnerProgressColor);
        setOuterProgressColor(mOuterProgressColor);
        setSegmentColor(mSegmentColor);

        if (mCurrProgress > 0) setProgress(mCurrProgress);
        if (mClockWise) setClockwise(mClockWise);

        setOnTouchListener(this);
    }

    private void addSegment() {
        if (mCurrProgress >= mMaxProgress) return;
        if (mSegmentStack == null) mSegmentStack = new Stack<>();

        Segment segment = new Segment();
        segment.setStartAngle(mStartAngle + mProgressAngle);
        segment.setProgressAngle(mSegmentSize);
        segment.setCurrProgress(mCurrProgress);
        mSegmentStack.add(segment);

        invalidate();
    }

    public int getSegmentsCount() {
        return mSegmentStack != null ? mSegmentStack.size() : 0;
    }

    public void removeAllSegments() {
        if (mSegmentStack != null) mSegmentStack.removeAllElements();
        notifySegmentCountChanged(getSegmentsCount());
    }

    public Stack<Segment> getSegments() {
        return mSegmentStack;
    }

    public void removeLastSegment() {
        if (mSegmentStack != null) {
            int lastSegmentCount = getSegmentsCount();
            Segment segment = !mSegmentStack.empty() ? mSegmentStack.pop() : null;

            if (mCurrProgress > 0) notifySegmentCountChanged(lastSegmentCount);

            if (segment != null) setProgress(segment.getCurrProgress());
            else if (mSegmentStack.size() == 0 && mCurrProgress != 0) setProgress(0);

        } else if (mCurrProgress != 0) setProgress(0);
    }

    private void setRadiusRect(int min) {
        float highStroke = Math.max(mInnerProgressWidth, mOuterProgressWidth);
        float properStrokeHigh = highStroke / 2 + mShadowWidth / 2;

        mRectF.set(properStrokeHigh, properStrokeHigh, min - properStrokeHigh,
                min - properStrokeHigh);
    }

    private void checkIfClockWiseProgress() {
        if (mClockWise) mProgressAngle = mProgressAngle > 0 ? -mProgressAngle : mProgressAngle;
    }

    //-- Segments listener methods

    public void setOnSegmentCountChangeListener(OnSegmentCountChangedListener onSegmentCountChangeListener) {
        mOnSegmentCountChangedListener = onSegmentCountChangeListener;
    }

    private void notifySegmentCountChanged(int count) {
        if (mOnSegmentCountChangedListener != null) {
            mOnSegmentCountChangedListener.onSegmentCountChanged(this, count);
        }
    }

    //-- Progress listener methods

    public void setOnProgressbarChangeListener(OnProgressChangedListener onProgressbarChangeListener) {
        mOnProgressChangedListener = onProgressbarChangeListener;
    }

    private void notifyProgressChanged(float progress) {
        if (mOnProgressChangedListener != null) {
            mOnProgressChangedListener.onProgressChanged(this, progress);
        }
    }

    private void notifyStartTracking() {
        if (mOnProgressChangedListener != null) {
            mOnProgressChangedListener.onStartTracking(this);
        }
    }

    private void notifyStopTracking() {
        if (mOnProgressChangedListener != null) {
            mOnProgressChangedListener.onStopTracking(this);
        }
    }

    //-- Progress bar setup methods

    public void setClockwise(boolean clockwise) {
        mClockWise = clockwise;

        checkIfClockWiseProgress();
        invalidate();
    }

    public boolean isClockWise() {
        return mClockWise;
    }

    public float getProgress() {
        return mCurrProgress;
    }

    public void setProgress(float progress) {
        mCurrProgress = (progress <= mMaxProgress) ? progress : mMaxProgress;
        mProgressAngle = (int) (PROGRESS_MAX_ANGLE * progress / mMaxProgress);

        notifyProgressChanged(progress);
        checkIfClockWiseProgress();
        invalidate();
    }

    public void setMaxProgress(float mMaxProgress) {
        this.mMaxProgress = mMaxProgress;
    }

    public float getMaxProgress() {
        return this.mMaxProgress;
    }

    public void setProgressWithAnimation(float progress, int duration) {
        setProgressWithAnimation(progress, 0, duration);
    }

    public void setProgressWithAnimation(float progress, int delay, int duration) {
        stopProgressAnimation();

        if (mCurrProgress >= mMaxProgress) return;

        mProgressAnimator = ObjectAnimator.ofFloat(this, PROGRESS_ANIM_PROPERTY,
                progress);
        mProgressAnimator.setStartDelay(delay);
        mProgressAnimator.setDuration(duration);
        mProgressAnimator.setInterpolator(new LinearInterpolator());
        mProgressAnimator.start();
    }

    public void stopProgressAnimation() {
        if (mProgressAnimator != null) mProgressAnimator.cancel();
    }

    //-- Progress bar style methods

    public void setInnerProgressWidth(float width) {
        mInnerProgressWidth = width;
        mInnerCircle.setStrokeWidth(mInnerProgressWidth);

        requestLayout();
        invalidate();
    }

    public void setOuterProgressWidth(float width) {
        mOuterProgressWidth = width;
        mOuterCircle.setStrokeWidth(mOuterProgressWidth);
        mSegmentCircle.setStrokeWidth(mOuterProgressWidth);

        requestLayout();
        invalidate();
    }

    public void setScaleFactor(float scaleFactor) {
        mScaleFactor = scaleFactor;
    }

    public void setShadowWidth(int width) {
        mShadowWidth = width;

        updateShadow();
        requestLayout();
        invalidate();
    }

    private void updateShadow() {
        mInnerCircle.setShadowLayer(mShadowWidth / 2, 0, 0, Color.BLACK);
    }

    public void setSegmentSize(float size) {
        mSegmentSize = size;
    }

    public void setSegmentColor(int color) {
        mSegmentColor = color;
        mSegmentCircle.setColor(color);
    }

    public void setInnerProgressColor(int color) {
        mInnerProgressColor = color;
        mInnerCircle.setColor(color);

        invalidate();
    }

    public void setOuterProgressColor(int color) {
        mOuterProgressColorsPositions = null;
        mOuterProgressColors = null;
        mOuterProgressColor = color;
        mOuterCircle.setColor(color);
        mOuterCircle.setShader(null);

        invalidate();
    }

    public void setOuterProgressColor(int[] colors, float[] positions) {
        mOuterProgressColors = colors;
        mOuterProgressColorsPositions = positions;

        upgradeInnerProgressColor();
    }

    public void setOuterProgressColor(int[] colors) {
        setOuterProgressColor(colors, null);
    }

    public void upgradeInnerProgressColor() {
        if (mOuterProgressColors == null) return;

        int halfWidth = mWidth / 2;
        int halfHeight = mWidth / 2;

        mOuterCircle.setShader(new SweepGradient(halfWidth, halfHeight, mOuterProgressColors,
                mOuterProgressColorsPositions));
        Matrix matrix = new Matrix();
        mOuterCircle.getShader().getLocalMatrix(matrix);

        matrix.postTranslate(-halfWidth, -halfHeight);
        matrix.postRotate(mStartAngle);
        matrix.postTranslate(halfWidth, halfHeight);

        mOuterCircle.getShader().setLocalMatrix(matrix);
    }

    //-- OnTouchListener

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mCurrProgress > 0) addSegment();
                scale(this, mScaleFactor, mScaleAnimDuration);
                notifyStartTracking();
                setProgressWithAnimation(mMaxProgress, (int) mScaleAnimDuration,
                        (int) (mMaxProgress - mCurrProgress));
                break;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                stopProgressAnimation();
                scale(this, DEFAULT_MIN_SCALE_FACTOR, mScaleAnimDuration);
                notifyStopTracking();
                notifySegmentCountChanged(getSegmentsCount() + 1);
                break;
        }
        return true;
    }
}