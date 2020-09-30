package com.mine.children.report.game

import android.annotation.SuppressLint
import com.lib.basiclib.base.mvp.BaseMvpPresenter
import com.lib.basiclib.utils.ToastUtils
import com.customer.data.mine.MineApi
import kotlinx.android.synthetic.main.act_mine_game_report.*

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
                    mView.tvTotal1?.text = it.amount
                    mView.tvTotal_1?.text = it.prize
                    mView.tvTotal2?.text = it.bl_amount
                    mView.tvTotal_2?.text = it.bl_prize
                }
                onFailed {
                    ToastUtils.showToast(it.getMsg())
                }
            }
        }

        MineApi.getGameLottery(start, end) {
            onSuccess {
                if (mView.isActive()) {
                    mView.tv_lottery_1?.text = it.count
                    mView.tv_lottery_2?.text = it.amount
                    mView.tv_lottery_3?.text = "￥ "+ it.prize
                    mView.tv_lottery_4?.text = it.bl_count
                    mView.tv_lottery_5?.text = it.bl_amount
                    mView.tv_lottery_6?.text = "￥ "+it.bl_prize
                }
            }
            onFailed {
                ToastUtils.showToast(it.getMsg())
            }
        }

        MineApi.getGamecChess(start, end){
            onSuccess {
                if (mView.isActive()) {
                    mView.tv_qp_1?.text = it.count
                    mView.tv_qp_2?.text = it.amount
                    mView.tv_qp_3?.text = "￥ "+ it.prize
                }
            }
            onFailed {
                ToastUtils.showToast(it.getMsg())
            }
        }
        MineApi.getGameAgSx(start, end){
            onSuccess {
                if (mView.isActive()) {
                    mView.tv_agsx_1?.text = it.count
                    mView.tv_agsx_2?.text = it.amount
                    mView.tv_agsx_3?.text = "￥ "+ it.prize
                }
            }
            onFailed {
                ToastUtils.showToast(it.getMsg())
            }
        }
        MineApi.getGameAgDz(start, end){
            onSuccess {
                mView.tv_agdz_1?.text = it.count
                mView.tv_agdz_2?.text = it.amount
                mView.tv_agdz_3?.text = "￥ "+ it.prize
            }
            onFailed {
                ToastUtils.showToast(it.getMsg())
            }
        }
    }

}