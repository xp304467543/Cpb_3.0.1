package com.customer.component.dialog

import android.app.Dialog
import android.content.Context
import com.fh.module_base_resouce.R

/**
 *
 * @ Author  QinTian
 * @ Date  2020/9/4
 * @ Describe
 *
 */
class DialogCommentsSuccess (context: Context) : Dialog(context) {


    init {
        setContentView(R.layout.dialog_comment_success)
        window?.setBackgroundDrawableResource(R.color.transparent)
//        val lp = window!!.attributes
//        lp.width = ViewUtils.dp2px(210)
//        lp.height = ViewUtils.dp2px(194)
    }  }