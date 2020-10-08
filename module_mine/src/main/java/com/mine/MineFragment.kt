package com.mine

import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.customer.ApiRouter
import com.customer.component.dialog.DialogDiamond
import com.customer.component.dialog.GlobalDialog
import com.customer.data.*
import com.customer.data.mine.ChangeSkin
import com.customer.data.mine.UpDateUserPhoto
import com.glide.GlideUtil
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.thread.EventThread
import com.lib.basiclib.base.mvp.BaseMvpFragment
import com.lib.basiclib.utils.FastClickUtil
import com.lib.basiclib.utils.ToastUtils
import com.lib.basiclib.utils.ViewUtils
import com.mine.children.*
import com.mine.children.skin.MineSkinAct
import com.qw.curtain.lib.Curtain
import com.qw.curtain.lib.IGuide
import com.qw.curtain.lib.shape.RoundShape
import com.xiaojinzi.component.anno.RouterAnno
import com.xiaojinzi.component.impl.Router
import cuntomer.them.AppMode
import cuntomer.them.IMode
import cuntomer.them.ITheme
import cuntomer.them.Theme
import kotlinx.android.synthetic.main.fragment_mine.*


@RouterAnno(host = "Mine", path = "main")
class MineFragment : BaseMvpFragment<MinePresenter>(), ITheme, IMode {

    var isInit = false
    //新消息
    var msg1 = ""
    var msg2 = ""
    var msg3 = ""

    override fun attachView() = mPresenter.attachView(this)

    override fun attachPresenter() = MinePresenter()

    override fun getLayoutResID() = R.layout.fragment_mine

    override fun isRegisterRxBus() = true


    @SuppressLint("SetTextI18n")
    override fun onSupportVisible() {
        if (UserInfoSp.getIsLogin()) {
            tvMineUserNickName.text = UserInfoSp.getUserNickName()
            GlideUtil.loadCircleImage(
                requireContext(),
                UserInfoSp.getUserPhoto(),
                imgMineUserAvatar,
                true
            )
            tvMineUserId.text = "ID: " + UserInfoSp.getUserUniqueId()
            setVisible(containerLogin)
            setGone(containerNoLogin)
            mPresenter.getUserVip()
            mPresenter.getUserBalance()
            mPresenter.getUserDiamond()
            mPresenter.getNewMsg()
            mPresenter.getUserInfo()
            if (!UserInfoSp.getIsSetPayPassWord()) mPresenter.getIsSetPayPassWord()
            setVisible(containerSetting)
        } else {
            imgMineUserAvatar.setBackgroundResource(R.mipmap.ic_base_user)
            setGone(containerLogin)
            setGone(containerSetting)
            setVisible(containerNoLogin)
            tvBalance.text = "0.00"
            tvDiamondBalance.text = "0.00"
        }
        isInit = true

    }

    override fun initContentView() {
        setSwipeBackEnable(false)
        setTheme(UserInfoSp.getThem())
        setMode(UserInfoSp.getAppMode())
    }

    override fun initData() {
        mPresenter.getCustomerUrl()
        if (!UserInfoSp.getMineGuide() && UserInfoSp.getAppMode()==AppMode.Normal) {
            showCurtain()
            UserInfoSp.putMineGuide(true)
        }
    }


    private fun showCurtain() {
        Curtain(requireActivity()).withShape(tvMoneyChange, RoundShape(35f))
            .setTopView(R.layout.guide_mine)
            .withShape(tvChangeDiamond, RoundShape(35f))
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
        imgPersonal.setOnClickListener {
            if (!FastClickUtil.isFastClick()) Router.withApi(ApiRouter::class.java)
                .toUserPage(UserInfoSp.getUserId().toString())
        }

        tvLogin.setOnClickListener {
            if (!FastClickUtil.isFastClick()) Router.withApi(ApiRouter::class.java).toLogin()
        }
        imgMineUserAvatar.setOnClickListener {
            if (!FastClickUtil.isFastClick()) startActivity(
                Intent(
                    getPageActivity(),
                    MinePersonalAct::class.java
                )
            )

        }

        tvMoneyChange.setOnClickListener {
            if (!UserInfoSp.getIsLogin()) {
                GlobalDialog.notLogged(requireActivity())
                return@setOnClickListener
            }
            if (!FastClickUtil.isFastClick()) {
                startActivity(Intent(activity, MineMoneyCenterAct::class.java))
            }
        }

        tvChangeDiamond.setOnClickListener {
            if (!UserInfoSp.getIsLogin()) {
                GlobalDialog.notLogged(requireActivity())
                return@setOnClickListener
            }
            if (!UserInfoSp.getIsSetPayPassWord()) {
                GlobalDialog.noSetPassWord(requireActivity())
                return@setOnClickListener
            }
            if (!FastClickUtil.isFastClick()) {
                if (tvBalance.text.toString() != "加载中") {
                    val dialog = DialogDiamond(requireContext(), tvBalance.text.toString())
                    dialog.show()
                } else ToastUtils.showToast("请等待加载完成")
            }
        }

        tvBalance.setOnClickListener {
            if (!UserInfoSp.getIsLogin()) {
                GlobalDialog.notLogged(requireActivity())
                return@setOnClickListener
            }
            if (!FastClickUtil.isFastClick()) {
                tvBalance.text = "加载中"
                mPresenter.getUserBalance()
                mPresenter.getUserDiamond()
            }
        }

        tvDiamondBalance.setOnClickListener {
            if (!UserInfoSp.getIsLogin()) {
                GlobalDialog.notLogged(requireActivity())
                return@setOnClickListener
            }
            if (!FastClickUtil.isFastClick()) {
                tvDiamondBalance.text = "加载中"
                mPresenter.getUserBalance()
                mPresenter.getUserDiamond()
            }
        }

        tvDepositMoney.setOnClickListener {
            if (!UserInfoSp.getIsLogin()) {
                GlobalDialog.notLogged(requireActivity())
                return@setOnClickListener
            }
            if (!FastClickUtil.isFastClick()) Router.withApi(ApiRouter::class.java)
                .toMineRecharge(0)
        }

        tvDrawMoney.setOnClickListener {
            if (!UserInfoSp.getIsLogin()) {
                GlobalDialog.notLogged(requireActivity())
                return@setOnClickListener
            }
            if (!FastClickUtil.isFastClick()) Router.withApi(ApiRouter::class.java)
                .toMineRecharge(1)
        }

        tvAttention.setOnClickListener {
            if (!UserInfoSp.getIsLogin()) {
                GlobalDialog.notLogged(requireActivity())
                return@setOnClickListener
            }
            if (!FastClickUtil.isFastClick())
                if (UserInfoSp.getAppMode() == AppMode.Normal) {
                    startActivity(Intent(getPageActivity(), MineAttentionAct::class.java))
                } else if (UserInfoSp.getAppMode() == AppMode.Pure) {
                    Router.withApi(ApiRouter::class.java).toReport()
                }


        }
        containerMessageCenter.setOnClickListener {
            if (!UserInfoSp.getIsLogin()) {
                GlobalDialog.notLogged(requireActivity())
                return@setOnClickListener
            }
            containerMessageCenter.showNewMessage(false)
            if (!FastClickUtil.isFastClick()) Router.withApi(ApiRouter::class.java)
                .toMineMessage(msg1, msg2, msg3)
        }
        containerMineCheck.setOnClickListener {
            if (!UserInfoSp.getIsLogin()) {
                GlobalDialog.notLogged(requireActivity())
                return@setOnClickListener
            }
            startActivity(Intent(getPageActivity(), MineBillAct::class.java))
        }
        containerMainSkin.setOnClickListener {
            startActivity(Intent(getPageActivity(), MineSkinAct::class.java))
        }
        containerAnchorGet.setOnClickListener {
            if (!UserInfoSp.getIsLogin()) {
                GlobalDialog.notLogged(requireActivity())
                return@setOnClickListener
            }
            if (!FastClickUtil.isFastClick()) startActivity(
                Intent(
                    getPageActivity(),
                    MineAnchorRecruitAct::class.java
                )
            )
        }

        containerGameReport.setOnClickListener {
            if (!UserInfoSp.getIsLogin()) {
                GlobalDialog.notLogged(requireActivity())
                return@setOnClickListener
            }
            if (!FastClickUtil.isFastClick()) Router.withApi(ApiRouter::class.java).toReportGame()
        }

        containerTuiReport.setOnClickListener {
            if (!UserInfoSp.getIsLogin()) {
                GlobalDialog.notLogged(requireActivity())
                return@setOnClickListener
            }
            if (!FastClickUtil.isFastClick()) Router.withApi(ApiRouter::class.java).toReport()
        }

        containerFeedBack.setOnClickListener {
            if (!UserInfoSp.getIsLogin()) {
                GlobalDialog.notLogged(requireActivity())
                return@setOnClickListener
            }
            if (!FastClickUtil.isFastClick()) Router.withApi(ApiRouter::class.java).toFeedBack()
        }
        containerContactCustomer.setOnClickListener {
            if (!UserInfoSp.getIsLogin()) {
                GlobalDialog.notLogged(requireActivity())
                return@setOnClickListener
            }
            if (!FastClickUtil.isFastClick()) Router.withApi(ApiRouter::class.java)
                .toGlobalWeb(UserInfoSp.getCustomer() ?: urlCustomer)
        }
        containerGroup.setOnClickListener {
            if (!UserInfoSp.getIsLogin()) {
                GlobalDialog.notLogged(requireActivity())
                return@setOnClickListener
            }
            if (!FastClickUtil.isFastClick()) Router.withApi(ApiRouter::class.java).toContentGroup()
        }
        containerSetting.setOnClickListener {
            if (!UserInfoSp.getIsLogin()) {
                GlobalDialog.notLogged(requireActivity())
                return@setOnClickListener
            }
            if (!FastClickUtil.isFastClick()) Router.withApi(ApiRouter::class.java).toSetting()
        }
    }

    //主题
    override fun setTheme(theme: Theme) {
        when (theme) {
            Theme.Default -> {
                tvLogin.setTextColor(ViewUtils.getColor(R.color.color_FF513E))
                imgMineBg.setImageResource(R.drawable.ic_them_default_top)
                tvDepositMoney.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    getDrawable(R.mipmap.ic_mine_deposit),
                    null,
                    null
                )
                tvDrawMoney.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    getDrawable(R.mipmap.ic_mine_wallet),
                    null,
                    null
                )
                tvAttention.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    getDrawable(R.mipmap.ic_mine_attention),
                    null,
                    null
                )
                containerMessageCenter.setBackRes(R.drawable.ic_mine_default_1)
                containerMineCheck.setBackRes(R.drawable.ic_mine_default_2)
                containerMainSkin.setBackRes(R.drawable.ic_mine_default_3)
                containerAnchorGet.setBackRes(R.drawable.ic_mine_default_4)
                containerGameReport.setBackRes(R.drawable.ic_mine_default_5)
                containerTuiReport.setBackRes(R.drawable.ic_mine_default_6)
                containerFeedBack.setBackRes(R.drawable.ic_mine_default_7)
                containerContactCustomer.setBackRes(R.drawable.ic_mine_default_8)
                containerGroup.setBackRes(R.drawable.ic_mine_default_9)
                containerSetting.setBackRes(R.drawable.ic_mine_default_10)
            }
            Theme.NewYear -> {
                tvLogin.setTextColor(ViewUtils.getColor(R.color.color_FF513E))
                imgMineBg.setImageResource(R.drawable.ic_them_newyear_top)
                tvDepositMoney.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    getDrawable(R.mipmap.ic_mine_deposit),
                    null,
                    null
                )
                tvDrawMoney.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    getDrawable(R.mipmap.ic_mine_wallet),
                    null,
                    null
                )
                tvAttention.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    getDrawable(R.mipmap.ic_mine_attention),
                    null,
                    null
                )
                containerMessageCenter.setBackRes(R.drawable.ic_mine_newyear_1)
                containerMineCheck.setBackRes(R.drawable.ic_mine_newyear_2)
                containerMainSkin.setBackRes(R.drawable.ic_mine_newyear_3)
                containerAnchorGet.setBackRes(R.drawable.ic_mine_newyear_4)
                containerGameReport.setBackRes(R.drawable.ic_mine_newyear_5)
                containerTuiReport.setBackRes(R.drawable.ic_mine_newyear_6)
                containerFeedBack.setBackRes(R.drawable.ic_mine_newyear_7)
                containerContactCustomer.setBackRes(R.drawable.ic_mine_newyear_8)
                containerGroup.setBackRes(R.drawable.ic_mine_newyear_9)
                containerSetting.setBackRes(R.drawable.ic_mine_newyear_10)
            }
            Theme.MidAutumn -> {
                tvLogin.setTextColor(ViewUtils.getColor(R.color.colorGreenPrimary))
                imgMineBg.setImageResource(R.drawable.ic_them_middle_top)
                tvDepositMoney.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    getDrawable(R.drawable.ic_middle_c_1),
                    null,
                    null
                )
                tvDrawMoney.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    getDrawable(R.drawable.ic_middle_c_2),
                    null,
                    null
                )
                tvAttention.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    getDrawable(R.drawable.ic_middle_c_3),
                    null,
                    null
                )
                containerMessageCenter.setBackRes(R.drawable.ic_mine_d5_1)
                containerMineCheck.setBackRes(R.drawable.ic_mine_d5_2)
                containerMainSkin.setBackRes(R.drawable.ic_mine_d5_3)
                containerAnchorGet.setBackRes(R.drawable.ic_mine_d5_4)
                containerGameReport.setBackRes(R.drawable.ic_mine_d5_5)
                containerTuiReport.setBackRes(R.drawable.ic_mine_d5_6)
                containerFeedBack.setBackRes(R.drawable.ic_mine_d5_7)
                containerContactCustomer.setBackRes(R.drawable.ic_mine_d5_8)
                containerGroup.setBackRes(R.drawable.ic_mine_d5_9)
                containerSetting.setBackRes(R.drawable.ic_mine_d5_10)

            }
            Theme.LoverDay -> {
                tvLogin.setTextColor(ViewUtils.getColor(R.color.purple))
                imgMineBg.setImageResource(R.drawable.ic_them_love_top)
                tvDepositMoney.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    getDrawable(R.drawable.ic_love_c_1),
                    null,
                    null
                )
                tvDrawMoney.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    getDrawable(R.drawable.ic_love_c_2),
                    null,
                    null
                )
                tvAttention.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    getDrawable(R.drawable.ic_love_c_3),
                    null,
                    null
                )
                containerMessageCenter.setBackRes(R.drawable.ic_mine_love_1)
                containerMineCheck.setBackRes(R.drawable.ic_mine_love_2)
                containerMainSkin.setBackRes(R.drawable.ic_mine_love_3)
                containerAnchorGet.setBackRes(R.drawable.ic_mine_love_4)
                containerGameReport.setBackRes(R.drawable.ic_mine_love_5)
                containerTuiReport.setBackRes(R.drawable.ic_mine_love_6)
                containerFeedBack.setBackRes(R.drawable.ic_mine_love_7)
                containerContactCustomer.setBackRes(R.drawable.ic_mine_love_8)
                containerGroup.setBackRes(R.drawable.ic_mine_love_9)
                containerSetting.setBackRes(R.drawable.ic_mine_love_10)
            }
            Theme.NationDay -> {
                tvLogin.setTextColor(ViewUtils.getColor(R.color.color_EF7E12))
                imgMineBg.setImageResource(R.drawable.ic_them_gq_top)
                tvDepositMoney.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    getDrawable(R.mipmap.ic_mine_deposit),
                    null,
                    null
                )
                tvDrawMoney.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    getDrawable(R.mipmap.ic_mine_wallet),
                    null,
                    null
                )
                tvAttention.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    getDrawable(R.mipmap.ic_mine_attention),
                    null,
                    null
                )
                containerMessageCenter.setBackRes(R.drawable.ic_them_gq_1)
                containerMineCheck.setBackRes(R.drawable.ic_them_gq_2)
                containerMainSkin.setBackRes(R.drawable.ic_them_gq_3)
                containerAnchorGet.setBackRes(R.drawable.ic_them_gq_4)
                containerGameReport.setBackRes(R.drawable.ic_them_gq_5)
                containerTuiReport.setBackRes(R.drawable.ic_them_gq_6)
                containerFeedBack.setBackRes(R.drawable.ic_them_gq_7)
                containerContactCustomer.setBackRes(R.drawable.ic_them_gq_8)
                containerGroup.setBackRes(R.drawable.ic_them_gq_9)
                containerSetting.setBackRes(R.drawable.ic_them_gq_10)
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


    //退出登录
    @SuppressLint("SetTextI18n")
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun loginOut(eventBean: LoginOut) {
        if (isActive()) {
            setGone(containerLogin)
            setGone(containerSetting)
            setVisible(containerNoLogin)
            tvBalance?.text = "0.00"
            tvDiamondBalance?.text = "0.00"
        }
    }

    //更新用户头像
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun upDataUserAvatar(eventBean: UpDateUserPhoto) {
        context?.let { GlideUtil.loadCircleImage(it, eventBean.img, imgMineUserAvatar, true) }
        UserInfoSp.putUserPhoto(eventBean.img)
    }

    //更新钻石 顺便更新余额
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun upDataMineUserDiamond(eventBean: MineUserDiamond) {
        if (isSupportVisible) {
            tvDiamondBalance?.text = if (eventBean.diamond == "0") "0.00" else eventBean.diamond
            mPresenter.getUserBalance()
        }
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
                tvAttention?.text = "关注"
                tvAttention?.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    null, ViewUtils.getDrawable(R.mipmap.ic_mine_attention), null, null
                )
                containerMainSkin?.visibility = View.VISIBLE
                containerAnchorGet?.visibility = View.VISIBLE
                containerTuiReport?.visibility = View.VISIBLE
                mineViewLine?.visibility = View.VISIBLE
                tvChangeDiamond?.visibility = View.VISIBLE
                tvSecond?.visibility = View.VISIBLE
                tvDiamondBalance?.visibility = View.VISIBLE
                imgPersonal?.visibility = View.VISIBLE
                tvMineUserOther?.visibility = View.VISIBLE
                val params = tvMoneyChange.layoutParams as ConstraintLayout.LayoutParams
                params.endToEnd = R.id.mineViewLine
                tvMoneyChange.layoutParams = params
            }
            AppMode.Pure -> {
                tvAttention?.text = "代理"
                tvAttention?.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    null, ViewUtils.getDrawable(R.mipmap.ic_mode_pure_mine), null, null
                )
                containerMainSkin?.visibility = View.GONE
                containerAnchorGet?.visibility = View.GONE
                containerTuiReport?.visibility = View.GONE
                mineViewLine?.visibility = View.GONE
                tvChangeDiamond?.visibility = View.GONE
                tvSecond?.visibility = View.GONE
                tvDiamondBalance?.visibility = View.GONE
                imgPersonal?.visibility = View.GONE
                tvMineUserOther?.visibility = View.GONE
                val params = tvMoneyChange.layoutParams as ConstraintLayout.LayoutParams
                params.endToEnd = R.id.linCard
                tvMoneyChange.layoutParams = params
            }
        }
    }

}
