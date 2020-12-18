package com.mine.children.message

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.customer.ApiRouter
import com.customer.bean.image.ImageViewInfo
import com.customer.utils.html.HtmlImageLoader
import com.customer.utils.html.HtmlText
import com.glide.GlideUtil
import com.lib.basiclib.base.activity.BaseNavActivity
import com.lib.basiclib.base.recycle.BaseRecyclerAdapter
import com.lib.basiclib.base.recycle.RecyclerViewHolder
import com.customer.component.panel.emotion.EmotionSpanBuilder
import com.lib.basiclib.utils.TimeUtils
import com.lib.basiclib.utils.ToastUtils
import com.lib.basiclib.utils.ViewUtils
import com.mine.R
import com.customer.data.mine.MineApi
import com.customer.data.mine.MineMessageCenter
import com.xiaojinzi.component.anno.RouterAnno
import com.xiaojinzi.component.impl.Router
import com.customer.component.dialog.GlobalDialog
import kotlinx.android.synthetic.main.act_msg_center_info.*


/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/25
 * @ Describe
 *
 */
@RouterAnno(host = "Mine", path = "messageInfo")
class MineMessageCenterInfoAct : BaseNavActivity() {

    private lateinit var adapter1: Adapter1

    private lateinit var adapter2: Adapter2

    override fun isShowBackIconWhite() = false

    override fun isSwipeBackEnable() = true

    override fun getContentResID() = R.layout.act_msg_center_info


    override fun initContentView() {
        msgSmartRefreshLayout.setEnableRefresh(false)//是否启用下拉刷新功能
        msgSmartRefreshLayout.setEnableLoadMore(false)//是否启用上拉加载功能
        msgSmartRefreshLayout.setEnableOverScrollBounce(true)//是否启用越界回弹
        msgSmartRefreshLayout.setEnableOverScrollDrag(true)//是否启用越界拖动（仿苹果效果）
        when (intent?.getIntExtra("msgType", 0) ?: 0) {
            0 -> {
                setPageTitle("互动消息")
                adapter1 = Adapter1()
                rvMsg.adapter = adapter1
                rvMsg.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                MineApi.getMessageTips("2") {
                    onSuccess {
                        if (!it.isNullOrEmpty()) {
                            adapter1.refresh(it)
                        } else {
                            tvHolder.text = "当前暂无任何互动消息哦~"
                            setVisible(tvHolder)
                        }

                    }
                    onFailed {
                        GlobalDialog.showError(this@MineMessageCenterInfoAct, it)
                    }
                }
            }
            1 -> {
                setPageTitle("官方消息")
                adapter2 = Adapter2()
                rvMsg.adapter = adapter2
                rvMsg.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                MineApi.getMessageTips("3") {
                    onSuccess {
                        if (!it.isNullOrEmpty()) {
                            adapter2.refresh(it)
                        } else {
                            tvHolder.text = "当前暂无任何官方消息哦~"
                            setVisible(tvHolder)
                        }

                    }
                    onFailed {
                        GlobalDialog.showError(this@MineMessageCenterInfoAct, it)
                    }
                }
            }
            else -> {
                setPageTitle("系统消息")
                adapter2 = Adapter2()
                rvMsg.adapter = adapter2
                rvMsg.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                MineApi.getMessageTips("0") {
                    onSuccess {
                        if (!it.isNullOrEmpty()) {
                            adapter2.refresh(it)
                        } else {
                            tvHolder.text = "当前暂无任何系统消息哦~"
                            setVisible(tvHolder)
                        }

                    }
                    onFailed {
                        GlobalDialog.showError(this@MineMessageCenterInfoAct, it)
                    }
                }
            }
        }
    }

    /**
     * 互动消息 adapter
     */

//    EmotionSpanBuilder.buildEmotionSpannable(context, data?.content)
    inner class Adapter1 : BaseRecyclerAdapter<MineMessageCenter>() {
        override fun getItemLayoutId(viewType: Int) = R.layout.adapter_message_center_user

        override fun bindData(holder: RecyclerViewHolder, position: Int, data: MineMessageCenter?) {
            GlideUtil.loadGrayscaleImage(
                this@MineMessageCenterInfoAct,
                data?.media,
                holder.getImageView(R.id.imgDes),
                10
            )
            GlideUtil.loadCircleImage(
                this@MineMessageCenterInfoAct,
                data?.avatar,
                holder.getImageView(R.id.imgPhoto)
            )
            holder.text(R.id.tvNickName, data?.nickname)
            holder.text(
                R.id.tvTime,
               data?.createtime_txt
            )
            holder.text(
                R.id.content,
                EmotionSpanBuilder.buildEmotionSpannable(getContext(), data?.content)
            )

            holder.itemView.setOnClickListener {
                showPageLoadingDialog()
                if (data?.apiType == "1") {
                    MineApi.getAnchorMoments(data.dynamic_id) {
                        onSuccess {
                            Router.withApi(ApiRouter::class.java).toMineAnchorInfo(
                                id = it[0].dynamic_id ?: "0",
                                userId = it[0].anchor_id ?: "",
                                name = it[0].nickname ?: "",
                                gender = it[0].sex ?: 1,
                                avatar = it[0].avatar ?: "",
                                time = it[0].create_time.toString(),
                                content = it[0].text ?: "",
                                commentNum = it[0].pls ?: "0",
                                imgList = getPicture(it[0].media),
                                liveState = it[0].live_status ?: "0"
                            )
                            hidePageLoadingDialog()
                        }
                        onFailed {
                            ToastUtils.showToast("获取数据失败")
                            hidePageLoadingDialog()
                        }
                    }
                } else if (data?.apiType == "2") {
                    MineApi.getHotDiscussSingle(data.dynamic_id) {
                        onSuccess {
                            Router.withApi(ApiRouter::class.java).toMineHotDiscussInfo(
                                id = it.id, userId = it.user_id,
                                name = it.nickname ?: "", gender = it.gender ?: 1,
                                avatar = it.avatar, time = it.created.toString(),
                                content = it.title ?: "", commentNum = it.comment_nums ?: "0",
                                imgList = getPicture(it.images)
                            )
                            hidePageLoadingDialog()
                        }
                        onFailed {
                            ToastUtils.showToast("获取数据失败")
                            hidePageLoadingDialog()
                        }
                    }

                }
            }
        }
    }

    /**
     * 官方活动，系统消息 adapter
     */
    inner class Adapter2 : BaseRecyclerAdapter<MineMessageCenter>() {
        override fun getItemLayoutId(viewType: Int) = R.layout.adapter_message_center

        @SuppressLint("SetJavaScriptEnabled")
        override fun bindData(holder: RecyclerViewHolder, position: Int, data: MineMessageCenter?) {
            val webView = holder.findViewById<TextView>(R.id.wbContent)
            HtmlText.from(data?.content ?: "").setImageLoader(object : HtmlImageLoader {

                override fun loadImage(url: String?, callback: HtmlImageLoader.Callback?) {
                    Glide.with(this@MineMessageCenterInfoAct)
                        .asBitmap()
                        .load(url)
                        .into(object : CustomTarget<Bitmap?>() {
                            override fun onLoadCleared(placeholder: Drawable?) {}
                            override fun onLoadFailed(errorDrawable: Drawable?) { callback?.onLoadFailed() }
                            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap?>?) {
                                callback?.onLoadComplete(resource)
                            }

                        })
                }

                override fun getErrorDrawable() = ViewUtils.getDrawable(R.mipmap.ic_placeholder)
                override fun getMaxWidth(): Int {
                    return  getTextWidth(webView)
                }


                override fun fitWidth(): Boolean {
                    return false
                }

                override fun getDefaultDrawable()= ViewUtils.getDrawable(R.mipmap.ic_placeholder)

            }).into(webView)
            when (intent?.getIntExtra("msgType", 0)) {
                2 -> holder.getImageView(R.id.imgType).setImageResource(R.mipmap.ic_message_xt)
                1 -> holder.getImageView(R.id.imgType).setImageResource(R.mipmap.ic_message_gf)
            }
        }

    }

    private fun getPicture(url: MutableList<String>?): ArrayList<ImageViewInfo> {
        val list: ArrayList<ImageViewInfo> = ArrayList()
        if (url != null) {
            for (i in url) {
                list.add(ImageViewInfo(i))
            }
        }
        return list
    }
    private fun getTextWidth(textView: TextView): Int {
        val dm = resources.displayMetrics
        return dm.widthPixels - textView.paddingLeft - textView.paddingRight
    }
}