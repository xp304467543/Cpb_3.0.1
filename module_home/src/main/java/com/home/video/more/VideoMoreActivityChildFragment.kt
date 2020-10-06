package com.home.video.more

import android.os.Bundle
import com.customer.base.BaseNormalFragment
import com.customer.data.VideoColumnChange
import com.home.R
import com.home.video.adapter.VideoAdapter
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.thread.EventThread
import com.lib.basiclib.base.mvp.BaseMvpNavFragment
import com.lib.basiclib.base.xui.adapter.recyclerview.XGridLayoutManager
import com.lib.basiclib.utils.LogUtils
import kotlinx.android.synthetic.main.fragment_video_more.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/30
 * @ Describe
 *
 */
class VideoMoreActivityChildFragment : BaseMvpNavFragment<VideoMoreActivityChildPresenter>() {

    var mTypeId = 1
    var mCid = 1
    var mColumn = "updated"//固定值：updated=最新 reads=最多观看 praise=最多喜欢
    var mPageTag = ""
    var mPage = 1
    var mPerPage = 15
    var videoAdapter:VideoAdapter?=null

    override fun attachView()  = mPresenter.attachView(this)

    override fun attachPresenter() = VideoMoreActivityChildPresenter()

    override fun getLayoutResID() = R.layout.fragment_video_more

    override fun isOverridePage() = true

    override fun isRegisterRxBus() = true

    override fun initContentView() {
        smartRefreshLayoutVideoMore.setEnableRefresh(false)//是否启用下拉刷新功能
        smartRefreshLayoutVideoMore.setEnableLoadMore(true)//是否启用上拉加载功能
        smartRefreshLayoutVideoMore.setEnableOverScrollBounce(true)//是否启用越界回弹
        smartRefreshLayoutVideoMore.setEnableOverScrollDrag(true)//是否启用越界拖动（仿苹果效果）
        mTypeId = arguments?.getInt("typeId",-1)?:-1
        mCid = arguments?.getInt("cid",-1)?:-1
        mColumn = arguments?.getString("column")?:"updated"
        mPageTag = arguments?.getString("namePage")?:""
        rvVideoMore.layoutManager = XGridLayoutManager(context,3)
        videoAdapter = context?.let { VideoAdapter(it) }
        rvVideoMore.adapter = videoAdapter
        LogUtils.e("---VideoMoreActivityChildFragment---"+mTypeId+"----"+mCid+"---"+mColumn+"---"+mPageTag)
    }

    override fun initEvent() {
        smartRefreshLayoutVideoMore.setOnRefreshListener {
            mPage = 1
            mPresenter.getMoreVideo(mTypeId,mCid,15,false,mColumn,mPage,mPerPage,mPageTag)
        }
        smartRefreshLayoutVideoMore.setOnLoadMoreListener {
            mPage ++
            mPresenter.getMoreVideo(mTypeId,mCid,15,false,mColumn,mPage,mPerPage,mPageTag)
        }
    }

    override fun initData() {
//        smartRefreshLayoutVideoMore.autoRefresh()

        mPresenter.getMoreVideo(mTypeId,mCid,15,false,mColumn,mPage,mPerPage,mPageTag)
    }

    companion object {
        fun newInstance(typeId: Int,cid: Int,column:String,name:String): VideoMoreActivityChildFragment {
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


    //column 变化接收
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun loginOut(eventBean: VideoColumnChange) {
            mColumn = eventBean.column
        LogUtils.e("---column---"+mTypeId+"----"+mCid+"---"+mColumn+"---"+mPageTag+"isVisible="+isVisible+"isHidden"+isHidden)
        if (isVisible){
            mPresenter.getMoreVideo(mTypeId,mCid,15,false,mColumn,mPage,mPerPage,mPageTag)
        }

    }
}