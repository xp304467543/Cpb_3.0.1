package com.customer.player.video.controller;

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
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.customer.data.UserInfoSp;
import com.lib.basiclib.utils.ViewUtils;
import com.player.customize.controller.ControlWrapper;
import com.player.customize.controller.IControlComponent;
import com.player.customize.player.VideoView;
import com.player.customize.util.PlayerUtils;
import com.videoplayer.R;

/**
 * 直播底部控制栏
 */
public class LiveControlView extends FrameLayout implements IControlComponent, View.OnClickListener {

    public ControlWrapper mControlWrapper;

    private ImageView mFullScreen;
    private LinearLayout mBottomContainer;
    private ImageView mPlayButton;
    private LinearLayout bottomGift;
    public TextView tvInput,tvNickName,tvNum,tvRoomId;
    public ImageView imgRecharge,imgRed,imgGift,iv_danmu;
    private ConstraintLayout cosInfo;

    public LiveControlView(@NonNull Context context) {
        super(context);
    }

    public LiveControlView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LiveControlView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    
    
    {
        setVisibility(GONE);
        LayoutInflater.from(getContext()).inflate(R.layout.dkplayer_layout_live_control_view, this, true);
        mFullScreen = findViewById(R.id.fullscreen);
        mFullScreen.setOnClickListener(this);
        mBottomContainer = findViewById(R.id.bottom_container);
        mPlayButton = findViewById(R.id.iv_play);
        mPlayButton.setOnClickListener(this);
        ImageView refresh = findViewById(R.id.iv_refresh);
        refresh.setOnClickListener(this);
        bottomGift = findViewById(R.id.bottomGift);
        tvInput = findViewById(R.id.tvInput);
        imgRecharge = findViewById(R.id.imgRecharge);
        imgRed = findViewById(R.id.imgRed);
        imgGift = findViewById(R.id.imgGift);
        cosInfo = findViewById(R.id.cosInfo);
        tvNickName = findViewById(R.id.tvNickName);
        tvNum = findViewById(R.id.tvNum);
        tvRoomId = findViewById(R.id.tvRoomId);
        iv_danmu = findViewById(R.id.iv_danmu);
        iv_danmu.setSelected(true);
        if (UserInfoSp.INSTANCE.getDanMuSwitch()){
            iv_danmu.setSelected(true);
        }else   iv_danmu.setSelected(false);
    }

    @SuppressLint("SetTextI18n")
    public void  setInfo(String NickName, String Num, String RoomId){
        tvNickName.setText(NickName);
        tvNum.setText("在线人数 "+Num);
        tvRoomId.setText("房间ID "+RoomId);

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
        if (isVisible) {
            if (getVisibility() == GONE) {
                setVisibility(VISIBLE);
                if (anim != null) {
                    startAnimation(anim);
                }
            }
        } else {
            if (getVisibility() == VISIBLE) {
                setVisibility(GONE);
                if (anim != null) {
                    startAnimation(anim);
                }
            }
        }
    }

    @Override
    public void onPlayStateChanged(int playState) {
        switch (playState) {
            case VideoView.STATE_IDLE:
            case VideoView.STATE_START_ABORT:
            case VideoView.STATE_PREPARING:
            case VideoView.STATE_PREPARED:
            case VideoView.STATE_ERROR:
            case VideoView.STATE_PLAYBACK_COMPLETED:
                setVisibility(GONE);
                break;
            case VideoView.STATE_PLAYING:
                mPlayButton.setSelected(true);
                break;
            case VideoView.STATE_PAUSED:
                mPlayButton.setSelected(false);
                break;
            case VideoView.STATE_BUFFERING:
            case VideoView.STATE_BUFFERED:
                mPlayButton.setSelected(mControlWrapper.isPlaying());
                break;
        }
    }

    @Override
    public void onPlayerStateChanged(int playerState) {
        switch (playerState) {
            case VideoView.PLAYER_NORMAL:
                mFullScreen.setSelected(false);
                ViewUtils.INSTANCE.setVisible(mFullScreen);
                ViewUtils.INSTANCE.setVisible(cosInfo);
                ViewUtils.INSTANCE.setGone(bottomGift);
                break;
            case VideoView.PLAYER_FULL_SCREEN:
                ViewUtils.INSTANCE.setGone(mFullScreen);
                ViewUtils.INSTANCE.setGone(cosInfo);
                ViewUtils.INSTANCE.setVisible(bottomGift);
//                mFullScreen.setSelected(true);
                break;
        }

        Activity activity = PlayerUtils.scanForActivity(getContext());
        if (activity != null && mControlWrapper.hasCutout()) {
            int orientation = activity.getRequestedOrientation();
            int cutoutHeight = mControlWrapper.getCutoutHeight();
            if (orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                mBottomContainer.setPadding(0, 0, 0, 0);
            } else if (orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                mBottomContainer.setPadding(cutoutHeight, 0, 0, 0);
            } else if (orientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE) {
                mBottomContainer.setPadding(0, 0, cutoutHeight, 0);
            }
        }
    }

    @Override
    public void setProgress(int duration, int position) {

    }

    @Override
    public void onLockStateChanged(boolean isLocked) {
        onVisibilityChanged(!isLocked, null);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.fullscreen) {
            toggleFullScreen();
        } else if (id == R.id.iv_play) {
            mControlWrapper.togglePlay();
        } else if (id == R.id.iv_refresh) {
            mControlWrapper.replay(true);
        }
    }




    /**
     * 横竖屏切换
     */
    public void toggleFullScreen() {
        Activity activity = PlayerUtils.scanForActivity(getContext());
        mControlWrapper.toggleFullScreen(activity);
    }
}
