package com.home.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/10
 * @ Describe
 *
 */
class HomeLotteryViewPagerAdapter(title: List<String>, list: List<View>) : PagerAdapter() {

    private var mFragments = list
    private var mTitles = title


    override fun getCount(): Int {
        return mFragments.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mTitles[position]
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }


    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val imageView: View = mFragments[position]
        container.addView(imageView)
        return imageView

    }


    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View?)
    }

}