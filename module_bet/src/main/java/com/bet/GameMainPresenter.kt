package com.bet

import com.customer.data.game.GameApi
import com.lib.basiclib.base.mvp.BaseMvpPresenter

/**
 *
 * @ Author  QinTian
 * @ Date  2020/9/13
 * @ Describe
 *
 */
class GameMainPresenter : BaseMvpPresenter<GameMainFragment>() {

    fun getAllGame(){
        GameApi.getAllGame {
            if (mView.isActive()){
                onSuccess {
                    mView.initViewPager(it)
                }
            }
        }
    }
}