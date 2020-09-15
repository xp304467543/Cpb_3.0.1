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
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import com.glide.GlideUtil;
import com.lib.basiclib.utils.ViewUtils;
import com.player.customize.controller.ControlWrapper;
import com.player.customize.controller.IControlComponent;
import com.player.customize.player.VideoView;
import com.player.customize.util.PlayerUtils;
import com.videoplayer.R;

/**
 * 播放器顶部标题栏
 */
public class TitleView extends FrameLayout implements IControlComponent {

    public ControlWrapper mControlWrapper;

    public LinearLayout mTitleContainer,topLin;

    public TextView tvNickName, tvPeopleNum,tvRoomId;

    public ImageView imgAvatar,backImg;

    public AppCompatButton btAttention;



    public TitleView(@NonNull Context context) {
        super(context);
    }

    public TitleView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TitleView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        setVisibility(GONE);
        LayoutInflater.from(getContext()).inflate(R.layout.dkplayer_layout_title_view, this, true);
        mTitleContainer = findViewById(R.id.title_container);
        backImg = findViewById(R.id.back);
//        back.setOnClickListener(new OnClickListener() {
//            @SuppressLint("SourceLockedOrientationActivity")
//            @Override
//            public void onClick(View v) {
//                Activity activity = PlayerUtils.scanForActivity(getContext());
//                if (activity != null && mControlWrapper.isFullScreen()) {
//                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//                    mControlWrapper.stopFullScreen();
//                }else if (activity != null) {
//                    activity.finish();
//                }
//            }
//        });
        initTitleViews();

    }

    private void initTitleViews() {
        tvNickName = findViewById(R.id.tvNickName);
        tvPeopleNum = findViewById(R.id.tvPeopleNum);
        imgAvatar = findViewById(R.id.imgAvatar);
        btAttention = findViewById(R.id.btAttention);
        tvRoomId = findViewById(R.id.tvRoomId);
        topLin = findViewById(R.id.topLin);
    }

    @SuppressLint("SetTextI18n")
    public void setInfo(String name, String num, String avatar, String roomId){
        tvNickName.setText(name);
        tvPeopleNum.setText(num+" 人");
        tvRoomId.setText("房间号 "+roomId);
        Activity activity = PlayerUtils.scanForActivity(getContext());
        if (!activity.isDestroyed()) GlideUtil.INSTANCE.loadImage(getContext(),avatar,imgAvatar);
    }

    public void setIsAttention(Boolean isAttention){
        if (isAttention){
            btAttention.setBackgroundDrawable(ViewUtils.INSTANCE.getDrawable(R.drawable.button_grey_background));
            btAttention.setTextColor(ViewUtils.INSTANCE.getColor(R.color.grey_95));
            btAttention.setText("已关注");
        }else {
            btAttention.setBackgroundDrawable(ViewUtils.INSTANCE.getDrawable(R.drawable.button_red_background));
            btAttention.setTextColor(ViewUtils.INSTANCE.getColor(R.color.white));
            btAttention.setText("＋ 关注");
        }
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
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
        //只在全屏时才有效
        if (!mControlWrapper.isFullScreen()) ViewUtils.INSTANCE.setGone(topLin); else ViewUtils.INSTANCE.setVisible(topLin);
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
        }
    }

    @Override
    public void onPlayerStateChanged(int playerState) {
        if (playerState == VideoView.PLAYER_FULL_SCREEN) {
            if (mControlWrapper.isShowing() && !mControlWrapper.isLocked()) {
                setVisibility(VISIBLE);
            }
        } else {
            setVisibility(GONE);
        }

        Activity activity = PlayerUtils.scanForActivity(getContext());
        if (activity != null && mControlWrapper.hasCutout()) {
            int orientation = activity.getRequestedOrientation();
            int cutoutHeight = mControlWrapper.getCutoutHeight();
            if (orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                mTitleContainer.setPadding(0, 0, 0, 0);
            } else if (orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                mTitleContainer.setPadding(cutoutHeight, 0, 0, 0);
            } else if (orientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE) {
                mTitleContainer.setPadding(0, 0, cutoutHeight, 0);
            }
        }
    }

    @Override
    public void setProgress(int duration, int position) {

    }

    @Override
    public void onLockStateChanged(boolean isLocked) {
        if (isLocked) {
            setVisibility(GONE);
        } else {
            setVisibility(VISIBLE);
        }
    }
}
