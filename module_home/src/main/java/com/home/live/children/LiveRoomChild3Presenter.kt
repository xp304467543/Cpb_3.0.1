package com.home.live.children

import android.view.View
import com.customer.data.home.HomeApi
import com.lib.basiclib.base.mvp.BaseMvpPresenter
import kotlinx.android.synthetic.main.fragmeent_live_child_3.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/26
 * @ Describe
 *
 */
class LiveRoomChild3Presenter : BaseMvpPresenter<LiveRoomChild3>() {

    fun getAllData(anchorID: String) {
        HomeApi.getRankList(anchorID) {
            if (mView.isActive()) {
                onSuccess {
                    mView.setGone(mView.spLiveRankLoading)
                    if (it.isEmpty()) {
                        mView.tvRankHolder.visibility = View.VISIBLE
                        mView.tvRankHolder.setText("暂无打赏记录")
                    } else {
                        mView.tvRankHolder.visibility = View.GONE
                        mView.initRankRewardList(it)
                    }
                }
                onFailed {
                    mView.setGone(mView.spLiveRankLoading)
                    if (mView.spLiveRankLoading != null) mView.tvRankHolder.visibility = View.VISIBLE
                    mView.tvRankHolder.text = "出错了~"
                }
            }

        }
    }
}