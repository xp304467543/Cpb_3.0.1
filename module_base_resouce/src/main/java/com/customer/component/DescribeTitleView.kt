package com.customer.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.fh.module_base_resouce.R
import com.lib.basiclib.base.round.RoundTextView
import com.lib.basiclib.utils.ViewUtils
import kotlinx.android.synthetic.main.view_describe_title.view.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/14
 * @ Describe 分区的title
 *
 */
class DescribeTitleView : LinearLayout {

    private var mContext: Context? = null
    private var tvBottomLine: RoundTextView? = null
    private var tvDesName: AppCompatTextView? = null
    private var lineView:RoundTextView?=null
    private var imgIcon: AppCompatImageView?=null

    constructor(context: Context) : super(context) {
        init(context)

    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)

    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context)

    }

    private fun init(context: Context) {
        this.mContext = context
        LayoutInflater.from(context).inflate(R.layout.view_describe_title, this)
        tvBottomLine = findViewById(R.id.tvBottomLine)
        tvDesName = findViewById(R.id.tvDesName)
        lineView = findViewById(R.id.lineView)
        imgIcon = findViewById(R.id.imgIcon)
    }


    fun setDesText(title: String?, textColor: Int? = null, bottomLineColor:Int? = null,image: Int? = null,isLineView:Boolean = false) {
        if (isLineView){
            ViewUtils.setVisible(lineView)
            ViewUtils.setGone(tvBottomLine)
        }
        if (title!=null) tvDesName?.text = title
        if (textColor != null && textColor !=-1)tvDesName?.setTextColor(ViewUtils.getColor(textColor))
        if (image != null && image !=-1)tvDesName?.setCompoundDrawablesWithIntrinsicBounds(ViewUtils.getDrawable(image), null, null, null)
        if (bottomLineColor != null && bottomLineColor !=-1){
            ViewUtils.setVisible(tvBottomLine)
            tvBottomLine?.delegate?.backgroundColor = ViewUtils.getColor(bottomLineColor)
        }else  ViewUtils.setGone(tvBottomLine)

    }

    fun setDesNew(isShowLine:Boolean = false,title:String,image: Int? = null){
        tvDesName?.text = title
        if (isShowLine){
            ViewUtils.setVisible(lineView)
            ViewUtils.setGone(imgIcon)
        }else{
            ViewUtils.setGone(lineView)
            ViewUtils.setVisible(imgIcon)
           if (image!=null) imgIcon?.setImageResource(image)
        }
    }
}