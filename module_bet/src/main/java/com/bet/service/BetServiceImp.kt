package com.bet.service

import com.bet.BetFragment
import com.services.BetService
import com.xiaojinzi.component.anno.ServiceAnno

/**
 *
 * @ Author  QinTian
 * @ Date  2020/5/22
 * @ Describe
 *
 */
@ServiceAnno(value = [BetService::class], singleTon = true)
open class BetServiceImp : BetService {

    override fun getBetFragment(): BetFragment {
        return BetFragment()
    }

}