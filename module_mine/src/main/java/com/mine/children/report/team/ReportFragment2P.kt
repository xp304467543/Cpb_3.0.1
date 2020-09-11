package com.mine.children.report.team

import com.lib.basiclib.base.mvp.BaseMvpPresenter
import com.customer.data.mine.MineApi
import kotlinx.android.synthetic.main.fragment_report_2.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/6/27
 * @ Describe
 *
 */
class ReportFragment2P : BaseMvpPresenter<ReportFragment2>() {

    fun getVip(page: Int) {
        MineApi.getVipLevel(page = page) {
            onSuccess {
                if (mView.isActive()) {
                    if (page == 1) {
                        if (it.isNullOrEmpty()) {
                            mView.adapter?.clear()
                            mView.rvVip.removeAllViews()
                            mView.setVisible(mView.vipHolder)
                        } else {
                            mView.setGone(mView.vipHolder)
                            mView.adapter?.clear()
                            mView.adapter?.refresh(it)
                            mView.page++
                        }
                    } else {
                        if (!it.isNullOrEmpty()) {
                            mView.adapter?.loadMore(it)
                            mView.page++
                        }
                    }
                    mView.smartVip.finishRefresh()
                    mView.smartVip.finishLoadMore()
                }
            }
            onFailed {
                if (mView.isActive()) {
                    mView.smartVip.finishRefresh()
                    mView.smartVip.finishLoadMore()
                    if (mView.page == 1) {
                        mView.setVisible(mView.vipHolder)
                        mView.vipHolder.text = "暂无会员信息"
                    }
                }
            }
        }
    }
}