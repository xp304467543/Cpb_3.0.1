package com.moment.children

import com.lib.basiclib.base.fragment.BaseContentFragment
import com.lib.basiclib.base.xui.adapter.recyclerview.XLinearLayoutManager
import com.lib.basiclib.utils.ToastUtils
import com.lib.basiclib.utils.ViewUtils
import com.moment.R
import com.moment.adapter.MomentsRecommendAdapter
import com.customer.data.moments.MomentsApi
import kotlinx.android.synthetic.main.fragment_moments_anchor.*

/**
 * @ Author  QinTian
 * @ Date  2020/8/23
 * @ Describe
 *
 */
class MomentsRecommendFragment : BaseContentFragment() {

    var adapter: MomentsRecommendAdapter? = null

    override fun isSwipeBackEnable() = false

    override fun getContentResID() = R.layout.fragment_moments_anchor

    override fun initContentView() {
        rootView.setBackgroundColor(ViewUtils.getColor(R.color.white))
        smartRefreshLayoutAnchor.setEnableRefresh(true)//是否启用下拉刷新功能
        smartRefreshLayoutAnchor.setEnableLoadMore(false)//是否启用上拉加载功能
        smartRefreshLayoutAnchor.setEnableOverScrollBounce(true)//是否启用越界回弹
        smartRefreshLayoutAnchor.setEnableOverScrollDrag(true)//是否启用越界拖动（仿苹果效果）
        adapter = context?.let { MomentsRecommendAdapter(it) }
        rvHotAnchor.layoutManager = XLinearLayoutManager(context)
        rvHotAnchor.adapter = adapter
    }

    override fun lazyInit() {
        smartRefreshLayoutAnchor.autoRefresh()
        smartRefreshLayoutAnchor.setOnRefreshListener {
            getData()
        }
    }

    private fun getData() {
        MomentsApi.getRecommend {
            if (isAdded) {
                onSuccess {
                    setGone(PlaceHolder)
                    if (!it.isNullOrEmpty()) {
                        adapter?.refresh(it)
                        smartRefreshLayoutAnchor.finishRefresh()
                        smartRefreshLayoutAnchor.setEnableRefresh(false)
                    } else {
                        smartRefreshLayoutAnchor.finishRefresh()
                        ToastUtils.showToast("暂无推荐")
                    }
                }
                onFailed {
                    setGone(PlaceHolder)
                    smartRefreshLayoutAnchor.finishRefresh()
                    ToastUtils.showToast("暂无推荐")
                }
            }
        }
    }
}