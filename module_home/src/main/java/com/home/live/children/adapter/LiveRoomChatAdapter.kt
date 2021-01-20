package com.home.live.children.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import com.customer.ApiRouter
import com.customer.component.dialog.DialogForbiddenWords
import com.customer.component.dialog.GlobalDialog
import com.customer.component.panel.emotion.EmotionSpanBuilder
import com.customer.data.LiveCallPersonal
import com.customer.data.UserInfoSp
import com.customer.data.home.HomeLiveTwentyNewsResponse
import com.customer.data.lottery.BetShareBean
import com.customer.data.lottery.PlaySecData
import com.customer.utils.JsonUtils
import com.glide.GlideUtil
import com.home.R
import com.home.live.bet.old.LiveRoomBetAccessFragment
import com.home.live.bet.old.LotteryBet
import com.home.live.bet.old.LotteryBetAccess
import com.hwangjr.rxbus.RxBus
import com.lib.basiclib.base.recycle.BaseRecyclerAdapter
import com.lib.basiclib.base.recycle.RecyclerViewHolder
import com.lib.basiclib.base.xui.widget.layout.ExpandableLayout
import com.lib.basiclib.utils.FastClickUtil
import com.lib.basiclib.utils.ViewUtils
import com.xiaojinzi.component.impl.Router
import org.json.JSONObject
import java.math.BigDecimal

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/26
 * @ Describe
 *
 */
class LiveRoomChatAdapter(private val context: Context, private val fragmentManager:FragmentManager) :
    BaseRecyclerAdapter<HomeLiveTwentyNewsResponse>() {
    val TYPE_NORMAL = 0
    val TYPE_GIFT = 1
    val TYPE_RED = 2
    val TYPE_SHARE_ORDER = 3


    override fun getItemViewType(position: Int): Int {
        return when {
            data[position].type == "publish" -> {
                //分享注单
                if (data[position].event == "pushPlan") TYPE_SHARE_ORDER else TYPE_NORMAL
            }
            //礼物
            data[position].type == "gift" -> TYPE_GIFT
            //红包
            data[position].gift_type == "4" -> TYPE_RED

            else -> TYPE_GIFT
        }
    }

    override fun getItemLayoutId(viewType: Int): Int {
        return when (viewType) {
            TYPE_NORMAL -> R.layout.adapter_fragment_chat_nomal
            TYPE_RED -> R.layout.adapter_fragment_chat_gift
            TYPE_SHARE_ORDER -> R.layout.adaptershare_bet_order
            else -> R.layout.adapter_fragment_chat_gift
        }
    }

    override fun bindData(
        holder: RecyclerViewHolder,
        position: Int,
        data: HomeLiveTwentyNewsResponse?
    ) {
        when {
            getItemViewType(position) == TYPE_NORMAL -> {
                initNormal(holder, position, data)
            }
            getItemViewType(position) == TYPE_RED -> {
                initRed(holder, position, data)
            }
            getItemViewType(position) == TYPE_GIFT -> {
                initGift(holder, position, data)
            }
            getItemViewType(position) == TYPE_SHARE_ORDER -> {
                initShardOrder(holder, position, data)
            }
        }

    }


    private fun initNormal(
        holder: RecyclerViewHolder,
        position: Int,
        data: HomeLiveTwentyNewsResponse?
    ) {
        holder.text(R.id.tvChatUserName, data?.userName)
        holder.text(R.id.tvChatTime, data?.sendTimeTxt)
        holder.text(
            R.id.tvChatContent,
            EmotionSpanBuilder.buildEmotionSpannable(context, data?.text)
        )
        GlideUtil.loadCircleImage(
            context,
            data?.avatar,
            holder.getImageView(R.id.imgChatUserPhoto),
            true
        )
        when (data?.userType) {
            "2" -> holder.findViewById<ImageView>(R.id.imgVipLevel)
                .setBackgroundResource(R.mipmap.ic_live_chat_manager)
            "1" -> holder.findViewById<ImageView>(R.id.imgVipLevel)
                .setBackgroundResource(R.mipmap.ic_live_anchor)
            "0" -> when (data.vip) {
                "1" -> holder.findViewById<ImageView>(R.id.imgVipLevel)
                    .setBackgroundResource(R.mipmap.vip1)
                "2" -> holder.findViewById<ImageView>(R.id.imgVipLevel)
                    .setBackgroundResource(R.mipmap.vip2)
                "3" -> holder.findViewById<ImageView>(R.id.imgVipLevel)
                    .setBackgroundResource(R.mipmap.vip3)
                "4" -> holder.findViewById<ImageView>(R.id.imgVipLevel)
                    .setBackgroundResource(R.mipmap.vip4)
                "5" -> holder.findViewById<ImageView>(R.id.imgVipLevel)
                    .setBackgroundResource(R.mipmap.vip5)
                "6" -> holder.findViewById<ImageView>(R.id.imgVipLevel)
                    .setBackgroundResource(R.mipmap.vip6)
                "7" -> holder.findViewById<ImageView>(R.id.imgVipLevel)
                    .setBackgroundResource(R.mipmap.vip7)
                else -> {
                    ViewUtils.setGone(holder.findViewById(R.id.imgVipLevel))
                    val text = holder.findViewById<TextView>(R.id.tvChatUserName)
                    val layout = text.layoutParams as ConstraintLayout.LayoutParams
                    layout.marginStart = 0
                }
            }
        }
        //@功能
        holder.findViewById<ConstraintLayout>(R.id.chatCons).setOnLongClickListener {
            RxBus.get().post(LiveCallPersonal(data?.userName.toString()))
            true
        }
        //个人主页
        holder.findViewById<ImageView>(R.id.imgChatUserPhoto).setOnClickListener {
            when (data?.userType) {
                "0" -> Router.withApi(ApiRouter::class.java).toUserPage(data.user_id.toString())
                "1" -> Router.withApi(ApiRouter::class.java).toAnchorPage(data.user_id.toString())
                "2" -> Router.withApi(ApiRouter::class.java)
                    .toExpertPage(data.user_id.toString(), "")
            }
        }
        //禁言
        holder.findViewById<ImageView>(R.id.imgChatUserPhoto).setOnLongClickListener {
            if (UserInfoSp.getUserType() == "2") {
                val dialog = data?.let { it1 -> DialogForbiddenWords(context, it1) }
                dialog?.show()
            }
            true
        }
    }

    private fun initRed(
        holder: RecyclerViewHolder,
        position: Int,
        data: HomeLiveTwentyNewsResponse?
    ) {
        ViewUtils.setGone(holder.findViewById(R.id.imgGift))
        when (data?.vip) {
            "1" -> holder.findViewById<ImageView>(R.id.imgVip).setBackgroundResource(R.mipmap.svip_1)
            "2" -> holder.findViewById<ImageView>(R.id.imgVip).setBackgroundResource(R.mipmap.svip_2)
            "3" -> holder.findViewById<ImageView>(R.id.imgVip).setBackgroundResource(R.mipmap.svip_3)
            "4" -> holder.findViewById<ImageView>(R.id.imgVip).setBackgroundResource(R.mipmap.svip_4)
            "5" -> holder.findViewById<ImageView>(R.id.imgVip).setBackgroundResource(R.mipmap.svip_5)
            "6" -> holder.findViewById<ImageView>(R.id.imgVip).setBackgroundResource(R.mipmap.svip_6)
            "7" -> holder.findViewById<ImageView>(R.id.imgVip).setBackgroundResource(R.mipmap.svip_7)
            else -> ViewUtils.setGone(holder.findViewById(R.id.imgVip))
        }
        holder.text(R.id.tvUserName, data?.userName)
        holder.text(R.id.tvGiftNum, "发出一个 ")
        holder.text(R.id.tvGiftName, data?.gift_price + "元红包")
        holder.text(R.id.tvDes, " 大家快去抢啊")
    }

    private fun initGift(
        holder: RecyclerViewHolder,
        position: Int,
        data: HomeLiveTwentyNewsResponse?
    ) {
        when (data?.vip) {
            "1" -> holder.findViewById<ImageView>(R.id.imgVip).setBackgroundResource(R.mipmap.svip_1)
            "2" -> holder.findViewById<ImageView>(R.id.imgVip).setBackgroundResource(R.mipmap.svip_2)
            "3" -> holder.findViewById<ImageView>(R.id.imgVip).setBackgroundResource(R.mipmap.svip_3)
            "4" -> holder.findViewById<ImageView>(R.id.imgVip).setBackgroundResource(R.mipmap.svip_4)
            "5" -> holder.findViewById<ImageView>(R.id.imgVip).setBackgroundResource(R.mipmap.svip_5)
            "6" -> holder.findViewById<ImageView>(R.id.imgVip).setBackgroundResource(R.mipmap.svip_6)
            "7" -> holder.findViewById<ImageView>(R.id.imgVip).setBackgroundResource(R.mipmap.svip_7)
            else -> ViewUtils.setGone(holder.findViewById(R.id.imgVip))
        }
        holder.text(R.id.tvUserName, data?.userName)
        holder.text(R.id.tvGiftNum, "送给主播 " + data?.gift_num + " 个 ")
        holder.text(R.id.tvGiftName, data?.gift_name)
        GlideUtil.loadImage(context, data?.icon, holder.getImageView(R.id.imgGift))
        holder.text(R.id.tvNum, "  x" + data?.final_num.toString())
    }

    var followList = arrayListOf<String>()
    private fun initShardOrder(
        holder: RecyclerViewHolder,
        position: Int,
        data: HomeLiveTwentyNewsResponse?
    ) {
        try {
            GlideUtil.loadCircleImage(
                context,
                data?.avatar,
                holder.getImageView(R.id.imgOrderUserPhoto),
                true
            )
            val result = JSONObject(data?.orders?.toString() ?: "未知")
            val isBalance  = result.getString("is_bl_play")
            holder.text(R.id.tvOrderName, result.getString("lottery_cid"))
            holder.text(R.id.tvOrderIssIue, result.getString("play_bet_issue") + "期")
            if (!followList.contains(result.getString("play_bet_issue") + "," + position)) {
                followList.add(result.getString("play_bet_issue") + "," + position)
            }
            val arrayList = JsonUtils.fromJson(result.getString("order_detail"), Array<BetShareBean>::class.java)
            holder.text(R.id.tv_t1, arrayList[0].play_sec_cname)
            holder.text(R.id.tv_t2, arrayList[0].play_class_cname)
            holder.text(R.id.tv_t3, arrayList[0].play_odds.toString())
            if (isBalance == "0") {
                holder.text(R.id.tv_t4, arrayList[0].play_bet_sum + " 钻石")
            } else holder.text(R.id.tv_t4, arrayList[0].play_bet_sum + " 余额")
            if (arrayList.size > 1) {
                ViewUtils.setVisible(holder.findView(R.id.tvShowMore))
                addOrderText(holder.findViewById(R.id.layoutParen), arrayList,(isBalance == "0")) //添加展开的文字
            } else ViewUtils.setGone(holder.findView(R.id.tvShowMore))
            if (data?.canFollow == true) {
                holder.findViewById<AppCompatButton>(R.id.btFollow).isEnabled = true
                holder.findViewById<AppCompatButton>(R.id.btFollow).background =
                    ViewUtils.getDrawable(R.drawable.button_red_background)
                holder.findViewById<AppCompatButton>(R.id.btFollow)
                    .setTextColor(ViewUtils.getColor(R.color.white))
            } else {
                holder.findViewById<AppCompatButton>(R.id.btFollow).isEnabled = false
                holder.findViewById<AppCompatButton>(R.id.btFollow).background =
                    ViewUtils.getDrawable(R.drawable.button_grey_background)
                holder.findViewById<AppCompatButton>(R.id.btFollow)
                    .setTextColor(ViewUtils.getColor(R.color.color_999999))
            }
            holder.click(R.id.tvShowMore) {
                val bt = holder.findViewById<ExpandableLayout>(R.id.betLin)
                if (data?.childIsShow == true) {
                    data.childIsShow = false
                    bt.setExpanded(false, true)
                    holder.text(R.id.tvShowMore, "展开")
                    holder.findViewById<TextView>(R.id.tvShowMore).setCompoundDrawablesWithIntrinsicBounds(null, null, ViewUtils.getDrawable(R.mipmap.ic_order_down), null)
                } else {
                    data?.childIsShow = true
                    bt.setExpanded(true, true)
                    holder.text(R.id.tvShowMore, "收起")
                    holder.findViewById<TextView>(R.id.tvShowMore).setCompoundDrawablesWithIntrinsicBounds(null, null, ViewUtils.getDrawable(R.mipmap.ic_order_up), null)
                }
            }
            holder.click(R.id.btFollow){
                betFollow(data)
            }
        } catch (e: Exception) {
            e.printStackTrace()
//            ToastUtils.showToast(e.toString())
        }
    }

    @SuppressLint("SetTextI18n")
    private fun addOrderText(container: LinearLayout, data: Array<BetShareBean>, boolean: Boolean) {
        if (!data.isNullOrEmpty()) {
            container.removeAllViews()
            for (item in data.toMutableList().subList(1,data.size)) {
                val linearLayout = LinearLayout(context)
                linearLayout.orientation = LinearLayout.HORIZONTAL
                repeat(4) {
                    val textView = AppCompatTextView(context)
                    textView.gravity = Gravity.CENTER
                    textView.textSize = 12F
                    textView.setTextColor(ViewUtils.getColor(R.color.color_333333))
                    when (it) {
                        0 -> textView.text = item.play_sec_cname
                        1 -> textView.text = item.play_class_cname
                        2 -> textView.text = item.play_odds.toString().trim()
                        3 -> {
                            if (boolean){
                                textView.text = item.play_bet_sum +" 钻石"
                            }else  textView.text = item.play_bet_sum +" 余额"
                        }
                    }
                    textView.layoutParams = LinearLayout.LayoutParams(0,ViewUtils.dp2px(20), 1f)
                    linearLayout.addView(textView)
                }
               container.addView(linearLayout)
            }
        }
    }

    //跟投
    private fun betFollow(data: HomeLiveTwentyNewsResponse?) {
        if (!FastClickUtil.isFastClick()) {
            if (!UserInfoSp.getIsLogin()) {
                GlobalDialog.notLogged(context as Activity)
                return
            }
            val result = JSONObject(data?.orders.toString())
            val array = JsonUtils.fromJson(result.getString("order_detail"), Array<BetShareBean>::class.java)
            val arrayList = arrayListOf<LotteryBet>()
            var totalMoney = BigDecimal(0)
            for (res in array) {
                arrayList.add(LotteryBet(
                    PlaySecData(play_class_cname = res.play_class_cname, play_class_id = 0, play_sec_name = res.play_sec_name,
                    play_class_name = res.play_class_name, play_odds = res.play_odds, money = res.play_bet_sum), res.play_sec_cname.toString()
                ))
                totalMoney = BigDecimal(res.play_bet_sum?:"0").add(totalMoney)
            }
            val liveRoomBetAccessFragment = LiveRoomBetAccessFragment.newInstance(
                LotteryBetAccess(arrayList, 1, totalMoney.toInt(), result.getString("play_bet_lottery_id"),
                result.getString("play_bet_issue"), "0x11123", result.getString("lottery_cid"), "", true,
                    followUserId = data?.user_id.toString(), isBalanceBet = (result.getString("is_bl_play") == "1"))
            )
            liveRoomBetAccessFragment.show(fragmentManager, "liveRoomBetAccessFragment")
        }
    }
}