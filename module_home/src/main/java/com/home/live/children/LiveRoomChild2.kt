package com.home.live.children

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.customer.ApiRouter
import com.customer.base.BaseNormalFragment
import com.customer.bean.image.ImageViewInfo
import com.customer.bean.image.NineGridInfoAnchor
import com.customer.data.home.HomeLiveAnchorDynamicBean
import com.customer.data.home.HomeLiveAnchorInfoBean
import com.customer.data.moments.MomentsAnchorListResponse
import com.glide.GlideUtil
import com.home.R
import com.home.live.children.adapter.HomeAnchorAdapter
import com.lib.basiclib.base.fragment.BaseContentFragment
import com.lib.basiclib.base.mvp.BaseMvpFragment
import com.lib.basiclib.base.xui.adapter.recyclerview.XLinearLayoutManager
import com.lib.basiclib.base.xui.widget.imageview.nine.NineGridImageView
import com.xiaojinzi.component.impl.Router
import kotlinx.android.synthetic.main.fragmeent_live_child_2.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/26
 * @ Describe
 *
 */
class LiveRoomChild2 : BaseNormalFragment<LiveRoomChild2Presenter>(){

    var anchorId:String = "-1"

    var rvAnchorAdapter: HomeAnchorAdapter? = null

    override fun attachView() = mPresenter.attachView(this)

    override fun attachPresenter() = LiveRoomChild2Presenter ()

    override fun getLayoutRes()= R.layout.fragmeent_live_child_2

    override fun isRegisterRxBus() = true


    override fun initContentView() {
        smartRefreshLiveRoomAnchor.setEnableRefresh(false)//是否启用下拉刷新功能
        smartRefreshLiveRoomAnchor.setEnableLoadMore(false)//是否启用上拉加载功能
        smartRefreshLiveRoomAnchor.setEnableOverScrollBounce(true)//是否启用越界回弹
        smartRefreshLiveRoomAnchor.setEnableOverScrollDrag(true)//是否启用越界拖动（仿苹果效果）
        anchorId =arguments?.getString("LIVE_ROOM_ANCHOR_ID","")?:""
        rvLiveRoomAnchor.layoutManager = XLinearLayoutManager(context)
        rvLiveRoomAnchor.adapter = HomeAnchorAdapter(requireActivity()).also { rvAnchorAdapter = it }
    }


    override fun initData() {
        mPresenter.getAnchorInfo(anchorId)
    }


    @SuppressLint("SetTextI18n")
    fun initAnchorInfo(data: HomeLiveAnchorInfoBean) {
        context?.let { GlideUtil.loadCircleImage(it,data.avatar, imgLiveRoomAnchorPhoto,true) }
        tvLiveRoomAnchorName?.text = data.nickname
        tvLiveRoomAnchorInfo?.text = "房间号 " + anchorId + " / " + data.fans + "粉丝"
        if (data.sex == "0") {
            imgLiveRoomAnchorSex?.setBackgroundResource(R.mipmap.ic_live_anchor_girl)
        } else imgLiveRoomAnchorSex?.setBackgroundResource(R.mipmap.ic_live_anchor_boy)
        imgLiveRoomAnchorAge?.text = data.age
        imgLiveRoomAnchorLevel?.text = data.level
        imgGoInfo?.setOnClickListener {
            Router.withApi(ApiRouter::class.java).toAnchorPage(anchorId)
        }
    }
    var sNineGridPics: List<NineGridInfoAnchor>? = null
    fun initAnchorNews(data: List<MomentsAnchorListResponse>) {
        if (data.isNullOrEmpty()){
            setVisible(tvHolder)
        }else{
            sNineGridPics = getMediaDemos(data)
            rvAnchorAdapter?.refresh(sNineGridPics)
        }
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

    companion object {
        fun newInstance(anchorId: String, liveState: String, name: String,id:String,type:String): LiveRoomChild2 {
            val fragment = LiveRoomChild2()
            val bundle = Bundle()
            bundle.putString("LIVE_ROOM_ANCHOR_ID", anchorId)
            bundle.putString("LIVE_ROOM_ANCHOR_STATUE", liveState)
            bundle.putString("LIVE_ROOM_NICK_NAME", name)
            bundle.putString("LIVE_ROOM_LOTTERY_ID", id)
            bundle.putString("LIVE_ROOM_LOTTERY_TYPE", type)
            fragment.arguments = bundle
            return fragment
        }
    }

}