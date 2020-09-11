package com.home.children

import android.graphics.Typeface
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.customer.ApiRouter
import com.customer.component.WaveView
import com.customer.component.dialog.GlobalDialog
import com.customer.data.AnchorAttention
import com.customer.data.UserInfoSp
import com.customer.data.home.HomeApi
import com.customer.data.home.HomeLiveAdvance
import com.customer.data.home.HomeLivePreResponse
import com.customer.utils.JsonUtils
import com.glide.GlideUtil
import com.home.R
import com.hwangjr.rxbus.RxBus
import com.lib.basiclib.base.activity.BaseNavActivity
import com.lib.basiclib.base.recycle.BaseRecyclerAdapter
import com.lib.basiclib.base.recycle.RecyclerViewHolder
import com.lib.basiclib.base.round.RoundLinearLayout
import com.lib.basiclib.base.round.RoundRelativeLayout
import com.lib.basiclib.utils.FastClickUtil
import com.lib.basiclib.utils.ToastUtils
import com.lib.basiclib.utils.ViewUtils
import com.xiaojinzi.component.impl.Router
import kotlinx.android.synthetic.main.act_live_pre.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/26
 * @ Describe 直播预告
 *
 */
class LivePreAct : BaseNavActivity() {

    private lateinit var headTitleAdapter: HeadTitleAdapter

    private lateinit var contentAdapter: LiveRoomAdvanceAdapter

    var contentAid = ""

    override fun getContentResID() = R.layout.act_live_pre

    override fun isShowBackIconWhite() = false

    override fun getPageTitle() = "直播预告"

    override fun isSwipeBackEnable() = true


    override fun initContentView() {
        smartContent.setEnableOverScrollBounce(true)//是否启用越界回弹
        smartContent.setEnableOverScrollDrag(true)//是否启用越界拖动（仿苹果效果）
        smartContent.setEnableRefresh(false)//是否启用下拉刷新功能
        smartContent.setEnableLoadMore(false)//是否启用上拉加载功能
        headTitleAdapter = HeadTitleAdapter()
        recyclerViewTitle.adapter = headTitleAdapter
        recyclerViewTitle.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        contentAdapter = LiveRoomAdvanceAdapter()
        recyclerViewContent.adapter = contentAdapter
        recyclerViewContent.layoutManager = GridLayoutManager(this, 2)
    }

    override fun initData() {
        getPreTitle()
        getContent(contentAid)
    }

    private fun getPreTitle() {
        HomeApi.getAdvanceTitle {
            onSuccess { initTitle(it) }
            onFailed { ToastUtils.showToast(it.getMsg().toString()) }

        }
    }


    private fun getContent(type: String) {
        setVisible(holder)
        HomeApi.getLiveAdvanceList(type = type) {
            onSuccess {
                setGone(holder)
                setGone(emptyHolder)
                if (it.data.toString().length > 10) {
                    val dataList = ArrayList<HomeLivePreResponse>()
                    //获取到map，取值
                    for (entry in (it.data!!.asJsonObject.entrySet())) {
                        val bean =
                            JsonUtils.fromJson(entry.value, Array<HomeLivePreResponse>::class.java)
                        for ((pos, result) in bean.withIndex()) {
                            when {
                                bean.size == 1 -> {
                                    dataList.add(HomeLivePreResponse(date = entry.key, type = 1))
                                    dataList.add(HomeLivePreResponse(date = "", type = 1))
                                    dataList.add(result)
                                }
                                pos < bean.size - 1 -> {
                                    if (pos == 0) {
                                        dataList.add(
                                            HomeLivePreResponse(
                                                date = entry.key,
                                                type = 1
                                            )
                                        )
                                        dataList.add(HomeLivePreResponse(date = "", type = 1))
                                    }
                                    dataList.add(result)
                                }
                                bean.size % 2 == 0 -> {
                                    dataList.add(result)
                                }
                                else -> {
                                    dataList.add(result)
                                    dataList.add(HomeLivePreResponse(date = "", type = 1))
                                }
                            }
                        }
                    }
                    contentAdapter.refresh(dataList)
                } else {
                    contentAdapter.clear()
                    setVisible(emptyHolder)
                }
            }
            onFailed {
                setGone(holder)
            }
        }
    }

    private fun initTitle(data: MutableList<HomeLiveAdvance>) {
        data.add(0, HomeLiveAdvance("", "全部"))
        headTitleAdapter.refresh(data)
    }

    inner class HeadTitleAdapter : BaseRecyclerAdapter<HomeLiveAdvance>() {

        var clickPosition: Int = 0

        override fun getItemLayoutId(viewType: Int) = R.layout.adapter_pre_type

        override fun bindData(holder: RecyclerViewHolder, position: Int, data: HomeLiveAdvance?) {
            if (clickPosition == position) {
                holder.findViewById<TextView>(R.id.tvLotteryType)
                    .setTextColor(ViewUtils.getColor(R.color.white))
                holder.findViewById<TextView>(R.id.tvLotteryType)
                    .setBackgroundResource(R.drawable.button_red_background)
                holder.findViewById<TextView>(R.id.tvLotteryType).typeface =
                    Typeface.defaultFromStyle(Typeface.BOLD)
            } else {
                holder.findViewById<TextView>(R.id.tvLotteryType)
                    .setTextColor(ViewUtils.getColor(R.color.color_999999))
                holder.findViewById<TextView>(R.id.tvLotteryType)
                    .setBackgroundResource(R.drawable.button_grey_background)
                holder.findViewById<TextView>(R.id.tvLotteryType).typeface =
                    Typeface.defaultFromStyle(Typeface.NORMAL)
            }
            holder.text(R.id.tvLotteryType, data?.name)

            holder.itemView.setOnClickListener {
                changeBackground(position)
                getContent(data?.room_id ?: "")
                contentAid = data?.room_id ?: ""
            }
        }

        private fun changeBackground(position: Int) {
            clickPosition = position
            notifyDataSetChanged()
        }
    }

    inner class LiveRoomAdvanceAdapter : BaseRecyclerAdapter<HomeLivePreResponse>() {
        private val TYPE_TITLE = 1
        private val TYPE_CONTENT = 2
        override fun getItemLayoutId(viewType: Int): Int {
            return if (viewType == TYPE_TITLE) {
                R.layout.adapter_live_pre_title
            } else R.layout.adapter__live_pre_content
        }

        override fun getItemViewType(position: Int): Int {
            return if (data?.get(position)?.type == 1) {
                TYPE_TITLE
            } else {
                TYPE_CONTENT
            }
        }


        override fun bindData(
            holder: RecyclerViewHolder,
            position: Int,
            data: HomeLivePreResponse?
        ) {
            if (getItemViewType(position) == TYPE_TITLE) {
                holder.text(R.id.tvAdvanceTitle, data?.date)
            } else if (getItemViewType(position) == TYPE_CONTENT) {
                val layoutPar =
                    holder.findViewById<RoundRelativeLayout>(R.id.advanceRootView).layoutParams
                layoutPar.width = ViewUtils.getScreenWidth() / 2 - 50
                holder.findViewById<RoundRelativeLayout>(R.id.advanceRootView).layoutParams =
                    layoutPar
                GlideUtil.loadCircleImage(
                    this@LivePreAct,
                    data?.avatar,
                    holder.getImageView(R.id.imgLiveRoomAdvanceUserPhoto),
                    true
                )
                holder.text(R.id.tvLiveRoomAdvanceUserName, data?.nickname)
                holder.text(R.id.tvLiveRoomAdvanceUserType, data?.name)
                holder.text(R.id.tvLiveRoomAdvanceUserTime, data?.starttime + "~" + data?.endtime)
                if (data?.isFollow == true) {
                    holder.findViewById<RoundLinearLayout>(R.id.rlAdvanceAttention).delegate.backgroundColor =
                        ViewUtils.getColor(R.color.color_EEEEEE)
                    holder.getImageView(R.id.imgAdvanceAttention)
                        .setImageResource(R.mipmap.ic_right_dui)
                } else {
                    holder.findViewById<RoundLinearLayout>(R.id.rlAdvanceAttention).delegate.backgroundColor =
                        ViewUtils.getColor(R.color.color_FF513E)
                    holder.getImageView(R.id.imgAdvanceAttention).setImageResource(R.mipmap.ic_add)
                }
                if (data?.livestatus == "1") {
                    holder.findViewById<WaveView>(R.id.circleWave).setInitialRadius(40f)
                    holder.findViewById<WaveView>(R.id.circleWave).start()
                }


                holder.click(R.id.rlAdvanceAttention) {
                    if (!FastClickUtil.isFastClick()) {
                        if (!UserInfoSp.getIsLogin()) {
                            GlobalDialog.notLogged(this@LivePreAct)
                            return@click
                        }
                        HomeApi.attentionAnchorOrUser(data?.aid.toString(),""){
                            onSuccess {atten ->
                                if (!atten.isFollow) {
                                    holder.findViewById<RoundLinearLayout>(R.id.rlAdvanceAttention).delegate.backgroundColor = ViewUtils.getColor(R.color.color_FF513E)
                                    holder.getImageView(R.id.imgAdvanceAttention).setImageResource( R.mipmap.ic_add)
                                    data?.isFollow = false
                                    RxBus.get().post(AnchorAttention(data?.aid ?:"0",false ))
                                } else {
                                    holder.findViewById<RoundLinearLayout>(R.id.rlAdvanceAttention).delegate.backgroundColor =
                                        ViewUtils.getColor(R.color.color_EEEEEE)
                                    holder.getImageView(R.id.imgAdvanceAttention).setImageResource( R.mipmap.ic_right_dui)
                                    data?.isFollow = true
                                    RxBus.get().post(AnchorAttention(data?.aid ?:"0",true))
                                }
                                notifyItemChanged(position)
                            }
                            onFailed {
                                ToastUtils.showToast("关注失败")
                            }
                        }
                    } else ToastUtils.showToast("请勿重复点击")
                }

                holder.click(R.id.imgLiveRoomAdvanceUserPhoto) {
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
                    }else Router.withApi(ApiRouter::class.java).toAnchorPage(data?.aid ?: "-1")
                }
            }
        }

    }
}