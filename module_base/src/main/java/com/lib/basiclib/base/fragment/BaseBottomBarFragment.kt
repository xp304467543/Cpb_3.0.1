package com.lib.basiclib.base.fragment

import com.fh.basemodle.R
import kotlinx.android.synthetic.main.base_fragment_bottombar.*

/**

 * @since  2019/3/27 20:38
 *
 * 带有BottomBar的样式Fragment
 */
abstract class BaseBottomBarFragment : BasePageFragment() {


    final override fun getLayoutResID() = R.layout.base_fragment_bottombar

    override fun initView() {
        bottomBar.inflateMenu(inflateBottomMenu())
        initRootFragment(R.id.container)
    }

    override fun initEvent() {
        bottomBar?.setOnNavigationItemSelectedListener {
            onNavigationItemSelectedListener(it.itemId)
            return@setOnNavigationItemSelectedListener true
        }
    }

    /**
     * 条目点击
     */
    abstract fun onNavigationItemSelectedListener(itemId: Int)

    /**
     * 填充Fragment
     */
    abstract fun initRootFragment(container: Int)

    /**
     * 填充Menu布局
     */
    abstract fun inflateBottomMenu(): Int
}