package com.home.live.children

import android.view.View
import com.customer.data.home.HomeApi
import com.lib.basiclib.base.mvp.BaseMvpPresenter
import com.lib.basiclib.utils.ToastUtils
import kotlinx.android.synthetic.main.fragmeent_live_child_4.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/26
 * @ Describe
 *
 */
class LiveRoomChild4Presenter : BaseMvpPresenter<LiveRoomChild4>() {

    fun getAllData(type: String) {
        HomeApi.getLiveAdvanceList(type) {
            if (mView.isActive() && mView.isVisible) {
                onSuccess {
                    if ( mView.spLiveAdvanceLoading!=null)  mView.spLiveAdvanceLoading.visibility = View.GONE
                    if (!it.data!!.isJsonNull && it.data.toString().length > 5) {
                            mView.initAdvanceRecycle(it)
                    } else mView.setVisible(mView.tvHolderAd)
                }

                onFailed {
                    mView.spLiveAdvanceLoading.visibility = View.GONE
                    ToastUtils.showToast(it.getMsg().toString())
                }
            }
        }
    }
}