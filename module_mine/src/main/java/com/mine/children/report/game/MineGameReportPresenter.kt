package com.mine.children.report.game

import android.annotation.SuppressLint
import android.util.Log
import android.widget.TextView
import com.customer.data.mine.MineApi
import com.lib.basiclib.base.mvp.BaseMvpPresenter
import com.lib.basiclib.utils.LogUtils
import com.lib.basiclib.utils.ToastUtils
import com.lib.basiclib.utils.ViewUtils
import com.mine.R
import kotlinx.android.synthetic.main.act_mine_game_report.*
import java.math.BigDecimal

/**
 *
 * @ Author  QinTian
 * @ Date  2020/6/27
 * @ Describe
 *
 */
class MineGameReportPresenter : BaseMvpPresenter<MineGameReportAct>() {

    @SuppressLint("SetTextI18n")
    fun getReport(start: String, end: String) {
        MineApi.getGameReportLast(start, end) {
            onSuccess { it ->
                if (mView.isActive()) {
                    mView.tvTotal1?.text = setMoney(it.amount)
                    mView.tvTotal_1?.text = setMoney(it.prize)
                    mView.tvTotal2?.text = setMoney(it.bl_amount)
                    mView.tvTotal_2?.text = setMoney(it.bl_prize)
                    mView.tvTotal_4?.text = setMoney(it.profit)
                    mView.tvTotal_5?.text = setMoney(it.bl_profit)
                    judgeMoney(mView.tvTotal_4,it.profit)
                    judgeMoney(mView.tvTotal_5,it.bl_profit)
                }
                onFailed {
                    ToastUtils.showToast(it.getMsg())
                }
            }
        }

        MineApi.getGameLottery(start, end) {
            onSuccess {
                if (mView.isActive()) {
                    mView.tv_lottery_1?.text = it.count.toString()
                    mView.tv_lottery_2?.text = setMoney(it.amount)
                    mView.tv_lottery_3?.text = "￥" + setMoney(it.prize)
                    mView.tv_lottery_4?.text = it.bl_count.toString()
                    mView.tv_lottery_5?.text = setMoney(it.bl_amount)
                    mView.tv_lottery_6?.text = "￥" + setMoney(it.bl_prize)
                    mView.tv_lottery_31?.text = setMoney(it.profit)
                    mView.tv_lottery_61?.text = setMoney(it.bl_profit)
                    judgeMoney( mView.tv_lottery_61,it.bl_profit)
                    judgeMoney( mView.tv_lottery_31,it.profit)
                }
            }
            onFailed {
                ToastUtils.showToast(it.getMsg())
            }
        }

        MineApi.getGamecChess(start, end) {
            onSuccess {
                if (mView.isActive()) {
                    mView.tv_qp_1?.text = it.count.toString()
                    mView.tv_qp_2?.text = setMoney(it.amount)
                    mView.tv_qp_3?.text = "￥" + setMoney(it.prize)
                    mView.tv_qp_31?.text = setMoney(it.profit)
                    judgeMoney( mView.tv_qp_31,it.profit)
                }
            }
            onFailed {
                ToastUtils.showToast(it.getMsg())
            }
        }
        MineApi.getGameAgSx(start, end) {
            onSuccess {
                if (mView.isActive()) {
                    mView.tv_agsx_1?.text = it.count.toString()
                    mView.tv_agsx_2?.text = setMoney(it.amount)
                    mView.tv_agsx_3?.text = "￥" + setMoney(it.prize)
                    mView.tv_agsx_31?.text = setMoney(it.profit)
                    judgeMoney( mView.tv_agsx_31,it.profit)
                }
            }
            onFailed {
                ToastUtils.showToast(it.getMsg())
            }
        }
        MineApi.getGameAgDz(start, end) {
            onSuccess {
                mView.tv_agdz_1?.text = it.count.toString()
                mView.tv_agdz_2?.text = setMoney(it.amount)
                mView.tv_agdz_3?.text = "￥" + setMoney(it.prize)
                mView.tv_agdz_31?.text = setMoney(it.profit)
                judgeMoney( mView.tv_agdz_31,it.profit)

            }
            onFailed {
                ToastUtils.showToast(it.getMsg())
            }
        }
        MineApi.getGameBgSx(start, end) {
            onSuccess {
                mView.tv_bgsx_1?.text = it.count.toString()
                mView.tv_bgsx_2?.text = setMoney(it.amount)
                mView.tv_bgsx_3?.text = "￥" + setMoney(it.prize)
                mView.tv_bgsx_31?.text = setMoney(it.profit)
                judgeMoney( mView.tv_bgsx_31,it.profit)
            }
            onFailed {
                ToastUtils.showToast(it.getMsg())
            }
        }
        MineApi.getGameBgFish(start, end) {
            onSuccess {
                mView.tv_bgby_1?.text = it.count.toString()
                mView.tv_bgby_2?.text = setMoney(it.amount)
                mView.tv_bgby_3?.text = "￥" + setMoney(it.prize)
                mView.tv_bgby_31?.text = setMoney(it.profit)
                judgeMoney( mView.tv_bgby_31,it.profit)
            }
            onFailed {
                ToastUtils.showToast(it.getMsg())
            }
        }
        MineApi.getGameKyqp(start, end) {
            onSuccess {
                mView.tv_kyqp_1?.text = it.count.toString()
                mView.tv_kyqp_2?.text = setMoney(it.amount)
                mView.tv_kyqp_3?.text = "￥" + setMoney(it.prize)
                mView.tv_kyqp_4?.text = setMoney(it.profit)
                judgeMoney( mView.tv_kyqp_4,it.profit)
            }
            onFailed {
                ToastUtils.showToast(it.getMsg())
            }
        }
        MineApi.getGameSbty(start, end) {
            onSuccess {
                mView.tv_sbty_1?.text = it.count.toString()
                mView.tv_sbty_2?.text = setMoney(it.amount)
                mView.tv_sbty_3?.text = "￥" + setMoney(it.prize)
                mView.tv_sbty_4?.text = setMoney(it.profit)
                judgeMoney( mView.tv_sbty_4,it.profit)
            }
            onFailed {
                ToastUtils.showToast(it.getMsg())
            }
        }
    }

    private fun setMoney(amount: BigDecimal?): String {
        if (amount?.compareTo(BigDecimal(10000)) == 1 || amount?.compareTo(BigDecimal(-10000)) == -1) {
            return "" + amount.divide(BigDecimal(10000))?.setScale(2, BigDecimal.ROUND_DOWN) + "万"
        }
        return amount.toString()
    }

    @SuppressLint("SetTextI18n")
    private fun judgeMoney(textView: TextView?, bigDecimal: BigDecimal?){
        if (bigDecimal?.compareTo(BigDecimal.ZERO)==1){
            textView?.setTextColor(ViewUtils.getColor(R.color.color_FF513E))
            textView?.text = "+" + setMoney(bigDecimal)
        }else{
            textView?.setTextColor(ViewUtils.getColor(R.color.color_333333))
        }
    }

}