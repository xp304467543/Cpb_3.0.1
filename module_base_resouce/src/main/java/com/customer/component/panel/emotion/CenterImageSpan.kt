package com.customer.component.panel.emotion

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.FontMetricsInt
import android.graphics.drawable.Drawable
import android.text.style.ImageSpan

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/21
 * @ Describe
 *
 */
class CenterImageSpan(drawable: Drawable) : ImageSpan(drawable) {
    /**
     * @param paint
     * @param text
     * @param start
     * @param end
     * @param fontMetricsInt
     * @return
     */
    override fun getSize(
        paint: Paint, text: CharSequence?, start: Int, end: Int,
        fontMetricsInt: FontMetricsInt?
    ): Int {
        val drawable = drawable
        val rect = drawable.bounds
        if (fontMetricsInt != null) {

            //获取绘制字体的度量
            val fmPaint = paint.fontMetricsInt
            val fontHeight = fmPaint.bottom - fmPaint.top
            val drHeight = rect.bottom - rect.top
            val top = drHeight / 2 - fontHeight / 4
            val bottom = drHeight / 2 + fontHeight / 4
            fontMetricsInt.ascent = -bottom
            fontMetricsInt.top = -bottom
            fontMetricsInt.bottom = top
            fontMetricsInt.descent = top
        }
        return rect.right
    }

    /**
     * @param canvas
     * @param text
     * @param start
     * @param end
     * @param x      要绘制的image的左边框到textview左边框的距离。
     * @param top    替换行的最顶部位置
     * @param y      要替换的文字的基线坐标，即基线到textview上边框的距离
     * @param bottom 替换行的最底部位置。注意，textview中两行之间的行间距是属于上一行的，所以这里bottom是指行间隔的底部位置
     * @param paint
     */
    override fun draw(
        canvas: Canvas, text: CharSequence?, start: Int, end: Int,
        x: Float, top: Int, y: Int, bottom: Int, paint: Paint
    ) {
        val drawable = drawable
        val fm = paint.fontMetricsInt

        /**
         * y + fm.descent 字体的descent线 y坐标
         * y + fm.ascent 字体的ascent线 y坐标
         * drawble.getBounds().bottom 图片的高度
         */
        val transY =
            (1f / 2 * (y + fm.descent + y + fm.ascent - drawable.bounds.bottom)).toInt()
        canvas.save()
        canvas.translate(x, transY.toFloat())
        drawable.draw(canvas)
        canvas.restore()
    }
}