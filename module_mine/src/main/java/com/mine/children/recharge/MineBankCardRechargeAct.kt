package com.mine.children.recharge

import android.annotation.SuppressLint
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import com.customer.component.dialog.DialogGlobalTips
import com.customer.data.mine.MineApi
import com.lib.basiclib.base.activity.BaseNavActivity
import com.lib.basiclib.base.xui.widget.picker.widget.builder.TimePickerBuilder
import com.lib.basiclib.base.xui.widget.picker.widget.configure.TimePickerType
import com.lib.basiclib.base.xui.widget.picker.widget.listener.OnTimeSelectListener
import com.lib.basiclib.utils.FastClickUtil
import com.lib.basiclib.utils.ToastUtils
import com.lib.basiclib.utils.ViewUtils
import com.mine.R
import kotlinx.android.synthetic.main.act_bank_recharge.*
import java.math.BigDecimal
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


/**
 *
 * @ Author  QinTian
 * @ Date  2020/10/5
 * @ Describe
 *
 */
class MineBankCardRechargeAct : BaseNavActivity() {


    var minMoney: BigDecimal? = null

    var highMoney: BigDecimal? = null

    var bankId = "-1"

    var cardType = ""
    var cardUser = ""
    var cardNum = ""
    var cardAddress = ""

    override fun getContentResID() = R.layout.act_bank_recharge

    override fun getPageTitle() = "银行卡转账"

    override fun isShowBackIconWhite() = false

    override fun isSwipeBackEnable() = true

    override fun initContentView() {
        etUser.filters = arrayOf(filter, LengthFilter(20))
    }

    override fun initData() {
        showPageLoadingDialog()
        getBank()
    }


    override fun initEvent() {
        tvTimePicker.setOnClickListener { timePicker() }
        btConfirm.setOnClickListener {
            if (!FastClickUtil.isFastClick()) {
                if (etUser.text.toString().trim().isEmpty()) {
                    ToastUtils.showToast("请填写持卡人姓名")
                    return@setOnClickListener
                }
                if (rtCardNum.text.toString().trim().isEmpty()) {
                    ToastUtils.showToast("请填写卡号")
                    return@setOnClickListener
                }
                if (etSaveMoney.text.toString().trim().isEmpty()) {
                    ToastUtils.showToast("请输入转账金额")
                    return@setOnClickListener
                }
                if (tvTimePicker.text.toString().trim().isEmpty()) {
                    ToastUtils.showToast("请选择转账时间")
                    return@setOnClickListener
                }
                if (rtCardNum.text.length < 12) {
                    ToastUtils.showToast("卡号长度错误")
                    return@setOnClickListener
                }
                if (etSaveMoney.text.isEmpty()) {
                    ToastUtils.showToast("请填写充值金额")
                    return@setOnClickListener
                }
//                if (minMoney == null || highMoney == null) {
//                    getBank()
//                    return@setOnClickListener
//                }
//                if (BigDecimal(etSaveMoney.text.toString()).compareTo(minMoney) == -1 || BigDecimal(
//                        etSaveMoney.text.toString()
//                    ).compareTo(highMoney) == 1
//                ) {
//                    ToastUtils.showToast("充值金额 ($minMoney ~ $highMoney)")
//                    return@setOnClickListener
//                }
                conFirm()
            }
        }
        copy_1.setOnClickListener {
            if (cardType !=""){
                ViewUtils.copyText(cardType)
                ToastUtils.showToast("已复制到剪贴板")
            }else  ToastUtils.showToast("暂无可使用银行卡")
        }
        copy_2.setOnClickListener {
            if (cardUser !=""){
                ViewUtils.copyText(cardUser)
                ToastUtils.showToast("已复制到剪贴板")
            }else  ToastUtils.showToast("暂无可使用银行卡")
        }
        copy_3.setOnClickListener {
            if (cardNum !=""){
                ViewUtils.copyText(cardNum)
                ToastUtils.showToast("已复制到剪贴板")
            }else  ToastUtils.showToast("暂无可使用银行卡")
        }
        copy_4.setOnClickListener {
            if (cardAddress !=""){
                ViewUtils.copyText(cardAddress)
                ToastUtils.showToast("已复制到剪贴板")
            }else  ToastUtils.showToast("暂无可使用银行卡")
        }
    }


    private fun timePicker() {
        val calendar: Calendar = Calendar.getInstance()
        calendar.time = Date()
        val mTimePicker = TimePickerBuilder(this,
            OnTimeSelectListener { date, v ->
                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                val str: String = sdf.format(date)
                tvTimePicker.text = str
            })
            .setType(TimePickerType.DATE)
            .setTitleText("时间选择")
            .isDialog(false)
            .setOutSideCancelable(true)
            .setDate(calendar)
            .build()
        mTimePicker.show()
    }

    @SuppressLint("SetTextI18n")
    private fun getBank() {
        MineApi.getBankCard {
            onSuccess {
                hidePageLoadingDialog()
                if (!it.isNullOrEmpty()) {
                    minMoney = BigDecimal(it[0].low_money)
                    highMoney = BigDecimal(it[0].high_money)
                    bankId = it[0].bank_id
                    cardType = it[0].bank
                    cardUser = it[0].name
                    cardNum = it[0].no
                    cardAddress = it[0].openbank
                    tvBan_1.text = "开户行:   " + it[0].bank
                    tvBan_2.text = "持卡人:   " + it[0].name
                    tvBan_3.text = "卡号:   " + it[0].no
                    tvBan_4.text = "开户行地址:   " + it[0].openbank
                    tvBan_5.text = it[0].rate
                    tvBan_6.text = "最低充值金额" + it[0].low_money + "元"
                } else ToastUtils.showToast("暂无可使用银行卡")
            }
            onFailed {
                hidePageLoadingDialog()
                ToastUtils.showToast(it.getMsg()) }
        }
    }

    private fun conFirm() {
        showPageLoadingDialog()
        if (bankId != "-1") {
            MineApi.getBankCardRecharge(
                bankId,
                etUser.text.toString().trim(),
                rtCardNum.text.toString().trim(),
                etSaveMoney.text.toString().trim(),
                dateToStamp(tvTimePicker.text.toString().trim())
            ) {
                onSuccess {
                    hidePageLoadingDialog()
                  val dialog = DialogGlobalTips(
                        this@MineBankCardRechargeAct,
                        "提示",
                        "确定",
                        "",
                        "您的订单已提交，我们会尽快为您 充值，请您稍等片刻"
                    )
                    dialog.setOnDismissListener { finish() }
                        dialog.show()
                }
                onFailed {
                    hidePageLoadingDialog()
                    DialogGlobalTips(
                        this@MineBankCardRechargeAct,
                        "提示",
                        "确定",
                        "",
                        it.getMsg().toString()
                    ).show()
                }
            }
        } else ToastUtils.showToast("暂无可使用银行卡")

    }


    /*
     * 将时间转换为时间戳
     */
    @Throws(ParseException::class)
    fun dateToStamp(s: String): String {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val date = simpleDateFormat.parse(s)
        val ts = date?.time
        return (ts?.div(1000)).toString()
    }

    /**
     * EditText只能输入中文
     */
    var filter = InputFilter { source, start, end, dest, dstart, dend ->
        for (i in start until end) {
            if (!isChinese(source[i])) {
                return@InputFilter ""
            }
        }
        null
    }

    /**
     * 只能输入中文的判断
     *
     * @param c
     * @return
     */
    private fun isChinese(c: Char): Boolean {
        val ub = Character.UnicodeBlock.of(c)
        return ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub === Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS || ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub === Character.UnicodeBlock.GENERAL_PUNCTUATION || ub === Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub === Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
    }
}