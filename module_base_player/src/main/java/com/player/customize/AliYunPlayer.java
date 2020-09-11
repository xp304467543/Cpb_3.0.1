package com.player.customize;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;

import com.aliyun.player.AliPlayer;
import com.aliyun.player.AliPlayerFactory;
import com.aliyun.player.IPlayer;
import com.aliyun.player.bean.ErrorInfo;
import com.aliyun.player.bean.InfoBean;
import com.aliyun.player.bean.InfoCode;
import com.aliyun.player.source.UrlSource;
import com.player.customize.player.AbstractPlayer;
import com.player.customize.util.L;

import java.util.Map;

/**
 * @ Author  QinTian
 * @ Date  2020/8/7
 * @ Describe
 */
public class AliYunPlayer extends AbstractPlayer {

    private final String TAG = "AliPlayer";
    /**
     * 真正的播放器实例对象
     */
    protected AliPlayer aliPlayer;

    //播放的进度
    private int mVideoPosition = 0;
    //原视频的buffered
    private int mVideoBufferedPercentage = 0;
    private Context mAppContext;
    private IPlayer.OnRenderingStartListener mOutFirstFrameStartListener = null;
    //当前播放器的状态
    private int mPlayerState = IPlayer.idle;

    public AliYunPlayer(Context context) {
        mAppContext = context;
    }

    @Override
    public void initPlayer() {
        aliPlayer = AliPlayerFactory.createAliPlayer(mAppContext);
        aliPlayer.setScaleMode(IPlayer.ScaleMode.SCALE_ASPECT_FILL);

    }

    @Override
    public void setDataSource(String path, Map<String, String> headers) {
        try {
            openVideo(path);
        } catch (Exception e) {
            mPlayerEventListener.onError();
        }
    }

    @Override
    public void setDataSource(AssetFileDescriptor fd) {
    }


    private void openVideo(String url) {
        if (aliPlayer == null) {
            aliPlayer = AliPlayerFactory.createAliPlayer(mAppContext);
        } else {
            stop();
            reset();
            resetListener();
        }
        aliPlayer.setOnPreparedListener(mPreparedListener);
        aliPlayer.setOnStateChangedListener(mOnStateChangedListener);
        aliPlayer.setOnVideoSizeChangedListener(mSizeChangedListener);
        aliPlayer.setOnCompletionListener(mCompletionListener);
        aliPlayer.setOnErrorListener(mErrorListener);
        aliPlayer.setOnSeekCompleteListener(mOnSeekCompleteListener);
        aliPlayer.setOnLoadingStatusListener(mOnLoadingStatusListener);
        aliPlayer.setOnRenderingStartListener(mOutFirstFrameStartListener);
        aliPlayer.setOnInfoListener(mInfoListener);
        if (url != null) {
            UrlSource urlSource = new UrlSource();
            urlSource.setUri(url);
            aliPlayer.setDataSource(urlSource);
            aliPlayer.setAutoPlay(true);
            aliPlayer.prepare();
        }
    }

    @Override
    public void start() {
        try {
            mPlayerState = IPlayer.started;
            aliPlayer.start();
        } catch (IllegalStateException e) {
            mPlayerEventListener.onError();
        }
    }

    @Override
    public void pause() {
        try {
            mPlayerState = IPlayer.paused;
            aliPlayer.pause();
        } catch (IllegalStateException e) {
            mPlayerEventListener.onError();
        }
    }

    @Override
    public void stop() {
        try {
            mPlayerState = IPlayer.stopped;
            aliPlayer.stop();
        } catch (IllegalStateException e) {
            mPlayerEventListener.onError();
        }
    }

    @Override
    public void prepareAsync() {
        try {
            aliPlayer.prepare();
        } catch (IllegalStateException e) {
            mPlayerEventListener.onError();
        }
    }

    @Override
    public void reset() {
        try {
            aliPlayer.reset();
        } catch (IllegalStateException e) {
            mPlayerEventListener.onError();
        }
    }

    @Override
    public boolean isPlaying() {
        L.d("->>>>>>"+mPlayerState+"------"+IPlayer.started);
        return mPlayerState == IPlayer.started;
    }


    @Override
    public void release() {
        mVideoPosition = 0;
        mVideoBufferedPercentage = 0;
        aliPlayer.setOnPreparedListener(null);
        aliPlayer.setOnVideoSizeChangedListener(null);
        aliPlayer.setOnCompletionListener(null);
        aliPlayer.setOnErrorListener(null);
        aliPlayer.setOnInfoListener(null);
        new Thread() {
            @Override
            public void run() {
                try {
                    aliPlayer.release();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public long getCurrentPosition() {
        return mVideoPosition;
    }

    @Override
    public void seekTo(long time) {
        try {
            aliPlayer.seekTo((int) time);
            mVideoPosition = (int) time;
        } catch (IllegalStateException e) {
            mPlayerEventListener.onError();
        }
    }

    @Override
    public long getDuration() {
        return aliPlayer.getDuration();
    }

    @Override
    public int getBufferedPercentage() {
        return mVideoBufferedPercentage;
    }

    @Override
    public void setSurface(Surface surface) {
        aliPlayer.setSurface(surface);
    }

    @Override
    public void setDisplay(SurfaceHolder holder) {
        aliPlayer.setDisplay(holder);
    }

    @Override
    public void setVolume(float v1, float v2) {
        aliPlayer.setVolume(v1);
    }

    @Override
    public void setLooping(boolean isLooping) {
        aliPlayer.setLoop(isLooping);
    }

    @Override
    public void setOptions() {

    }

    @Override
    public void setSpeed(float speed) {
        aliPlayer.setSpeed(speed);
    }

    @Override
    public float getSpeed() {
        return aliPlayer.getSpeed();
    }

    @Override
    public long getTcpSpeed() {
        return 10;
    }


    AliPlayer.OnInfoListener mInfoListener = new AliPlayer.OnInfoListener() {

        @Override
        public void onInfo(InfoBean infoBean) {
            if (infoBean.getCode() == InfoCode.CurrentPosition) {
                mVideoPosition = (int) infoBean.getExtraValue();
            }else if (infoBean.getCode() == InfoCode.BufferedPosition){
                mVideoBufferedPercentage =  (int) infoBean.getExtraValue();
            }
            mPlayerEventListener.onInfo(infoBean.getCode().getValue(), infoBean.getCode().getValue());
        }
    };


    AliPlayer.OnErrorListener mErrorListener = new IPlayer.OnErrorListener() {
        @Override
        public void onError(ErrorInfo errorInfo) {
            Log.e(TAG, "onError------>" + errorInfo.getMsg());
            mPlayerEventListener.onError();
            start();
        }
    };
    AliPlayer.OnCompletionListener mCompletionListener = new IPlayer.OnCompletionListener() {
        @Override
        public void onCompletion() {
            Log.e(TAG, "PLAYER_EVENT_ON_PLAY_COMPLETE");
            mPlayerEventListener.onCompletion();
        }
    };
    AliPlayer.OnLoadingStatusListener mOnLoadingStatusListener = new IPlayer.OnLoadingStatusListener() {
        @Override
        public void onLoadingBegin() {
        }

        @Override
        public void onLoadingProgress(int percent, float v) {

        }

        @Override
        public void onLoadingEnd() {
        }
    };
    AliPlayer.OnPreparedListener mPreparedListener = new AliPlayer.OnPreparedListener() {

        @Override
        public void onPrepared() {
            mPlayerEventListener.onPrepared();
        }
    };

    AliPlayer.OnVideoSizeChangedListener mSizeChangedListener = new AliPlayer.OnVideoSizeChangedListener() {
        @Override
        public void onVideoSizeChanged(int w, int h) {
            int videoWidth = aliPlayer.getVideoWidth();
            int videoHeight = aliPlayer.getVideoHeight();
            if (videoWidth != 0 && videoHeight != 0) {
                mPlayerEventListener.onVideoSizeChanged(videoWidth, videoHeight);
            }
        }
    };

    AliPlayer.OnStateChangedListener mOnStateChangedListener = new IPlayer.OnStateChangedListener() {
        @Override
        public void onStateChanged(int i) {
            mPlayerState = i;
            L.d("->>>>>>22"+mPlayerState+"------"+i);
        }
    };

    AliPlayer.OnSeekCompleteListener mOnSeekCompleteListener = new IPlayer.OnSeekCompleteListener() {
        @Override
        public void onSeekComplete() {
            Log.e(TAG, "EVENT_CODE_SEEK_COMPLETE");
//            mPlayerEventListener.onCompletion();
        }
    };


    private void resetListener() {
        if (aliPlayer == null)
            return;
        mVideoPosition = 0;
        mVideoBufferedPercentage = 0;
        aliPlayer.setOnPreparedListener(null);
        aliPlayer.setOnVideoSizeChangedListener(null);
        aliPlayer.setOnCompletionListener(null);
        aliPlayer.setOnErrorListener(null);
        aliPlayer.setOnInfoListener(null);
        aliPlayer.setOnStateChangedListener(null);
    }
}
