package com.mine.children.report.game

import com.lib.basiclib.base.mvp.BaseMvpPresenter
import com.lib.basiclib.utils.ToastUtils
import com.mine.R
import com.customer.data.mine.MineApi

/**
 *
 * @ Author  QinTian
 * @ Date  2020/7/2
 * @ Describe
 *
 */
class MineGameReportMorePresenter : BaseMvpPresenter<MineGameReportMoreAct>() {

    fun getInfo(is_bl_play: String = "0", start: String, end: String) {
        MineApi.getGameLotteryInfo(is_bl_play, start, end) {
            onSuccess {
                if (mView.isActive()){
                    if (it.isNullOrEmpty()){
                        mView.lotteryAdapter?.clear()
                        mView.setVisible(R.id.place_holder)
                    }else{
                        mView.setGone(R.id.place_holder)
                        mView.lotteryAdapter?.refresh(it)
                    }

                }
            }
            onFailed {
                ToastUtils.showToast(it.getMsg())
            }
        }
    }

    fun getGameInfo(index:Int, start: String, end: String){
        MineApi.getGameInfo(index,start,end){
            onSuccess { it ->
                if (mView.isActive()){
                    if (it.isNullOrEmpty()){
                        mView.gameAdapter?.clear()
                        mView.setVisible(R.id.place_holder)
                    }else{
                        mView.setGone(R.id.place_holder)
                        mView.gameAdapter?.refresh(it)
                    }
                }
                onFailed {
                    ToastUtils.showToast(it.getMsg())
                }
            }
        }
    }
}