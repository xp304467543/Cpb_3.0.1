package com.home.live.children

import android.view.View
import com.customer.data.home.HomeApi
import com.lib.basiclib.base.mvp.BaseMvpPresenter
import kotlinx.android.synthetic.main.fragmeent_live_child_2.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/26
 * @ Describe
 *
 */
class LiveRoomChild2Presenter : BaseMvpPresenter<LiveRoomChild2>() {
    fun getAnchorInfo(anchorID: String) {
        HomeApi.getLiveAnchorInfo(anchorID) {
            if (mView.isActive()) {
                onSuccess {
                    mView.initAnchorInfo(it)
                    getAnchorDynamic(anchorID)
                }
            }
        }
    }

    //主播动态
    private fun getAnchorDynamic(anchorID: String) {
        HomeApi.getAnchorDynamic(anchorId = anchorID) {
            onSuccess {
                if (mView.isActive()){
                    mView.spLiveAnchorLoading.visibility = View.GONE
                    mView.initAnchorNews(it)
                }
            }
        }
    }
}