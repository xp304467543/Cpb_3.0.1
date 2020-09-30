package com.customer.component.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.fh.module_base_resouce.R
import kotlinx.android.synthetic.main.dialog_video_preview.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/9/4
 * @ Describe
 *
 */
class DialogVideoPreview (context: Context) : Dialog(context, R.style.dialog){

    private var setIsLoginListener: ((str: Boolean) -> Unit)? = null
    fun setOnIsLoginListener(listener: (str: Boolean) -> Unit) {
        setIsLoginListener = listener
    }

    init {
        window?.setWindowAnimations(R.style.BaseDialogAnim)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(R.layout.dialog_video_preview)
        close.setOnClickListener { dismiss() }
        toPerView.setOnClickListener { setIsLoginListener?.invoke(true) }
        toLogin.setOnClickListener {setIsLoginListener?.invoke(false) }
    }
}