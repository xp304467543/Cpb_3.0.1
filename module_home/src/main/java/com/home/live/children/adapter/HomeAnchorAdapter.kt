package com.home.live.children.adapter

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.customer.ApiRouter
import com.customer.bean.image.ImageViewInfo
import com.customer.bean.image.NineGridInfoAnchor
import com.customer.component.WaveView
import com.customer.component.dialog.GlobalDialog
import com.customer.data.UserInfoSp
import com.glide.GlideUtil
import com.home.R
import com.lib.basiclib.base.recycle.XRecyclerAdapter
import com.lib.basiclib.base.xui.widget.imageview.nine.NineGridImageView
import com.lib.basiclib.base.xui.widget.imageview.nine.NineGridImageViewAdapter
import com.lib.basiclib.base.xui.widget.imageview.preview.PreviewBuilder
import com.lib.basiclib.base.xui.widget.imageview.preview.loader.GlideMediaLoader
import com.lib.basiclib.base.xui.widget.popupwindow.good.GoodView
import com.lib.basiclib.utils.FastClickUtil
import com.lib.basiclib.utils.TimeUtils
import com.lib.basiclib.utils.ToastUtils
import com.lib.basiclib.utils.ViewUtils
import com.xiaojinzi.component.impl.Router

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/31
 * @ Describe
 *
 */
class HomeAnchorAdapter(val mContext: Context) :
    XRecyclerAdapter<NineGridInfoAnchor, HomeAnchorAdapter.NineGridHolder>() {

    val mGoodView = GoodView(mContext)

    override fun getViewHolder(parent: ViewGroup, viewType: Int): NineGridHolder {
        return NineGridHolder(inflateView(parent, R.layout.adapter_item_nine_grid_fill_style))
    }

    override fun bindData(holder: NineGridHolder, position: Int, data: NineGridInfoAnchor?) {
        if (data != null) {
            holder.bind(data)
        }
    }


    override fun getItemViewType(position: Int): Int {
        return getItem(position).getSpanType()
    }


    inner class NineGridHolder : RecyclerView.ViewHolder {
        private var mNglContent: NineGridImageView<ImageViewInfo>? = null
        private var tvMomentsDiscussHolderTitle: TextView? = null
        private var tvMomentsDiscussHolderName: TextView? = null
        private var tvMomentsDiscussHolderTime: TextView? = null
        private var tvHotDiscussHolderDianZan: TextView? = null
        private var tvLiveHotDiscussHolderReply: TextView? = null
        private var imgMomentsDiscussHolderPhoto: ImageView? = null
        private var imgHotDiscussHolderDianZan: ImageView? = null
        private var linDianZan: LinearLayout? = null
        private var linReply: LinearLayout? = null
        private var imgAnchorSex: ImageView? = null
        private var circleWave: WaveView? = null

        constructor(itemView: View) : super(itemView) {
            tvMomentsDiscussHolderTitle = itemView.findViewById(R.id.tvMomentsDiscussHolderTitle)
            mNglContent = itemView.findViewById(R.id.ngl_images)
            tvMomentsDiscussHolderName = itemView.findViewById(R.id.tvMomentsDiscussHolderName)
            tvMomentsDiscussHolderTime = itemView.findViewById(R.id.tvMomentsDiscussHolderTime)
            tvHotDiscussHolderDianZan = itemView.findViewById(R.id.tvHotDiscussHolderDianZan)
            imgHotDiscussHolderDianZan = itemView.findViewById(R.id.imgHotDiscussHolderDianZan)
            tvLiveHotDiscussHolderReply = itemView.findViewById(R.id.tvLiveHotDiscussHolderReply)
            imgMomentsDiscussHolderPhoto = itemView.findViewById(R.id.imgMomentsDiscussHolderPhoto)
            linReply = itemView.findViewById(R.id.linReply)
            linDianZan = itemView.findViewById(R.id.linDianZan)
            imgAnchorSex = itemView.findViewById(R.id.imgAnchorSex)
            circleWave = itemView.findViewById(R.id.circleWave)
            /**
             * 图片加载
             */
            val mAdapter = object : NineGridImageViewAdapter<ImageViewInfo>() {
                override fun onDisplayImage(
                    context: Context?,
                    imageView: ImageView?,
                    imageViewInfo: ImageViewInfo?
                ) {
                    if (imageView != null) {
                        Glide.with(mContext)
                            .load(imageViewInfo?.url)
                            .apply(GlideMediaLoader.getRequestOptions())
                            .into(imageView)
                    }
                }
            }
            mNglContent?.setAdapter(mAdapter)
            mNglContent?.setItemImageClickListener { imageView, index, list ->
                computeBoundsBackward(list) //组成数据
                PreviewBuilder.from((imageView.context as Activity))
                    .setImgs(list!!)
                    .setCurrentIndex(index)
                    .setType(PreviewBuilder.IndicatorType.Dot)
                    .start() //启动
            }

        }


        /**
         * 查找信息
         * @param list 图片集合
         */
        private fun computeBoundsBackward(list: List<ImageViewInfo>) {
            if (mNglContent == null || mNglContent?.childCount!! < 0) return
            for (i in 0 until mNglContent!!.childCount) {
                val itemView = mNglContent!!.getChildAt(i)
                val bounds = Rect()
                if (itemView != null) {
                    val thumbView = itemView as ImageView
                    thumbView.getGlobalVisibleRect(bounds)
                }
                list[i].bounds = bounds
                list[i].url = list[i].url!!
            }
        }

        fun bind(gridInfo: NineGridInfoAnchor) {
            mNglContent?.setImagesData(gridInfo.getImgUrlList(), gridInfo.getSpanType())
            tvMomentsDiscussHolderTitle?.text = gridInfo.getContent()
            tvMomentsDiscussHolderName?.text = gridInfo.getUserName()
            tvMomentsDiscussHolderTime?.text = TimeUtils.longToDateString(gridInfo.getTime() ?: 0).toString()
            tvHotDiscussHolderDianZan?.text = gridInfo.getLikeNum()
            tvLiveHotDiscussHolderReply?.text = gridInfo.getCommentNum()
            imgMomentsDiscussHolderPhoto?.let {
                GlideUtil.loadCircleImage(
                    it.context, gridInfo.getUserAvatar(),
                    it, true
                )
            }
            if (gridInfo.getGender() == 1) {
                imgAnchorSex?.setImageResource(R.mipmap.ic_live_anchor_boy)
            } else if (gridInfo.getGender() == 0) {
                imgAnchorSex?.setBackgroundResource(R.mipmap.ic_live_anchor_girl)
            }
            //点赞
            linDianZan?.setOnClickListener {
                //未登录
                if (!UserInfoSp.getIsLogin()) {
                    GlobalDialog.notLogged(mContext as Activity)
                    return@setOnClickListener
                }
                if (!FastClickUtil.isFastClick()) {
                    if (gridInfo.getIsLike() == true) {
                        gridInfo.setIsLike(false)
                        imgHotDiscussHolderDianZan?.setImageResource(R.mipmap.ic_dianzan)
                        val zan = (gridInfo.getLikeNum()?.toInt()?.minus(1)).toString()
                        gridInfo.setLikeNum(zan)
                        tvHotDiscussHolderDianZan?.text = zan
                        mGoodView.setText("-1")
                            .setTextColor(Color.parseColor("#AFAFAF"))
                            .setTextSize(14)
                            .show(imgHotDiscussHolderDianZan)
                    } else {
                        gridInfo.setIsLike(true)
                        imgHotDiscussHolderDianZan?.setImageResource(R.mipmap.ic_yidianzan)
                        val zan = (gridInfo.getLikeNum()?.toInt()?.plus(1)).toString()
                        gridInfo.setLikeNum(zan)
                        tvHotDiscussHolderDianZan?.text = zan
                        mGoodView.setText("+1")
                            .setTextColor(Color.parseColor("#f66467"))
                            .setTextSize(14)
                            .show(imgHotDiscussHolderDianZan)
                    }
                } else ToastUtils.showToast("请勿重复点击")
            }
            if (gridInfo.getIsLive() == "1") {
                circleWave?.setInitialRadius(50f)
                circleWave?.start()
            }else ViewUtils.setGone(circleWave)
            //评论跳转
            linReply?.setOnClickListener {
                Router.withApi(ApiRouter::class.java)
                    .toMineAnchorInfo(
                        gridInfo.getId().toString(),
                        gridInfo.getUserId().toString(),
                        gridInfo.getUserName().toString(),
                        gridInfo.getGender(),
                        gridInfo.getUserAvatar().toString(),
                        TimeUtils.longToDateString(gridInfo.getTime() ?: 0).toString(),
                        gridInfo.getContent().toString(),
                        gridInfo.getCommentNum().toString(),
                        gridInfo.getIsLive().toString(),
                        gridInfo.getImgUrlList() as ArrayList<ImageViewInfo>
                    )
            }
        }
    }

}