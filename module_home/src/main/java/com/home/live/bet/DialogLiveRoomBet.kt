package com.home.live.bet

import android.content.Context
import com.customer.component.BaseBottomSheet
import com.home.R

/**
 *
 * @ Author  QinTian
 * @ Date  2020/9/1
 * @ Describe 新版本用这个
 *
 */
class DialogLiveRoomBet(context: Context) : BaseBottomSheet(context) {

    init {
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        window?.setDimAmount(0f)
        setContentView(R.layout.old_dialog_bet)
    }
}