package com.customer.component.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.Gravity
import android.view.ViewGroup
import com.fh.module_base_resouce.R
import com.lib.basiclib.utils.ViewUtils
import kotlinx.android.synthetic.main.dialog_login_expired.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/11/12
 * @ Describe 扫码登录过期
 *
 */
class DialogLoginExpired  (context: Context) : Dialog(context) {



    init {
        setContentView(R.layout.dialog_login_expired)
        window?.setWindowAnimations(R.style.BaseDialogAnim)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setGravity(Gravity.CENTER or Gravity.CENTER)
        val lp = window?.attributes
        lp?.width = ViewUtils.dp2px(300) // 宽度
        lp?.height = ViewGroup.LayoutParams.WRAP_CONTENT  // 高度
//      lp.alpha = 0.7f // 透明度
        window?.attributes = lp

        val spannableString = SpannableString("二维码已过期,请刷新二维码")
        spannableString.setSpan(
            ForegroundColorSpan(Color.parseColor("#ff513e")),
            8,
            10,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        tvTop.text = spannableString
        vipClose.setOnClickListener {
            dismiss()
        }
    }


}