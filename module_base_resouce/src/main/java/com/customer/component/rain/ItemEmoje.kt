package com.customer.component.rain

import android.graphics.Bitmap

/**
 *
 * @ Author  QinTian
 * @ Date  2020/9/30
 * @ Describe
 *
 */
class ItemEmoje {
    //坐标
    var x: Int = 0
    var y: Int = 0
    // 横向偏移
    var offsetX: Int = 0
    //纵向偏移
    var offsetY: Int = 0
    //缩放
    var scale: Float = 0.toFloat()
    //图片资源
    var bitmap: Bitmap? = null
}