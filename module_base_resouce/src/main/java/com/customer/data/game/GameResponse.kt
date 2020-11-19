package com.customer.data.game

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 *
 * @ Author  QinTian
 * @ Date  2020/9/13
 * @ Describe
 *
 */


@Parcelize
data class GameAll(val name:String?,val list:List<GameAllChild0>?): Parcelable


@Parcelize
data class GameAllChild0(val name:String?, val list:ArrayList<GameAllChild1>?, val type:String?, val id:String?, val img_url:String?, val tag:String?, val remark:String?,
                         var isOpen:Boolean = false): Parcelable

@Parcelize
data class GameAllChild1(val type:String?, val id:String?, val img_url:String?, val name:String?, val itemType:Int = 0, val tag:String?, val remark:String?,
                         var isOpen:Boolean = false): Parcelable


data class GameRecently(val data1:GameAllChild1?,val data2:GameAllChild1?,val data3:GameAllChild1?)


data class Game060(val url:String?)

//"id": 3,
//"user_id": 25,
//"nickname": "abc1234",
//"chair_id": 1,
//"prize": "-4.80",
//"play_time_count": 126,
//"play_time": 1598002633,
//"curr_score": "119995.20",
//"amount": "4.80",
//"order_no": "02146-1598002507-00015280",
//"sz_server_name": "斗地主_初级场",
//"kind_id": 1,
//"server": 2146,
//"total_bet": "0.00",
//"bet_detail": "N‘’",
//"is_audited": 0,
//"created_at": 1598009486,
//"audited_at": 0
data class GameChess(val play_time:Long?,val sz_server_name:String?,val prize:String?,val amount:String?)

data class GameAgLive(val bet_time:Long?,val game_name:String?,val prize:String?,val amount:String?)

data class GameAg(val billtime:Long?,val game_name:String?,val prize:String?,val amount:String?)