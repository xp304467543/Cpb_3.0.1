package com.home

import android.text.TextUtils
import androidx.recyclerview.widget.GridLayoutManager
import com.customer.ApiRouter
import com.customer.adapter.HomeHotLiveAdapter
import com.customer.data.UserInfoSp
import com.customer.data.home.Game
import com.customer.data.home.HomeHotLiveResponse
import com.customer.data.home.HomeSystemNoticeResponse
import com.home.adapter.HomeGameRvAdapter
import com.lib.basiclib.base.mvp.BaseMvpFragment
import com.lib.basiclib.base.xui.widget.banner.widget.banner.BannerItem
import com.lib.basiclib.utils.FastClickUtil
import com.xiaojinzi.component.impl.Router
import cuntomer.them.ITheme
import cuntomer.them.Theme
import kotlinx.android.synthetic.main.fragment_home_recommend_new.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/9/11
 * @ Describe
 *
 */
class HomeRecommendNewFragment : BaseMvpFragment<HomeRecommendNewPresenter>(), ITheme {




    override fun attachView() = mPresenter.attachView(this)

    override fun attachPresenter() = HomeRecommendNewPresenter()

    override fun getLayoutResID() = R.layout.fragment_home_recommend_new

    override fun isRegisterRxBus() = true


    override fun initContentView() {
        setTheme(UserInfoSp.getThem())
        initRecycle()
    }

    private fun initRecycle() {
        upDateBanner(null)
        upDateSystemNotice(null)
        upDateGame()
        initHotRecommend()
    }


    override fun initData() {
        mPresenter.getAllData()
        homeSmartRefreshLayout.setOnRefreshListener {
            mPresenter.getAllData()
            homeSmartRefreshLayout.finishRefresh()
        }
    }



    fun upDateBanner(data: List<BannerItem>?) {
        val result: List<BannerItem>
        if (data == null) {
            result = ArrayList()
            for (index in 1..3) {
                result.add(BannerItem())
            }
        } else result = data
        if (BannerView!=null){
            BannerView?.setSource(result)?.startScroll()
            BannerView?.setOnItemClickListener { view, item, position ->
                if (!FastClickUtil.isFastClick()) {
                    if (item.title != null && !TextUtils.isEmpty(item.title))
                        Router.withApi(ApiRouter::class.java).toGlobalWeb(item.title)
                }
            }
        }
    }

    //========= 公告 =========
     fun upDateSystemNotice(data: List<HomeSystemNoticeResponse>?) {
        val result = ArrayList<String>()
        val sb = StringBuffer()
        if (data != null && data.isNotEmpty()) {
            data.forEachIndexed { index, value ->
                val s = (index + 1).toString() + "." + value.content + "        "
                sb.append(s)
                result.add((index + 1).toString() + "." + value.content)
            }
        } else sb.append("暂无公告。")
        tvNoticeMassages.setText(sb.toString())
    }

    //游戏列表
    var gameAdapter: HomeGameRvAdapter?=null
    private fun upDateGame(){
        val it: List<Game>
        it = ArrayList()
        for (index in 1..6) {
            it.add(Game(name = "加载中...", img_url = "", game_id = "-1"))
        }
        gameAdapter = HomeGameRvAdapter(context)
        val gridLayoutManager = object : GridLayoutManager(context, 3) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        rvHotGame.adapter = gameAdapter
        rvHotGame.layoutManager = gridLayoutManager
        gameAdapter?.refresh(it)
    }


    // 热门直播
    var hotLiveAdapter: HomeHotLiveAdapter? = null
    private fun initHotRecommend() {
        val it: List<HomeHotLiveResponse>
        it = ArrayList()
        for (index in 1..10) {
            it.add(
                HomeHotLiveResponse(
                    name = "加载中...",
                    nickname = "加载中...",
                    live_intro = "加载中...",
                    online = 0,
                    red_paper_num = 0,
                    daxiu = false
                )
            )
        }
        hotLiveAdapter = HomeHotLiveAdapter()
        val gridLayoutManager = object : GridLayoutManager(context, 2) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        rvHotLiveNew.adapter = hotLiveAdapter
        rvHotLiveNew.layoutManager = gridLayoutManager
        hotLiveAdapter?.refresh(it)
    }





    //主题
    override fun setTheme(theme: Theme) {
        when (theme) {
            Theme.Default -> {

            }
            Theme.NewYear -> {

            }
            Theme.MidAutumn -> {

            }
            Theme.LoverDay -> {

            }
        }

    }

}