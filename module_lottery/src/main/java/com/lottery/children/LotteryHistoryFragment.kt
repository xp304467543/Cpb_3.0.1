package com.lottery.children

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.customer.base.BaseNormalFragment
import com.customer.utils.countdowntimer.lotter.LotteryConstant
import com.fh.module_lottery.R
import com.lib.basiclib.utils.TimeUtils
import com.lottery.adapter.LotteryChildTypeAdapter
import com.lottery.adapter.LotteryHistoryOpenCodeAdapter
import com.customer.data.lottery.LotteryApi
import kotlinx.android.synthetic.main.child_fragment_history_open.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/16
 * @ Describe
 *
 */

class LotteryHistoryFragment : BaseNormalFragment<LotteryHistoryFragmentPresenter>() {

    var lotteryId = "-1"

    private var lotteryTypeAdapter: LotteryChildTypeAdapter? = null

    override fun attachView() = mPresenter.attachView(this)

    override fun attachPresenter() = LotteryHistoryFragmentPresenter()

    override fun getLayoutRes() = R.layout.child_fragment_history_open

    override fun initContentView() {
        smartRefreshLayoutLotteryHistoryCode.setEnableRefresh(false)//是否启用下拉刷新功能
        smartRefreshLayoutLotteryHistoryCode.setEnableLoadMore(true)//是否启用上拉加载功能
        smartRefreshLayoutLotteryHistoryCode.setEnableOverScrollBounce(true)//是否启用越界回弹
        smartRefreshLayoutLotteryHistoryCode.setEnableOverScrollDrag(true)//是否启用越界拖动（仿苹果效果）
        codeAdapter = LotteryHistoryOpenCodeAdapter(
            requireActivity(),
            arguments?.getString("lotteryId")?:"8",
            LotteryConstant.TYPE_1
        )
        rvLotteryHistoryCode.adapter = codeAdapter
        rvLotteryHistoryCode.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val value = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        lotteryTypeAdapter = LotteryChildTypeAdapter()
        rvTypeSelect.layoutManager = value
        rvTypeSelect.adapter = lotteryTypeAdapter
        lotteryTypeAdapter?.setOnItemClickListener { _, item, position ->
            lotteryTypeAdapter?.changeBackground(position)
            if (codeAdapter != null) codeAdapter!!.changeDiffType(item)
        }
        smartRefreshLayoutLotteryHistoryCode.setOnLoadMoreListener {
            getCodeData(arguments?.getString("lotteryId")!!)
        }
    }

    override fun initData() {
        lotteryId = arguments?.getString("lotteryId") ?: "-1"
        setType(lotteryId)
        getCodeData(arguments?.getString("lotteryId")!!)
    }

    private fun setType(lotteryID: String) {
        val data =
            if (lotteryID == "7" || lotteryID == "9" || lotteryID == "11" || lotteryID == "26" || lotteryID == "27") {
                arrayOf(
                    LotteryConstant.TYPE_1,
                    LotteryConstant.TYPE_2,
                    LotteryConstant.TYPE_3,
                    LotteryConstant.TYPE_4
                )
            } else if (lotteryID == "8") {
                arrayOf(LotteryConstant.TYPE_1, LotteryConstant.TYPE_5, LotteryConstant.TYPE_6)
            } else {
                arrayOf(
                    LotteryConstant.TYPE_1,
                    LotteryConstant.TYPE_2,
                    LotteryConstant.TYPE_3,
                    LotteryConstant.TYPE_7
                )
            }
        lotteryTypeAdapter?.clear()
        lotteryTypeAdapter?.refresh(data)
    }

    private var firstPage = 1
    private var codeAdapter: LotteryHistoryOpenCodeAdapter? = null

    //获取历史开奖数据
    private fun getCodeData(id: String) {
        LotteryApi.getLotteryHistoryCode(id, TimeUtils.getToday(), firstPage) {
            onSuccess {
                if (smartRefreshLayoutLotteryHistoryCode != null) smartRefreshLayoutLotteryHistoryCode.finishLoadMore()
                if (it.isNotEmpty()) {
                    codeAdapter!!.loadMore(it)
                    setGone(tvOpenCodePlaceHolder)
                    firstPage++
                } else if (smartRefreshLayoutLotteryHistoryCode != null) {
                    smartRefreshLayoutLotteryHistoryCode.setEnableLoadMore(false)
                }
            }
            onFailed {
                if (smartRefreshLayoutLotteryHistoryCode != null) smartRefreshLayoutLotteryHistoryCode.finishLoadMore()
                if (it.getMsg() == "暂无历史开奖记录" && smartRefreshLayoutLotteryHistoryCode != null) {
                    smartRefreshLayoutLotteryHistoryCode.setEnableLoadMore(false)
                    if (firstPage == 1) {
                        setVisible(tvHisHolder)
                        setGone(tvOpenCodePlaceHolder)
                    }
                }
            }
        }
    }


    companion object {
        fun newInstance(lotteryId: String, issue: String): LotteryHistoryFragment {
            val fragment = LotteryHistoryFragment()
            val bundle = Bundle()
            bundle.putString("lotteryId", lotteryId)
            bundle.putString("issue", issue)
            fragment.arguments = bundle
            return fragment
        }
    }

}