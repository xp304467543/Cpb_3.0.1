package com.customer.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView
import com.fh.module_base_resouce.R
import com.lib.basiclib.base.round.RoundTextView
import com.lib.basiclib.utils.ViewUtils

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/14
 * @ Describe 分区的title
 *
 */
class DescribeTitleView : LinearLayout {

    private var mContext: Context? = null
    private var tvBottomLine: RoundTextView? = null
    private var tvDesName: AppCompatTextView? = null

    constructor(context: Context) : super(context) {
        init(context)

    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)

    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context)

    }

    private fun init(context: Context) {
        this.mContext = context
        LayoutInflater.from(context).inflate(R.layout.view_describe_title, this)
        tvBottomLine = findViewById(R.id.tvBottomLine)
        tvDesName = findViewById(R.id.tvDesName)
    }


    fun setDesText(title: String?, textColor: Int? = null, bottomLineColor:Int? = null,int: Int? = null) {
        if (title!=null) tvDesName?.text = title
        if (textColor != null && textColor !=-1)tvDesName?.setTextColor(ViewUtils.getColor(textColor))
        if (int != null && int !=-1)tvDesName?.setCompoundDrawablesWithIntrinsicBounds(ViewUtils.getDrawable(int), null, null, null)
        if (bottomLineColor != null && bottomLineColor !=-1){
            ViewUtils.setVisible(tvBottomLine)
            tvBottomLine?.delegate?.backgroundColor = ViewUtils.getColor(bottomLineColor)
        }else  ViewUtils.setGone(tvBottomLine)

    }
}