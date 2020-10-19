package com.mine.children.recharge

import android.annotation.SuppressLint
import com.customer.adapter.TabNormalAdapter
import com.hwangjr.rxbus.RxBus
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.thread.EventThread
import com.lib.basiclib.base.activity.BaseNavActivity
import com.lib.basiclib.base.adapter.BaseFragmentPageAdapter
import com.lib.basiclib.utils.ToastUtils
import com.lib.basiclib.utils.ViewUtils
import com.lib.basiclib.widget.tab.ViewPagerHelper
import com.lib.basiclib.widget.tab.buildins.commonnavigator.CommonNavigator
import com.mine.R
import com.customer.data.mine.MineApi
import com.customer.data.mine.MineUpDateMoney
import com.xiaojinzi.component.anno.RouterAnno
import com.customer.component.dialog.GlobalDialog
import com.customer.data.UserInfoSp
import com.lib.basiclib.utils.FastClickUtil
import cuntomer.them.AppMode
import kotlinx.android.synthetic.main.act_recharge.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/25
 * @ Describe 充值提现
 *
 */
@RouterAnno(host = "Mine", path = "recharge")
class MineRechargeAct : BaseNavActivity() {


    override fun isShowBackIconWhite() = false

    override fun isSwipeBackEnable() = true

    override fun getContentResID() = R.layout.act_recharge

    override fun isShowToolBar() = false


    override fun initContentView() {
        val title = arrayListOf("充值", "提现")
        val commonNavigator = CommonNavigator(this)
        commonNavigator.scrollPivotX = 0.65f
        commonNavigator.isAdjustMode = true
        commonNavigator.adapter = TabNormalAdapter(
            titleList = title, viewPage = viewPagerRecharge,
            colorTextSelected = ViewUtils.getColor(R.color.color_333333),
            colorTextNormal = ViewUtils.getColor(R.color.color_AFAFAF),
            colorLine = ViewUtils.getColor(R.color.color_FF513E),
            textSize = 16F
        )
        rechargeTab.navigator = commonNavigator
        if (UserInfoSp.getAppMode() == AppMode.Pure){
            setGone(linDiamond)
        }
    }


    private fun initBottom() {
        val fragments = arrayListOf(
            MineRechargeActChild1(),
            MineRechargeActChild2.newInstance(tvCountBalance.text.toString())
        )
        viewPagerRecharge.adapter = BaseFragmentPageAdapter(supportFragmentManager, fragments)
        viewPagerRecharge.offscreenPageLimit = 2
        ViewPagerHelper.bind(rechargeTab, viewPagerRecharge)
        viewPagerRecharge.currentItem = intent.getIntExtra("index", 0)
    }

    override fun initData() {
        upDateBalance(true)
    }

    override fun initEvent() {
        imgGoBack.setOnClickListener {
            finish()
        }
        tvRecycle.setOnClickListener {
            if (!FastClickUtil.isTenFastClick()){
                recycleAll()
            }else ToastUtils.showToast("点击过于频繁,请10秒后重试")
        }
    }


    private fun recycleAll() {
        showPageLoadingDialog("转账中")
        MineApi.recycleAll {
            onSuccess {
                hidePageLoadingDialog()
                ToastUtils.showToast("转账成功")
                upDateBalance(false)
            }
            onFailed {
                hidePageLoadingDialog()
                ToastUtils.showToast(it.getMsg())
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun upDateBalance(isUpdate: Boolean) {
        MineApi.getUserBalance {
            onSuccess {
                tvCountBalance.text = it.balance.toString()
                RxBus.get().post(
                    MineUpDateMoney(
                        it.balance.toString(),
                        true
                    )
                )
                if (isUpdate) initBottom()
            }
            onFailed {
                GlobalDialog.showError(this@MineRechargeAct, it)
            }
        }
        upDateDiamond()
    }

    private fun upDateDiamond() {
        MineApi.getUserDiamond {
            onSuccess {
                tvCountBalanceDiamond.text = it.diamond
            }
            onFailed {
                ToastUtils.showToast("钻石获取失败")
            }
        }
    }

    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun upDateUserMoney(event: MineUpDateMoney) {
        if (!event.isUpdate) {
            if (!event.isDiamond) upDateBalance(false) else upDateDiamond()
        }
    }

}