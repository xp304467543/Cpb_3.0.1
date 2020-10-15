package com.mine.children.recharge

import android.content.Intent
import com.customer.data.BankAddSuccess
import com.customer.data.BankCardChoose
import com.customer.data.mine.UserBankCard
import com.hwangjr.rxbus.RxBus
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.thread.EventThread
import com.lib.basiclib.base.mvp.BaseMvpActivity
import com.lib.basiclib.base.recycle.BaseRecyclerAdapter
import com.lib.basiclib.base.recycle.RecyclerViewHolder
import com.lib.basiclib.base.xui.adapter.recyclerview.XLinearLayoutManager
import com.lib.basiclib.utils.FastClickUtil
import com.lib.basiclib.utils.StatusBarUtils
import com.mine.R
import kotlinx.android.synthetic.main.act_user_bank_card.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/10/14
 * @ Describe
 *
 */
class MineUserBankCardAct : BaseMvpActivity<MineUserBankCardActPresenter>() {

    private var userCardAdapter: UserCardAdapter? = null

    override fun attachView() = mPresenter.attachView(this)

    override fun attachPresenter() = MineUserBankCardActPresenter()

    override fun isShowToolBar() = false

    override fun getContentResID() = R.layout.act_user_bank_card

    override fun isSwipeBackEnable() = true

    override fun isRegisterRxBus() = true

    override fun initContentView() {
        StatusBarUtils.setStatusBarHeight(centerBankView)
        rvUserBankCard?.layoutManager = XLinearLayoutManager(this)
        userCardAdapter = UserCardAdapter()
        rvUserBankCard?.adapter = userCardAdapter
    }

    override fun initEvent() {
        imgBankCardBack.setOnClickListener { finish() }
        tvAddBankCard.setOnClickListener {
            if (!FastClickUtil.isFastClick()) {
                startActivity((Intent(this, MineUserAddBankCard::class.java)))
            }
        }
    }

    override fun initData() {
        mPresenter.getUserBank()
    }

    fun initUserBank(it: List<UserBankCard>) {
        if (it.isNullOrEmpty()) {
            userCardAdapter?.clear()
            if (userCardPlaceHolder != null) setVisible(userCardPlaceHolder)
        } else {
            if (userCardPlaceHolder != null) setGone(userCardPlaceHolder)
            userCardAdapter?.refresh(it)
        }

    }


    inner class UserCardAdapter : BaseRecyclerAdapter<UserBankCard>() {
        override fun getItemLayoutId(viewType: Int) = R.layout.adapter_user_bank_card

        override fun bindData(holder: RecyclerViewHolder, position: Int, data: UserBankCard?) {
            holder.text(R.id.tvCardName,"存款人: "+ data?.name)
            holder.text(R.id.tvCardBank, data?.remark)
            holder.text(R.id.tvCardNo, "存款账号: "+data?.no)
            holder.findView(R.id.btUse).setOnClickListener {
                if (!FastClickUtil.isFastClick()){
                    RxBus.get().post(BankCardChoose(data?.no?:"null",data?.name?:"null"))
                    finish()
                }
            }
        }

    }

    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun upDateUserBankSelect(event: BankAddSuccess) {
        mPresenter.getUserBank()
    }

}