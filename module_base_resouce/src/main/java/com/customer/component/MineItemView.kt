package com.customer.component

import android.content.Context
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
 * @ Date  1/12/21
 * @ Describe
 *
 */
class MineItemView : LinearLayout {

    private var imgIcon: ImageView? = null
    private var tvItemName: TextView? = null
    private var tvDian: RoundTextView? = null


    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        //init(context)要在retrieveAttributes(attrs)前调用
        //因为属性赋值，会直接赋值到控件上去。如:
        //调用label = ""时，相当于调用了label的set方法。
        init(context)
        //retrieveAttributes(attrs: AttributeSet)方法只接受非空参数
        attrs?.let { retrieveAttributes(attrs) }
    }

    fun init(context: Context) {
        val root = LayoutInflater.from(context).inflate(R.layout.mine_item, this)
        tvItemName = root?.findViewById(R.id.tvItemName)
        imgIcon = root?.findViewById(R.id.imgIcon)
        tvDian = root?.findViewById(R.id.tvDian)
    }

    private fun retrieveAttributes(attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.MineItem)
        val int = typedArray.getResourceId(R.styleable.MineItem_ItemIcon, 0)
        val text = typedArray.getString(R.styleable.MineItem_ItemName) ?: ""
        tvItemName?.text = text
        imgIcon?.setImageResource(int)
        typedArray.recycle()
    }

    fun showNewMessage(boolean: Boolean){
        if (boolean){
            ViewUtils.setVisible(tvDian)
        }else  ViewUtils.setGone(tvDian)
    }

    fun setBackRes(res:Int){
        imgIcon?.setImageResource(res)
    }
}