package com.customer.component.dialog

import android.app.Dialog
import android.content.Context
import com.customer.ApiRouter
import com.fh.module_base_resouce.R
import com.lib.basiclib.utils.FastClickUtil
import com.xiaojinzi.component.impl.Router
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
        window?.setWindowAnimations(R.style.BaseDialogAnim)
        setContentView(R.layout.dialog_register_success)
        window?.setBackgroundDrawableResource(R.color.transparent)
        setCanceledOnTouchOutside(false)
        setCancelable(false)
        tvRegisterKnow.setOnClickListener {
            dismiss()
        }
        tvRegisterSetInfo.setOnClickListener {
            if (!FastClickUtil.isFastClick()){
                Router.withApi(ApiRouter::class.java).toMyPage()
                dismiss()
            }
        }
    }
}