package com.fh.view

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import androidx.core.content.ContextCompat
import com.customer.ApiRouter
import com.customer.component.dialog.*
import com.customer.component.rain.RainViewGroup
import com.customer.data.*
import com.customer.data.home.HomeApi
import com.customer.data.login.LoginSuccess
import com.customer.data.login.RegisterSuccess
import com.customer.data.mine.ChangeSkin
import com.customer.utils.RxPermissionHelper
import com.fh.R
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.thread.EventThread
import com.lib.basiclib.base.fragment.BaseContentFragment
import com.lib.basiclib.base.fragment.BaseFragment
import com.lib.basiclib.base.fragment.PlaceholderFragment
import com.lib.basiclib.utils.FastClickUtil
import com.lib.basiclib.utils.ViewUtils
import com.services.BetService
import com.services.HomeService
import com.services.LotteryService
import com.services.MineService
import com.tbruyelle.rxpermissions2.RxPermissions
import com.xiaojinzi.component.impl.Router
import com.xiaojinzi.component.impl.service.ServiceManager
import cuntomer.them.AppMode
import cuntomer.them.IMode
import cuntomer.them.ITheme
import cuntomer.them.Theme
import kotlinx.android.synthetic.main.fragment_main.*

/**
 * @ Author  QinTian
 * @ Date  2020/9/4
 * @ Describe
 */
class MainFragment : BaseContentFragment(), ITheme, IMode {


    private var bottomWebSelect: BottomWebSelect? = null

    val mFragments = arrayListOf<BaseFragment>()


    override fun getContentResID() = R.layout.fragment_main


    override fun isSwipeBackEnable() = false

    override fun isRegisterRxBus() = true


    @SuppressLint("CheckResult")
    override fun initContentView() {
        setTheme(UserInfoSp.getThem())
        RxPermissions(this)
            .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .subscribe { granted ->
                if (granted) {
                    // 已获得所有权限
                } else {
                    // 部分或所有权限被拒绝
                }
            }
        initFragments()
        setMode(UserInfoSp.getAppMode())
    }


    private fun initFragments() {
        val homeFragment = ServiceManager.get(HomeService::class.java)?.getHomeFragment()
            ?: PlaceholderFragment.newInstance()
        val gameFragment = ServiceManager.get(BetService::class.java)?.getGameFragment()
            ?: PlaceholderFragment.newInstance()
        val lotteryFragment = ServiceManager.get(LotteryService::class.java)?.getLotteryFragment()
            ?: PlaceholderFragment.newInstance()
        val mineFragment = ServiceManager.get(MineService::class.java)?.getMineFragment()
            ?: PlaceholderFragment.newInstance()
        mFragments.add(homeFragment)
        mFragments.add(gameFragment)
        mFragments.add(lotteryFragment)
        mFragments.add(mineFragment)
        loadMultipleRootFragment(
            R.id.mainContainer,
            0,
            mFragments[0],
            mFragments[1],
            mFragments[2],
            mFragments[3]
        )
    }

    override fun initData() {
        checkDialog()
//        getUpDate()
        getNotice()
    }


    private fun redRain() {
        rainView.setIsIntercept(true)
        //屏幕中最多显示item的数量
        rainView?.setAmount(50);
        //设置下落的次数。在保持密度不变的情况下，设置下落数量。例如：数量 = 50，下落次数 = 3，总共数量150。
        // rainView.setTimes(2);
        //设置无效循环
        rainView?.setTimes(RainViewGroup.INFINITE)
        rainView?.start()
    }

    override fun initEvent() {
        tabHome.setOnClickListener {
            showHideFragment(mFragments[0])
        }
        tabGame.setOnClickListener {
            showHideFragment(mFragments[1])
        }
        tabLotteryOpen.setOnClickListener {
            showHideFragment(mFragments[2])
        }
        tabMine.setOnClickListener {
            showHideFragment(mFragments[3])
        }
        rainView.setOnClickListener {
            if (!FastClickUtil.isFastClick()) {
                getRedRainAmount()

            }
        }
    }


    private fun getRedRainAmount() {
        HomeApi.getRedRain {
            onSuccess {
                val dialog = context?.let { it1 -> DialogRedRain(it1, it.amount.toString()) }
                dialog?.setOnDismissListener {
                    DialogRegisterSuccess(requireActivity()).show()
                }
                dialog?.show()
                rainView?.stop()
                rainView?.setIsIntercept(false)
            }
            onFailed {
//                ToastUtils.showToast(it.getMsg())
                rainView?.stop()
                rainView?.setIsIntercept(false)
            }
        }
    }

    /***
     * 回到主页面弹出一些列的窗口
     */
    private fun checkDialog() {
        // 权限弹窗
        RxPermissionHelper.request(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.SYSTEM_ALERT_WINDOW,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA
        )
    }

    //系统公告
    private fun getNotice() {
        if (UserInfoSp.getAppMode() == AppMode.Normal) {
            HomeApi.getSystemNotice {
                onSuccess {
                    if (it.content != null) {
                        val dialog = DialogSystemNotice(requireActivity())
                        dialog.setContent(it.content.toString())
                        dialog.show()
                    }
                }
            }
        } else {
            HomeApi.getSystemNoticeDl {
                onSuccess {
                    if (!it.isNullOrEmpty()) {
                        val dialog = DialogSystemNotice(requireActivity())
                        dialog.setContent(it[0].content.toString())
                        dialog.show()
                    }
                }
            }
        }
    }


    //更新
    private fun getUpDate() {
        HomeApi.getVersion {
            onSuccess {
                if (it.version_data != null) {
                    val dialog = DialogVersion(requireActivity())
                    dialog.setContent(it.version_data?.upgradetext!!)
                    if (it.version_data?.enforce == 1) {
                        dialog.setCanceledOnTouchOutside(false)
                        dialog.setCancelable(false)
                    } else {
                        dialog.setCanceledOnTouchOutside(true)
                        dialog.setCancelable(true)
                    }
                    dialog.setJum(it.version_data?.downloadurl!!)
                    dialog.show()
                }
            }
        }
    }


    //主题
    @SuppressLint("ResourceType")
    override fun setTheme(theme: Theme) {
        when (theme) {
            Theme.Default -> {
                tabHome.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    null, ViewUtils.getDrawable(
                        R.drawable.button_tab_home
                    ), null, null
                )
                tabLotteryOpen.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    null, ViewUtils.getDrawable(
                        R.drawable.button_tab_lottery
                    ), null, null
                )
                tabGame.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    null, ViewUtils.getDrawable(
                        R.drawable.button_tab_quiz
                    ), null, null
                )
                tabMine.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    null, ViewUtils.getDrawable(
                        R.drawable.button_tab_mine
                    ), null, null
                )
                tabLotteryOpen.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    null, ViewUtils.getDrawable(
                        R.drawable.button_tab_lottery
                    ), null, null
                )
                tabGame.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    null, ViewUtils.getDrawable(
                        R.drawable.button_tab_quiz
                    ), null, null
                )
                tabMine.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    null, ViewUtils.getDrawable(
                        R.drawable.button_tab_mine
                    ), null, null
                )
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    tabHome.setTextColor(
                        ContextCompat.getColorStateList(
                            requireContext(),
                            R.drawable.tab_bottom_menu_text_selector
                        )
                    )
                    tabLotteryOpen.setTextColor(
                        ContextCompat.getColorStateList(
                            requireContext(),
                            R.drawable.tab_bottom_menu_text_selector
                        )
                    )
                    tabGame.setTextColor(
                        ContextCompat.getColorStateList(
                            requireContext(),
                            R.drawable.tab_bottom_menu_text_selector
                        )
                    )
                    tabMine.setTextColor(
                        ContextCompat.getColorStateList(
                            requireContext(),
                            R.drawable.tab_bottom_menu_text_selector
                        )
                    )
                }
            }
            Theme.NewYear -> {
                val drawable1 = ViewUtils.getDrawable(R.drawable.ic_tab_new_year_1)
                val drawable2 = ViewUtils.getDrawable(R.drawable.ic_tab_new_year_2)
                val drawable3 = ViewUtils.getDrawable(R.drawable.ic_tab_new_year_4)
                val drawable4 = ViewUtils.getDrawable(R.drawable.ic_tab_new_year_3)
                drawable1?.setBounds(0, 0, ViewUtils.dp2px(30), ViewUtils.dp2px(30))
                drawable2?.setBounds(0, 0, ViewUtils.dp2px(30), ViewUtils.dp2px(30))
                drawable3?.setBounds(0, 0, ViewUtils.dp2px(30), ViewUtils.dp2px(30))
                drawable4?.setBounds(0, 0, ViewUtils.dp2px(30), ViewUtils.dp2px(30))
                tabHome.setCompoundDrawables(null, drawable1, null, null)
                tabGame.setCompoundDrawables(null, drawable2, null, null)
                tabLotteryOpen.setCompoundDrawables(null, drawable3, null, null)
                tabMine.setCompoundDrawables(null, drawable4, null, null)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    tabHome.setTextColor(
                        ContextCompat.getColorStateList(
                            requireContext(),
                            R.drawable.tab_bottom_menu_text_selector
                        )
                    )
                    tabLotteryOpen.setTextColor(
                        ContextCompat.getColorStateList(
                            requireContext(),
                            R.drawable.tab_bottom_menu_text_selector
                        )
                    )
                    tabGame.setTextColor(
                        ContextCompat.getColorStateList(
                            requireContext(),
                            R.drawable.tab_bottom_menu_text_selector
                        )
                    )
                    tabMine.setTextColor(
                        ContextCompat.getColorStateList(
                            requireContext(),
                            R.drawable.tab_bottom_menu_text_selector
                        )
                    )
                }
            }
            Theme.MidAutumn -> {
                val drawable1 = ViewUtils.getDrawable(R.drawable.ic_tab_d5_1)
                val drawable2 = ViewUtils.getDrawable(R.drawable.ic_tab_d5_2)
                val drawable3 = ViewUtils.getDrawable(R.drawable.ic_tab_d5_3)
                val drawable4 = ViewUtils.getDrawable(R.drawable.ic_tab_d5_4)
                drawable1?.setBounds(0, 0, ViewUtils.dp2px(30), ViewUtils.dp2px(30))
                drawable2?.setBounds(0, 0, ViewUtils.dp2px(30), ViewUtils.dp2px(30))
                drawable3?.setBounds(0, 0, ViewUtils.dp2px(30), ViewUtils.dp2px(30))
                drawable4?.setBounds(0, 0, ViewUtils.dp2px(30), ViewUtils.dp2px(30))
                tabHome.setCompoundDrawables(null, drawable1, null, null)
                tabGame.setCompoundDrawables(null, drawable2, null, null)
                tabLotteryOpen.setCompoundDrawables(null, drawable3, null, null)
                tabMine.setCompoundDrawables(null, drawable4, null, null)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    tabHome.setTextColor(
                        ContextCompat.getColorStateList(
                            requireContext(),
                            R.drawable.tab_them_text_middle
                        )
                    )
                    tabLotteryOpen.setTextColor(
                        ContextCompat.getColorStateList(
                            requireContext(),
                            R.drawable.tab_them_text_middle
                        )
                    )
                    tabGame.setTextColor(
                        ContextCompat.getColorStateList(
                            requireContext(),
                            R.drawable.tab_them_text_middle
                        )
                    )
                    tabMine.setTextColor(
                        ContextCompat.getColorStateList(
                            requireContext(),
                            R.drawable.tab_them_text_middle
                        )
                    )
                }
            }
            Theme.LoverDay -> {
                val drawable1 = ViewUtils.getDrawable(R.drawable.ic_tab_love_1)
                val drawable2 = ViewUtils.getDrawable(R.drawable.ic_tab_love_2)
                val drawable3 = ViewUtils.getDrawable(R.drawable.ic_tab_love_4)
                val drawable4 = ViewUtils.getDrawable(R.drawable.ic_tab_love_5)
                drawable1?.setBounds(0, 0, ViewUtils.dp2px(30), ViewUtils.dp2px(25))
                drawable2?.setBounds(0, 0, ViewUtils.dp2px(30), ViewUtils.dp2px(25))
                drawable3?.setBounds(0, 0, ViewUtils.dp2px(30), ViewUtils.dp2px(25))
                drawable4?.setBounds(0, 0, ViewUtils.dp2px(30), ViewUtils.dp2px(25))
                tabHome.setCompoundDrawables(null, drawable1, null, null)
                tabGame.setCompoundDrawables(null, drawable2, null, null)
                tabLotteryOpen.setCompoundDrawables(null, drawable3, null, null)
                tabMine.setCompoundDrawables(null, drawable4, null, null)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    tabHome.setTextColor(
                        ContextCompat.getColorStateList(
                            requireContext(),
                            R.drawable.tab_them_text_love
                        )
                    )
                    tabLotteryOpen.setTextColor(
                        ContextCompat.getColorStateList(
                            requireContext(),
                            R.drawable.tab_them_text_love
                        )
                    )
                    tabGame.setTextColor(
                        ContextCompat.getColorStateList(
                            requireContext(),
                            R.drawable.tab_them_text_love
                        )
                    )
                    tabMine.setTextColor(
                        ContextCompat.getColorStateList(
                            requireContext(),
                            R.drawable.tab_them_text_love
                        )
                    )
                }
            }
            Theme.NationDay -> {
                val drawable1 = ViewUtils.getDrawable(R.drawable.ic_tab_gq_1)
                val drawable2 = ViewUtils.getDrawable(R.drawable.ic_tab_gq_2)
                val drawable3 = ViewUtils.getDrawable(R.drawable.ic_tab_gq_3)
                val drawable4 = ViewUtils.getDrawable(R.drawable.ic_tab_gq_4)
                drawable1?.setBounds(0, 0, ViewUtils.dp2px(30), ViewUtils.dp2px(25))
                drawable2?.setBounds(0, 0, ViewUtils.dp2px(30), ViewUtils.dp2px(25))
                drawable3?.setBounds(0, 0, ViewUtils.dp2px(30), ViewUtils.dp2px(25))
                drawable4?.setBounds(0, 0, ViewUtils.dp2px(30), ViewUtils.dp2px(25))
                tabHome.setCompoundDrawables(null, drawable1, null, null)
                tabGame.setCompoundDrawables(null, drawable2, null, null)
                tabLotteryOpen.setCompoundDrawables(null, drawable3, null, null)
                tabMine.setCompoundDrawables(null, drawable4, null, null)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    tabHome.setTextColor(
                        ContextCompat.getColorStateList(
                            requireContext(),
                            R.drawable.tab_them_text_nation
                        )
                    )
                    tabLotteryOpen.setTextColor(
                        ContextCompat.getColorStateList(
                            requireContext(),
                            R.drawable.tab_them_text_nation
                        )
                    )
                    tabGame.setTextColor(
                        ContextCompat.getColorStateList(
                            requireContext(),
                            R.drawable.tab_them_text_nation
                        )
                    )
                    tabMine.setTextColor(
                        ContextCompat.getColorStateList(
                            requireContext(),
                            R.drawable.tab_them_text_nation
                        )
                    )
                }
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

    /**
     * 接收Home头像点击事件
     */
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun onClickMine(clickMine: HomeJumpToMine) {
        if (clickMine.jump) {
            if (tabMine != null) tabMine?.isChecked = true
            showHideFragment(mFragments[3])
        }
    }

    /**
     * live去充值
     */
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun homeJumpToMineCloseLive(clickMine: HomeJumpTo) {
        if (tabMine != null) tabMine?.isChecked = true
        showHideFragment(mFragments[3])
        if (clickMine.isOpenAct) Router.withApi(ApiRouter::class.java).toMineRecharge(0)
    }


    //注册成功后红包雨
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun loginInfoResponse(eventBean: RegisterSuccess) {
        if (eventBean.isShowDialog) {
            redRain()
        }
    }

    //登录成功
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun login(eventBean: LoginSuccess) {
        if (isAdded) {
            HomeApi.getIsShowRed {
                onSuccess { redRain() }
            }
        }
    }


    /**
     * live去Bet
     */
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun toBetView(eventBean: ToBetView) {
        if (tabGame != null) tabGame?.isChecked = true
        showHideFragment(mFragments[1])
    }


    //纯净版切换
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun changeSkin(eventBean: AppChangeMode) {
        if (isAdded) {
            setMode(eventBean.mode)
        }
    }

    override fun setMode(mode: AppMode) {
        when (mode) {
            AppMode.Normal -> {
                showHideFragment(mFragments[0])
                setVisible(tabHome)
                if (tabHome != null) tabHome?.isChecked = true
            }
            AppMode.Pure -> {
                showHideFragment(mFragments[1])
                setGone(tabHome)
                if (tabGame != null) tabGame?.isChecked = true
            }
        }
    }


}