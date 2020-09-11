package com.home.live.bet.old

import android.content.Context
import com.customer.data.lottery.LotteryBetHistoryResponse
import com.home.R
import com.lib.basiclib.base.recycle.BaseRecyclerAdapter
import com.lib.basiclib.base.recycle.RecyclerViewHolder
import com.lib.basiclib.utils.TimeUtils

/**
 *
 * @ Author  QinTian
 * @ Date  2020/9/1
 * @ Describe
 *
 */
class LiveRoomRecordAdapter (context: Context, val pos:Int) : BaseRecyclerAdapter<LotteryBetHistoryResponse>() {

    var currentSel = "0"


    override fun getItemLayoutId(viewType: Int): Int {
       return  R.layout.old_holder_bet_history
    }

    override fun bindData(
        holder: RecyclerViewHolder,
        position: Int,
        data: LotteryBetHistoryResponse?
    ) {
        holder.text(R.id.tvBetTime, TimeUtils.longToDateString(data?.play_bet_time ?: 0))
        holder.text(R.id.tvBetName, data?.play_bet_lottery_name)
        holder.text(R.id.tvBetIssue, data?.play_bet_issue + " 期")
        holder.text(R.id.tvBetCodeName, data?.play_sec_name)
        holder.text(R.id.tvBetCode, data?.play_class_name)
        holder.text(R.id.tvBetOdds, data?.play_odds)
        if (pos==1){
            if (currentSel == "0"){
                holder.text(R.id.tvBetMoney, data?.play_bet_sum+"\n钻石")
            }else holder.text(R.id.tvBetMoney, data?.play_bet_sum+"\n余额")

        }else{
            if (currentSel == "0"){
                holder.text(R.id.tvBetMoney, data?.play_bet_score+"\n钻石")
            }else holder.text(R.id.tvBetMoney, data?.play_bet_score+"\n余额")
            if (data?.play_bet_score?.contains("+") == true) {
                holder.textColorId(R.id.tvBetMoney, R.color.color_FF513E)
            } else  holder.textColorId(R.id.tvBetMoney, R.color.color_333333)
    }}
}