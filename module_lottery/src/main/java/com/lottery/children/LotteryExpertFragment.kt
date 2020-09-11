package com.lottery.children

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.customer.base.BaseNormalFragment
import com.fh.module_lottery.R
import com.lib.basiclib.utils.ToastUtils
import com.customer.data.lottery.LotteryApi
import kotlinx.android.synthetic.main.child_fragment_expert_plan.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/16
 * @ Describe
 *
 */

class LotteryExpertFragment : BaseNormalFragment<LotteryExpertFragmentPresenter>() {

    var page = 1

    var limit = "10"

    var lotteryId = "-1"

    var issue = "-1"

    var adapter: LotteryExpertPlanFragmentAdapter? = null

    override fun attachView() = mPresenter.attachView(this)

    override fun attachPresenter() = LotteryExpertFragmentPresenter()

    override fun getLayoutRes() = R.layout.child_fragment_expert_plan

    override fun initContentView() {
        lotteryExpertPlanSmartRefreshLayout.setOnRefreshListener {
            this.page = 1
            lotteryExpertPlanSmartRefreshLayout.setEnableLoadMore(true)
            getExpertPlanData(0)
        }
        lotteryExpertPlanSmartRefreshLayout.setOnLoadMoreListener {
            this.page++
            getExpertPlanData(1)
        }
        adapter = LotteryExpertPlanFragmentAdapter(requireActivity())
        rcExpertPlan.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rcExpertPlan.adapter = adapter
    }

    override fun initData() {
        issue = arguments?.getString("issue") ?: "-1"
        lotteryId = arguments?.getString("lotteryId") ?: "-1"
        getExpertPlanData(0)
    }

    private fun getExpertPlanData(load: Int) {
        if (this.lotteryId != "-1" && this.issue != "-1") {
            LotteryApi.getExpertPlan(this.lotteryId, this.issue, limit, page) {
                if (isAdded) {
                    onSuccess {
                        if (lotteryExpertPlanSmartRefreshLayout != null) {
                            lotteryExpertPlanSmartRefreshLayout.finishRefresh()
                            lotteryExpertPlanSmartRefreshLayout.finishLoadMore()
                        }
                        if (adapter != null && it.isNotEmpty()) {
                            if (load == 0) {
                                adapter?.refresh(it)
                            } else adapter?.loadMore(it)
                        } else if (lotteryExpertPlanSmartRefreshLayout != null) {
                            if (load != 0)
                                lotteryExpertPlanSmartRefreshLayout.setNoMoreData(true)
                        }
                        setGone(expertPlaceHolder)
                    }
                    onFailed {
                        if (lotteryExpertPlanSmartRefreshLayout != null) {
                            lotteryExpertPlanSmartRefreshLayout.finishRefresh()
                            lotteryExpertPlanSmartRefreshLayout.finishLoadMore()
                        }
                        if (page != 1) page--
                        ToastUtils.showToast("获取数据失败")
                        setGone(expertPlaceHolder)
                    }
                }
            }
        } else ToastUtils.showToast("获取期数失败")
    }


    companion object {
        fun newInstance(lotteryId: String, issue: String): LotteryExpertFragment {
            val fragment = LotteryExpertFragment()
            val bundle = Bundle()
            bundle.putString("lotteryId", lotteryId)
            bundle.putString("issue", issue)
            fragment.arguments = bundle
            return fragment
        }
    }


}