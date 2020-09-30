package com.customer.component.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import com.fh.module_base_resouce.R

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/26
 * @ Describe
 *
 */
class LoadingDialog(context: Context) : Dialog(context) {


    init {
        setContentView(R.layout.dialog_loading)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setGravity(Gravity.CENTER or Gravity.CENTER)
        window?.setDimAmount(0f)
        val lp = window?.attributes
//      lp.alpha = 0.7f // 透明度
        window?.attributes = lp
    }


}