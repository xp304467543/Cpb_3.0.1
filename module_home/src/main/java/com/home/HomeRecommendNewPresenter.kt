package com.home

import com.customer.data.home.HomeApi
import com.home.HomeRecommendNewFragment
import com.lib.basiclib.base.mvp.BaseMvpPresenter
import com.lib.basiclib.base.xui.widget.banner.widget.banner.BannerItem
import com.rxnetgo.rxcache.CacheMode
import kotlinx.android.synthetic.main.fragment_home_recommend_new.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/9/11
 * @ Describe
 *
 */

class HomeRecommendNewPresenter : BaseMvpPresenter<HomeRecommendNewFragment>() {


    //获取首页数据
    fun getAllData() {
        if (mView.isActive()) {
            val uiScope = CoroutineScope(Dispatchers.Main)
            uiScope.launch {
                val getHomeBannerResult = async { HomeApi.getHomeBannerResult(CacheMode.NONE) }

                val getHomeSystemNoticeResult =
                    async { HomeApi.getHomeSystemNoticeResult(CacheMode.NONE) }

                val getHomeGameResult = async { HomeApi.getHomeGame() }

                val getHomeHotLive = async { HomeApi.getHomeHotLive(CacheMode.NONE) }

                val resultGetHomeBannerResult = getHomeBannerResult.await()
                val resultGetHomeSystemNoticeResult = getHomeSystemNoticeResult.await()
                val resultGetHomeGameResult = getHomeGameResult.await()
                val resultGetHomeHotLive = getHomeHotLive.await()

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

                resultGetHomeGameResult.onSuccess {
                    mView.gameAdapter?.clear()
                    mView.rvHotGame.removeAllViews()
                    if (it.size > 6) mView.gameAdapter?.refresh(it.subList(0, 6)) else mView.gameAdapter?.refresh(it)}

                resultGetHomeHotLive.onSuccess {
                    mView.hotLiveAdapter?.clear()
                    mView.rvHotLiveNew.removeAllViews()
                    if (it.size > 10) mView.hotLiveAdapter?.refresh(it.subList(0, 10)) else mView.hotLiveAdapter?.refresh(it) }
            }
        }
    }

}