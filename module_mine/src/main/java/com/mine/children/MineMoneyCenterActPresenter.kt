package com.mine.children

import android.annotation.SuppressLint
import com.customer.component.dialog.GlobalDialog
import com.customer.data.mine.MineApi
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
                }
                onFailed {
                    GlobalDialog.showError(mView, it)
                }
            }
            MineApi.getChessMoney {
                onSuccess {
                    mView.tv_qp_money?.text = it.bl.toString()
                    if (mView.tv_ag_money.text.toString() == "维护中" || mView.tv_ag_money.text.toString() == "加载中") {
                        mView.tvOtherMoney?.text = mView.tv_qp_money.text.toString()
                    } else mView.tvOtherMoney?.text =
                        BigDecimal(mView.tv_ag_money.text.toString()).add(BigDecimal(mView.tv_qp_money.text.toString()))
                            .toString()
                }
                onFailed {
                    GlobalDialog.showError(mView, it)
                }
            }
            MineApi.getAgMoney {
                onSuccess {
                    mView.tv_ag_money?.text = it.bl.toString()
                    try {
                        if (mView.tv_ag_money.text.toString() == "维护中") {
                            mView.tvOtherMoney?.text = mView.tv_qp_money.text.toString()
                        } else mView.tvOtherMoney?.text =
                            BigDecimal(mView.tv_ag_money.text.toString()).add(BigDecimal(mView.tv_qp_money.text.toString()))
                                .toString()
                        MineApi.getBgMoney {
                            onSuccess {
                                mView.tv_bg_money?.text = it.bl.toString()
                                mView.hidePageLoadingDialog()
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
                                            )).add(BigDecimal(mView.tv_ag_money.text.toString())).toString()
                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                            onFailed {
                                if (it.getCode() == 16) mView.tv_ag_money.text = "维护中"
                                mView.tvOtherMoney?.text = mView.tv_qp_money.text.toString()
                                mView.hidePageLoadingDialog()
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                onFailed {
                    if (it.getCode() == 16) mView.tv_ag_money.text = "维护中"
                    mView.tvOtherMoney?.text = mView.tv_qp_money.text.toString()
                    mView.hidePageLoadingDialog()
                }
            }

        }
    }

    fun getThird(boolean: Boolean) {
        if (mView.isActive()) {
            MineApi.getThird {
                onSuccess {
                    mView.thirdList = it
                    mView.tvMoneyGame?.text = it[0].name_cn
                    if (boolean) {
                        mView.showPickerView()
                        mView.hidePageLoadingDialog()
                    }

                }
                onFailed {
                    ToastUtils.showToast(it.getMsg())
                    if (boolean) {
                        mView.hidePageLoadingDialog()
                    }
                }
            }
        }
    }

    //上分下分  1 棋牌  2 Ag 3 Bg true 上分 false 下分
    fun upAndDownMoney(index: Int, boolean: Boolean, amount: String, isRecycle: Boolean = false) {
        if (mView.isActive()) {
            when (index) {
                1 -> {
                    MineApi.getChessMoneyUpOrDown(boolean, amount) {
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
                    MineApi.getAgMoneyUpOrDown(boolean, amount) {
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
                    MineApi.getBgMoneyUpOrDown(boolean, amount) {
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

    fun recycleAll(amount: String, amount1: String, amount2: String) {
        MineApi.getChessMoneyUpOrDown(false, amount) {
            onSuccess {

            }
            onFailed {
                ToastUtils.showToast(it.getMsg())
                mView.hidePageLoadingDialog()
            }
        }
        MineApi.getAgMoneyUpOrDown(false, amount1) {
            onSuccess {

            }
            onFailed {
                mView.hidePageLoadingDialog()
                ToastUtils.showToast(it.getMsg())
            }
        }
        MineApi.getBgMoneyUpOrDown(false,amount2){
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