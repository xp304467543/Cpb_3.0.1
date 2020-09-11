package com.home.children

import com.customer.adapter.TabNormalAdapter
import com.home.R
import com.lib.basiclib.base.activity.BaseNavActivity
import com.lib.basiclib.base.adapter.BaseFragmentPageAdapter
import com.lib.basiclib.base.fragment.BaseFragment
import com.lib.basiclib.utils.ViewUtils
import com.lib.basiclib.widget.tab.ViewPagerHelper
import com.lib.basiclib.widget.tab.buildins.commonnavigator.CommonNavigator
import kotlinx.android.synthetic.main.act_news.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/25
 * @ Describe
 *
 */
class NewsAct : BaseNavActivity() {


    override fun getContentResID() = R.layout.act_news

    override fun isShowBackIconWhite() = false

    override fun getPageTitle() = "最新资讯"

    override fun isSwipeBackEnable() = true


    override fun initContentView() {
        initTab()
    }

    private fun initTab() {
        val title = arrayListOf("综合", "直播", "活动", "公告", "咨询")
        val fragments = arrayListOf<BaseFragment>(NewsActChild.newInstance(""),
            NewsActChild.newInstance("1"), NewsActChild.newInstance("2"),
            NewsActChild.newInstance("3"), NewsActChild.newInstance("4"))
        val commonNavigator = CommonNavigator(this)
        commonNavigator.scrollPivotX = 0.65f
        commonNavigator.isAdjustMode = true
        commonNavigator.adapter = TabNormalAdapter(
            titleList = title, viewPage = newsVP,
            colorTextSelected = ViewUtils.getColor(R.color.color_333333),
            colorTextNormal = ViewUtils.getColor(R.color.color_AFAFAF),
            colorLine = ViewUtils.getColor(R.color.color_FF513E)
        )
        newsTab.navigator = commonNavigator
        newsVP.adapter = BaseFragmentPageAdapter(supportFragmentManager, fragments)
        newsVP.offscreenPageLimit = 5
        ViewPagerHelper.bind(newsTab, newsVP)
    }
}