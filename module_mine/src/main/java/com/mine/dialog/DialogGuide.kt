package com.mine.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.ViewGroup
import com.mine.R

/**
 *
 * @ Author  QinTian
 * @ Date  2020/10/4
 * @ Describe
 *
 */
class DialogGuide(context: Context) : Dialog(context) {

    init {
        setContentView(R.layout.guide_mine)
        window?.setWindowAnimations(R.style.BaseDialogAnim)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setGravity(Gravity.TOP or Gravity.TOP)
        val lp = window?.attributes
        lp?.width = ViewGroup.LayoutParams.MATCH_PARENT // 宽度
        lp?.height = ViewGroup.LayoutParams.MATCH_PARENT  // 高度
//        lp?.alpha = 0.3f // 透明度
        window?.attributes = lp
    }
}