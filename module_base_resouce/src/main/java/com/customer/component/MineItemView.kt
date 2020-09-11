package com.customer.component

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.fh.module_base_resouce.R
import com.lib.basiclib.base.round.RoundTextView
import com.lib.basiclib.utils.ViewUtils

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/19
 * @ Describe
 *
 */
class MineItemView : LinearLayout {

    private var mContext: Context? = null
    private var imgIcon: ImageView? = null
    private var tvItemName: TextView? = null
    private var tvNewMsg: TextView? = null
    private var tvDian: RoundTextView? = null

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        val typedArray =
            context.theme.obtainStyledAttributes(attrs, R.styleable.MineItem, defStyleAttr, 0)
        val text = typedArray.getString(R.styleable.MineItem_ItemName) ?: ""
        val drawable = typedArray.getDrawable(R.styleable.MineItem_ItemIcon)
        init(context, text, drawable)
        typedArray.recycle()
    }

    private fun init(context: Context, text: String, drawable: Drawable?) {
        this.mContext = context
        //加载布局文件，与setContentView()效果一样
        LayoutInflater.from(context).inflate(R.layout.mine_item, this)
        imgIcon = findViewById(R.id.imgIcon)
        tvItemName = findViewById(R.id.tvItemName)
        tvNewMsg = findViewById(R.id.tvNewMsg)
        tvDian = findViewById(R.id.tvDian)
        imgIcon?.background = drawable
        tvItemName?.text = text
    }

    fun showNewMessage(isShow: Boolean) {
        if (isShow) {
            ViewUtils.setVisible(tvNewMsg)
            ViewUtils.setVisible(tvDian)
        } else {
            ViewUtils.setGone(tvNewMsg)
            ViewUtils.setGone(tvDian)
        }
    }

    fun setBackRes(res: Int) {
        imgIcon?.setBackgroundResource(res)
    }
}