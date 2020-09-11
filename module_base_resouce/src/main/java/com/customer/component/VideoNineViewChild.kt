package com.customer.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.customer.data.video.ClassificationChild
import com.fh.module_base_resouce.R
import com.glide.GlideUtil
import com.lib.basiclib.utils.TimeUtils

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/30
 * @ Describe
 *
 */
class VideoNineViewChild :LinearLayout {
    private var mContext: Context? = null
    private var imgVideoCover:ImageView? = null
    private var tvWatchNum:TextView? = null
    private var tvTimeLong:TextView? = null
    private var tvBottom:TextView? = null
    private var data: ClassificationChild?=null

    constructor(context: Context) : super(context) {
        init(context)

    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)

    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)

    }

    private fun init(context: Context) {
        this.mContext = context
        LayoutInflater.from(context).inflate(R.layout.view_video_nine_child, this)
        imgVideoCover =findViewById(R.id.imgVideoCover)
        tvWatchNum =findViewById(R.id.tvWatchNum)
        tvTimeLong =findViewById(R.id.tvTimeLong)
        tvBottom =findViewById(R.id.tvBottom)
    }

    fun initSetData(classificationChild: ClassificationChild?){
        data = classificationChild
        imgVideoCover?.let { GlideUtil.loadImage(context,data?.cover, it) }
        tvWatchNum?.text = data?.reads?:"0"
        tvTimeLong?.text = TimeUtils.secondToTime((data?.play_time?:"0").toLong())
        tvBottom?.text = data?.title?:"未知"
    }

    fun getNineItemData(): ClassificationChild? {
        return data
    }


}