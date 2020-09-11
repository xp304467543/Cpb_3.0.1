package com.home.weight

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.customer.adapter.TabThem
import com.customer.component.recyclepage.PagerGridLayoutManager
import com.customer.component.recyclepage.PagerGridSnapHelper
import com.home.R
import com.home.adapter.HomeLotteryViewRvAdapter
import com.customer.data.home.HomeTypeListResponse
import com.customer.component.panel.IndicatorView

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/12
 * @ Describe
 *
 */
class HomeLotteryTypeView : LinearLayout {

    private var mContext: Context? = null
    private var pagerRecyclerView: RecyclerView? = null
    private var containerCircle: IndicatorView? = null
    private var currentPage = 0
    private var totalPage = 0

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context)
    }

    private fun init(context: Context) {
        this.mContext = context
        LayoutInflater.from(context).inflate(R.layout.view_lotterytypeview, this)
        containerCircle = findViewById(R.id.containerCircle)
        pagerRecyclerView = findViewById(R.id.rvView)
    }


    private var mOnItemClickListener: ((homeTypeListResponse: HomeTypeListResponse) -> Unit)? = null

    fun setOnItemClickListener(listener: (homeTypeListResponse: HomeTypeListResponse) -> Unit) {
        mOnItemClickListener = listener
    }


    fun setData(list: Array<HomeTypeListResponse>) {
        val layoutManager = PagerGridLayoutManager(2, 3, PagerGridLayoutManager.HORIZONTAL)
        pagerRecyclerView?.layoutManager = layoutManager
        val pageSnapHelper = PagerGridSnapHelper()
        pageSnapHelper.attachToRecyclerView(pagerRecyclerView)
        val adapter = HomeLotteryViewRvAdapter(context)
        pagerRecyclerView?.adapter = adapter
        adapter.refresh(list)
        adapter.setOnItemClickListener{
                _, item, _ ->
            mOnItemClickListener?.invoke(item)
        }
        totalPage = getTotalPageCount(list.size) + 1
        containerCircle?.mItemCount = totalPage
        layoutManager.setPageListener(object : PagerGridLayoutManager.PageListener {
            override fun onPageSelect(pageIndex: Int) {
                containerCircle?.mCurSelect = pageIndex
                currentPage = pageIndex
            }

            override fun onPageSizeChanged(pageSize: Int) {

            }
        })
    }

    private fun getTotalPageCount(total: Int): Int {
        if (total <= 0) return 0
        var totalCount: Int = total / 6
        if (total % total != 0) {
            totalCount++
        }
        return totalCount
    }

    var xNew = 0f
    var xOld = 0f
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                xOld = ev.x
                // 保证子View能够接收到Action_move事件
                parent.requestDisallowInterceptTouchEvent(true)
            }
            MotionEvent.ACTION_MOVE -> {
                xNew = ev.x
                if (xNew - xOld > 0) { //右
                    if (currentPage == 0) {
                        parent.requestDisallowInterceptTouchEvent(false)
                    } else parent.requestDisallowInterceptTouchEvent(true)
                } else {//左
                    if (currentPage + 1 == totalPage) {
                        parent.requestDisallowInterceptTouchEvent(false)
                    } else parent.requestDisallowInterceptTouchEvent(true)
                }
            }
            MotionEvent.ACTION_UP -> {
                xNew = 0f
                xOld = 0f
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    fun setThem(){
        containerCircle?.setSelectThem(TabThem.getTabSelect())
    }


}