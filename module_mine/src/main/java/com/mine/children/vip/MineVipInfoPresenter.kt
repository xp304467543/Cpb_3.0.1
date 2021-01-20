package com.mine.children.vip

import com.customer.data.mine.MineApi
import com.lib.basiclib.base.mvp.BaseMvpPresenter
import com.lib.basiclib.utils.ToastUtils

/**
 *
 * @ Author  QinTian
 * @ Date  1/15/21
 * @ Describe
 *
 */
class MineVipInfoPresenter : BaseMvpPresenter<MineVipInfoActivity>() {

    fun getInfo(){
        MineApi.getVipInfo {
            onSuccess {
                if (mView.isActive()){
                    mView.initDataInfo(it)
                }
            }
            onFailed {
                ToastUtils.showToast(it.getMsg())
            }
        }
    }
}