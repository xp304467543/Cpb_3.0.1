package com.mine.children

import com.lib.basiclib.base.activity.BaseNavActivity
import com.mine.R
import kotlinx.android.synthetic.main.act_anchor_recruit.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/24
 * @ Describe
 *
 */
class MineAnchorRecruitAct : BaseNavActivity() {

    override val layoutResID = R.layout.act_anchor_recruit

    override fun isOverride() = true

    override fun isSwipeBackEnable() = true

    override fun initEvent() {
        imgBack.setOnClickListener { finish() }
    }


}