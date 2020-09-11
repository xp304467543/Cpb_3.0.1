package com.lottery.adapter

import com.fh.module_lottery.R
import com.lib.basiclib.base.recycle.BaseRecyclerAdapter
import com.lib.basiclib.base.recycle.RecyclerViewHolder
import com.lib.basiclib.base.round.RoundTextView
import com.lib.basiclib.utils.ViewUtils

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/17
 * @ Describe
 *
 */
class LotteryChildTypeAdapter : BaseRecyclerAdapter<String>() {

    var clickPosition: Int = 0

    override fun getItemLayoutId(viewType: Int) =R.layout.adapter_lottery_child_type

    override fun bindData(holder: RecyclerViewHolder, position: Int, item: String?) {
        if (clickPosition == position) {
            holder.findViewById<RoundTextView>(R.id.tvLotteryType).setTextColor(ViewUtils.getColor(R.color.color_FF513E))
            holder.findViewById<RoundTextView>(R.id.tvLotteryType).delegate.backgroundColor = ViewUtils.getColor(R.color.color_FFECE8)
        } else {
            holder.findViewById<RoundTextView>(R.id.tvLotteryType).setTextColor(ViewUtils.getColor(R.color.color_333333))
            holder.findViewById<RoundTextView>(R.id.tvLotteryType).delegate.backgroundColor = ViewUtils.getColor(R.color.color_F5F7FA)
        }
        holder.text(R.id.tvLotteryType, item)
    }

    fun changeBackground(position: Int) {
        clickPosition = position
        notifyDataSetChanged()
    }
}