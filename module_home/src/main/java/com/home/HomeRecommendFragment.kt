package com.home

import android.content.Intent
import android.graphics.Typeface
import android.text.TextUtils
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.customer.ApiRouter
import com.customer.adapter.HomeHotLiveAdapter
import com.customer.adapter.TabThemAdapter
import com.customer.data.LoginOut
import com.customer.data.UserInfoSp
import com.customer.data.home.*
import com.customer.data.mine.ChangeSkin
import com.customer.data.mine.LotteryToLiveRoom
import com.customer.data.mine.UpDatePre
import com.customer.utils.JsonUtils
import com.home.adapter.ExpertHotAdapter
import com.home.adapter.HomeLivePreviewAdapter
import com.home.adapter.HomeLotteryViewPagerAdapter
import com.home.adapter.HomeNewsAdapter
import com.home.children.LivePreAct
import com.home.children.MoreAnchorAct
import com.home.children.NewsAct
import com.home.weight.HomeLotteryTypeView
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.thread.EventThread
import com.lib.basiclib.base.mvp.BaseMvpFragment
import com.lib.basiclib.base.xui.widget.banner.widget.banner.BannerItem
import com.lib.basiclib.utils.FastClickUtil
import com.lib.basiclib.utils.ToastUtils
import com.lib.basiclib.utils.ViewUtils
import com.lib.basiclib.widget.tab.ViewPagerHelper
import com.lib.basiclib.widget.tab.buildins.commonnavigator.CommonNavigator
import com.rxnetgo.rxcache.CacheMode
import com.xiaojinzi.component.impl.Router
import cuntomer.bean.BaseApiBean
import cuntomer.them.ITheme
import cuntomer.them.Theme
import kotlinx.android.synthetic.main.fragment_home_recommend.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/14
 * @ Describe
 *
 */
class HomeRecommendFragment : BaseMvpFragment<HomeRecommendPresenter>(), ITheme {

    override fun attachView() = mPresenter.attachView(this)

    override fun attachPresenter() = HomeRecommendPresenter()

    override fun getLayoutResID() = R.layout.fragment_home_recommend

    override fun isRegisterRxBus() = true

    override fun initData() {
        mPresenter.getAllData(CacheMode.NONE)
        homeSmartRefreshLayout.setOnRefreshListener {
            mPresenter.getAllData(CacheMode.NONE)
            homeSmartRefreshLayout.finishRefresh()
        }
        toMoreNews()
        toMorePre()
        toMorAnchor()
    }

    override fun initEvent() {

    }

    fun toMoreNews() {
        tvNewsMore?.setOnClickListener {
            if (!FastClickUtil.isFastClick()) startActivity(
                Intent(
                    requireActivity(),
                    NewsAct::class.java
                )
            )
        }
    }

    fun toMorePre() {
        tvPreMore?.setOnClickListener {
            if (!FastClickUtil.isFastClick()) startActivity(
                Intent(
                    requireActivity(),
                    LivePreAct::class.java
                )
            )
        }
    }

    fun toMorAnchor() {
        tvAnchorMore?.setOnClickListener {
            if (!FastClickUtil.isFastClick()) startActivity(
                Intent(
                    requireActivity(),
                    MoreAnchorAct::class.java
                )
            )
        }
    }

    override fun initContentView() {
        ivNotice.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD_ITALIC)
        setTheme(UserInfoSp.getThem())
        upDateBanner(null)
        upDateSystemNotice(null)
        initHotRecommend()
        livePreView()
        news()
        anchorRecommend()
        expertRed()
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

    //========= 彩种直播 =========
    var tabAdapter: TabThemAdapter? = null
    private var childLotteryTypeViewList: ArrayList<HomeLotteryTypeView> = arrayListOf()
    private var gameRoomList: ArrayList<Array<HomeTypeListResponse>?>? = null
    fun upDateLotteryType(bean: BaseApiBean) {
        lotteryTypeContainer.visibility = View.VISIBLE
        val typeObject = bean.typeList?.asJsonObject
        val dataObject = bean.data?.asJsonObject
        val title = arrayListOf<String>()
        val view = arrayListOf<View>()
        //取出所有type
        if (typeObject?.isJsonNull != true) {
            gameRoomList = ArrayList()
            for (entryType in typeObject?.entrySet()!!) {
                title.add(entryType.value.asString)
                if (dataObject?.isJsonNull != true) {
                    val beanData = dataObject?.get(entryType.key)?.asJsonArray?.let {
                        JsonUtils.fromJson(
                            it,
                            Array<HomeTypeListResponse>::class.java
                        )
                    }
                    gameRoomList?.add(beanData)
                    val childLotteryTypeView = HomeLotteryTypeView(getApplication())
                    childLotteryTypeView.setOnItemClickListener {
                        if (!FastClickUtil.isFastClick()) {
                            Router.withApi(ApiRouter::class.java).toLive(
                                it.anchor_id ?: "1",
                                it.lottery_id ?: "1",
                                it.name ?: "未知",
                                it.live_status ?: "0",
                                it.online.toString(),
                                it.game_id ?: "1",
                                it.name ?: "未知",
                                it.image ?: ""
                            )
                        }
                    }
                    beanData?.let {
                        childLotteryTypeView.setData(it)
                        view.add(childLotteryTypeView)
                        childLotteryTypeViewList.add(childLotteryTypeView)
                        val adapter = HomeLotteryViewPagerAdapter(title, view)
                        homeLotteryVP.adapter = adapter
                    }
                }
            }
            val commonNavigator = CommonNavigator(context)
            commonNavigator.scrollPivotX = 0.65f
            commonNavigator.isAdjustMode = true
            tabAdapter = TabThemAdapter(
                titleList = title, viewPage = homeLotteryVP,
                colorTextSelected = ViewUtils.getColor(R.color.color_333333),
                colorTextNormal = ViewUtils.getColor(R.color.color_AFAFAF)
            )
            commonNavigator.adapter = tabAdapter
            switchLotteryTab.navigator = commonNavigator
            ViewPagerHelper.bind(switchLotteryTab, homeLotteryVP)
        }
    }

    // 热门推荐
    var hotLiveAdapter: HomeHotLiveAdapter? = null
    private fun initHotRecommend() {
        desHotRecommend.setDesText("热门推荐")
        val it: List<HomeHotLiveResponse>
        it = ArrayList()
        for (index in 1..6) {
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
        rvHotLive.adapter = hotLiveAdapter
        rvHotLive.layoutManager = gridLayoutManager
        hotLiveAdapter?.refresh(it)
    }

    // ========= 直播预告 =========
    var preLiveAdapter: HomeLivePreviewAdapter? = null
    private fun livePreView() {
        desLivePre.setDesText("直播预告")
        val it: List<HomeLivePreResponse>
        it = ArrayList()
        for (index in 1..6) {
            it.add(
                HomeLivePreResponse(
                    nickname = "加载中..",
                    name = "加载中..",
                    starttime = "0",
                    endtime = "0",
                    livestatus = "0",
                    isFollow = false
                )
            )
        }
        rvLivePreview.layoutManager =
            LinearLayoutManager(getPageActivity(), LinearLayoutManager.HORIZONTAL, false)
        preLiveAdapter = HomeLivePreviewAdapter(requireActivity())
        rvLivePreview.adapter = preLiveAdapter
        preLiveAdapter?.refresh(it)
    }

    // ========= 最新资讯 =========
    var newsAdapter: HomeNewsAdapter? = null

    private fun news() {
        desNes.setDesText("最新资讯")
        val it: List<HomeNewsResponse>
        it = ArrayList()
        for (index in 1..3) {
            it.add(
                HomeNewsResponse(
                    title = "加载中...",
                    timegap = "",
                    type_txt = "..."
                )
            )
        }
        rvNews.layoutManager =
            LinearLayoutManager(getPageActivity(), LinearLayoutManager.VERTICAL, false)
        newsAdapter = HomeNewsAdapter()
        rvNews.adapter = newsAdapter
        newsAdapter?.refresh(it)
    }

    // ========= 主播推荐 =========
    var anchorRecommendAdapter: HomeHotLiveAdapter? = null
    private fun anchorRecommend() {
        desAnchorRecommend.setDesText("主播推荐")
        val it: List<HomeHotLiveResponse>
        it = ArrayList()
        for (index in 1..6) {
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
        anchorRecommendAdapter = HomeHotLiveAdapter()
        val gridLayoutManager = object : GridLayoutManager(context, 2) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        rvAnchorRecommend.adapter = anchorRecommendAdapter
        rvAnchorRecommend.layoutManager = gridLayoutManager
        anchorRecommendAdapter?.refresh(it)
    }

    // ========= 专家红单 =========
    var expertHotAdapter: ExpertHotAdapter? = null
    private fun expertRed() {
        desExpert.setDesText("专家红单")
        val it: List<HomeExpertList>
        it = ArrayList()
        for (index in 1..4) {
            it.add(
                HomeExpertList(
                    nickname = "加载中...",
                    win_rate = "加载中...",
                    profit_rate = "0",
                    winning = "加载中...",
                    lottery_name = "加载中...",
                    last_10_games = null
                )
            )
        }
        expertHotAdapter = ExpertHotAdapter(context)
        rvExpertHot.layoutManager =
            LinearLayoutManager(getPageActivity(), LinearLayoutManager.VERTICAL, false)
        rvExpertHot.adapter = expertHotAdapter
        expertHotAdapter?.refresh(it)
    }

    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun loginOut(eventBean: LoginOut) {
        if (isActive()) mPresenter.upDatePreView()
    }

    //换肤
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun changeSkin(eventBean: ChangeSkin) {
        when (eventBean.id) {
            1 -> setTheme(Theme.Default)
            2 -> setTheme(Theme.NewYear)
            3 -> setTheme(Theme.MidAutumn)
            4 -> setTheme(Theme.LoverDay)
            5 ->setTheme(Theme.NationDay)
        }

    }

    //更新关注
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun upDatePre(eventBean: UpDatePre) {
        if (isAdded) {
            view?.postDelayed({ mPresenter.upDatePreView() }, 2000)
        }
    }

    //lottery页面打开直播间
    @Subscribe(thread = EventThread.HANDLER)
    fun lotteryToLiveRoom(eventBean: LotteryToLiveRoom) {
        if (isAdded) {
            if (gameRoomList != null && gameRoomList?.isNotEmpty()!!) {
                for (bean in gameRoomList!!) {
                    if (bean.isNullOrEmpty()) return
                    for ((index, item) in bean.withIndex()) {
                        if (eventBean.id == item.lottery_id) {
                            Router.withApi(ApiRouter::class.java).toLive(
                                item.anchor_id ?: "1",
                                item.lottery_id ?: "1",
                                item.name ?: "未知",
                                item.live_status ?: "0",
                                item.online.toString(),
                                item.game_id ?: "1",
                                item.name ?: "未知",
                                item.image ?: ""
                            )
                            return
                        } else if (index == bean.size - 1) {
                            ToastUtils.showToast("该彩种暂无直播")
                        }
                    }
                }

            }
        }
    }

    //主题
    override fun setTheme(theme: Theme) {
        when (theme) {
            Theme.Default -> {
                ivNotice.setTextColor(ViewUtils.getColor(R.color.color_FF513E))
                tvPreMore.setTextColor(ViewUtils.getColor(R.color.color_FF513E))
                tvNewsMore.setTextColor(ViewUtils.getColor(R.color.color_FF513E))
                tvAnchorMore.setTextColor(ViewUtils.getColor(R.color.color_FF513E))
                desHotRecommend.setDesText(null, null, R.color.color_FFD4AF, null)
                desLivePre.setDesText(null, null, R.color.color_FFD4AF, null)
                desNes.setDesText(null, null, R.color.color_FFD4AF, null)
                desAnchorRecommend.setDesText(null, null, R.color.color_FFD4AF, null)
                desExpert.setDesText(null, null, R.color.color_FFD4AF, null)
            }
            Theme.NewYear -> {
                ivNotice.setTextColor(ViewUtils.getColor(R.color.color_FF513E))
                tvPreMore.setTextColor(ViewUtils.getColor(R.color.color_FF513E))
                tvNewsMore.setTextColor(ViewUtils.getColor(R.color.color_FF513E))
                tvAnchorMore.setTextColor(ViewUtils.getColor(R.color.color_FF513E))
                desHotRecommend.setDesText(null, null, null, R.drawable.ic_them_newyear_1)
                desLivePre.setDesText(null, null, null, R.drawable.ic_them_newyear_3)
                desNes.setDesText(null, null, null, R.drawable.ic_them_newyear_4)
                desAnchorRecommend.setDesText(null, null, null, R.drawable.ic_them_newyear_5)
                desExpert.setDesText(null, null, null, R.drawable.ic_them_newyear_6)
            }
            Theme.MidAutumn -> {
                ivNotice.setTextColor(ViewUtils.getColor(R.color.colorGreenPrimary))
                tvPreMore.setTextColor(ViewUtils.getColor(R.color.color_333333))
                tvNewsMore.setTextColor(ViewUtils.getColor(R.color.color_333333))
                tvAnchorMore.setTextColor(ViewUtils.getColor(R.color.color_333333))
                desHotRecommend.setDesText(null, null, null, R.drawable.ic_them_middle_1)
                desLivePre.setDesText(null, null, null, R.drawable.ic_them_middle_3)
                desNes.setDesText(null, null, null, R.drawable.ic_them_middle_4)
                desAnchorRecommend.setDesText(null, null, null, R.drawable.ic_them_middle_5)
                desExpert.setDesText(null, null, null, R.drawable.ic_them_middle_6)
            }
            Theme.LoverDay -> {
                ivNotice.setTextColor(ViewUtils.getColor(R.color.purple))
                tvPreMore.setTextColor(ViewUtils.getColor(R.color.black))
                tvPreMore.setTextColor(ViewUtils.getColor(R.color.color_333333))
                tvNewsMore.setTextColor(ViewUtils.getColor(R.color.color_333333))
                tvAnchorMore.setTextColor(ViewUtils.getColor(R.color.color_333333))
                desHotRecommend.setDesText(null, null, null, R.drawable.ic_them_love_1)
                desLivePre.setDesText(null, null, null, R.drawable.ic_them_love_3)
                desNes.setDesText(null, null, null, R.drawable.ic_them_love_4)
                desAnchorRecommend.setDesText(null, null, null, R.drawable.ic_them_love_5)
                desExpert.setDesText(null, null, null, R.drawable.ic_them_love_6)
            }
        }
        tabAdapter?.notifyDataSetChanged()
        for (item in childLotteryTypeViewList) {
            item.setThem()
        }
    }

}