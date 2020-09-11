package com.home.live.bet.old

import android.content.Context
import com.home.R
import com.lib.basiclib.base.recycle.BaseRecyclerAdapter
import com.lib.basiclib.base.recycle.RecyclerViewHolder
import com.lib.basiclib.base.round.RoundTextView
import com.lib.basiclib.utils.ViewUtils

/**
 *
 * @ Author  QinTian
 * @ Date  2020/9/1
 * @ Describe
 *
 */
class LotteryChildTypeAdapter(context: Context) : BaseRecyclerAdapter<String>() {

    var clickPosition: Int = 0
    override fun getItemLayoutId(viewType: Int): Int {
        return   R.layout.old_holder_lottery_child_type
    }

    override fun bindData(holder: RecyclerViewHolder, position: Int, data: String?) {
        if (clickPosition == position) {
            holder.findViewById<RoundTextView>(R.id.tvLotteryType).setTextColor(ViewUtils.getColor(R.color.color_FF513E))
            holder.findViewById<RoundTextView>(R.id.tvLotteryType).delegate.backgroundColor = ViewUtils.getColor(R.color.color_FFECE8)
        } else {
            holder.findViewById<RoundTextView>(R.id.tvLotteryType).setTextColor(ViewUtils.getColor(R.color.color_333333))
            holder.findViewById<RoundTextView>(R.id.tvLotteryType).delegate.backgroundColor = ViewUtils.getColor(R.color.color_F5F7FA)
        }
        holder.text(R.id.tvLotteryType, data)
    }

    fun changeBackground(position: Int) {
        clickPosition = position
        notifyDataSetChanged()
    }
}