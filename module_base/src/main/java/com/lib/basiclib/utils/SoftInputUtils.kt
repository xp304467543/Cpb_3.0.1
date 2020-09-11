package com.lib.basiclib.utils

import android.app.Activity
import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager


/**
 * 软键盘相关工具类，键盘的弹出和隐藏
 */
object SoftInputUtils {

    private val mFilterViews = arrayListOf<View>()
    private var mTouchOutsideFunction: (() -> Unit)? = null

    /**
     * 是否隐藏输入框
     */
    fun isHideSoftInput(event: MotionEvent): Boolean {
        var hide = true
        for (view in mFilterViews) {
            val array = intArrayOf(0, 0)
            view.getLocationInWindow(array)
            val left = array[0]
            val top = array[1]
            val bottom = top + view.height
            val right = left + view.width
            if (event.x > left && event.x < right && event.y > top && event.y < bottom) {
                hide = false
                break
            }
        }
        return hide
    }

    /**
     * 设置软键盘展示或者不展示
     */
    fun setSoftInput(view: View?, isShow: Boolean) {
        view?.let {
            if (isShow) {
                showSoftInput(it)
            } else {
                hideSoftInput(it.context)
            }
        }
    }


    /**
     * 显示软键盘
     */
    fun showSoftInput(view: View?) {
        if (view == null || view.context == null) return
        try {
            val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            view.requestFocus()
            view.postDelayed({ imm.showSoftInput(view, InputMethodManager.SHOW_FORCED) }, 200)
        } catch (e: Exception) {
            LogUtils.e(e)
        }
    }

    /**
     * 隐藏软键盘
     */
    fun hideSoftInput(context: Context?) {
        if (context == null || context !is Activity) return
        try {
            val view = context.window.decorView
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        } catch (e: Exception) {
            LogUtils.e(e)
        }
    }

    /**
     * 添加过滤的View
     */
    fun addFilterView(vararg views: View) {
        for (view in views) {
            mFilterViews.add(view)
        }
    }

    fun getFilterView(): List<View> {
        return mFilterViews
    }

    fun clearFilterView() {
        mFilterViews.clear()
        mTouchOutsideFunction = null
    }

    /**
     * 激活触摸软键盘外部的监听
     */
    fun invokeOnTouchOutsideListener(context: Context) {
        SoftInputUtils.hideSoftInput(context)
        mTouchOutsideFunction?.invoke()
    }

    /**
     * 设置触摸软键盘外部的监听
     */
    fun setOnTouchOutsideListener(function: () -> Unit) {
        mTouchOutsideFunction = function
    }
}