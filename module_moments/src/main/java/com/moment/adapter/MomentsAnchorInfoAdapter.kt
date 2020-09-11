package com.moment.adapter

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.style.AbsoluteSizeSpan
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.customer.ApiRouter
import com.customer.component.dialog.DialogCommentsSuccess
import com.customer.component.spanlite.SpanBuilder
import com.customer.component.spanlite.SpanLite
import com.glide.GlideUtil
import com.lib.basiclib.base.recycle.BaseRecyclerAdapter
import com.lib.basiclib.base.recycle.RecyclerViewHolder
import com.lib.basiclib.base.xui.widget.popupwindow.good.GoodView
import com.customer.component.panel.emotion.EmotionSpanBuilder
import com.lib.basiclib.utils.FastClickUtil
import com.lib.basiclib.utils.ToastUtils
import com.lib.basiclib.utils.ViewUtils
import com.moment.R
import com.customer.data.moments.DavisCommentOnReplayResponse
import com.customer.data.moments.DavisCommentOnResponse
import com.customer.component.dialog.GlobalDialog
import com.customer.component.input.InputPopWindow
import com.customer.data.UserInfoSp
import com.customer.data.moments.MomentsApi
import com.xiaojinzi.component.impl.Router
import java.util.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/21
 * @ Describe主播详情
 *
 */
class MomentsAnchorInfoAdapter(private val context: Activity, private val mainId: String) :
    BaseRecyclerAdapter<DavisCommentOnResponse>() {

    private val mGoodView = GoodView(context)

    private lateinit var replayAdapter: ReplayAdapter

    override fun getItemLayoutId(viewType: Int): Int {
        return R.layout.adapter_hotdiscuss_info
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        replayAdapter = ReplayAdapter(context, mainId)
        val recyclerView = holder.findViewById<RecyclerView>(R.id.rvReplyList)
        recyclerView.layoutManager = object : LinearLayoutManager(context, VERTICAL, false) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        recyclerView.adapter = replayAdapter
        super.onBindViewHolder(holder, position)
    }


    override fun bindData(
        holder: RecyclerViewHolder,
        position: Int,
        data: DavisCommentOnResponse?
    ) {
        val img = holder.findViewById<ImageView>(R.id.imgCommentUserPhoto)
        GlideUtil.loadCircleImage(img.context, data?.avatar, img, true)
        holder.text(R.id.tvCommentUserName, data?.nickname)
        holder.text(
            R.id.tvCommentContent,
            EmotionSpanBuilder.buildEmotionSpannable(context, data?.content)
        )
        holder.text(R.id.tvCommentTime, data?.created)
        holder.text(R.id.tvDianZan, data?.like ?: "0")
        holder.text(R.id.tvCommentTime, data?.created)
        if (data?.is_like == true) {
           holder.findViewById<ImageView>(R.id.imgDianZan).background = ViewUtils.getDrawable(R.mipmap.ic_yidianzan)
        } else holder.findViewById<ImageView>(R.id.imgDianZan).background = ViewUtils.getDrawable(R.mipmap.ic_dianzan)
        holder.click(R.id.linDianZan) {
            //未登录
            if (!UserInfoSp.getIsLogin()) {
                GlobalDialog.notLogged(context)
                return@click
            }
            if (!FastClickUtil.isFastClick()) {
                //未登录
                if (!UserInfoSp.getIsLogin()) {
                    GlobalDialog.notLogged(context)
                    return@click
                }
                MomentsApi.clickZansDavis("2",data?.comment_id.toString()){
                    onSuccess {
                        if (data?.is_like == true) {
                            data.is_like = false
                            holder.getImageView(R.id.imgDianZan)?.setImageResource(R.mipmap.ic_dianzan)
                            val zan = (data.like?.toInt()?.minus(1)).toString()
                            data.like = data.like?.toInt()?.minus(1).toString()
                            holder.text(R.id.tvDianZan, zan)
                            mGoodView.setText("-1")
                                .setTextColor(Color.parseColor("#AFAFAF"))
                                .setTextSize(12)
                                .show(holder.getImageView(R.id.imgDianZan))
                        } else {
                            data?.is_like = true
                            holder.getImageView(R.id.imgDianZan)?.setImageResource(R.mipmap.ic_yidianzan)
                            val zan = (data?.like?.toInt()?.plus(1)).toString()
                            data?.like = data?.like?.plus(1)
                            holder.text(R.id.tvDianZan, zan)
                            mGoodView.setText("+1")
                                .setTextColor(Color.parseColor("#f66467"))
                                .setTextSize(12)
                                .show(holder.getImageView(R.id.imgDianZan))
                        }
                        notifyItemChanged(position)
                    }
                    onFailed {ToastUtils.showToast("点赞失败")  }
                }

            } else ToastUtils.showToast("请勿重复点击")
        }

        if (data?.user_type == "2") {
            holder.backgroundResId(R.id.imgCommentUserType, R.mipmap.ic_live_anchor)
        } else holder.backgroundResId(R.id.imgCommentUserType, 0)

        if (!data?.reply.isNullOrEmpty()) {
            replayAdapter.replayId = data?.uid ?: ""
            replayAdapter.refresh(data?.reply)
            ViewUtils.setVisible(holder.findView(R.id.rvReplyList))
        } else ViewUtils.setGone(holder.findView(R.id.rvReplyList))
        holder.click(R.id.imgCommentUserPhoto){
            if (!FastClickUtil.isFastClick()){
                when(data?.user_type){
                    "1","0" ->  Router.withApi(ApiRouter::class.java).toUserPage(data.uid.toString())
                    "2" ->  Router.withApi(ApiRouter::class.java).toAnchorPage(data.uid.toString())
                }
            }
        }
        holder.click(R.id.linReply){
            //未登录
            if (!UserInfoSp.getIsLogin()) {
                GlobalDialog.notLogged(context)
                return@click
            }
            showInput(mainId, data?.comment_id.toString())
        }
    }

    inner class ReplayAdapter(
        private val context: Context,
        private val mainId: String, var replayId: String = ""
    ) : BaseRecyclerAdapter<DavisCommentOnReplayResponse>() {
        override fun getItemLayoutId(viewType: Int) = R.layout.adapter_hotdiscuss_info_replay

        override fun bindData(
            holder: RecyclerViewHolder,
            position: Int,
            data: DavisCommentOnReplayResponse?
        ) {
            val viewSpan = SpanLite.with(holder.findViewById(R.id.tvReplay))
            when {
                //回复楼主
                data?.p_uid == replayId -> {
                    //设置type
                    setUserType(viewSpan, data)
                    //回复名字
                    val str = " " + data.nickname + " : "
                    viewSpan.append(SpanBuilder.Builder(str)
                        .drawTextColor(ViewUtils.getColor(R.color.color_5F84B0))
                        .drawTextSizeAbsolute(36).setOnClick(object : SpanBuilder.Listener() {
                            override fun onClick(widget: View?) {
//                                        ToastUtils.showToast("回复名字")
                                if (!FastClickUtil.isFastClick()) Router.withApi(ApiRouter::class.java).toUserPage(data.uid.toString())
//                                startPersonal(data.user_type,data.uid)
                            }
                        }).build())
                    //回复内容
                    setReplayContent(viewSpan, data.content ?: "",data)

                }
                //回复评论
                data?.p_uid != replayId -> {
                    //设置type
                    setUserType(viewSpan, data)
                    //被回复评论名字
                    val str = " " + data?.nickname
                    viewSpan.append(
                        SpanBuilder.Builder(str)
                            .drawTextColor(ViewUtils.getColor(R.color.color_5F84B0))
                            .drawTextSizeAbsolute(36).setOnClick(object : SpanBuilder.Listener() {
                                override fun onClick(widget: View?) {
//                                    ToastUtils.showToast("被回复评论名字" )
//                                startPersonal(data.user_type,data.user_id)
                                    if (!FastClickUtil.isFastClick()) Router.withApi(ApiRouter::class.java).toUserPage(data?.uid.toString())
                                }
                            }).build()
                    )
                    viewSpan.append(
                        SpanBuilder.Builder(" 回复 ")
                            .drawTextColor(ViewUtils.getColor(R.color.color_333333))
                            .drawTextSizeAbsolute(36).build()
                    )
                    //设置type
                    setUserType(viewSpan, data)
                    //被回复评论名字
                    viewSpan.append(
                        SpanBuilder.Builder(data?.p_nickname + " : ")
                            .drawTextColor(ViewUtils.getColor(R.color.color_5F84B0))
                            .drawTextSizeAbsolute(36).setOnClick(object : SpanBuilder.Listener() {
                                override fun onClick(widget: View?) {
                                    if (!FastClickUtil.isFastClick()) Router.withApi(ApiRouter::class.java).toUserPage(data?.p_uid.toString())
//                                    ToastUtils.showToast("被回复评论名字 2")
//                                startPersonal(data.user_type,data.user_id)
                                }
                            }).build()
                    )
                    //回复内容
                    setReplayContent(viewSpan, data?.content ?: "",data)
                }
            }
            viewSpan.active()
        }


        private fun setUserType(viewSpan: SpanLite, data: DavisCommentOnReplayResponse?) {
            if (data?.user_type == "2") {
                //主播
                viewSpan.append(
                    SpanBuilder.Builder("1")
                        .drawImageCenter(context, R.mipmap.ic_zhubo)
                        .build()
                )
            }
        }

        private fun setReplayContent(viewSpan: SpanLite, str: String,data: DavisCommentOnReplayResponse??) {
            val builder = EmotionSpanBuilder.buildEmotionSpannable(context, str)
            builder.setSpan(
                ForegroundColorSpan(ViewUtils.getColor(R.color.color_333333)),
                0,
                str.length,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE
            )
            builder.setSpan(
                AbsoluteSizeSpan(13, true),
                0,
                str.length,
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE
            )
            builder.setSpan(object : ClickableSpan() {
                override fun onClick(widget: View) {
//                    ToastUtils.showToast("吊起键盘回复")
                    showInput(mainId, data?.comment_id.toString())
                }

                override fun updateDrawState(ds: TextPaint) {
                    /**set textColor**/
                    // ds.setColor( ds.linkColor );
                    /**Remove the underline**/
                    ds.isUnderlineText = false
                }
            }, 0, str.length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
            viewSpan.append(SpannableStringBuilder.valueOf(builder))
        }
    }


    private var inputPopWindow: InputPopWindow? = null
    //输入框
    private fun showInput(id:String,replayId:String) {
        if (inputPopWindow == null) {
            inputPopWindow =  InputPopWindow(context)
            inputPopWindow?.setOnTextSendListener {
                //消息
                goCommend(id,replayId,it)
                inputPopWindow?.dismiss()
            }
        }
        inputPopWindow?.showAtLocation(context.window?.decorView?.rootView, Gravity.NO_GRAVITY, 0, 0)
        inputPopWindow?.editTextPanel?.postDelayed({ inputPopWindow?.showKeyboard() }, 50)
    }
    private fun goCommend(id:String,replayId:String,str: String) {
        val dialogCommentSuccess = DialogCommentsSuccess(context)
        MomentsApi.setDavisCommentReplay(id, replayId, str) {
            onSuccess {
                dialogCommentSuccess.show()
                Timer().schedule(object : TimerTask() {
                    override fun run() {
                        if (dialogCommentSuccess.isShowing)  dialogCommentSuccess.dismiss()
                        this.cancel()
                    }

                }, 2000)
            }
            onFailed {
                ToastUtils.showToast(it.getMsg())
            }
        }
    }
}