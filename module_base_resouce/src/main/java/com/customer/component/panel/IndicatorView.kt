package com.customer.component.panel
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.customer.adapter.TabThem
import com.fh.basemodle.R
import com.lib.basiclib.utils.ViewUtils

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/12
 * @ Describe
 *
 */
class IndicatorView : View {
    // 指示器的类型
    private val typeCircle = 1
    private val typeRect = 2

    // 默认的颜色和选中的颜色
    private var mNormalColor = ViewUtils.getColor(R.color.color_EBF0F6)
    private var mSelectedColor = TabThem.getTabSelect()

    // 每一项的高度和宽度
    private var mHeight = 10f
    private var mWidth = 10f

    // 每一项的间隔
    private var mSpace = 0f

    // 一共有多少个
    var mItemCount = 1
        set(value) {
            if (value < 1) return
            field = value
            requestLayout()
        }

    private var mType = typeCircle

    private val mNormalPaint = Paint()
    private val mSelectedPaint = Paint()

    private val mRectF = RectF()

    private var mCornerRadius = 0f

    var mCurSelect = 0
        set(value) {
            if (value < 0 || value >= mItemCount) return
            field = value
            invalidate()
        }

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        val arr = context?.obtainStyledAttributes(attrs, R.styleable.IndicatorView, defStyleAttr, 0)
        arr?.let {
            mNormalColor = it.getColor(R.styleable.IndicatorView_indicatorNormalColor, mNormalColor)
            mSelectedColor = it.getColor(R.styleable.IndicatorView_indicatorSelectedColor, mSelectedColor)

            mHeight = it.getDimension(R.styleable.IndicatorView_indicatorItemHeight, mHeight)
            mWidth = it.getDimension(R.styleable.IndicatorView_indicatorItemWidth, mWidth)

            mSpace = it.getDimension(R.styleable.IndicatorView_indicatorItemSpace, mSpace)

            mItemCount = it.getInt(R.styleable.IndicatorView_indicatorItemCount, mItemCount)

            mType = it.getInt(R.styleable.IndicatorView_indicatorItemType, mType)

            mCornerRadius = it.getDimension(R.styleable.IndicatorView_indicatorCornerRadius, mCornerRadius)
        }
        arr?.recycle()

        init()
    }

    private fun init() {
        mNormalPaint.isAntiAlias = true
        mNormalPaint.color = mNormalColor

        mSelectedPaint.isAntiAlias = true
        mSelectedPaint.color = mSelectedColor

    }

    fun setSelectThem(color: Int){
        mSelectedPaint.color = color
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val widthSpecMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSpecSize = MeasureSpec.getSize(widthMeasureSpec)

        // 设置wrap_content的默认高度和宽度
        if (widthSpecMode == MeasureSpec.AT_MOST) {
            val width = mWidth * mItemCount + (mItemCount - 1) * mSpace
            setMeasuredDimension(
                width.toInt() + paddingLeft + paddingRight,
                mHeight.toInt() + paddingTop + paddingBottom
            )
        } else {
            setMeasuredDimension(
                widthSpecSize + paddingLeft + paddingRight,
                mHeight.toInt() + paddingTop + paddingBottom
            )
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        var left = width.toFloat()
        var top = height.toFloat()
        left = (left - (mItemCount * mWidth + (mItemCount - 1) * mSpace)) / 2f
        top = (top - mHeight) / 2f

        when (mType) {
            typeCircle -> {
                drawRoundSate(canvas, top, left)
            }
            else -> {
                drawRectSate(canvas, top, left)
            }
        }
    }

    private fun drawRoundSate(canvas: Canvas?, top: Float, left: Float) {
        val r = mWidth / 2
        val cy = top + r
        var cx = left + r

        for (i in 0 until mItemCount) {
            if (i == mCurSelect) {
                canvas?.drawCircle(cx, cy, r, mSelectedPaint)
            } else {
                canvas?.drawCircle(cx, cy, r, mNormalPaint)
            }
            cx += mWidth + mSpace
        }
    }

    private fun drawRectSate(canvas: Canvas?, top: Float, left: Float) {
        var l = left
        mRectF.left = l
        mRectF.top = top
        mRectF.right = l + mWidth
        mRectF.bottom = top + mHeight
        for (i in 0 until mItemCount) {
            if (i == mCurSelect) { // 当前选中的
                canvas?.drawRoundRect(mRectF, mCornerRadius, mCornerRadius, mSelectedPaint)
            } else {
                canvas?.drawRoundRect(mRectF, mCornerRadius, mCornerRadius, mNormalPaint)
            }
            l += mWidth + mSpace
            mRectF.left = l
            mRectF.right = l + mWidth
        }
    }
}