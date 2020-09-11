package com.moment

import com.customer.adapter.TabThemAdapter
import com.customer.data.UserInfoSp
import com.customer.data.mine.ChangeSkin
import com.customer.data.moments.MomentsTopBannerResponse
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.thread.EventThread
import com.lib.basiclib.base.adapter.BaseFragmentPageAdapter
import com.lib.basiclib.base.fragment.BaseFragment
import com.lib.basiclib.base.mvp.BaseMvpFragment
import com.lib.basiclib.base.xui.widget.banner.widget.banner.BannerItem
import com.lib.basiclib.utils.StatusBarUtils
import com.lib.basiclib.utils.ViewUtils
import com.lib.basiclib.widget.tab.ViewPagerHelper
import com.lib.basiclib.widget.tab.buildins.commonnavigator.CommonNavigator
import com.moment.children.MomentsAnchorFragment
import com.moment.children.MomentsHotDiscussFragment
import com.moment.children.MomentsRecommendFragment
import com.xiaojinzi.component.anno.RouterAnno
import cuntomer.them.ITheme
import cuntomer.them.Theme
import kotlinx.android.synthetic.main.fragment_moment.*

@RouterAnno(
    host = "Moment",
    path = "main"
)
class MomentFragment : BaseMvpFragment<MomentPresenter>(),ITheme {

    override fun attachView() = mPresenter.attachView(this)

    override fun attachPresenter() = MomentPresenter()

    override fun getLayoutResID() = R.layout.fragment_moment

    override fun isRegisterRxBus() = true

    override fun initContentView() {
        setSwipeBackEnable(false)
        StatusBarUtils.setStatusBarHeight(stateViewMoment)
        setTheme(UserInfoSp.getThem())
        initTopTab()
    }

    override fun initData() {
        mPresenter.getMomentsData()
    }
    // ========= Tab =========
    var tabAdapter:TabThemAdapter?=null
    private fun initTopTab() {
        val mDataList = arrayListOf("热门讨论", "主播","精品推荐")
        val fragments:ArrayList<BaseFragment> = arrayListOf(
            MomentsHotDiscussFragment(),
            MomentsAnchorFragment(),
            MomentsRecommendFragment()
        )
        val adapter = BaseFragmentPageAdapter(childFragmentManager, fragments)
        xViewPageMoments.adapter = adapter
        xViewPageMoments.offscreenPageLimit = 3
        val commonNavigator = CommonNavigator(context)
        commonNavigator.scrollPivotX = 0.65f
        commonNavigator.isAdjustMode = true
        tabAdapter = TabThemAdapter(
            titleList = mDataList, viewPage = xViewPageMoments,
            colorTextSelected = ViewUtils.getColor(R.color.color_333333),
            colorTextNormal = ViewUtils.getColor(R.color.color_AFAFAF)
        )
        commonNavigator.adapter = tabAdapter
        momentsTab.navigator = commonNavigator
        ViewPagerHelper.bind(momentsTab, xViewPageMoments)
    }

    // ========= banner =========
    fun upDateBanner(data: List<MomentsTopBannerResponse>) {
        val list = ArrayList<BannerItem>()
        for (i in data) {
            val item = BannerItem()
            item.imgUrl = i.img
            item.title = i.title
            list.add(item)
        }
        momentBannerView.setSource(list)?.startScroll()

    }

    override fun initEvent() {
        topMoment.setOnClickListener {

        }
    }


    //主题
    override fun setTheme(theme: Theme) {
        when (theme) {
            Theme.Default -> {
                skinMoment.setImageResource(R.drawable.ic_them_default_top)
            }
            Theme.NewYear -> {
                skinMoment.setImageResource(R.drawable.ic_them_newyear_top)
            }
            Theme.MidAutumn -> {
                skinMoment.setImageResource(R.drawable.ic_them_middle_top)
            }
            Theme.LoverDay -> {
                skinMoment.setImageResource(R.drawable.ic_them_love_top)
            }
        }
    }

    //换肤
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun changeSkin(eventBean: ChangeSkin) {
        when (eventBean.id) {
            1 ->  setTheme(Theme.Default)
            2 ->  setTheme(Theme.NewYear)
            3 ->  setTheme(Theme.MidAutumn)
            4 ->  setTheme(Theme.LoverDay)
        }
        tabAdapter?.notifyDataSetChanged()
    }
}
