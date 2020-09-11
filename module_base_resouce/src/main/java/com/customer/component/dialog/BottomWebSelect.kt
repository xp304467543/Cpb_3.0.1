package com.customer.component.dialog

import android.content.Context
import android.graphics.Color
import android.view.View
import com.fh.module_base_resouce.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.dialog_bottom_web_select.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/5/24
 * @ Describe
 *
 */

class BottomWebSelect(context: Context) : BottomSheetDialog(context) {

    private var selectListener: ((it: Int) -> Unit)? = null

    init {
        setContentView(R.layout.dialog_bottom_web_select)
        val root = delegate?.findViewById<View>(R.id.design_bottom_sheet)
        val behavior = root?.let { BottomSheetBehavior.from(it) }
        behavior?.isHideable = false
        delegate?.findViewById<View>(R.id.design_bottom_sheet)?.setBackgroundColor(Color.TRANSPARENT)
        initEvent()
    }

    private fun initEvent() {
        imgWebClose.setOnClickListener { dismiss() }
        imgWebCp.setOnClickListener {
            selectListener?.invoke(1)
            dismiss()
        }
        imgWebQp.setOnClickListener {
            selectListener?.invoke(2)
            dismiss()
        }
    }


    fun setSelectListener(SelectListener: ((it: Int) -> Unit)) {
        selectListener = SelectListener
    }
}