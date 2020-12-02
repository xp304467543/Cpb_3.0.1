package com.home

import android.annotation.SuppressLint
import com.customer.component.dialog.GlobalDialog
import com.lib.basiclib.base.mvp.BaseMvpPresenter
import com.customer.data.home.HomeApi
import com.customer.data.mine.MineApi
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
                if (mView.isActive()) {
                    if (it.msgCount > 0) {
                        mView.setVisible(mView.topDian)
                    } else {
                        mView.setGone(mView.topDian)
                    }
                    mView.msg1 = it.countList.`_$0`
                    mView.msg2 = it.countList.`_$2`
                    mView.msg3 = it.countList.`_$3`
                    mView.msg4 = it.countList.`_$5`
                }
            }
            onFailed {
                if (it.getCode() == 2003){
                    GlobalDialog.otherLogin(mView.requireActivity())
                }
            }
        }
    }

    fun getRedTask() {
        HomeApi.getRedTask {
            onSuccess {
                if (mView.isActive()) {
                    if (it.prompt == 1) {
                        mView.setVisible(mView.topDianTask)
                    } else mView.setGone(mView.topDianTask)
                }
            }
        }
    }



}