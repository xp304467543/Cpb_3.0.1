package com.home.service

import androidx.fragment.app.Fragment
import com.customer.data.home.HomeHotLiveResponse
import com.home.HomeFragment
import com.home.live.bet.LiveRoomBetRecordFragment
import com.home.live.bet.old.LiveRoomBetToolsRulesFragment
import com.lib.basiclib.base.recycle.BaseRecyclerAdapter
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

    override fun getRulerFragment(lotteryId:String): LiveRoomBetToolsRulesFragment {
        return LiveRoomBetToolsRulesFragment.newInstance(lotteryId)
    }

    override fun getRecordFragment(): Fragment {
        return LiveRoomBetRecordFragment()
    }

}