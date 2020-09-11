package com.lottery.adapter

import android.graphics.Typeface
import android.widget.TextView
import com.fh.module_lottery.R
import com.lib.basiclib.base.recycle.BaseRecyclerAdapter
import com.lib.basiclib.base.recycle.RecyclerViewHolder
import com.lib.basiclib.utils.ViewUtils
import com.customer.data.lottery.LotteryTypeResponse
import com.customer.data.UserInfoSp
import cuntomer.them.Theme


/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/16
 * @ Describe
 *
 */
class LotteryTypeAdapter : BaseRecyclerAdapter<LotteryTypeResponse>() {

    var clickPosition: Int = 0

    override fun getItemLayoutId(viewType: Int) = R.layout.adapter_lottery_type

    override fun bindData(holder: RecyclerViewHolder, position: Int, data: LotteryTypeResponse?) {
        if (clickPosition == position) {
            holder.findViewById<TextView>(R.id.tvLotteryType)
                .setTextColor(ViewUtils.getColor(R.color.white))
            when (UserInfoSp.getThem()) {
                Theme.MidAutumn -> {
                    holder.findViewById<TextView>(R.id.tvLotteryType)
                        .setBackgroundResource(R.drawable.button_green_background)
                }
                Theme.LoverDay -> {
                    holder.findViewById<TextView>(R.id.tvLotteryType)
                        .setBackgroundResource(R.drawable.button_purple_background)
                }
                else -> holder.findViewById<TextView>(R.id.tvLotteryType)
                    .setBackgroundResource(R.drawable.button_red_background)
            }
            holder.findViewById<TextView>(R.id.tvLotteryType).typeface =
                Typeface.defaultFromStyle(Typeface.BOLD)
        } else {
            holder.findViewById<TextView>(R.id.tvLotteryType)
                .setTextColor(ViewUtils.getColor(R.color.color_999999))
            holder.findViewById<TextView>(R.id.tvLotteryType)
                .setBackgroundResource(R.drawable.button_grey_background)
            holder.findViewById<TextView>(R.id.tvLotteryType).typeface =
                Typeface.defaultFromStyle(Typeface.NORMAL)
        }
        holder.text(R.id.tvLotteryType, data?.cname)
    }

    fun changeBackground(position: Int) {
        clickPosition = position
        notifyDataSetChanged()
    }
}