package com.moment.children

import com.customer.bean.image.ImageViewInfo
import com.customer.bean.image.NineGridInfo
import com.lib.basiclib.base.mvp.BaseMvpFragment
import com.lib.basiclib.base.xui.adapter.recyclerview.XLinearLayoutManager
import com.lib.basiclib.base.xui.widget.imageview.nine.NineGridImageView
import com.moment.R
import com.moment.adapter.MomentsHotDiscussAdapter
import com.customer.data.moments.MomentsHotDiscussResponse
import kotlinx.android.synthetic.main.fragment_moments_hot_discuss.*
import kotlin.collections.ArrayList

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/19
 * @ Describe
 *
 */
class MomentsHotDiscussFragment : BaseMvpFragment<MomentsHotDiscussPresenter>() {

    var page = 1

    private var sNineGridPics: List<NineGridInfo>? = null

    private var rvDiscussAdapter: MomentsHotDiscussAdapter? = null

    override fun attachView() = mPresenter.attachView(this)

    override fun attachPresenter() = MomentsHotDiscussPresenter()

    override fun getContentResID() = R.layout.fragment_moments_hot_discuss

    override fun initContentView() {
        rvHotDiscuss.layoutManager = XLinearLayoutManager(context)
        rvHotDiscuss.adapter =
            MomentsHotDiscussAdapter(requireActivity()).also { rvDiscussAdapter = it }
    }

    override fun lazyInit() {
        smartRefreshLayoutHotDiscuss.autoRefresh()
    }

    override fun initEvent() {
        smartRefreshLayoutHotDiscuss.setOnRefreshListener {
            this.page = 1
            if (rvDiscussAdapter != null) rvDiscussAdapter?.clear()
            mPresenter.getHotDiscuss(true)
        }
        smartRefreshLayoutHotDiscuss.setOnLoadMoreListener {
            this.page++
            mPresenter.getHotDiscuss(false)
        }
    }


    fun initNineView(data: List<MomentsHotDiscussResponse>, isRefresh: Boolean) {
        sNineGridPics = getMediaDemos(data)
        if (!isRefresh) rvDiscussAdapter?.loadMore(sNineGridPics) else rvDiscussAdapter?.refresh(
            sNineGridPics
        )
    }


    private fun getMediaDemos(data: List<MomentsHotDiscussResponse>): MutableList<NineGridInfo>? {
        val list: MutableList<NineGridInfo> = ArrayList()
        var info: NineGridInfo
        for (result in data) {
            info = NineGridInfo(
                content = result.title,
                imgUrlList = getPicture(result.images),
                userAvatar = result.avatar,
                userName = result.nickname,
                time = result.created,
                commentNum = result.comment_nums,
                likeNum = result.like,
                id = result.id,
                userId = result.user_id,
                is_like = result.is_like,
                is_promote = result.is_promote,
                gender = result.gender ?: 0
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