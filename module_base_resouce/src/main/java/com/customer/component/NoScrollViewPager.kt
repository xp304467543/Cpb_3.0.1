package com.customer.component

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

/**
 *
 * @ Author  QinTian
 * @ Date  12/8/20
 * @ Describe
 *
 */
class NoScrollViewPager : ViewPager {

    private var isScroll: Boolean = false

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun setScroll(scroll: Boolean) {
        isScroll = scroll
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return this.isScroll && super.onTouchEvent(event)
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return this.isScroll && super.onInterceptTouchEvent(event)
    }
}