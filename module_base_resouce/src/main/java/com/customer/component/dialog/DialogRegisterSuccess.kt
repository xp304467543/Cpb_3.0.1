package com.customer.component.dialog

import android.app.Dialog
import android.content.Context
import com.fh.module_base_resouce.R
import kotlinx.android.synthetic.main.dialog_register_success.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/25
 * @ Describe
 *
 */
class DialogRegisterSuccess (context: Context) : Dialog(context) {
    init {
        setContentView(R.layout.dialog_register_success)
        window?.setBackgroundDrawableResource(R.color.transparent)
        setCanceledOnTouchOutside(false)
        setCancelable(false)
        tvRegisterKnow.setOnClickListener {
            dismiss()
        }
    }
}