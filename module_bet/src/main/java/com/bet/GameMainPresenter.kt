package com.bet

import com.customer.data.game.GameApi
import com.customer.data.home.HomeApi
import com.customer.data.lottery.LotteryApi
import com.lib.basiclib.base.mvp.BaseMvpPresenter
import com.rxnetgo.rxcache.CacheMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 *
 * @ Author  QinTian
 * @ Date  2020/9/13
 * @ Describe
 *
 */
class GameMainPresenter : BaseMvpPresenter<GameMainFragment>() {

    fun getAllGame(){
            if (mView.isActive()){
                val uiScope = CoroutineScope(Dispatchers.Main)

                uiScope.launch {

                    val getLotteryType = async { GameApi.getAllGame() }

                    val getHomeSystemNoticeResult = async { HomeApi.getHomeSystemNoticeResult(CacheMode.NONE) }

                    val resultGetLotteryType = getLotteryType.await()

                    val resultGetHomeSystemNoticeResult = getHomeSystemNoticeResult.await()

                    resultGetHomeSystemNoticeResult.onSuccess { mView.upDateSystemNotice(it) }

                    resultGetLotteryType.onSuccess {
                        if (it.isNotEmpty()) {
                            mView.initViewPager(it)
                        }
                    }
                }

            }
    }
}