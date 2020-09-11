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

class NineGridInfo : Serializable{
    private var mContent: String? = null
    private var mImgUrlList: List<ImageViewInfo>? = null
    private var mShowType = 1
    private var mSpanType = 0
    private var mUserAvatar: String? = null
    private var mUserName: String? = null
    private var mTime: Long? = null
    private var mCommentNum: String? = null
    private var mLikeNum: String? = null
    private var mIs_like: String? = null
    private var mIs_promote: String? = null
    private var mId: String? = null
    private var mUserId: String? = null
    private var mGender = 0

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
        is_like: String?,
        is_promote: String?,
        gender: Int,
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
        mIs_promote = is_promote
        mGender = gender
        mSpanType = spanType
        mShowType = showType
        mImgUrlList = imgUrlList
        mId = id
        mUserId = userId
    }

    fun getUserId(): String? {
        return mUserId
    }

    fun setUserId(User: String?) {
        mUserId = User
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

    fun setShowType(showType: Int): NineGridInfo {
        mShowType = showType
        return this
    }

    fun getImgUrlList(): List<ImageViewInfo>? {
        return mImgUrlList
    }

    fun setImgUrlList(imgUrlList: List<ImageViewInfo>?) {
        mImgUrlList = imgUrlList
    }

    fun setSpanType(spanType: Int): NineGridInfo {
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

    fun getIsLike(): String? {
        return mIs_like
    }

    fun setIsLike(mIs_like: String?) {
        this.mIs_like = mIs_like
    }

    fun getIsPromote(): String? {
        return mIs_promote
    }

    fun setIsPromote(mIs_promote: String?) {
        this.mIs_promote = mIs_promote
    }

    fun getGender(): Int {
        return mGender
    }

    fun setGender(mGender: Int) {
        this.mGender = mGender
    }
}
