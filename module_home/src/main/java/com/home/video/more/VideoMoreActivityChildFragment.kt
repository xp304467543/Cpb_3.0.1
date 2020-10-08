package com.home.video.more

import android.os.Bundle
import com.customer.base.BaseNormalFragment
import com.home.R
import com.home.video.adapter.VideoAdapter
import com.lib.basiclib.base.xui.adapter.recyclerview.XGridLayoutManager
import kotlinx.android.synthetic.main.fragment_video_more.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/30
 * @ Describe
 *
 */
class VideoMoreActivityChildFragment : BaseNormalFragment<VideoMoreActivityChildPresenter>() {

    var mTypeId = 1
    var mCid = 1
    var mColumn = "updated"//固定值：updated=最新 reads=最多观看 praise=最多喜欢
    var mPageTag = ""
    var mPage = 1
    var mPerPage = 15
    var videoAdapter: VideoAdapter? = null



    override fun attachView() = mPresenter.attachView(this)

    override fun attachPresenter() = VideoMoreActivityChildPresenter()

    override fun getLayoutRes() = R.layout.fragment_video_more



    override fun isRegisterRxBus() = true

    override fun initContentView() {
        smartRefreshLayoutVideoMore.setEnableRefresh(false)//是否启用下拉刷新功能
        smartRefreshLayoutVideoMore.setEnableLoadMore(true)//是否启用上拉加载功能
        smartRefreshLayoutVideoMore.setEnableOverScrollBounce(true)//是否启用越界回弹
        smartRefreshLayoutVideoMore.setEnableOverScrollDrag(true)//是否启用越界拖动（仿苹果效果）
        mTypeId = arguments?.getInt("typeId", -1) ?: -1
        mCid = arguments?.getInt("cid", -1) ?: -1
        mColumn = arguments?.getString("column") ?: "updated"
        mPageTag = arguments?.getString("namePage") ?: ""
        rvVideoMore.layoutManager = XGridLayoutManager(context, 3)
        videoAdapter = context?.let { VideoAdapter(it) }
        rvVideoMore.adapter = videoAdapter

    }

    override fun initEvent() {
        smartRefreshLayoutVideoMore.setOnRefreshListener {
            mPage = 1
            mPresenter.getMoreVideo(mTypeId, mCid, 15, false, mColumn, mPage, mPerPage, mPageTag)
        }
        smartRefreshLayoutVideoMore.setOnLoadMoreListener {
            mPage++
            mPresenter.getMoreVideo(mTypeId, mCid, 15, false, mColumn, mPage, mPerPage, mPageTag)
        }
    }

    override fun initData() {
    }

    companion object {
        fun newInstance(
            typeId: Int,
            cid: Int,
            column: String,
            name: String
        ): VideoMoreActivityChildFragment {
            val fragment = VideoMoreActivityChildFragment()
            val bundle = Bundle()
            bundle.putInt("typeId", typeId)
            bundle.putInt("cid", cid)
            bundle.putString("column", column)
            bundle.putString("namePage", name)
            fragment.arguments = bundle
            return fragment
        }
    }

    var isInit = false
    fun upDateView(column:String,isTopClick:Boolean = false){
        mPage = 1
        if (isInit){
            if (this.mColumn!=column || isTopClick){
                this.mColumn = column
                mPresenter.getMoreVideo(mTypeId, mCid, 15, false, column, mPage, mPerPage, mPageTag)
                isInit = true
            }
        }else {
            this.mColumn = column
            mPresenter.getMoreVideo(mTypeId, mCid, 15, false, column, mPage, mPerPage, mPageTag)
            isInit = true
        }
    }

}