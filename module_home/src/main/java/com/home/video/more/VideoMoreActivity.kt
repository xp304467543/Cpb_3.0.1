package com.home.video.more

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import com.customer.ApiRouter
import com.customer.component.PopWindowVideo
import com.customer.data.VideoColumnChange
import com.customer.data.video.MovieApi
import com.customer.data.video.MovieType
import com.customer.data.video.MovieTypeChild
import com.home.R
import com.hwangjr.rxbus.RxBus
import com.lib.basiclib.base.activity.BaseNavActivity
import com.lib.basiclib.base.adapter.BaseFragmentPageAdapter
import com.lib.basiclib.utils.FastClickUtil
import com.lib.basiclib.utils.LogUtils
import com.lib.basiclib.utils.StatusBarUtils.setStatusBarHeight
import com.lib.basiclib.utils.ToastUtils
import com.lib.basiclib.utils.ViewUtils
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
 * @ Date  2020/8/30
 * @ Describe
 *
 */
@RouterAnno(host = "Home", path = "videoMore")
class VideoMoreActivity : BaseNavActivity() {

    private var topList: List<MovieType>? = null

    private var childTabList: HashMap<Int, List<String>> = HashMap()
    var childTabCid: HashMap<Int, List<Int>> = HashMap()

    var mColumn = "updated"//固定值：updated=最新 reads=最多观看 praise=最多喜欢

    private var mTypeId = -1

    private var topName = "0"

    private var childName = "0"

    private var videoPop: PopWindowVideo? = null

    private var selectItems: MovieTypeChild?=null

    override fun getContentResID() = R.layout.act_video_more

    override fun isSwipeBackEnable() = true

    override fun isShowToolBar() = false

    override fun initContentView() {
        setStatusBarHeight(stateVideoMore)
        if (intent.getStringExtra("topStr") != "0" ) {
            topName = intent.getStringExtra("topStr") ?: "0"
        }
        if (intent.getStringExtra("childStr") != "0"){
            childName = intent.getStringExtra("childStr") ?: "0"
        }
    }

    override fun initData() {
        getTabTitle()
    }


    override fun initEvent() {
        moreBack.setOnClickListener { finish() }
        imgSearch.setOnClickListener {
            if (!FastClickUtil.isFastClick()) Router.withApi(ApiRouter::class.java).toVideoSearch()
        }
        imgVideoPop.setOnClickListener {
            if (videoPop == null){
                videoPop = topList?.let { it1 -> PopWindowVideo(this, it1,selectItems) }
                videoPop?.seItemListener{
                        _, MovieTypeChild, topPos, _ ->
                    selectItems = MovieTypeChild
                    magic_indicator1.onPageSelected(topPos)
                    magic_indicator1.onPageScrolled(topPos, 0.0F, 0)
                    childName =MovieTypeChild?.name?:"0"
                    getChildTab(topPos)
                }
                videoPop?.showAsDropDown(magic_indicator1)
            }else{
                videoPop?.dismiss()
                videoPop = null
            }
        }
    }


    private fun initMagicIndicator1(title: ArrayList<String>) {
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
                    getChildTab(index)
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
                getChildTab(title.indexOf(topName))
            }
        } else getChildTab(0)
    }

    private fun initMagicIndicator2() {
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
                    magic_indicator2.onPageSelected(index)
                    magic_indicator2.onPageScrolled(index, 0.0F, 0)
                    mColumn = when (index) {
                        0 -> "updated"
                        1 -> "reads"
                        else -> "praise"
                    }
                    RxBus.get().post(VideoColumnChange(mTypeId,mColumn))
                }
                return clipPagerTitleView
            }
        }
        magic_indicator2.navigator = commonNavigator
    }

    private fun initMagicIndicator3(title: List<String>, index: Int) {
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
                    vpMoreVideo.currentItem = index
                }
                return clipPagerTitleView
            }
        }
        magic_indicator3.navigator = commonNavigator
        initViewPager(index,title)

    }

    private fun initViewPager(index: Int,childList: List<String>) {
        val fragments = arrayListOf<VideoMoreActivityChildFragment>()
        if (index != -1 && !childTabCid[index].isNullOrEmpty()) {
            for ((index,dataCid) in childTabCid[index]!!.withIndex()) {
                fragments.add(VideoMoreActivityChildFragment.newInstance(mTypeId, dataCid, mColumn,childList[index]))
            }
            val viewPagerAdapter = BaseFragmentPageAdapter(supportFragmentManager, fragments)
            vpMoreVideo.adapter = viewPagerAdapter
            vpMoreVideo.offscreenPageLimit = 30
            ViewPagerHelper.bind(magic_indicator3, vpMoreVideo)
            if (childName != "0") {
                if (childList.indexOf(childName) != -1) {
                    vpMoreVideo.currentItem = childList.indexOf(childName)
                }
            }
        } else ToastUtils.showToast("未获取到CID")

    }


    private fun getTabTitle() {
        MovieApi.getMovieType {
            onSuccess {
                topList = it
                if (!topList.isNullOrEmpty()) mTypeId = topList!![0].id ?: -1
                val tab1 = arrayListOf<String>()
                for (res in it) {
                    tab1.add(res.name ?: "未知")
                    val tab2 = arrayListOf<String>()
                    val tab2Cid = arrayListOf<Int>()
                    if (!res.children.isNullOrEmpty()) {
                        for (child in res.children!!) {
                            tab2.add(child.name ?: "未知")
                            tab2Cid.add(child.id ?: -1)

                        }
                        childTabList[res.id ?: 1] = tab2
                        childTabCid[res.id ?: 1] = tab2Cid
                    }
                }
                initMagicIndicator1(tab1)
                initMagicIndicator2()
            }
            onFailed {
                ToastUtils.showToast("获取数据失败")
            }
        }
    }

    private fun getChildTab(index: Int) {
        if (!topList.isNullOrEmpty() || topList!![index].id ?: -1 == -1) {
            val pos = topList!![index].id ?: -1
            mTypeId = pos
            childTabList[mTypeId]?.let { initMagicIndicator3(it, pos) }
        } else ToastUtils.showToast("无详细标签")
    }

}