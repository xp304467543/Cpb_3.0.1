package com.lottery.adapter

import android.content.Context
import com.customer.utils.countdowntimer.lotter.LotteryConstant
import com.customer.utils.countdowntimer.lotter.LotteryTypeSelectUtil
import com.fh.module_lottery.R
import com.lib.basiclib.base.recycle.BaseRecyclerAdapter
import com.lib.basiclib.base.recycle.RecyclerViewHolder
import com.lib.basiclib.utils.TimeUtils
import com.customer.data.lottery.LotteryCodeHistoryResponse
import java.util.ArrayList

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/18
 * @ Describe
 *
 */
class LotteryHistoryOpenCodeAdapter(val context: Context, val lotteryId: String, type: String) :
    BaseRecyclerAdapter<LotteryCodeHistoryResponse>() {

    private var typeSelect = type

    override fun getItemLayoutId(viewType: Int): Int {
        return R.layout.adapter_lottery_history
    }

    override fun bindData(
        holder: RecyclerViewHolder,
        position: Int,
        data: LotteryCodeHistoryResponse?
    ) {

        if (data?.issue != "") holder.text(R.id.tvOpenCount, data?.issue + "期")
        if (data?.input_time != "" && data?.input_time != "0") holder.text(
            R.id.tvOpenTime,
            TimeUtils.initData(data?.input_time ?: "0")
        )
        when (lotteryId) {
            "8" -> {
                val tbList = data?.code?.split(",") as ArrayList<String>
                tbList.add(6, "+")
                LotteryTypeSelectUtil.addOpenCode(
                    context,
                    holder.findViewById(R.id.codeContainer),
                    tbList,
                    lotteryId
                )
                when (typeSelect) {

                    LotteryConstant.TYPE_5 -> LotteryTypeSelectUtil.specialLotterySum(
                        context,
                        holder.findViewById(R.id.codeContainer),
                        data.code?.split(",") as ArrayList<String>

                    )

                    LotteryConstant.TYPE_6 -> LotteryTypeSelectUtil.lotterySpecialCode(
                        context,
                        holder.findViewById(R.id.codeContainer),
                        data.code?.split(",") as ArrayList<String>
                    )
                }
            }
            else -> {
                when (typeSelect) {
                    LotteryConstant.TYPE_1 -> LotteryTypeSelectUtil.addOpenCode(
                        context,
                        holder.findViewById(R.id.codeContainer),
                        data?.code?.split(","),
                        lotteryId
                    )
                    LotteryConstant.TYPE_2 -> LotteryTypeSelectUtil.addOpenCodeBigger(
                        context,
                        holder.findViewById(R.id.codeContainer),
                        data?.code?.split(",") ?: arrayListOf(),
                        lotteryId
                    )
                    LotteryConstant.TYPE_3 -> LotteryTypeSelectUtil.addOpenCodeSingle(
                        context,
                        holder.findViewById(R.id.codeContainer),
                        data?.code?.split(",") ?: arrayListOf()
                    )
                    LotteryConstant.TYPE_4 -> LotteryTypeSelectUtil.addSumAndDragonAndTiger(
                        context,
                        holder.findViewById(R.id.codeContainer),
                        data?.code?.split(",") ?: arrayListOf()
                    )
                    LotteryConstant.TYPE_7 -> LotteryTypeSelectUtil.addOpenCodeTypeAndSum(
                        context,
                        holder.findViewById(R.id.codeContainer),
                        data?.code?.split(",") ?: arrayListOf()
                    )
                }
            }
        }
    }


    fun changeDiffType(types: String) {
        this.typeSelect = types
        notifyDataSetChanged()
    }

}