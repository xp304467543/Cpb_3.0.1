package com.home.live.bet.old

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.customer.data.lottery.LotteryApi
import com.google.android.material.tabs.TabLayout
import com.home.R
import com.lib.basiclib.utils.ToastUtils

/**
 *
 * @ Author  QinTian
 * @ Date  2020-04-16
 * @ Describe 规则 玩法
 *
 */

class LiveRoomBetToolsRulesFragment : BaseNormalFragment() {

    private var livBetTypeTab: TabLayout? = null

    private var vpLiveBet: ViewPager? = null

    private var pagerAdapter: ViewPagerAdapter? = null

    override fun getLayoutRes() = R.layout.old_fragment_live_bet_tool_trend

    override fun initView(rootView: View?) {
        livBetTypeTab = rootView?.findViewById(R.id.livBetTypeTab)
        vpLiveBet = rootView?.findViewById(R.id.vp_live_bet)
    }

    override fun initData() {
        if (isAdded){
            val result = LotteryApi.getLotteryBetRule()
            result.onSuccess { op ->
                if (isAdded){
                    if (livBetTypeTab != null) {
                        val fragments: ArrayList<Fragment?> = arrayListOf()
                        fragments.clear()
                        for ((index,res) in op.withIndex()) {
                            livBetTypeTab?.newTab()?.setText(res.play_rule_type_name)?.let { livBetTypeTab?.addTab(it) }
                            fragments.add(LiveRoomBetToolsRulesChildFragment.newInstance(op,index))
                        }
                        pagerAdapter = ViewPagerAdapter(childFragmentManager, fragments)
                        vpLiveBet?.adapter = pagerAdapter
                        vpLiveBet?.offscreenPageLimit = fragments.size
                        livBetTypeTab?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
                            override fun onTabReselected(p0: TabLayout.Tab?) {}
                            override fun onTabUnselected(p0: TabLayout.Tab?) {}
                            override fun onTabSelected(p0: TabLayout.Tab?) {
                                vpLiveBet?.currentItem = p0?.position ?: 0
                            }
                        })
                        vpLiveBet?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                            override fun onPageScrollStateChanged(state: Int) {}
                            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
                            override fun onPageSelected(position: Int) {
                                if (livBetTypeTab?.getTabAt(position) != null) livBetTypeTab?.getTabAt(position)!!.select()
                            }

                        })
                    }
                }
            }
            result.onFailed {
                ToastUtils.showToast(it.getMsg())
            }
        }
    }

    companion object {
        fun newInstance(anchorId: String): LiveRoomBetToolsRulesFragment {
            val fragment = LiveRoomBetToolsRulesFragment()
            val bundle = Bundle()
            bundle.putString("lotteryId", anchorId)
            fragment.arguments = bundle
            return fragment
        }
    }

}