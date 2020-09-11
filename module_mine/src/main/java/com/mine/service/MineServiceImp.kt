package com.mine.service

import com.mine.MineFragment
import com.services.HomeService
import com.services.MineService
import com.xiaojinzi.component.anno.ServiceAnno

/**
 *
 * @ Author  QinTian
 * @ Date  2020/5/22
 * @ Describe
 *
 */
@ServiceAnno(value = [MineService::class], singleTon = true)
open class MineServiceImp : MineService {

    override fun getMineFragment(): MineFragment {
        return MineFragment()
    }
}