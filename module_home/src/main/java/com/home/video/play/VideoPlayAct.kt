package com.home.video.play

import android.annotation.SuppressLint
import android.graphics.Color
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.customer.ApiRouter
import com.customer.component.dialog.GlobalDialog
import com.customer.data.UserInfoSp
import com.customer.data.video.MovieZan
import com.customer.data.video.VideoPlay
import com.customer.player.video.controller.StandardVideoController
import com.customer.player.video.controller.VodTryView
import com.glide.GlideUtil
import com.home.R
import com.home.video.adapter.VideoSearchAdapter
import com.lib.basiclib.base.mvp.BaseMvpActivity
import com.lib.basiclib.base.xui.adapter.recyclerview.XLinearLayoutManager
import com.lib.basiclib.base.xui.widget.popupwindow.good.GoodView
import com.lib.basiclib.utils.FastClickUtil
import com.lib.basiclib.utils.StatusBarUtils
import com.lib.basiclib.utils.TimeUtils
import com.lib.basiclib.utils.ToastUtils
import com.player.customize.ui.videocontroller.component.*
import com.xiaojinzi.component.anno.RouterAnno
import com.xiaojinzi.component.impl.Router
import kotlinx.android.synthetic.main.act_video_play.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/31
 * @ Describe
 *
 */
@RouterAnno(host = "Home", path = "videoPlayer")
class VideoPlayAct : BaseMvpActivity<VideoPlayActPresenter>() {

    var adapter: VideoSearchAdapter? = null

    private var videoId: Int = -1

    private var videoTitle: String = "未知"

    private var thumb: ImageView? = null

    private var isRefreshAdapter:Boolean = true

    override fun attachView() = mPresenter.attachView(this)

    override fun attachPresenter() = VideoPlayActPresenter()

    override val layoutResID = R.layout.act_video_play

    override fun isOverride() = true

    override fun isSwipeBackEnable() = true

    override fun isRegisterRxBus() = true

    override fun initContentView() {
        StatusBarUtils.setStatusBarHeight(stateViewPlay)

        videoId = intent.getIntExtra("videoId", -1)
        videoTitle = intent.getStringExtra("videoTitle") ?: "未知"
        if (videoId != -1) {
            initVideo()
        } else ToastUtils.showToast("视频信息获取失败")

    }


    private fun initOther() {
        GlideUtil.loadImage(this, UserInfoSp.getMovieBanner().split(",")[0], imgBanner)
        if (UserInfoSp.getMovieBanner().contains(",")){
            imgBanner.setOnClickListener { Router.withApi(ApiRouter::class.java).toGlobalWeb(UserInfoSp.getMovieBanner().split(",")[1]) }
        }else {
            imgBanner.setOnClickListener { Router.withApi(ApiRouter::class.java).toGlobalWeb(UserInfoSp.getMovieBanner()) }
        }

        smartRefreshLayoutPlay.setEnableRefresh(false)//是否启用下拉刷新功能
        smartRefreshLayoutPlay.setEnableLoadMore(false)//是否启用上拉加载功能
        smartRefreshLayoutPlay.setEnableOverScrollBounce(true)//是否启用越界回弹
        smartRefreshLayoutPlay.setEnableOverScrollDrag(true)//是否启用越界拖动（仿苹果效果）
        val layoutManager = XLinearLayoutManager(this)
        layoutManager.setScrollEnabled(false)
        rvPlay.layoutManager = layoutManager
        adapter = VideoSearchAdapter(this,true)
        rvPlay.adapter = adapter
        adapter?.setOnItemClickListener{
            itemView, item, position ->
            isRefreshAdapter = false
            titleViewController?.setTitle(item.title)
            item?.id?.let { mPresenter.getVideoAddress(it) }
        }
    }


    private var titleViewController:VodTitleView?=null
    private fun initVideo() {
        val controller = StandardVideoController(this)
        val completeView = VodCompleteView(this)
        val errorView = VodErrorView(this)
        val prepareView = VodPrepareView(this)
        val vodControlView = VodControlView(this)
        val gestureControlView = VodGestureView(this)
        titleViewController =  VodTitleView(this)
        controller.addControlComponent(completeView)
        controller.addControlComponent(errorView)
        controller.addControlComponent(prepareView)
        controller.addControlComponent(titleViewController)
        controller.addControlComponent(vodControlView)
        controller.addControlComponent(gestureControlView)
        controller.addControlComponent(VodTryView(this))
        titleViewController?.setTitle(intent.getStringExtra("videoTitle"))
        thumb = prepareView.findViewById(R.id.thumb) //封面图
        controller.setCanChangePosition(true)
        controller.setEnableOrientation(false)
        controller.setEnableInNormal(true)
        mVideoPlayerView.setVideoController(controller)
    }

    override fun initData() {
        initOther()
        mPresenter.getVideoAddress(videoId)
    }
//
    override fun initEvent() {
        tvChange.setOnClickListener { if (!FastClickUtil.isFastClick())  mPresenter.getLike(videoId) else ToastUtils.showToast("请勿重复点击") }

    linZan.setOnClickListener {
            if (!UserInfoSp.getIsLogin()) {
                GlobalDialog.notLogged(this)
                return@setOnClickListener
            }
            mPresenter.zan(videoId,1)
        }
    linCai.setOnClickListener {
            if (!UserInfoSp.getIsLogin()) {
                GlobalDialog.notLogged(this)
                return@setOnClickListener
            }
            mPresenter.zan(videoId,0)
        }
    }

    fun initZan(data: MovieZan){
        tvZan.text = data.praise.toString()
        tvCai.text = data.tread.toString()
        val mGoodView = GoodView(this)
        when (data.msg) {
            "点赞成功" -> {
                mGoodView.setText("+1")
                    .setTextColor(Color.parseColor("#f66467"))
                    .setTextSize(14)
                    .show(linZan)
                imgZan.setImageResource(R.mipmap.ic_movie_zan_red)
                imgCai.setImageResource(R.mipmap.ic_movie_down)
            }
            "取消点赞" -> {
                mGoodView.setText("-1")
                    .setTextColor(Color.parseColor("#AFAFAF"))
                    .setTextSize(14)
                    .show(linZan)
                imgZan.setImageResource(R.mipmap.ic_movie_zan)
            }
            "点踩成功" -> {
                mGoodView.setText("+1")
                    .setTextColor(Color.parseColor("#f66467"))
                    .setTextSize(14)
                    .show(linCai)
                imgCai.setImageResource(R.mipmap.ic_movie_down_red)
                imgZan.setImageResource(R.mipmap.ic_movie_zan)
            }
            "取消点踩" -> {
                mGoodView.setText("-1")
                    .setTextColor(Color.parseColor("#AFAFAF"))
                    .setTextSize(14)
                    .show(linCai)
                imgCai.setImageResource(R.mipmap.ic_movie_down)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun initVideoInfo(data: VideoPlay) {
        if (isActive()){
            when (data.state) {
                "1" -> {
                    imgZan.setImageResource(R.mipmap.ic_movie_zan_red)
                    imgCai.setImageResource(R.mipmap.ic_movie_down)
                }
                "2" -> {
                    imgZan.setImageResource(R.mipmap.ic_movie_zan)
                    imgCai.setImageResource(R.mipmap.ic_movie_down_red)
                }
                else -> {
                    imgZan.setImageResource(R.mipmap.ic_movie_zan)
                    imgCai.setImageResource(R.mipmap.ic_movie_down)
                }
            }

            thumb?.let { Glide.with(this).load(data.cover).into(it) }
            mVideoPlayerView.setUrl(data.play_url)
            if (isRefreshAdapter)   mVideoPlayerView.start() else mVideoPlayerView.replay(true)
            tvVideoTitle.text = data.title
            tvTimeInfo.text = data.reads + "播放                " + TimeUtils.longToDateStringYYMMDD(
                data.shelftime ?: 0
            )
            tvZan.text = data.praise
            tvCai.text = data.tread
            if (isRefreshAdapter) adapter?.refresh(data.list)
        }
    }


    var isTrySee = true //标记试看
    override fun onResume() {
        super.onResume()
        if (mVideoPlayerView != null) {
            if (UserInfoSp.getIsLogin() || isTrySee) {
                mVideoPlayerView.resume()
                isTrySee = false
            }
        }
    }


    override fun onPause() {
        super.onPause()
        if (mVideoPlayerView != null) {
            mVideoPlayerView.pause()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mVideoPlayerView != null) {
            mVideoPlayerView.release()
        }
    }

    override fun onBackPressed() {
        if (mVideoPlayerView == null || !mVideoPlayerView.onBackPressed()) {
            super.onBackPressed()
        }
    }
}