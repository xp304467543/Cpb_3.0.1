package com.customer.data

import com.customer.data.mine.MineUserBankList


/**
 *
 * @ Author  QinTian
 * @ Date  2020/5/26
 * @ Describe
 *
 */
const val urlCustomer = "https://chatlink.mstatik.com/widget/standalone.html?eid=222397"

data class LotteryTypeSelect(var lotteryId: String?, var issiue: String?)

data class HomeJumpToMine(var jump:Boolean)

data class HomeJumpToMineCloseLive(var jump:Boolean)

//退出登录 设置回默认值
data class LoginOut(var loginOut:Boolean)
//跟新预告关注
data class UpDatePreView(var boolean: Boolean)

//跳转购彩nt
data class JumpToBuyLottery(var index: Int)

//用户钻石
data class MineUserDiamond(var diamond: String)

//Rx存储用户选择的银行卡
data class MineSaveBank(var data: MineUserBankList)

data class IsFirstRecharge(var res:Boolean)

//进场提醒 只给自己提醒
data class EnterVip(var vip:String)


//视频的打开或收起
data class LiveVideoClose(var closeOrOpen:Boolean)

//动画或收起
data class LiveAnimClose(var closeOrOpen:Boolean)


//直播间人数
data class OnLineInfo(var online:Int?)

data class LineCheck(var url:String,var boolean:Boolean = false)

//礼物
data class Gift(var gift:String?)


//横屏小红包提示
data class RedTips(var boolean:Boolean)

//横屏小红包点击
data class RedPaperClick(var  click:Boolean)


//礼物连数
data class GiftClickNum(var clickNum:String)

//接收横屏弹幕
data class DanMu(var userName:String,var userType:String,var text:String,var vip:String,var isMe:Boolean)

//横屏发弹幕
data class SendDanMu(var content:String)

//更新横屏钻石
data class UpDataHorDiamon(var boolean: Boolean)

//直播间@功能
data class LiveCallPersonal(var name:String)

//关注更新
data class UpDateAttention(var boolean: Boolean,var anchorId:String)


data class VideoColumnChange(val typeId:Int,var column:String)

//关注
data class AnchorAttention(val androidId:String, val isFlow:Boolean)

data class WebSelect(val pos:Int)

data class ToBetView(val pos:Int)

//投注彩种变化
data class ChangeLottery(val lotteryId:String)