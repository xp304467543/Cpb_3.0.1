package com.customer.component.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.fh.module_base_resouce.R
import com.lib.basiclib.utils.ViewUtils
import kotlinx.android.synthetic.main.dialog_phone_contact.*

/**
 *
 * @ Author  QinTian
 * @ Date  1/14/21
 * @ Describe 电话回访
 *
 */
class DialogPhoneContact (context: Context) : Dialog(context) {


    init {
        window?.setWindowAnimations(R.style.BaseDialogAnim)
        setContentView(R.layout.dialog_phone_contact)
        val lp = window?.attributes
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        lp?.height = ViewUtils.dp2px(480)
        window?.attributes = lp
        setCanceledOnTouchOutside(false)
        initEvent()
    }
    fun initEvent(){
        imgDialogClose?.setOnClickListener {
            dismiss()
        }
    }

}
