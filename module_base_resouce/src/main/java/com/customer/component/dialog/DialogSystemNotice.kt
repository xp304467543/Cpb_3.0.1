package com.customer.component.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Html
import android.view.Gravity
import com.fh.module_base_resouce.R
import kotlinx.android.synthetic.main.dialog_system_notice.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/9/5
 * @ Describe
 *
 */
class DialogSystemNotice(context: Context) : Dialog(context){

    init {
        window?.setWindowAnimations(R.style.BaseDialogAnim)
        setContentView(R.layout.dialog_system_notice)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setGravity(Gravity.CENTER or Gravity.CENTER)
        val lp = window?.attributes
//      lp.alpha = 0.7f // 透明度
        window?.attributes = lp
        setCanceledOnTouchOutside(true)
        btNotice.setOnClickListener {
            dismiss()
        }
    }


    fun setContent(string: String) {
        noticeContent.text = Html.fromHtml(string)
    }

}