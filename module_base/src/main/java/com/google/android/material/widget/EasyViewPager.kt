package com.google.android.material.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.viewpager.widget.ViewPager

class EasyViewPager : ViewPager {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    override fun setCurrentItem(item: Int) {
        setCurrentItem(item, false)
    }


    override fun canScroll(v: View?, checkV: Boolean, dx: Int, x: Int, y: Int): Boolean {
        // 判断子View中是否有ViewPager，有的话拦截事件
        if (v != this && v is ViewPager) {
            return true
        }
        return super.canScroll(v, checkV, dx, x, y)
    }
}