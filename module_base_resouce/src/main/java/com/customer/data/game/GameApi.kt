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

    private const val GAME_BG_SX ="bg/play-live"

    private const val GAME_BG_FISH ="bg/play-fishing"

    private const val GAME_CHESS_HIS = "fhchess/game-record"

    private const val GAME_AG_LIVE = "ag/game-record"

    private const val GAME_AG_GAME = "ag/slot-record"


    /**
     * 游戏列表
     */
    fun getAllGame( ):ApiSubscriber<ArrayList<GameAll>>{
        val subscriber = object : ApiSubscriber<ArrayList<GameAll>>() {}
        getApiOther()
            .get<ArrayList<GameAll>>(GAME_ALL)
            .headers("Authorization", UserInfoSp.getTokenWithBearer())
            .subscribe(subscriber)
        return  subscriber
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

    /**
     * BG视讯
     */
    fun getBgSx(function: ApiSubscriber<Game060>.() -> Unit){
        val subscriber = object : ApiSubscriber<Game060>() {}
        subscriber.function()
        getApiOther()
            .get<Game060>(GAME_BG_SX)
            .headers("Authorization", UserInfoSp.getTokenWithBearer())
            .subscribe(subscriber)
    }


    /**
     * BG捕鱼
     */
    fun getBgFish(game_id:String,function: ApiSubscriber<Game060>.() -> Unit){
        val subscriber = object : ApiSubscriber<Game060>() {}
        subscriber.function()
        getApiOther()
            .get<Game060>(GAME_BG_FISH)
            .headers("Authorization", UserInfoSp.getTokenWithBearer())
            .params("game_id",game_id)
            .subscribe(subscriber)
    }


    /**
     * 棋牌历史 记录
     */
    fun getChessHis(game_id:String , st:String,et:String,page:Int,function: ApiSubscriber<List<GameChess>>.() -> Unit){
        val subscriber = object : ApiSubscriber<List<GameChess>>() {}
        subscriber.function()
        getApiOther()
            .get<List<GameChess>>(GAME_CHESS_HIS)
            .headers("Authorization", UserInfoSp.getTokenWithBearer())
            .params("game_id",game_id)
            .params("st",st)
            .params("et",et)
            .params("page",page)
            .params("limit",20)
            .subscribe(subscriber)
    }

    /**
     * AG视讯 记录
     */
    fun getAgLive(game_id:String , st:String,et:String,page:Int,function: ApiSubscriber<List<GameAgLive>>.() -> Unit){
        val subscriber = object : ApiSubscriber<List<GameAgLive>>() {}
        subscriber.function()
        getApiOther()
            .get<List<GameAgLive>>(GAME_AG_LIVE)
            .headers("Authorization", UserInfoSp.getTokenWithBearer())
            .params("game_id",game_id)
            .params("st",st)
            .params("et",et)
            .params("page",page)
            .params("limit",20)
            .subscribe(subscriber)
    }


    /**
     * AG游戏 记录
     */
    fun getAgGame(game_id:String , st:String,et:String,page:Int,function: ApiSubscriber<List<GameAg>>.() -> Unit){
        val subscriber = object : ApiSubscriber<List<GameAg>>() {}
        subscriber.function()
        getApiOther()
            .get<List<GameAg>>(GAME_AG_GAME)
            .headers("Authorization", UserInfoSp.getTokenWithBearer())
            .params("game_id",game_id)
            .params("st",st)
            .params("et",et)
            .params("page",page)
            .params("limit",20)
            .subscribe(subscriber)
    }
}