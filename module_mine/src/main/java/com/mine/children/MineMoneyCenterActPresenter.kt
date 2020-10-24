package com.mine.children

import android.annotation.SuppressLint
import com.customer.component.dialog.GlobalDialog
import com.customer.data.mine.MineApi
import com.google.gson.JsonParser
import com.lib.basiclib.base.mvp.BaseMvpPresenter
import com.lib.basiclib.utils.ToastUtils
import kotlinx.android.synthetic.main.act_mine_money_center.*
import java.math.BigDecimal

/**
 *
 * @ Author  QinTian
 * @ Date  2020/9/27
 * @ Describe
 *
 */
class MineMoneyCenterActPresenter : BaseMvpPresenter<MineMoneyCenterAct>() {

    //获取余额
    @SuppressLint("SetTextI18n")
    fun getUserBalance() {
        if (mView.isActive()) {
            MineApi.getUserBalance {
                onSuccess {
                    mView.tvCenterMoney?.text = it.balance.toString()
                    getTotalGameCenterMoney()
                }
                onFailed {
                    GlobalDialog.showError(mView, it)
                    getTotalGameCenterMoney()
                }
            }
            MineApi.getChessMoney {
                onSuccess {
                    mView.tv_qp_money?.text = it.bl.toString()
                    getTotalGameCenterMoney()
                }
                onFailed {
                    GlobalDialog.showError(mView, it)
                    getTotalGameCenterMoney()
                }
            }
            MineApi.getAgMoney {
                onSuccess {
                    mView.tv_ag_money?.text = it.bl.toString()
                    getTotalGameCenterMoney()
                }
                onFailed {
                    if (it.getCode() == 16) mView.tv_ag_money.text = "维护中" else ToastUtils.showToast(it.getMsg())
                    mView.hidePageLoadingDialog()
                    getTotalGameCenterMoney()
                }
            }

            MineApi.getBgMoney {
                onSuccess {
                    mView.tv_bg_money?.text = it.bl.toString()
                    mView.hidePageLoadingDialog()
                    getTotalGameCenterMoney()
                }
                onFailed {
                    if (it.getCode() == 16) mView.tv_bg_money.text = "维护中" else ToastUtils.showToast(it.getMsg())
                    mView.hidePageLoadingDialog()
                    getTotalGameCenterMoney()
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun getTotalGameCenterMoney() {
        try {
            if (mView.tv_bg_money.text.toString() == "维护中" && mView.tv_ag_money.text.toString() == "维护中") {
                mView.tvOtherMoney?.text = mView.tv_qp_money.text.toString()
            } else if (mView.tv_bg_money.text.toString() == "维护中") {
                mView.tvOtherMoney?.text =
                    BigDecimal(mView.tv_ag_money.text.toString()).add(
                        BigDecimal(mView.tv_qp_money.text.toString())
                    ).toString()
            } else if (mView.tv_ag_money.text.toString() == "维护中") {
                mView.tvOtherMoney?.text =
                    BigDecimal(mView.tv_bg_money.text.toString()).add(
                        BigDecimal(mView.tv_qp_money.text.toString())
                    ).toString()
            } else {
                mView.tvOtherMoney?.text =
                    (BigDecimal(mView.tv_bg_money.text.toString()).add(
                        BigDecimal(mView.tv_qp_money.text.toString())
                    )).add(BigDecimal(mView.tv_ag_money.text.toString()))
                        .toString()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getThird() {
        if (mView.isActive()) {
            MineApi.getThird {
                onSuccess {
                    mView.thirdList = it
                    mView.tvMoneyGame?.text = it[1].name_cn
                    mView.thirdChineseList = arrayListOf()
                    for (item in it) {
                        mView.thirdChineseList?.add(item.name_cn ?: "null")
                    }
                }
                onFailed {
                    ToastUtils.showToast(it.getMsg())
                }
            }
        }
    }

    fun getIsAutoChange(){
        MineApi.getAutoChange {
            if (mView.isActive()){
                onSuccess {
                    val jsonObject = JsonParser.parseString(it).asInt
                    mView.initCheck(jsonObject == 1)
                }
                onFailed {
                    ToastUtils.showToast(it.getMsg())
                }
            }
        }
    }

    //上分下分  1 棋牌  2 Ag 3 Bg true 上分 false 下分
    fun upAndDownMoney(index: Int, upOrDown: Boolean, amount: String) {
        if (mView.isActive()) {
            mView.showPageLoadingDialog("转账中")
            when (index) {
                1 -> {
                    MineApi.getChessMoneyUpOrDown(upOrDown, amount) {
                        onSuccess {
                            mView.etMoney?.setText("")
                            getUserBalance()
                            mView.hidePageLoadingDialog()
                            ToastUtils.showToast("转账成功")
                        }
                        onFailed {
                            ToastUtils.showToast(it.getMsg())
                            mView.hidePageLoadingDialog()
                        }
                    }
                }
                2 -> {
                    MineApi.getAgMoneyUpOrDown(upOrDown, amount) {
                        onSuccess {
                            mView.etMoney?.setText("")
                            getUserBalance()
                            mView.hidePageLoadingDialog()
                            ToastUtils.showToast("转账成功")
                        }
                        onFailed {
                            mView.hidePageLoadingDialog()
                            ToastUtils.showToast(it.getMsg())
                        }
                    }
                }
                3 -> {
                    MineApi.getBgMoneyUpOrDown(upOrDown, amount) {
                        onSuccess {
                            mView.etMoney?.setText("")
                            getUserBalance()
                            mView.hidePageLoadingDialog()
                            ToastUtils.showToast("转账成功")
                        }
                        onFailed {
                            mView.hidePageLoadingDialog()
                            ToastUtils.showToast(it.getMsg())
                        }
                    }
                }
            }
        }
    }

    fun recycleAll() {
        mView.showPageLoadingDialog("转账中")
        MineApi.recycleAll {
            onSuccess {
                getUserBalance()
                mView.hidePageLoadingDialog()
                ToastUtils.showToast("转账成功")
            }
            onFailed {
                mView.hidePageLoadingDialog()
                ToastUtils.showToast(it.getMsg())
            }
        }
    }

    fun platformChange(amount:String,plateOut:String,plateIn:String){
        mView.showPageLoadingDialog("转账中")
        MineApi.platFormTrans(amount,plateOut,plateIn){
            onSuccess {
                mView.hidePageLoadingDialog()
                ToastUtils.showToast("转账成功")
                getUserBalance()
            }
            onFailed {
                mView.hidePageLoadingDialog()
                ToastUtils.showToast(it.getMsg())
            }
        }
    }

    fun setPlatformChange(isClose:Boolean){
        MineApi.setAutoChange {
            onSuccess {  }
            onFailed {
                ToastUtils.showToast(it.getMsg())
               if (isClose) mView.moneySwitch.setCheckedImmediatelyNoEvent(true) else mView.moneySwitch.setCheckedImmediatelyNoEvent(false)
            }
        }
    }

}