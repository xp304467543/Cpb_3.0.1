package com.mine.children.recharge

import com.customer.data.mine.MineApi
import com.lib.basiclib.base.mvp.BaseMvpPresenter
import com.lib.basiclib.utils.ToastUtils

/**
 *
 * @ Author  QinTian
 * @ Date  2020/10/14
 * @ Describe
 *
 */
class MineUserBankCardActPresenter : BaseMvpPresenter<MineUserBankCardAct>() {

    fun getUserBank(){
        MineApi.getUserBank {
            if (mView.isActive()){
                onSuccess {
                    mView.initUserBank(it)
                }
                onFailed {
                    ToastUtils.showToast(it.getMsg())
                }
            }
        }
    }
}