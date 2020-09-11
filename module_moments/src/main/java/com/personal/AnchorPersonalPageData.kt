package com.personal

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.customer.data.home.HomeLiveAnchorLiveRecordBean
import com.customer.data.home.HomeLiveAnchorTagListBean
import com.customer.data.moments.AnchorPageInfoBean
import com.customer.data.moments.UserPageGift
import com.glide.GlideUtil
import com.lib.basiclib.base.fragment.BaseContentFragment
import com.lib.basiclib.base.recycle.BaseRecyclerAdapter
import com.lib.basiclib.base.recycle.RecyclerViewHolder
import com.lib.basiclib.base.round.RoundTextView
import com.lib.basiclib.utils.TimeUtils
import com.lib.basiclib.utils.ViewUtils
import com.moment.R
import kotlinx.android.synthetic.main.child_fragment_anchor_data.*
import kotlin.random.Random

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/27
 * @ Describe
 *
 */
class AnchorPersonalPageData : BaseContentFragment() {

    lateinit var data: AnchorPageInfoBean
    private lateinit var mHomeAnchorTagAdapter: HomeAnchorTagAdapter
    private lateinit var mHomeAnchorLiveRecordAdapter: HomeAnchorLiveRecordAdapter


    override fun getContentResID() = R.layout.child_fragment_anchor_data

    override fun isSwipeBackEnable() = false

    override fun initContentView() {
        data = arguments?.getSerializable("anchorData") as AnchorPageInfoBean
        initAnchorTagAdapter()
        initAnchorLiveRecordAdapter()
    }

    override fun initData() {
        if (data.lottery.isNotEmpty()) {
            val sb = StringBuffer()
            data.lottery.forEach {
                sb.append(it.name + "   ")
            }
            setText(R.id.tvAnchorGame, sb.toString())
        }
        if (data.tagList.isNotEmpty()) mHomeAnchorTagAdapter.refresh(data.tagList)
        if (data.giftList.isNotEmpty())
            initAnchorGiftAdapter(data.giftList)
        else {
            setVisible(tvNotReceiveGift)
        }
        setText(R.id.tvAnchorDate, data.duration + "分钟")
        setText(
            R.id.tvAnchorOpenDate,
            TimeUtils.longToDateString(data.liveStartTime) + "-" + TimeUtils.longToDateString(data.liveEndTime)
        )
        setText(R.id.anchorGiftNumber, "共 " + data.giftNum + " 件")
        if (data.live_record.isNotEmpty())
            mHomeAnchorLiveRecordAdapter.refresh(data.live_record)
        else {
            setGone(liveRecordingRecycler)
            setVisible(tvNotLiveReceive)
        }
    }


    private fun initAnchorLiveRecordAdapter() {
        mHomeAnchorLiveRecordAdapter = HomeAnchorLiveRecordAdapter()
        liveRecordingRecycler.adapter = mHomeAnchorLiveRecordAdapter
        val linearLayoutManager = LinearLayoutManager(getPageActivity())
        liveRecordingRecycler.layoutManager = linearLayoutManager
    }

    private fun initAnchorGiftAdapter(giftList: List<UserPageGift>) {
        val color = arrayOf("#FFEFED", "#FFF4E3", "#E9F8FF", "#E9F8FF", "#FFF4E3", "#FFEFED")
        if (!giftList.isNullOrEmpty()) {
            //往容器内添加TextView数据
            val layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            layoutParams.setMargins(15, 20, 15, 5)
            if (flowLayoutAnchor != null) {
                flowLayoutAnchor.removeAllViews()
            }
            for (i in giftList) {
                val tv = RoundTextView(getPageActivity())
                tv.setPadding(28, 10, 28, 10)
                val builder = SpannableStringBuilder(i.gift_name + "  ")
                val length = builder.length
                builder.append("x" + i.num)
                builder.setSpan(
                    ForegroundColorSpan(ViewUtils.getColor(R.color.color_FF513E)),
                    length,
                    builder.length,
                    Spannable.SPAN_EXCLUSIVE_INCLUSIVE
                )
                tv.text = builder
                tv.maxEms = 10
                tv.setSingleLine()
                tv.textSize = 12f
                tv.setTextColor(ViewUtils.getColor(R.color.color_333333))
                tv.delegate.cornerRadius = 15
                tv.layoutParams = layoutParams
                tv.delegate.backgroundColor = Color.parseColor(color[Random.nextInt(5)])
                flowLayoutAnchor.addView(tv, layoutParams)
            }
        }
    }

    private fun initAnchorTagAdapter() {
        mHomeAnchorTagAdapter = HomeAnchorTagAdapter()
        anchorTagRecyclerView.adapter = mHomeAnchorTagAdapter
        val linearLayoutManager = LinearLayoutManager(getPageActivity())
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        anchorTagRecyclerView.layoutManager = linearLayoutManager
    }


    /**
     *  desc   : 主播信息直播记录适配器
     */
    inner class HomeAnchorLiveRecordAdapter : BaseRecyclerAdapter<HomeLiveAnchorLiveRecordBean>() {
        override fun getItemLayoutId(viewType: Int) = R.layout.adapter_anchor_live_record
        override fun bindData(
            holder: RecyclerViewHolder,
            position: Int,
            data: HomeLiveAnchorLiveRecordBean?
        ) {
            holder.text(R.id.tvAnchorRecordGameName, data?.name + ":")
            holder.text(R.id.tvAnchorRecordStatus, data?.tip)
            holder.text(R.id.tvAnchorRecordTime, data?.startTimeTxt)
        }

    }


    /**
     *  desc   : 主播信息标签适配器
     */
    inner class HomeAnchorTagAdapter : BaseRecyclerAdapter<HomeLiveAnchorTagListBean>() {
        override fun getItemLayoutId(viewType: Int) = R.layout.adapter_anchor_tag
        override fun bindData(
            holder: RecyclerViewHolder,
            position: Int,
            data: HomeLiveAnchorTagListBean?
        ) {
            holder.text(R.id.tvAnchorTag, data?.title)
            context?.let {
                GlideUtil.loadImage(
                    it,
                    data?.icon,
                    holder.getImageView(R.id.ivAnchorTag)
                )
            }
        }

    }

    companion object {
        fun newInstance(anchorId: AnchorPageInfoBean): AnchorPersonalPageData {
            val fragment = AnchorPersonalPageData()
            val bundle = Bundle()
            bundle.putSerializable("anchorData", anchorId)
            fragment.arguments = bundle
            return fragment
        }
    }
}