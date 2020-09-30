package com.bet.game

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.bet.R
import com.customer.adapter.HomeHotLiveAdapter
import com.customer.base.BaseNormalFragment
import com.customer.data.home.HomeHotLiveResponse
import kotlinx.android.synthetic.main.game_bet_fragment3.*

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
        initHotRecommend()
        mPresenter.getResult()
    }


    // 热门推荐
    var hotLiveAdapter:HomeHotLiveAdapter? = null

    private fun initHotRecommend() {
        val it: List<HomeHotLiveResponse>
        it = ArrayList()
        for (index in 1..6) {
            it.add(
                HomeHotLiveResponse(
                    name = "加载中...",
                    nickname = "加载中...",
                    live_intro = "加载中...",
                    online = 0,
                    red_paper_num = 0,
                    daxiu = false
                )
            )
        }
        hotLiveAdapter =HomeHotLiveAdapter()
        val gridLayoutManager = GridLayoutManager(context, 2)
        rvBetHotLive.adapter = hotLiveAdapter
        rvBetHotLive.layoutManager = gridLayoutManager
        hotLiveAdapter?.refresh(it)
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