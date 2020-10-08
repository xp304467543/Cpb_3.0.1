package com.home.video.search

import com.customer.data.video.MovieApi
import com.lib.basiclib.base.mvp.BaseMvpPresenter
import com.lib.basiclib.utils.ToastUtils
import com.lib.basiclib.utils.ViewUtils
import kotlinx.android.synthetic.main.act_video_search.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/30
 * @ Describe
 *
 */
class VideoSearchActPresenter : BaseMvpPresenter<VideoSearchAct>() {


    fun getHot(){
        MovieApi.getVideoHot {
            if (mView.isActive()){
                onSuccess {
                    ViewUtils.setVisible(mView.recommendContainer)
                    mView.adapterHot?.refresh(it.list)
                }
                onFailed {
                    ToastUtils.showToast(it.getMsg())
                }
            }
        }
    }

    fun searchVideo(name: String) {
        MovieApi.getSearchVideo(name) {
            if (mView.isActive()) {
                onSuccess {
                    if (it.isEmpty()) {
                        ViewUtils.setVisible(mView.searchHolder)
                    }else{
                        ViewUtils.setGone(mView.searchContainer)
                        ViewUtils.setGone(mView.searchHolder)
                        ViewUtils.setGone(mView.recommendContainer)
                        ViewUtils.setVisible(mView.smartSearch)
                        mView.adapter?.refresh(it)
                    }
                }
                onFailed {
                    ViewUtils.setVisible(mView.searchHolder)
                    ToastUtils.showToast(it.getMsg())
                }
            }
        }
    }
}