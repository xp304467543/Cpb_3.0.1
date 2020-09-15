package com.customer.data.game

import com.customer.data.UserInfoSp
import com.customer.data.lottery.LotteryCodeNewResponse
import cuntomer.api.ApiSubscriber
import cuntomer.net.BaseApi

/**
 *
 * @ Author  QinTian
 * @ Date  2020/9/13
 * @ Describe
 *
 */
object GameApi : BaseApi {

    private const val GAME_ALL = "user/all-games"





    /**
     * 游戏列表
     */
    fun getAllGame( function: ApiSubscriber<ArrayList<GameAll>>.() -> Unit){
        val subscriber = object : ApiSubscriber<ArrayList<GameAll>>() {}
        subscriber.function()
        getApiOther()
            .get<ArrayList<GameAll>>(GAME_ALL)
            .headers("Authorization", UserInfoSp.getTokenWithBearer())
            .subscribe(subscriber)
    }

}