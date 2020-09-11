package com.home

import com.lib.basiclib.base.mvp.BaseMvpPresenter
import com.customer.data.home.HomeApi
import kotlinx.android.synthetic.main.fragment_home.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/5/22
 * @ Describe
 *
 */
class HomePresenter : BaseMvpPresenter<HomeFragment>() {

    //获取新消息
    fun getNewMsg() {
        HomeApi.getIsNewMessage {
            onSuccess {
                if (it.msgCount > 0) {
                    mView.setVisible(mView.topDian)
                } else {
                    mView.setGone(mView.topDian)
                }
                mView.msg1 = it.countList.`_$0`
                mView.msg2 = it.countList.`_$2`
                mView.msg3 = it.countList.`_$3`
            }
        }
    }

}