package com.home.service

import com.home.HomeFragment
import com.services.HomeService
import com.xiaojinzi.component.anno.ServiceAnno

/**
 *
 * @ Author  QinTian
 * @ Date  2020/5/22
 * @ Describe
 *
 */
@ServiceAnno(value = [HomeService::class], singleTon = true)
open class HomeServiceImp : HomeService {

    override fun getHomeFragment(): HomeFragment {
        return HomeFragment()
    }
}