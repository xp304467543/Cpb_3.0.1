package com.mine.children

import android.graphics.Color
import android.text.Editable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.util.TypedValue.COMPLEX_UNIT_SP
import com.customer.ApiRouter
import com.customer.data.UserInfoSp
import com.customer.data.mine.Third
import com.customer.data.urlCustomer
import com.lib.basiclib.base.mvp.BaseMvpActivity
import com.lib.basiclib.base.xui.widget.picker.widget.OptionsPickerView
import com.lib.basiclib.base.xui.widget.picker.widget.builder.OptionsPickerBuilder
import com.lib.basiclib.base.xui.widget.popupwindow.ViewTooltip
import com.lib.basiclib.utils.FastClickUtil
import com.lib.basiclib.utils.StatusBarUtils
import com.lib.basiclib.utils.ToastUtils
import com.lib.basiclib.utils.ViewUtils
import com.mine.R
import com.xiaojinzi.component.impl.Router
import kotlinx.android.synthetic.main.act_mine_money_center.*
import java.math.BigDecimal


/**
 *
 * @ Author  QinTian
 * @ Date  2020/9/27
 * @ Describe
 *
 */
class MineMoneyCenterAct : BaseMvpActivity<MineMoneyCenterActPresenter>() {

    var maxEtLength = 8

    var thirdList: ArrayList<Third>? = null

    private var rightList = arrayListOf("中心钱包")

    private var pvOptions: OptionsPickerView<String>? = null

    override fun attachView() = mPresenter.attachView(this)

    override fun attachPresenter() = MineMoneyCenterActPresenter()

    override fun getContentResID() = R.layout.act_mine_money_center

    override fun isShowToolBar() = false

    override fun isShowBackIconWhite() = false

    override fun isSwipeBackEnable() = true


    override fun initContentView() {
        StatusBarUtils.setStatusBarHeight(centerView)
        val spannableString = SpannableString("如需帮助,请 联系客服")
        spannableString.setSpan(
            ForegroundColorSpan(Color.parseColor("#ff513e")),
            7,
            spannableString.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        tvCustomer.text = spannableString

    }

    override fun initData() {
        mPresenter.getUserBalance()
        mPresenter.getThird(false)
    }

    override fun initEvent() {
        imgBack.setOnClickListener {
            finish()
        }
        tvCustomer.setOnClickListener {
            if (!FastClickUtil.isFastClick()) {
                Router.withApi(ApiRouter::class.java)
                    .toGlobalWeb(UserInfoSp.getCustomer() ?: urlCustomer)
            }
        }
        tvMoneyCenter.setOnClickListener {
            if (!FastClickUtil.isFastClick()) {
                showPickerViewCenter()
            }
        }
        tvMoneyGame.setOnClickListener {
            if (!FastClickUtil.isFastClick()) {
                showPickerView()
            }
        }
        etMoney.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    ViewUtils.setGone(tvMoneyTips)
                    return
                }
                if (s.toString().contains(".")) {
                    if (s.length - 1 - s.toString().indexOf(".") > 2) {
                        val str = s.toString().subSequence(0, s.toString().indexOf(".") + 3)
                        etMoney.setText(str)
                        etMoney.setSelection(str.length)
                    }
                } else {
                    if (s.length > maxEtLength) {
                        etMoney.setText(s.subSequence(0, maxEtLength))
                        etMoney.setSelection(etMoney.text.length)
                    }
                }

                if (s.toString().trim().substring(0) == ".") {
                    val str = "0$s"
                    etMoney.setText(str)
                    etMoney.setSelection(2)
                }

                if (s.toString().startsWith("0") && s.toString().trim().length > 1) {
                    if (s.toString().substring(1, 2) != ".") {
                        etMoney.setText(s.subSequence(0, 1))
                        etMoney.setSelection(1)
                        return
                    }
                }

                val all = s.toString()
                val t1 = tvCenterMoney.text.toString()
                val t2 = tv_3_money.text.toString()
                val t3 = tv_4_money.text.toString()
                if (current_1 == 0) {
                    if (BigDecimal(t1).compareTo(BigDecimal(all)) == -1) {
                        ViewUtils.setVisible(tvMoneyTips)
                    } else ViewUtils.setGone(tvMoneyTips)
                } else if (current_1 == 1) {
                    if (BigDecimal(t2).compareTo(BigDecimal(all)) == -1) {
                        ViewUtils.setVisible(tvMoneyTips)
                    } else ViewUtils.setGone(tvMoneyTips)
                } else if (current_1 == 2) {
                    if (t3 == "维护中" || t3 == "加载中") return
                    if (BigDecimal(t3).compareTo(BigDecimal(all)) == -1) {
                        ViewUtils.setVisible(tvMoneyTips)
                    } else ViewUtils.setGone(tvMoneyTips)
                }


            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        btSure.setOnClickListener {
            if (!FastClickUtil.isFastClick()) {
                if (etMoney.text.isNullOrEmpty()) {
                    ToastUtils.showToast("请输入转账金额")
                    return@setOnClickListener
                }
                if (BigDecimal(etMoney.text.toString()).compareTo(BigDecimal(1)) == -1) {
                    ToastUtils.showToast("转账金额不能小于1")
                    return@setOnClickListener
                }
                val all = etMoney.text.toString()
                val t1 = tvCenterMoney.text.toString()
                val t2 = tv_3_money.text.toString()
                val t3 = tv_4_money.text.toString()
                if (current_1 == 0) {
                    if (BigDecimal(t1).compareTo(BigDecimal(all)) == -1) {
                        ViewUtils.setVisible(tvMoneyTips)
                        ToastUtils.showToast("余额不足")
                        return@setOnClickListener
                    }
                } else if (current_1 == 1) {
                    if (BigDecimal(t2).compareTo(BigDecimal(all)) == -1) {
                        ViewUtils.setVisible(tvMoneyTips)
                        ToastUtils.showToast("余额不足")
                        return@setOnClickListener
                    }
                } else if (current_1 == 2) {
                    if (t3 == "维护中" || t3 == "加载中") {
                        ToastUtils.showToast("AG平台维护中")
                        return@setOnClickListener
                    }
                    if (BigDecimal(t3).compareTo(BigDecimal(all)) == -1) {
                        ViewUtils.setVisible(tvMoneyTips)
                        ToastUtils.showToast("余额不足")
                        return@setOnClickListener
                    }
                }
                if (BigDecimal(tvCenterMoney.text.toString()).compareTo(BigDecimal(etMoney.text.toString())) == -1) ViewUtils.setVisible(
                    tvMoneyTips
                )
                if (current_1 == 0) {
                    if (thirdList?.get(current)?.name == "fh_chess") mPresenter.upAndDownMoney(
                        1,
                        true,
                        etMoney.text.toString()
                    )
                    if (thirdList?.get(current)?.name == "ag") {
                        if (t3 == "维护中") {
                            ToastUtils.showToast("AG平台维护中")
                            return@setOnClickListener
                        }
                        mPresenter.upAndDownMoney(2, true, etMoney.text.toString())
                    }
                } else {
                    if (thirdList?.get(current)?.name == "fh_chess") mPresenter.upAndDownMoney(
                        1,
                        false,
                        etMoney.text.toString()
                    )
                    if (thirdList?.get(current)?.name == "ag") {
                        if (t3 == "维护中") {
                            ToastUtils.showToast("AG平台维护中")
                            return@setOnClickListener
                        }
                        mPresenter.upAndDownMoney(2, false, etMoney.text.toString())
                    }
                }
                showPageLoadingDialog("转账中")
            }
        }
        tvAll.setOnClickListener {
            when (current_1) {
                0 -> {
                    if (BigDecimal(tvCenterMoney.text.toString()) == BigDecimal.ZERO) {
                        ToastUtils.showToast("转账金额不能小于1")
                        return@setOnClickListener
                    }
                    maxEtLength = tvCenterMoney.text.length
                    etMoney.setText(tvCenterMoney.text.toString())
                }
                1 -> {
                    if (BigDecimal(tv_3_money.text.toString()) == BigDecimal.ZERO) {
                        ToastUtils.showToast("转账金额不能小于1")
                        return@setOnClickListener
                    }
                    maxEtLength = tv_3_money.text.length
                    etMoney.setText(tv_3_money.text.toString())
                }
                2 -> {
                    if (!tv_4_money.text.isNullOrEmpty()) {
                        if (tv_4_money.text.toString() != "维护中") {
                            if (BigDecimal(tv_4_money.text.toString()) == BigDecimal.ZERO) {
                                ToastUtils.showToast("转账金额不能小于1")
                                return@setOnClickListener
                            }
                            maxEtLength = tv_4_money.text.length
                            etMoney.setText(tv_4_money.text.toString())
                        } else ToastUtils.showToast("维护中")
                    }
                }
            }

        }
        imgTips.setOnClickListener {
            ViewTooltip.on(imgTips)
                .color(ViewUtils.getColor(R.color.color_F1F0F6))
                .textColor(ViewUtils.getColor(R.color.color_666666))
                .position(ViewTooltip.Position.BOTTOM)
                .text(" 把游戏场馆的余额一键回收到中心钱包 ")
                .textSize(COMPLEX_UNIT_SP, 12f)
                .clickToHide(true)
                .autoHide(true, 5000)
                .animation(ViewTooltip.FadeTooltipAnimation(500))
                .show()
        }
        tvRecycle.setOnClickListener {
            if (!FastClickUtil.isTenFastClick()) {
                val t1 = BigDecimal(tv_3_money.text.toString())
                if (tv_4_money.text.toString() != "维护中" && tv_4_money.text.toString() != "加载中") {
                    val t2 = BigDecimal(tv_4_money.text.toString())
                    if (t1.compareTo(BigDecimal.ZERO) != 0 && t2.compareTo(BigDecimal.ZERO) != 0) {
                        showPageLoadingDialog("转账中")
                        mPresenter.recycleAll(
                            tv_3_money.text.toString(),
                            tv_4_money.text.toString()
                        )
                    } else if (t1.compareTo(BigDecimal.ZERO) != 0) {
                        showPageLoadingDialog("转账中")
                        mPresenter.upAndDownMoney(1, false, tv_3_money.text.toString(), true)
                    } else if (t2.compareTo(BigDecimal.ZERO) != 0) {
                        showPageLoadingDialog("转账中")
                        mPresenter.upAndDownMoney(2, false, tv_4_money.text.toString(), true)
                    } else {
                        ToastUtils.showToast("没有可回收账户")
                    }
                } else {
                    if (t1.compareTo(BigDecimal.ZERO) == 0) {
                        ToastUtils.showToast("没有可回收账户")
                    } else {
                        showPageLoadingDialog("转账中")
                        mPresenter.upAndDownMoney(1, false, tv_3_money.text.toString(), true)
                    }
                }
            }else ToastUtils.showToast("点击过于频繁,请10秒后重试")
        }
        titleTop.setOnClickListener {
            if (!FastClickUtil.isTenFastClick2()) {
//                tvCenterMoney.text = "加载中"
//                tvOtherMoney.text = "加载中"
//                tv_3_money.text = "加载中"
//                tv_4_money.text = "加载中"
                showPageLoadingDialog("加载中")
                mPresenter.getUserBalance()
            }else ToastUtils.showToast("点击过于频繁,请10秒后重试")
        }
    }

    var current_1 = 0
    private fun showPickerViewCenter() {
        val arrayList = arrayListOf("中心钱包", "乐购棋牌", "AG游戏")
        pvOptions = OptionsPickerBuilder(this) { _, options1, _, _ ->
            current_1 = options1
            if (current_1 != 0) {
                tvMoneyGame.text = "中心钱包"
            } else {
                current = 0
                tvMoneyGame.text = "乐购棋牌"
            }
            tvMoneyCenter.text = arrayList[options1]
            false
        }
            .setTitleText("")
            .setSelectOptions(current_1)
            .setContentTextSize(18)
            .build()
        pvOptions?.setPicker(arrayList)
        pvOptions?.show()
    }

    var current = 0
    fun showPickerView() {
        if (thirdList.isNullOrEmpty()) {
            showPageLoadingDialog()
            mPresenter.getThird(true)
            return
        }
        val final = arrayListOf<String>()
        for (res in thirdList!!) {
            final.add(res.name_cn.toString())
        }
        pvOptions = OptionsPickerBuilder(this) { _, options1, _, _ ->
            current = options1
            tvMoneyGame.text =
                if (current_1 == 0) thirdList?.get(options1)?.name_cn else rightList[options1]
            false
        }
            .setTitleText("")
            .setSelectOptions(current)
            .setContentTextSize(18)
            .build()
        if (current_1 == 0) pvOptions?.setPicker(final) else pvOptions?.setPicker(rightList)
        pvOptions?.show()
    }
}