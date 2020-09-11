package com.player.customize.ui.videocontroller.component;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lib.basiclib.utils.LogUtils;
import com.player.customize.controller.ControlWrapper;
import com.player.customize.controller.IControlComponent;
import com.player.customize.player.VideoView;
import com.player.customize.player.VideoViewManager;
import com.player.customize.util.PlayerUtils;
import com.videoplayer.R;

/**
 * @ Author  QinTian
 * @ Date  2020/8/31
 * @ Describe
 */
public  class VodPrepareView extends FrameLayout implements IControlComponent {

    private ControlWrapper mControlWrapper;

    private ImageView mThumb;
    private ImageView mStartPlay;
    private ProgressBar mLoading;
    private FrameLayout mNetWarning;

    public VodPrepareView(@NonNull Context context) {
        super(context);
    }

    public VodPrepareView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public VodPrepareView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        LayoutInflater.from(getContext()).inflate(R.layout.dkplayer_layout_prepare_view, this, true);
        mThumb = findViewById(R.id.thumb);
        mStartPlay = findViewById(R.id.start_play);
        mLoading = findViewById(R.id.loading);
        mNetWarning = findViewById(R.id.net_warning_layout);
        findViewById(R.id.status_btn).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mNetWarning.setVisibility(GONE);
                VideoViewManager.instance().setPlayOnMobileNetwork(true);
                mControlWrapper.start();
            }
        });
        findViewById(R.id.imgExit).setOnClickListener(new OnClickListener() {
            @SuppressLint("SourceLockedOrientationActivity")
            @Override
            public void onClick(View v) {
                Activity activity = PlayerUtils.scanForActivity(getContext());
                if (activity != null && mControlWrapper.isFullScreen()) {
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    mControlWrapper.stopFullScreen();
                }else if (activity!=null)activity.finish();
            }
        });
    }

    /**
     * 设置点击此界面开始播放
     */
    public void setClickStart() {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mControlWrapper.start();
            }
        });
    }

    @Override
    public void attach(@NonNull ControlWrapper controlWrapper) {
        mControlWrapper = controlWrapper;
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void onVisibilityChanged(boolean isVisible, Animation anim) {

    }

    @Override
    public void onPlayStateChanged(int playState) {
        switch (playState) {
            case VideoView.STATE_PREPARING:
                bringToFront();
                setVisibility(VISIBLE);
                mStartPlay.setVisibility(View.GONE);
                mNetWarning.setVisibility(GONE);
                mLoading.setVisibility(View.VISIBLE);
                break;
            case VideoView.STATE_PLAYING:
            case VideoView.STATE_PAUSED:
            case VideoView.STATE_ERROR:
            case VideoView.STATE_BUFFERING:
            case VideoView.STATE_BUFFERED:
            case VideoView.STATE_PLAYBACK_COMPLETED:
                setVisibility(GONE);
                break;
            case VideoView.STATE_IDLE:
                setVisibility(VISIBLE);
                bringToFront();
                mLoading.setVisibility(View.GONE);
                mNetWarning.setVisibility(GONE);
                mStartPlay.setVisibility(View.VISIBLE);
                mThumb.setVisibility(View.VISIBLE);
                break;
            case VideoView.STATE_START_ABORT:
                setVisibility(VISIBLE);
                mNetWarning.setVisibility(VISIBLE);
                mNetWarning.bringToFront();
                break;
        }
    }

    @Override
    public void onPlayerStateChanged(int playerState) {

    }

    @Override
    public void setProgress(int duration, int position) {

    }

    @Override
    public void onLockStateChanged(boolean isLocked) {

    }
}

