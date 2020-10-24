package com.personal

import android.os.Bundle
import android.view.View
import com.customer.bean.image.ImageViewInfo
import com.customer.bean.image.NineGridInfoAnchor
import com.customer.data.moments.MomentsAnchorListResponse
import com.customer.data.moments.MomentsApi
import com.lib.basiclib.base.fragment.BaseContentFragment
import com.lib.basiclib.base.xui.adapter.recyclerview.XLinearLayoutManager
import com.lib.basiclib.base.xui.widget.imageview.nine.NineGridImageView
import com.lib.basiclib.utils.ToastUtils
import com.moment.R
import com.moment.adapter.MomentsAnchorAdapter
import kotlinx.android.synthetic.main.fragment_moments_anchor.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/27
 * @ Describe
 *
 */
class AnchorPersonalPageData2 : BaseContentFragment() {

    var page = 1

    var sNineGridPics: List<NineGridInfoAnchor>? = null

    var rvAnchorAdapter: MomentsAnchorAdapter? = null

    override fun getContentResID() = R.layout.fragment_moments_anchor

    override fun isSwipeBackEnable() = false

    override fun initContentView() {
        rvHotAnchor.layoutManager = XLinearLayoutManager(context)
        rvHotAnchor.adapter = MomentsAnchorAdapter(requireActivity()).also { rvAnchorAdapter = it }
        smartRefreshLayoutAnchor.setOnRefreshListener {
            this.page = 1
            if (rvAnchorAdapter != null) rvAnchorAdapter?.clear()
            getAnchorDynamic(arguments?.getString("anchor_id")?:"0",isRefresh = true)
        }
        smartRefreshLayoutAnchor.setOnLoadMoreListener {
            this.page++
            getAnchorDynamic(arguments?.getString("anchor_id")?:"0",isRefresh = false)
        }
    }

    override fun lazyInit() {
        smartRefreshLayoutAnchor.autoRefresh()
    }


    private fun getAnchorDynamic(anchor_id: String, isRefresh: Boolean) {
        MomentsApi.getAnchorDynamic(anchor_id, "10", page) {
            onSuccess {
                setGone(PlaceHolder)

                if (it.isNotEmpty()) initNineView(it, isRefresh) else {
                    if (!isRefresh) smartRefreshLayoutAnchor.finishLoadMoreWithNoMoreData()
                }
                if (smartRefreshLayoutAnchor != null) {
                    smartRefreshLayoutAnchor.finishLoadMore()
                    smartRefreshLayoutAnchor.finishRefresh()
                }
            }
            onFailed {
                setGone(PlaceHolder)
                if (smartRefreshLayoutAnchor != null) {
                    smartRefreshLayoutAnchor.finishLoadMore()
                    smartRefreshLayoutAnchor.finishRefresh()
                }
                if (page != 1) page--
                ToastUtils.showToast("数据获取失败")
            }
        }
    }

    private fun initNineView(data: List<MomentsAnchorListResponse>, isRefresh: Boolean) {
        sNineGridPics = getMediaDemos(data)
        if (!isRefresh) rvAnchorAdapter?.loadMore(sNineGridPics) else rvAnchorAdapter?.refresh(
            sNineGridPics
        )
    }


    private fun getMediaDemos(data: List<MomentsAnchorListResponse>): MutableList<NineGridInfoAnchor>? {
        val list: MutableList<NineGridInfoAnchor> = ArrayList()
        var info: NineGridInfoAnchor
        for (result in data) {
            info = NineGridInfoAnchor(
                content = result.text,
                imgUrlList = getPicture(result.media),
                userAvatar = result.avatar,
                userName = result.nickname,
                time = result.create_time,
                commentNum = result.pls,
                likeNum = result.zans,
                id = result.dynamic_id,
                userId = result.anchor_id,
                is_like = result.is_zan,
                gender = result.sex ?: 0,
                live_status = result.live_status,
                dynamic_id = result.dynamic_id.toString()

            ).setShowType(NineGridImageView.STYLE_GRID)
            list.add(info)
        }
        return list
    }

    private fun getPicture(url: MutableList<String>?): ArrayList<ImageViewInfo> {
        val list: ArrayList<ImageViewInfo> = ArrayList()
        if (url != null) {
            for (i in url) {
                list.add(ImageViewInfo(i))
            }
        }
        return list
    }

    companion object{
        fun newInstance(anchor_id: String): AnchorPersonalPageData2 {
            val fragment = AnchorPersonalPageData2()
            val bundle = Bundle()
            bundle.putString("anchor_id", anchor_id)
            fragment.arguments = bundle
            return fragment
        }
    }
}