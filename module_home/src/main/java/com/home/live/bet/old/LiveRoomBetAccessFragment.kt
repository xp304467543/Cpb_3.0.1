package com.home.live.bet.old

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.customer.component.dialog.DialogGlobalTips
import com.customer.component.dialog.LoadingDialog
import com.customer.data.HomeJumpToMineCloseLive
import com.customer.data.LotteryResetDiamond
import com.customer.data.UserInfoSp
import com.customer.data.lottery.BetBean
import com.customer.data.lottery.BetShareBean
import com.customer.data.lottery.LotteryApi
import com.customer.data.mine.MineApi
import com.customer.utils.AESUtils
import com.google.gson.GsonBuilder
import com.home.R
import com.hwangjr.rxbus.RxBus
import com.lib.basiclib.utils.LogUtils
import com.lib.basiclib.utils.ToastUtils
import org.json.JSONObject
import java.math.BigDecimal

/**
 *
 * @ Author  QinTian
 * @ Date  2020/4/24
 * @ Describe 投注确认
 *
 */

class LiveRoomBetAccessFragment : BottomDialogFragment() {

    private var totalMoney = 0L

    private var UserDiamond = BigDecimal(0.00)

    private var UserBalance = BigDecimal(0.00)

    private var liveRoomBetAccessAdapter: LiveRoomBetAccessAdapter? = null

    private var rvBetAccess: RecyclerView? = null

    private var tvTotalDiamond: TextView? = null

    private var tvBetAccessSubmit: TextView? = null

    private var loadingDialog: LoadingDialog? = null

    private var orderMap: HashMap<String, Any>? = null


    override val layoutResId: Int = R.layout.old_dialog_fragment_bet_access

    override val resetHeight: Int = com.lib.basiclib.utils.ViewUtils.getScreenHeight() * 2 / 3 - com.lib.basiclib.utils.ViewUtils.dp2px(160)

    override fun isShowTop() = false

    override fun canceledOnTouchOutside() = true

    override fun initView() {
        rvBetAccess = rootView?.findViewById(R.id.rvBetAccess)
        tvTotalDiamond = rootView?.findViewById(R.id.tvTotalDiamond)
        tvBetAccessSubmit = rootView?.findViewById(R.id.tvBetAccessSubmit)
        initLoading()
    }

    override fun initData() {
        if (arguments?.getString("diamond") == "0x11123") {
            if (arguments?.getBoolean("isBalanceBet") ==false) {
                getUserDiamond()
            } else getUserBalance()
        } else {
            if (arguments?.getBoolean("isBalanceBet") ==false) {
                UserDiamond = (arguments?.getString("diamond") ?: "0").toBigDecimal()
            } else {
                UserBalance = (arguments?.getString("totalBalance") ?: "0").toBigDecimal()
            }
        }
        val dataList = arguments?.getParcelableArrayList<LotteryBet>("lotteryBet")
        if (dataList.isNullOrEmpty()) return
        liveRoomBetAccessAdapter = context?.let {
            LiveRoomBetAccessAdapter(it, arguments?.getBoolean("isBalanceBet")?:false)
        }
        rvBetAccess?.adapter = liveRoomBetAccessAdapter
        rvBetAccess?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        liveRoomBetAccessAdapter?.refresh(dataList)
        totalMoney = (arguments?.getInt("totalDiamond") ?: 0).toLong()
        if (arguments?.getBoolean("isBalanceBet")==false) {
            tvTotalDiamond?.text = HtmlCompat.fromHtml("总下注钻石: <font color=\"#FF513E\">$totalMoney</font>", HtmlCompat.FROM_HTML_MODE_COMPACT)
        } else {
            tvTotalDiamond?.text = HtmlCompat.fromHtml("总下注金额: <font color=\"#FF513E\">$totalMoney</font>", HtmlCompat.FROM_HTML_MODE_COMPACT)
        }
        liveRoomBetAccessAdapter?.onMoneyChangeListener { money, pos ->
            if (liveRoomBetAccessAdapter?.data.isNullOrEmpty()) return@onMoneyChangeListener
            if (money == "") {
                liveRoomBetAccessAdapter?.getItem(pos)?.result?.money = "0"
            } else liveRoomBetAccessAdapter?.getItem(pos)?.result?.money = money
            setMoney()
        }
        tvBetAccessSubmit?.setOnClickListener {
            for (single in liveRoomBetAccessAdapter?.data!!) {
                if (single.result.money != "") {
                    if (arguments?.getBoolean("isBalanceBet")==false) {
                        if (single.result.money?.toInt()?:0 < 10) {
                             ToastUtils.showToast("单注投注钻石最小为 10")
                            return@setOnClickListener
                        }
                    } else {
                        if (single.result.money?.toInt()?:0 < 1) {
                             ToastUtils.showToast("单注投注金额最小为 1")
                            return@setOnClickListener
                        }
                    }

                }
            }
            if (totalMoney<10){
                if (arguments?.getBoolean("isBalanceBet")==false) {
                     ToastUtils.showToast("单注投注钻石最小为 10")
                    return@setOnClickListener
                } else {
                    if (totalMoney < 1) {
                         ToastUtils.showToast("单注投注金额最小为 1")
                        return@setOnClickListener
                    }
                }
            }
            when {
                totalMoney > 999999999 -> {
                     ToastUtils.showToast("投注金额最大为 999999999")
                }
                else -> {
                    //余额不足
                    if (arguments?.getBoolean("isBalanceBet")==false) {
                        if (UserDiamond < (totalMoney.toBigDecimal())) {
                            val tips = context?.let { it1 -> DialogGlobalTips(it1, "您的钻石不足,请兑换钻石", "确定", "取消", "") }
                            tips?.setConfirmClickListener {
                                RxBus.get().post(HomeJumpToMineCloseLive(true))
                                dismiss()
                            }
                            tips?.show()
                            return@setOnClickListener
                        }
                    } else {
                        if (UserBalance < (totalMoney.toBigDecimal())) {
                            val tips = context?.let { it1 -> DialogGlobalTips(it1, "您余额不足,请充值", "确定", "取消", "") }
                            tips?.setConfirmClickListener {
                                RxBus.get().post(HomeJumpToMineCloseLive(true))
                                dismiss()
                            }
                            tips?.show()
                            return@setOnClickListener
                        }
                    }
                    //投注
                    loadingDialog?.show()
                    try {
                        val id = arguments?.getString("lotteryID")
                        val issue = arguments?.getString("issue")
                        val jsonRes = arguments?.getParcelableArrayList<LotteryBet>("lotteryBet")
                        val bean = arrayListOf<BetBean>()
                        if (jsonRes != null) {
                            for (js in jsonRes) {
                                val result = BetBean(js.result.play_sec_name, js.result.play_class_name, js.result.money)
                                bean.add(result)
                            }
                            val followUser = arguments?.getString("followUserId") ?: "0"
                            lotteryBet(id ?: "", issue ?: "", bean, followUser)
                        }
                    } catch (e: Exception) {
                         ToastUtils.showToast(e.message.toString())
                    }

                }
            }
        }
    }


    private fun setMoney() {
        var total = 0L
        for (it in liveRoomBetAccessAdapter?.data!!) {
            if (it.result.money != "") {
                total += it.result.money?.toInt()?:0
            }
        }
        totalMoney = total
        if (arguments?.getBoolean("isBalanceBet") == false) {
            tvTotalDiamond?.text = HtmlCompat.fromHtml("总下注钻石: <font color=\"#FF513E\">$total</font>", HtmlCompat.FROM_HTML_MODE_COMPACT)
        }else{
            tvTotalDiamond?.text = HtmlCompat.fromHtml("总下注金额: <font color=\"#FF513E\">$total</font>", HtmlCompat.FROM_HTML_MODE_COMPACT)
        }

    }


    /**
     * 投注 跟投
     * play_bet_follow_user	跟投用户id，默认0为正常投注
     */
    private fun lotteryBet(play_bet_lottery_id: String, play_bet_issue: String, order_detail: ArrayList<BetBean>, play_bet_follow_user: String) {
        orderMap = hashMapOf()
        val goon = GsonBuilder().disableHtmlEscaping().create()
        val orderString = goon.toJson(order_detail).toString()
        orderMap!!["play_bet_lottery_id"] = play_bet_lottery_id
        orderMap!!["play_bet_issue"] = play_bet_issue
        orderMap!!["play_bet_follow_user"] = play_bet_follow_user
        orderMap!!["order_detail"] = orderString
        orderMap!!["is_bl_play"] = if (arguments?.getBoolean("isBalanceBet") == true) "1" else "0"
        AESUtils.encrypt(UserInfoSp.getRandomStr() ?: "", goon.toJson(orderMap))?.let {
            val param = HashMap<String, String>()
            param["datas"] = it
            LotteryApi.toBet(it){
                onSuccess {
                    //投注成功
                    loadingDialog?.dismiss()
                    if (UserInfoSp.getUserType() == "1" && (arguments?.getBoolean("isFollow") == false)) {
                        val dialog = context?.let { it1 -> DialogGlobalTips(it1, "投注成功", "分享方案", "确定", "") }
                        dialog?.setConfirmClickListener {
                            getShareOrder()
                        }
                        dialog?.setOnDismissListener {
                            RxBus.get().post(LotteryResetDiamond(true))
                        }
                        dialog?.show()
                    } else context?.let { it1 ->
                       val dialog =  DialogGlobalTips(it1, "投注成功", "确定", "", "")
                        dialog.setOnDismissListener {
                            RxBus.get().post(LotteryResetDiamond(true))
                        }
                        dialog.show()
                    }

                    dismiss()
                }
                onFailed { err->
                    loadingDialog?.dismiss()
                    ToastUtils.showToast(err.getMsg())
                }
            }
        }
    }

    //拼接分享注单
    private fun getShareOrder() {
        val jsonRes = arguments?.getParcelableArrayList<LotteryBet>("lotteryBet")
        val goon = GsonBuilder().disableHtmlEscaping().create()
        val bean = arrayListOf<BetShareBean>()
        if (jsonRes != null) {
            for (js in jsonRes) {
                val result = BetShareBean(js.playName, js.result.money, js.result.play_class_cname,
                        js.result.play_class_name, js.result.play_odds, js.result.play_sec_name)
                bean.add(result)
            }
        }
        val json = JSONObject()
        json.put("play_bet_issue", arguments?.getString("issue") ?: "")
        json.put("play_bet_lottery_id", arguments?.getString("lotteryID") ?: "")
        json.put("lottery_cid", arguments?.getString("lotteryName") ?: "")
        json.put("order_detail", goon.toJson(bean))
        json.put("is_bl_play",  if (arguments?.getBoolean("isBalanceBet") == true) "1" else "0")
        RxBus.get().post(LotteryShareBet(true, json))
    }
    private fun initLoading() {
        loadingDialog = context?.let { LoadingDialog(it) }
        loadingDialog?.setCanceledOnTouchOutside(false)

    }

    override fun initFragment() {
    }

    companion object {
        fun newInstance(lotteryBet: LotteryBetAccess) = LiveRoomBetAccessFragment().apply {
            arguments = Bundle(1).apply {
                putParcelableArrayList("lotteryBet", lotteryBet.result)
                putInt("totalDiamond", lotteryBet.totalMoney)//每一注金额
                putInt("totalCount", lotteryBet.totalCount) //多少注
                putString("lotteryID", lotteryBet.lotteryID) //ID
                putString("issue", lotteryBet.issue) //ISSUE
                putString("diamond", lotteryBet.diamond) //diamond
                putString("lotteryName", lotteryBet.lotteryName) //lotteryName
                putString("lotteryNameType", lotteryBet.lotteryNameType) //lotteryNameType
                putBoolean("isFollow", lotteryBet.isFollow) //isFollow
                putString("followUserId", lotteryBet.followUserId) //isFollow
                putBoolean("isBalanceBet", lotteryBet.isBalanceBet?:false) //是否余额投注 false不是 true是
                putString("totalBalance", lotteryBet.totalBalance) //余额
            }
        }
    }

    /**
     * 获取钻石
     */
    private fun getUserDiamond() {
        try {
            MineApi.getUserDiamond {
                if (isAdded) {
                    onSuccess {
                        UserDiamond = it.diamond.toBigDecimal()
                    }

                }
            }
        } catch (e: Exception) {
             ToastUtils.showToast(e.toString())
        }

    }

    //获取余额
    @SuppressLint("SetTextI18n")
    fun getUserBalance() {
        MineApi.getUserBalance {
            onSuccess {
//                    mView.setBalance(it.balance.toString())
//                userBalance = it.balance.toString()
//                if (tvUserDiamond != null) tvUserDiamond.text = userBalance
                UserBalance = it.balance
            }
            onFailed {
                 ToastUtils.showToast(it.getMsg() ?: "")
            }
        }
    }
}