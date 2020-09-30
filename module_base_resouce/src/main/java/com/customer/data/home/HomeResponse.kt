package com.customer.data.home

import com.google.gson.annotations.SerializedName

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/10
 * @ Describe
 *
 */
//服务器地址
data class SystemUrl(
    val live_api: String?, val user_api: String?, val forum_api: String?,
    val lottery_api: String?, val chat_url: String?, val notice_url: String?, val movie_api: String?
)

// 轮播图
data class HomeBannerResponse(
    val type: String?, val title: String?, val name: String?,
    val image_url: String?, val content: String?, val url: String?
)

// 公告信息
data class HomeSystemNoticeResponse(
    val msgtype: String?, val trgttype: String?,
    val trgtuid: String?, val content: String?, val action: String?, val gnrtime: String?
)

// 彩种列表
data class HomeLotteryTypeResponse(
    @SerializedName("1") var s1: List<HomeTypeListResponse>?,
    @SerializedName("2") var s2: List<HomeTypeListResponse>?,
    @SerializedName("3") var s3: List<HomeTypeListResponse>?,
    @SerializedName("4") var s4: List<HomeTypeListResponse>?
)

data class HomeTypeListResponse(
    var type: String?, var anchor_id: String?, var game_id: String?,
    var name: String?, var image: String?, var image_pc: String?,
    var live_status: String?, var live_intro: String?, var online: Int?,
    var lottery_id: String?, var live_status_txt: String?
)

// 热门直播
data class HomeHotLiveResponse(
    var anchor_id: String? = "-1",
    var game_id: String? = "-1",
    var name: String?,
    var r_id: String?="-1",
    var live_status: String? = "-1",
    var nickname: String?,
    var avatar: String? = "-1",
    var tags: List<HomeExpertTags> = arrayListOf(),
    var live_intro: String?,
    var lottery_id: String? = "-1",
    var red_paper_num: Int,
    var online: Int?,
    var daxiu: Boolean?,
    var background: String? = ""
)

data class HomeExpertTags(var icon: String?, var title: String?)
data class HomeLiveAdvanceList(val title: String = "", val bean: Array<HomeLivePreResponse>)
//直播预告
data class HomeLivePreResponse(
    var aid: String? = "-1",
    var starttime: String? = "-1",
    var endtime: String? = "-1",
    var nickname: String? = "-1",
    var avatar: String? = "-1",
    var name: String? = "-1",
    var isFollow: Boolean = false,
    var livestatus: String? = "-1",
    var lottery_id: String? = "-1",
    var date: String? = "-1",
    var type: Int = 2
)

//新闻
data class HomeNewsResponse(
    var id: String? = "-1",
    var info_id: String? = "-1",
    var title: String?,
    var type: String? = "-1",
    var createtime: String? = "-1",
    var tag: String? = "-1",
    var timegap: String?,
    var type_txt: String?,
    var tag_txt: String? = "-1",
    var settype: String? = "-1",
    var cover_img: List<String>? = null
)

//新闻详情
data class HomeNesInfoResponse(
    var info_id: String?, var title: String?, var cover_img: List<String>? = null,
    var type: String?, var createtime: String?, var tag: String?,
    var detail: String?, var des: String?, var source: String?,
    var settype: String?, var timegap: String?, var type_txt: String?, var tag_txt: String?
)

//广告图
data class HomeAdResponse(
    var type: String?,
    var title: String?,
    var name: String?,
    var image_url: String?,
    var content: String?,
    var url: String?,
    var image_url_pc: String?
)

//专家列表
data class HomeExpertList(
    var id: String? = "",
    var nickname: String?,
    var avatar: String? = "",
    var win_rate: String?,
    var profit_rate: String?,
    var winning: String?,
    var last_10_games: List<String>? = null,
    var created: String? = "",
    var lottery_id: String? = "",
    var lottery_name: String?
)

//搜索推荐
data class HomeAnchorRecommend(
    var nickname: String?, var id: String?, var name: String?,
    var live_status: String?, var avatar: String?, var online: Int?, var lotteryId: Int?
)


//搜索推荐
data class HomeAnchorSearch(var result: List<HomeHotLiveResponse>?, var related: List<HomeHotLiveResponse>?)



data class Tag(var title: String?, var icon: String?)

data class BetLotteryBean(
    var betting: String,
    var customer: String,
    var gameUrl: String,
    var protocol: String,
    var bettingArr: List<String>?,
    var chessArr: List<String>?,
    var app_start_banner: StartBanner?
)

data class StartBanner(var type: String?, var image_url: String?, var url: String?)


//新消息提醒
data class MineNewMsg(var msgCount: Int, var countList: MineNewBean)
data class MineNewBean(
    @SerializedName("0") var `_$0`: String,
    @SerializedName("2") var `_$2`: String,
    @SerializedName("3") var `_$3`: String
)


/**
 * version_data : 版本信息
 * version_data.enforce : 是否强制更新 1是 0-否
 * version_data.version : 客户端版本号
 * version_data.newversion : 新版本号
 * version_data.downloadurl : 下载地址
 * version_data.packagesize : 包大小
 * version_data.upgradetext : 更新内容
 */

data class UpdateData(var version_data: Update?)

data class RedRain(var amount: String?)

data class RedTask(var prompt: Int?)

data class TaskGift(val gift_type:Int?,val amount:String?)

data class UserTask(var task_id: Int?,var title: String?,var status:Int?,var target:Int?,var jump:Int?,var archive:String?)

data class Update(
    var enforce: Int = 0,
    var version: String?,
    var newversion: String?,
    var downloadurl: String?,
    var packagesize: String?,
    var upgradetext: String?
)


//系统公告
data class SystemNotice(
    var msg_id: String?, var msg_type: String?, var content: String?, var create_time: String?
    , var createtime_txt: String?
)

//首页游戏
data class Game(var id: String?, var name: String?, var img_url: String?,var type:String?)