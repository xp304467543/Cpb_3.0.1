package com.home

import android.content.Intent
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.AnimationUtils
import androidx.appcompat.widget.AppCompatImageView
import com.customer.ApiRouter
import com.customer.adapter.TabScaleAdapter
import com.customer.component.dialog.GlobalDialog
import com.customer.data.*
import com.customer.data.mine.ChangeSkin
import com.customer.data.mine.UpDateUserPhoto
import com.glide.GlideUtil
import com.home.children.HomeNewHandTask
import com.home.live.search.LiveSearchActivity
import com.hwangjr.rxbus.RxBus
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.thread.EventThread
import com.lib.basiclib.base.adapter.BaseFragmentPageAdapter
import com.lib.basiclib.base.fragment.BaseFragment
import com.lib.basiclib.base.mvp.BaseMvpFragment
import com.lib.basiclib.utils.FastClickUtil
import com.lib.basiclib.utils.StatusBarUtils
import com.lib.basiclib.utils.ViewUtils
import com.lib.basiclib.widget.tab.ViewPagerHelper
import com.lib.basiclib.widget.tab.buildins.commonnavigator.CommonNavigator
import com.qw.curtain.lib.Curtain
import com.qw.curtain.lib.IGuide
import com.xiaojinzi.component.anno.RouterAnno
import com.xiaojinzi.component.impl.Router
import cuntomer.them.AppMode
import cuntomer.them.IMode
import cuntomer.them.ITheme
import cuntomer.them.Theme
import kotlinx.android.synthetic.main.fragment_home.*


@RouterAnno(host = "Home", path = "main")
class HomeFragment : BaseMvpFragment<HomePresenter>(), ITheme, IMode {

    //新消息
    var msg1 = ""
    var msg2 = ""
    var msg3 = ""

    override fun attachView() = mPresenter.attachView(this)

    override fun attachPresenter() = HomePresenter()

    override fun getLayoutResID() = R.layout.fragment_home

    override fun isRegisterRxBus() = true


    override fun onSupportVisible() {
        super.onSupportVisible()
        if (UserInfoSp.getIsLogin()) {
            GlideUtil.loadCircleImage(
                requireContext(),
                UserInfoSp.getUserPhoto(),
                imgHomeUserIcon,
                true
            )
            mPresenter.getNewMsg()
            mPresenter.getRedTask()
        } else imgHomeUserIcon.setImageResource(R.mipmap.ic_base_user)
        if (UserInfoSp.getIsShowAppModeChange()){
            setVisible(homeAppSwitchMode)
        }else setGone(homeAppSwitchMode)
    }


    override fun initContentView() {
        setSwipeBackEnable(false)
        if (UserInfoSp.getAppMode() == AppMode.Pure) {
            tvAppMode.text = "直播版"
        } else {
            tvAppMode.text = "纯净版"
        }
        StatusBarUtils.setStatusBarHeight(statusViewHome)
        initViewPager()
        initTopTab()
        setTheme(UserInfoSp.getThem())
        setMode(UserInfoSp.getAppMode())
    }

    override fun initData() {
        if (!UserInfoSp.getMainGuide()) {
            showCurtain()
            UserInfoSp.putMainGuide(true)
        }
    }


    private fun showCurtain() {
        Curtain(requireActivity()).with(imgNewTask)
            .setTopView(R.layout.guide_main)
            .setCallBack(object : Curtain.CallBack {
                override fun onDismiss(iGuide: IGuide?) {}
                override fun onShow(iGuide: IGuide?) {
                    iGuide?.findViewByIdInTopView<AppCompatImageView>(R.id.imgKnow)
                        ?.setOnClickListener {
                            iGuide.dismissGuide()
                        }
                }
            })
            .show()

    }

    override fun initEvent() {
        homeCustomer.setOnClickListener {
            if (!FastClickUtil.isFastClick()) Router.withApi(ApiRouter::class.java)
                .toGlobalWeb(UserInfoSp.getCustomer() ?: urlCustomer)
        }
        imgHomeUserIcon.setOnClickListener {
            if (UserInfoSp.getIsLogin()) RxBus.get().post(
                HomeJumpToMine(true)
            )
            else Router.withApi(ApiRouter::class.java).toLogin()
        }
        imgHomeUserRecharge.setOnClickListener {
            if (!UserInfoSp.getIsLogin()) {
                GlobalDialog.notLogged(requireActivity())
                return@setOnClickListener
            }
            Router.withApi(ApiRouter::class.java).toMineRecharge(0)
        }
        imgHomeTopMessage.setOnClickListener {
            if (!UserInfoSp.getIsLogin()) {
                GlobalDialog.notLogged(requireActivity())
                return@setOnClickListener
            }
            Router.withApi(ApiRouter::class.java).toMineMessage(msg1, msg2, msg3)
        }
        imgHomeTopSearch.setOnClickListener {
            if (homeSwitchViewPager.currentItem == 0) {
                startActivity(Intent(getPageActivity(), LiveSearchActivity::class.java))
            } else Router.withApi(ApiRouter::class.java).toVideoSearch()
        }
        imgNewTask.setOnClickListener {
            if (!UserInfoSp.getIsLogin()) {
                GlobalDialog.notLogged(requireActivity())
                return@setOnClickListener
            }
            startActivity(Intent(activity, HomeNewHandTask::class.java))
        }

        homeAppSwitchMode?.setOnClickListener {
            if (!FastClickUtil.isFastClick()) {
                val anim = AnimationUtils.loadAnimation(context, R.anim.left_out) as AnimationSet
                anim.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationRepeat(animation: Animation?) {
                    }

                    override fun onAnimationStart(animation: Animation?) {
                    }

                    override fun onAnimationEnd(animation: Animation?) {
                        if (UserInfoSp.getAppMode() == AppMode.Pure) {
                            tvAppMode.text = "直播版"
                            UserInfoSp.putAppMode(AppMode.Normal)
                            RxBus.get().post(AppChangeMode(AppMode.Normal))
                        } else {
                            tvAppMode.text = "纯净版"
                            UserInfoSp.putAppMode(AppMode.Pure)
                            RxBus.get().post(AppChangeMode(AppMode.Pure))
                        }
                    }
                })
                homeAppSwitchMode?.startAnimation(anim)
            }
        }
    }

    private fun initViewPager() {
        val fragments = arrayListOf<BaseFragment>(HomeRecommendNewFragment(), HomeVideoFragment())
        val adapter = BaseFragmentPageAdapter(childFragmentManager, fragments)
        homeSwitchViewPager.adapter = adapter
    }

    private fun initTopTab() {
        val mDataList = arrayListOf("热门", "影视区")
        val commonNavigator = CommonNavigator(context)
        commonNavigator.scrollPivotX = 0.65f
        commonNavigator.adapter = TabScaleAdapter(
            titleList = mDataList,
            viewPage = homeSwitchViewPager,
            normalColor = ViewUtils.getColor(R.color.white),
            selectedColor = ViewUtils.getColor(R.color.white),
            colorLine = ViewUtils.getColor(R.color.white),
            isChange = false
        )
        homeSwitchVideoTab.navigator = commonNavigator
        ViewPagerHelper.bind(homeSwitchVideoTab, homeSwitchViewPager)
    }

    //主题
    override fun setTheme(theme: Theme) {
        when (theme) {
            Theme.Default -> {
                imgHomeUserRecharge.setTextColor(ViewUtils.getColor(R.color.alivc_orange))
                imgHomeBg.setImageResource(R.drawable.ic_them_default_top)
                imgHomeUserRecharge.background =
                    ViewUtils.getDrawable(R.mipmap.ic_home_top_recharge)
            }
            Theme.NewYear -> {
                imgHomeUserRecharge.setTextColor(ViewUtils.getColor(R.color.color_FF513E))
                imgHomeBg.setImageResource(R.drawable.ic_them_newyear_top)
                imgHomeUserRecharge.background =
                    ViewUtils.getDrawable(R.mipmap.ic_home_top_recharge)
            }
            Theme.MidAutumn -> {
                imgHomeUserRecharge.setTextColor(ViewUtils.getColor(R.color.colorGreenPrimary))
                imgHomeBg.setImageResource(R.drawable.ic_them_middle_top)
                imgHomeUserRecharge.background = ViewUtils.getDrawable(R.drawable.home_white)
            }
            Theme.LoverDay -> {
                imgHomeUserRecharge.setTextColor(ViewUtils.getColor(R.color.purple))
                imgHomeBg.setImageResource(R.drawable.ic_them_love_top)
                imgHomeUserRecharge.background = ViewUtils.getDrawable(R.drawable.home_white)
            }
            Theme.NationDay -> {
                imgHomeUserRecharge.setTextColor(ViewUtils.getColor(R.color.color_EF7E12))
                imgHomeBg.setImageResource(R.drawable.ic_them_gq_top)
                imgHomeUserRecharge.background =
                    ViewUtils.getDrawable(R.mipmap.ic_home_top_recharge)
            }
        }
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
        }

    }

//    //登录成功dialog
//    @Subscribe(thread = EventThread.MAIN_THREAD)
//    fun loginInfoResponse(eventBean: RegisterSuccess) {
//        if (eventBean.isShowDialog) {
//            DialogRegisterSuccess(requireActivity()).show()
//        }
//    }

    //更新用户头像
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun upDataUserAvatar(eventBean: UpDateUserPhoto) {
        if (imgHomeUserIcon != null) {
            context?.let { GlideUtil.loadCircleImage(it, eventBean.img, imgHomeUserIcon, true) }
        }

    }

    //退出登录
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun loginOut(eventBean: LoginOut) {
        if (isActive() && imgHomeUserIcon != null) imgHomeUserIcon?.setImageResource(R.mipmap.ic_base_user)
    }

    //纯净版切换
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun changeMode(eventBean: AppChangeMode) {
        if (isActive()){
            setMode(eventBean.mode)
        }
    }

    override fun setMode(mode: AppMode) {
        if (mode == AppMode.Pure) {
            tvAppMode?.text = "直播版"
        } else {
            tvAppMode?.text = "纯净版"

        }

    }
}
