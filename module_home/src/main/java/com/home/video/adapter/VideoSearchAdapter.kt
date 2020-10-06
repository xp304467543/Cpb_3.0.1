package com.home.video.adapter

import android.content.Context
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView
import com.customer.ApiRouter
import com.customer.data.video.VideoMoreChild
import com.glide.GlideUtil
import com.home.R
import com.lib.basiclib.base.recycle.BaseRecyclerAdapter
import com.lib.basiclib.base.recycle.RecyclerViewHolder
import com.lib.basiclib.utils.FastClickUtil
import com.lib.basiclib.utils.LogUtils
import com.lib.basiclib.utils.ViewUtils
import com.xiaojinzi.component.impl.Router

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/30
 * @ Describe
 *
 */
class VideoSearchAdapter(val context: Context,val isInLivePage:Boolean = false) : BaseRecyclerAdapter<VideoMoreChild>() {
    override fun getItemLayoutId(viewType: Int) = R.layout.adapter_video_child_item_search

    override fun bindData(holder: RecyclerViewHolder, position: Int, data: VideoMoreChild?) {
        holder.text(R.id.title, data?.title)
        if ((data?.reads?.toInt())?:0 > 10000){
            holder.text(R.id.tvReds, (((data?.reads?.toInt())?:0)/10000).toString() +"万播放")
        }else holder.text(R.id.tvReds, data?.reads +"播放")
        LogUtils.e("=====>"+data?.tag)
        GlideUtil.loadImage(context, data?.cover, holder.getImageView(R.id.imgCover))
        val tag = data?.tag?.split(",")
        val layoutContainer = holder.findViewById<LinearLayout>(R.id.layoutContainer)
        if (tag != null) {
            layoutContainer.removeAllViews()
            val index = if (tag.size>2) 3 else 1
             repeat(index){
                val text = AppCompatTextView(context)
                text.textSize = 9F
                if (tag[it].length >5 ){
                    text.text = tag[it].substring(0,5)
                }else  text.text = tag[it]
                text.background = ViewUtils.getDrawable(R.drawable.button_grey_line_background)
                text.setPadding(10,5,10,5)
                layoutContainer.addView(text)
                val palms = text.layoutParams as LinearLayout.LayoutParams
                palms.marginStart = 10
                text.layoutParams = palms
            }

        }else layoutContainer.removeAllViews()
        if (position == mData.size-1){
          ViewUtils.setVisible(holder.findView(R.id.tvNoData))
        }else ViewUtils.setGone(holder.findView(R.id.tvNoData))

        if (!isInLivePage){
            holder.itemView.setOnClickListener {
                if (!FastClickUtil.isFastClick()) {
                    Router.withApi(ApiRouter::class.java).toVideoPlay(
                        data?.id ?: -1, data?.title ?: "未知"
                    )
                }
            }
        }
    }


}