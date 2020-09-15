package com.bet.old

import android.annotation.SuppressLint
import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bet.R
import com.customer.data.LineCheck
import com.lib.basiclib.base.recycle.BaseRecyclerAdapter
import com.lib.basiclib.base.recycle.RecyclerViewHolder
import com.lib.basiclib.base.xui.widget.button.switchbutton.SwitchButton
import com.lib.basiclib.widget.popup.BasePopupWindow

/**
 *
 * @ Author  QinTian
 * @ Date  2020/9/2
 * @ Describe
 *
 */

class LotteryLinePop(context: Context, listLine: List<LineCheck>,val pos: Int) :
    BasePopupWindow(context, R.layout.pop_line_select) {

    private var lotteryLineView: RecyclerView


    private var mLDNetPingService: NetPingManager? = null

    private var getSelectListener: ((it: String, pos: Int, ms: String) -> Unit)? = null


    fun setSelectListener(GetSelectListener: ((it: String, pos: Int, ms: String) -> Unit)) {
        getSelectListener = GetSelectListener
    }

        var rvAdapter: LineAdapter?=null
    init {
        width = ViewGroup.LayoutParams.WRAP_CONTENT
        height = ViewGroup.LayoutParams.WRAP_CONTENT
        lotteryLineView = findView(R.id.lotteryLine)
        lotteryLineView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvAdapter = LineAdapter()
        lotteryLineView.adapter = rvAdapter
        rvAdapter?.refresh(listLine)
    }

    inner class LineAdapter : BaseRecyclerAdapter<LineCheck>() {
        override fun getItemLayoutId(viewType: Int) =
            R.layout.holder_line
        override fun bindData(holder: RecyclerViewHolder, position: Int, data: LineCheck?) {

            val check = holder.findViewById<SwitchButton>(R.id.lineSwitch)
            val tvMs = holder.findViewById<TextView>(R.id.tvLineMs)
            check.isChecked = pos == position
            var real = data?.url?.split("//")?.get(1)?:"www.baidu.com"
            if (real.contains("/"))real = real.split("/")[0]
            mLDNetPingService = NetPingManager(
                context,
                real,
                object : NetPingManager.IOnNetPingListener {
                    @SuppressLint("SetTextI18n")
                    override fun ontDelay(log: Long) {
                        tvMs.post {
                            tvMs.text = (log / 2).toString() + "ms"
                            if ((log / 2) > 100) {
                                holder.textColorId(
                                    R.id.tvLineMs,
                                    R.color.colorYellow
                                )
                            } else {
                                holder.textColorId(
                                    R.id.tvLineMs,
                                    R.color.colorGreen
                                )
                            }
                        }
                    }

                    override fun onError() {
                        mLDNetPingService?.release()
                    }
                })
            mLDNetPingService?.getDelay()
            holder.text(R.id.tvLine, "线路 " + (position + 1))
            check.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    val text = tvMs.text.toString()
                    data?.boolean = false
                    val ms = if (text == "") "50" else text.substring(0, text.length - 2)
                    getSelectListener?.invoke(data?.url ?: "", position, ms)
                }
                dismiss()
            }
        }
    }
}