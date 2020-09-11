package com.home.adapter

import android.content.Context
import com.glide.GlideUtil
import com.home.R
import com.customer.data.home.HomeTypeListResponse
import com.lib.basiclib.base.recycle.BaseRecyclerAdapter
import com.lib.basiclib.base.recycle.RecyclerViewHolder
import com.lib.basiclib.utils.ViewUtils

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/12
 * @ Describe
 *
 */
class HomeLotteryViewRvAdapter(var context: Context?) : BaseRecyclerAdapter<HomeTypeListResponse>() {
    override fun getItemLayoutId(viewType: Int): Int {
        return R.layout.adapter_lotteryview
    }

    override fun bindData(holder: RecyclerViewHolder, position: Int, item: HomeTypeListResponse?) {
        context?.let { GlideUtil.loadImage(it,item?.image,holder.getImageView(R.id.imgLotteryType)) }
        holder.text(R.id.tvLotteryTypeName,item?.name)
        if (item?.live_status == "1"){
            context?.let { GlideUtil.loadGifImage(it,R.drawable.ic_home_live_gif,holder.getImageView(R.id.imgLive)) }
        }else ViewUtils.setGone(holder.getImageView(R.id.imgLive))
    }
}