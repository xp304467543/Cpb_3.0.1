package com.home.video

import android.os.Bundle
import com.customer.base.BaseNormalFragment
import com.customer.data.mine.ChangeSkin
import com.customer.data.video.*
import com.home.R
import com.home.video.adapter.VideoTypeAdapter
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.thread.EventThread
import com.lib.basiclib.base.xui.adapter.recyclerview.XLinearLayoutManager
import kotlinx.android.synthetic.main.fragment_home_video_child.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/29
 * @ Describe
 *
 */
class VideoChildFragment : BaseNormalFragment<VideoChildPresenter>() {

    private var itemData: ArrayList<MovieTypeChild>? = null
    private var bannerData: VideoBannerForDataChild? = null
    private var videoId = -1
    private var videoPid = -1
    private var currentName = "精品"

    private var movieTypeAdapter: VideoTypeAdapter? = null

    override fun attachView() = mPresenter.attachView(this)

    override fun attachPresenter() = VideoChildPresenter()

    override fun getLayoutRes() = R.layout.fragment_home_video_child

    override fun isRegisterRxBus() = true


    override fun initContentView() {
        itemData = arguments?.getParcelableArrayList("videoTypeData")
        bannerData = arguments?.getParcelable("videoBannerData")
        videoId = arguments?.getInt("videoId", -1) ?: -1
        videoPid = arguments?.getInt("videoPid", -1) ?: -1
        currentName =arguments?.getString("currentName")?:"精品"
        videoSmartRefreshLayout.setEnableRefresh(true)//是否启用下拉刷新功能
        videoSmartRefreshLayout.setEnableLoadMore(false)//是否启用上拉加载功能
        videoSmartRefreshLayout.setEnableOverScrollBounce(true)//是否启用越界回弹
        videoSmartRefreshLayout.setEnableOverScrollDrag(true)//是否启用越界拖动（仿苹果效果）
        videoSmartRefreshLayout.setOnRefreshListener {
            if (videoId != -1) {
                mPresenter.getChildData(videoId, 6, 1, 6)
            }
        }
    }


    override fun initData() {
        videoSmartRefreshLayout.autoRefresh()
    }


    fun initVideoData(list: List<MovieClassification>) {
        movieTypeAdapter = context?.let { VideoTypeAdapter(it, bannerData?.url1?:"",bannerData?.url2?:"",bannerData?.link?:"",currentName) }
        rvVideo.layoutManager =XLinearLayoutManager(context)
        rvVideo.adapter = movieTypeAdapter
        movieTypeAdapter?.refresh(list)
    }


    //换肤
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun changeSkin(eventBean: ChangeSkin) {
        movieTypeAdapter?.notifyDataSetChanged()
    }



    companion object {
        fun newInstance(
            videoId: Int,
            videoPid: Int,
            name:String,
            movieType: ArrayList<MovieTypeChild>,
            videoBanner: VideoBannerForDataChild
        ): VideoChildFragment {
            val fragment = VideoChildFragment()
            val bundle = Bundle()
            bundle.putInt("videoId", videoId)
            bundle.putInt("videoPid", videoPid)
            bundle.putString("currentName", name)
            bundle.putParcelableArrayList("videoTypeData", movieType)
            bundle.putParcelable("videoBannerData", videoBanner)
            fragment.arguments = bundle
            return fragment
        }
    }
}