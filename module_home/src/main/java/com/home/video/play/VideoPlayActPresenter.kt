package com.home.video.play

import com.customer.data.video.MovieApi
import com.lib.basiclib.base.mvp.BaseMvpPresenter
import com.lib.basiclib.utils.ToastUtils
import kotlinx.android.synthetic.main.act_video_play.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/31
 * @ Describe
 *
 */
class VideoPlayActPresenter : BaseMvpPresenter<VideoPlayAct>() {


    fun getVideoAddress(id: Int) {
        MovieApi.getPlayInfo(id) {
            if (mView.isActive()) {
                onSuccess {
                    mView.setGone(mView.tvHolder)
                    mView.initVideoInfo(it)
                }
                onFailed {ToastUtils.showToast(it.getMsg()) }
            }

        }
    }

    fun getLike(id: Int) {
        MovieApi.getYouLike(id) {
            if (mView.isActive()) {
                onSuccess { if (!it.isNullOrEmpty()) mView.adapter?.refresh(it) else ToastUtils.showToast("暂无数据") }
                onFailed { ToastUtils.showToast(it.getMsg())  }
            }
        }
    }

    fun zan(id: Int,isZan:Int){
        MovieApi.getZan(id,isZan){
            if (mView.isActive()) {
                onSuccess {
                    mView.initZan(it)
                }
                onFailed {ToastUtils.showToast(it.getMsg()) }
            }
        }
    }

}