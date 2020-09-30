package com.mine.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.ViewGroup
import com.lib.basiclib.utils.ToastUtils
import com.lib.basiclib.utils.ViewUtils
import com.mine.R
import kotlinx.android.synthetic.main.dialog_report.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/24
 * @ Describe
 *
 */
class DialogReport(context: Context) : Dialog(context) {



    init {
        setContentView(R.layout.dialog_report)
        window?.setWindowAnimations(R.style.BaseDialogAnim)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setGravity(Gravity.CENTER or Gravity.CENTER)
        val lp = window?.attributes
        lp?.width = ViewUtils.dp2px(316) // 宽度
        lp?.height = ViewGroup.LayoutParams.WRAP_CONTENT  // 高度
//      lp.alpha = 0.7f // 透明度
        window?.attributes = lp
        initEvent()
    }

    private fun initEvent() {
        tvReportConfirm.setOnClickListener {
            if (et_qq.text.toString().isNotEmpty()) {
                mListener?.invoke(et_qq.text.toString())
                dismiss()
            } else ToastUtils.showToast("请输入正确QQ号")

        }
    }

    private var mListener: ((str: String) -> Unit)? = null
    fun setConfirmClickListener(listener: (str: String) -> Unit) {
        mListener = listener
    }
}