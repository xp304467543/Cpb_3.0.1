package com.home.live.bet.old

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.ViewGroup
import com.home.R
import com.lib.basiclib.base.xui.widget.picker.widget.OptionsPickerView
import com.lib.basiclib.utils.ViewUtils
import kotlinx.android.synthetic.main.old_dialog_lottery_select.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/9/1
 * @ Describe
 *
 */
class BottomLotterySelectDialog(context: Context, val title: ArrayList<String>) : Dialog(context) {
    private var lotterySelectDialog: OptionsPickerView<String>? = null


    private var getSelectTextListener: ((it: Int) -> Unit)? = null

    fun setLis(getTextListener: ((it: Int) -> Unit)) {
        getSelectTextListener = getTextListener
    }
    init {
        setContentView(R.layout.old_dialog_lottery_select)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window!!.setGravity(Gravity.BOTTOM)
        val lp = window!!.attributes
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT // 宽度
        lp.dimAmount = 0.3f
        window!!.attributes = lp
        val mOptionsPickerView = findViewById<com.zyyoona7.picker.OptionsPickerView<String>>(R.id.lotteryPickerView)
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