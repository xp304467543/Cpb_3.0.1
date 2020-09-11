package com.home.live.children

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.home.R
import com.customer.player.video.PipManager
import com.customer.player.video.TagVideo
import com.player.customize.player.VideoView
import com.player.customize.player.VideoViewManager
import com.player.customize.ui.videocontroller.StandardVideoController
import kotlinx.android.synthetic.main.testtt.*


/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/26
 * @ Describe
 *
 */
class TexsA : AppCompatActivity() {


    private var mPIPManager: PipManager? = null
    var mp4 = "http://ivi.bupt.edu.cn/hls/cctv1hd.m3u8"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.testtt)
        val playerContainer: FrameLayout = findViewById(R.id.container)
        mPIPManager = PipManager.getInstance()
        val videoView: VideoView<*> = VideoViewManager.instance().get(TagVideo.PIP)
        val controller = StandardVideoController(this)
        controller.addDefaultControlComponent("getString(R.string.str_pip)", true)
        videoView.setVideoController(controller)
        if (mPIPManager?.isStartFloatWindow != false) {
            mPIPManager?.stopFloatWindow()
            controller.setPlayerState(videoView.currentPlayerState)
            controller.setPlayState(videoView.currentPlayState)
        } else {
            mPIPManager?.actClass = TexsA::class.java
            videoView.setUrl(mp4)
        }
        playerContainer.addView(videoView)

        startBT.setOnClickListener {
            mPIPManager?.startFloatWindow()
            mPIPManager?.resume()
            finish()
        }
    }

}
