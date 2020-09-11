package com.customer.player.video.controller;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.customer.ApiRouter;
import com.customer.data.UserInfoSp;
import com.fh.module_base_resouce.R;
import com.lib.basiclib.utils.LogUtils;
import com.lib.basiclib.utils.ViewUtils;
import com.player.customize.controller.ControlWrapper;
import com.player.customize.controller.IControlComponent;
import com.player.customize.util.PlayerUtils;
import com.xiaojinzi.component.impl.Router;

/**
 * @ Author  QinTian
 * @ Date  2020/8/31
 * @ Describe 试看view
 */
public class VodTryView extends LinearLayout implements IControlComponent {


    private ControlWrapper mControlWrapper;

    public VodTryView(Context context) {
        this(context, null);
    }

    public VodTryView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public VodTryView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        setVisibility(GONE);
        LayoutInflater.from(getContext()).inflate(R.layout.vod_try_view, this, true);
        findViewById(R.id.imgExit).setOnClickListener(new OnClickListener() {
            @SuppressLint("SourceLockedOrientationActivity")
            @Override
            public void onClick(View v) {
                Activity activity = PlayerUtils.scanForActivity(getContext());
                if (activity != null && mControlWrapper.isFullScreen()) {
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    mControlWrapper.stopFullScreen();
                } else if (activity != null) activity.finish();
            }
        });
        findViewById(R.id.btLogin).setOnClickListener(v -> {
            Router.withApi(ApiRouter.class).toLogin(1);

        });
        findViewById(R.id.btRegister).setOnClickListener(v -> {
            Router.withApi(ApiRouter.class).toLogin(2);

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

    }

    @Override
    public void onPlayerStateChanged(int playerState) {

    }

    @Override
    public void setProgress(int duration, int position) {
        if (UserInfoSp.INSTANCE.getIsLogin()){ setVisibility(GONE);return; }
        if (position > 60000) {
            ViewUtils.INSTANCE.setVisible(this);
            mControlWrapper.pause();
            this.bringToFront();
        }

    }

    @Override
    public void onLockStateChanged(boolean isLocked) {

    }
}
