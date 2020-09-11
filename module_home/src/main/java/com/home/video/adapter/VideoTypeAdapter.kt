package com.home.video.adapter

import android.content.Context
import com.customer.ApiRouter
import com.customer.component.VideoNineView
import com.customer.component.dialog.DialogVideoPreview
import com.customer.data.UserInfoSp
import com.customer.data.video.MovieApi
import com.customer.data.video.MovieClassification
import com.glide.GlideUtil
import com.home.R
import com.lib.basiclib.base.recycle.BaseRecyclerAdapter
import com.lib.basiclib.base.recycle.RecyclerViewHolder
import com.lib.basiclib.utils.FastClickUtil
import com.lib.basiclib.utils.ToastUtils
import com.xiaojinzi.component.impl.Router

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/29
 * @ Describe
 *
 */
class VideoTypeAdapter(var context: Context, var urlTop: String, var urlCenter: String,var link:String,val currentName:String) :
    BaseRecyclerAdapter<MovieClassification>() {

    private val header: Int = 1
    private val content: Int = 2

    override fun getItemLayoutId(viewType: Int): Int {
        return if (viewType == header) {
            R.layout.adapter_fragment_video_type
        } else {
            R.layout.adapter_fragment_video_type2
        }
    }


    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            header
        } else content
    }


    override fun bindData(holder: RecyclerViewHolder, position: Int, data: MovieClassification?) {

        if (getItemViewType(position) == header) {
            val nineView = holder.findViewById<VideoNineView>(R.id.nineViews)
            GlideUtil.loadImage(context, urlTop, holder.getImageView(R.id.videoBanner))
            GlideUtil.loadImage(context, urlCenter, holder.getImageView(R.id.imgInfo))
            data?.list?.let { nineView.setViewData(data.name ?: "未知", it) }
            nineView.setNineChangeListener {
                if (!FastClickUtil.isFastClick()) getChangeData(
                    data,
                    position
                ) else ToastUtils.showToast("请勿重复点击")
            }
            nineView.tvMoreVideo?.setOnClickListener {
                if (!FastClickUtil.isFastClick())  Router.withApi(ApiRouter::class.java).toVideoMore(currentName,data?.name.toString())

            }
            nineView.setNineItemClickListener {
                val itemData = data?.list?.get(it)
                if (!FastClickUtil.isFastClick()) {
                    if(!UserInfoSp.getIsLogin()){
                        val dialog = DialogVideoPreview(context)
                        dialog.setOnIsLoginListener { isPre ->
                            if (isPre){
                                Router.withApi(ApiRouter::class.java).toVideoPlay(itemData?.id ?: -1, itemData?.title ?: "未知")
                            }else {
                                Router.withApi(ApiRouter::class.java).toLogin()
                            }
                            dialog.dismiss()
                        }
                        dialog.show()
                    }else  Router.withApi(ApiRouter::class.java).toVideoPlay(itemData?.id ?: -1, itemData?.title ?: "未知")

                }
            }
            when(UserInfoSp.getThemInt()){
                3 ->nineView.setLineColor(R.color.colorGreenPrimary)
                4 ->nineView.setLineColor(R.color.purple)
                else -> nineView.setLineColor(R.color.color_FF513E)
            }
            holder.click(R.id.imgInfo){
                if (!FastClickUtil.isFastClick()) Router.withApi(ApiRouter::class.java).toGlobalWeb(link)
            }
        } else if (getItemViewType(position) == content) {
            val nineView1 = holder.findViewById<VideoNineView>(R.id.nineViews2)
            data?.list?.let { nineView1.setViewData(data.name ?: "未知", it) }
            nineView1.setNineChangeListener {
                if (!FastClickUtil.isFastClick()) getChangeData(
                    data,
                    position
                ) else ToastUtils.showToast("请勿重复点击")
            }
            nineView1.tvMoreVideo?.setOnClickListener {
                if (!FastClickUtil.isFastClick())  Router.withApi(ApiRouter::class.java).toVideoMore(currentName,data?.name.toString())
            }
            nineView1.setNineItemClickListener {
                val itemData = data?.list?.get(it)
                if (!FastClickUtil.isFastClick()) {
                    if(!UserInfoSp.getIsLogin()){
                        val dialog = DialogVideoPreview(context)
                        dialog.setOnIsLoginListener { isPre ->
                            if (!isPre){
                                Router.withApi(ApiRouter::class.java).toVideoPlay(itemData?.id ?: -1, itemData?.title ?: "未知")
                            }else {
                                Router.withApi(ApiRouter::class.java).toLogin()
                            }
                            dialog.dismiss()
                        }
                        dialog.show()
                    }else  Router.withApi(ApiRouter::class.java).toVideoPlay(itemData?.id ?: -1, itemData?.title ?: "未知")
                }
            }
            when(UserInfoSp.getThemInt()){
                3 ->nineView1.setLineColor(R.color.colorGreenPrimary)
                4 ->nineView1.setLineColor(R.color.purple)
                else -> nineView1.setLineColor(R.color.color_FF513E)
            }

            if (position == getData().size-1) nineView1.showOrHideBlank(true) else nineView1.showOrHideBlank(false)
        }
    }


    private fun getChangeData(data: MovieClassification?, position: Int) {
        MovieApi.getVideoChange(data?.pid ?: 0, data?.id ?: 0, 6, true) {
            onSuccess {
                data?.list = it
                refresh(position, data)
            }
            onFailed {
                ToastUtils.showToast("暂无数据")
            }
        }
    }

}