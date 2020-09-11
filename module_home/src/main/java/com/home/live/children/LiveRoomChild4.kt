package com.home.live.children

import android.app.Activity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.customer.ApiRouter
import com.customer.base.BaseNormalFragment
import com.customer.component.WaveView
import com.customer.component.dialog.GlobalDialog
import com.customer.data.UserInfoSp
import com.customer.data.home.HomeApi
import com.customer.data.home.HomeLivePreResponse
import com.customer.utils.JsonUtils
import com.glide.GlideUtil
import com.home.R
import com.lib.basiclib.base.recycle.BaseRecyclerAdapter
import com.lib.basiclib.base.recycle.RecyclerViewHolder
import com.lib.basiclib.base.round.RoundLinearLayout
import com.lib.basiclib.base.round.RoundRelativeLayout
import com.lib.basiclib.utils.FastClickUtil
import com.lib.basiclib.utils.ToastUtils
import com.lib.basiclib.utils.ViewUtils
import com.xiaojinzi.component.impl.Router
import cuntomer.bean.BaseApiBean
import kotlinx.android.synthetic.main.fragmeent_live_child_4.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/26
 * @ Describe
 *
 */
class LiveRoomChild4 : BaseNormalFragment<LiveRoomChild4Presenter>() {

    var adapter: LiveRoomAdvanceAdapter? = null

    override fun attachView() = mPresenter.attachView(this)

    override fun attachPresenter() = LiveRoomChild4Presenter()

    override fun getLayoutRes() = R.layout.fragmeent_live_child_4

    override fun isRegisterRxBus() = true


    override fun initContentView() {

    }


    override fun initData() {
        mPresenter.getAllData(arguments?.getString("LIVE_ROOM_LOTTERY_TYPE", "") ?: "")
    }

    fun initAdvanceRecycle(result: BaseApiBean) {
        adapter = LiveRoomAdvanceAdapter()
        tvLiveRoomAdvance.layoutManager = GridLayoutManager(context, 2)
        tvLiveRoomAdvance.adapter = adapter
        if (result.data.toString().length > 10) {
            val dataList = ArrayList<HomeLivePreResponse>()
            //获取到map，取值
            for (entry in (result.data!!.asJsonObject.entrySet())) {
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
                                dataList.add(HomeLivePreResponse(date = entry.key, type = 1))
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
            adapter?.refresh(dataList)
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
                context?.let {
                    GlideUtil.loadCircleImage(
                        it,
                        data?.avatar,
                        holder.getImageView(R.id.imgLiveRoomAdvanceUserPhoto),
                        true
                    )
                }
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
                    if (!UserInfoSp.getIsLogin()) {
                        GlobalDialog.notLogged(context as Activity)
                        return@click
                    }
                    if (!FastClickUtil.isFastClick()) {
                        HomeApi.attentionAnchorOrUser(data?.aid.toString(),""){
                            onSuccess {
                                if (data?.isFollow == true) {
                                    data.isFollow = false
//                            RxBus.get().post(UpDateAttention(false, data.aid ?:"0"))
                                } else {
                                    data?.isFollow = true
//                            RxBus.get().post(UpDateAttention(true,data?.aid ?:"0"))
                                }
                                notifyItemChanged(position)
                            }
                            onFailed { ToastUtils.showToast("关注失败") }
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
                    } else Router.withApi(ApiRouter::class.java).toAnchorPage(data?.aid ?: "-1")
                }
            }
        }
    }


    companion object {
        fun newInstance(
            anchorId: String,
            liveState: String,
            name: String,
            id: String,
            type: String
        ): LiveRoomChild4 {
            val fragment = LiveRoomChild4()
            val bundle = Bundle()
            bundle.putString("LIVE_ROOM_ANCHOR_ID", anchorId)
            bundle.putString("LIVE_ROOM_ANCHOR_STATUE", liveState)
            bundle.putString("LIVE_ROOM_NICK_NAME", name)
            bundle.putString("LIVE_ROOM_LOTTERY_ID", id)
            bundle.putString("LIVE_ROOM_LOTTERY_TYPE", type)
            fragment.arguments = bundle
            return fragment
        }
    }

}