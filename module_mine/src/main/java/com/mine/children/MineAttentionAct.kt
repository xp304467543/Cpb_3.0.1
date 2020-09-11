package com.mine.children

import com.customer.adapter.TabNormalAdapter
import com.customer.data.HomeJumpToMineCloseLive
import com.customer.data.ToBetView
import com.customer.data.mine.UpDatePre
import com.hwangjr.rxbus.RxBus
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.thread.EventThread
import com.lib.basiclib.base.activity.BaseNavActivity
import com.lib.basiclib.base.adapter.BaseFragmentPageAdapter
import com.lib.basiclib.utils.ViewUtils
import com.lib.basiclib.widget.tab.ViewPagerHelper
import com.lib.basiclib.widget.tab.buildins.commonnavigator.CommonNavigator
import com.mine.R
import kotlinx.android.synthetic.main.act_attention.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/25
 * @ Describe
 *
 */
class MineAttentionAct : BaseNavActivity() {

    override fun getContentResID() = R.layout.act_attention

    override fun isSwipeBackEnable() = true

    override fun getPageTitle() = "我的关注"

    override fun isShowBackIconWhite() = false

    override fun isRegisterRxBus() = true


    override fun initContentView() {
        val title = arrayListOf("主播", "用户", "专家")
        val fragments = arrayListOf(
            MineAttentionActChild.newInstance(1), MineAttentionActChild.newInstance(2),
            MineAttentionActChild.newInstance(3)
        )
        val commonNavigator = CommonNavigator(this)
        commonNavigator.scrollPivotX = 0.65f
        commonNavigator.isAdjustMode = true
        commonNavigator.adapter = TabNormalAdapter(
            titleList = title, viewPage = xViewPageAttention,
            colorTextSelected = ViewUtils.getColor(R.color.color_333333),
            colorTextNormal = ViewUtils.getColor(R.color.color_AFAFAF),
            colorLine = ViewUtils.getColor(R.color.color_FF513E)
        )
        attentionTab.navigator = commonNavigator
        xViewPageAttention.adapter = BaseFragmentPageAdapter(supportFragmentManager, fragments)
        xViewPageAttention.offscreenPageLimit = 3
        ViewPagerHelper.bind(attentionTab, xViewPageAttention)
    }

    //跳到投注闭页面
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun tBetView(clickMine: ToBetView) {
        finish()
    }

}