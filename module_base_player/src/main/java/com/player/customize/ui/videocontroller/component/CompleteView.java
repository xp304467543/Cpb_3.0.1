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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.player.customize.controller.ControlWrapper;
import com.player.customize.controller.IControlComponent;
import com.player.customize.player.VideoView;
import com.player.customize.util.PlayerUtils;
import com.videoplayer.R;

/**
 * 自动播放完成界面
 */
public class CompleteView extends FrameLayout implements IControlComponent {

    private ControlWrapper mControlWrapper;

    private ImageView mStopFullscreen;

    public CompleteView(@NonNull Context context) {
        super(context);
    }

    public CompleteView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CompleteView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        setVisibility(GONE);
        LayoutInflater.from(getContext()).inflate(R.layout.dkplayer_layout_complete_view, this, true);
        findViewById(R.id.iv_replay).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mControlWrapper.replay(true);
            }
        });
        mStopFullscreen = findViewById(R.id.stop_fullscreen);
        mStopFullscreen.setOnClickListener(new OnClickListener() {
            @SuppressLint("SourceLockedOrientationActivity")
            @Override
            public void onClick(View v) {
                if (mControlWrapper.isFullScreen()) {
                    Activity activity = PlayerUtils.scanForActivity(getContext());
                    if (activity != null && !activity.isFinishing()) {
                        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                        mControlWrapper.stopFullScreen();
                    }
                }
            }
        });
        setClickable(true);
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
        if (playState == VideoView.STATE_PLAYBACK_COMPLETED) {
            setVisibility(VISIBLE);
            mStopFullscreen.setVisibility(mControlWrapper.isFullScreen() ? VISIBLE : GONE);
            bringToFront();
        } else {
            setVisibility(GONE);
        }
    }

    @Override
    public void onPlayerStateChanged(int playerState) {
        if (playerState == VideoView.PLAYER_FULL_SCREEN) {
            mStopFullscreen.setVisibility(VISIBLE);
        } else if (playerState == VideoView.PLAYER_NORMAL) {
            mStopFullscreen.setVisibility(GONE);
        }

        Activity activity = PlayerUtils.scanForActivity(getContext());
        if (activity != null && mControlWrapper.hasCutout()) {
            int orientation = activity.getRequestedOrientation();
            int cutoutHeight = mControlWrapper.getCutoutHeight();
            LayoutParams sftp = (LayoutParams) mStopFullscreen.getLayoutParams();
            if (orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                sftp.setMargins(0, 0, 0, 0);
            } else if (orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                sftp.setMargins(cutoutHeight, 0, 0, 0);
            } else if (orientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE) {
                sftp.setMargins(0, 0, 0, 0);
            }
        }
    }

    @Override
    public void setProgress(int duration, int position) {

    }

    @Override
    public void onLockStateChanged(boolean isLock) {

    }
}
