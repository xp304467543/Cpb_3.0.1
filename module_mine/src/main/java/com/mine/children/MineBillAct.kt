package com.mine.children

import com.customer.adapter.TabNormalAdapter
import com.customer.data.UserInfoSp
import com.lib.basiclib.base.activity.BaseNavActivity
import com.lib.basiclib.base.adapter.BaseFragmentPageAdapter
import com.lib.basiclib.base.fragment.BaseFragment
import com.lib.basiclib.base.fragment.PlaceholderFragment
import com.lib.basiclib.utils.ViewUtils
import com.lib.basiclib.widget.tab.ViewPagerHelper
import com.lib.basiclib.widget.tab.buildins.commonnavigator.CommonNavigator
import com.mine.R
import cuntomer.them.AppMode
import kotlinx.android.synthetic.main.act_bill.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/24
 * @ Describe
 *
 */

class MineBillAct : BaseNavActivity() {

    override fun getPageTitle() = "我的账单"

    override fun isShowBackIconWhite() = false

    override fun isSwipeBackEnable() = true


    override fun getContentResID() = R.layout.act_bill


    override fun initContentView() {
        initTab()
    }

    private fun initTab() {

        val title = when(UserInfoSp.getAppMode()){
            AppMode.Normal ->  arrayListOf("余额记录", "投注记录", "打赏记录", "兑换记录")

            AppMode.Pure ->  arrayListOf("余额记录", "投注记录")
        }

        val fragments = when(UserInfoSp.getAppMode()){
            AppMode.Normal -> arrayListOf(
                MineBillActChild.newInstance(0),  MineBillActChild.newInstance(1),
                MineBillActChild.newInstance(2),  MineBillActChild.newInstance(3))

            AppMode.Pure -> arrayListOf(MineBillActChild.newInstance(0),  MineBillActChild.newInstance(1))
        }





        val commonNavigator = CommonNavigator(this)
        commonNavigator.scrollPivotX = 0.65f
        commonNavigator.isAdjustMode = true
        commonNavigator.adapter = TabNormalAdapter(
            titleList = title, viewPage = homeBillVP,
            colorTextSelected = ViewUtils.getColor(R.color.color_333333),
            colorTextNormal = ViewUtils.getColor(R.color.color_AFAFAF),
            colorLine = ViewUtils.getColor(R.color.color_FF513E)
        )
        switchBillTab.navigator = commonNavigator
        homeBillVP.adapter = BaseFragmentPageAdapter(supportFragmentManager, fragments)
        homeBillVP.offscreenPageLimit = 4
        ViewPagerHelper.bind(switchBillTab, homeBillVP)
    }
}