package com.customer.component.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import com.fh.module_base_resouce.R
import com.lib.basiclib.utils.ViewUtils
import com.zyyoona7.picker.OptionsPickerView
import kotlinx.android.synthetic.main.old_dialog_lottery_select.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/9/1
 * @ Describe
 *
 */
class BottomLotterySelectDialog(context: Context, val title: ArrayList<String>) : Dialog(context) {

    var tvLotteryWheelSure:TextView?=null
    var lotteryPickerView: OptionsPickerView<String>?=null
    private var getSelectTextListener: ((it: Int) -> Unit)? = null

    fun setLis(getTextListener: ((it: Int) -> Unit)) {
        getSelectTextListener = getTextListener
    }
    init {
        setContentView(R.layout.old_dialog_lottery_select)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setGravity(Gravity.BOTTOM)
        tvLotteryWheelSure = findViewById(R.id.tvLotteryWheelSure)
        lotteryPickerView = findViewById(R.id.lotteryPickerView)
        val lp = window?.attributes
        lp?.width = ViewGroup.LayoutParams.MATCH_PARENT // 宽度
        lp?.dimAmount = 0.3f
        window?.attributes = lp
        val mOptionsPickerView = findViewById<OptionsPickerView<String>>(R.id.lotteryPickerView)
        mOptionsPickerView.setData(title)
        mOptionsPickerView.setTextSize(18f, true)
        mOptionsPickerView.setShowDivider(true)
        mOptionsPickerView.setVisibleItems(6)
        mOptionsPickerView.setDividerColor(ViewUtils.getColor(R.color.grey_dd))
        mOptionsPickerView.setLineSpacing(80f)
        mOptionsPickerView.setSelectedItemTextColor(ViewUtils.getColor(R.color.black))
        mOptionsPickerView.setNormalItemTextColor(ViewUtils.getColor(R.color.grey_e6))
        tvWheelCancel.setOnClickListener {
            dismiss()
        }
    }

}