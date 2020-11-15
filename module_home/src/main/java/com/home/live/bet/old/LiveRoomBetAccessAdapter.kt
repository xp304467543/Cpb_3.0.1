package com.home.live.bet.old

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.home.R
import com.lib.basiclib.base.recycle.BaseRecyclerAdapter
import com.lib.basiclib.base.recycle.RecyclerViewHolder
import com.lib.basiclib.utils.ToastUtils

/**
 *
 * @ Author  QinTian
 * @ Date  2020/9/1
 * @ Describe
 *
 */

class LiveRoomBetAccessAdapter(context: Context, var is_bal:Boolean) : BaseRecyclerAdapter<LotteryBet>() {

    private var onMoneyChangeListener: ((it: String, pos: Int) -> Unit)? = null


    fun onMoneyChangeListener(OnMoneyChangeListener: ((it: String, pos: Int) -> Unit)) {
        onMoneyChangeListener = OnMoneyChangeListener
    }


    override fun getItemLayoutId(viewType: Int): Int {
     return   R.layout.old_holder_bet_access
    }

    override fun bindData(holder: RecyclerViewHolder, position: Int, data: LotteryBet?) {
        val edit = holder.findViewById<EditText>(R.id.tvBetPlayMoney)
        holder.text(R.id.tvBetPlayName, data?.playName)
        holder.text(R.id.tvBetPlayType, data?.result?.play_class_cname)
        holder.text(R.id.tvBetPlayOdds, data?.result?.play_odds.toString())

        //1、为了避免TextWatcher在第2步被调用，提前将他移除。
        if (edit.tag is TextWatcher) {
            edit.removeTextChangedListener(edit.tag as TextWatcher)
        }

        // 第2步：移除TextWatcher之后，设置EditText的Text。
        edit.setText(data?.result?.money)


        val watcher: TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if ( editable.isNotEmpty()) {
                    if (editable.toString().toLong() < 10) {
                        if (!is_bal){
                            ToastUtils.showToast("请输入≥10的整数")
                        }
                        if (is_bal){
                            if (editable.toString().toLong() < 1){
                                ToastUtils.showToast("请输入≥1的整数")
                            }
                        }
                    }
                    val now= if (editable.length > 9) {
                        edit.setText(editable.substring(0, 9)) //截取前x位
                        edit.requestFocus()
                        edit.setSelection(edit.length()) //光标移动到最后
                        editable.substring(0, 9)
                    } else {
                        editable.toString()
                    }
                    onMoneyChangeListener?.invoke(now,position)
                }
            }
        }
        edit.addTextChangedListener(watcher)
        edit.tag = watcher
    }


}