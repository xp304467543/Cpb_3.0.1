package com.moment.activity

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.customer.bean.image.ImageViewInfo
import com.lib.basiclib.base.mvp.BaseMvpPresenter
import com.lib.basiclib.base.xui.widget.imageview.nine.NineGridImageView
import com.lib.basiclib.base.xui.widget.imageview.nine.NineGridImageViewAdapter
import com.lib.basiclib.base.xui.widget.imageview.preview.PreviewBuilder
import com.lib.basiclib.base.xui.widget.imageview.preview.loader.GlideMediaLoader
import com.lib.basiclib.utils.ToastUtils
import com.moment.R
import com.customer.data.moments.MomentsApi
import kotlinx.android.synthetic.main.act_hotdiscuss_info.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/20
 * @ Describe
 *
 */
class MomentHotDiscussInfoActivityPresenter : BaseMvpPresenter<MomentHotDiscussInfoActivity>() {


    fun getCommentOnList(id: String, page: Int, isRefresh: Boolean = false) {
        MomentsApi.getCommentOnList(id, page) {
            if (mView.isActive()) {
                onSuccess {
                    if (!it.isNullOrEmpty()) {
                        if (isRefresh) {
                            mView.hotDiscussInfoAdapter?.refresh(it)
                            mView.smartRefreshLayoutHotDiscussInfo.finishRefresh()
                            if (it.size < 10) mView.smartRefreshLayoutHotDiscussInfo.setEnableLoadMore(
                                false
                            )
                        } else {
                            mView.hotDiscussInfoAdapter?.loadMore(it)
                            mView.smartRefreshLayoutHotDiscussInfo.finishLoadMore()
                        }
                    } else {
                        if (isRefresh) {
                            mView.smartRefreshLayoutHotDiscussInfo.finishRefreshWithNoMoreData()
                        } else mView.smartRefreshLayoutHotDiscussInfo.finishLoadMoreWithNoMoreData()
                    }
                }

                onFailed {
                    ToastUtils.showToast(it.getMsg())
                    mView.smartRefreshLayoutHotDiscussInfo.finishRefresh()
                    mView.smartRefreshLayoutHotDiscussInfo.finishLoadMore()
                }
            }
        }
    }

    /**
     * 评论
     */
    private var commentSuccessListener: ((it: String) -> Unit)? = null
    private var commentFailedListener: ((it: String) -> Unit)? = null

    //评论
    fun commentReply(article_id: String, reply_id: String, content: String) {
        MomentsApi.setCommentReply(article_id, reply_id, content) {
            onSuccess {
                commentSuccessListener?.invoke(it)
            }
            onFailed {
                commentFailedListener?.invoke(it.getMsg().toString())
            }
        }
    }

    fun setCommentSuccessListener(CommentSuccessListener: ((it: String) -> Unit)) {
        commentSuccessListener = CommentSuccessListener
    }

    fun setCommentFailedListener(CommentFailedListener: ((it: String) -> Unit)) {
        commentFailedListener = CommentFailedListener
    }

    /**
     * 图片加载
     */
    fun initNineImg(dataList: List<ImageViewInfo>) {
        mView.nineGridImageView = mView.findViewById(R.id.hotDiscussInfoNine)
        /**
         * 图片加载
         */
        val mAdapter = object : NineGridImageViewAdapter<ImageViewInfo>() {
            override fun onDisplayImage(
                context: Context?,
                imageView: ImageView?,
                imageViewInfo: ImageViewInfo?
            ) {
                if (imageView != null) {
                    Glide.with(mView)
                        .load(imageViewInfo?.url)
                        .apply(GlideMediaLoader.getRequestOptions())
                        .into(imageView)
                }
            }
        }
        mView.nineGridImageView?.setAdapter(mAdapter)
        mView.nineGridImageView?.setItemImageClickListener { imageView, index, list ->
            if (!list.isNullOrEmpty()) {
                computeBoundsBackward(mView.nineGridImageView, list) //组成数据
                PreviewBuilder.from((imageView.context as Activity))
                    .setImgs(list)
                    .setCurrentIndex(index)
                    .setProgressColor(R.color.xui_config_color_main_theme)
                    .setType(PreviewBuilder.IndicatorType.Dot)
                    .start() //启动
            }

        }
        mView.nineGridImageView?.setImagesData(dataList)
    }

    private fun computeBoundsBackward(
        nineGridImageView: NineGridImageView<ImageViewInfo>?,
        list: List<ImageViewInfo>
    ) {
        if (nineGridImageView == null || nineGridImageView.childCount < 0) return
        for (i in 0 until nineGridImageView.childCount) {
            val itemView = nineGridImageView.getChildAt(i)
            val bounds = Rect()
            if (itemView != null) {
                val thumbView = itemView as ImageView
                thumbView.getGlobalVisibleRect(bounds)
            }
            list[i].bounds = bounds
            list[i].url = list[i].url!!
        }
    }
}