package com.home.adapter

import android.graphics.Color
import com.customer.ApiRouter
import com.glide.GlideUtil
import com.home.R
import com.customer.data.home.HomeNewsResponse
import com.lib.basiclib.base.recycle.BaseRecyclerAdapter
import com.lib.basiclib.base.recycle.RecyclerViewHolder
import com.lib.basiclib.base.round.RoundTextView
import com.lib.basiclib.utils.FastClickUtil
import com.lib.basiclib.utils.ViewUtils
import com.xiaojinzi.component.impl.Router

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/14
 * @ Describe
 *
 */
class HomeNewsAdapter : BaseRecyclerAdapter<HomeNewsResponse>() {

    override fun getItemLayoutId(viewType: Int) = R.layout.adapter_home_news

    override fun bindData(holder: RecyclerViewHolder, position: Int, data: HomeNewsResponse?) {
        holder.text(R.id.tvTime, data?.timegap)
        holder.text(R.id.tvNewsTitle, data?.title)
        if (data?.cover_img != null && data.cover_img?.isNotEmpty()!!) GlideUtil.loadImage(
            data.cover_img!![0],
            holder.getImageView(R.id.imgNews)
        )
        if (data?.tag == "1") {
            holder.getImageView(R.id.imgTagView).background = ViewUtils.getDrawable(R.mipmap.ic_new)
        } else {
            holder.getImageView(R.id.imgTagView).background = ViewUtils.getDrawable(R.mipmap.ic_hot)
        }
        getTagBg(data?.type.toString(), holder.findViewById(R.id.imgTag), data?.type_txt.toString())

        holder.itemView.setOnClickListener {
            if (!FastClickUtil.isFastClick() && data?.info_id != null && data.info_id != "-1") {
                Router.withApi(ApiRouter::class.java).toGlobalWeb("", true, data.info_id ?: "")
            }
        }
    }

    private fun getTagBg(type: String, view: RoundTextView, text: String) {
        when (type) {
            "1" -> {
                view.delegate.backgroundColor = Color.parseColor("#EDE5F9")
                view.setTextColor(Color.parseColor("#D444F3"))
            }
            "2" -> {
                view.delegate.backgroundColor = Color.parseColor("#FFF9E8")
                view.setTextColor(Color.parseColor("#FD8208"))
            }
            "3" -> {
                view.delegate.backgroundColor = Color.parseColor("#F5F7FA")
                view.setTextColor(Color.parseColor("#79818C"))
            }
            "4" -> {
                view.delegate.backgroundColor = Color.parseColor("#FFECE8")
                view.setTextColor(Color.parseColor("#FF513E"))
            }
        }
        view.text = text
    }
}