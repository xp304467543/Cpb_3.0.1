package com.home.live

import android.content.Context
import android.os.Build
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.customer.adapter.TabNormalAdapter
import com.customer.component.dialog.GlobalDialog
import com.customer.component.panel.emotion.EmotionSpanBuilder
import com.customer.component.spanlite.CenterAlignImageSpan
import com.customer.component.spanlite.SpanBuilder
import com.customer.data.DanMu
import com.customer.data.UserInfoSp
import com.customer.player.video.PipManager
import com.customer.player.video.TagVideo
import com.customer.player.video.Utils
import com.customer.player.video.controller.LiveControlView
import com.customer.player.video.controller.StandardLiveController
import com.customer.player.video.danmaku.DanMuView
import com.home.R
import com.home.live.children.LiveRoomChild1
import com.home.live.children.LiveRoomChild2
import com.home.live.children.LiveRoomChild3
import com.home.live.children.LiveRoomChild4
import com.lib.basiclib.base.adapter.BaseFragmentPageAdapter
import com.lib.basiclib.utils.ViewUtils
import com.lib.basiclib.widget.tab.MagicIndicator
import com.lib.basiclib.widget.tab.ViewPagerHelper
import com.lib.basiclib.widget.tab.buildins.commonnavigator.CommonNavigator
import com.player.customize.player.VideoView
import com.player.customize.player.VideoViewManager
import com.player.customize.ui.videocontroller.component.CompleteView
import com.player.customize.ui.videocontroller.component.GestureView
import com.player.customize.ui.videocontroller.component.PrepareView
import com.player.customize.ui.videocontroller.component.TitleView
import java.util.regex.Pattern

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/26
 * @ Describe
 *
 */
object LiveRoomHelper {

    private lateinit var mActivity: LiveRoomActivity
    var mPIPManager: PipManager? = null
    var mMyDanMuView: DanMuView? = null
    var videoView: VideoView<*>?=null
    lateinit var controller: StandardLiveController
    lateinit var prepareView: PrepareView
    lateinit var titleView: TitleView
    lateinit var gestureControlView: GestureView
//    lateinit var errorVView: ErrorView
    lateinit var completeView: CompleteView
    lateinit var bottomController: LiveControlView


    var mp4 = "http://ivi.bupt.edu.cn/hls/cctv1hd.m3u8"
    fun initHelper(activity: LiveRoomActivity, container: FrameLayout) {
        this.mActivity = activity
        initVideo(activity,container)
    }

    private fun initVideo(activity: LiveRoomActivity, container: FrameLayout){
        if ((activity.anchorId != mPIPManager?.anchorId)) PipManager.getInstance().forceReset()
        val playerContainer: FrameLayout? = container
        mPIPManager = PipManager.getInstance()
        mPIPManager?.anchorId = activity.anchorId
        mPIPManager?.lotteryId = activity.lotteryId
        mPIPManager?.nickName = activity.nickName
        mPIPManager?.liveStatus = activity.liveStatus
        mPIPManager?.onLine = activity.onLine
        mPIPManager?.setRid(activity.rId)
        mPIPManager?.name = activity.name
        mPIPManager?.avatar = activity.avatar
        videoView  = VideoViewManager.instance().get(TagVideo.PIP)
        Utils.removeViewFormParent(videoView)
        controller = StandardLiveController(activity)
        initController(activity)
        videoView?.setVideoController(controller)
        if (mPIPManager?.isStartFloatWindow != false) {
            mPIPManager?.stopFloatWindow()
            controller.setPlayerState(videoView?.currentPlayerState?:0)
            controller.setPlayState(videoView?.currentPlayState?:0)
        } else {
            mPIPManager?.actClass = LiveRoomActivity::class.java
        }
        playerContainer?.removeAllViews()
        playerContainer?.addView(videoView)
    }


    fun initTab(
        tab: MagicIndicator,
        vp: ViewPager,
        anchorId: String,
        lotteryId: String,
        lotteryType: String,
        liveState: String,
        nickName: String
    ) {

        val title = arrayListOf("聊天", "主播", "排行", "预告")

        val fragments = arrayListOf(
            LiveRoomChild1.newInstance(anchorId, liveState, nickName, lotteryId, lotteryType),
            LiveRoomChild2.newInstance(anchorId, liveState, nickName, lotteryId, lotteryType),
            LiveRoomChild3.newInstance(anchorId, liveState, nickName, lotteryId, lotteryType),
            LiveRoomChild4.newInstance(anchorId, liveState, nickName, lotteryId, lotteryType)
        )
        val commonNavigator = CommonNavigator(mActivity)
        commonNavigator.scrollPivotX = 0.65f
        commonNavigator.isAdjustMode = true
        commonNavigator.adapter = TabNormalAdapter(
            titleList = title, viewPage = vp,
            colorTextSelected = ViewUtils.getColor(R.color.color_333333),
            colorTextNormal = ViewUtils.getColor(R.color.color_AFAFAF),
            colorLine = ViewUtils.getColor(R.color.color_FF513E)
        )
        tab.navigator = commonNavigator
        vp.adapter = BaseFragmentPageAdapter(mActivity.supportFragmentManager, fragments)
        vp.offscreenPageLimit = 4
        ViewPagerHelper.bind(tab, vp)
    }

    private fun initController(context: Context) {
        prepareView = PrepareView(context)
        gestureControlView = GestureView(context)
//        errorVView = ErrorView(context)
        completeView = CompleteView(context)
        titleView = TitleView(context)
        bottomController =
            LiveControlView(context)
        mMyDanMuView = DanMuView(context)
        //根据屏幕方向自动进入/退出全屏
        controller.setEnableOrientation(false)
        controller.addControlComponent(prepareView)
        controller.addControlComponent(completeView) //自动完成播放界面
//        controller.addControlComponent(errorVView) //错误界面
        controller.addControlComponent(titleView)
        //根据是否为直播设置不同的底部控制条
        controller.addControlComponent(bottomController) //直播控制条
        controller.addControlComponent(gestureControlView)
        controller.addControlComponent(mMyDanMuView)
        //根据是否为直播决定是否需要滑动调节进度
        controller.setCanChangePosition(false)

    }

    fun showDanMu() {
        mMyDanMuView?.show()
    }

    fun hideDanMu() {
        mMyDanMuView?.hide()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun addDanMu(data: DanMu) {
        mMyDanMuView?.addDanmakuWithDrawable(createSpannable(data), data.isMe)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun createSpannable(data: DanMu): SpannableStringBuilder {
        val content = replaceBlank(data.text)
        when (data.userType) {
            "1" -> {
                val spanBuilder = SpanBuilder.Builder("1")
                    .drawImageWidthHeight(mActivity, R.mipmap.ic_live_anchor, 100, 44).build()
                spanBuilder.append(" " + data.userName + " : ")
                spanBuilder.append(EmotionSpanBuilder.buildEmotionSpannable(mActivity, content))
                return spanBuilder
            }

            "2" -> {
                val spanBuilder = SpanBuilder.Builder("1")
                    .drawImageWidthHeight(mActivity, R.mipmap.ic_live_chat_manager, 100, 44).build()
                spanBuilder.append(" " + data.userName + " : ")
                spanBuilder.append(EmotionSpanBuilder.buildEmotionSpannable(mActivity, content))
                return spanBuilder
            }

            else -> {
                val spanBuilder = SpannableStringBuilder()

                val drawable = when (data.vip) {
                    "1" -> {
                        ViewUtils.getDrawable(R.mipmap.v1)
                    }
                    "2" -> {
                        ViewUtils.getDrawable(R.mipmap.v2)
                    }
                    "3" -> {
                        ViewUtils.getDrawable(R.mipmap.v3)
                    }
                    "4" -> {
                        ViewUtils.getDrawable(R.mipmap.v4)
                    }
                    "5" -> {
                        ViewUtils.getDrawable(R.mipmap.v5)
                    }
                    "6" -> {
                        ViewUtils.getDrawable(R.mipmap.v6)
                    }
                    "7" -> {
                        ViewUtils.getDrawable(R.mipmap.v7)
                    }
                    else -> {
                        spanBuilder.append(" " + data.userName + " : ")
                        spanBuilder.append(
                            EmotionSpanBuilder.buildEmotionSpannable(
                                mActivity,
                                content
                            )
                        )
                        return spanBuilder
                    }
                }
                drawable?.setBounds(0, 0, 100, 44)
                val span = CenterAlignImageSpan(drawable)
                spanBuilder.append("icon", span, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
                spanBuilder.append(" " + data.userName + " : ")
                spanBuilder.append(EmotionSpanBuilder.buildEmotionSpannable(mActivity, content))
                return spanBuilder
            }
        }
    }

    //去掉换行符
    private fun replaceBlank(src: String?): String {
        var dest = ""
        if (src != null) {
            val pattern = Pattern.compile("\n\\s*")
            val matcher = pattern.matcher(src)
            dest = matcher.replaceAll(" ")
        }
        return dest
    }


    fun isLogin(activity: AppCompatActivity): Boolean {
        return if (!UserInfoSp.getIsLogin()) {
            controller.toggleFullScreen()
            GlobalDialog.notLogged(activity)
            false
        } else true
    }
}