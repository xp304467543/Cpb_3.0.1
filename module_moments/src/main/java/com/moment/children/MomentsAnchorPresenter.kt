package com.moment.children

import com.lib.basiclib.base.mvp.BaseMvpPresenter
import com.lib.basiclib.utils.ToastUtils
import com.customer.data.moments.MomentsApi
import kotlinx.android.synthetic.main.fragment_moments_anchor.*
import kotlinx.android.synthetic.main.fragment_moments_anchor.PlaceHolder

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/22
 * @ Describe
 *
 */
class MomentsAnchorPresenter : BaseMvpPresenter<MomentsAnchorFragment>() {



    //主播动态
    fun getAnchorList(anchor_id: String = "0",isRefresh: Boolean) {
        MomentsApi.getAnchorMoments(anchor_id, "10", mView.page) {
            if (mView.isActive()) {
                onSuccess {
                    mView.setGone(mView.PlaceHolder)
                    if (it.isNotEmpty()) mView.initNineView(it, isRefresh) else {
                        if (!isRefresh) mView.smartRefreshLayoutAnchor.finishLoadMoreWithNoMoreData()
                    }
                    if (mView.smartRefreshLayoutAnchor != null) {
                        mView.smartRefreshLayoutAnchor.finishLoadMore()
                        mView.smartRefreshLayoutAnchor.finishRefresh()
                    }
                }
                onFailed {
                    mView.setGone(mView.PlaceHolder)
                    if (mView.smartRefreshLayoutAnchor != null) {
                        mView.smartRefreshLayoutAnchor.finishLoadMore()
                        mView.smartRefreshLayoutAnchor.finishRefresh()
                    }
                    if (mView.page != 1) mView.page--
                    ToastUtils.showToast("数据获取失败")
                }
            }
        }
    }
}