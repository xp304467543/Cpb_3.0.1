package com.customer.component.dialog

import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.customer.data.LotteryResetDiamond
import com.customer.data.UserInfoSp
import com.customer.data.lottery.BetBean
import com.customer.data.lottery.LotteryApi
import com.customer.data.lottery.PlaySecData
import com.customer.utils.AESUtils
import com.fh.module_base_resouce.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.GsonBuilder
import com.hwangjr.rxbus.RxBus
import com.lib.basiclib.base.recycle.BaseRecyclerAdapter
import com.lib.basiclib.base.recycle.RecyclerViewHolder
import com.lib.basiclib.utils.FastClickUtil
import com.lib.basiclib.utils.LogUtils
import com.lib.basiclib.utils.ToastUtils
import kotlinx.android.synthetic.main.dialog_bottom_bet_access.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/9/25
 * @ Describe
 *
 */
class BottomBetAccessDialog( context: Context,var lotteryId:String,var rightTop:String,var nextIssue:String,var is_bal: Int,var totalMoney:String,var dataList: ArrayList<PlaySecData>) : BottomSheetDialog(context) {

    private var singleMoney = 0L
    private  var liveRoomBetAccessAdapter:LiveRoomBetAccessAdapter?=null
    init {
        setContentView(R.layout.dialog_bottom_bet_access)
        val root = this.delegate?.findViewById<View>(R.id.design_bottom_sheet)
        val behavior = root?.let { BottomSheetBehavior.from(it) }
        behavior?.isHideable = false
        this.delegate?.findViewById<View>(R.id.design_bottom_sheet)?.setBackgroundColor(Color.TRANSPARENT)
        initContent()
        initLoading()
        initEvent()
    }

    private fun initContent() {
        singleMoney = if (totalMoney.isNotEmpty()) totalMoney.toLong()/dataList.size else 0
        if (is_bal == 0) {
            tvTotalDiamond?.text = HtmlCompat.fromHtml("总下注钻石: <font color=\"#FF513E\">$totalMoney</font>", HtmlCompat.FROM_HTML_MODE_COMPACT)
        } else {
            tvTotalDiamond?.text = HtmlCompat.fromHtml("总下注金额: <font color=\"#FF513E\">$totalMoney</font>", HtmlCompat.FROM_HTML_MODE_COMPACT)
        }
        liveRoomBetAccessAdapter = LiveRoomBetAccessAdapter()
        rvBetAccess?.adapter = liveRoomBetAccessAdapter
        rvBetAccess?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        liveRoomBetAccessAdapter?.refresh(dataList)
    }
    inner class LiveRoomBetAccessAdapter : BaseRecyclerAdapter<PlaySecData>() {

        private var textWatcher: TextWatcher? = null

        override fun getItemLayoutId(viewType: Int): Int {
            return   R.layout.adapter_bet_access
        }

        override fun bindData(holder: RecyclerViewHolder, position: Int, data: PlaySecData?) {
            data?.money = singleMoney.toString()
            val edit = holder.findViewById<EditText>(R.id.tvBetPlayMoney)
            holder.text(R.id.tvBetPlayName, data?.play_sec_cname)
            holder.text(R.id.tvBetPlayType, data?.play_class_cname)
            holder.text(R.id.tvBetPlayOdds, data?.play_odds)
            textWatcher = object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {}
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            }
            //1、为了避免TextWatcher在第2步被调用，提前将他移除。
            if (edit.tag is TextWatcher) {
                edit.removeTextChangedListener(edit.tag as TextWatcher)
            }
            // 第2步：移除TextWatcher之后，设置EditText的Text。
            edit.setText(singleMoney.toString())

            val watcher: TextWatcher = object : TextWatcher {
                override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
                override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
                override fun afterTextChanged(editable: Editable) {
                    if ( editable.isNotEmpty()) {
                        if (editable.toString().toLong() < 10) {
                            if (is_bal==0){
                                ToastUtils.showToast("请输入≥10的整数")
                            }
                            if (is_bal==1){
                                if (editable.toString().toLong() < 1){
                                    ToastUtils.showToast("请输入≥1的整数")
                                }
                            }
                        }
                        val now= if (editable.length > 9) {
                            edit.setText(editable.substring(0, 9)); //截取前x位
                            edit.requestFocus()
                            edit.setSelection(edit.length()) //光标移动到最后
                            editable.substring(0, 9)
                        } else {
                            editable.toString()
                        }
                        if (now == ""){
                            data?.money = "0"
                        }else data?.money = now
                        setMoney()
                    }
                }
            }
            edit.addTextChangedListener(watcher)
            edit.tag = watcher
        }
    }


    private fun setMoney() {
        var total = 0L
        for (it in liveRoomBetAccessAdapter?.data!!) {
            if (it.money != "") {
                total += it.money?.toInt()?:0
            }
        }
        totalMoney = total.toString()
        if (is_bal==0) {
            tvTotalDiamond?.text = HtmlCompat.fromHtml("总下注钻石: <font color=\"#FF513E\">$total</font>", HtmlCompat.FROM_HTML_MODE_COMPACT)
        }else{
            tvTotalDiamond?.text = HtmlCompat.fromHtml("总下注金额: <font color=\"#FF513E\">$total</font>", HtmlCompat.FROM_HTML_MODE_COMPACT)
        }

    }

    private fun initLoading() {
        loadingDialog = LoadingDialog(context)
        loadingDialog?.setCanceledOnTouchOutside(false)
    }


    private fun initEvent() {
        tvBetAccessSubmit.setOnClickListener {
            if (!FastClickUtil.isFastClick()){
                //投注
                loadingDialog?.show()
                try {
                    val id = lotteryId
                    val issue = nextIssue
                    val bean = arrayListOf<BetBean>()
                    if (!dataList.isNullOrEmpty()) {
                        if (!liveRoomBetAccessAdapter?.data.isNullOrEmpty()){
                            for (result in liveRoomBetAccessAdapter?.data!!) {
                                bean.add(BetBean(result.play_sec_name, result.play_class_name, result.money))
                            }
                            lotteryToBet(id, issue, bean)
                        }else ToastUtils.showToast("投注错误，请重新选择投注选项")
                    }
                } catch (e: Exception) {
                    ToastUtils.showToast(e.message.toString())
                }
            }
        }
    }


    /**
     * 投注
     */
    private var loadingDialog: LoadingDialog? = null
    private fun lotteryToBet(play_bet_lottery_id: String, play_bet_issue: String, order_detail: ArrayList<BetBean>){
        val orderMap = hashMapOf<String, Any>()
        val goon = GsonBuilder().disableHtmlEscaping().create()
        val orderString = goon.toJson(order_detail).toString()
        orderMap["play_bet_lottery_id"] = play_bet_lottery_id
        orderMap["play_bet_issue"] = play_bet_issue
        orderMap["play_bet_follow_user"] = "0"
        orderMap["order_detail"] = orderString
        orderMap["is_bl_play"] = is_bal
        AESUtils.encrypt(UserInfoSp.getRandomStr() ?: "", goon.toJson(orderMap))?.let {
            val param = HashMap<String, String>()
            param["datas"] = it
            LotteryApi.toBet(it){
                onSuccess {
                    //投注成功
                    loadingDialog?.dismiss()
                    context.let { it1 -> DialogGlobalTips(it1, "投注成功", "确定", "", "").show() }
                    RxBus.get().post(LotteryResetDiamond(true))
                    dismiss()
                }
                onFailed { err->
                    loadingDialog?.dismiss()
                    ToastUtils.showToast(err.getMsg())
                }
            }
        }
    }
}