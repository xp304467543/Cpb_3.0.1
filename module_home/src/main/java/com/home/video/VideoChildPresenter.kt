package com.home.video

import com.customer.data.video.MovieApi
import com.lib.basiclib.base.mvp.BaseMvpPresenter
import com.lib.basiclib.utils.ToastUtils
import kotlinx.android.synthetic.main.fragment_home_video_child.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/29
 * @ Describe
 *
 */
class VideoChildPresenter : BaseMvpPresenter<VideoChildFragment>() {


    fun getChildData(typeId:Int, limit:Int, page:Int,perPage:Int) {

        MovieApi.getMovieTypeData(typeId, limit, page , perPage ) {
            if (mView.isAdded) {
                onSuccess {
                    if (!it.isNullOrEmpty()){
                        mView.initVideoData(it)
                    }else ToastUtils.showToast("无数据")
                    mView.videoSmartRefreshLayout.finishRefresh()
                }
                onFailed {
                    ToastUtils.showToast("暂无数据")
                    mView.videoSmartRefreshLayout.finishRefresh()
                }
            }


        }
    }

    fun getMoreVideo(typeId:Int,cid:Int,num:Int,isMore:Boolean,pos:Int){
        MovieApi.getVideoChange(typeId,cid,num,isMore){
            if (mView.isAdded){
                onSuccess {

                }
                onFailed {
                    ToastUtils.showToast("暂无数据")
                }
            }
            }
    }


}