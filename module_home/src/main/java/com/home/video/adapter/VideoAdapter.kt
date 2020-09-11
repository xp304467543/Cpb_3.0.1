package com.home.video.adapter

import android.content.Context
import com.customer.ApiRouter
import com.customer.data.video.VideoMoreChild
import com.glide.GlideUtil
import com.home.R
import com.lib.basiclib.base.recycle.BaseRecyclerAdapter
import com.lib.basiclib.base.recycle.RecyclerViewHolder
import com.lib.basiclib.utils.FastClickUtil
import com.lib.basiclib.utils.TimeUtils
import com.xiaojinzi.component.impl.Router

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/30
 * @ Describe
 *
 */
class VideoAdapter(val context: Context) : BaseRecyclerAdapter<VideoMoreChild>() {
    override fun getItemLayoutId(viewType: Int) = R.layout.adapter_video_child_item

    override fun bindData(holder: RecyclerViewHolder, position: Int, data: VideoMoreChild?) {
        holder.text(R.id.tvWatchNum,data?.reads)
        holder.text(R.id.tvTimeLong,TimeUtils.secondToTime((data?.play_time?:"0").toLong()))
        holder.text(R.id.tvBottom,data?.title)
        GlideUtil.loadImage(context,data?.cover,holder.getImageView(R.id.imgVideoCover))
        holder.itemView.setOnClickListener {
            if (!FastClickUtil.isFastClick()) {
                Router.withApi(ApiRouter::class.java).toVideoPlay(
                    data?.id ?: -1, data?.title ?: "未知"
                )
            }
        }
    }
}