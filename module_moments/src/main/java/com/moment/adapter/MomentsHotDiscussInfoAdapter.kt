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
import com.customer.component.dialog.GlobalDialog
import com.customer.component.input.InputPopWindow
import com.customer.component.panel.emotion.EmotionSpanBuilder
import com.customer.component.spanlite.SpanBuilder
import com.customer.component.spanlite.SpanLite
import com.customer.data.UserInfoSp
import com.customer.data.moments.CommentOnReplayResponse
import com.customer.data.moments.CommentOnResponse
import com.customer.data.moments.MomentsApi
import com.glide.GlideUtil
import com.lib.basiclib.base.recycle.BaseRecyclerAdapter
import com.lib.basiclib.base.recycle.RecyclerViewHolder
import com.lib.basiclib.base.xui.widget.popupwindow.good.GoodView
import com.lib.basiclib.utils.FastClickUtil
import com.lib.basiclib.utils.ToastUtils
import com.lib.basiclib.utils.ViewUtils
import com.moment.R
import com.xiaojinzi.component.impl.Router
import java.util.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/21
 * @ Describe 热门讨论详情
 *
 */
class MomentsHotDiscussInfoAdapter(private val activity: Activity, private val mainId: String) :

    BaseRecyclerAdapter<CommentOnResponse>() {

    private var inputPopWindow: InputPopWindow? = null

    private val mGoodView = GoodView(activity)

    private lateinit var replayAdapter: ReplayAdapter

    override fun getItemLayoutId(viewType: Int): Int {
        return R.layout.adapter_hotdiscuss_info
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        replayAdapter = ReplayAdapter(activity, mainId)
        val recyclerView = holder.findViewById<RecyclerView>(R.id.rvReplyList)
        recyclerView.layoutManager = object : LinearLayoutManager(activity, VERTICAL, false) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        recyclerView.adapter = replayAdapter
        super.onBindViewHolder(holder, position)
    }


    override fun bindData(holder: RecyclerViewHolder, position: Int, data: CommentOnResponse?) {
        val img = holder.findViewById<ImageView>(R.id.imgCommentUserPhoto)
        GlideUtil.loadCircleImage(img.context, data?.avatar, img, true)
        holder.text(R.id.tvCommentUserName, data?.nickname)
        holder.text(
            R.id.tvCommentContent,
            EmotionSpanBuilder.buildEmotionSpannable(activity, data?.content)
        )
        holder.text(R.id.tvCommentTime, data?.created)
        holder.text(R.id.tvDianZan, (data?.like ?: 0).toString())
        holder.text(R.id.tvCommentTime, data?.created)
        if (data?.is_like == "1") {
            holder.findViewById<ImageView>(R.id.imgDianZan).background =
                ViewUtils.getDrawable(R.mipmap.ic_yidianzan)
        } else holder.findViewById<ImageView>(R.id.imgDianZan).background =
            ViewUtils.getDrawable(R.mipmap.ic_dianzan)
        holder.click(R.id.linDianZan) {
            //未登录
            if (!UserInfoSp.getIsLogin()) {
                GlobalDialog.notLogged(activity)
                return@click
            }
            MomentsApi.getQuizCommentLike(data?.id.toString()) {
                onSuccess {
                    data?.is_like = "1"
                    data?.like = data?.like?.plus(1)
                    mGoodView.setText("+1")
                        .setTextColor(Color.parseColor("#f66467"))
                        .setTextSize(12)
                        .show(holder.getImageView(R.id.imgDianZan))
                    notifyItemChanged(position)
                }
                onFailed {
                    data?.is_like = "0"
                    data?.like = data?.like?.minus(1)
                    mGoodView.setText("-1")
                        .setTextColor(Color.parseColor("#AFAFAF"))
                        .setTextSize(12)
                        .show(holder.getImageView(R.id.imgDianZan))
                    notifyItemChanged(position)
                }
            }

        }
        if (!data?.reply.isNullOrEmpty()) {
            replayAdapter.replayId = data?.id ?: ""
            replayAdapter.refresh(data?.reply)
            ViewUtils.setVisible(holder.findView(R.id.rvReplyList))
        } else ViewUtils.setGone(holder.findView(R.id.rvReplyList))
        holder.click(R.id.imgCommentUserPhoto) {
            if (!FastClickUtil.isFastClick()) {
                when (data?.user_type) {
                    "1", "0" -> Router.withApi(ApiRouter::class.java)
                        .toUserPage(data.user_id.toString())
                    "2" -> Router.withApi(ApiRouter::class.java)
                        .toAnchorPage(data.user_id.toString())
                }
            }
        }
        holder.click(R.id.linReply) {
            showInput(data?.article_id.toString(), data?.id.toString())
        }
    }

    inner class ReplayAdapter(
        private val context: Context,
        private val mainId: String, var replayId: String = ""
    ) : BaseRecyclerAdapter<CommentOnReplayResponse>() {
        override fun getItemLayoutId(viewType: Int) = R.layout.adapter_hotdiscuss_info_replay

        override fun bindData(
            holder: RecyclerViewHolder,
            position: Int,
            data: CommentOnReplayResponse?
        ) {
            val viewSpan = SpanLite.with(holder.findViewById(R.id.tvReplay))
            when {
                //回复楼主
                data?.reply_id == replayId -> {
                    //设置type
                    setUserType(viewSpan, data)
                    //被回复评论名字
                    viewSpan.append(
                        SpanBuilder.Builder(data.reply_nickname + " : ")
                            .drawTextColor(ViewUtils.getColor(R.color.color_5F84B0))
                            .drawTextSizeAbsolute(36)
                            .setOnClick(object : SpanBuilder.Listener() {
                                override fun onClick(widget: View?) {
//                                    ToastUtils.showToast("回复评论名字 热门讨论")
                                    if (!FastClickUtil.isFastClick()) Router.withApi(ApiRouter::class.java)
                                        .toUserPage(data.user_id.toString())
                                }
                            }).build()
                    )
                    //回复内容
                    setReplayContent(viewSpan, data.content ?: "", data)

                }
                //回复评论
                data?.reply_id != replayId -> {
                    //设置type
                    setUserType(viewSpan, data)
                    //被回复评论名字
                    val str = " " + data?.nickname
                    viewSpan.append(
                        SpanBuilder.Builder(str)
                            .drawTextColor(ViewUtils.getColor(R.color.color_5F84B0))
                            .drawTextSizeAbsolute(36).setOnClick(object : SpanBuilder.Listener() {
                                override fun onClick(widget: View?) {
//                                    ToastUtils.showToast("被回复评论名字 热门讨论")
                                    if (!FastClickUtil.isFastClick()) Router.withApi(ApiRouter::class.java)
                                        .toUserPage(data?.user_id.toString())
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
                        SpanBuilder.Builder(data?.reply_nickname + " : ")
                            .drawTextColor(ViewUtils.getColor(R.color.color_5F84B0))
                            .drawTextSizeAbsolute(36).setOnClick(object : SpanBuilder.Listener() {
                                override fun onClick(widget: View?) {
//                                    ToastUtils.showToast("被回复评论名字 热门讨论")
//                                startPersonal(data.user_type,data.user_id)
                                    if (!FastClickUtil.isFastClick()) Router.withApi(ApiRouter::class.java)
                                        .toUserPage(data?.user_id.toString())
                                }
                            }).build()
                    )
                    //回复内容
                    setReplayContent(viewSpan, data?.content ?: "", data)
                }
            }
            viewSpan.active()
        }


        private fun setUserType(viewSpan: SpanLite, data: CommentOnReplayResponse?) {
            if (data?.user_id == mainId) {
                //楼主
                viewSpan.append(
                    SpanBuilder.Builder("2")
                        .drawImageCenter(context, R.mipmap.ic_louzhu)
                        .build()
                )
            } else if (data?.user_type == "2") {
                //主播
                viewSpan.append(
                    SpanBuilder.Builder("1")
                        .drawImageCenter(context, R.mipmap.ic_zhubo)
                        .build()
                )
            }
        }

        private fun setReplayContent(
            viewSpan: SpanLite,
            str: String,
            data: CommentOnReplayResponse?
        ) {
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
                    showInput(data?.article_id.toString(), data?.reply_id.toString())
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

    //输入框
    private fun showInput(id: String, replayId: String) {
        if (inputPopWindow == null) {
            inputPopWindow = InputPopWindow(activity)
            inputPopWindow?.setOnTextSendListener {
                //消息
                goCommend(id, replayId, it)
                inputPopWindow?.dismiss()
            }
        }
        inputPopWindow?.showAtLocation(
            activity.window?.decorView?.rootView,
            Gravity.NO_GRAVITY,
            0,
            0
        )
        inputPopWindow?.editTextPanel?.postDelayed({ inputPopWindow?.showKeyboard() }, 50)
    }

    private fun goCommend(id: String, replayId: String, str: String) {
        val dialogCommentSuccess = DialogCommentsSuccess(activity)
        MomentsApi.setCommentReply(id, replayId, str) {
            onSuccess {
                dialogCommentSuccess.show()
                Timer().schedule(object : TimerTask() {
                    override fun run() {
                        if (dialogCommentSuccess.isShowing) dialogCommentSuccess.dismiss()
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