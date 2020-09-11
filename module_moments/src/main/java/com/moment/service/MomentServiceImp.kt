package com.moment.service

import com.lib.basiclib.base.activity.BaseActivity
import com.lib.basiclib.base.fragment.BaseFragment
import com.moment.MomentFragment
import com.moment.activity.MomentHotDiscussInfoActivity
import com.services.HomeService
import com.services.MomentsService
import com.xiaojinzi.component.anno.ServiceAnno

/**
 *
 * @ Author  QinTian
 * @ Date  2020/5/22
 * @ Describe
 *
 */
@ServiceAnno(value = [MomentsService::class], singleTon = true)
open class MomentServiceImp : MomentsService {

    override fun getMomentsFragment(): BaseFragment {
        return MomentFragment()
    }
}