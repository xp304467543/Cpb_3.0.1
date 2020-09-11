package com.customer.player.video;

import android.view.View;

import com.customer.data.UserInfoSp;
import com.customer.player.video.pip.FloatController;
import com.customer.player.video.pip.FloatView;
import com.customer.player.video.pip.PipControlView;
import com.lib.basiclib.utils.ViewUtils;
import com.player.customize.player.VideoView;
import com.player.customize.player.VideoViewManager;


/**
 * 悬浮播放
 */

public class PipManager {

    private static PipManager instance;
    public VideoView mVideoView;
    private FloatView mFloatView;
    private FloatController mFloatController;
    private boolean mIsShowing;
    private int mPlayingPosition = -1;
    private Class mActClass;
    public PipControlView pipControlView;
    public Boolean isLan = false;

    private PipManager() {
        mVideoView = new VideoView(ViewUtils.INSTANCE.getContext());
        VideoViewManager.instance().add(mVideoView, TagVideo.PIP);
        pipControlView = new PipControlView(ViewUtils.INSTANCE.getContext());
        mFloatController = new FloatController(ViewUtils.INSTANCE.getContext());
        rotateView();
        mFloatController.addControlComponent(pipControlView);
        mFloatView = new FloatView(ViewUtils.INSTANCE.getContext(), 0, 0);
    }

    public static PipManager getInstance() {
        if (instance == null) {
            synchronized (PipManager.class) {
                if (instance == null) {
                    instance = new PipManager();
                }
            }
        }
        return instance;
    }

    public void startFloatWindow() {
        if (mIsShowing) return;
        Utils.removeViewFormParent(mVideoView);
        mVideoView.setVideoController(mFloatController);
        mFloatController.setPlayState(mVideoView.getCurrentPlayState());
        mFloatController.setPlayerState(mVideoView.getCurrentPlayerState());
        mFloatView.addView(mVideoView);
        mFloatView.addToWindow();
        mIsShowing = true;
    }

    public void stopFloatWindow() {
        if (!mIsShowing) return;
        mFloatView.removeFromWindow();
        Utils.removeViewFormParent(mVideoView);
        mIsShowing = false;
    }

    public void rotateView() {
        if (pipControlView != null) {
            pipControlView.btn_rotate.setOnClickListener(v -> changeSize());
        }
    }

    private void changeSize() {
        int width, height;
        if (!isLan) {
            mVideoView.mRenderView.setVideoRotation(90);
            height = ViewUtils.INSTANCE.dp2px(250);
            width = height * 9 / 16;
            isLan = true;
        } else {
            mVideoView.mRenderView.setVideoRotation(0);
            width = ViewUtils.INSTANCE.dp2px(250);
            height = width * 9 / 16;
            isLan = false;
        }
        switch (UserInfoSp.INSTANCE.getVideoSize()) {
            case 1:
                mFloatView.upDateSize(width, height);
                break;
            case 2:
                mFloatView.upDateSize((int) (width * 1.3), (int) (height * 1.3));
                break;
            case 3:
                mFloatView.upDateSize((int) (width * 1.5), (int) (height * 1.5));
                break;
        }
    }

    public static void setInstance(PipManager instance) {
        PipManager.instance = instance;
    }

    public VideoView getmVideoView() {
        return mVideoView;
    }

    public void setmVideoView(VideoView mVideoView) {
        this.mVideoView = mVideoView;
    }

    public FloatView getmFloatView() {
        return mFloatView;
    }

    public void setmFloatView(FloatView mFloatView) {
        this.mFloatView = mFloatView;
    }

    public FloatController getmFloatController() {
        return mFloatController;
    }

    public void setmFloatController(FloatController mFloatController) {
        this.mFloatController = mFloatController;
    }

    public boolean ismIsShowing() {
        return mIsShowing;
    }

    public void setmIsShowing(boolean mIsShowing) {
        this.mIsShowing = mIsShowing;
    }

    public int getmPlayingPosition() {
        return mPlayingPosition;
    }

    public void setmPlayingPosition(int mPlayingPosition) {
        this.mPlayingPosition = mPlayingPosition;
    }

    public Class getmActClass() {
        return mActClass;
    }

    public void setmActClass(Class mActClass) {
        this.mActClass = mActClass;
    }


    private String anchorId;
    private String lotteryId;
    private String nickName;
    private String liveStatus;
    private String onLine;
    private String rId;
    private String name;
    private String avatar;


    public void setPlayingPosition(int position) {
        this.mPlayingPosition = position;
    }

    public int getPlayingPosition() {
        return mPlayingPosition;
    }

    public void pause() {
        if (mIsShowing) return;
        mVideoView.pause();
    }

    public void resume() {
        if (mIsShowing) return;
        mVideoView.resume();
    }

    public void replay(Boolean replay) {
        if (mIsShowing) return;
        mVideoView.replay(replay);
    }

    public void reset() {
        if (mIsShowing) return;
        Utils.removeViewFormParent(mVideoView);
        mVideoView.release();
        mVideoView.setVideoController(null);
        mPlayingPosition = -1;
        mActClass = null;
    }

    //强制释放
    public void forceReset() {
        if (getAnchorId() == null) return;
        Utils.removeViewFormParent(mVideoView);
        mVideoView.release();
        mVideoView.setVideoController(null);
        mPlayingPosition = -1;
        mActClass = null;
    }

    public boolean onBackPress() {
        return !mIsShowing && mVideoView.onBackPressed();
    }

    public boolean isStartFloatWindow() {
        return mIsShowing;
    }


    /**
     * 显示悬浮窗
     */
    public void setFloatViewVisible() {
        if (mIsShowing) {
            mVideoView.resume();
            mFloatView.setVisibility(View.VISIBLE);
        }
    }

    public void setActClass(Class cls) {
        this.mActClass = cls;
    }

    public Class getActClass() {
        return mActClass;
    }


    public String getAnchorId() {
        return anchorId;
    }

    public void setAnchorId(String anchorId) {
        this.anchorId = anchorId;
    }

    public String getLotteryId() {
        return lotteryId;
    }

    public void setLotteryId(String lotteryId) {
        this.lotteryId = lotteryId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getLiveStatus() {
        return liveStatus;
    }

    public void setLiveStatus(String liveStatus) {
        this.liveStatus = liveStatus;
    }

    public String getOnLine() {
        return onLine;
    }

    public void setOnLine(String onLine) {
        this.onLine = onLine;
    }

    public String getrId() {
        return rId;
    }

    public void setRid(String rId) {
        this.rId = rId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
