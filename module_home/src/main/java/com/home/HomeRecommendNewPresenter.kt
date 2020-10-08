package com.home

import android.annotation.SuppressLint
import android.view.View
import com.customer.data.UserInfoSp
import com.customer.data.home.HomeApi
import com.customer.data.home.HomeTypeListResponse
import com.customer.utils.JsonUtils
import com.home.HomeRecommendNewFragment
import com.lib.basiclib.base.mvp.BaseMvpPresenter
import com.lib.basiclib.base.xui.widget.banner.widget.banner.BannerItem
import com.rxnetgo.rxcache.CacheMode
import kotlinx.android.synthetic.main.fragment_home_recommend_new.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.math.BigDecimal
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
    @SuppressLint("SetTextI18n")
    fun getAllData() {
        if (mView.isActive()) {
            val uiScope = CoroutineScope(Dispatchers.Main)
            uiScope.launch {
                val getHomeBannerResult = async { HomeApi.getHomeBannerResult(CacheMode.NONE) }

                val getHomeSystemNoticeResult =
                    async { HomeApi.getHomeSystemNoticeResult(CacheMode.NONE) }

                val getHomeGameResult = async { HomeApi.getHomeGame() }

                val getOnLine = async { HomeApi.getOnLine() }

                val getHomeHotLive = async { HomeApi.getHomeHotLive(CacheMode.NONE) }


                val getHomeLotteryTypeResult = async { HomeApi.getHomeLotteryTypeResult(CacheMode.NONE) }

                val resultGetHomeLotteryTypeResult = getHomeLotteryTypeResult.await()
                val resultGetHomeBannerResult = getHomeBannerResult.await()
                val resultGetHomeSystemNoticeResult = getHomeSystemNoticeResult.await()
                val resultGetHomeGameResult = getHomeGameResult.await()
                val resultGetHomeHotLive = getHomeHotLive.await()
                val resultOnLine = getOnLine.await()

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
                    if (it.size > 8) mView.gameAdapter?.refresh(it.subList(0, 8)) else mView.gameAdapter?.refresh(it)}

                resultGetHomeHotLive.onSuccess {
                    mView.hotLiveAdapter?.clear()
                    mView.rvHotLiveNew.removeAllViews()
                    if (it.size > 10) mView.hotLiveAdapter?.refresh(it.subList(0, 10)) else mView.hotLiveAdapter?.refresh(it) }

                resultGetHomeLotteryTypeResult.onSuccess {bean ->
                    val typeObject = bean.typeList?.asJsonObject
                    val dataObject = bean.data?.asJsonObject
                    //取出所有type
                    if (typeObject?.isJsonNull != true) {
                        mView.gameRoomList = ArrayList()
                        for (entryType in typeObject?.entrySet()!!) {
                            if (dataObject?.isJsonNull != true) {
                                val beanData = dataObject?.get(entryType.key)?.asJsonArray?.let { JsonUtils.fromJson(it, Array<HomeTypeListResponse>::class.java) }
                               mView.gameRoomList?.add(beanData)
                            }
                        }
                    }
                }

                resultOnLine.onSuccess {
                   if (mView.isActive()){
                       mView.tvOnline?.visibility = View.VISIBLE
                       mView.onLine = BigDecimal(it.base_online)
                       mView.tvOnline?.text ="在线人数: "+ mView.onLine.toString()
                   }
                }
            }
        }
    }

}