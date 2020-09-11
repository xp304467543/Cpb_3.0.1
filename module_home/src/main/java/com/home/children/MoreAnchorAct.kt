package com.home.children

import android.graphics.Typeface
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.customer.data.HomeJumpToMine
import com.customer.data.JumpToBuyLottery
import com.customer.utils.JsonUtils
import com.home.R
import com.home.adapter.HomeHotLiveAdapter
import com.customer.data.home.HomeApi
import com.customer.data.home.HomeHotLiveResponse
import com.customer.data.home.HomeLiveAnchor
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.thread.EventThread
import com.lib.basiclib.base.activity.BaseNavActivity
import com.lib.basiclib.base.recycle.BaseRecyclerAdapter
import com.lib.basiclib.base.recycle.RecyclerViewHolder
import com.lib.basiclib.utils.ToastUtils
import com.lib.basiclib.utils.ViewUtils
import kotlinx.android.synthetic.main.act_all_anchor.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/26
 * @ Describe 更多主播
 *
 */
class MoreAnchorAct : BaseNavActivity() {

    var page = 1
    var type = "0"

    private var initTitle:Boolean = true

    private lateinit var headTitleAdapter: HeadTitleAdapter
    private lateinit var contentAdapter: HomeHotLiveAdapter
    override fun getContentResID() = R.layout.act_all_anchor

    override fun isShowBackIconWhite() = false

    override fun getPageTitle() = "全部主播"

    override fun isSwipeBackEnable() = true

    override fun isRegisterRxBus() = true

    override fun initContentView() {
        smartContent.setEnableRefresh(true)//是否启用下拉刷新功能
        smartContent.setEnableLoadMore(false)//是否启用上拉加载功能
        smartContent.setEnableOverScrollBounce(true)//是否启用越界回弹
        smartContent.setEnableOverScrollDrag(true)//是否启用越界拖动（仿苹果效果）

        headTitleAdapter = HeadTitleAdapter()
        recyclerViewTitle.adapter = headTitleAdapter
        recyclerViewTitle.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        //内容
        contentAdapter = HomeHotLiveAdapter()
        recyclerViewContent.layoutManager = GridLayoutManager(this, 2)
        recyclerViewContent.adapter = contentAdapter
        smartContent.setOnRefreshListener {
            page = 1
            getAll( isRefresh = true)
        }
        smartContent.setOnLoadMoreListener {
            page++
            getAll( isRefresh = false)
        }
    }
    override fun initData() {
        smartContent.autoRefresh()
    }


    inner class HeadTitleAdapter : BaseRecyclerAdapter<HomeLiveAnchor>() {

        private var clickPosition: Int = 0

        override fun getItemLayoutId(viewType: Int) = R.layout.adapter_pre_type

        override fun bindData(holder: RecyclerViewHolder, position: Int, data: HomeLiveAnchor?) {
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
                page = 1
                type = data?.type.toString()
                showPageLoadingDialog()
                getAll( isRefresh = true)
            }
        }

        private fun changeBackground(position: Int) {
            clickPosition = position
            notifyDataSetChanged()
        }
    }

    private fun initTitle(data: Array<HomeLiveAnchor>) {
        headTitleAdapter.refresh(data)
    }

    private fun initAdvanceRecycle(data: Array<HomeHotLiveResponse>, isRefresh: Boolean) {
        if (data.isEmpty()) {
            if (isRefresh) {
                setVisible(emptyHolder)
                smartContent.finishRefreshWithNoMoreData()
            }else smartContent.finishLoadMoreWithNoMoreData()
        } else {
            setGone(emptyHolder)
            if (isRefresh) contentAdapter.refresh(data) else contentAdapter.loadMore(data)
        }
    }

    fun getAll(  isRefresh: Boolean) {
        HomeApi.getAllAnchor(page, type) {
            onSuccess {
                smartContent.setEnableLoadMore(true)
                if (initTitle && it.typeList.toString().length > 10) {
                    val bean = it.typeList?.let { it1 -> JsonUtils.fromJson(it1, Array<HomeLiveAnchor>::class.java) }
                    bean?.let { it1 -> initTitle(it1) }
                    initTitle = false
                }
                if (it.data.toString().length > 10) {
                    val content = it.data?.let { it1 -> JsonUtils.fromJson(it1, Array<HomeHotLiveResponse>::class.java) }
                    content?.let { it1 -> initAdvanceRecycle(it1,isRefresh) }
                }
                hidePageLoadingDialog()
                smartContent.finishRefresh()
                smartContent.finishLoadMore()
            }
            onFailed {
                hidePageLoadingDialog()
                ToastUtils.showToast(it.getMsg().toString())
            }
        }
    }

    /**
     * 跳转购彩
     */
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun jumpToBuyLottery(eventBean: JumpToBuyLottery) {
        finish()
    }

    /**
     * 跳转mine
     */
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun onClickMine(clickMine: HomeJumpToMine) {
        finish()
    }
}