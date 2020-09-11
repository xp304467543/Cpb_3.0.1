package com.home

import com.customer.ApiRouter
import com.customer.adapter.TabNormalAdapter
import com.customer.component.PopWindowVideo
import com.customer.data.video.MovieType
import com.customer.data.video.MovieTypeChild
import com.customer.data.video.VideoBanner
import com.customer.data.video.VideoBannerForDataChild
import com.home.video.VideoChildFragment
import com.lib.basiclib.base.adapter.BaseFragmentPageAdapter
import com.lib.basiclib.base.mvp.BaseMvpFragment
import com.lib.basiclib.utils.FastClickUtil
import com.lib.basiclib.utils.ViewUtils
import com.lib.basiclib.widget.tab.ViewPagerHelper
import com.lib.basiclib.widget.tab.buildins.commonnavigator.CommonNavigator
import com.xiaojinzi.component.impl.Router
import kotlinx.android.synthetic.main.fragment_home_video.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/14
 * @ Describe
 *
 */
class HomeVideoFragment : BaseMvpFragment<HomeVideoPresenter>() {

    var typeList: List<MovieType>? = null

    private var videoPop: PopWindowVideo? = null

    override fun attachView() = mPresenter.attachView(this)

    override fun attachPresenter() = HomeVideoPresenter()

    override fun getLayoutResID() = R.layout.fragment_home_video

    override fun isRegisterRxBus() = true


    override fun lazyInit() {
        initViewTab()
        mPresenter.getTitle()
    }
    private var selectItems: MovieTypeChild?=null
    override fun initEvent() {
        imgVideoMore.setOnClickListener {
            if (videoPop == null){
                videoPop = typeList?.let { it1 -> context?.let { it2 -> PopWindowVideo(it2, it1,selectItems) } }
                videoPop?.seItemListener{
                    name ,MovieTypeChild,_,_ ->
                    selectItems = MovieTypeChild
                    Router.withApi(ApiRouter::class.java).toVideoMore(name,MovieTypeChild?.name?:"0")
                }
                videoPop?.showAsDropDown(videoTab)
            }else{
                videoPop?.dismiss()
                videoPop = null
            }
        }
        tvRetry.setOnClickListener {
            if (!FastClickUtil.isFastClick())mPresenter.getTitle()
        }
    }

    fun initTabViewData(it: List<MovieType>, videoBanner: VideoBanner?) {
        val fragments = arrayListOf<VideoChildFragment>()
        for ((index, item) in it.withIndex()) {
            if (!item.children.isNullOrEmpty()) {
                val url2 = videoBanner?.banner_movie_list?.get(0)?.image_url ?: ""
                val link = videoBanner?.banner_movie_home?.get(0)?.url ?: ""
                val dataChild = when (index) {
                    0 -> VideoBannerForDataChild(
                        videoBanner?.banner_movie_home?.get(0)?.image_url ?: "",
                        url2,
                        link
                    )
                    1 -> VideoBannerForDataChild(
                        videoBanner?.banner_movie_type_list?.banner_movie_type_1?.image_url,
                        url2,
                        link
                    )
                    2 -> VideoBannerForDataChild(
                        videoBanner?.banner_movie_type_list?.banner_movie_type_2?.image_url,
                        url2,
                        link
                    )
                    3 -> VideoBannerForDataChild(
                        videoBanner?.banner_movie_type_list?.banner_movie_type_3?.image_url,
                        url2,
                        link
                    )
                    4 -> VideoBannerForDataChild(
                        videoBanner?.banner_movie_type_list?.banner_movie_type_4?.image_url,
                        url2,
                        link
                    )
                    5 -> VideoBannerForDataChild(
                        videoBanner?.banner_movie_type_list?.banner_movie_type_5?.image_url,
                        url2,
                        link
                    )
                    else -> VideoBannerForDataChild("", url2, link)
                }
                fragments.add(
                    VideoChildFragment.newInstance(
                        item.id ?: -1,
                        item.pid ?: -1,
                        title[index],
                        item.children!!,
                        dataChild
                    )
                )
            }
        }
        val adapter = BaseFragmentPageAdapter(childFragmentManager, fragments)
        videoVP.adapter = adapter
        videoVP.offscreenPageLimit = 6
        ViewPagerHelper.bind(videoTab, videoVP)
    }

    val title = arrayListOf("精品", "国产", "日韩", "欧美", "三级", "动漫")
    private fun initViewTab() {
        val commonNavigator = CommonNavigator(context)
        commonNavigator.scrollPivotX = 0.65f
        commonNavigator.isAdjustMode = true
        commonNavigator.adapter = TabNormalAdapter(
            titleList = title, viewPage = videoVP,
            colorTextSelected = ViewUtils.getColor(R.color.white),
            colorTextNormal = ViewUtils.getColor(R.color.text_black),
            colorLine = ViewUtils.getColor(R.color.white)
        )
        videoTab.navigator = commonNavigator

    }
}