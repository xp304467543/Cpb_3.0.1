package com.customer.component.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import com.fh.module_base_resouce.R
import com.lib.basiclib.utils.ViewUtils
import kotlinx.android.synthetic.main.dialog_vip_tips.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/23
 * @ Describe
 *
 */
class DialogVipTips (context: Context) : Dialog(context) {
    init {
        setContentView(R.layout.dialog_vip_tips)
        window?.setWindowAnimations(R.style.BaseDialogAnim)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setGravity(Gravity.CENTER or Gravity.CENTER)
        val lp = window?.attributes
        lp?.width = ViewUtils.dp2px(316) // 宽度
        lp?.height = ViewGroup.LayoutParams.WRAP_CONTENT  // 高度
//      lp.alpha = 0.7f // 透明度
        window?.attributes = lp
        initText()
        vipClose.setOnClickListener {
            dismiss()
        }
    }

    private fun initText() {

            reChange.setOnClickListener {
                dismiss()
                mListener?.invoke()
            }
            reCharge?.setOnClickListener {
                dismiss()
                mListenerCancel?.invoke()
        }
    }

    private var mListener: (() -> Unit)? = null
    fun setConfirmClickListener(listener: () -> Unit) {
        mListener = listener
    }

    private var mListenerCancel: (() -> Unit)? = null
    fun setCanCalClickListener(listener: () -> Unit) {
        mListenerCancel = listener
    }


}