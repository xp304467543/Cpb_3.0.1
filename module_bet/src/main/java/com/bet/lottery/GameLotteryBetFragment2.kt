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
class GameLotteryBetFragment2 : BaseNormalFragment<GameLotteryBetFragment2Presenter>() {
    override fun attachView() = mPresenter.attachView(this)

    override fun attachPresenter() = GameLotteryBetFragment2Presenter()

    override fun initData() {
    }

    override fun getLayoutRes() = R.layout.game_bet_fragment2

    override fun initContentView() {
    }


    companion object {
        fun newInstance(lotteryId: String): GameLotteryBetFragment2 {
            val fragment = GameLotteryBetFragment2()
            val bundle = Bundle()
            bundle.putString("gameBetLotteryId", lotteryId)
            fragment.arguments = bundle
            return fragment
        }
    }
}