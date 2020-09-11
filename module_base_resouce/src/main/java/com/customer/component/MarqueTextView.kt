package com.customer.component

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/27
 * @ Describe
 *
 */
class MarqueTextView : AppCompatTextView {

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context) : super(context) {}

    override fun isFocused(): Boolean {
        //就是把这里返回true即可
        return true
    }
}