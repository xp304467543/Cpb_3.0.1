package com.lottery.adapter

import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.customer.component.VerticalTextView
import com.customer.utils.countdowntimer.lotter.LotteryComposeUtil
import com.customer.utils.countdowntimer.lotter.LotteryConstant
import com.fh.module_lottery.R
import com.google.gson.JsonArray
import com.lib.basiclib.base.recycle.BaseRecyclerAdapter
import com.lib.basiclib.base.recycle.RecyclerViewHolder
import com.lib.basiclib.utils.ViewUtils
import com.customer.data.UserInfoSp
import com.lib.basiclib.base.round.RoundTextView
import cuntomer.them.Theme
import java.util.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/18
 * @ Describe
 *
 */
class LotteryChildLuZhuAdapter(private val context: Context, private val lotteryID: String) :
    BaseRecyclerAdapter<List<List<String>>>() {

    var type: String = LotteryConstant.TYPE_LUZHU_2
    var total: JsonArray? = null
    var hideList: ArrayList<Boolean>? = null

    var isRePlay = true


    override fun getItemLayoutId(viewType: Int) = R.layout.adapter_lottery_child_luzhu
    override fun bindData(holder: RecyclerViewHolder, position: Int, data: List<List<String>>?) {
        LotteryComposeUtil.initTitle(
            holder.findViewById(R.id.tvLuZhuTotal),
            holder.findViewById(R.id.tvLuZhuBallIndex),
            lotteryID,
            type,
            total,
            position
        )
        initLuZhuData(holder.findViewById(R.id.horizontalRecycle), data)
        val param = holder.itemView.layoutParams
        when(UserInfoSp.getThem()){
            Theme.MidAutumn -> holder.findViewById<RoundTextView>(R.id.topLine).delegate.backgroundColor = ViewUtils.getColor(R.color.colorGreen)
            Theme.LoverDay -> holder.findViewById<RoundTextView>(R.id.topLine).delegate.backgroundColor = ViewUtils.getColor(R.color.purple)
            Theme.NationDay -> holder.findViewById<RoundTextView>(R.id.topLine).delegate.backgroundColor = ViewUtils.getColor(R.color.color_EF7E12)
            else -> holder.findViewById<RoundTextView>(R.id.topLine).delegate.backgroundColor = ViewUtils.getColor(R.color.text_red)
        }
        if (hideList != null && hideList!!.isNotEmpty()) {
            if (!hideList!![position]) {
                param.height = 0
                param.width = 0
                ViewUtils.setGone(holder.itemView)
            } else {
                param.height = ConstraintLayout.LayoutParams.WRAP_CONTENT
                param.width = ConstraintLayout.LayoutParams.MATCH_PARENT
                ViewUtils.setVisible(holder.itemView)
            }
        } else {
            param.height = ConstraintLayout.LayoutParams.WRAP_CONTENT
            param.width = ConstraintLayout.LayoutParams.MATCH_PARENT
            ViewUtils.setVisible(holder.itemView)
        }
    }
    private fun initLuZhuData(recyclerView: RecyclerView, data: List<List<String>>?) {
        val adapter = LotteryChildLuZhuTestItem()
        recyclerView.setItemViewCacheSize(300)
        val layout = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layout
        recyclerView.adapter = adapter
        layout.reverseLayout = true//列表翻转
        if (isRePlay){
            Collections.reverse(data) //反向排序
        }
        adapter.refresh(data)
    }
    inner class LotteryChildLuZhuTestItem: BaseRecyclerAdapter<List<String>>() {
        override fun getItemLayoutId(viewType: Int) =  R.layout.adapter_lottery_child_item_test_luzhu

        override fun bindData(holder: RecyclerViewHolder, position: Int, data: List<String>?) {
            val view = holder.findViewById<VerticalTextView>(R.id.linLuZhuDataItemText)
            if (data != null) {
                for (it in data) {
                    if (it != "") view.setText(it.split("_")[1]) else view.setTextEmpty()
                }
                when {
                    position == 0 -> view.setBackgroundResource(R.drawable.storke_right)
                    position % 2 == 0 -> view.setBackgroundResource(R.drawable.storke_left_grey)
                    else -> view.setBackgroundResource(R.drawable.storke_left_white)
                }
            }

        }

    }
    fun notifyHideItem(it: ArrayList<Boolean>) {
        hideList = it
        isRePlay = false
        notifyDataSetChanged()
    }

    fun  clearList(){
        hideList?.clear()
    }

}