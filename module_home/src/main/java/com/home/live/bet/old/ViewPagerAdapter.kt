package com.home.live.bet.old

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 *
 * @ Author  QinTian
 * @ Date  2020/9/1
 * @ Describe
 *
 */
class ViewPagerAdapter(fm: FragmentManager, fragmentList: ArrayList<Fragment?>) : FragmentPagerAdapter(fm) {

    private var fm: FragmentManager? = null

    private var fragmentList: ArrayList<Fragment?>? = null

    init {
        this.fm = fm
        this.fragmentList = fragmentList
    }

    override fun getItem(position: Int): Fragment {
       return fragmentList?.get(position)!!
    }
    override fun getCount(): Int {
        return fragmentList?.size ?: 0
    }
}