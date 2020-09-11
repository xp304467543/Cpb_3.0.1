package com.moment

import com.lib.basiclib.base.mvp.BaseMvpPresenter
import com.customer.data.moments.MomentsApi

/**
 *
 * @ Author  QinTian
 * @ Date  2020/5/22
 * @ Describe
 *
 */
class  MomentPresenter : BaseMvpPresenter< MomentFragment>() {


    fun getMomentsData() {
        MomentsApi.getTopBanner {
            onSuccess {
                mView.upDateBanner(it)
            }
        }
    }

}