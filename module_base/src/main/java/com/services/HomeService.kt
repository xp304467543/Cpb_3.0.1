package com.services

import androidx.fragment.app.Fragment
import com.lib.basiclib.base.fragment.BaseFragment

/**
 *
 * @ Author  QinTian
 * @ Date  2020/5/22
 * @ Describe
 *
 */

interface HomeService{

   fun getHomeFragment(): BaseFragment

   fun getRulerFragment(lotteryId:String): Fragment
}