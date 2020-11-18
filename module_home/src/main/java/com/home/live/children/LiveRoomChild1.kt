package com.home.live.children

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.customer.ApiRouter
import com.customer.base.BaseNormalFragment
import com.customer.component.dialog.*
import com.customer.component.dialog.live.BottomGiftWindow
import com.customer.component.gift.RewardAdapter
import com.customer.component.gift.bean.SendGiftBean
import com.customer.component.input.InputPopWindow
import com.customer.component.spanlite.SpanBuilder
import com.customer.component.spanlite.SpanLite
import com.customer.data.*
import com.customer.data.home.HomeLiveAnimatorBean
import com.customer.data.home.HomeLiveRedRoom
import com.customer.data.mine.MineApi
import com.customer.data.mine.MinePassWordTime
import com.customer.utils.JsonUtils
import com.customer.utils.ObjectAnimatorViw
import com.home.R
import com.home.live.bet.new.LiveRoomBetFragment
import com.home.live.bet.old.LotteryShareBet
import com.home.live.children.adapter.LiveRoomChatAdapter
import com.home.live.children.util.task.AnimUtils
import com.hwangjr.rxbus.RxBus
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.thread.EventThread
import com.lib.basiclib.utils.FastClickUtil
import com.lib.basiclib.utils.ToastUtils
import com.lib.basiclib.utils.ViewUtils
import com.player.ali.base.util.NetWatchdog
import com.xiaojinzi.component.impl.Router
import kotlinx.android.synthetic.main.fragmeent_live_child_1.*
import java.util.*


/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/26
 * @ Describe
 *
 */
class LiveRoomChild1 : BaseNormalFragment<LiveRoomChild1Presenter>() {

    var anchorId = "-1"
    var lotteryId = "-1"
    var linearLayoutManager: LinearLayoutManager? = null

    //网络状态监听
    private var mNetWatchdog: NetWatchdog? = null

    var rvChatAdapter: LiveRoomChatAdapter? = null

    private var inputPopWindow: InputPopWindow? = null

    var isShowPop: Boolean = false
    var popMenuManager: LiveRoomPop? = null

    override fun attachView() = mPresenter.attachView(this)

    override fun isRegisterRxBus() = true

    override fun attachPresenter() =
        LiveRoomChild1Presenter(arguments?.getString("LIVE_ROOM_ANCHOR_ID", "-1") ?: "0")

    override fun getLayoutRes() = R.layout.fragmeent_live_child_1

    override fun initContentView() {
        initIntent()
        initSocket()
        initChat()
        initParams()
        if (UserInfoSp.getOpenWindow()) setVisible(floatButton) else setGone(floatButton)
    }

    private fun initParams() {
        val pair2 = tvMoreInfo.layoutParams as ConstraintLayout.LayoutParams
        val height2 = (ViewUtils.getScreenHeight() - ViewUtils.dp2px(42)) / 2
        pair2.setMargins(0, height2, 0, 0)
        tvMoreInfo.layoutParams = pair2
    }

    private fun initIntent() {
        anchorId = arguments?.getString("LIVE_ROOM_ANCHOR_ID", "-1") ?: "0"
        lotteryId = arguments?.getString("LIVE_ROOM_LOTTERY_ID", "-1") ?: "0"
//        if ( arguments?.getString("LIVE_ROOM_ANCHOR_STATUE", "-1") ?: "0" == "1")
        setGone(tvNoInput)
        upDateSystemNotice()
    }

    override fun initData() {
        mPresenter.getAllData(anchorId)
        mPresenter.homeLiveRedList(anchorId, false)
    }


    // ========= 公告 =========
    @SuppressLint("SetTextI18n")
    private fun upDateSystemNotice() {
        val name = arguments?.getString("LIVE_ROOM_NICK_NAME") ?: "0"
        if (TextUtils.isEmpty(name)) mtvLiveRoom.text = "暂无公告" else mtvLiveRoom.text =
            "欢迎来到 $name 的直播间,喜欢就点关注吧。"
        mtvLiveRoom.setTextColor(getColor(R.color.color_333333))
    }

    private fun initChat() {
        chartSmartRefreshLayout.setEnableRefresh(false)//是否启用下拉刷新功能
        chartSmartRefreshLayout.setEnableLoadMore(false)//是否启用上拉加载功能
        chartSmartRefreshLayout.setEnableOverScrollBounce(true)//是否启用越界回弹
        chartSmartRefreshLayout.setEnableOverScrollDrag(true)//是否启用越界拖动（仿苹果效果）
        rvChatAdapter =
            context?.let { fragmentManager?.let { it1 -> LiveRoomChatAdapter(it, it1) } }
        linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recycleChat.layoutManager = linearLayoutManager
        recycleChat.adapter = rvChatAdapter
        initAnim()

    }

    override fun initEvent() {
        initRvScoldListen()
        //游戏
        imgBuyLotteryQp.setOnClickListener {
            if (!UserInfoSp.getIsLogin()) {
                GlobalDialog.notLogged(requireActivity())
                return@setOnClickListener
            }
            RxBus.get().post(ToBetView(2))
        }
        //投注
        imgBuyLottery.setOnClickListener {
            if (!UserInfoSp.getIsLogin()) {
                GlobalDialog.notLogged(requireActivity())
                return@setOnClickListener
            }
            RxBus.get().post(ToBetView(1))
        }
        //存款
        imgRecharge.setOnClickListener {
            if (!UserInfoSp.getIsLogin()) {
                GlobalDialog.notLogged(requireActivity())
                return@setOnClickListener
            }
            if (!FastClickUtil.isFastClick()) Router.withApi(ApiRouter::class.java)
                .toMineRecharge(0)
        }
        //礼物
        imgGift.setOnClickListener {
            if (!UserInfoSp.getIsLogin()) {
                GlobalDialog.notLogged(requireActivity())
                return@setOnClickListener
            }
            initGitWidow()
        }
        //键盘
        tvInput.setOnClickListener {
            showInput()
        }
        //底部有新消息
        tvMoreInfo.setOnClickListener {
            rvChatAdapter?.let { it1 -> scrollToBottom(it1) }
        }
        //红包
        imgRed.setOnClickListener {
            if (!UserInfoSp.getIsLogin()) {
                GlobalDialog.notLogged(requireActivity())
                return@setOnClickListener
            }
            if (!UserInfoSp.getIsSetPayPassWord()) {
                mPresenter.getIsSetPayPassWord()
            } else sendRed()
        }
        //悬浮按钮
        floatButton.setOnClickListener {
            showOrCloseFloatView()
        }

        imgShake.setOnClickListener {
            if (!UserInfoSp.getIsLogin()) {
                GlobalDialog.notLogged(requireActivity())
                return@setOnClickListener
            }
            if (!FastClickUtil.isFastClick()) {
                liveRoomBetFragment = LiveRoomBetFragment.newInstance(lotteryId)
                fragmentManager?.let { it1 ->
                    liveRoomBetFragment?.show(
                        it1,
                        "LiveRoomBottomBetFragment"
                    )
                }
            } else ToastUtils.showToast("请勿重复点击")
//            context?.let { it1 -> DialogLiveRoomBet(it1).show() }
        }

        imgMouse.setOnClickListener {
            if (!FastClickUtil.isFastClick()) {
                startActivity(Intent(activity, LiveRoomPostCardAct::class.java))
            }
        }

        imgGame.setOnClickListener {
            if (!FastClickUtil.isFastClick()) {
                if (!UserInfoSp.getIsLogin()) {
                    GlobalDialog.notLogged(requireActivity())
                    return@setOnClickListener
                }
                RxBus.get().post(ToBetView(2))
            }
        }
    }

    private var liveRoomBetFragment: LiveRoomBetFragment? = null

    //滑动监听
    private fun initRvScoldListen() {
        recycleChat.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            //用来标记是否正在向最后一个滑动
            var isSlidingToLast = false

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!recyclerView.canScrollVertically(1)) {
                    if (tvMoreInfo.visibility == View.VISIBLE) setGone(tvMoreInfo)
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                //dx用来判断横向滑动方向，dy用来判断纵向滑动方向
                isSlidingToLast = dx > 0
            }

        })
    }

    /**
     * 发红包弹框
     */
    fun sendRed() {
        val dialog = DialogSendRed(requireActivity(), false)
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
        val passWordDialog =
            context?.let { DialogPassWord(it, ViewUtils.getScreenWidth(), ViewUtils.dp2px(156)) }
        passWordDialog?.setTextWatchListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s?.length == 6) {
                    passWordDialog.showOrHideLoading()
                    //验证支付密码
                    MineApi.verifyPayPass(s.toString()) {
                        onSuccess {
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
                            passWordDialog.showOrHideLoading()
                            if (it.getCode() == 1002) {
                                passWordDialog.showTipsText(
                                    it.getMsg().toString() + "," + "您还有" +
                                            JsonUtils.fromJson(
                                                it.getDataCode().toString(),
                                                MinePassWordTime::class.java
                                            ).remain_times.toString() +
                                            "次机会"
                                )
                                passWordDialog.clearText()
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
        passWordDialog?.show()
    }


    /**
     * 礼物弹框
     */
    var bottomGiftWindow: BottomGiftWindow? = null
    private fun initGitWidow() {
        if (bottomGiftWindow == null) {
            bottomGiftWindow = BottomGiftWindow(requireActivity())
            bottomGiftWindow?.show()
            mPresenter.getGiftList()
        } else bottomGiftWindow?.show()
        MineApi.getUserDiamond {
            onSuccess { bottomGiftWindow?.setDiamond(it.diamond) }
            onFailed { GlobalDialog.showError(this@LiveRoomChild1.requireActivity(), it) }
        }
    }

    //红包动画
    fun startRedAnimation() {
        if (liveRedTips != null) {
            setVisible(liveRedTips)
            val animator = ObjectAnimatorViw.nope(liveRedTips, 3f)
            animator.repeatCount = ValueAnimator.INFINITE
            animator.start()
            RxBus.get().post(RedTips(true))
        }
    }

    //隐藏小红包和动画
    fun stopRedAnimation() {
        if (liveRedTips != null && liveRedTips.animation != null) {
            liveRedTips.clearAnimation()
        }
        setGone(liveRedTips)
        RxBus.get().post(RedTips(false))
    }


    /**
     * 有红包
     */
    private var showIt: Boolean = false
    private var homeLiveRedRoomBean: HomeLiveRedRoom? = null
    private var mOpenRedPopup: DialogRedPaper? = null
    fun initRedDialog(homeLiveRedRoom: HomeLiveRedRoom, isOpen: Boolean) {
        if (isActive()) {
            homeLiveRedRoomBean = homeLiveRedRoom
            if (mOpenRedPopup == null) {
                mOpenRedPopup = context?.let { DialogRedPaper(it) }
            }
            //抢红包
            mOpenRedPopup?.setOnOpenClickListener {
                showIt = true
                mPresenter.getRed(homeLiveRedRoom.id.toString(), mOpenRedPopup!!)
            }
            mOpenRedPopup?.setOnDismissListener {
                if (showIt) {
                    mPresenter.homeLiveRedList(anchorId, true)
                    showIt = false
                }
//            if (isFullScreen()) {
//                val decorView = ScreenUtils.getDecorView(getPageActivity())
//                if (decorView != null) ScreenUtils.hideSysBar(getPageActivity(), decorView)
//            }
            }
            //点击小红包
            liveRedTips?.setOnClickListener {
                if (!FastClickUtil.isFastClick()) {
                    if (!UserInfoSp.getIsLogin()) {
                        GlobalDialog.notLogged(requireActivity())
                        return@setOnClickListener
                    }
                    try {
                        if (UserInfoSp.getVipLevel()?.toInt() ?: 0 > 0) {
                            mOpenRedPopup?.show()
                        } else {
                            val dialog = context?.let { it1 -> DialogVipTips(it1) }
                            dialog?.setCanCalClickListener {
                                if (!FastClickUtil.isFastClickSmall()) {
                                    Router.withApi(ApiRouter::class.java).toMineRecharge(0)
                                }
                            }
                            dialog?.setConfirmClickListener {
                                if (!FastClickUtil.isFastClickSmall()) {
                                    RxBus.get().post(HomeJumpToMineCloseLive(true))
                                }
                            }
                            dialog?.show()
                        }
                    } catch (e: Exception) {

                    }

                }
            }
            if (isOpen) {
                if (mOpenRedPopup?.isShowing == false) mOpenRedPopup?.show()
            }
        }
    }

    /**
     * 初始化socket
     */
    private fun initSocket() {
        mNetWatchdog = NetWatchdog(context)
        mNetWatchdog?.setNetChangeListener(object : NetWatchdog.NetChangeListener {
            override fun on4GToWifi() {}
            override fun onNetDisconnected() {
                mPresenter.stopConnect()
            }

            override fun onWifiTo4G() {}

        })
        mNetWatchdog?.setNetConnectedListener(object : NetWatchdog.NetConnectedListener {
            override fun onReNetConnected(isReconnect: Boolean) {
                mPresenter.startWebSocketConnect()
            }

            override fun onNetUnConnected() {
                mPresenter.stopConnect()
            }
        })
        mNetWatchdog?.startWatch()
    }

    //输入框
    private fun showInput(text: String = "") {
        if (inputPopWindow == null) {
            inputPopWindow = this.activity?.let { it1 -> InputPopWindow(it1) }
            inputPopWindow?.setOnTextSendListener {
                mPresenter.sendMessage(it)
                inputPopWindow?.dismiss()
            }
        }
        if (!TextUtils.isEmpty(text)) inputPopWindow?.editTextPanel?.setText(text)
        inputPopWindow?.showAtLocation(
            this.activity?.window?.decorView?.rootView,
            Gravity.NO_GRAVITY,
            0,
            0
        )
        inputLayout.postDelayed(
            {
                if (inputPopWindow != null) {
                    setGone(imgThree)
                    setGone(inputLayout)
                    inputPopWindow?.showKeyboard()
                }
            }, 50
        )
        inputPopWindow?.setOnDismissListener {
            setVisible(imgThree)
            setVisible(inputLayout)
        }
    }

    //拖拽按钮
    private fun showOrCloseFloatView() {
        if (popMenuManager == null) {
            popMenuManager = context?.let { LiveRoomPop(it) }
            popMenuManager?.setSendClickListener {
                if (it) {
                    //收起
                    RxBus.get().post(LiveVideoClose(it))
                } else {
                    //展开
                    RxBus.get().post(LiveVideoClose(it))
                }
            }
            //清屏
            popMenuManager?.setClearClickListener {
                rvChatAdapter?.clear()
                rvChatAdapter?.notifyDataSetChanged()
            }
            //管理清屏
            popMenuManager?.setManagerClearClickListener {
                mPresenter.managerClear(anchorId)
            }

        }
        isShowPop = if (!isShowPop) {
            popMenuManager!!.showAtLocationBottom(findView(R.id.floatButton), 5f)
            true
        } else {
            popMenuManager!!.dismiss()
            false
        }


    }


    //首冲
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun isFirstRecharge(eventBean: IsFirstRecharge) {
        UserInfoSp.putIsFirstRecharge(eventBean.res)
        val par = imgRecharge.layoutParams as LinearLayout.LayoutParams
        if (eventBean.res) {
            imgRecharge.setBackgroundResource(R.mipmap.ic_live_first_recharge)
            par.height = ViewUtils.dp2px(25)
            imgRecharge.layoutParams = par
            val animator = ObjectAnimatorViw.tada(imgRecharge, 1f)
            animator.repeatCount = ValueAnimator.INFINITE
            animator.start()
        } else {
            imgRecharge.clearAnimation()
            imgRecharge.setBackgroundResource(R.mipmap.ic_live_chat_recharge)
            par.height = ViewUtils.dp2px(25)
            par.width = ViewUtils.dp2px(20)
            imgRecharge.layoutParams = par
        }
    }

    /**
     * 滑动到最底部
     */
    fun scrollToBottom(multiTypeAdapter: LiveRoomChatAdapter) {
        linearLayoutManager?.scrollToPosition(multiTypeAdapter.itemCount - 1)
        setGone(tvMoreInfo)
    }

    fun showToast() {
        if (!getScreenFull()) {
            ToastUtils.showToast("发送成功")
        }
        if (bottomGiftWindow != null) {
            MineApi.getUserDiamond {
                onSuccess {
                    bottomGiftWindow?.setDiamond(it.diamond)
                    bottomGiftWindow?.hideLoading()
                }
            }
        }
    }

    /**
     * 横幅接收动画
     */
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun upDataMineUserDiamond(eventBean: HomeLiveAnimatorBean) {
        mPresenter.homeLiveSendGift(anchorId, eventBean.gift_id, eventBean.giftCount, eventBean)
    }


    private fun initAnim() {
        //初始化礼物界面
        rewardLayout.setGiftItemRes(R.layout.gift_animation_item)
        rewardLayout.setGiftAdapter(RewardAdapter(requireActivity()))
    }

    /**
     * 横幅接收动画
     */
    fun showAnim(eventBean: HomeLiveAnimatorBean) {
        val time: Long
        //展示3秒
        when (eventBean.gift_id) {
            "18",
            "19",
            "20",
            "22",
            "24",
            "25",
            "26",
            "30",
            "31",
            "32",
            "33",
            "47",
            "48",
            "49",
            "50" -> time = 3000
            "16",     //烟花城堡
            "17",    //凤凰机车
            "21",   //口红
            "28",  //火凤凰
            "29"  //游艇
            -> time = 5000

            else -> time = 8000
        }
        try {
            val bean = SendGiftBean(
                eventBean.user_id.toInt(), eventBean.gift_id.toInt(),
                eventBean.user_name, eventBean.git_name, eventBean.gift_icon,
                eventBean.user_icon, eventBean.giftCount, time
            )
            rewardLayout.put(bean)
        } catch (e: Exception) {
            e.printStackTrace()
            ToastUtils.showToast(e.toString())
        }
    }


    /**
     * 是否在最底部
     */
    fun isSlideToBottom(recyclerView: RecyclerView?): Boolean {
        if (recyclerView == null) return false
        return (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange())
    }

    //红包
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun gift(eventBean: Gift) {
        eventBean.gift?.let { mPresenter.notifySocket(it) }
    }

    //横屏发送弹幕
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun sendDanMu(eventBean: SendDanMu) {
        mPresenter.sendMessage(eventBean.content)
    }

    // @某个人
    @SuppressLint("SetTextI18n")
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun call(eventBean: LiveCallPersonal) {
        if (!UserInfoSp.getIsLogin()) {
            GlobalDialog.notLogged(requireActivity())
        } else showInput("@" + eventBean.name + " ")

    }

    //分享注单
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun shareBet(eventBean: LotteryShareBet) {
        if (eventBean.reset) {
            mPresenter.shareOrder(eventBean.order)
            RxBus.get().post(LotteryResetDiamond(true))
        }
    }

    override fun onPause() {
        super.onPause()
        if (rewardLayout != null) {
            rewardLayout.onPause()
        }
    }

    override fun onResume() {
        super.onResume()
        if (rewardLayout != null) {
            rewardLayout.onResume()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.stopConnect()
        mNetWatchdog?.stopWatch()
        mPresenter.stopConnect()
    }

    fun getScreenFull(): Boolean {
        return this.activity?.requestedOrientation == Configuration.ORIENTATION_PORTRAIT
    }

    /**
     * 进场 vip 或者普通提醒
     */

    @SuppressLint("SetTextI18n")
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun enter(eventBean: EnterVip) {
        mPresenter.setVip(eventBean.vip, imgEnterImg)
        if (eventBean.vip == "0") {
            tvEnterContent.text = "欢迎 " + UserInfoSp.getUserNickName() + " 进入直播间"
            ObjectAnimatorViw.setShowAnimation(linEnter, 1000)
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    if (isVisible && linEnter != null) {
                        linEnter.post { ObjectAnimatorViw.setHideAnimation(linEnter, 1000) }
                    }
                    this.cancel()
                }
            }, 3000)
            setGone(tvInput)
            setVisible(tvVipCan)
        } else {
            setVisible(tvInput)
            setGone(tvVipCan)
            SpanLite.with(tvVipEnter).append(
                SpanBuilder.Builder("VIP" + eventBean.vip).drawTextColor("#FF513E")
                    .drawTypeFaceBold().drawTypeFaceItalic().build()
            )
                .append(SpanBuilder.Builder(" 贵族 ").drawTextColor("#333333").build())
                .append(
                    SpanBuilder.Builder(UserInfoSp.getUserNickName()).drawTextColor("#D3904E")
                        .build()
                )
                .append(SpanBuilder.Builder(" 驾临直播间 ").drawTextColor("#333333").build()).active()
            AnimUtils.getInAnimation(context!!, tvVipEnter)
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    if (view != null && tvVipEnter != null) {
                        tvVipEnter?.post { AnimUtils.getOutAnimation(context!!, tvVipEnter) }
                        this.cancel()
                    }
                }
            }, 5000)
        }
    }


    companion object {
        fun newInstance(
            anchorId: String,
            liveState: String,
            name: String,
            id: String,
            type: String
        ): LiveRoomChild1 {
            val fragment = LiveRoomChild1()
            val bundle = Bundle()
            bundle.putString("LIVE_ROOM_ANCHOR_ID", anchorId)
            bundle.putString("LIVE_ROOM_ANCHOR_STATUE", liveState)
            bundle.putString("LIVE_ROOM_NICK_NAME", name)
            bundle.putString("LIVE_ROOM_LOTTERY_ID", id)
            bundle.putString("LIVE_ROOM_LOTTERY_TYPE", type)
            fragment.arguments = bundle
            return fragment
        }
    }

}