package com.home.live

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import androidx.annotation.RequiresApi
import com.customer.ApiRouter
import com.customer.base.BaseNormalMvpActivity
import com.customer.component.dialog.DialogPassWordHor
import com.customer.component.dialog.DialogSendRed
import com.customer.component.dialog.GlobalDialog
import com.customer.component.dialog.live.BottomHorGiftWindow
import com.customer.component.input.InputPopWindowHor
import com.customer.data.*
import com.customer.data.home.HomeApi
import com.customer.data.home.HomeLiveBigAnimatorBean
import com.customer.data.home.HomeLiveEnterRoomResponse
import com.customer.data.mine.MineApi
import com.customer.data.mine.MinePassWordTime
import com.customer.utils.HiddenAnimUtils
import com.customer.utils.JsonUtils
import com.customer.utils.SvgaUtils
import com.customer.utils.WindowPermissionCheck
import com.glide.GlideUtil
import com.home.R
import com.hwangjr.rxbus.RxBus
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.thread.EventThread
import com.lib.basiclib.utils.ToastUtils
import com.lib.basiclib.utils.ViewUtils
import com.player.ali.base.util.ScreenUtils
import com.xiaojinzi.component.anno.RouterAnno
import com.xiaojinzi.component.impl.Router
import kotlinx.android.synthetic.main.act_live.*

/**
 * @ Author  QinTian
 * @ Date  2020/6/11
 * @ Describe
 */
@RouterAnno(host = "Home", path = "live")
class LiveRoomActivity : BaseNormalMvpActivity<LiveActPresenter>() {

    lateinit var anchorId: String
    lateinit var lotteryId: String
    lateinit var nickName: String
    lateinit var liveStatus: String
    lateinit var onLine: String
    lateinit var rId: String
    lateinit var name: String
    lateinit var avatar: String

    private var inputPopWindow: InputPopWindowHor? = null

    lateinit var svgaUtils: SvgaUtils

    override fun getContentResID() = R.layout.act_live

    override fun attachView() = mPresenter.attachView(this)

    override fun attachPresenter() = LiveActPresenter()

    override fun isRegisterRx() = true


    override fun initContentView() {
        initIntent()
        LiveRoomHelper.initHelper(this, mVideoView)
        initControllerEvent()
        initSvga()
        initTabView()
    }


    private fun initIntent() {
        anchorId = intent.getStringExtra("anchorId") ?: "-1"
        if (anchorId == "-1") {
            finish()
            ToastUtils.showToast("加载中..")
        }
        lotteryId = intent.getStringExtra("lottery_id") ?: "-1"
        nickName = intent.getStringExtra("nickName") ?: "-1"
        liveStatus = intent.getStringExtra("live_status") ?: "-1"
        onLine = intent.getStringExtra("online") ?: "-1"
        rId = intent.getStringExtra("r_id") ?: "-1"
        name = intent.getStringExtra("name") ?: "-1"
        avatar = intent.getStringExtra("avatar") ?: "-1"
    }

    private fun initTabView() {
        LiveRoomHelper.initTab(
            liveTab, liveVp, anchorId = anchorId, lotteryId = lotteryId
            , lotteryType = name, liveState = liveStatus
            , nickName = nickName
        )
    }

    override fun initData() {
        showPageLoadingDialog()
        mPresenter.getAllData(anchorId)
    }

    override fun initEvent() {
        linAttention.setOnClickListener {
            if (!UserInfoSp.getIsLogin()) {
                GlobalDialog.notLogged(this)
                return@setOnClickListener
            }
            goAttention()
        }

        noAnchorBack.setOnClickListener {
            LiveRoomHelper.mPIPManager?.forceReset()
            finish()
        }

        LiveRoomHelper.bottomController.tvInput.setOnClickListener {
            if (!UserInfoSp.getIsLogin()) {
                GlobalDialog.notLogged(this)
                return@setOnClickListener
            }
            if (inputPopWindow == null) {
                inputPopWindow = InputPopWindowHor(this)
                inputPopWindow?.setOnTextSendListener{
                    RxBus.get().post(SendDanMu(it))
                    inputPopWindow?.dismiss()
                }
            }
            inputPopWindow?.showAtLocation(window?.decorView?.rootView, Gravity.NO_GRAVITY, 0, 0)
            window.decorView.rootView.postDelayed(
                { if (inputPopWindow != null) {
                    inputPopWindow?.showKeyboard()
                } }, 50)
            inputPopWindow?.setOnDismissListener {
            }
        }
    }


    private fun initControllerEvent() {
        LiveRoomHelper.titleView.backImg.setOnClickListener {
            backListener()
        }
//        LiveRoomHelper.errorVView.imgExit.setOnClickListener {
//            backListener()
//        }
        LiveRoomHelper.prepareView.imgExit.setOnClickListener {
            backListener()
        }
        LiveRoomHelper.titleView.btAttention.setOnClickListener {
            if (LiveRoomHelper.isLogin(this)) goAttention()
        }
        LiveRoomHelper.bottomController.imgRecharge.setOnClickListener {
            if (LiveRoomHelper.isLogin(this)) {
                LiveRoomHelper.bottomController.toggleFullScreen()
                Router.withApi(ApiRouter::class.java)
                    .toMineRecharge(1)
            }
        }
        LiveRoomHelper.bottomController.imgGift.setOnClickListener {
            if (LiveRoomHelper.isLogin(this)) bottomGift()
        }
        LiveRoomHelper.bottomController.imgRed.setOnClickListener {
            if (LiveRoomHelper.isLogin(this)) initRedHor()
        }
        LiveRoomHelper.bottomController.iv_danmu.setOnClickListener {
            if (LiveRoomHelper.bottomController.iv_danmu.isSelected) {
                LiveRoomHelper.bottomController.iv_danmu.isSelected = false
                LiveRoomHelper.hideDanMu()
                UserInfoSp.putDanMuSwitch(false)
            } else {
                LiveRoomHelper.bottomController.iv_danmu.isSelected = true
                LiveRoomHelper.showDanMu()
                UserInfoSp.putDanMuSwitch(true)
            }
        }
    }

    private fun backListener() {
        if (LiveRoomHelper.titleView.mControlWrapper.isFullScreen) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            LiveRoomHelper.titleView.mControlWrapper.stopFullScreen()
        } else if (LiveRoomHelper.videoView?.isPlaying == true) {
            if (!isDestroyed && WindowPermissionCheck.checkPermission(this)) {
                LiveRoomHelper.mPIPManager?.startFloatWindow()
                LiveRoomHelper.mPIPManager?.resume()
                finish()
            } else {
                LiveRoomHelper.mPIPManager?.forceReset()
                finish()
            }
        } else {
            LiveRoomHelper.mPIPManager?.forceReset()
            finish()
        }
    }
    var mp4 = "http://ivi.bupt.edu.cn/hls/cctv1hd.m3u8"
    fun initThings(data: HomeLiveEnterRoomResponse) {
        lotteryId =data.lottery_id ?: "-1"
        nickName = data.nickname?: "-1"
        liveStatus = data.live_status ?: "-1"
        onLine = data.online ?: "-1"
        name =data.name ?: "-1"
        avatar = data.avatar ?: "-1"
        LiveRoomHelper.titleView.setInfo(data.nickname, data.online, data.avatar, data.anchor_id)
        LiveRoomHelper.bottomController.setInfo(data.nickname, data.online, data.anchor_id)
        if (data.live_status == "1") {
            setGone(noAnchor)
            data.liveInfo?.get(2)?.liveUrl?.originPullUrl?.let {
                LiveRoomHelper.videoView?.setUrl(mp4)
                LiveRoomHelper.videoView?.start()
            }
        } else {
            setVisible(noAnchor)
            GlideUtil.loadCircleImage(this, data.avatar, ImgNoAnchor, true)
        }
    }


    //关注取关
    private fun goAttention() {
        HomeApi.attentionAnchorOrUser(anchorId, "") {
            onSuccess { changeAttention(it.isFollow) }
            onFailed { GlobalDialog.showError(this@LiveRoomActivity, it, isFullScreen()) }
        }
    }

    fun changeAttention(isAttention: Boolean) {
        if (isAttention) {
            ViewUtils.setGone(imgAttention)
            tvAttention.text = "已关注"
            setTextColor(tvAttention, ViewUtils.getColor(R.color.grey_97))
            linAttention.setBackgroundResource(R.color.color_EEEEEE)
        } else {
            setVisible(imgAttention)
            tvAttention.text = "关注"
            setTextColor(tvAttention, ViewUtils.getColor(R.color.white))
            linAttention.setBackgroundResource(R.drawable.background_gradient)
        }
        LiveRoomHelper.titleView.setIsAttention(isAttention)
    }

    /**
     * 横屏红包
     */
    private fun initRedHor() {
        val dialog = DialogSendRed(this, true)
        dialog.setOnSendClickListener { total, redNumber, redContent ->
            dialog.dismiss()
            initPassWordDialog(total, redNumber, redContent)
        }
        dialog.show()
    }

    /**
     * 密码框弹窗
     */
    private fun initPassWordDialog(total: String, redNumber: String, redContent: String) {
        val passWordDialog = DialogPassWordHor(this)
        passWordDialog.setTextWatchListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s?.length == 6) {
                    //验证支付密码
                    MineApi.verifyPayPass(s.toString()) {
                        onSuccess {
                            passWordDialog.showOrHideLoading()
                            //发红包
                            mPresenter.homeLiveSendRedEnvelope(
                                anchorId,
                                total,
                                redNumber,
                                redContent,
                                s.toString(),
                                passWordDialog
                            )
                        }
                        onFailed {
                            if (it.getCode() == 1002) {
                                passWordDialog.showOrHideLoading()
                                passWordDialog.showTipsText(
                                    it.getMsg().toString() + "," + "您还有" +
                                            JsonUtils.fromJson(
                                                it.getDataCode().toString(),
                                                MinePassWordTime::class.java
                                            ).remain_times.toString() +
                                            "次机会"
                                )
                                passWordDialog.clearText()
                                passWordDialog.showOrHideLoading()
                            } else {
                                passWordDialog.showTipsText(it.getMsg().toString())
                            }
                        }
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        passWordDialog.setOnDismissListener {
            val decorView = ScreenUtils.getDecorView(this)
            if (decorView != null) ScreenUtils.hideSysBar(this, decorView)
        }
        passWordDialog.show()
    }

    /**
     * 横屏底部礼物栏
     */
    var bottomHorGiftWindow: BottomHorGiftWindow? = null

    private fun bottomGift() {
        if (!UserInfoSp.getIsLogin()) {
            GlobalDialog.notLogged(this, true)
            return
        }

        if (bottomHorGiftWindow == null) {
            bottomHorGiftWindow =
                BottomHorGiftWindow(this@LiveRoomActivity)
            bottomHorGiftWindow?.show()
            mPresenter.getGiftList()
        } else bottomHorGiftWindow?.show()
        MineApi.getUserDiamond {
            onSuccess {
                bottomHorGiftWindow?.setDiamond(it.diamond)
            }
            onFailed {
                GlobalDialog.showError(this@LiveRoomActivity, it, true)
            }
        }
    }

    /**
     * 初始化svga
     */
    private fun initSvga() {
        svgaUtils = SvgaUtils(this, svgaImage)
        svgaUtils.initAnimator()
    }

    //收弹幕
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun showRed(eventBean: DanMu) {
        if (isFullScreen() && UserInfoSp.getDanMuSwitch()) {
            LiveRoomHelper.addDanMu(eventBean)
        }
    }

    //更新横屏钻石
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun upDataHorDiamond(eventBean: UpDataHorDiamon) {
        if (isFullScreen() && bottomHorGiftWindow != null) {
            MineApi.getUserDiamond {
                onSuccess {
                    bottomHorGiftWindow?.hideLoading()
                    bottomHorGiftWindow?.setDiamond(it.diamond)
                }
            }
        }
    }

    /**
     * 收起视频 展开视频
     */
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun videoState(eventBean: LiveVideoClose) {
        if (eventBean.closeOrOpen) {
            HiddenAnimUtils.newInstance(this, mVideoView, null, 200).toggle()
            LiveRoomHelper.mPIPManager?.pause()
        } else {
            HiddenAnimUtils.newInstance(this, mVideoView, null, 200).toggle()
            LiveRoomHelper.mPIPManager?.replay(true)
        }
    }

    //跳到mine并关闭页面
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun onClickMine(clickMine: HomeJumpToMineCloseLive) {
        backListener()
        RxBus.get().post(HomeJumpTo(clickMine.isOpenAct))
    }
    /**
     * Bet页面
     */

    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun toBet(clickMine: ToBetView) {
        backListener()
//        RxBus.get().post(WebSelect(clickMine.pos))
    }

    //动画效果
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun isShowAnim(eventBean: LiveAnimClose) {
        if (eventBean.closeOrOpen) {
            setVisible(svgaImage)
        } else setGone(svgaImage)
    }

    //关注接收
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun upDateAttention(eventBean: AnchorAttention) {
        if (eventBean.androidId == anchorId){
            changeAttention(eventBean.isFlow)
        }
    }


    @SuppressLint("SetTextI18n")
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun enter(eventBean: OnLineInfo) {
        if (LiveRoomHelper.bottomController.tvNum!=null){
            LiveRoomHelper.bottomController.tvNum.text = eventBean.online.toString()+" 人"
        }
    }

    /**
     * 礼物接收动画
     */
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun upDataAnim(eventBean: HomeLiveBigAnimatorBean) {
        if (UserInfoSp.getIsShowAnim()) {
            when (eventBean.gift_id) {
                "16" -> svgaUtils.startAnimator("烟花城堡", svgaImage)
                "15" -> svgaUtils.startAnimator("兰博基尼", svgaImage)
                "17" -> svgaUtils.startAnimator("凤凰机车", svgaImage)

                "21" -> svgaUtils.startAnimator("口红", svgaImage)
                "23" -> svgaUtils.startAnimator("LOVE", svgaImage)

                "29" -> svgaUtils.startAnimator("游艇一号", svgaImage)
                "28" -> svgaUtils.startAnimator("火凤凰", svgaImage)
                "27" -> svgaUtils.startAnimator("帝王花车", svgaImage)

                "51" -> svgaUtils.startAnimator("天灯祈福", svgaImage)
                "52" -> svgaUtils.startAnimator("新春大鼓", svgaImage)
                "53" -> svgaUtils.startAnimator("年年有余", svgaImage)
                "54" -> svgaUtils.startAnimator("鞭炮齐鸣", svgaImage)

                "41" -> svgaUtils.startAnimator("电动棒", svgaImage)
                "42" -> svgaUtils.startAnimator("黄瓜", svgaImage)
                "43" -> svgaUtils.startAnimator("茄子", svgaImage)
                "44" -> svgaUtils.startAnimator("皮鞭", svgaImage)
                "45" -> svgaUtils.startAnimator("滴蜡", svgaImage)
                "46" -> svgaUtils.startAnimator("为所欲为", svgaImage)
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        WindowPermissionCheck.onActivityResult(this, requestCode, resultCode, data, null)
    }


    override fun onResume() {
        super.onResume()
        LiveRoomHelper.mPIPManager?.resume()
    }


    override fun onPause() {
        super.onPause()
        LiveRoomHelper.mPIPManager?.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
    }


    override fun onBackPressed() {
        backListener()
        if (LiveRoomHelper.mPIPManager != null && LiveRoomHelper.mPIPManager?.onBackPress() == false) {
            if (LiveRoomHelper.videoView?.isPlaying == true) {
                if (WindowPermissionCheck.checkPermission(this)) {
                    LiveRoomHelper.mPIPManager?.startFloatWindow()
                    LiveRoomHelper.mPIPManager?.resume()
                    finish()
                } else {
                    LiveRoomHelper.mPIPManager?.forceReset()
                    finish()
                }
            } else {
                LiveRoomHelper.mPIPManager?.forceReset()
                super.onBackPressed()
            }
        }
    }
}