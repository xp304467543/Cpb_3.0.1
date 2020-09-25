package com.bet.lottery

import android.os.Bundle
import com.bet.R
import com.customer.base.BaseNormalFragment

/**
 *
 * @ Author  QinTian
 * @ Date  2020/9/15
 * @ Describe
 *
 */
class GameLotteryBetFragment3 : BaseNormalFragment<GameLotteryBetFragment3Presenter>() {
    override fun attachView() = mPresenter.attachView(this)

    override fun attachPresenter() = GameLotteryBetFragment3Presenter()

    override fun initData() {
    }

    override fun getLayoutRes() = R.layout.game_bet_fragment3

    override fun initContentView() {
    }

    companion object {
        fun newInstance(lotteryId: String): GameLotteryBetFragment3 {
            val fragment = GameLotteryBetFragment3()
            val bundle = Bundle()
            bundle.putString("gameBetLotteryId", lotteryId)
            fragment.arguments = bundle
            return fragment
        }
    }
}