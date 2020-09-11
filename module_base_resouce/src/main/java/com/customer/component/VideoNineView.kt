package com.customer.component

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.customer.data.video.ClassificationChild
import com.fh.module_base_resouce.R
import com.lib.basiclib.base.round.RoundTextView
import com.lib.basiclib.utils.ViewUtils

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/30
 * @ Describe
 *
 */
class VideoNineView : LinearLayout {
    private var mContext: Context? = null
    private var tvChange: TextView? = null
    private var tvVideoTitle: TextView? = null
    var tvMoreVideo: TextView? = null
    private var viewBlank:View?=null
    private var nine1: VideoNineViewChild? = null
    private var nine2: VideoNineViewChild? = null
    private var nine3: VideoNineViewChild? = null
    private var nine4: VideoNineViewChild? = null
    private var nine5: VideoNineViewChild? = null
    private var nine6: VideoNineViewChild? = null
    private var tvMoveLine:RoundTextView?=null
    private var clickData: List<ClassificationChild>? = null
    private var mNineChangeClickListener: (() -> Unit)? = null
    private var mNineItemClickListener: ((pos: Int) -> Unit)? = null

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
        //加载布局文件，与setContentView()效果一样
        LayoutInflater.from(context).inflate(R.layout.view_video_nine, this)
        nine1 = findViewById(R.id.nine1)
        nine2 = findViewById(R.id.nine2)
        nine3 = findViewById(R.id.nine3)
        nine4 = findViewById(R.id.nine4)
        nine5 = findViewById(R.id.nine5)
        nine6 = findViewById(R.id.nine6)
        tvChange = findViewById(R.id.tvChange)
        tvVideoTitle = findViewById(R.id.tvVideoTitle)
        tvMoreVideo = findViewById(R.id.tvMoreVideo)
        tvMoveLine = findViewById(R.id.tvMoveLine)
        viewBlank = findViewById(R.id.viewBlank)
        initViewClick()
    }


    fun setViewData(title: String, data: List<ClassificationChild>) {
        if (!data.isNullOrEmpty()) {
            clickData = data
            when (data.size) {
                1 -> {
                    nine1?.initSetData(data[0])
                }
                2 -> {
                    nine1?.initSetData(data[0])
                    nine2?.initSetData(data[1])
                }
                3 -> {
                    nine1?.initSetData(data[0])
                    nine2?.initSetData(data[1])
                    nine3?.initSetData(data[2])
                }
                4 -> {
                    nine1?.initSetData(data[0])
                    nine2?.initSetData(data[1])
                    nine3?.initSetData(data[2])
                    nine4?.initSetData(data[3])
                }
                5 -> {
                    nine1?.initSetData(data[0])
                    nine2?.initSetData(data[1])
                    nine3?.initSetData(data[2])
                    nine4?.initSetData(data[3])
                    nine5?.initSetData(data[4])
                }
                else -> {
                    nine1?.initSetData(data[0])
                    nine2?.initSetData(data[1])
                    nine3?.initSetData(data[2])
                    nine4?.initSetData(data[3])
                    nine5?.initSetData(data[4])
                    nine6?.initSetData(data[5])
                }
            }
            tvVideoTitle?.text = title
        }
    }

    fun showOrHideBlank(boolean: Boolean){
        if (boolean) ViewUtils.setVisible(viewBlank) else ViewUtils.setGone(viewBlank)
    }


    fun upDateViewData(pos: Int, data: ClassificationChild) {
        when (pos) {
            1 -> nine1?.initSetData(data)
            2 -> nine2?.initSetData(data)
            3 -> nine3?.initSetData(data)
            4 -> nine4?.initSetData(data)
            5 -> nine5?.initSetData(data)
            6 -> nine6?.initSetData(data)
        }
    }

    fun setLineColor(color: Int){
        tvMoveLine?.delegate?.backgroundColor = ViewUtils.getColor(color)
    }


    private fun initViewClick() {
        tvChange?.setOnClickListener {
            mNineChangeClickListener?.invoke()
        }
        nine1?.setOnClickListener {
            mNineItemClickListener?.invoke(0)
        }
        nine2?.setOnClickListener {
            mNineItemClickListener?.invoke(1)
        }
        nine3?.setOnClickListener {
            mNineItemClickListener?.invoke(2)
        }
        nine4?.setOnClickListener {
            mNineItemClickListener?.invoke(3)
        }
        nine5?.setOnClickListener {
            mNineItemClickListener?.invoke(4)
        }
        nine6?.setOnClickListener {
            mNineItemClickListener?.invoke(5)
        }

    }

    fun setNineItemClickListener(listener: (pos:Int) -> Unit) {
        mNineItemClickListener = listener
    }

    //换一批
    fun setNineChangeListener(listener: () -> Unit) {
        mNineChangeClickListener = listener
    }
}
