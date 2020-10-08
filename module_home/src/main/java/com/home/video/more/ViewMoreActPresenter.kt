package com.home.video.more

import com.customer.data.video.MovieApi
import com.lib.basiclib.base.mvp.BaseMvpPresenter
import com.lib.basiclib.utils.ToastUtils
import kotlinx.android.synthetic.main.act_video_more.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/10/7
 * @ Describe
 *
 */

class ViewMoreActPresenter : BaseMvpPresenter<VideoMoreAct>() {

    fun getTabTitle() {
        MovieApi.getMovieType {
            if (mView.isActive()){
                onSuccess {
                    mView.topList = it
                    if (! mView.topList.isNullOrEmpty())  mView.mTypeId =  mView.topList!![0].id ?: -1
                    val tab1 = arrayListOf<String>()
                    for (res in it) {
                        tab1.add(res.name ?: "未知")
                        val tab3 = arrayListOf<String>()
                        val tab3Cid = arrayListOf<Int>()
                        if (!res.children.isNullOrEmpty()) {
                            for (child in res.children!!) {
                                tab3.add(child.name ?: "未知")
                                tab3Cid.add(child.id ?: -1)
                            }
                            mView.tab3List[res.id ?: 1] = tab3
                            mView.tab3Cid[res.id ?: 1] = tab3Cid
                        }
                    }
                    mView.initMagicIndicator1(tab1)
                    mView.initMagicIndicator2()
                }
                onFailed {
                    ToastUtils.showToast(it.getMsg())
                }
            }
        }
    }


     fun getChildTab(index: Int,isTopClick:Boolean = false) {
         if (mView.isActive()){
             if (!mView.topList.isNullOrEmpty() || mView.topList!![index].id ?: -1 == -1) {
                 val pos = mView.topList!![index].id ?: -1
                 mView.mTypeId = pos
                 mView.tab3List[mView.mTypeId].let { if (it != null) { mView.initMagicIndicator3(it, pos,isTopClick) } }
             } else ToastUtils.showToast("无详细标签")
         }
    }
}