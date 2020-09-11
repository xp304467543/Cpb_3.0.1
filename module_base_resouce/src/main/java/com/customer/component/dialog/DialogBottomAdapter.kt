package com.customer.component.dialog

import android.graphics.Typeface
import android.widget.TextView
import com.fh.module_base_resouce.R
import com.lib.basiclib.base.recycle.BaseRecyclerAdapter
import com.lib.basiclib.base.recycle.RecyclerViewHolder
import com.lib.basiclib.utils.ViewUtils

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/17
 * @ Describe
 *
 */
class BottomDialogAdapter : BaseRecyclerAdapter<BottomDialogBean>() {


    override fun getItemLayoutId(viewType: Int) = R.layout.adapter_bottom_lottery_child_rank

    override fun bindData(holder: RecyclerViewHolder, position: Int, data: BottomDialogBean?) {
        if (data?.isSelect == true) {
            holder.findViewById<TextView>(R.id.tvLotteryRank).setTextColor(ViewUtils.getColor(R.color.white))
            holder.findViewById<TextView>(R.id.tvLotteryRank).background = ViewUtils.getDrawable(R.drawable.button_red_background)
            holder.findViewById<TextView>(R.id.tvLotteryRank).typeface = Typeface.defaultFromStyle(Typeface.BOLD)
        } else {
            holder.findViewById<TextView>(R.id.tvLotteryRank).setTextColor(ViewUtils.getColor(R.color.color_333333))
            holder.findViewById<TextView>(R.id.tvLotteryRank).background =ViewUtils.getDrawable(R.drawable.button_grey_background)
            holder.findViewById<TextView>(R.id.tvLotteryRank).typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
        }
        holder.text(R.id.tvLotteryRank, data?.str)
    }

}

data  class  BottomDialogBean(var str: String="",var isSelect:Boolean = true)