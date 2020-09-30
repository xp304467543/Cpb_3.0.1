package com.bet.game

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bet.R
import com.google.android.material.tabs.TabLayout
import com.lib.basiclib.base.activity.BaseNavActivity
import com.services.HomeService
import com.services.LotteryService
import com.xiaojinzi.component.impl.service.ServiceManager
import kotlinx.android.synthetic.main.act_game_tools.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/9/15
 * @ Describe
 *
 */
class GameLotteryBetToolsActivity : BaseNavActivity() {

    override fun isSwipeBackEnable() = true

    override fun getContentResID() = R.layout.act_game_tools

    override fun getPageTitle() = "助赢工具"

    override fun isShowBackIconWhite() = false

    override fun initContentView() {

        if (gameBetTab != null) {
            gameBetTab?.newTab()?.setText("露珠走势")?.let { gameBetTab?.addTab(it) }
            gameBetTab?.newTab()?.setText("历史开奖")?.let { gameBetTab?.addTab(it) }
            gameBetTab?.newTab()?.setText("玩法规则")?.let { gameBetTab?.addTab(it) }
        }
        val lotteryId = intent.getStringExtra("gameLotteryToolsId") ?: "1"
        //初始化viewPager
        val fragments = arrayListOf(
            ServiceManager.get(LotteryService::class.java)?.getLuZhuFragment(lotteryId, ""),
            ServiceManager.get(LotteryService::class.java)?.getHistoryFragment(lotteryId, ""),
            ServiceManager.get(HomeService::class.java)?.getRulerFragment(lotteryId)
        )
        val   pagerAdapter = ViewPagerAdapter(supportFragmentManager, fragments)
        gameBetToolsViewPager?.adapter = pagerAdapter
        gameBetToolsViewPager?.currentItem = 0
        gameBetToolsViewPager?.offscreenPageLimit = fragments.size
        gameBetToolsViewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                if (gameBetTab?.getTabAt(position) != null) gameBetTab?.getTabAt(position)!!.select()
            }
        })
        gameBetTab?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {}
            override fun onTabUnselected(p0: TabLayout.Tab?) {}
            override fun onTabSelected(p0: TabLayout.Tab?) {
                gameBetToolsViewPager?.currentItem = p0?.position ?: 0
            }

        })
    }


 inner  class ViewPagerAdapter(fm: FragmentManager, fragmentList: ArrayList<Fragment?>) : FragmentPagerAdapter(fm) {

        private var fm: FragmentManager? = null

        private var fragmentList: ArrayList<Fragment?>? = null

        init {
            this.fm = fm
            this.fragmentList = fragmentList
        }

        override fun getItem(position: Int): Fragment {
            return fragmentList?.get(position)!!
        }
        override fun getCount(): Int {
            return fragmentList?.size ?: 0
        }
    }
}