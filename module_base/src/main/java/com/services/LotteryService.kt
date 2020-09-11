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

interface LotteryService{

   fun getLotteryFragment(): BaseFragment

   fun getLuZhuFragment(lotteryId:String,issue:String):Fragment

   fun getHistoryFragment(lotteryId:String,issue:String):Fragment
}