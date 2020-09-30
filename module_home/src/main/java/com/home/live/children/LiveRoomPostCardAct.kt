package com.home.live.children

import com.home.R
import com.lib.basiclib.base.activity.BaseNavActivity
import kotlinx.android.synthetic.main.act_live_post_card.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/9/28
 * @ Describe
 *
 */
class LiveRoomPostCardAct : BaseNavActivity() {

    override fun isShowToolBar() = false

    override val layoutResID = R.layout.act_live_post_card

    override fun isOverride() = true

    override fun isSwipeBackEnable() = true

    override fun initEvent() {
        imgBack.setOnClickListener {
            finish()
        }
    }
}