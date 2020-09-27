package com.home

import com.customer.ApiRouter
import com.glide.GlideUtil
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.customer.data.home.HomeApi
import com.customer.data.home.HomeLivePreResponse
import com.lib.basiclib.base.mvp.BaseMvpPresenter
import com.rxnetgo.rxcache.CacheMode
import com.lib.basiclib.base.xui.widget.banner.widget.banner.BannerItem
import com.xiaojinzi.component.impl.Router
import kotlinx.android.synthetic.main.fragment_home_recommend.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.ArrayList

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/14
 * @ Describe
 *
 */
class HomeRecommendPresenter : BaseMvpPresenter<HomeRecommendFragment>() {

    //获取首页数据
    fun getAllData(cacheMode: CacheMode) {
        if (mView.isActive()) {
            val uiScope = CoroutineScope(Dispatchers.Main)
            uiScope.launch {
                val getHomeBannerResult = async { HomeApi.getHomeBannerResult(cacheMode) }

                val getHomeSystemNoticeResult =
                    async { HomeApi.getHomeSystemNoticeResult(cacheMode) }

                val getHomeLotteryTypeResult = async { HomeApi.getHomeLotteryTypeResult(cacheMode) }

                val getHomeHotLive = async { HomeApi.getHomeHotLive(cacheMode) }

                val getNews = async { HomeApi.getNews() }

                val getAd = async { HomeApi.getAd() }

                val getAd2 = async { HomeApi.getAd2() }

                val getHomeAnchorRecommend = async { HomeApi.getHomeAnchorRecommend(cacheMode) }

                val getHomeLivePreView = async { HomeApi.getHomeLivePreView(cacheMode) }

                val expertRed = async { HomeApi.expertRed() }

                val resultGetHomeBannerResult = getHomeBannerResult.await()
                val resultGetHomeSystemNoticeResult = getHomeSystemNoticeResult.await()
                val resultGetHomeLotteryTypeResult = getHomeLotteryTypeResult.await()
                val resultGetHomeHotLive = getHomeHotLive.await()
                val resultGetNews = getNews.await()
                val resultGetAd = getAd.await()
                val resultGetAd2 = getAd2.await()
                val resultGetHomeAnchorRecommend = getHomeAnchorRecommend.await()
                val resultExpertRed = expertRed.await()
                val resultGetHomeLivePreView = getHomeLivePreView.await()

                resultGetHomeBannerResult.onSuccess {
                    val list = ArrayList<BannerItem>()
                    for (i in it) {
                        val item = BannerItem()
                        item.imgUrl = i.image_url
                        item.title = i.url
                        list.add(item)
                    }
                    mView.upDateBanner(list)
                }

                resultGetHomeSystemNoticeResult.onSuccess { mView.upDateSystemNotice(it) }

                resultGetHomeLotteryTypeResult.onSuccess {  mView.upDateLotteryType(it) }

                resultGetHomeHotLive.onSuccess {  mView.hotLiveAdapter?.clear()
                    mView.rvHotLive.removeAllViews()
                    if (it.size > 6) mView.hotLiveAdapter?.refresh(it.subList(0, 6)) else mView.hotLiveAdapter?.refresh(it) }

                resultGetHomeLivePreView.onSuccess { getLivePreView(it) }

                resultGetNews.onSuccess {
                    mView.newsAdapter?.clear()
                    if (it.size > 3) mView.newsAdapter?.refresh(it.subList(0, 3)) else mView.newsAdapter?.refresh(it)

                }
                resultGetAd.onSuccess { GlideUtil.loadGrayscaleImage(mView.requireContext(),it[0].image_url, mView.imgAd,15)
                    mView.imgAd.setOnClickListener { _->
                        Router.withApi(ApiRouter::class.java).toGlobalWeb(it[0].url.toString())
                    }
                }

                resultGetAd2.onSuccess { GlideUtil.loadGrayscaleImage(mView.requireContext(),it[0].image_url, mView.imgAd2,15)
                    mView.imgAd2.setOnClickListener { _->
                        Router.withApi(ApiRouter::class.java).toGlobalWeb(it[0].url.toString())
                    }}
                resultGetHomeAnchorRecommend.onSuccess {
                    mView.anchorRecommendAdapter?.clear()
                    if (it.size > 6) mView.anchorRecommendAdapter?.refresh(it.subList(0, 6)) else mView.anchorRecommendAdapter?.refresh(it)
                }

                resultExpertRed.onSuccess {
                    mView.expertHotAdapter?.clear()
                    if (it.size > 4) mView.expertHotAdapter?.refresh(it.subList(0, 4)) else mView.expertHotAdapter?.refresh(it)
                }
            }
        }
    }


    /**
     * 直播预告 特殊的
     */
    private fun getLivePreView(result: String) {
        if (result.length > 10) {
            //将JSON的String 转成一个JsonArray对象
            val jsonArray = JsonParser.parseString(result).asJsonArray
            //遍历JsonArray
            val userBeanList = arrayListOf<HomeLivePreResponse>()
            for (data in jsonArray) {
                userBeanList.add(Gson().fromJson(data, HomeLivePreResponse::class.java))
            }
            mView.rvLivePreview.removeAllViews()
            mView.preLiveAdapter?.clear()
            mView.preLiveAdapter?.refresh(userBeanList)
        }
    }

    /**
     * 单独更新预告
     */
    fun upDatePreView() {
        HomeApi.getHomeLivePreView(CacheMode.NONE)
            .onSuccess {
                if (mView.isActive()) {

                   getLivePreView(it)
                }
            }
    }
}