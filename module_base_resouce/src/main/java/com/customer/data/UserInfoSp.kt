package com.customer.data

import com.customer.component.dialog.GlobalDialog
import com.customer.data.mine.MineUserBankList
import com.customer.utils.JsonUtils
import com.lib.basiclib.utils.SpUtils
import cuntomer.constant.UserConstant
import cuntomer.them.Theme


/**
 *
 * @ Author  QinTian
 * @ Date  2020/6/5
 * @ Describe
 *
 */
object UserInfoSp {


    /**
     * 小视频初始大小
     */
    fun getVideoSize(): Int {
        return SpUtils.getInt("VideoSize", 1)
    }

    fun putVideoSize(size: Int) {
        SpUtils.putInt("VideoSize", size)
    }

    /**
     * 影视区广告
     */
    fun getMovieBanner(): String {
        return SpUtils.getString("MovieBanner").toString()
    }

    fun putMovieBanner(str: String) {
        SpUtils.putString("MovieBanner", str)
    }

    /**
     * 视频搜索tag存储
     */
    fun putVideoSearChTag(input: Set<String>) {
        SpUtils.setStringSet("VideoSearChTagSet", input)
    }

    fun clearVideoSearChTag() {
        SpUtils.clear("VideoSearChTagSet")
    }

    fun getVideoSearChTag(): List<String>? {
        return SpUtils.getStringSet("VideoSearChTagSet", emptySet())?.toList()
    }

    /**
     * 推荐红点
     */
    fun putIsShowRed(IsShow: Boolean) {
        SpUtils.putBoolean("renBall", IsShow)
    }

    fun getIsShowRed(): Boolean {
        return SpUtils.getBoolean("renBall", true)
    }

    /**
     * 是否登录
     */
    fun putIsLogin(isLogin: Boolean) {
        SpUtils.putBoolean(UserConstant.USER_LOGIN, isLogin)
        if (!isLogin) GlobalDialog.spClear()
    }

    fun getIsLogin(): Boolean {
        return SpUtils.getBoolean(UserConstant.USER_LOGIN)
    }

    /**
     * 提示音
     */
    fun putIsPlaySound(isPlay: Boolean) {
        SpUtils.putBoolean(UserConstant.PLAY_SOUND, isPlay)
    }

    fun getIsPlaySound(): Boolean {
        return SpUtils.getBoolean(UserConstant.PLAY_SOUND, true)
    }

    /**
     * token
     */
    fun putToken(token: String?) {
        token?.let { SpUtils.putString(UserConstant.TOKEN, it) }
        SpUtils.putString(UserConstant.TOKEN_WITH_BEARER, "Bearer $token")
    }

    fun getToken(): String? {
        return SpUtils.getString(UserConstant.TOKEN)
    }

    fun getTokenWithBearer(): String? {
        return SpUtils.getString(UserConstant.TOKEN_WITH_BEARER)
    }

    /**
     * 用户ID
     */

    fun putUserId(id: Int) {
        SpUtils.putInt(UserConstant.USER_ID, id)
    }

    fun getUserId(): Int {
        return SpUtils.getInt(UserConstant.USER_ID, 0)
    }

    /**
     * 用户名称 nickName
     */
    fun putUserNickName(nickName: String) {
        SpUtils.putString(UserConstant.USER_NICK_NAME, nickName)
    }

    fun getUserNickName(): String? {
        return SpUtils.getString(UserConstant.USER_NICK_NAME)
    }

    /**
     * 客服地址
     */
    fun putCustomer(customer: String) {
        SpUtils.putString("customer", customer)
    }

    fun getCustomer(): String? {
        return SpUtils.getString("customer")
    }

    /**
     * 用户名 Name
     */
    fun putUserName(Name: String) {
        SpUtils.putString(UserConstant.USER_NAME, Name)
    }

    fun getUserName(): String? {
        return SpUtils.getString(UserConstant.USER_NAME)
    }

    /**
     * 用户名头像
     */
    fun putUserPhoto(url: String) {
        SpUtils.putString(UserConstant.USER_AVATAR, url)
    }

    fun getUserPhoto(): String? {
        return SpUtils.getString(UserConstant.USER_AVATAR)
    }

    /**
     * 手机号
     */
    fun putUserPhone(phone: String) {
        SpUtils.putString(UserConstant.USER_PHONE, phone)
    }

    fun getUserPhone(): String? {
        return SpUtils.getString(UserConstant.USER_PHONE)
    }


    /**
     * 性别
     */
    fun putUserSex(sex: Int) {
        SpUtils.putInt(UserConstant.USER_SEX, sex)
    }

    fun getUserSex(): Int {
        return SpUtils.getInt(UserConstant.USER_SEX, 1)
    }

    /**
     * 个性签名
     */
    fun putUserProfile(profile: String) {
        SpUtils.putString(UserConstant.USER_PROFILE, profile)
    }

    fun getUserProfile(): String? {
        return SpUtils.getString(UserConstant.USER_PROFILE)
    }

    /**
     * 用户粉丝 关注 点赞
     */
    fun putUserFans(fans: String) {
        SpUtils.putString("UserFans", fans)
    }

    fun getUserFans(): String? {
        return SpUtils.getString("UserFans")
    }

    /**
     * 用户粉丝 关注 点赞
     */
    fun putUserUniqueId(fans: String) {
        SpUtils.putString("UniqueId", fans)
    }

    fun getUserUniqueId(): String? {
        return SpUtils.getString("UniqueId")
    }

    /**
     * 是否提示送礼物信息
     */
    fun putSendGiftTips(boolean: Boolean) {
        SpUtils.putBoolean("GiftTips", boolean)
    }

    fun getSendGiftTips(): Boolean {
        return SpUtils.getBoolean("GiftTips", true)
    }

    /**
     * 是否设置支付密码
     */

    fun putIsSetPayPassWord(boolean: Boolean) {
        SpUtils.putBoolean("PayPassWord", boolean)
    }

    fun getIsSetPayPassWord(): Boolean {
        return SpUtils.getBoolean("PayPassWord", false)
    }

    /**
     * 是否首冲
     */
    fun putIsFirstRecharge(boolean: Boolean) {
        SpUtils.putBoolean("IsFirstRecharge", boolean)
    }

    fun getIsFirstRecharge(): Boolean {
        return SpUtils.getBoolean("IsFirstRecharge", true)
    }

    /**
     * 弹幕开关
     */
    fun putDanMuSwitch(boolean: Boolean) {
        SpUtils.putBoolean("DanMuSwitch", boolean)
    }

    fun getDanMuSwitch(): Boolean {
        return SpUtils.getBoolean("DanMuSwitch", true)
    }

    /**
     * 首页引导图
     */
    fun putMainGuide(boolean: Boolean) {
        SpUtils.putBoolean("MainGuide", boolean)
    }

    fun getMainGuide(): Boolean {
        return SpUtils.getBoolean("MainGuide", false)
    }

    /**
     * 开奖引导图
     */
    fun putOpenCodeGuide(boolean: Boolean) {
        SpUtils.putBoolean("OpenCodeGuide", boolean)
    }

    fun getOpenCodeGuide(): Boolean {
        return SpUtils.getBoolean("OpenCodeGuide", false)
    }

    /**
     * 我的界面引导图
     */
    fun putMineGuide(boolean: Boolean) {
        SpUtils.putBoolean("MineGuide", boolean)
    }

    fun getMineGuide(): Boolean {
        return SpUtils.getBoolean("MineGuide", false)
    }

    /**
     * 关注引导图
     */
    fun putAttentionGuide(boolean: Boolean) {
        SpUtils.putBoolean("AttentionGuide", boolean)
    }

    fun getAttentionGuide(): Boolean {
        return SpUtils.getBoolean("AttentionGuide", false)
    }

    /**
     * random_str
     */
    fun putRandomStr(random_str: String?) {
        random_str?.let { SpUtils.putString("random_str", it) }
    }

    fun getRandomStr(): String? {
        return SpUtils.getString("random_str", "")
    }

    /**
     * 打赏引导图
     */
    fun putRewardnGuide(boolean: Boolean) {
        SpUtils.putBoolean("RewardnGuide", boolean)
    }

    fun getRewardnGuide(): Boolean {
        return SpUtils.getBoolean("RewardnGuide", false)
    }

    /**
     * 直播引导图
     */
    fun putLiveGuide(boolean: Boolean) {
        SpUtils.putBoolean("MineGuide", boolean)
    }

    fun getLiveeGuide(): Boolean {
        return SpUtils.getBoolean("MineGuide", false)
    }

    /**
     * 是否悬浮
     */
    fun isAboveLive(boolean: Boolean) {
        SpUtils.putBoolean("AboveLive", boolean)
    }

    fun getIsAboveLive(): Boolean {
        return SpUtils.getBoolean("AboveLive", false)
    }

    /**
     * 记录Vip等级
     */
    fun setVipLevel(str: String) {
        SpUtils.putString("VipLevel", str)
    }

    fun getVipLevel(): String? {
        return SpUtils.getString("VipLevel", "0")
    }

    /**
     * 记录Vip等级 0-用户 1-主播 2-管理
     */
    fun setUserType(boolean: String) {
        SpUtils.putString("UserType", boolean)
    }

    fun getUserType(): String? {
        return SpUtils.getString("UserType", "0")
    }

    /**
     * 悬浮按钮
     */
    fun putOpenWindow(boolean: Boolean) {
        SpUtils.putBoolean("openWindow", boolean)
    }

    fun getOpenWindow(): Boolean {
        return SpUtils.getBoolean("openWindow", true)
    }

    /**
     * 主题 1-默认 2-春节 3-中秋 4-情人节
     */
    fun putThem(int: Int) {
        SpUtils.putInt("ThemSelect", int)
    }

    fun getThem(): Theme {
        return when (SpUtils.getInt("ThemSelect", 1)) {
            1 -> {
                Theme.Default
            }
            2 -> {
                Theme.NewYear
            }
            3 -> {
                Theme.MidAutumn
            }
            4 -> {
                Theme.LoverDay
            }
            else -> Theme.Default
        }
    }

    fun getThemInt(): Int {
        return (SpUtils.getInt("ThemSelect", 1))
    }

    /**
     * 银行卡信息记录
     */
    fun putSelectBankCard(mineBank: MineUserBankList) {
        SpUtils.putString(UserConstant.USER_BANK_SELECT, JsonUtils.toJson(mineBank))
    }

    fun getSelectBankCard(): MineUserBankList? {
        return if (SpUtils.getString(UserConstant.USER_BANK_SELECT) != "") {
            JsonUtils.fromJson(
                SpUtils.getString(UserConstant.USER_BANK_SELECT).toString(),
                MineUserBankList::class.java
            )
        } else null

    }

    /**
     * 显示动画效果
     */

    fun putIsShowAnim(boolean: Boolean) {
        SpUtils.putBoolean("IsShowAnim", boolean)
    }

    fun getIsShowAnim(): Boolean {
        return SpUtils.getBoolean("IsShowAnim", true)
    }

}