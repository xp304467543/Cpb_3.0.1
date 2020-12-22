package com.bet

import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import com.customer.ApiRouter
import com.customer.adapter.TabScaleAdapterBet
import com.customer.component.dialog.GlobalDialog
import com.customer.component.marquee.DisplayEntity
import com.customer.component.marquee.MarqueeTextView
import com.customer.data.*
import com.customer.data.game.GameAll
import com.customer.data.home.HomeSystemNoticeResponse
import com.customer.data.mine.ChangeSkin
import com.glide.GlideUtil
import com.hwangjr.rxbus.RxBus
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.thread.EventThread
import com.lib.basiclib.base.adapter.BaseFragmentPageAdapter
import com.lib.basiclib.base.mvp.BaseMvpFragment
import com.lib.basiclib.utils.FastClickUtil
import com.lib.basiclib.utils.StatusBarUtils
import com.lib.basiclib.utils.ViewUtils
import com.lib.basiclib.widget.tab.ViewPagerHelper
import com.lib.basiclib.widget.tab.buildins.commonnavigator.CommonNavigator
import com.xiaojinzi.component.impl.Router
import cuntomer.them.AppMode
import cuntomer.them.IMode
import cuntomer.them.ITheme
import cuntomer.them.Theme
import kotlinx.android.synthetic.main.fragment_game.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/9/13
 * @ Describe
 *
 */

class GameMainFragment : BaseMvpFragment<GameMainPresenter>(), ITheme, IMode {

    override fun attachView() = mPresenter.attachView(this)

    override fun attachPresenter() = GameMainPresenter()

    override fun getLayoutResID() = R.layout.fragment_game

    override fun isRegisterRxBus() = true

    override fun isShowBackIconWhite() = false

    override fun initContentView() {
        StatusBarUtils.setStatusBarHeight(gameStateView)
        if (UserInfoSp.getAppMode() == AppMode.Pure) {
            tvAppMode.text = "直播版"
        } else {
            tvAppMode.text = "纯净版"
        }
        setTheme(UserInfoSp.getThem())
        setMode(UserInfoSp.getAppMode())
    }

    override fun initData() {
        mPresenter.getAllGame()
    }


    override fun initEvent() {
        betAppSwitchMode?.setOnClickListener {
            if (!FastClickUtil.isFastClick()) {
                val anim = AnimationUtils.loadAnimation(context, R.anim.left_out) as AnimationSet
                anim.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationRepeat(animation: Animation?) {
                    }

                    override fun onAnimationStart(animation: Animation?) {
                    }

                    override fun onAnimationEnd(animation: Animation?) {
                        if (UserInfoSp.getAppMode() == AppMode.Normal) {
                            tvAppMode.text = "纯净版"
                            UserInfoSp.putAppMode(AppMode.Pure)
                            RxBus.get().post(AppChangeMode(AppMode.Pure))
                            setVisible(betMarquee)
                            RxBus.get().post(ChangeSkin(1))
                            UserInfoSp.putThem(1)
                        } else {
                            tvAppMode.text = "直播版"
                            UserInfoSp.putAppMode(AppMode.Normal)
                            RxBus.get().post(AppChangeMode(AppMode.Normal))
                            setGone(betMarquee)
                        }
                    }
                })
                betAppSwitchMode?.startAnimation(anim)
            }
        }

        imgBetUserRecharge?.setOnClickListener {
            if (!FastClickUtil.isFastClick()) {
                if (!UserInfoSp.getIsLogin()) {
                    GlobalDialog.notLogged(requireActivity())
                    return@setOnClickListener
                }
                Router.withApi(ApiRouter::class.java).toMineRecharge(0)
            }
        }
        imgBetUserIcon?.setOnClickListener {
            if (!FastClickUtil.isFastClick()) {
                if (UserInfoSp.getIsLogin()) RxBus.get().post(
                    HomeJumpToMine(true)
                )
                else Router.withApi(ApiRouter::class.java).toLogin()
            }
        }

        gameCustomer.setOnClickListener {
            if (!FastClickUtil.isFastClick()) Router.withApi(ApiRouter::class.java)
                .toGlobalWeb(UserInfoSp.getCustomer() ?: urlCustomer)
        }
        imgGoToRight.setOnClickListener {
            if (!FastClickUtil.isFastClick()){
                commonNavigator?.fullScroll()
            }
        }
        tvReGet.setOnClickListener {
            if (!FastClickUtil.isFastClick()){
                mPresenter.getAllGame()
            }
        }
    }

    fun initViewPager(data: ArrayList<GameAll>) {
        if (vpGame != null) {
            val fragments = arrayListOf<Fragment>()
            for ((index,item) in data.withIndex()){
                if (index == 0){
                   fragments.add(GameMainChildFragment.newInstance(0, data = item))
                }else {
                    fragments.add(GameMainChildOtherFragment.newInstance(index, data = item))
                }
            }
            val adapter = BaseFragmentPageAdapter(childFragmentManager, fragments)
            vpGame?.adapter = adapter
            val list = arrayListOf<String>()
            for (item in data) {
                list.add(item.name ?: "未知")
            }
            initTopTab(list)
        }
    }

    private var tabAdapter: TabScaleAdapterBet? = null
    private var commonNavigator:CommonNavigator?= null
    private fun initTopTab(mDataList: ArrayList<String>) {
        if (vpGame != null) {
            commonNavigator = CommonNavigator(context)
            commonNavigator?.scrollPivotX = 0.65f
            tabAdapter = TabScaleAdapterBet(
                titleList = mDataList,
                viewPage = vpGame,
                normalColor = ViewUtils.getColor(R.color.color_333333),
                selectedColor = ViewUtils.getColor(R.color.color_FF513E),
                colorLine = ViewUtils.getColor(R.color.color_333333),
                textSize = 14F
            )
            commonNavigator?.adapter = tabAdapter
            vpGame.offscreenPageLimit = 7
            gameSwitchVideoTab.navigator = commonNavigator
            ViewPagerHelper.bind(gameSwitchVideoTab, vpGame)
        }
    }


    override fun setTheme(theme: Theme) {
        when (theme) {
            Theme.Default -> {
                imgBetUserRecharge.setTextColor(ViewUtils.getColor(R.color.alivc_orange))
                imgBetUserRecharge.background = ViewUtils.getDrawable(R.mipmap.ic_home_top_recharge)
                imgGameBg.setImageResource(R.drawable.ic_them_default_top)
                gameCustomer.background = ViewUtils.getDrawable(R.mipmap.ic_customer)
            }
            Theme.NewYear -> {
                imgBetUserRecharge.setTextColor(ViewUtils.getColor(R.color.color_FF513E))
                imgBetUserRecharge.background = ViewUtils.getDrawable(R.mipmap.ic_home_top_recharge)
                imgGameBg.setImageResource(R.drawable.ic_them_newyear_top)
                gameCustomer.background = ViewUtils.getDrawable(R.mipmap.ic_customer_xn)
            }
            Theme.MidAutumn -> {
                imgBetUserRecharge.setTextColor(ViewUtils.getColor(R.color.colorGreenPrimary))
                imgBetUserRecharge.background = ViewUtils.getDrawable(R.drawable.home_white)
                imgGameBg.setImageResource(R.drawable.ic_them_middle_top)
                gameCustomer.background = ViewUtils.getDrawable(R.mipmap.ic_customer_middle)
            }
            Theme.LoverDay -> {
                imgBetUserRecharge.setTextColor(ViewUtils.getColor(R.color.purple))
                imgBetUserRecharge.background = ViewUtils.getDrawable(R.drawable.home_white)
                imgGameBg.setImageResource(R.drawable.ic_them_love_top)
                gameCustomer.background = ViewUtils.getDrawable(R.mipmap.ic_customer_love)
            }
            Theme.NationDay -> {
                imgBetUserRecharge.setTextColor(ViewUtils.getColor(R.color.color_EF7E12))
                imgBetUserRecharge.background = ViewUtils.getDrawable(R.mipmap.ic_home_top_recharge)
                imgGameBg.setImageResource(R.drawable.ic_them_gq_top)
                gameCustomer.background = ViewUtils.getDrawable(R.mipmap.ic_customer_gq)
            }
            Theme.ChristmasDay -> {
                imgBetUserRecharge.setTextColor(ViewUtils.getColor(R.color.color_SD))
                imgBetUserRecharge.background = ViewUtils.getDrawable(R.mipmap.ic_home_top_recharge_sd)
                imgGameBg.setImageResource(R.drawable.ic_them_sd_top)
                gameCustomer.background = ViewUtils.getDrawable(R.mipmap.ic_customer_sd)
            }
        }
        tabAdapter?.notifyDataSetChanged()
    }


    override fun onSupportVisible() {
        super.onSupportVisible()
        RxBus.get().post(UnDateTopGame())
        if (UserInfoSp.getIsShowAppModeChange()) {
            setVisible(betAppSwitchMode)
        } else setGone(betAppSwitchMode)
        if (UserInfoSp.getIsLogin()) {
            GlideUtil.loadCircleImage(
                requireContext(),
                UserInfoSp.getUserPhoto(),
                imgBetUserIcon,
                true
            )
        } else imgBetUserIcon.setImageResource(R.mipmap.ic_base_user)
    }


    //========= 公告 =========
    fun upDateSystemNotice(data: List<HomeSystemNoticeResponse>?) {
        val result = ArrayList<String>()
        if (data != null && data.isNotEmpty()) {
            data.forEachIndexed { index, value ->
                result.add((index + 1).toString() + "." + value.content)
            }
        } else result.add("暂无公告。")
        tvNoticeMassages.setOnClickListener {
            if (!FastClickUtil.isFastClick()) {
                Router.withApi(ApiRouter::class.java)
                    .toGlobalWeb("", true, data?.get(tvNoticeMassages.currentIndex)?.id ?: "-1")
            }
        }
        tvNoticeMassages.setOnMarqueeListener(object : MarqueeTextView.OnMarqueeListener {
            override fun onStartMarquee(displayEntity: DisplayEntity?, index: Int): DisplayEntity? {
                return displayEntity
            }

            override fun onMarqueeFinished(displayDatas: MutableList<DisplayEntity>): MutableList<DisplayEntity> {
                return displayDatas
            }

        })
        tvNoticeMassages.speed = 3
        tvNoticeMassages.startSimpleRoll(result)

    }

    //换肤
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun changeSkin(eventBean: ChangeSkin) {
        when (eventBean.id) {
            1 -> setTheme(Theme.Default)
            2 -> setTheme(Theme.NewYear)
            3 -> setTheme(Theme.MidAutumn)
            4 -> setTheme(Theme.LoverDay)
            5 -> setTheme(Theme.NationDay)
            6 -> setTheme(Theme.ChristmasDay)
        }
    }

    //纯净版切换
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun changeMode(eventBean: AppChangeMode) {
        if (isActive()) {
            setMode(eventBean.mode)
        }

    }


    override fun setMode(mode: AppMode) {
        when (mode) {
            AppMode.Normal -> {
                tvAppMode.text = "纯净版"
                setGone(betMarquee)
                setGone(imgBetUserRecharge)
                setGone(imgBetUserIcon)
                setGone(gameCustomer)
                tvTopName.text = "游戏中心"
            }
            AppMode.Pure -> {
                tvAppMode.text = "直播版"
                setVisible(betMarquee)
                setVisible(imgBetUserRecharge)
                setVisible(imgBetUserIcon)
                setVisible(gameCustomer)
                tvTopName.text = "乐购"
            }
        }
    }
}