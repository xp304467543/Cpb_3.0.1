package com.home.adapter

import android.content.Context
import com.customer.data.home.Game
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
class HomeGameRvAdapter(var context: Context?) : BaseRecyclerAdapter<Game>() {
    override fun getItemLayoutId(viewType: Int): Int {
        return R.layout.adapter_lotteryview
    }

    override fun bindData(holder: RecyclerViewHolder, position: Int, item: Game?) {
      context?.let { GlideUtil.loadImage(it,item?.img_url,holder.getImageView(R.id.imgLotteryType)) }
        holder.text(R.id.tvLotteryTypeName,item?.name)
    }
}