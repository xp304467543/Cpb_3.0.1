package com.lib.basiclib.base.adapter

import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.lib.basiclib.base.fragment.BaseFragment

/**

 * @since 2020/10/31 21:27
 *
 * 给ViewPager创建Fragment使用
 *
 */
class BaseFragmentPageAdapter : FragmentStatePagerAdapter {

    private var mFragments = listOf<Any>()
    private var mTitles = listOf<String>()

    constructor(manager: FragmentManager) : super(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)

    constructor(manager: FragmentManager, fragments: List<Any>) : super(manager,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        this.mFragments = fragments
    }


    constructor(manager: FragmentManager, fragments: Array<Any>) : super(manager,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        this.mFragments = fragments.toList()
    }

    constructor(manager: FragmentManager, fragments: List<Any>, titles: List<String>) : super(manager,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        this.mFragments = fragments
        this.mTitles = titles
    }

    constructor(manager: FragmentManager, fragments: Array<Any>, titles: Array<String>) : super(manager,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        this.mFragments = fragments.toList()
        this.mTitles = titles.toList()
    }

    override fun getItem(position: Int): Fragment {
        return mFragments[position] as Fragment
    }

    override fun getCount(): Int {
        return mFragments.size
    }


    override fun getPageTitle(position: Int): CharSequence? {
        return if (mTitles.isNotEmpty()) {
            mTitles[position]
        } else {
            super.getPageTitle(position)
        }
    }

    override fun saveState(): Parcelable? {
        return null
    }

    /**
     * 更新数据
     */
    fun setData(fragments: ArrayList<*>, titles: ArrayList<String>) {
        mFragments = fragments
        mTitles = titles
        notifyDataSetChanged()
    }
}