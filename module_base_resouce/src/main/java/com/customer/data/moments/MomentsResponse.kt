package com.customer.data.moments

import com.customer.data.home.HomeLiveAnchorLiveRecordBean
import com.customer.data.home.HomeLiveAnchorLotteryBean
import com.customer.data.home.HomeLiveAnchorTagListBean
import java.io.Serializable

/**
 *
 * @ Author  QinTian
 * @ Date  2020-02-09
 * @ Describe
 *
 */

data class MomentsTopBannerResponse(var title: String?, var url: String?, var img: String?)

data class MomentsHotDiscussResponse(
    var id: String?,
    var user_id: String?,
    var title: String?,
    var images: MutableList<String>? = null,
    var nickname: String?,
    var avatar: String?,
    var lottery_name: String?,
    var issue: String?,
    var like: String?,
    var comment_nums: String?,
    var created: Long = 0,
    var is_like: String?,
    var url: String?,
    var is_promote: String?,
    var gender: Int?
) : Serializable

data class UserPageResponse(
    var gift: List<UserPageGift>? = null,
    var gender: String = "",
    var created: String = "",
    var vip: String = "",
    var fans: Int? = 0,
    var follow: Int? = 0,
    var all_gift: String = "",
    var zan: String = "",
    var profile: String = "",
    var avatar: String = "",
    var nickname: String = "",
    var unique_id: String = "",
    var is_follow: Boolean
)

data class UserPageGift(var gift_name: String = "", var num: String = "", var icon: String = "")

data class AnchorPageInfoBean(
    var anchor_id: String = "",
    var avatar: String,
    var zan: Int = 0,
    var duration: String = "",
    var fans: Int = 0,
    var follow_num: Int = 0,
    var giftList: List<UserPageGift>,
    var giftNum: String = "",
    var isFollow: Boolean,
    var level: String = "",
    var liveStatus: String = "",
    var live_record: List<HomeLiveAnchorLiveRecordBean>,
    var lottery: List<HomeLiveAnchorLotteryBean>,
    var nickname: String = "",
    var sex: String = "",
    var age: Int = 0,
    var liveStartTime: Long = 0,
    var liveEndTime: Long = 0,
    var sign: String = "",
    var tagList: List<HomeLiveAnchorTagListBean>
) : Serializable


data class ExpertPageInfo(
    var id: String = "",
    var nickname: String = "",
    var avatar: String = "",
    var profile: String = "",
    var following: String = "",
    var followers: String = "",
    var win_rate: String = "",
    var profit_rate: String = "",
    var winning: String = "",
    var like: String = "",
    var speciality: String = "",
    var created: String = "",
    var win_rate_mul_100: String = "",
    var profit_rate_mul_100: String = "",
    var is_followed: String = "",
    var lottery_id: String = "",
    var lottery_name: String = ""
)

data class ExpertPageHistory(
    var id: String = "",
    var lottery_id: String = "",
    var method: String = "",
    var issue: String = "",
    var open_code: String = "",
    var code: String = "",
    var is_right: String = "",
    var created: String = ""
)

data class MomentsAnchorListResponse(
    var anchor_id: String?, var dynamic_id: String?, var media: MutableList<String>? = null,
    var text: String?, var zans: String?, var pls: String?,
    var shares: String?, var avatar: String?, var live_status: String?,
    var create_time: Long = 0, var nickname: String?, var is_zan: Boolean = false,
    var live_status_txt: String?, var isToLive: Boolean = true, var sex: Int?
) : Serializable

data class MomentsRecommend(
    var id: String?,
    var title: String?,
    var description: String?,
    var icon: String?,
    var url: String?,
    var create: String?
)


//评论列表
data class CommentOnResponse(
    var id: String?,
    var article_id: String?,
    var user_id: String?,
    var user_type: String?,
    var nickname: String?,
    var avatar: String?,
    var ori_reply_id: String?,
    var reply_id: String?,
    var reply_user_id: String?,
    var reply_user_type: String?,
    var reply_nickname: String?,
    var content: String?,
    var like: Int?,
    var today_like: String?,
    var upt_like_time: String?,
    var created: String?,
    var updated: String?,
    var is_like: String?,
    var reply: MutableList<CommentOnReplayResponse>? = null
)

//回复列表
data class CommentOnReplayResponse(
    var id: String?, var article_id: String?, var user_id: String?,
    var user_type: String?, var nickname: String?, var avatar: String?,
    var ori_reply_id: String?, var reply_id: String?, var reply_user_id: String?,
    var reply_user_type: String?, var reply_nickname: String?, var content: String?,
    var like: String?, var today_like: String?, var upt_like_time: String?,
    var created: String?, var updated: String?, var is_like: String?
)


//评论列表 davis
data class DavisCommentOnResponse(
    var comment_id: String?,
    var uid: String?,
    var pid: String?,
    var nickname: String?,
    var content: String?,
    var like: String?,
    var created: String?,
    var type: String?,
    var avatar: String?,
    var user_type: String?,
    var isanchor: Boolean,
    var is_like: Boolean,
    var reply: MutableList<DavisCommentOnReplayResponse>? = null
)

//回复列表 davis
data class DavisCommentOnReplayResponse(
    var comment_id: String?, var uid: String?, var pid: String?,
    var nickname: String?, var content: String?, var like: String?,
    var created: String?, var type: String?, var avatar: String?,
    var p_nickname: String?, var p_uid: String?, var p_user_type: String?,
    var user_type: String?, var isanchor: Boolean, var is_like: Boolean,
    var created_text: String?, var reply_user_id: String?
)

data class Zan(val is_like:Boolean,val like_num:String)
