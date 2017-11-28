package com.z4.segmentosample;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

import com.z4.segmento.SegmentedProgressBar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        SegmentedProgressBar.OnProgressbarChangeListener, SeekBar.OnSeekBarChangeListener {

    private final static int MAX_PROGRESS_PERCENT = 100;
    private final static int MIN_PROGRESS_PERCENT = 20;
    private final static int MAX_DURATION = 10000;
    private final static int MIN_DURATION = 3000;

    private final static int MIN_PROGRESS_WIDTH = 15;

    private int mSeekBarTag = 0;
    private int mProperProgressColors[];
    private int mMinProgressColors[];
    private final float[] mMinColorPositions = {0f, 0.2f, 1.0f};

    private SegmentedProgressBar mSegmentedProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
    }

    private void initUI() {
        int shadowWidth = (int) getResources()
                .getDimension(R.dimen.segmented_progress_bar_shadow_width) * 2;
        int maxProgress = (int) getResources()
                .getDimension(R.dimen.segmented_progress_bar_outer_circle_width) * 2;
        int halfProgress = maxProgress / 2;

        findViewById(R.id.remove_segment_button).setOnClickListener(this);

        mSegmentedProgressBar = findViewById(R.id.segmented_progress_bar);
        mSegmentedProgressBar.setOnProgressbarChangeListener(this);

        setupSeekBar(R.id.outer_progress_seek_bar, maxProgress, halfProgress);
        setupSeekBar(R.id.inner_progress_seek_bar, maxProgress, halfProgress);
        setupSeekBar(R.id.progress_duration_seek_bar, MAX_DURATION, MAX_DURATION);
        setupSeekBar(R.id.shadow_width_seek_bar, shadowWidth, shadowWidth / 2);
    }

    private void setupSeekBar(int viewId, int maxProgress, int progress) {
        SeekBar seekBar = findViewById(viewId);
        seekBar.setMax(maxProgress);
        seekBar.setProgress(progress);
        seekBar.setTag(++mSeekBarTag);
        seekBar.setOnSeekBarChangeListener(this);
    }

    private int[] getProperProgressColors() {
        if (mProperProgressColors == null) {
            mProperProgressColors = new int[5];

            mProperProgressColors[0] = ContextCompat.getColor(this,
                    R.color.outerProgressGreenColor);
            mProperProgressColors[1] = ContextCompat.getColor(this,
                    R.color.outerProgressGreenColor);
            mProperProgressColors[2] = ContextCompat.getColor(this,
                    R.color.outerProgressBlueColor);
            mProperProgressColors[3] = ContextCompat.getColor(this,
                    R.color.outerProgressBlueColor);
            mProperProgressColors[4] = ContextCompat.getColor(this,
                    R.color.outerProgressGreenColor);
        }
        return mProperProgressColors;
    }

    private int[] getMinProgressColors() {
        if (mMinProgressColors == null) {
            mMinProgressColors = new int[3];

            mMinProgressColors[0] = ContextCompat.getColor(this,
                    R.color.outerProgressRedColor);
            mMinProgressColors[1] = ContextCompat.getColor(this,
                    R.color.outerProgressOrangeColor);
            mMinProgressColors[2] = ContextCompat.getColor(this,
                    R.color.outerProgressRedColor);
        }
        return mMinProgressColors;
    }

    //-- OnClick Listener

    @Override
    public void onClick(View v) {
        mSegmentedProgressBar.removeLastSegment();
    }

    //Segmented progress bar listener

    @Override
    public void onProgressChanged(SegmentedProgressBar progressBar, float progress) {
        int currProgressPercent = (int) (progress * MAX_PROGRESS_PERCENT
                / progressBar.getMaxProgress());

        if (currProgressPercent >= MIN_PROGRESS_PERCENT) {
            mSegmentedProgressBar.setOuterProgressColor(getProperProgressColors());
        } else if (progress < MIN_PROGRESS_PERCENT) {
           mSegmentedProgressBar.setOuterProgressColor(getMinProgressColors(), mMinColorPositions);
        }
    }

    @Override
    public void onStartTracking(SegmentedProgressBar progressBar) {}

    @Override
    public void onStopTracking(SegmentedProgressBar progressBar) {}

    //Seek bar listener

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch ((int) seekBar.getTag()) {
            case 1:
                if (progress >= MIN_PROGRESS_WIDTH) {
                    mSegmentedProgressBar.setOuterProgressWidth(progress);
                }
                break;
            case 2:
                if (progress >= MIN_PROGRESS_WIDTH) {
                    mSegmentedProgressBar.setInnerProgressWidth(progress);
                }
                break;
            case 3:
                if (progress >= MIN_DURATION) {
                    mSegmentedProgressBar.removeAllSegments();
                    mSegmentedProgressBar.setProgress(0);
                    mSegmentedProgressBar.setMaxProgress(progress);
                }
                break;
            case 4:
                mSegmentedProgressBar.setShadowWidth(progress);
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {}
}