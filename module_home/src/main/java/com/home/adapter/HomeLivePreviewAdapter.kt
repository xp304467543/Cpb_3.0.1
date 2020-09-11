package com.home.adapter

import android.app.Activity
import android.content.Context
import com.customer.ApiRouter
import com.customer.component.WaveView
import com.customer.component.dialog.GlobalDialog
import com.customer.data.UserInfoSp
import com.customer.data.home.HomeApi
import com.glide.GlideUtil
import com.home.R
import com.customer.data.home.HomeLivePreResponse
import com.lib.basiclib.base.recycle.BaseRecyclerAdapter
import com.lib.basiclib.base.recycle.RecyclerViewHolder
import com.lib.basiclib.base.round.RoundTextView
import com.lib.basiclib.utils.AppUtils.getDrawable
import com.lib.basiclib.utils.FastClickUtil
import com.lib.basiclib.utils.TimeUtils
import com.lib.basiclib.utils.ToastUtils
import com.lib.basiclib.utils.ViewUtils
import com.lib.basiclib.utils.ViewUtils.setGone
import com.xiaojinzi.component.impl.Router

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/14
 * @ Describe
 *
 */
class HomeLivePreviewAdapter(var context: Context?) : BaseRecyclerAdapter<HomeLivePreResponse>() {

    override fun getItemLayoutId(viewType: Int) = R.layout.adapter_home_live_preview

    override fun bindData(holder: RecyclerViewHolder, position: Int, data: HomeLivePreResponse?) {
        when (position) {
            0 -> {
                if (data?.livestatus == "1") holder.getImageView(R.id.imgChampion).background =
                    getDrawable(R.mipmap.ic_no_1)
            }
            1 -> {
                if (data?.livestatus == "1") holder.getImageView(R.id.imgChampion).background =
                    getDrawable(R.mipmap.ic_no_2)
            }
            2 -> {
                if (data?.livestatus == "1") holder.getImageView(R.id.imgChampion).background =
                    getDrawable(R.mipmap.ic_no_3)
            }
            else -> setGone(holder.getImageView(R.id.imgChampion))
        }

        holder.text(R.id.tvLiveNoticeName, data?.nickname)
        holder.text(R.id.tvLiveNoticeGameName, data?.name)
        holder.text(
            R.id.tvLiveNoticeDate,
            TimeUtils.longToDateStringTime(
                data?.starttime?.toLong() ?: 0
            ) + "~" + TimeUtils.longToDateStringTime(data?.endtime?.toLong() ?: 0)
        )
        context?.let { GlideUtil.loadCircleImage(it,data?.avatar, holder.getImageView(R.id.ivLiveNoticeLogo),true) }
        val attention = holder.findViewById<RoundTextView>(R.id.tvLiveNoticeAttention)
        if (data?.isFollow == true) {
            attention.text = "已关注"
            attention.delegate.backgroundColor = ViewUtils.getColor(R.color.color_f5f5f5)
            attention.setTextColor(ViewUtils.getColor(R.color.color_DDDDDD))
        } else {
            attention.text = "关注"
            attention.delegate.backgroundColor = ViewUtils.getColor(R.color.color_FF513E)
            attention.setTextColor(ViewUtils.getColor(R.color.white))
        }
        if (data?.livestatus == "1") {
            holder.findViewById<WaveView>(R.id.circleWave).setInitialRadius(50f)
            holder.findViewById<WaveView>(R.id.circleWave).start()
        }
        holder.click(R.id.ivLiveNoticeLogo) {
            if(!FastClickUtil.isFastClick()){
                    if (data?.livestatus == "1") {
                        Router.withApi(ApiRouter::class.java).toLive(
                            data.aid ?: "-1",
                            data.lottery_id ?: "1",
                            data.nickname ?: "",
                            data.livestatus ?: "0",
                            "50",
                            "-1",
                            data.name ?: "",
                            data.avatar ?: ""
                        )
                    }else Router.withApi(ApiRouter::class.java).toAnchorPage(data?.aid?: "-1")
            }
        }
        holder.click(R.id.tvLiveNoticeAttention) {
            if(!FastClickUtil.isFastClick()){
                if (!UserInfoSp.getIsLogin()) {
                    GlobalDialog.notLogged(context as Activity)
                    return@click
                }
                HomeApi.attentionAnchorOrUser(data?.aid.toString(),""){
                    onSuccess {
                        data?.isFollow = it.isFollow
                        notifyItemChanged(position)
                    }
                    onFailed { ToastUtils.showToast("关注失败") }
                }
            }
        }

    }
}