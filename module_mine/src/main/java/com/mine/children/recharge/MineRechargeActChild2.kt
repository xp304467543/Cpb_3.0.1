package com.mine.children.recharge

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import com.customer.utils.MoneyValueFilter
import com.glide.GlideUtil
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.thread.EventThread
import com.lib.basiclib.base.mvp.BaseMvpFragment
import com.lib.basiclib.utils.ToastUtils
import com.mine.R
import com.customer.data.mine.MineUpDateBank
import com.customer.data.mine.MineUpDateMoney
import com.customer.component.dialog.GlobalDialog
import com.customer.data.UserInfoSp
import com.customer.data.MineSaveBank
import com.lib.basiclib.utils.FastClickUtil
import com.xiaojinzi.component.impl.Router
import kotlinx.android.synthetic.main.fragment_cash_out.*
import java.math.BigDecimal

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/25
 * @ Describe 提现
 *
 */

class MineRechargeActChild2 : BaseMvpFragment<MineRechargeActChild2Presenter>() {

    var balanceNow = "0"

    override fun getContentResID() = R.layout.fragment_cash_out

    override fun attachView() = mPresenter.attachView(this)

    override fun attachPresenter() = MineRechargeActChild2Presenter()

    override fun isRegisterRxBus() = true

    override fun initContentView() {
        balanceNow = arguments?.getString("balance") ?: "0"
    }

    override fun initData() {
        mPresenter.getBankList()
    }

    override fun initEvent() {
        rlAddBankItem.setOnClickListener {
            if (FastClickUtil.isFastClick()){
                if (UserInfoSp.getIsSetPayPassWord()) {
                    startActivity(Intent(getPageActivity(), MineAddBankCardAct::class.java))
                } else GlobalDialog.noSetPassWord(requireActivity())
            }
        }
        tvGetMoneyAll.setOnClickListener {
            etGetMoneyToBank.setText(balanceNow)
        }
        btUserGetCash.setOnClickListener {
            mPresenter.getCashOutMoney()
        }

        etGetMoneyToBank.filters = arrayOf<InputFilter>(MoneyValueFilter())
        etGetMoneyToBank.addTextChangedListener(object : TextWatcher {
            @SuppressLint("SetTextI18n")
            override fun afterTextChanged(p0: Editable?) {
                if (p0?.length!! > 0 && BigDecimal(p0.toString()).compareTo(BigDecimal(balanceNow)) == 1) {
                    ToastUtils.showToast("余额不足")
                    etGetMoneyToBank.setText("")
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        })
    }


    @SuppressLint("SetTextI18n")
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun saveUserBankSelect(event: MineSaveBank) {
        GlideUtil.loadImage(event.data.bank_img, imgBankItem)
        tvBankNameItem.text = event.data.bank_name
        tvBankCodeItem.text = "尾号" + event.data.card_num.substring(
            event.data.card_num.length - 4,
            event.data.card_num.length
        ) + "储蓄卡"
        mPresenter.mineUserBank = event.data
    }


    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun upDateUserBankSelect(event: MineUpDateBank) {
        mPresenter.getBankList()
    }


    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun upDateUserMoney(event: MineUpDateMoney) {
        if (event.isUpdate) {
            balanceNow = event.money
            hidePageLoadingDialog()
        }
    }

    companion object {
        fun newInstance(balance: String): MineRechargeActChild2 {
            val fragment = MineRechargeActChild2()
            val bundle = Bundle()
            bundle.putString("balance", balance)
            fragment.arguments = bundle
            return fragment
        }
    }


}