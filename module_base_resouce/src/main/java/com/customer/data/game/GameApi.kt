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

    private const val GAME_SHA_BA = "ibc/play"

    private const val GAME_KY = "ky/play"

    private const val GAME_CHESS_HISTORY = "fhchess/game-record"

    private const val GAME_AG_LIVE_HISTORY = "ag/game-record"

    private const val GAME_AG_HISTORY = "ag/slot-record"

    private const val GAME_BG_LIVE_HISTORY = "bg/live-record"

    private const val GAME_BG_FISH_HISTORY = "bg/fishing-record"

    private const val GAME_KYQI_HISTORY = "ky/game-record"

    private const val GAME_SBTY_HISTORY = "ibc/game-record"

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
     * 沙巴游戏
     */
    fun getSb(game_id:String,function: ApiSubscriber<Game060>.() -> Unit){
        val subscriber = object : ApiSubscriber<Game060>() {}
        subscriber.function()
        getApiOther()
            .get<Game060>(GAME_SHA_BA)
            .headers("Authorization", UserInfoSp.getTokenWithBearer())
            .params("is_mobile",1)
            .params("game_id",game_id)
            .subscribe(subscriber)
    }

    /**
     * 开元棋牌
     */
    fun getKy(game_id:String,function: ApiSubscriber<Game060>.() -> Unit){
        val subscriber = object : ApiSubscriber<Game060>() {}
        subscriber.function()
        getApiOther()
            .get<Game060>(GAME_KY)
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
            .get<List<GameChess>>(GAME_CHESS_HISTORY)
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
            .get<List<GameAgLive>>(GAME_AG_LIVE_HISTORY)
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
            .get<List<GameAg>>(GAME_AG_HISTORY)
            .headers("Authorization", UserInfoSp.getTokenWithBearer())
            .params("game_id",game_id)
            .params("st",st)
            .params("et",et)
            .params("page",page)
            .params("limit",20)
            .subscribe(subscriber)
    }


    /**
     * BG视讯 记录
     */
    fun getBgLiveHistory(game_id:String , st:String,et:String,page:Int,function: ApiSubscriber<List<GameAg>>.() -> Unit){
        val subscriber = object : ApiSubscriber<List<GameAg>>() {}
        subscriber.function()
        getApiOther()
            .get<List<GameAg>>(GAME_BG_LIVE_HISTORY)
            .headers("Authorization", UserInfoSp.getTokenWithBearer())
            .params("game_id",game_id)
            .params("st",st)
            .params("et",et)
            .params("page",page)
            .params("limit",20)
            .subscribe(subscriber)
    }


    /**
     * BG游戏 记录
     */
    fun getBgGameHistory(game_id:String , st:String,et:String,page:Int,function: ApiSubscriber<List<GameAg>>.() -> Unit){
        val subscriber = object : ApiSubscriber<List<GameAg>>() {}
        subscriber.function()
        getApiOther()
            .get<List<GameAg>>(GAME_BG_FISH_HISTORY)
            .headers("Authorization", UserInfoSp.getTokenWithBearer())
            .params("game_id",game_id)
            .params("st",st)
            .params("et",et)
            .params("page",page)
            .params("limit",20)
            .subscribe(subscriber)
    }

    /**
     * 开元棋牌游戏 记录
     */
    fun getKyqpGameHistory(game_id:String , st:String,et:String,page:Int,function: ApiSubscriber<List<GameAg>>.() -> Unit){
        val subscriber = object : ApiSubscriber<List<GameAg>>() {}
        subscriber.function()
        getApiOther()
            .get<List<GameAg>>(GAME_KYQI_HISTORY)
            .headers("Authorization", UserInfoSp.getTokenWithBearer())
            .params("game_id",game_id)
            .params("st",st)
            .params("et",et)
            .params("page",page)
            .params("limit",20)
            .subscribe(subscriber)
    }

    /**
     * 沙巴体育游戏 记录
     */
    fun getSbtyGameHistory(game_id:String , st:String,et:String,page:Int,function: ApiSubscriber<List<GameAg>>.() -> Unit){
        val subscriber = object : ApiSubscriber<List<GameAg>>() {}
        subscriber.function()
        getApiOther()
            .get<List<GameAg>>(GAME_SBTY_HISTORY)
            .headers("Authorization", UserInfoSp.getTokenWithBearer())
            .params("game_id",game_id)
            .params("st",st)
            .params("et",et)
            .params("page",page)
            .params("limit",20)
            .subscribe(subscriber)
    }
}