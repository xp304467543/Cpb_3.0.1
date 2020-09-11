package com.lottery.children

import android.content.Context
import com.customer.ApiRouter
import com.fh.module_lottery.R
import com.glide.GlideUtil
import com.lib.basiclib.base.recycle.BaseRecyclerAdapter
import com.lib.basiclib.base.recycle.RecyclerViewHolder
import com.customer.data.lottery.LotteryExpertPaleyResponse
import com.lib.basiclib.utils.FastClickUtil
import com.xiaojinzi.component.impl.Router
import java.math.BigDecimal

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/17
 * @ Describe
 *
 */
class LotteryExpertPlanFragmentAdapter(private var context: Context) : BaseRecyclerAdapter<LotteryExpertPaleyResponse>() {
    private val TYPE_HEAD = 0
    private val TYPE_COMMON = 1
    private val TYPE_FOOT = 2

    override fun getItemLayoutId(viewType: Int): Int {
        return if (viewType == TYPE_FOOT) {
            R.layout.adapter_head
        } else {
            R.layout.adapter_lottery_child_expert_plan
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
//            0 -> {
//                TYPE_HEAD
//            }
            itemCount - 1 -> {
                return TYPE_FOOT
            }
            else -> {
                TYPE_COMMON
            }
        }
    }


    override fun bindData(
        holder: RecyclerViewHolder,
        position: Int,
        data: LotteryExpertPaleyResponse?
    ) {
        if (getItemViewType(position) == TYPE_COMMON) {


            GlideUtil.loadCircleImage(context,data?.avatar, holder.getImageView(R.id.expertPlanAvatar), true)
            holder.text(
                R.id.rtvExpertWinPresent,
                "胜率  " + BigDecimal(data?.hit_rate).setScale(2, BigDecimal.ROUND_HALF_UP).multiply(
                    BigDecimal(100)
                ).stripTrailingZeros().toPlainString() + "%"
            )
            holder.text(R.id.rtvExpertWinAdd, data?.winning + "  连红")
            holder.text(
                R.id.rtvExpertWinProfit,
                "盈利  " + BigDecimal(data?.profit_rate).setScale(2, BigDecimal.ROUND_HALF_UP)
                    .multiply(
                        BigDecimal(100)
                    ).stripTrailingZeros().toPlainString() + "%"
            )
            holder.text(R.id.tvExpertPlanType, data?.method)
            holder.text(R.id.tvExpertPlanName, data?.nickname)
            holder.text(R.id.tvExpertIssue, data?.issue + " 期")
            val str = StringBuilder()
            if (!data?.code.isNullOrEmpty()) {
                for (result in data?.code?.split(",")!!) {
                    str.append("$result    ")
                }
            }
            holder.text(R.id.tvTenList, str)
            if (data?.last_10_games != null) {
                for ((index, it) in data.last_10_games!!.withIndex()) {
                    val color = if (it == "1") {
                        R.color.color_FF513E
                    } else R.color.color_999999
                    val text = if (it == "1") {
                        "胜"
                    } else "负"

                    when (index) {
                        0 -> {
                            holder.text(R.id.tvCode_0, text)
                            holder.textColorId(R.id.tvCode_0, color)
                        }
                        1 -> {
                            holder.text(R.id.tvCode_1, text)
                            holder.textColorId(R.id.tvCode_1, color)
                        }
                        2 -> {
                            holder.text(R.id.tvCode_2, text)
                            holder.textColorId(R.id.tvCode_2, color)
                        }
                        3 -> {
                            holder.text(R.id.tvCode_3, text)
                            holder.textColorId(R.id.tvCode_3, color)
                        }
                        4 -> {
                            holder.text(R.id.tvCode_4, text)
                            holder.textColorId(R.id.tvCode_4, color)
                        }
                        5 -> {
                            holder.text(R.id.tvCode_5, text)
                            holder.textColorId(R.id.tvCode_5, color)
                        }
                        6 -> {
                            holder.text(R.id.tvCode_6, text)
                            holder.textColorId(R.id.tvCode_6, color)
                        }
                        7 -> {
                            holder.text(R.id.tvCode_7, text)
                            holder.textColorId(R.id.tvCode_7, color)
                        }
                        8 -> {
                            holder.text(R.id.tvCode_8, text)
                            holder.textColorId(R.id.tvCode_8, color)
                        }
                        9 -> {
                            holder.text(R.id.tvCode_9, text)
                            holder.textColorId(R.id.tvCode_9, color)
                        }
                    }
                }
            }
            holder.click(R.id.expertPlanAvatar) {
//            LaunchUtils.startPersonalPage(getContext(), getData()?.expert_id!!, 3, getData()?.lottery_id
//                ?: "-1")
                if (!FastClickUtil.isFastClick())Router.withApi(ApiRouter::class.java).toExpertPage(
                    data?.expert_id.toString(),data?.lottery_id.toString()
                )
            }
        }
    }


}