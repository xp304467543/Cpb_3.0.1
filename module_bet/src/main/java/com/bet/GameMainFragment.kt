package com.bet

import androidx.fragment.app.Fragment
import com.customer.adapter.TabScaleAdapter
import com.customer.data.game.GameAll
import com.lib.basiclib.base.adapter.BaseFragmentPageAdapter
import com.lib.basiclib.base.mvp.BaseMvpFragment
import com.lib.basiclib.utils.StatusBarUtils
import com.lib.basiclib.utils.ViewUtils
import com.lib.basiclib.widget.tab.ViewPagerHelper
import com.lib.basiclib.widget.tab.buildins.commonnavigator.CommonNavigator
import cuntomer.them.ITheme
import cuntomer.them.Theme
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

    override fun initContentView() {
        StatusBarUtils.setStatusBarHeight(gameStateView)
    }

    override fun initData() {
        mPresenter.getAllGame()
    }

    fun initViewPager(data: ArrayList<GameAll>) {
        val fragments = arrayListOf<Fragment>(
            GameMainChildFragment.newInstance(0,data = data),
            GameMainChildOtherFragment.newInstance(1,data = data),
            GameMainChildOtherFragment.newInstance(2,data = data),
            GameMainChildOtherFragment.newInstance(3,data = data),
            GameMainChildOtherFragment.newInstance(4,data = data)
        )
        val adapter = BaseFragmentPageAdapter(childFragmentManager, fragments)
        vpGame.adapter = adapter
        val list = arrayListOf<String>()
        for ( item in data){ list.add(item.name?:"未知") }
        initTopTab(list)
    }

    private fun initTopTab(mDataList: ArrayList<String>) {
        val commonNavigator = CommonNavigator(context)
        commonNavigator.scrollPivotX = 0.65f
        commonNavigator.adapter = TabScaleAdapter(
            titleList = mDataList,
            viewPage = vpGame,
            normalColor = ViewUtils.getColor(R.color.color_333333),
            selectedColor = ViewUtils.getColor(R.color.color_FF513E),
            colorLine = ViewUtils.getColor(R.color.color_FF513E),
            textSize = 14F
        )
        gameSwitchVideoTab.navigator = commonNavigator
        ViewPagerHelper.bind(gameSwitchVideoTab, vpGame)
    }


    override fun setTheme(theme: Theme) {

    }
}