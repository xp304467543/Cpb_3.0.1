package com.customer.data.game

import com.customer.data.UserInfoSp
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

    private const val GAME_060 ="fhchess/play"

    private const val GAME_AG = "ag/play"

    private const val GAME_AG_DZ = "ag/play-slot"




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


    /**
     * 060棋牌
     */
    fun get060(game_id:String, function: ApiSubscriber<Game060>.() -> Unit){
        val subscriber = object : ApiSubscriber<Game060>() {}
        subscriber.function()
        getApiOther()
            .post<Game060>(GAME_060)
            .headers("Authorization", UserInfoSp.getTokenWithBearer())
            .params("game_id",game_id)
            .subscribe(subscriber)
    }

    /**
     * AG视讯
     */
    fun getAg( function: ApiSubscriber<Game060>.() -> Unit){
        val subscriber = object : ApiSubscriber<Game060>() {}
        subscriber.function()
        getApiOther()
            .get<Game060>(GAME_AG)
            .headers("Authorization", UserInfoSp.getTokenWithBearer())
            .subscribe(subscriber)
    }

    /**
     * AG电子
     */
    fun getAgDZ(function: ApiSubscriber<Game060>.() -> Unit){
        val subscriber = object : ApiSubscriber<Game060>() {}
        subscriber.function()
        getApiOther()
            .get<Game060>(GAME_AG_DZ)
            .headers("Authorization", UserInfoSp.getTokenWithBearer())
            .subscribe(subscriber)
    }
}