package com.moment.children

import com.customer.bean.image.ImageViewInfo
import com.customer.bean.image.NineGridInfoAnchor
import com.lib.basiclib.base.mvp.BaseMvpFragment
import com.lib.basiclib.base.xui.adapter.recyclerview.XLinearLayoutManager
import com.lib.basiclib.base.xui.widget.imageview.nine.NineGridImageView
import com.moment.R
import com.moment.adapter.MomentsAnchorAdapter
import com.customer.data.moments.MomentsAnchorListResponse
import kotlinx.android.synthetic.main.fragment_moments_anchor.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/22
 * @ Describe
 *
 */
class MomentsAnchorFragment : BaseMvpFragment<MomentsAnchorPresenter>() {

    var page = 1

    var sNineGridPics: List<NineGridInfoAnchor>? = null

    var rvAnchorAdapter: MomentsAnchorAdapter? = null

    override fun attachView() = mPresenter.attachView(this)

    override fun attachPresenter() = MomentsAnchorPresenter()

    override fun getContentResID() = R.layout.fragment_moments_anchor

    override fun initContentView() {
        rvHotAnchor.layoutManager = XLinearLayoutManager(context)
        rvHotAnchor.adapter = MomentsAnchorAdapter(requireActivity()).also { rvAnchorAdapter = it }
        smartRefreshLayoutAnchor.setOnRefreshListener {
            this.page = 1
            if (rvAnchorAdapter != null) rvAnchorAdapter?.clear()
            mPresenter.getAnchorList(isRefresh = true)
        }
        smartRefreshLayoutAnchor.setOnLoadMoreListener {
            this.page++
            mPresenter.getAnchorList(isRefresh = false)
        }
    }

    override fun lazyInit() {
        smartRefreshLayoutAnchor.autoRefresh()
    }

    fun initNineView(data: List<MomentsAnchorListResponse>, isRefresh: Boolean) {
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
                live_status = result.live_status

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

}