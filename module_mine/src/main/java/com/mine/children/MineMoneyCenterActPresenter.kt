package com.mine.children

import android.annotation.SuppressLint
import com.customer.component.dialog.GlobalDialog
import com.customer.data.mine.MineApi
import com.google.gson.JsonParser
import com.lib.basiclib.base.mvp.BaseMvpPresenter
import com.lib.basiclib.utils.ToastUtils
import kotlinx.android.synthetic.main.act_mine_money_center.*
import kotlinx.android.synthetic.main.layout_grild_game_center.*
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
    fun getUserBalance(isSelected: Boolean = false, pos: Int = -1) {
        if (mView.isActive()) {
            if (!isSelected) {
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
                        mView.tv_money_1?.text = it.bl.toString()
                        getTotalGameCenterMoney()
                    }
                    onFailed {
                        GlobalDialog.showError(mView, it)
                        getTotalGameCenterMoney()
                    }
                }

                MineApi.getBgMoney {
                    onSuccess {
                        mView.tv_bg_money?.text = it.bl.toString()
                        mView.tv_money_2?.text = it.bl.toString()
                        mView.hidePageLoadingDialog()
                        getTotalGameCenterMoney()
                    }
                    onFailed {
                        if (it.getCode() == 16) {
                            mView.tv_bg_money.text = "维护中"
                            mView.tv_money_2.text = "维护中"
                        } else ToastUtils.showToast(it.getMsg())
                        mView.hidePageLoadingDialog()
                        getTotalGameCenterMoney()
                    }
                }

                MineApi.getAgMoney {
                    onSuccess {
                        mView.tv_ag_money?.text = it.bl.toString()
                        mView.tv_money_3?.text = it.bl.toString()
                        getTotalGameCenterMoney()
                    }
                    onFailed {
                        if (it.getCode() == 16) {
                            mView.tv_ag_money.text = "维护中"
                            mView.tv_money_3.text = "维护中"
                        } else ToastUtils.showToast(it.getMsg())
                        mView.hidePageLoadingDialog()
                        getTotalGameCenterMoney()
                    }
                }

                MineApi.getKyMoney {
                    onSuccess {
                        mView.tv_money_4?.text = it.bl.toString()
                        mView.hidePageLoadingDialog()
                        getTotalGameCenterMoney()
                    }
                    onFailed {
                        if (it.getCode() == 16) mView.tv_money_4.text =
                            "维护中" else ToastUtils.showToast(it.getMsg())
                        mView.hidePageLoadingDialog()
                        getTotalGameCenterMoney()
                    }
                }

                MineApi.getSbMoney {
                    onSuccess {
                        mView.tv_money_5?.text = it.bl.toString()
                        mView.hidePageLoadingDialog()
                        getTotalGameCenterMoney()
                    }
                    onFailed {
                        if (it.getCode() == 16) mView.tv_money_5.text =
                            "维护中" else ToastUtils.showToast(it.getMsg())
                        mView.hidePageLoadingDialog()
                        getTotalGameCenterMoney()
                    }
                }
            } else {
                when (pos) {
                    1 -> {
                        MineApi.getChessMoney {
                            onSuccess {
                                mView.tv_qp_money?.text = it.bl.toString()
                                mView.tv_money_1?.text = it.bl.toString()
                                getTotalGameCenterMoney()
                            }
                            onFailed {
                                GlobalDialog.showError(mView, it)
                                getTotalGameCenterMoney()
                            }
                        }
                    }
                    2 -> {
                        MineApi.getBgMoney {
                            onSuccess {
                                mView.tv_bg_money?.text = it.bl.toString()
                                mView.tv_money_2?.text = it.bl.toString()
                                mView.hidePageLoadingDialog()
                                getTotalGameCenterMoney()
                            }
                            onFailed {
                                if (it.getCode() == 16) {
                                    mView.tv_bg_money.text = "维护中"
                                    mView.tv_money_2.text = "维护中"
                                } else ToastUtils.showToast(it.getMsg())
                                mView.hidePageLoadingDialog()
                                getTotalGameCenterMoney()
                            }
                        }
                    }
                    3 -> {
                        MineApi.getAgMoney {
                            onSuccess {
                                mView.tv_ag_money?.text = it.bl.toString()
                                mView.tv_money_3?.text = it.bl.toString()
                                getTotalGameCenterMoney()
                            }
                            onFailed {
                                if (it.getCode() == 16) {
                                    mView.tv_ag_money.text = "维护中"
                                    mView.tv_money_3.text = "维护中"
                                } else ToastUtils.showToast(it.getMsg())
                                mView.hidePageLoadingDialog()
                                getTotalGameCenterMoney()
                            }
                        }
                    }
                    4 -> {
                        MineApi.getKyMoney {
                            onSuccess {
                                mView.tv_money_4?.text = it.bl.toString()
                                mView.hidePageLoadingDialog()
                                getTotalGameCenterMoney()
                            }
                            onFailed {
                                if (it.getCode() == 16) mView.tv_money_4.text =
                                    "维护中" else ToastUtils.showToast(it.getMsg())
                                mView.hidePageLoadingDialog()
                                getTotalGameCenterMoney()
                            }
                        }
                    }
                    5 -> {
                        MineApi.getSbMoney {
                            onSuccess {
                                mView.tv_money_5?.text = it.bl.toString()
                                mView.hidePageLoadingDialog()
                                getTotalGameCenterMoney()
                            }
                            onFailed {
                                if (it.getCode() == 16) mView.tv_money_5.text =
                                    "维护中" else ToastUtils.showToast(it.getMsg())
                                mView.hidePageLoadingDialog()
                                getTotalGameCenterMoney()
                            }
                        }
                    }
                }
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
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun getTotalGameCenterMoney() {
        try {
            val qp =
                if (mView.tv_qp_money.text.toString() == "维护中") "0" else mView.tv_qp_money.text.toString()
            val ag =
                if (mView.tv_ag_money.text.toString() == "维护中") "0" else mView.tv_ag_money.text.toString()
            val bg =
                if (mView.tv_bg_money.text.toString() == "维护中") "0" else mView.tv_bg_money.text.toString()
            val ky =
                if (mView.tv_money_4.text.toString() == "维护中") "0" else mView.tv_money_4.text.toString()
            val sb =
                if (mView.tv_money_5.text.toString() == "维护中") "0" else mView.tv_money_5.text.toString()
            mView.tvOtherMoney?.text = BigDecimal(qp).add(BigDecimal(ag)).add(BigDecimal(bg)).add(BigDecimal(ky)).add(BigDecimal(sb)).toString()
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

    fun getIsAutoChange() {
        MineApi.getAutoChange {
            if (mView.isActive()) {
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
                            getUserBalance(true, 1)
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
                            getUserBalance(true, 3)
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
                            getUserBalance(true, 2)
                            mView.hidePageLoadingDialog()
                            ToastUtils.showToast("转账成功")
                        }
                        onFailed {
                            mView.hidePageLoadingDialog()
                            ToastUtils.showToast(it.getMsg())
                        }
                    }
                }
                4 -> {
                    MineApi.getKyMoneyUpOrDown(upOrDown, amount) {
                        onSuccess {
                            mView.etMoney?.setText("")
                            getUserBalance(true, 4)
                            mView.hidePageLoadingDialog()
                            ToastUtils.showToast("转账成功")
                        }
                        onFailed {
                            mView.hidePageLoadingDialog()
                            ToastUtils.showToast(it.getMsg())
                        }
                    }
                }
                5 -> {
                    MineApi.getSbMoneyUpOrDown(upOrDown, amount) {
                        onSuccess {
                            mView.etMoney?.setText("")
                            getUserBalance(true, 5)
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

    fun platformChange(amount: String, plateOut: String, plateIn: String) {
        mView.showPageLoadingDialog("转账中")
        MineApi.platFormTrans(amount, plateOut, plateIn) {
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

    fun setPlatformChange(isClose: Boolean) {
        MineApi.setAutoChange {
            onSuccess { }
            onFailed {
                ToastUtils.showToast(it.getMsg())
                if (isClose) mView.moneySwitch.setCheckedImmediatelyNoEvent(true) else mView.moneySwitch.setCheckedImmediatelyNoEvent(
                    false
                )
            }
        }
    }

}