package com.customer.component.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.widget.TextView
import com.fh.module_base_resouce.R
import com.lib.basiclib.utils.ViewUtils
import kotlinx.android.synthetic.main.dialog_live_chat_recharge.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/9/4
 * @ Describe
 *
 */
class DialogReCharge(context: Context, var isDiamond:Boolean) : Dialog(context) {

    init {
        window!!.setWindowAnimations(R.style.BaseDialogAnim)
        setContentView(R.layout.dialog_live_chat_recharge)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window!!.setGravity(Gravity.CENTER or Gravity.CENTER)
        val lp = window!!.attributes
        lp.width = ViewUtils.dp2px(314) // 宽度
        lp.height = ViewUtils.dp2px(290)  // 高度
        window!!.attributes = lp

        if (isDiamond){
            tvRechargeTip.text = "钻石不足,请兑换"
            tvGoRecharge.text = "去兑换"
        }else{
            tvRechargeTip.text = "余额不足,请充值"
            tvGoRecharge.text = "去充值"
        }

        findViewById<TextView>(R.id.tvRechargeCancel).setOnClickListener {
            dismiss()
        }
        findViewById<TextView>(R.id.tvGoRecharge).setOnClickListener {
            dismiss()
            mListener?.invoke()
        }
    }


    private var mListener: (() -> Unit)? = null
    fun setOnSendClickListener(listener: () -> Unit) {
        mListener = listener
    }



}