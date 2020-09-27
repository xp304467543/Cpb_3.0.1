package com.customer

import com.customer.bean.image.ImageViewInfo
import com.xiaojinzi.component.anno.ParameterAnno
import com.xiaojinzi.component.anno.router.HostAndPathAnno
import com.xiaojinzi.component.anno.router.RouterApiAnno
import cuntomer.constant.UserConstant


/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/20
 * @ Describe
 *
 */

@RouterApiAnno
interface ApiRouter {

    //web页面
    @HostAndPathAnno("Base/web")
    fun toGlobalWeb(
        @ParameterAnno("webActUrl") url: String,
        @ParameterAnno("isNews") isNews: Boolean = false,
        @ParameterAnno("infoId") infoId: String = ""
    )

    //充值web页面
    @HostAndPathAnno("Base/rechargeWeb")
    fun toRechargeWeb(
        @ParameterAnno("MINE_INVEST_AMOUNT") amount: Float,
        @ParameterAnno("MINE_RECHARGE_ID") id: Int,
        @ParameterAnno("MINE_RECHARGE_URL") url: String,
        @ParameterAnno("isRen") isRen: Boolean
    )

    //Login
    @HostAndPathAnno("Home/login")
    fun toLogin(@ParameterAnno("dialogLogin") loginMode: Int = 1)

    //直播间
    @HostAndPathAnno("Home/live")
    fun toLive(
        @ParameterAnno("anchorId") anchorId: String,
        @ParameterAnno("lottery_id") lottery_id: String,
        @ParameterAnno("nickName") nickname: String,
        @ParameterAnno("live_status") live_status: String,
        @ParameterAnno("online") online: String,
        @ParameterAnno("r_id") r_id: String,
        @ParameterAnno("name") name: String,
        @ParameterAnno("avatar") avatar: String
    )
    //更多视频
    @HostAndPathAnno("Home/videoMore")
    fun toVideoMore(@ParameterAnno("topStr") id: String = "0",
                    @ParameterAnno("childStr") userId: String = "0")

    //搜索视频
    @HostAndPathAnno("Home/videoSearch")
    fun toVideoSearch()

    //视频播放
    @HostAndPathAnno("Home/videoPlayer")
    fun toVideoPlay(
        @ParameterAnno("videoId") videoId: Int,
        @ParameterAnno("videoTitle") videoTitle: String
    )

    //评论详情页面
    @HostAndPathAnno("Moment/HotDiscussInfo")
    fun toMineHotDiscussInfo(
        @ParameterAnno("id") id: String,
        @ParameterAnno("userId") userId: String,
        @ParameterAnno("name") name: String,
        @ParameterAnno("gender") gender: Int,
        @ParameterAnno("avatar") avatar: String,
        @ParameterAnno("time") time: String,
        @ParameterAnno("content") content: String,
        @ParameterAnno("commentNum") commentNum: String,
        @ParameterAnno("imgList") imgList: ArrayList<ImageViewInfo>
    )

    //主播pl详情页面
    @HostAndPathAnno("Moment/AnchorInfo")
    fun toMineAnchorInfo(
        @ParameterAnno("id") id: String,
        @ParameterAnno("userId") userId: String,
        @ParameterAnno("name") name: String,
        @ParameterAnno("gender") gender: Int,
        @ParameterAnno("avatar") avatar: String,
        @ParameterAnno("time") time: String,
        @ParameterAnno("content") content: String,
        @ParameterAnno("commentNum") commentNum: String,
        @ParameterAnno("liveState") liveState: String,
        @ParameterAnno("imgList") imgList: ArrayList<ImageViewInfo>
    )

    //Setting
    @HostAndPathAnno("Mine/setting")
    fun toSetting()

    //设置密码
    @HostAndPathAnno("Mine/setPassWord")
    fun toSetPassWord(@ParameterAnno("loadMode") mode: Int)

    //修改密码
    @HostAndPathAnno("Mine/modifyPassWord")
    fun toModifyPassWord()

    //官方交流群
    @HostAndPathAnno("Mine/contentGroup")
    fun toContentGroup()

    //意见反馈
    @HostAndPathAnno("Mine/feedBack")
    fun toFeedBack()

    //推广报表
    @HostAndPathAnno("Mine/report")
    fun toReport()

    //游戏报表
    @HostAndPathAnno("Mine/reportGame")
    fun toReportGame()

    //消息中心
    @HostAndPathAnno("Mine/message")
    fun toMineMessage(
        @ParameterAnno("msg1") msg1: String,
        @ParameterAnno("msg2") msg2: String,
        @ParameterAnno("msg3") msg3: String
    )

    //消息中心详情
    @HostAndPathAnno("Mine/messageInfo")
    fun toMineMessageInfo(@ParameterAnno("msgType") msgType: Int)

    //充值
    @HostAndPathAnno("Mine/recharge")
    fun toMineRecharge(@ParameterAnno("index") msg1: Int)

    //用户
    @HostAndPathAnno("Moment/UserPersonalPage")
    fun toUserPage(@ParameterAnno(UserConstant.FOLLOW_ID) id: String)
    //主播
    @HostAndPathAnno("Moment/AnchorPersonalPage")
    fun toAnchorPage(@ParameterAnno(UserConstant.FOLLOW_ID) id: String)
    //专家
    @HostAndPathAnno("Moment/ExpertPersonalPage")
    fun toExpertPage(
        @ParameterAnno(UserConstant.FOLLOW_ID) id: String,
        @ParameterAnno(UserConstant.FOLLOW_lottery_ID) lotteryId: String)

    //彩票游戏
    @HostAndPathAnno("BetMain/gameLotteryBet")
    fun toLotteryGame(@ParameterAnno("gameLotteryId") lotteryId: String,@ParameterAnno("gameLotteryName") lotteryName: String)
}