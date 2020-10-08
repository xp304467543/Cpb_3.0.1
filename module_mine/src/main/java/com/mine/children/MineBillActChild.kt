package com.mine.children

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import com.customer.data.UserInfoSp
import com.customer.data.mine.MineApi
import com.customer.data.mine.MineBillBean
import com.customer.utils.JsonUtils
import com.glide.GlideUtil
import com.lib.basiclib.base.fragment.BaseContentFragment
import com.lib.basiclib.base.recycle.BaseRecyclerAdapter
import com.lib.basiclib.base.recycle.RecyclerViewHolder
import com.lib.basiclib.base.xui.adapter.recyclerview.XLinearLayoutManager
import com.lib.basiclib.utils.ToastUtils
import com.lib.basiclib.utils.ViewUtils
import com.mine.R
import cuntomer.bean.BaseApiBean
import cuntomer.them.AppMode
import kotlinx.android.synthetic.main.fragment_bill_child.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/24
 * @ Describe
 *
 */
class MineBillActChild : BaseContentFragment() {

    var selectType = 0

    var page = 1

    private var isBal = "1"  //是否是余额

    var adapter0: Adapter0? = null

    var adapter1: Adapter1? = null

    var adapter2: Adapter2? = null

    var adapter3: Adapter3? = null

    override fun getContentResID() = R.layout.fragment_bill_child

    override fun isSwipeBackEnable() = false

    override fun initContentView() {
        smartRefreshLayoutChildBill?.setEnableRefresh(true)//是否启用下拉刷新功能
        smartRefreshLayoutChildBill?.setEnableLoadMore(false)//是否启用上拉加载功能
        smartRefreshLayoutChildBill?.setEnableOverScrollBounce(true)//是否启用越界回弹
        smartRefreshLayoutChildBill?.setEnableOverScrollDrag(true)//是否启用越界拖动（仿苹果效果）
        rvBill.layoutManager = XLinearLayoutManager(context)
        selectType = arguments?.getInt("typeSelect") ?: 0
        if (selectType == 1 && UserInfoSp.getAppMode() == AppMode.Normal) setVisible(topLin)
        when (selectType) {
            0 -> {
                adapter0 = Adapter0()
                rvBill.adapter = adapter0
            }
            1 -> {
                adapter1 = Adapter1()
                rvBill.adapter = adapter1
            }
            2 -> {
                adapter2 = Adapter2()
                rvBill.adapter = adapter2
            }
            3 -> {
                adapter3 = Adapter3()
                rvBill.adapter = adapter3
            }
        }
    }

    override fun lazyInit() {
        smartRefreshLayoutChildBill?.autoRefresh()
    }

    override fun initEvent() {
        tv_start.setOnClickListener {
            page = 1
            isBal = "1"
            tv_start.delegate.backgroundColor = ViewUtils.getColor(R.color.color_FF513E)
            tv_start.setTextColor(ViewUtils.getColor(R.color.white))
            tv_end.delegate.backgroundColor = ViewUtils.getColor(R.color.white)
            tv_end.setTextColor(ViewUtils.getColor(R.color.color_999999))
            betRecord()
        }
        tv_end.setOnClickListener {
            page = 1
            isBal = "0"
            tv_end.delegate.backgroundColor = ViewUtils.getColor(R.color.color_FF513E)
            tv_end.setTextColor(ViewUtils.getColor(R.color.white))
            tv_start.delegate.backgroundColor = ViewUtils.getColor(R.color.white)
            tv_start.setTextColor(ViewUtils.getColor(R.color.color_999999))
            betRecord()
        }
        smartRefreshLayoutChildBill?.setOnRefreshListener {
            page = 1
            when (selectType) {
                0 -> {
                    getBalance()
                }
                1 -> {
                    betRecord()
                }
                2 -> {
                    getReward()
                }
                3 -> {
                    getChange()
                }
            }
        }
        smartRefreshLayoutChildBill?.setOnLoadMoreListener {
            page++
            when (selectType) {
                0 -> {
                    getBalance()
                }
                1 -> {
                    betRecord()
                }
                2 -> {
                    getReward()
                }
                3 -> {
                    getChange()
                }
            }
        }
    }


    private fun getBalance() {
        MineApi.getBalance(page) {
            onSuccess {
              if (smartRefreshLayoutChildBill!=null)  smartRefreshLayoutChildBill?.setEnableLoadMore(true)
                val data = parseResult(it)
                if (!data.isNullOrEmpty()) {
                    if (page == 1) adapter0?.refresh(data) else adapter0?.loadMore(data)
                } else {
                    if (page == 1) {
                        setVisible(tvHolder)
                        smartRefreshLayoutChildBill?.finishRefreshWithNoMoreData()
                    } else smartRefreshLayoutChildBill?.finishLoadMoreWithNoMoreData()
                }

                smartRefreshLayoutChildBill?.finishRefresh()
                smartRefreshLayoutChildBill?.finishLoadMore()
            }
            onFailed {
                ToastUtils.showToast(it.getMsg())
                smartRefreshLayoutChildBill?.finishRefresh()
                smartRefreshLayoutChildBill?.finishLoadMore()
            }
        }
    }

    private fun betRecord() {
        MineApi.betRecord(page, isBal) {
            onSuccess {
                setGone(tvHolder)
                adapter1?.clear()
                smartRefreshLayoutChildBill?.setEnableRefresh(true)
                smartRefreshLayoutChildBill?.setEnableLoadMore(true)
                val data = parseResult(it)
                if (!data.isNullOrEmpty()) {
                    if (page == 1) adapter1?.refresh(data) else adapter1?.loadMore(data)
                } else {
                    if (page == 1) {
                        setVisible(tvHolder)
                        smartRefreshLayoutChildBill?.finishRefreshWithNoMoreData()
                    } else smartRefreshLayoutChildBill?.finishLoadMoreWithNoMoreData()
                }

                smartRefreshLayoutChildBill?.finishRefresh()
                smartRefreshLayoutChildBill?.finishLoadMore()
            }
            onFailed {
                ToastUtils.showToast(it.getMsg())
                smartRefreshLayoutChildBill?.finishRefresh()
                smartRefreshLayoutChildBill?.finishLoadMore()
            }
        }
    }

    private fun getReward() {
        MineApi.getReward(page) {
            onSuccess {
                smartRefreshLayoutChildBill?.setEnableRefresh(true)
                smartRefreshLayoutChildBill?.setEnableLoadMore(true)
                val data = parseResult(it)
                if (!data.isNullOrEmpty()) {
                    if (page == 1) adapter2?.refresh(data) else adapter2?.loadMore(data)
                } else {
                    if (page == 1) {
                        setVisible(tvHolder)
                        smartRefreshLayoutChildBill?.finishRefreshWithNoMoreData()
                    } else smartRefreshLayoutChildBill?.finishLoadMoreWithNoMoreData()
                }
                smartRefreshLayoutChildBill?.finishRefresh()
                smartRefreshLayoutChildBill?.finishLoadMore()
            }
            onFailed {
                ToastUtils.showToast(it.getMsg())
                smartRefreshLayoutChildBill?.finishRefresh()
                smartRefreshLayoutChildBill?.finishLoadMore()
            }
        }
    }

    private fun getChange() {
        MineApi.getChange(page) {
            onSuccess {
                smartRefreshLayoutChildBill?.setEnableRefresh(true)
                smartRefreshLayoutChildBill?.setEnableLoadMore(true)
                val data = parseResult(it)
                if (!data.isNullOrEmpty()) {
                    if (page == 1) adapter3?.refresh(data) else adapter3?.loadMore(data)
                } else {
                    if (page == 1) {
                        setVisible(tvHolder)
                        smartRefreshLayoutChildBill?.finishRefreshWithNoMoreData()
                    } else smartRefreshLayoutChildBill?.finishLoadMoreWithNoMoreData()
                }

                smartRefreshLayoutChildBill?.finishRefresh()
                smartRefreshLayoutChildBill?.finishLoadMore()
            }
            onFailed {
                ToastUtils.showToast(it.getMsg())
                smartRefreshLayoutChildBill?.finishRefresh()
                smartRefreshLayoutChildBill?.finishLoadMore()
            }
        }
    }


    /**
     * 余额记录 adapter
     */
    inner class Adapter0 : BaseRecyclerAdapter<MineBillBean>() {

        private val TYPE_TITLE = 1
        private val TYPE_CONTENT = 2

        override fun getItemLayoutId(viewType: Int): Int {
            return if (viewType == TYPE_TITLE) {
                R.layout.adapter_mine_bill_title
            } else R.layout.adapter_mine_bill
        }


        override fun getItemViewType(position: Int): Int {
            return if (data?.get(position)?.itemType == 1) {
                TYPE_TITLE
            } else TYPE_CONTENT
        }

        override fun bindData(holder: RecyclerViewHolder, position: Int, data: MineBillBean?) {
            if (getItemViewType(position) == TYPE_TITLE) {
                holder.text(R.id.tvBillTile, data?.date)
            } else if (getItemViewType(position) == TYPE_CONTENT) {
                holder.text(R.id.tvTime, data?.time)
                when (data?.type) {
                    "0" -> {
                        holder.text(R.id.tvName, "存款")
                        holder.text(R.id.tvEnd, "+ " + data.amount + " 元")
                        holder.getImageView(R.id.imgPhoto).setImageResource(R.mipmap.ic_save)
                    }
                    "1" -> {
                        holder.text(R.id.tvName, "提现")
                        holder.text(R.id.tvEnd, "- " + data.amount + " 元")
                        holder.getImageView(R.id.imgPhoto).setImageResource(R.mipmap.ic_tixian)
                    }
                    "2" -> {
                        holder.text(R.id.tvName, "兑换")
                        holder.text(R.id.tvEnd, "- " + data.amount + " 元")
                        holder.getImageView(R.id.imgPhoto).setImageResource(R.mipmap.ic_change)
                    }
                    "3" -> {
                        holder.text(R.id.tvName, "抢红包")
                        holder.text(R.id.tvEnd, "+ " + data.amount + " 元")
                        holder.getImageView(R.id.imgPhoto)
                            .setImageResource(R.mipmap.ic_qianghoongbao)
                    }
                    "4" -> {
                        holder.text(R.id.tvName, "发红包")
                        holder.text(R.id.tvEnd, "- " + data.amount + " 元")
                        holder.getImageView(R.id.imgPhoto).setImageResource(R.mipmap.ic_fahongbao)
                    }
                    "5" -> {
                        holder.text(R.id.tvName, "会员返佣")
                        holder.text(R.id.tvEnd, "+ " + data.amount + " 元")
                        holder.getImageView(R.id.imgPhoto).setImageResource(R.mipmap.ic_fanyong)
                    }
                    "6" -> {
                        holder.text(R.id.tvName, "下级返佣")
                        holder.text(R.id.tvEnd, "+ " + data.amount + " 元")
                        holder.getImageView(R.id.imgPhoto).setImageResource(R.mipmap.ic_fanyong_2)
                    }
                    "7" -> {
                        holder.text(R.id.tvName, "邀请返佣")
                        holder.text(R.id.tvEnd, "+ " + data.amount + " 元")
                        holder.getImageView(R.id.imgPhoto).setImageResource(R.mipmap.ic_fanyong_1)
                    }
                    "8" -> {
                        holder.text(R.id.tvName, "彩金")
                        holder.text(R.id.tvEnd, "+ " + data.amount + " 元")
                        holder.getImageView(R.id.imgPhoto).setImageResource(R.mipmap.ic_caijin)
                    }
                }
            }
        }

    }

    /**
     * 投注记录 adapter
     */
    inner class Adapter1 : BaseRecyclerAdapter<MineBillBean>() {

        private val TYPE_TITLE = 1
        private val TYPE_CONTENT = 2

        override fun getItemLayoutId(viewType: Int): Int {
            return if (viewType == TYPE_TITLE) {
                R.layout.adapter_mine_bill_title
            } else R.layout.adapter_mine_bill
        }


        override fun getItemViewType(position: Int): Int {
            return if (data?.get(position)?.itemType == 1) {
                TYPE_TITLE
            } else TYPE_CONTENT
        }

        override fun bindData(holder: RecyclerViewHolder, position: Int, data: MineBillBean?) {
            if (getItemViewType(position) == TYPE_TITLE) {
                holder.text(R.id.tvBillTile, data?.date)
            } else if (getItemViewType(position) == TYPE_CONTENT) {
                holder.text(R.id.tvTime, data?.issue + "   " + data?.time)
                holder.text(R.id.tvName, data?.lottery_name)
                holder.text(
                    R.id.tvGiftName,
                    data?.method_name + "  " + data?.code + "  " + data?.type
                )
                if (isBal == "0") {
                    val spannableString = SpannableString(data?.amount+" 钻石")
                    spannableString.setSpan(
                        ForegroundColorSpan(ViewUtils.getColor(R.color.color_FF513E)),
                        0,
                        data?.amount?.length?:0,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    holder.text(R.id.tvGiftPrise, spannableString)
                } else {
                    val spannableString = SpannableString(data?.amount+" 余额")
                    spannableString.setSpan(
                        ForegroundColorSpan(ViewUtils.getColor(R.color.color_333333)),
                        0,
                        data?.amount?.length?:0,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    holder.text(R.id.tvGiftPrise, spannableString)
                }
                if (data?.type == "中奖") {
                    holder.getImageView(R.id.imgPhoto).setImageResource(R.mipmap.icc_re_bet_get)
                } else holder.getImageView(R.id.imgPhoto).setImageResource(R.mipmap.ic_re_bet)
            }
        }
    }

    /**
     * 打赏记录 adapter
     */
    inner class Adapter2 : BaseRecyclerAdapter<MineBillBean>() {
        private val TYPE_TITLE = 1
        private val TYPE_CONTENT = 2
        override fun getItemLayoutId(viewType: Int): Int {
            return if (viewType == TYPE_TITLE) {
                R.layout.adapter_mine_bill_title
            } else R.layout.adapter_mine_bill
        }

        override fun getItemViewType(position: Int): Int {
            return if (data?.get(position)?.itemType == 1) {
                TYPE_TITLE
            } else TYPE_CONTENT
        }

        override fun bindData(holder: RecyclerViewHolder, position: Int, data: MineBillBean?) {
            if (getItemViewType(position) == TYPE_TITLE) {
                holder.text(R.id.tvBillTile, data?.date)
            } else if (getItemViewType(position) == TYPE_CONTENT) {
                holder.text(R.id.tvTime, data?.time)
                holder.text(R.id.tvName, data?.nickname)
                holder.text(R.id.tvGiftName, data?.giftname + " x" + data?.gift_num)
                holder.text(R.id.tvGiftPrise, "- " + data?.amount + " 钻石")
                context?.let {
                    GlideUtil.loadCircleImage(
                        it,
                        data?.avatar,
                        holder.getImageView(R.id.imgPhoto),
                        true
                    )
                }
            }
        }

    }

    /**
     * 兑换记录 adapter
     */
    inner class Adapter3 : BaseRecyclerAdapter<MineBillBean>() {
        private val TYPE_TITLE = 1
        private val TYPE_CONTENT = 2
        override fun getItemLayoutId(viewType: Int): Int {
            return if (viewType == TYPE_TITLE) {
                R.layout.adapter_mine_bill_title
            } else R.layout.adapter_mine_bill
        }

        override fun getItemViewType(position: Int): Int {
            return if (data?.get(position)?.itemType == 1) {
                TYPE_TITLE
            } else TYPE_CONTENT
        }

        override fun bindData(holder: RecyclerViewHolder, position: Int, data: MineBillBean?) {
            if (getItemViewType(position) == TYPE_TITLE) {
                holder.text(R.id.tvBillTile, data?.date)
            } else if (getItemViewType(position) == TYPE_CONTENT) {
                holder.text(R.id.tvTime, data?.time)
                if (data?.type == "5"){
                    holder.text(R.id.tvName, "新手任务")
                    holder.getImageView(R.id.imgPhoto).setImageResource(R.mipmap.ic_user_task)
                }else {
                    holder.text(R.id.tvName, "兑换")
                    holder.getImageView(R.id.imgPhoto).setImageResource(R.mipmap.ic_change)
                }

                holder.text(R.id.tvEnd, "+ " + data?.get_money + " 钻石")

            }
        }


    }


    companion object {
        fun newInstance(type: Int): MineBillActChild {
            val fragment = MineBillActChild()
            val bundle = Bundle()
            bundle.putInt("typeSelect", type)
            fragment.arguments = bundle
            return fragment
        }
    }


    private fun parseResult(result: BaseApiBean): ArrayList<MineBillBean>? {
        if (result.data.toString().length > 10) {
            val realData = arrayListOf<MineBillBean>()
            for (entry in (result.data!!.asJsonObject.entrySet())) {
                val dataList = JsonUtils.fromJson(entry.value, Array<MineBillBean>::class.java)
                for ((index, result) in dataList.withIndex()) {
                    if (index == 0) {
                        val ch = MineBillBean(
                            date = entry.key,
                            itemType = 1
                        )
                        realData.add(ch)
                    }
                    realData.add(result)
                }

            }
            return realData
        }
        return null
    }

}