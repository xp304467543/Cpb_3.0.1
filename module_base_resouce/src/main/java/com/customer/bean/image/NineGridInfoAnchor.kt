package com.customer.bean.image

import com.lib.basiclib.base.xui.widget.imageview.nine.NineGridImageView
import java.io.Serializable

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/20
 * @ Describe
 *
 */

class NineGridInfoAnchor : Serializable{
    private var mContent: String? = null
    private var mImgUrlList: List<ImageViewInfo>? = null
    private var mShowType = 1
    private var mSpanType = 0
    private var mUserAvatar: String? = null
    private var mUserName: String? = null
    private var mTime: Long? = null
    private var mCommentNum: String? = null
    private var mLikeNum: String? = null
    private var mIs_like: Boolean? = null
    private var mLive_status: String? = null
    private var mId: String? = null
    private var mUserId: String? = null
    private var mGender = 0
    private var mDynamic_id:String?=null

    constructor(
        content: String?,
        imgUrlList: List<ImageViewInfo>?,
        userAvatar: String?,
        userName: String?,
        time: Long?,
        commentNum: String?,
        likeNum: String?,
        id:String?,
        userId:String?,
        is_like: Boolean?,
        live_status: String?,
        gender: Int,
        dynamic_id:String,
        showType: Int = NineGridImageView.STYLE_FILL,
        spanType: Int = NineGridImageView.NOSPAN
    ) {
        mContent = content
        mUserAvatar = userAvatar
        mUserName = userName
        mTime = time
        mCommentNum = commentNum
        mLikeNum = likeNum
        mIs_like = is_like
        mLive_status = live_status
        mGender = gender
        mSpanType = spanType
        mShowType = showType
        mImgUrlList = imgUrlList
        mId = id
        mDynamic_id = dynamic_id
        mUserId = userId
    }

    fun getUserId(): String? {
        return mUserId
    }

    fun setUserId(User: String?) {
        mUserId = User
    }

    fun getDynamicId(): String? {
        return mDynamic_id
    }

    fun setDynamicId(dynamic_id: String?) {
        mDynamic_id = dynamic_id
    }

    fun getId(): String? {
        return mId
    }

    fun setId(id: String?) {
        mId = id
    }
    fun getContent(): String? {
        return mContent
    }

    fun setContent(content: String?) {
        mContent = content
    }

    fun getShowType(): Int {
        return mShowType
    }

    fun setShowType(showType: Int): NineGridInfoAnchor {
        mShowType = showType
        return this
    }

    fun getImgUrlList(): List<ImageViewInfo>? {
        return mImgUrlList
    }

    fun setImgUrlList(imgUrlList: List<ImageViewInfo>?) {
        mImgUrlList = imgUrlList
    }

    fun setSpanType(spanType: Int): NineGridInfoAnchor {
        mSpanType = spanType
        return this
    }

    fun getSpanType(): Int {
        return mSpanType
    }

    fun getUserAvatar(): String? {
        return mUserAvatar
    }

    fun setUserAvatar(mUserAvatar: String?) {
        this.mUserAvatar = mUserAvatar
    }

    fun getUserName(): String? {
        return mUserName
    }

    fun setUserName(mUserName: String?) {
        this.mUserName = mUserName
    }

    fun getTime(): Long? {
        return mTime
    }

    fun setTime(mTime: Long?) {
        this.mTime = mTime
    }

    fun getCommentNum(): String? {
        return mCommentNum
    }

    fun setCommentNum(mCommentNum: String?) {
        this.mCommentNum = mCommentNum
    }

    fun getLikeNum(): String? {
        return mLikeNum
    }

    fun setLikeNum(mLikeNum: String?) {
        this.mLikeNum = mLikeNum
    }

    fun getIsLike(): Boolean? {
        return mIs_like
    }

    fun setIsLike(mIs_like: Boolean?) {
        this.mIs_like = mIs_like
    }

    fun getIsLive(): String? {
        return mLive_status
    }

    fun setIsLive(live_status: String?) {
        this.mLive_status = live_status
    }

    fun getGender(): Int {
        return mGender
    }

    fun setGender(mGender: Int) {
        this.mGender = mGender
    }
}
