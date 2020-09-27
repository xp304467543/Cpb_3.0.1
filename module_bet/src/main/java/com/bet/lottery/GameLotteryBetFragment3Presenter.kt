package com.bet.lottery

import com.customer.data.home.HomeApi
import com.lib.basiclib.base.mvp.BaseMvpPresenter
import com.rxnetgo.rxcache.CacheMode
import kotlinx.android.synthetic.main.game_bet_fragment3.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/9/15
 * @ Describe
 *
 */
class GameLotteryBetFragment3Presenter : BaseMvpPresenter<GameLotteryBetFragment3>() {


    fun getResult(){
       val home = HomeApi.getHomeHotLive(CacheMode.NONE)
        home.onSuccess {
            if (mView.isAdded){
                mView.hotLiveAdapter?.clear()
                mView.rvBetHotLive?.removeAllViews()
                if (it.size > 10) mView.hotLiveAdapter?.refresh(it.subList(0, 10)) else mView.hotLiveAdapter?.refresh(it) }
            }
        }
    }
