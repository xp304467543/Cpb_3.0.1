package com.home.live.children

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import com.customer.ApiRouter
import com.customer.component.dialog.DialogPassWord
import com.customer.component.dialog.DialogReCharge
import com.customer.component.dialog.DialogRedPaper
import com.customer.component.dialog.GlobalDialog
import com.customer.data.*
import com.customer.data.home.*
import com.customer.data.mine.MineApi
import com.customer.utils.JsonUtils
import com.customer.utils.ObjectAnimatorViw
import com.customer.wsmanager.WsManager
import com.customer.wsmanager.listener.WsStatusListener
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.home.live.LiveRoomChatPresenterHelper
import com.home.live.children.socket.SocketHelper
import com.home.live.children.util.LiveRoomVipEnterTask
import com.home.live.children.util.task.LiveRoomChatTask
import com.hwangjr.rxbus.RxBus
import com.lib.basiclib.base.mvp.BaseMvpPresenter
import com.lib.basiclib.utils.LogUtils
import com.lib.basiclib.utils.ToastUtils
import com.xiaojinzi.component.impl.Router
import cuntomer.api.WebUrlProvider
import kotlinx.android.synthetic.main.act_live_panel.recycleChat
import kotlinx.android.synthetic.main.fragmeent_live_child_1.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Response
import okio.ByteString
import org.json.JSONObject
import java.util.*
import java.util.concurrent.TimeUnit


/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/26
 * @ Describe
 *
 */
class LiveRoomChild1Presenter(private val anchorId: String) : BaseMvpPresenter<LiveRoomChild1>() {

    private val uiScope = CoroutineScope(Dispatchers.Main)


    //获取直播间信息
    fun getAllData(anchorId: String) {
        if (mView.isActive()) {
            uiScope.launch {
                val getTwentyNews = async {
                    HomeApi.getTwentyNews(anchorId)
                }
                val getTwentyNewsResult = getTwentyNews.await()
                getTwentyNewsResult.onSuccess {
                    mView.rvChatAdapter?.refresh(it)
                    if (mView.isVisible && mView.rvChatAdapter != null) mView.scrollToBottom(
                        mView.rvChatAdapter!!
                    )
                }
            }
        }
    }

    //获取礼物列表
    fun getGiftList() {
        HomeApi.getGiftList {
            if (mView.isActive()) {
                onSuccess { result ->
                    val json = JsonParser.parseString(result).asJsonObject
                    val typeList = json.get("typeList").asJsonObject
                    val data = json.get("data").asJsonObject
                    val type = arrayListOf<String>()
                    val content = ArrayList<List<HomeLiveGiftList>>()
                    repeat(typeList.size()) {
                        val realPosition = (it + 1).toString()
                        type.add(typeList.get(realPosition).asString)
                        val res = data.get(realPosition).asJsonArray
                        val bean = arrayListOf<HomeLiveGiftList>()
                        for (op in res) {
                            val beanData = JsonUtils.fromJson(op, HomeLiveGiftList::class.java)
                            bean.add(beanData)
                        }
                        content.add(bean)
                    }
                    if (mView.bottomGiftWindow != null) {
                        mView.bottomGiftWindow?.setData(type, content)
                    }
                }
                onFailed { }
            }
        }
    }


    //获取直播间红包队列
    fun homeLiveRedList(anchorId: String, isOpen: Boolean) {
        HomeApi.homeLiveRedList(anchorId) {
            if (mView.isActive()) {
                onSuccess {
                    if (it.isNullOrEmpty()) {
                        mView.stopRedAnimation()
                    } else {
                        mView.startRedAnimation()
                        mView.initRedDialog(it[it.size - 1], isOpen) //展示右下角红包
                    }
                }
                onFailed { ToastUtils.showToast(it.getMsg().toString()) }
            }
        }
    }

    //管理员清屏
    fun managerClear(anchor_id: String) {
        HomeApi.managerClear(anchor_id) {
            onSuccess {
                if (mWsManager?.isWsConnected!!) {
                    mWsManager?.sendMessage(LiveRoomChatPresenterHelper.getManagerCommend(anchor_id))
                } else {
                    mWsManager?.startConnect()
                    ToastUtils.showToast("请重试！")
                }
            }
            onFailed { ToastUtils.showToast("清屏失败") }
        }
    }

    //抢红包
    fun getRed(rid: String, redPaperDialog: DialogRedPaper) {
        mView.showPageLoadingDialog()
        HomeApi.homeGetRed(rid) {
            if (mView.isActive()) {
                onSuccess {
                    //开红包
                    mView.stopRedAnimation()
                    redPaperDialog.showGetRed(
                        it.send_user_name ?: "", it.send_text
                            ?: "恭喜发财", it.amount ?: "", it.send_user_avatar ?: ""
                    )
                    mView.hidePageLoadingDialog()
                }
                onFailed {
                    if (it.getCode() == 2) {
                        // 红包被抢完了
                        mView.stopRedAnimation()
                        val bean = JsonUtils.fromJson(
                            it.getDataCode().toString(),
                            HomeLiveRedReceiveBean::class.java
                        )
                        redPaperDialog.noGetRed(
                            bean.send_user_name ?: "", bean.send_text
                                ?: "恭喜发财", bean.send_user_avatar ?: ""
                        )
                        mView.hidePageLoadingDialog()
                    } else GlobalDialog.showError(
                        mView.requireActivity(),
                        it,
                        !mView.getScreenFull()
                    )
                }
            }
        }
    }

    // 送礼物
    fun homeLiveSendGift(
        anchorId: String,
        gift_id: String,
        gift_num: String,
        bean: HomeLiveAnimatorBean
    ) {
        HomeApi.setGift(UserInfoSp.getUserId(), anchorId, gift_id, gift_num) {
            if (mView.isActive()) {
                onSuccess {
                    //通知scoket
                    notifySocket(
                        LiveRoomChatPresenterHelper.getGifParams(
                            anchorId,
                            "1",
                            "",
                            bean.git_name,
                            "",
                            bean.giftCount,
                            "",
                            bean.gift_id,
                            bean.gift_icon
                        )
                    )
                    mView.showToast()
                    if (!mView.getScreenFull()) RxBus.get().post(UpDataHorDiamon(true))
                }
                onFailed {
                    if (it.getCode() == 2) {
                        val dia = DialogReCharge(mView.requireActivity(), true)
                        dia.setOnSendClickListener {
                            RxBus.get().post(HomeJumpToMineCloseLive(true))
                        }
                        dia.show()
                    } else GlobalDialog.showError(
                        mView.requireActivity(),
                        it,
                        !mView.getScreenFull()
                    )
                    mView.hidePageLoadingDialog()
                    if (mView.bottomGiftWindow != null) {
                        mView.bottomGiftWindow?.hideLoading()
                    }
                }
            }
        }
    }

    //查询是否设置支付密码
    fun getIsSetPayPassWord() {
        mView.showPageLoadingDialog()
        MineApi.getIsSetPayPass {
            onSuccess {
                mView.hidePageLoadingDialog()
                UserInfoSp.putIsSetPayPassWord(true)
                mView.sendRed()

            }
            onFailed {
                mView.hidePageLoadingDialog()
                GlobalDialog.showError(mView.requireActivity(), it)
            }
        }
    }


    //发红包
    fun homeLiveSendRedEnvelope(
        anchorId: String,
        amount: String,
        num: String,
        text: String,
        password: String,
        passWordDialog: DialogPassWord
    ) {
        HomeApi.homeLiveSendRedEnvelope(anchorId, amount, num, text, password) {
            onSuccess {
                //通知socket
                mWsManager?.sendMessage(
                    LiveRoomChatPresenterHelper.getGifParams(
                        anchorId,
                        "4",
                        it.rid,
                        "",
                        amount,
                        num,
                        text,
                        "",
                        ""
                    )
                )
                passWordDialog.showOrHideLoading()
                passWordDialog.dismiss()
                ToastUtils.showToast("红包发送成功")
            }
            onFailed {
                passWordDialog.showOrHideLoading()
                passWordDialog.dismiss()
                if (it.getCode() == 2) {
                    val dia = DialogReCharge(mView.requireActivity(), false)
                    dia.setOnSendClickListener {
                        RxBus.get().post(HomeJumpToMineCloseLive(true, isOpenAct = true))

                    }
                    dia.show()
                } else GlobalDialog.showError(mView.requireActivity(), it)

            }
        }
    }

    var mWsManager: WsManager? = null
    private var mTimer: Timer? = null
    private var mWsStatusListener: WsStatusListener? = null
    private var isReconnect = false

    /**
     * 释放wobSocket
     */
    fun stopConnect() {
        if (null != mWsManager) {
            mWsManager?.stopConnect()
            mWsManager = null
        }
    }

    /**
     * 发送一条聊天消息
     */
    fun sendMessage(content: String) {
        mWsManager?.sendMessage(SocketHelper.getPublishParams(anchorId, content))
    }

    /**
     * 分享注单
     */
    fun shareOrder(content: JSONObject?) {
        mWsManager?.sendMessage(SocketHelper.getPublishParams(anchorId, "", true, content))
    }

    /**
     * 通知socket
     */
    fun notifySocket(content: String) {
        mWsManager?.sendMessage(content)
    }

    //初始化socket
    fun startWebSocketConnect() {
        if (mView.isActive()) {
            mView.showPageLoadingDialog("聊天室连接中..")
            initStatusListener()
            mWsManager = WsManager.Builder(mView.requireActivity())
                .client(
                    OkHttpClient().newBuilder()
                        .pingInterval(1000 * 50, TimeUnit.SECONDS)
                        .retryOnConnectionFailure(true)
                        .build()
                )
                .needReconnect(true).wsUrl(WebUrlProvider.getBaseUrl())
                .build()
            mWsManager?.setWsStatusListener(mWsStatusListener)
            mWsManager?.startConnect()
        }
    }

    private fun initStatusListener() {
        mWsStatusListener = object : WsStatusListener() {
            override fun onOpen(response: Response) {
                super.onOpen(response)
                mView.hidePageLoadingDialog()
                LogUtils.d("WsManager-----onOpen response=$response")
                mWsManager?.sendMessage(SocketHelper.getSubscribeParams(isReconnect, anchorId))
                if (mTimer == null) mTimer = Timer()
                mTimer?.schedule(object : TimerTask() {
                    override fun run() {
                        mWsManager?.sendMessage(SocketHelper.getPingParams(anchorId))
                        LogUtils.d("WsManager-----发送了心跳")
                    }
                }, 0, 1000 * 54)
            }

            override fun onMessage(text: String) {
                super.onMessage(text)
                if (mView.isActive()) initChatText(text)
                LogUtils.e("WsManager-----c$text")
            }

            override fun onMessage(bytes: ByteString) {
                super.onMessage(bytes)
                LogUtils.d("WsManager-----onMessage$bytes")
            }

            override fun onReconnect() {
                super.onReconnect()
                isReconnect = true
                LogUtils.d("WsManager-----onReconnect")

            }

            override fun onClosing(code: Int, reason: String) {
                super.onClosing(code, reason)
                LogUtils.d("WsManager-----onClosing")
            }

            override fun onClosed(code: Int, reason: String) {
                super.onClosed(code, reason)
                LogUtils.d("WsManager-----onClosed")
            }

            override fun onFailure(t: Throwable?, response: Response?) {
                super.onFailure(t, response)
                LogUtils.d("WsManager-----onFailure$response=$t")
                if (mTimer != null) {
                    mTimer?.cancel()
                    mTimer = null
                }
            }
        }
    }

    var showBean = HomeLiveTwentyNewsResponse()

    //信息接收
    @SuppressLint("SetTextI18n")
    private fun initChatText(text: String) {
        val data =
            WebUrlProvider.getData<HomeLiveChatBeanNormal>(text, HomeLiveChatBeanNormal::class.java)
        LogUtils.e("WsManager-----111onMessage${text}")
        if (data != null) {
            if (mView.isActive()) {
                when (data.type) {
                    //聊天内容
                    LiveRoomChatPresenterHelper.TYPE_PUBLISH -> {
                        if (data.event == "pushPlan") {
                            val res = HomeLiveTwentyNewsResponse(
                                data.type,
                                data.room_id,
                                data.user_id,
                                "",
                                data.text,
                                data.vip,
                                data.userType,
                                data.avatar,
                                data.sendTime,
                                data.sendTimeTxt,
                                event = data.event ?: "",
                                orders = data.orders,
                                canFollow = true
                            )
                            mView.rvChatAdapter?.add(res)
                        } else {
                            val res = HomeLiveTwentyNewsResponse(
                                data.type, data.room_id, data.user_id,
                                data.userName ?: "", data.text, data.vip,
                                data.userType, data.avatar, data.sendTime,
                                data.sendTimeTxt
                            )
                            mView.rvChatAdapter?.add(res)
                            RxBus.get().post(
                                DanMu(
                                    data.userName.toString(),
                                    data.userType.toString(),
                                    data.text.toString(),
                                    data.vip.toString(),
                                    data.isMe
                                )
                            )
                        }
                        scrollBottom()//滑动到底部
                    }
                    //管理员清屏
                    LiveRoomChatPresenterHelper.TYPE_COMMEND -> if (data.commend == "clear") mView.rvChatAdapter?.clear()

                    //进场提示
                    LiveRoomChatPresenterHelper.TYPE_SUBSCRIBE -> {
                        if (!data.isMe) {
                            //普通进场
                            if (data.vip == "0" || data.vip == "") {
                                val text = "欢迎 " + data.userName + " 进入直播间"
                                mView.tvEnterContent.text = text
//                                setVip(data.vip ?: "0", mView.imgEnterImg)
                                ObjectAnimatorViw.setShowAnimation(mView.linEnter, 1000)
                                Timer().schedule(object : TimerTask() {
                                    override fun run() {
                                        if (mView.isVisible && mView.linEnter != null) {
                                            mView.linEnter.post {
                                                ObjectAnimatorViw.setHideAnimation(
                                                    mView.linEnter,
                                                    1000
                                                )
                                            }
                                        }
                                        this.cancel()
                                    }
                                }, 5000)
                            } else {
                                //vip进场 进入vip队列
                                mView.context?.let {
                                    LiveRoomVipEnterTask(
                                        it,
                                        data.vip + "," + data.userName,
                                        mView.tvVipEnter
                                    ).enqueue()
                                }
                            }
                        }
                    }
                    //礼物
                    LiveRoomChatPresenterHelper.TYPE_GIFT -> {
                        if (data.type == "gift") {
                            when (//礼物
                                data.gift_type) {
                                "1" -> {
                                    showAnim(data)//显示动画
                                    if (data.sendTime - showBean.sendTime <= 10 && data.gift_id == showBean.gift_id && data.user_id == showBean.user_id && data.gift_num == showBean.gift_num) {
                                        for ((index, res) in mView.rvChatAdapter?.data?.withIndex()!!) {
                                            if (res.sendTime == showBean.sendTime) {
                                                showBean = HomeLiveTwentyNewsResponse(
                                                    res.type,
                                                    gift_name = res.gift_name,
                                                    userType = res.userType,
                                                    gift_num = res.gift_num,
                                                    icon = res.icon,
                                                    userName = res.userName,
                                                    gift_id = res.gift_id,
                                                    vip = res.vip,
                                                    user_id = res.user_id,
                                                    sendTime = res.sendTime,
                                                    final_num = res.final_num + 1
                                                )
                                                mView.rvChatAdapter?.refresh(index, showBean)
                                            }
                                        }
                                    } else {
                                        showBean = HomeLiveTwentyNewsResponse(
                                            data.type,
                                            gift_name = data.gift_name,
                                            userType = data.userType,
                                            gift_num = data.gift_num,
                                            icon = data.icon,
                                            userName = data.userName
                                                ?: "",
                                            gift_id = data.gift_id,
                                            vip = data.vip,
                                            user_id = data.user_id,
                                            sendTime = data.sendTime,
                                            final_num = 1
                                        )
                                        mView.rvChatAdapter?.add(showBean)
                                    }
                                    scrollBottom()
                                }

                                //红包
                                "4" -> {
                                    val bean = HomeLiveTwentyNewsResponse(
                                        gift_type = data.gift_type,
                                        gift_num = data.gift_num,
                                        userName = data.userName
                                            ?: "",
                                        vip = data.vip,
                                        gift_price = data.gift_price,
                                        userType = data.userType
                                    )
                                    mView.rvChatAdapter?.add(bean)
                                    mView.startRedAnimation()
                                    val result = HomeLiveRedRoom(
                                        id = data.r_id,
                                        text = data.gift_text.toString(),
                                        userName = data.userName
                                            ?: "",
                                        avatar = data.avatar.toString()
                                    )
                                    mView.initRedDialog(result, true)
                                }
                            }
                        }
                        scrollBottom()
                    }
                    //主播长龙 队列
                    LiveRoomChatPresenterHelper.TYPE_SERVER_PUSH -> {
                        if (data.data == null) return
                        when (data.dataType) {
                            "long_dragon" -> for ((index, res) in data.data?.asJsonArray!!.withIndex()) {
                                val bean = Gson().fromJson(res, HomeLiveChatChildBean::class.java)
                                if (mView.isVisible) {
                                    if (index % 2 == 0) {
                                        LiveRoomChatTask(
                                            mView.requireContext(),
                                            bean,
                                            mView.tvAnchorOpenPrise
                                        ).enqueue()
                                    } else LiveRoomChatTask(
                                        mView.requireContext(),
                                        bean,
                                        mView.tvAnchorOpenPrise2
                                    ).enqueue()
                                    //                                                .setDuration(5000) //设置了时间，代表这个任务时间是确定的，如果不确定，则不用设置
                                    //                                                .setPriority(TaskPriority.DEFAULT) //设置优先级，默认是DEFAULT
                                    //                                                .enqueue(); //入队
                                }
                            }
                            "update_online" -> {
                                RxBus.get()
                                    .post(OnLineInfo(data.data!!.asJsonObject.get("online").asInt))
                            }
                            "push_open_issue" -> {
                                val res = data.data!!.asJsonArray
                                for (result in res) {
                                    if (result.asJsonObject.get("end_sale_time").asLong <= 0) {
                                        val issue = result.asJsonObject.get("issue").asString
                                        if ((mView.rvChatAdapter?.followList != null)) {
                                            try {
                                                repeat(mView.rvChatAdapter?.followList?.size ?: 0) {
                                                    val pos =
                                                        mView.rvChatAdapter?.followList?.get(it)
                                                            ?.split(",")?.get(1)?.toInt()
                                                    val posStr =
                                                        mView.rvChatAdapter?.followList?.get(it)
                                                            ?.split(",")?.get(0)
                                                    if (issue == posStr) {
                                                        mView.rvChatAdapter?.getItem(
                                                            pos ?: 0
                                                        )?.canFollow = false
                                                        mView.rvChatAdapter?.notifyItemChanged(pos!!)
                                                        mView.rvChatAdapter?.followList?.removeAt(
                                                            pos!!
                                                        )
                                                    }
                                                }

                                            } catch (e: Exception) {

                                            }
                                        }
                                    } else {
                                        val issue = result.asJsonObject.get("issue").asString
                                        if ((mView.rvChatAdapter?.followList != null)) {
                                            repeat(mView.rvChatAdapter?.followList?.size ?: 0) {
                                                try {
                                                    val pos =
                                                        mView.rvChatAdapter?.followList?.get(it)
                                                            ?.split(",")?.get(1)?.toInt()
                                                    val posStr =
                                                        mView.rvChatAdapter?.followList?.get(it)
                                                            ?.split(",")?.get(0)
                                                    if (issue == posStr) {
                                                        mView.rvChatAdapter?.getItem(pos!!)?.canFollow =
                                                            true
                                                        mView.rvChatAdapter?.notifyItemChanged(pos!!)
                                                        mView.rvChatAdapter?.followList?.removeAt(
                                                            pos!!
                                                        )
                                                    }
                                                } catch (e: Exception) {

                                                }
                                            }
                                        }
                                    }
                                }

                            }
                        }
                    }
                    LiveRoomChatPresenterHelper.TYPE_ERROE -> {
                        ToastUtils.showToast(data.msg)
                    }
                }
            }
        }
    }

    private fun showAnim(data: HomeLiveChatBeanNormal) {
        val bean = HomeLiveAnimatorBean(
            data.gift_id.toString(),
            data.gift_name.toString(),
            data.icon.toString(),
            data.user_id.toString(),
            data.avatar.toString(),
            data.userName ?: "",
            data.gift_num.toString()
        )
        mView.showAnim(bean)
        val ben = HomeLiveBigAnimatorBean(
            data.gift_id.toString(),
            data.gift_name.toString(),
            data.icon.toString(),
            data.user_id.toString(),
            data.avatar.toString(),
            data.userName ?: "",
            data.gift_num.toString()
        )
        RxBus.get().post(ben)

    }

    private fun scrollBottom() {
        if (mView.isSlideToBottom(mView.recycleChat)) {
            mView.rvChatAdapter?.let { mView.scrollToBottom(it) }
        } else {
            mView.tvMoreInfo.visibility = View.VISIBLE
        }
    }

    fun setVip(vip: String, imageView: ImageView) {
//        val id = when (vip) {
//            "1" -> R.mipmap.v1
//            "2" -> R.mipmap.v2
//            "3" -> R.mipmap.v3
//            "4" -> R.mipmap.v4
//            "5" -> R.mipmap.v5
//            "6" -> R.mipmap.v6
//            "7" -> R.mipmap.v7
//            else -> 0
//        }
//        if (id != 0) {
//            mView.setVisible(mView.imgEnterImg)
//            imageView.setImageResource(id)
//            mView.setVisible(mView.tvInput)
//            mView.setGone(mView.tvVipCan)
//        } else {
//            mView.setGone(mView.tvInput)
//            mView.setVisible(mView.tvVipCan)
//            mView.setGone(mView.imgEnterImg)
//        }
    }

}