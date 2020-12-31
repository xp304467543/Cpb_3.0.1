package com.bet.game

import android.widget.LinearLayout
import android.widget.TextView
import com.bet.R
import com.lib.basiclib.utils.ViewUtils

/**
 *
 * @ Author  QinTian
 * @ Date  12/28/20
 * @ Describe
 *
 */
object GamePlay {

    //背景变化
    fun changeBg(
        isSelected: Boolean?,
        container: LinearLayout,
        tv1: TextView){
        if (isSelected == true) {
           container.background = ViewUtils.getDrawable(R.drawable.bet_select_background)
            tv1.background = ViewUtils.getDrawable(R.drawable.xcode_red)
        } else {
            container.background = ViewUtils.getDrawable(R.drawable.bet_normal_background)
            tv1.background = ViewUtils.getDrawable(R.drawable.xcode_blue)
        }
    }
}