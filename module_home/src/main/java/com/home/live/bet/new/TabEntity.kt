package com.home.live.bet.new

import com.flyco.tablayout.listener.CustomTabEntity

/**
 *
 * @ Author  QinTian
 * @ Date  11/17/20
 * @ Describe
 *
 */
class TabEntity(var title: String, selectedIcon: Int?, unSelectedIcon: Int?) : CustomTabEntity {

    private var selectedIcon: Int? = selectedIcon
    private var unSelectedIcon: Int? = unSelectedIcon
    override fun getTabTitle(): String {
        return title
    }

    override fun getTabSelectedIcon(): Int {
        return selectedIcon?:0
    }

    override fun getTabUnselectedIcon(): Int {
        return unSelectedIcon?:0
    }

}