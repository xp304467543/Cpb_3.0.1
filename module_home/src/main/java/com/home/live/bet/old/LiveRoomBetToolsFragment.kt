package com.home.live.bet.old

import android.os.Bundle
import android.widget.ImageView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.home.R
import com.services.LotteryService
import com.xiaojinzi.component.impl.service.ServiceManager

/**
 *
 * @ Author  QinTian
 * @ Date  2020-04-16
 * @ Describe 助赢工具
 *
 */

class LiveRoomBetToolsFragment : BottomDialogFragment() {
    private var livBetTab: TabLayout? = null
    private var liveBetViewPager: ViewPager? = null

    private var pagerAdapter: ViewPagerAdapter? = null
    override val layoutResId: Int = R.layout.old_fragment_live_bet_tool

    override val resetHeight: Int = 0

    override fun isShowTop(): Boolean = false

    override fun canceledOnTouchOutside(): Boolean = true
//
    override fun initView() {
        setOnclick()
        livBetTab = rootView?.findViewById(R.id.livBetTab)

        if (livBetTab != null) {
            livBetTab?.newTab()?.setText("露珠走势")?.let { livBetTab?.addTab(it) }
            livBetTab?.newTab()?.setText("历史开奖")?.let { livBetTab?.addTab(it) }
            livBetTab?.newTab()?.setText("玩法规则")?.let { livBetTab?.addTab(it) }
        }
    }

    override fun initData() {

    }

    override fun initFragment() {
        liveBetViewPager = rootView?.findViewById(R.id.liveBetViewPager)
        liveBetViewPager?.removeAllViews()

       //初始化viewPager
        val fragments= arrayListOf(
            ServiceManager.get(LotteryService::class.java)?.getLuZhuFragment(arguments?.getString("LOTTERY_ID") ?: "1",arguments?.getString("issue") ?: "1"),
            ServiceManager.get(LotteryService::class.java)?.getHistoryFragment(arguments?.getString("LOTTERY_ID") ?: "1",arguments?.getString("issue") ?: "1"),
            LiveRoomBetToolsRulesFragment.newInstance(arguments?.getString("LOTTERY_ID") ?: "1")
        )
        pagerAdapter = ViewPagerAdapter(childFragmentManager, fragments)
        liveBetViewPager?.adapter = pagerAdapter
        liveBetViewPager?.currentItem = 0
        liveBetViewPager?.offscreenPageLimit = fragments.size
        liveBetViewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                if (livBetTab?.getTabAt(position) != null) livBetTab?.getTabAt(position)!!.select()
            }

        })
        livBetTab?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {}
            override fun onTabUnselected(p0: TabLayout.Tab?) {}
            override fun onTabSelected(p0: TabLayout.Tab?) {
                liveBetViewPager?.currentItem = p0?.position ?: 0
            }

        })
    }
    private fun setOnclick() {
        rootView?.findViewById<ImageView>(R.id.imgBetToolsBack)?.setOnClickListener { dismiss() }
    }

    companion object {
        fun newInstance(lotteryID: String?,isssue: String?): LiveRoomBetToolsFragment {
            val fragment = LiveRoomBetToolsFragment()
            val bundle = Bundle()
            bundle.putString("LOTTERY_ID", lotteryID)
            bundle.putString("isssue", isssue)
            fragment.arguments = bundle
            return fragment
        }
    }
}