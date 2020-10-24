package com.home.video.more

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import androidx.viewpager.widget.ViewPager
import com.customer.ApiRouter
import com.customer.component.PopWindowVideo
import com.customer.data.VideoColumnChange
import com.customer.data.video.MovieType
import com.customer.data.video.MovieTypeChild
import com.home.R
import com.hwangjr.rxbus.RxBus
import com.lib.basiclib.base.adapter.BaseFragmentPageAdapter
import com.lib.basiclib.base.mvp.BaseMvpActivity
import com.lib.basiclib.utils.FastClickUtil
import com.lib.basiclib.utils.StatusBarUtils
import com.lib.basiclib.utils.ToastUtils
import com.lib.basiclib.utils.ViewUtils
import com.lib.basiclib.widget.tab.MagicIndicator
import com.lib.basiclib.widget.tab.ViewPagerHelper
import com.lib.basiclib.widget.tab.buildins.commonnavigator.CommonNavigator
import com.lib.basiclib.widget.tab.buildins.commonnavigator.abs.CommonNavigatorAdapter
import com.lib.basiclib.widget.tab.buildins.commonnavigator.abs.IPagerIndicator
import com.lib.basiclib.widget.tab.buildins.commonnavigator.abs.IPagerTitleView
import com.lib.basiclib.widget.tab.buildins.commonnavigator.indicators.WrapPagerIndicator
import com.lib.basiclib.widget.tab.buildins.commonnavigator.titles.ClipPagerTitleView
import com.lib.basiclib.widget.tab.buildins.commonnavigator.titles.ScaleTransitionPagerTitleView
import com.lib.basiclib.widget.tab.buildins.commonnavigator.titles.SimplePagerTitleView
import com.xiaojinzi.component.anno.RouterAnno
import com.xiaojinzi.component.impl.Router
import kotlinx.android.synthetic.main.act_video_more.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/10/7
 * @ Describe
 *
 */
@RouterAnno(host = "Home", path = "videoMore")
class VideoMoreAct : BaseMvpActivity<ViewMoreActPresenter>() {

    var topList: List<MovieType>? = null

    var tab3List: HashMap<Int, List<String>> = HashMap()

    var tab3Cid: HashMap<Int, List<Int>> = HashMap()

    var mTypeId = -1

    var topName = "0"

    private var childName = "0"

    var mColumn = "updated"//固定值：updated=最新 reads=最多观看 praise=最多喜欢

    private var videoPop: PopWindowVideo? = null

    private var selectItems: MovieTypeChild? = null

    override fun attachView() = mPresenter.attachView(this)

    override fun attachPresenter() = ViewMoreActPresenter()

    override fun getContentResID() = R.layout.act_video_more

    override fun isSwipeBackEnable() = true

    override fun isShowToolBar() = false


    override fun initContentView() {
        StatusBarUtils.setStatusBarHeight(stateVideoMore)
        if (intent.getStringExtra("topStr") != "0") {
            topName = intent.getStringExtra("topStr") ?: "0"
        }
        if (intent.getStringExtra("childStr") != "0") {
            childName = intent.getStringExtra("childStr") ?: "0"
        }
    }

    override fun initData() {
        mPresenter.getTabTitle()
    }


    override fun initEvent() {
        moreBack.setOnClickListener { finish() }
        imgSearch.setOnClickListener {
            if (!FastClickUtil.isFastClick()) Router.withApi(ApiRouter::class.java).toVideoSearch()
        }
        imgVideoPop.setOnClickListener {
            if (videoPop == null) {
                videoPop = topList?.let { it1 -> PopWindowVideo(this, it1, selectItems) }
                videoPop?.seItemListener { _, MovieTypeChild, topPos, _ ->
                    if (!FastClickUtil.isFastClick()){
                        selectItems = MovieTypeChild
                        magic_indicator1.onPageSelected(topPos)
                        magic_indicator1.onPageScrolled(topPos, 0.0F, 0)
                        childName = MovieTypeChild?.name ?: "0"
                        mPresenter.getChildTab(topPos)
                    }else ToastUtils.showToast("请勿频繁操作")

                }
                videoPop?.showAsDropDown(magic_indicator1)
            } else {
                videoPop?.dismiss()
                videoPop = null
            }
        }
    }


    fun initMagicIndicator1(title: ArrayList<String>) {
        val commonNavigator = CommonNavigator(this)
        commonNavigator.scrollPivotX = 0.8f
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getTitleView(context: Context?, index: Int): IPagerTitleView {
                val simplePagerTitleView: SimplePagerTitleView =
                    ScaleTransitionPagerTitleView(context)
                simplePagerTitleView.text = title[index]
                simplePagerTitleView.textSize = 16F
                simplePagerTitleView.typeface = Typeface.DEFAULT_BOLD
                simplePagerTitleView.normalColor = ViewUtils.getColor(R.color.text_black)
                simplePagerTitleView.selectedColor = ViewUtils.getColor(R.color.color_FF513E)
                simplePagerTitleView.setOnClickListener {
                    magic_indicator1.onPageSelected(index)
                    magic_indicator1.onPageScrolled(index, 0.0F, 0)
                    mPresenter.getChildTab(index)
                }
                return simplePagerTitleView
            }

            override fun getCount() = title.size

            override fun getIndicator(context: Context?): IPagerIndicator? = null
        }
        magic_indicator1.navigator = commonNavigator
        if (topName != "0") {
            if (title.indexOf(topName) != -1) {
                magic_indicator1.onPageSelected(title.indexOf(topName))
                magic_indicator1.onPageScrolled(title.indexOf(topName), 0.0F, 0)
                mPresenter.getChildTab(title.indexOf(topName))
            }
        } else mPresenter.getChildTab(0)
    }

    fun initMagicIndicator2() {
        val title = arrayListOf("最新片源", "最多观影", "最多喜欢")
        val commonNavigator = CommonNavigator(this)
        commonNavigator.scrollPivotX = 0.35f
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getCount() = title.size
            override fun getIndicator(context: Context?): IPagerIndicator {
                val indicator = WrapPagerIndicator(context)
                indicator.fillColor = ViewUtils.getColor(R.color.color_FF513E)
                indicator.verticalPadding = 7
                indicator.horizontalPadding = 20
                return indicator
            }

            override fun getTitleView(context: Context?, index: Int): IPagerTitleView {
                val clipPagerTitleView = ClipPagerTitleView(context)
                clipPagerTitleView.text = title[index]
                clipPagerTitleView.textSize = 35F
                clipPagerTitleView.textColor = ViewUtils.getColor(R.color.text_black)
                clipPagerTitleView.clipColor = Color.WHITE
                clipPagerTitleView.setOnClickListener {
                    if (!FastClickUtil.isFastClick()){
                        magic_indicator2.onPageSelected(index)
                        magic_indicator2.onPageScrolled(index, 0.0F, 0)
                        mColumn = when (index) {
                            0 -> "updated"
                            1 -> "reads"
                            else -> "praise"
                        }
                        fragments[vpMoreVideo.currentItem].upDateView(mColumn,true)
                    } else ToastUtils.showToast("请勿频繁操作")

                }
                return clipPagerTitleView
            }
        }
        magic_indicator2.navigator = commonNavigator
    }


    fun initMagicIndicator3(title: List<String>, index: Int,isTopClick:Boolean=false) {
        val commonNavigator = CommonNavigator(this)
        commonNavigator.scrollPivotX = 0.35f
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getCount() = title.size
            override fun getIndicator(context: Context?): IPagerIndicator {
                val indicator = WrapPagerIndicator(context)
                indicator.fillColor = ViewUtils.getColor(R.color.color_FF513E)
                indicator.verticalPadding = 7
                indicator.horizontalPadding = 20
                return indicator
            }

            override fun getTitleView(context: Context?, pos: Int): IPagerTitleView {
                val clipPagerTitleView = ClipPagerTitleView(context)
                clipPagerTitleView.text = title[pos]
                clipPagerTitleView.textSize = 35F
                clipPagerTitleView.textColor = ViewUtils.getColor(R.color.text_black)
                clipPagerTitleView.clipColor = Color.WHITE
                clipPagerTitleView.setOnClickListener {
                    magic_indicator3.onPageSelected(pos)
                    magic_indicator3.onPageScrolled(pos, 0.0F, 0)
                    vpMoreVideo.currentItem = pos
                    childName = title[pos]
                }
                return clipPagerTitleView
            }
        }
        magic_indicator3.navigator = commonNavigator
        initViewPager(index, title)
    }
    var fragments= arrayListOf<VideoMoreActivityChildFragment>()
    private var viewPagerAdapter:BaseFragmentPageAdapter?=null
    private fun initViewPager(index: Int, childList: List<String>) {
        fragments.clear()
        vpMoreVideo.removeAllViews()
        viewPagerAdapter?.notifyDataSetChanged()
        for ((pos, dataCid) in tab3Cid[index]!!.withIndex()) {
            fragments.add(
                VideoMoreActivityChildFragment.newInstance(
                    mTypeId,
                    dataCid,
                    mColumn,
                    childList[pos]
                )
            )
        }
        if (!fragments.isNullOrEmpty()){
             viewPagerAdapter = BaseFragmentPageAdapter(supportFragmentManager, fragments)
            vpMoreVideo.adapter = viewPagerAdapter
            vpMoreVideo.offscreenPageLimit = 8
            if (childName != "0") {
                if (childList.indexOf(childName) != -1) {
                    vpMoreVideo.currentItem = childList.indexOf(childName)
                    magic_indicator3.onPageSelected(childList.indexOf(childName))
                    magic_indicator3.onPageScrolled(childList.indexOf(childName), 0.0F, 0)
                    bind(magic_indicator3, vpMoreVideo,childList.indexOf(childName))
                }else  bind(magic_indicator3, vpMoreVideo,0)
            }

        }
    }


    private fun bind(magicIndicator: MagicIndicator, viewPager: ViewPager,pos:Int) {
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int)
            {
                magicIndicator.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                magicIndicator.onPageSelected(position)
                fragments[vpMoreVideo.currentItem].upDateView(mColumn)
            }

            override fun onPageScrollStateChanged(state: Int) {
                magicIndicator.onPageScrollStateChanged(state)
            }
        })
        fragments[pos].upDateView(mColumn,true)
    }
}