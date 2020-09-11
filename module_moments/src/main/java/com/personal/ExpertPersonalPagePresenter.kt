package com.personal

import android.os.Handler
import com.customer.data.lottery.LotteryApi
import com.customer.data.moments.PersonalApi
import com.lib.basiclib.base.mvp.BaseMvpPresenter
import com.lib.basiclib.utils.ToastUtils
import kotlinx.android.synthetic.main.act_presonal_expert.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/27
 * @ Describe
 *
 */
class ExpertPersonalPagePresenter : BaseMvpPresenter<ExpertPersonalPage>(){


    fun getExpertInfo(expert_id: String,lottery_id: String) {
        PersonalApi.getExpertPage(expert_id,lottery_id) {
            if (mView.isActive()) {
                onSuccess {
                    mView.initExpert(it)
                    mView.hidePageLoadingDialog()
                }
                onFailed {
                    ToastUtils.showToast(it.getMsg().toString())
                    mView.hidePageLoadingDialog()
                }
            }
        }
    }

    fun getExpertHistory(expert_id: String, lottery_id: String, limit: String) {
        PersonalApi.getExpertPageHistory(expert_id, lottery_id, limit) {
            if (mView.isActive()) {
                onSuccess { mView.initExpertHistory(it) }
                onFailed {
                    ToastUtils.showToast(it.getMsg().toString())
                    mView.tvDescription.text = "暂无历史记录！"
                }
            }
        }
    }


    var handler: Handler? = null
    var runnable: Runnable? = null
    fun getNextTime(lottery_id: String) {
        LotteryApi.getLotteryNewCode(lottery_id) {
            if (mView.isActive()) {
                onSuccess {
                    if (it.next_lottery_time?.toInt()?:0 > 1) {
                        mView.countDownTime(it.next_lottery_time.toString(), lottery_id)
                    } else {
                        if (mView.timer != null) mView.timer?.cancel()
                        handler = Handler()
                        runnable = Runnable {
                            getNextTime(it.lottery_id.toString())
                        }
                        handler?.postDelayed(runnable, 5000)
                    }
                }
                onFailed { }
            }
        }
    }
}