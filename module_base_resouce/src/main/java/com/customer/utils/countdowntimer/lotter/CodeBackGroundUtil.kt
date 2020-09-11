package com.customer.utils.countdowntimer.lotter

import android.content.Context
import android.graphics.drawable.Drawable
import com.fh.module_base_resouce.R
import com.lib.basiclib.utils.ViewUtils

/**
 *
 * @ Author  QinTian
 * @ Date  2020/1/2- 11:24
 * @ Describe
 *
 */
object CodeBackGroundUtil {

    // ===== 根据号码设置背景色 =====
    fun setBackGroundColor(context: Context, code: String, lotteryId: String): Drawable? {
        if (lotteryId != "8") return when (code) {
            "1" -> ViewUtils.getDrawable(R.drawable.code_1)
            "2" ->  ViewUtils.getDrawable(R.drawable.code_2)
            "3" ->  ViewUtils.getDrawable(R.drawable.code_3)
            "4" ->  ViewUtils.getDrawable(R.drawable.code_4)
            "5" ->  ViewUtils.getDrawable(R.drawable.code_5)
            "6" ->  ViewUtils.getDrawable(R.drawable.code_6)
            "7" ->  ViewUtils.getDrawable(R.drawable.code_7)
            "8" ->  ViewUtils.getDrawable(R.drawable.code_8)
            "9" ->  ViewUtils.getDrawable(R.drawable.code_9)
            "10" ->  ViewUtils.getDrawable(R.drawable.code_10)
            else ->  ViewUtils.getDrawable(R.drawable.code_3)
        } else {
            return when (code) {
                "1" ->  ViewUtils.getDrawable(R.drawable.xcode_red)
                "2" ->  ViewUtils.getDrawable(R.drawable.xcode_red)
                "3" ->  ViewUtils.getDrawable(R.drawable.xcode_blue)
                "4" ->  ViewUtils.getDrawable(R.drawable.xcode_blue)
                "5" ->  ViewUtils.getDrawable(R.drawable.xcode_green)
                "6" ->  ViewUtils.getDrawable(R.drawable.xcode_green)
                "7" ->  ViewUtils.getDrawable(R.drawable.xcode_red)
                "8" ->  ViewUtils.getDrawable(R.drawable.xcode_red)
                "9" ->  ViewUtils.getDrawable(R.drawable.xcode_blue)
                "10" ->  ViewUtils.getDrawable(R.drawable.xcode_blue)

                "11" ->  ViewUtils.getDrawable(R.drawable.xcode_green)
                "12" ->  ViewUtils.getDrawable(R.drawable.xcode_red)
                "13" ->  ViewUtils.getDrawable(R.drawable.xcode_red)
                "14" ->  ViewUtils.getDrawable(R.drawable.xcode_blue)
                "15" ->  ViewUtils.getDrawable(R.drawable.xcode_blue)
                "16" ->  ViewUtils.getDrawable(R.drawable.xcode_green)
                "17" ->  ViewUtils.getDrawable(R.drawable.xcode_green)
                "18" ->  ViewUtils.getDrawable(R.drawable.xcode_red)
                "19" ->  ViewUtils.getDrawable(R.drawable.xcode_red)
                "20" ->  ViewUtils.getDrawable(R.drawable.xcode_blue)

                "21" ->  ViewUtils.getDrawable(R.drawable.xcode_green)
                "22" ->  ViewUtils.getDrawable(R.drawable.xcode_green)
                "23" ->  ViewUtils.getDrawable(R.drawable.xcode_red)
                "24" ->  ViewUtils.getDrawable(R.drawable.xcode_red)
                "25" ->  ViewUtils.getDrawable(R.drawable.xcode_blue)
                "26" ->  ViewUtils.getDrawable(R.drawable.xcode_blue)
                "27" ->  ViewUtils.getDrawable(R.drawable.xcode_green)
                "28" ->  ViewUtils.getDrawable(R.drawable.xcode_green)
                "29" ->  ViewUtils.getDrawable(R.drawable.xcode_red)
                "30" ->  ViewUtils.getDrawable(R.drawable.xcode_red)

                "31" ->  ViewUtils.getDrawable(R.drawable.xcode_blue)
                "32" ->  ViewUtils.getDrawable(R.drawable.xcode_green)
                "33" ->  ViewUtils.getDrawable(R.drawable.xcode_green)
                "34" ->  ViewUtils.getDrawable(R.drawable.xcode_red)
                "35" ->  ViewUtils.getDrawable(R.drawable.xcode_red)
                "36" ->  ViewUtils.getDrawable(R.drawable.xcode_blue)
                "37" ->  ViewUtils.getDrawable(R.drawable.xcode_blue)
                "38" ->  ViewUtils.getDrawable(R.drawable.xcode_green)
                "39" ->  ViewUtils.getDrawable(R.drawable.xcode_green)
                "40" ->  ViewUtils.getDrawable(R.drawable.xcode_red)

                "41" ->  ViewUtils.getDrawable(R.drawable.xcode_blue)
                "42" ->  ViewUtils.getDrawable(R.drawable.xcode_blue)
                "43" ->  ViewUtils.getDrawable(R.drawable.xcode_green)
                "44" ->  ViewUtils.getDrawable(R.drawable.xcode_green)
                "45" ->  ViewUtils.getDrawable(R.drawable.xcode_red)
                "46" ->  ViewUtils.getDrawable(R.drawable.xcode_red)
                "47" ->  ViewUtils.getDrawable(R.drawable.xcode_blue)
                "48" ->  ViewUtils.getDrawable(R.drawable.xcode_blue)
                "49" ->  ViewUtils.getDrawable(R.drawable.xcode_green)
                else ->  ViewUtils.getDrawable(R.drawable.xcode_red)
            }
        }
    }


}