package com.customer.component.dialog

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.text.Html
import android.view.Gravity
import com.fh.module_base_resouce.R
import kotlinx.android.synthetic.main.dialog_version.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/9/5
 * @ Describe
 *
 */
class DialogVersion(context: Context) : Dialog(context){

    init {
        window!!.setWindowAnimations(R.style.BaseDialogAnim)
        setContentView(R.layout.dialog_version)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window!!.setGravity(Gravity.CENTER or Gravity.CENTER)
        val lp = window!!.attributes
//      lp.alpha = 0.7f // 透明度
        window!!.attributes = lp
        setCanceledOnTouchOutside(false)
    }


    fun setContent(string: String) {
        upDateContent.text = Html.fromHtml(string)
    }

    fun setJum(url: String) {
        btUpDate.setOnClickListener {
            val intent = Intent()
            intent.action = "android.intent.action.VIEW"
            val contentUrl = Uri.parse(url)//此处填链接
            intent.data = contentUrl
            context.startActivity(intent)
        }
    }
}