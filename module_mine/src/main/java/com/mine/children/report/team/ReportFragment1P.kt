package com.mine.children.report.team

import com.lib.basiclib.base.mvp.BaseMvpPresenter
import com.lib.basiclib.utils.ToastUtils
import com.customer.data.mine.MineApi
import kotlinx.android.synthetic.main.fragment_report_1.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/6/27
 * @ Describe
 *
 */
class ReportFragment1P : BaseMvpPresenter<ReportFragment1>() {


    fun getReport(start: String = "", end: String = "") {
        mView.showPageLoadingDialog()
        MineApi.getTeamReport(start, end) {
            onSuccess {
                if (mView.isActive()) {
                    mView.tv_r_1.text = it.reg_num
                    mView.tv_r_2.text = it.recharge_user_num
                    mView.tv_r_3.text = it.exchange
                    mView.tv_r_4.text = it.recharge
                    mView.tv_r_5.text = it.brokerage
                    mView.tv_r_6.text = it.sub_brokerage
                    mView.hidePageLoadingDialog()
                }
            }
            onFailed {
                mView.hidePageLoadingDialog()
                ToastUtils.showToast("获取数据失败")
            }
        }
    }

}