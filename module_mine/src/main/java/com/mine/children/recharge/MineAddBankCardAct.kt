package com.mine.children.recharge

import android.annotation.SuppressLint
import android.text.TextUtils
import com.customer.component.dialog.CityGetSelect
import com.customer.component.dialog.CityPickView
import com.hwangjr.rxbus.RxBus
import com.lib.basiclib.base.activity.BaseNavActivity
import com.lib.basiclib.base.xui.widget.picker.widget.OptionsPickerView
import com.lib.basiclib.base.xui.widget.picker.widget.builder.OptionsPickerBuilder
import com.lib.basiclib.utils.ToastUtils
import com.mine.R
import com.customer.data.mine.MineApi
import com.customer.data.mine.MineBankList
import com.customer.data.mine.MineUpDateBank
import kotlinx.android.synthetic.main.act_mine_add_bank_card.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/25
 * @ Describe
 *
 */

class MineAddBankCardAct : BaseNavActivity() {
    private var pvOptions: OptionsPickerView<String>? = null
    private var province: String? = null
    private var city: String? = null
    var bankCode: String = "-1001"
    var dataList: List<MineBankList>? = null


    override fun getContentResID() = R.layout.act_mine_add_bank_card

    override fun getPageTitle() = getString(R.string.mine_add_bank_card)

    override fun isShowBackIconWhite() = false

    override fun isSwipeBackEnable() = true

    override fun isOverride() = false

    val list = arrayListOf<String>()

    override fun initData() {
        getBankList()
    }

    @SuppressLint("SetTextI18n")
    override fun initEvent() {
        tvOpenCity.setOnClickListener {
            val view = CityPickView(this, CityGetSelect {
                province = it.split(",")[0]
                city = it.split(",")[1]
                tvOpenCity.text = "$province $city"
            })
            view.showPickerView(false)
        }

        linPickLayout.setOnClickListener {
            pvOptions?.show()
        }

        bindSubmit.setOnClickListener {
            if (bankCode == "-1001") {
                ToastUtils.showToast("请选择银行卡")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(tvOpenCity.text)) {
                ToastUtils.showToast("请选择开户城市")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(etOpenCityOther.text)) {
                ToastUtils.showToast("请填写开户支行")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(etOpenName.text)) {
                ToastUtils.showToast("请填写开户姓名")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(etOpenNumber.text)) {
                ToastUtils.showToast("请填写银行卡号")
                return@setOnClickListener
            }
            if (etOpenNumber.text.length < 15 || etOpenNumber.text.length > 22) {
                ToastUtils.showToast("请填写正确的15-22位银行卡号")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(etOpenPassWord.text)) {
                ToastUtils.showToast("请填写支付密码")
                return@setOnClickListener
            }

            bindBankCard(
                bankCode,
                province.toString(),
                city.toString(),
                etOpenCityOther.text.toString(),
                etOpenName.text.toString(),
                etOpenNumber.text.toString(), etOpenPassWord.text.toString()
            )
        }
    }

    private fun getBankList() {
        showPageLoadingDialog()
        MineApi.getBankList {
            onSuccess {
                dataList = it
                if (it.isNotEmpty()) {
                    tvUserBankCard.text = it[0].name
                    bankCode = it[0].code
                    showSexPickerView(it, 0)
                }
                hidePageLoadingDialog()
            }
            onFailed {
                ToastUtils.showToast(it.getMsg().toString())
                hidePageLoadingDialog()
            }
        }
    }

    private fun bindBankCard(
        bank_code: String,
        province: String,
        city: String,
        branch: String,
        realname: String,
        card_num: String,
        fund_password: String
    ) {
        showPageLoadingDialog()
        MineApi.bingBankCard(bank_code, province, city, branch, realname, card_num, fund_password) {
            onSuccess {
                ToastUtils.showToast("绑定成功")
                RxBus.get().post(MineUpDateBank(true))
                hidePageLoadingDialog()
                finish()
            }
            onFailed {
                hidePageLoadingDialog()
                ToastUtils.showToast(it.getMsg().toString())

            }
        }
    }


    private fun showSexPickerView(list: List<MineBankList>, pos: Int) {
        val final = arrayListOf<String>()
        for (res in list) {
            final.add(res.name)
        }
        pvOptions = OptionsPickerBuilder(this) { _, options1, _, _ ->
            tvUserBankCard.text = list[options1].name
            bankCode = list[options1].code
            false
        }
            .setTitleText("请选择银行卡")
            .setSelectOptions(pos)
            .setContentTextSize(20)
            .build()
        pvOptions?.setPicker(final)
    }
}