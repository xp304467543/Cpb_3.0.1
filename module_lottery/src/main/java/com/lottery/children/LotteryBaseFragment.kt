package com.lottery.children

import com.customer.adapter.TabNormalAdapter
import com.customer.adapter.TabThemAdapter
import com.customer.data.LotteryTypeSelect
import com.customer.data.mine.ChangeSkin
import com.fh.module_lottery.R
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.thread.EventThread
import com.lib.basiclib.base.adapter.BaseFragmentPageAdapter
import com.lib.basiclib.base.fragment.BaseContentFragment
import com.lib.basiclib.utils.ViewUtils
import com.lib.basiclib.widget.tab.ViewPagerHelper
import com.lib.basiclib.widget.tab.buildins.commonnavigator.CommonNavigator
import cuntomer.them.Theme
import kotlinx.android.synthetic.main.child_lottery_base.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/18
 * @ Describe
 *
 */

class LotteryBaseFragment : BaseContentFragment() {


    private var commonNavigator: CommonNavigator? = null

    override fun getContentResID() = R.layout.child_lottery_base

    override fun isSwipeBackEnable() = false

    override fun isRegisterRxBus() = true


    private fun initTab() {
        commonNavigator = CommonNavigator(context)
        commonNavigator?.scrollPivotX = 0.65f
        commonNavigator?.isAdjustMode = true
        commonNavigator?.leftPadding = 50
        commonNavigator?.rightPadding = 50
    }


    private var tabAdapter: TabThemAdapter? = null
    private fun loadBottomTab(lotteryId: String, issue: String) {
        if (lotteryId == "-1" || issue == "-1") return
       if (vpLotteryFragment!=null) vpLotteryFragment.removeAllViews() else return
        val title: ArrayList<String>
        val fragments: ArrayList<*>
        val adapter: BaseFragmentPageAdapter
        if (lotteryId != "8") {
            title = arrayListOf("历史开奖", "露珠", "走势", "专家计划")
            fragments = arrayListOf(
                LotteryHistoryFragment.newInstance(lotteryId, issue),
                LotteryLuZhuFragment.newInstance(lotteryId, issue),
                LotteryTrendFragment.newInstance(lotteryId, issue),
                LotteryExpertFragment.newInstance(lotteryId, issue)
            )
            adapter = BaseFragmentPageAdapter(childFragmentManager, fragments)
        } else {
            title = arrayListOf("历史开奖", "露珠", "专家计划")
            fragments = arrayListOf(
                LotteryHistoryFragment.newInstance(lotteryId, issue),
                LotteryLuZhuFragment.newInstance(lotteryId, issue),
                LotteryExpertFragment.newInstance(lotteryId, issue)
            )
            adapter = BaseFragmentPageAdapter(childFragmentManager, fragments)
        }
        tabAdapter = TabThemAdapter(
            titleList = title, viewPage = vpLotteryFragment,
            colorTextSelected = ViewUtils.getColor(R.color.color_333333),
            colorTextNormal = ViewUtils.getColor(R.color.color_AFAFAF),
            textSize = 12F
        )
        initTab()
        switchLotteryTypeTab.navigator = commonNavigator
        commonNavigator?.adapter = tabAdapter
        vpLotteryFragment.adapter = adapter
        vpLotteryFragment?.currentItem = 0
        vpLotteryFragment.offscreenPageLimit = fragments.size
        ViewPagerHelper.bind(switchLotteryTypeTab, vpLotteryFragment)

    }

    //换肤
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun changeSkin(eventBean: ChangeSkin) {
        tabAdapter?.notifyDataSetChanged()
    }


    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun onLotteryTypeSelect(eventBean: LotteryTypeSelect) {
        loadBottomTab(eventBean.lotteryId ?: "-1", eventBean.issiue ?: "-1")
    }
}