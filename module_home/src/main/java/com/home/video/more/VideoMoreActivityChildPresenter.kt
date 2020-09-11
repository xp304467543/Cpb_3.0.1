package com.home.video.more

import com.customer.data.video.MovieApi
import com.lib.basiclib.base.mvp.BaseMvpPresenter
import com.lib.basiclib.utils.ViewUtils
import kotlinx.android.synthetic.main.fragment_video_more.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/30
 * @ Describe
 *
 */
class VideoMoreActivityChildPresenter : BaseMvpPresenter<VideoMoreActivityChildFragment>() {

    fun getMoreVideo(
        typeId: Int,
        cid: Int,
        num: Int,
        isMore: Boolean,
        column: String,
        page: Int,
        prePage: Int
    ) {
        MovieApi.getVideoMore(typeId, cid, num, isMore, column, page, prePage) {
            if (mView.isAdded) {
                onSuccess {
                    if (it.isNullOrEmpty()) {
                        if (mView.mPage == 1) {
                            //显示无数据页面
                            ViewUtils.setVisible(mView.videoHolder)
                            if (mView.smartRefreshLayoutVideoMore != null) mView.smartRefreshLayoutVideoMore.finishRefreshWithNoMoreData()
                        } else {
                            if (mView.smartRefreshLayoutVideoMore != null) mView.smartRefreshLayoutVideoMore.finishLoadMoreWithNoMoreData()
                        }
                    } else {
                        ViewUtils.setGone(mView.videoHolder)
                        if (mView.mPage == 1) mView.videoAdapter?.refresh(it) else mView.videoAdapter?.loadMore(
                            it
                        )
                    }
                    if (mView.smartRefreshLayoutVideoMore != null) {
                        mView.smartRefreshLayoutVideoMore.finishRefresh()
                        mView.smartRefreshLayoutVideoMore.finishLoadMore()
                    }
                }
                onFailed {
                    ViewUtils.setVisible(mView.videoHolder)
                    if (mView.smartRefreshLayoutVideoMore != null) {
                        mView.smartRefreshLayoutVideoMore.finishRefresh()
                        mView.smartRefreshLayoutVideoMore.finishLoadMore()
                    }
                }
            }
        }
    }
}