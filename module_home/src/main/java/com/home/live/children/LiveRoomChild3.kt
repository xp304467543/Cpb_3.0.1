package com.home.live.children

import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import com.customer.ApiRouter
import com.customer.base.BaseNormalFragment
import com.customer.data.home.HomeLiveRankList
import com.glide.GlideUtil
import com.home.R
import com.lib.basiclib.base.recycle.BaseRecyclerAdapter
import com.lib.basiclib.base.recycle.RecyclerViewHolder
import com.lib.basiclib.utils.FastClickUtil
import com.xiaojinzi.component.impl.Router
import kotlinx.android.synthetic.main.fragmeent_live_child_3.*
import java.math.BigDecimal
import java.math.RoundingMode

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/26
 * @ Describe
 *
 */
class LiveRoomChild3 : BaseNormalFragment<LiveRoomChild3Presenter>(){

    override fun attachView() = mPresenter.attachView(this)

    override fun attachPresenter() = LiveRoomChild3Presenter ()

    override fun getLayoutRes()= R.layout.fragmeent_live_child_3

    override fun isRegisterRxBus() = true


    override fun initContentView() {
    }


    override fun initData() {
        mPresenter.getAllData(arguments?.getString("LIVE_ROOM_ANCHOR_ID") ?: "0")
    }

    fun initRankRewardList(data: List<HomeLiveRankList>) {
        val adapter = LiveRoomRankAdapter()
        rvLiveRankList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvLiveRankList.adapter = adapter
        adapter.refresh(data)

    }

    inner class LiveRoomRankAdapter: BaseRecyclerAdapter<HomeLiveRankList>() {
        override fun getItemLayoutId(viewType: Int) = R.layout.adapter_live_rank

        override fun bindData(holder: RecyclerViewHolder, position: Int, data: HomeLiveRankList?) {
            when (position) {
                0 -> holder.findViewById<ImageView>(R.id.imgRankLevel).setBackgroundResource(R.mipmap.ic_live_rank_1)
                1 -> holder.findViewById<ImageView>(R.id.imgRankLevel).setBackgroundResource(R.mipmap.ic_live_rank_2)
                2 -> holder.findViewById<ImageView>(R.id.imgRankLevel).setBackgroundResource(R.mipmap.ic_live_rank_3)
                else -> holder.text(R.id.tvRankLevel, (position + 1).toString())
            }
            when (data?.vip) {
                "1" -> holder.findViewById<ImageView>(R.id.imgRankUserVipLevel).setBackgroundResource(R.mipmap.v1)
                "2" -> holder.findViewById<ImageView>(R.id.imgRankUserVipLevel).setBackgroundResource(R.mipmap.v2)
                "3" -> holder.findViewById<ImageView>(R.id.imgRankUserVipLevel).setBackgroundResource(R.mipmap.v3)
                "4" -> holder.findViewById<ImageView>(R.id.imgRankUserVipLevel).setBackgroundResource(R.mipmap.v4)
                "5" -> holder.findViewById<ImageView>(R.id.imgRankUserVipLevel).setBackgroundResource(R.mipmap.v5)
                "6" -> holder.findViewById<ImageView>(R.id.imgRankUserVipLevel).setBackgroundResource(R.mipmap.v6)
                "7" -> holder.findViewById<ImageView>(R.id.imgRankUserVipLevel).setBackgroundResource(R.mipmap.v7)
            }
            context?.let { GlideUtil.loadCircleImage(it,data?.avatar, holder.getImageView(R.id.imgRankUserPhoto),true) }
            holder.text(R.id.tvRankUserName, data?.nickname)
            val amount = BigDecimal(data?.amount)
            if (amount.compareTo(BigDecimal(10000)) == 1) {
                holder.text(R.id.tvRankRewardNum, BigDecimal(data?.amount).divide(BigDecimal(10000), 2, RoundingMode.HALF_DOWN).toString() + "万钻石")
            } else holder.text(R.id.tvRankRewardNum, data?.amount + "钻石")
            holder.click(R.id.imgRankUserPhoto){
                if (!FastClickUtil.isFastClick()){
                    Router.withApi(ApiRouter::class.java).toUserPage(data?.user_id.toString())
                }
            }
        }

    }




    companion object {
        fun newInstance(anchorId: String, liveState: String, name: String,id:String,type:String): LiveRoomChild3 {
            val fragment = LiveRoomChild3()
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