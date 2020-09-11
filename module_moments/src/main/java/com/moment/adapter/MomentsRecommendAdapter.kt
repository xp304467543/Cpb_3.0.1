package com.moment.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.glide.GlideUtil
import com.lib.basiclib.base.recycle.BaseRecyclerAdapter
import com.lib.basiclib.base.recycle.RecyclerViewHolder
import com.lib.basiclib.utils.ToastUtils
import com.moment.R
import com.customer.data.moments.MomentsRecommend

/**
 * @ Author  QinTian
 * @ Date  2020/8/23
 * @ Describe 精品推荐
 */
class MomentsRecommendAdapter(val context: Context) : BaseRecyclerAdapter<MomentsRecommend>() {

    override fun getItemLayoutId(viewType: Int) = R.layout.adapter_moments_recommend

    override fun bindData(holder: RecyclerViewHolder, position: Int, data: MomentsRecommend?) {
        GlideUtil.loadGrayscaleImage(
            context,
            data?.icon,
            holder.getImageView(R.id.imgRecommend),
            10
        )
        holder.text(R.id.tvRecommendName, data?.title)
        holder.text(R.id.tvRecommendContent, data?.description)

        holder.click(R.id.tvDownLoad) {
            try {
                val uri = Uri.parse(data?.url)
                val intent = Intent(Intent.ACTION_VIEW, uri)
                context.startActivity(intent)
            } catch (e: Exception) {
                ToastUtils.showToast("地址解析异常")
            }

        }
    }
}