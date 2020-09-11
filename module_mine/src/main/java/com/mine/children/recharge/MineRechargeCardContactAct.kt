package com.mine.children.recharge

import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import androidx.recyclerview.widget.LinearLayoutManager
import com.customer.component.dialog.DialogRechargeSuccess
import com.glide.GlideUtil
import com.lib.basiclib.base.activity.BaseNavActivity
import com.lib.basiclib.base.recycle.BaseRecyclerAdapter
import com.lib.basiclib.base.recycle.RecyclerViewHolder
import com.lib.basiclib.utils.StatusBarUtils
import com.lib.basiclib.utils.ToastUtils
import com.lib.basiclib.utils.ViewUtils
import com.mine.R
import com.customer.data.mine.MineApi
import com.customer.data.mine.MineRechargeDiamond
import kotlinx.android.synthetic.main.act_card_contact.*
import kotlinx.android.synthetic.main.act_card_contact.imgBack

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/25
 * @ Describe
 *
 */
class MineRechargeCardContactAct : BaseNavActivity() {

    override fun getPageTitle() = "充值"

    override fun getContentResID() = R.layout.act_card_contact

    override fun isShowToolBar() = false

    override fun isSwipeBackEnable() = true

    override fun isShowBackIconWhite() = false

    var adapter = RvAdapter()

    override fun initContentView() {
        StatusBarUtils.setStatusBarHeight(cardStateView)
        cardRv.adapter = adapter
        cardRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    override fun initEvent() {
        imgBack.setOnClickListener { finish() }
        val content = "1.代理只负责卡密销售，如果您有其他问题，请联系我们在线客服处理。代理如果骚扰或推荐您到其他平台，欢迎举报，举报属实奖励5千元\n\n" +
                "2.点击图标，系统自动复制对应（支付宝，qq、微信）账号，打开对应（支付宝，qq、微信）号，添加好友购买.\n\n" +
                "3.代理人员会提供，账号密码，返回客户端输入账号密码充值。 \n\n" +
                "4.充值完成，提示充值成功，返回钱包页面查看金额。\n\n" +
                "5.代理人员不定期更换，为了避免不必要损失每次支付请到平台提单，以官方为准，谢谢！"
        imgTips.setOnClickListener {
            DialogRechargeSuccess(this, content, 0, "充值说明").show()
        }
    }

    override fun initData() {
        showPageLoadingDialog()
        MineApi.cardList {
            onSuccess {
                adapter.refresh(it)
                hidePageLoadingDialog()
            }
            onFailed {
                ToastUtils.showToast("获取失败")
                hidePageLoadingDialog()
            }
        }
    }

    inner class RvAdapter : BaseRecyclerAdapter<MineRechargeDiamond>() {
        override fun getItemLayoutId(viewType: Int) = R.layout.adapter_diamond_recharge

        override fun bindData(
            holder: RecyclerViewHolder,
            position: Int,
            data: MineRechargeDiamond?
        ) {
            holder.text(R.id.tvTipTitle, data?.name)
            val spannableString = SpannableString("可用额度: " + data?.quota)
            spannableString.setSpan(
                ForegroundColorSpan(Color.parseColor("#ff513e")),
                6,
                spannableString.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            holder.text(R.id.tvTipContent, spannableString)

            if (data?.contact?.size ?: 0 > 0) {
                GlideUtil.loadImage(
                    data?.contact?.get(0)?.icon ?: "",
                    holder.findViewById(R.id.img_1)
                )
                holder.text(R.id.tv_1, data?.contact?.get(0)?.title ?: "")
                holder.click(R.id.lin1) {
                    setContentText(
                        data?.contact?.get(0)?.title ?: "",
                        data?.contact?.get(0)?.value ?: ""
                    )
                }
                if (data?.contact?.size ?: 0 > 1) {
                    GlideUtil.loadImage(
                        data?.contact?.get(1)?.icon ?: "",
                        holder.findViewById(R.id.img_2)
                    )
                    holder.text(R.id.tv_2, data?.contact?.get(1)?.title ?: "")
                    holder.click(R.id.lin2) {
                        setContentText(
                            data?.contact?.get(1)?.title ?: "",
                            data?.contact?.get(1)?.value ?: ""
                        )
                    }
                    if (data?.contact?.size ?: 0 > 2) {
                        GlideUtil.loadImage(
                            data?.contact?.get(2)?.icon ?: "",
                            holder.findViewById(R.id.img_3)
                        )
                        holder.text(R.id.tv_3, data?.contact?.get(2)?.title ?: "")
                        holder.click(R.id.lin3) {
                            setContentText(
                                data?.contact?.get(2)?.title ?: "",
                                data?.contact?.get(2)?.value ?: ""
                            )
                        }
                    }
                }
            }
        }

        private fun setContentText(type: String, copy: String) {
            if (type != "" && copy != "") {
                ViewUtils.copyText(copy)
                val text = "请前往" + type + "联系"
                val spannableString = SpannableString(text)
                spannableString.setSpan(
                    ForegroundColorSpan(Color.parseColor("#ff513e")),
                    3,
                    type.length + 3,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                DialogRechargeSuccess(
                    this@MineRechargeCardContactAct,
                    "",
                    0,
                    "已复制联系方式",
                    spannableString
                ).show()
            }
        }
    }

}