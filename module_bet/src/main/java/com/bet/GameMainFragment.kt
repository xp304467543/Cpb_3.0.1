package com.bet

import androidx.fragment.app.Fragment
import com.customer.adapter.TabScaleAdapter
import com.customer.adapter.TabThemAdapter
import com.customer.data.UnDateTopGame
import com.customer.data.UserInfoSp
import com.customer.data.game.GameAll
import com.customer.data.mine.ChangeSkin
import com.hwangjr.rxbus.RxBus
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.thread.EventThread
import com.lib.basiclib.base.adapter.BaseFragmentPageAdapter
import com.lib.basiclib.base.mvp.BaseMvpFragment
import com.lib.basiclib.utils.StatusBarUtils
import com.lib.basiclib.utils.ViewUtils
import com.lib.basiclib.widget.tab.ViewPagerHelper
import com.lib.basiclib.widget.tab.buildins.commonnavigator.CommonNavigator
import cuntomer.them.ITheme
import cuntomer.them.Theme
import kotlinx.android.synthetic.main.adapter_game_child_other.*
import kotlinx.android.synthetic.main.fragment_game.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/9/13
 * @ Describe
 *
 */

class GameMainFragment : BaseMvpFragment<GameMainPresenter>(), ITheme {

    override fun attachView() = mPresenter.attachView(this)

    override fun attachPresenter() = GameMainPresenter()

    override fun getLayoutResID() = R.layout.fragment_game

    override fun isRegisterRxBus() = true

    override fun isShowBackIconWhite() = false

    override fun initContentView() {
        StatusBarUtils.setStatusBarHeight(gameStateView)
        gameSmartRefreshLayout?.setEnableOverScrollBounce(true)//是否启用越界回弹
        gameSmartRefreshLayout?.setEnableOverScrollDrag(true)//是否启用越界拖动（仿苹果效果）
        gameSmartRefreshLayout?.setEnableRefresh(false)//是否启用下拉刷新功能
        gameSmartRefreshLayout?.setEnableLoadMore(false)//是否启用上拉加载功能
        setTheme(UserInfoSp.getThem())
    }

    override fun initData() {
        mPresenter.getAllGame()
    }

    fun initViewPager(data: ArrayList<GameAll>) {
        if (vpGame!=null){
            val fragments = arrayListOf<Fragment>(
                GameMainChildFragment.newInstance(0,data = data),
                GameMainChildOtherFragment.newInstance(1,data = data),
                GameMainChildOtherFragment.newInstance(2,data = data),
                GameMainChildOtherFragment.newInstance(3,data = data),
                GameMainChildOtherFragment.newInstance(4,data = data)
            )
            val adapter = BaseFragmentPageAdapter(childFragmentManager, fragments)
            vpGame?.adapter = adapter
            val list = arrayListOf<String>()
            for ( item in data){ list.add(item.name?:"未知") }
            initTopTab(list)
        }
    }
    private var tabAdapter: TabScaleAdapter? = null
    private fun initTopTab(mDataList: ArrayList<String>) {
        if (vpGame!=null){
            val commonNavigator = CommonNavigator(context)
            commonNavigator.scrollPivotX = 0.65f
            tabAdapter = TabScaleAdapter(
                titleList = mDataList,
                viewPage = vpGame,
                normalColor = ViewUtils.getColor(R.color.color_333333),
                selectedColor = ViewUtils.getColor(R.color.color_FF513E),
                colorLine = ViewUtils.getColor(R.color.color_FF513E),
                textSize = 14F
            )
            commonNavigator.adapter = tabAdapter
            gameSwitchVideoTab.navigator = commonNavigator
            ViewPagerHelper.bind(gameSwitchVideoTab, vpGame)
        }
    }


    override fun setTheme(theme: Theme) {
        when (theme) {
            Theme.Default -> {
                imgGameBg.setImageResource(R.drawable.ic_them_default_top)
            }
            Theme.NewYear -> {
                imgGameBg.setImageResource(R.drawable.ic_them_newyear_top)
            }
            Theme.MidAutumn -> {
                imgGameBg.setImageResource(R.drawable.ic_them_middle_top)
            }
            Theme.LoverDay -> {
                imgGameBg.setImageResource(R.drawable.ic_them_love_top)
            }
            Theme.NationDay ->{
                imgGameBg.setImageResource(R.drawable.ic_them_gq_top)
            }
        }
        tabAdapter?.notifyDataSetChanged()
    }


    override fun onSupportVisible() {
        super.onSupportVisible()
        RxBus.get().post(UnDateTopGame())
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
}