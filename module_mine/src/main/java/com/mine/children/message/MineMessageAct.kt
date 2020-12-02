package com.mine.children.message

import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.customer.data.UserInfoSp
import com.lib.basiclib.base.activity.BaseNavActivity
import com.lib.basiclib.base.adapter.BaseFragmentPageAdapter
import com.lib.basiclib.utils.ViewUtils
import com.lib.basiclib.widget.tab.buildins.commonnavigator.CommonNavigator
import com.lib.basiclib.widget.tab.buildins.commonnavigator.abs.CommonNavigatorAdapter
import com.lib.basiclib.widget.tab.buildins.commonnavigator.abs.IPagerIndicator
import com.lib.basiclib.widget.tab.buildins.commonnavigator.abs.IPagerTitleView
import com.lib.basiclib.widget.tab.buildins.commonnavigator.indicators.LinePagerIndicator
import com.lib.basiclib.widget.tab.buildins.commonnavigator.titles.ColorFlipPagerTitleView
import com.lib.basiclib.widget.tab.buildins.commonnavigator.titles.SimplePagerTitleView
import com.lib.basiclib.widget.tab.buildins.commonnavigator.titles.badge.BadgeAnchor
import com.lib.basiclib.widget.tab.buildins.commonnavigator.titles.badge.BadgePagerTitleView
import com.lib.basiclib.widget.tab.buildins.commonnavigator.titles.badge.BadgeRule
import com.mine.R
import com.xiaojinzi.component.anno.RouterAnno
import cuntomer.them.AppMode
import kotlinx.android.synthetic.main.act_msg_center_info_new.*


/**
 *
 * @ Author  QinTian
 * @ Date  11/20/20
 * @ Describe
 *
 */
@RouterAnno(host = "Mine", path = "message")
class MineMessageAct : BaseNavActivity() {

    override fun isShowBackIconWhite() = false

    override fun isSwipeBackEnable() = true

    override fun getContentResID() = R.layout.act_msg_center_info_new

    override fun getPageTitle() = "消息中心"

    var msg1 = ""
    var msg2 = ""
    var msg3 = ""
    var msg4 = ""
    override fun initContentView() {
        msg1 = intent?.getStringExtra("msg1") ?: "0" //0
        msg2 = intent?.getStringExtra("msg2") ?: "0" //2
        msg3 = intent?.getStringExtra("msg3") ?: "0" //3
        msg4 = intent?.getStringExtra("msg4") ?: "0" //5

        initViewPager()
        initMagicIndicator()
    }

    private fun initViewPager() {
        val fragment =  if (UserInfoSp.getAppMode() == AppMode.Pure) {
            arrayListOf(
                MineMessageActContent.newInstance("5"),
                MineMessageActContent.newInstance("3"),
                MineMessageActContent.newInstance("0")

            )
        } else {
            arrayListOf(
                MineMessageActContent.newInstance("5"),
                MineMessageActContent.newInstance("3"),
                MineMessageActContent.newInstance("0"),
                MineMessageActContent.newInstance("2")
            )
        }

        vpMessage.adapter = BaseFragmentPageAdapter(supportFragmentManager, fragment)
        vpMessage.offscreenPageLimit = fragment.size

    }

    var simplePagerTitleView: SimplePagerTitleView? = null
    var badgeList = arrayListOf<BadgePagerTitleView?>()
    private fun initMagicIndicator() {
        val mDataList = if (UserInfoSp.getAppMode() == AppMode.Pure) {
            arrayListOf("通知", "活动", "公告")
        }else  arrayListOf("通知", "活动", "公告", "互动")
        val commonNavigator = CommonNavigator(this)
        commonNavigator.isAdjustMode = true
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getCount(): Int {
                return mDataList.size
            }

            override fun getTitleView(context: Context?, index: Int): IPagerTitleView? {
                val badgePagerTitleView = BadgePagerTitleView(context)
                simplePagerTitleView = ColorFlipPagerTitleView(context)
                simplePagerTitleView?.text = mDataList[index]
                simplePagerTitleView?.textSize = 14F
                simplePagerTitleView?.normalColor = ViewUtils.getColor(R.color.text_black)
                simplePagerTitleView?.selectedColor = ViewUtils.getColor(R.color.color_FF513E)
                simplePagerTitleView?.setOnClickListener {
                    vpMessage.currentItem = index
                    badgePagerTitleView.badgeView = null // cancel badge when click tab
                }
                badgePagerTitleView.innerPagerTitleView = simplePagerTitleView


                // set badge position
                when (index) {
//                    0 -> {
//                        if (msg4!="0" && msg4!=""){
//                            val badgeTextView = LayoutInflater.from(context).inflate(R.layout.simple_count_badge_layout, null) as TextView
//                            badgeTextView.text =msg4
//                            badgePagerTitleView?.badgeView = badgeTextView
//                            badgePagerTitleView?.xBadgeRule = BadgeRule(
//                                BadgeAnchor.CONTENT_RIGHT,
//                                ViewUtils.dp2px(6)
//                            )
//                            badgePagerTitleView?.yBadgeRule = BadgeRule(BadgeAnchor.CONTENT_TOP, 0)
//                        }
//                    }
                    1 -> {
                        if (msg3 != "0" && msg3 != "") {
                            val badgeTextView = LayoutInflater.from(context)
                                .inflate(R.layout.simple_count_badge_layout, null) as TextView
                            badgeTextView.text = msg3
                            badgePagerTitleView.badgeView = badgeTextView
                            badgePagerTitleView.xBadgeRule = BadgeRule(
                                BadgeAnchor.CONTENT_RIGHT,
                                ViewUtils.dp2px(6)
                            )
                            badgePagerTitleView.yBadgeRule = BadgeRule(BadgeAnchor.CONTENT_TOP, 0)
                        }
                    }
                    2 -> {
                        if (msg1 != "0" && msg1 != "") {
                            val badgeTextView = LayoutInflater.from(context)
                                .inflate(R.layout.simple_count_badge_layout, null) as TextView
                            badgeTextView.text = msg1
                            badgePagerTitleView.badgeView = badgeTextView
                            badgePagerTitleView.xBadgeRule = BadgeRule(
                                BadgeAnchor.CONTENT_RIGHT,
                                ViewUtils.dp2px(6)
                            )
                            badgePagerTitleView.yBadgeRule = BadgeRule(BadgeAnchor.CONTENT_TOP, 0)
                        }

                    }
                    3 -> {
                        if (msg2 != "0" && msg2 != "") {
                            val badgeTextView = LayoutInflater.from(context)
                                .inflate(R.layout.simple_count_badge_layout, null) as TextView
                            badgeTextView.text = msg2
                            badgePagerTitleView.badgeView = badgeTextView
                            badgePagerTitleView.xBadgeRule = BadgeRule(
                                BadgeAnchor.CONTENT_RIGHT,
                                ViewUtils.dp2px(6)
                            )
                            badgePagerTitleView.yBadgeRule = BadgeRule(BadgeAnchor.CONTENT_TOP, 0)
                        }
                    }
                }

                badgePagerTitleView.isAutoCancelBadge = false
                badgeList.add(badgePagerTitleView)
                return badgePagerTitleView
            }

            override fun getIndicator(context: Context?): IPagerIndicator? {
                val indicator = LinePagerIndicator(context)
                indicator.setColors(ViewUtils.getColor(R.color.color_FF513E))
                return indicator
            }
        }
        magicIndicator.navigator = commonNavigator
        vpMessage.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                magicIndicator.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                magicIndicator.onPageSelected(position)
                val view = badgeList[position]
                if (view?.badgeView != null) {
                    view.badgeView = null
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
                magicIndicator.onPageScrollStateChanged(state)
            }
        })
    }
}