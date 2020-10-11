package com.mine.children.recharge

import android.content.Intent
import android.text.TextUtils
import androidx.recyclerview.widget.LinearLayoutManager
import com.customer.ApiRouter
import com.customer.component.dialog.DialogInvest
import com.customer.component.dialog.GlobalDialog
import com.customer.data.mine.MineApi
import com.customer.data.mine.MinePayTypeList
import com.glide.GlideUtil
import com.lib.basiclib.base.fragment.BaseContentFragment
import com.lib.basiclib.base.recycle.BaseRecyclerAdapter
import com.lib.basiclib.base.recycle.RecyclerViewHolder
import com.lib.basiclib.utils.FastClickUtil
import com.lib.basiclib.utils.ToastUtils
import com.mine.R
import com.xiaojinzi.component.impl.Router
import kotlinx.android.synthetic.main.fragment_recharge.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/25
 * @ Describe 充值
 *
 */
class MineRechargeActChild1 : BaseContentFragment() {

    override fun getContentResID() = R.layout.fragment_recharge

    override fun isSwipeBackEnable() = false

    override fun initData() {
        getPayTypeList()
    }

    private fun getPayTypeList() {
        showPageLoadingDialog()
        MineApi.getPayTypeList {
            onSuccess {
                if (isSupportVisible) {
                    initAdapter(it)
                }
            }
            onFailed {
                GlobalDialog.showError(requireActivity(), it)
            }
        }
    }

    override fun initEvent() {
        rl_kami.setOnClickListener {
            if (!FastClickUtil.isFastClick()) startActivity(
                Intent(
                    getPageActivity(),
                    MineCardRechargeAct::class.java
                )
            )
        }
//        rl_bank.setOnClickListener {
//            startActivity(
//                Intent(
//                    getPageActivity(),
//                    MineBankCardRechargeAct::class.java
//                )
//            )
//        }
    }


    private fun initAdapter(data: List<MinePayTypeList>) {
        val mineRechargeItemAdapter = MineRechargeItemAdapter()
        mineRechargeItemAdapter.refresh(data)
        rvRecharges.adapter = mineRechargeItemAdapter
        val value = object : LinearLayoutManager(getPageActivity()) {
            override fun canScrollVertically(): Boolean {
                return true
            }
        }
        rvRecharges.layoutManager = value
        hidePageLoadingDialog()
    }

    /**
     * @ Describe 充值Adapter
     */
    inner class MineRechargeItemAdapter : BaseRecyclerAdapter<MinePayTypeList>() {
        override fun getItemLayoutId(viewType: Int) = R.layout.adapter_mine_recharge_item

        override fun bindData(holder: RecyclerViewHolder, position: Int, data: MinePayTypeList?) {
            if (data?.pay_type != "yhkcz") {
                holder.text(R.id.tvBankName, data?.channels_type)
            }else   holder.text(R.id.tvBankName, "银行卡充值")
            holder.text(
                R.id.tvMoneyBorder,
                "(" + (if ((data?.low_money + "") == "null") "" else data?.low_money) + " ~ " + (if ((data?.low_money + "") == "null") "" else data?.high_money) + ")"
            )
            context?.let {
                GlideUtil.loadCircleImage(
                    it,
                    data?.icon?.replace("\\", "/"),
                    holder.getImageView(R.id.imgBankType)
                )
            }

            holder.itemView.setOnClickListener {
                when (data?.pay_type) {
                    "rgcz" -> {
                        Router.withApi(ApiRouter::class.java)
                            .toRechargeWeb(0F, data.id, data.apiroute, true)
                    }
                    "yhkcz" -> {

                        startActivity(
                            Intent(
                                getPageActivity(),
                                MineBankCardRechargeAct::class.java
                            )
                        )
                    }
                    else -> {
                        val dialog = context?.let {
                            DialogInvest(
                                it,
                                data?.channels_type.toString(), "确定", "取消"
                            )
                        }
                        dialog?.setConfirmClickListener {
                            if (data != null) {
                                judgeMoney(dialog, data)
                            }
                        }
                        dialog?.show()
                    }
                }
            }
        }

        private fun judgeMoney(dialog: DialogInvest, it: MinePayTypeList) {
            if (!TextUtils.isEmpty(dialog.getText())) {
                val money = dialog.getText().toDouble()
                if (it.high_money.toDouble() >= money && it.low_money.toDouble() <= money) {
                    Router.withApi(ApiRouter::class.java)
                        .toRechargeWeb(money.toFloat(), it.id, it.apiroute, false)
                    dialog.dismiss()
                } else ToastUtils.showToast("充值金额为:" + it.low_money + "~" + it.high_money)

            } else ToastUtils.showToast("充值金额为:" + it.low_money + "~" + it.high_money)
        }
    }

}