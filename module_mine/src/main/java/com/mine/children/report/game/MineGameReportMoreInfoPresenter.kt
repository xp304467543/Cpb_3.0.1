package com.mine.children.report.game


import com.lib.basiclib.base.mvp.BaseMvpPresenter
import com.lib.basiclib.utils.ToastUtils
import com.lib.basiclib.utils.ViewUtils
import com.customer.data.mine.MineApi
import kotlinx.android.synthetic.main.act_mine_game_report_more_info.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/7/3
 * @ Describe
 *
 */
class MineGameReportMoreInfoPresenter : BaseMvpPresenter<MineGameReportMoreInfoAct>() {

    fun getResponse(play_bet_state: Int,lotteryId:String,st:String="",et:String="",is_bl_play:String="0") {
        val res = MineApi.getLotteryBetHistory(play_bet_state, page = mView.index,lotteryId = lotteryId,st = st,et = et,is_bl_play =is_bl_play )
        res.onSuccess {
            if (mView.isActive()) {
                mView.smBetRecord_1.finishLoadMore()
                mView.smBetRecord_1?.finishRefresh()
                if (!it.isNullOrEmpty()) {
                    ViewUtils.setVisible(mView.recordTop_1)
                    ViewUtils.setGone(mView.tvBetRecordHolder_1)
                    if (mView.index == 1)  mView.adapter?.refresh(it) else  mView.adapter?.loadMore(it)
                } else {
                    if (mView.index == 1) {
                        mView.rvGameReportInfo?.removeAllViews()
                        ViewUtils.setVisible(mView.tvBetRecordHolder_1)
                        mView.smBetRecord_1?.setEnableAutoLoadMore(false)
                        mView.smBetRecord_1?.setEnableRefresh(false)
                    } else {
                        mView.index--
                        mView.smBetRecord_1?.setEnableAutoLoadMore(false)
                    }
                }
            }
        }
        res.onFailed {
            if (mView.isActive()) {
                mView.smBetRecord_1?.finishLoadMore()
                mView.smBetRecord_1?.finishRefresh()
                ToastUtils.showToast(it.getMsg())

            }
        }

    }
}