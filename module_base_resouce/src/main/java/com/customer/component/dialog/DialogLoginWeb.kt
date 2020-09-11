package com.customer.component.dialog

import android.app.Dialog
import android.content.Context
import android.view.ViewGroup
import com.fh.module_base_resouce.R
import kotlinx.android.synthetic.main.dialog_web.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/23
 * @ Describe
 *
 */
class DialogLoginWeb (context: Context) : Dialog(context) {

    init {
        setContentView(R.layout.dialog_web)
        val lp = window!!.attributes
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT  // 宽度
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT
        window!!.attributes = lp
        dialogWeb.loadUrl("file:///android_asset/web/legou.html")
        webDialogClose.setOnClickListener {
            dismiss()
        }
    }

}