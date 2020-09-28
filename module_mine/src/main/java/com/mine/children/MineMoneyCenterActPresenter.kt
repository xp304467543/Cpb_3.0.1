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
                    mView.tv_3_money?.text = it.bl.toString()
                    if (mView.tv_4_money.text.toString() == "维护中"){
                        mView.tvOtherMoney?.text = mView.tv_3_money.text.toString()
                    }else mView.tvOtherMoney?.text = BigDecimal(mView.tv_4_money.text.toString()).add(BigDecimal(mView.tv_3_money.text.toString())).toString()
                }
            }
            MineApi.getAgMoney {
                onSuccess {
                    mView.tv_4_money?.text = it.bl.toString()
                    try {
                        mView.tvOtherMoney?.text = BigDecimal(mView.tv_4_money.text.toString()).add(BigDecimal(mView.tv_3_money.text.toString())).toString()
                    }catch (e:Exception){
                        e.printStackTrace()
                    }
                }
                onFailed {
                    if (it.getCode() == 16) mView.tv_4_money.text = "维护中"
                    mView.tvOtherMoney?.text = mView.tv_3_money.text.toString()
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

    //上分下分  1 棋牌  2 Ag  true 上分 false 下分
    fun upAndDownMoney(index: Int, boolean: Boolean, amount: String) {
        if (index == 1) {
            MineApi.getChessMoneyUpOrDown(boolean, amount) {
                onSuccess {
                    getUserBalance()
                    mView.hidePageLoadingDialog()
                }
                onFailed {
                    ToastUtils.showToast(it.getMsg())
                    mView.hidePageLoadingDialog()
                }
            }
        } else {
            MineApi.getAgMoneyUpOrDown(boolean, amount) {
                onSuccess {
                    getUserBalance()
                    mView.hidePageLoadingDialog()
                }
                onFailed {
                    mView.hidePageLoadingDialog()
                    ToastUtils.showToast(it.getMsg())
                }
            }
        }
    }

}