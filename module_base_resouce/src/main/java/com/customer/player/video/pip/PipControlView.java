package com.customer.player.video.pip;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.customer.player.video.PipManager;
import com.fh.module_base_resouce.R;
import com.player.customize.controller.ControlWrapper;
import com.player.customize.controller.IControlComponent;
import com.player.customize.player.VideoView;

/**
 * @ Author  QinTian
 * @ Date  2020/8/28
 * @ Describe
 */

public class PipControlView extends FrameLayout implements IControlComponent, View.OnClickListener {

    private ControlWrapper mControlWrapper;

//    private ImageView mPlay;
    public ImageView mClose,btn_rotate;
    private ProgressBar mLoading;

    public PipControlView(@NonNull Context context) {
        super(context);
    }

    public PipControlView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PipControlView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_float_controller, this, true);
//        mPlay = findViewById(R.id.start_play);
        mLoading = findViewById(R.id.loading);
        mClose = findViewById(R.id.btn_close);
        btn_rotate= findViewById(R.id.btn_rotate);
        mClose.setOnClickListener(this);
//        mPlay.setOnClickListener(this);
//        findViewById(R.id.btn_skip).setOnClickListener(this);
    }

    public ControlWrapper getmControlWrapper() {
        return mControlWrapper;
    }

    public void setmControlWrapper(ControlWrapper mControlWrapper) {
        this.mControlWrapper = mControlWrapper;
    }

//    public ImageView getmPlay() {
//        return mPlay;
//    }

//    public void setmPlay(ImageView mPlay) {
//        this.mPlay = mPlay;
//    }

    public ImageView getmClose() {
        return mClose;
    }

    public void setmClose(ImageView mClose) {
        this.mClose = mClose;
    }

    public ProgressBar getmLoading() {
        return mLoading;
    }

    public void setmLoading(ProgressBar mLoading) {
        this.mLoading = mLoading;
    }




    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_close) {
            PipManager.getInstance().stopFloatWindow();
            PipManager.getInstance().reset();
        } else if (id == R.id.start_play) {
            mControlWrapper.togglePlay();
        }
//        else if (id == R.id.btn_skip) {
//            startToLive();
//        }
    }

    private void startToLive(){
        if (PipManager.getInstance().getActClass() != null) {
            PipManager manager = PipManager.getInstance();
            Intent intent = new Intent(getContext(), PipManager.getInstance().getActClass());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("anchorId",manager.getAnchorId());
            intent.putExtra("lottery_id",manager.getLotteryId());
            intent.putExtra("nickName",manager.getNickName());
            intent.putExtra("live_status",manager.getLiveStatus());
            intent.putExtra("online",manager.getOnLine());
            intent.putExtra("r_id",manager.getrId());
            intent.putExtra("name",manager.getName());
            intent.putExtra("avatar",manager.getAvatar());
            getContext().startActivity(intent);
        }
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
//        if (isVisible) {
//            if (mPlay.getVisibility() == VISIBLE)
//                return;
//            mPlay.setVisibility(VISIBLE);
//            mPlay.startAnimation(anim);
//        } else {
//            if (mPlay.getVisibility() == GONE)
//                return;
//            mPlay.setVisibility(GONE);
//            mPlay.startAnimation(anim);
//        }
    }

    @Override
    public void onPlayStateChanged(int playState) {
        switch (playState) {
            case VideoView.STATE_IDLE:
//                mPlay.setSelected(false);
//                mPlay.setVisibility(VISIBLE);
                mLoading.setVisibility(GONE);
                break;
            case VideoView.STATE_PLAYING:
//                mPlay.setSelected(true);
//                mPlay.setVisibility(GONE);
                mLoading.setVisibility(GONE);
                break;
            case VideoView.STATE_PAUSED:
//                mPlay.setSelected(false);
//                mPlay.setVisibility(VISIBLE);
                mLoading.setVisibility(GONE);
                break;
            case VideoView.STATE_PREPARING:
//                mPlay.setVisibility(GONE);
                mLoading.setVisibility(VISIBLE);
                break;
            case VideoView.STATE_PREPARED:
//                mPlay.setVisibility(GONE);
                mLoading.setVisibility(GONE);
                break;
            case VideoView.STATE_ERROR:
                mLoading.setVisibility(GONE);
//                mPlay.setVisibility(GONE);
                bringToFront();
                break;
            case VideoView.STATE_BUFFERING:
//                mPlay.setVisibility(GONE);
                mLoading.setVisibility(VISIBLE);
                break;
            case VideoView.STATE_BUFFERED:
//                mPlay.setVisibility(GONE);
                mLoading.setVisibility(GONE);
//                mPlay.setSelected(mControlWrapper.isPlaying());
                break;
            case VideoView.STATE_PLAYBACK_COMPLETED:
                bringToFront();
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
