package com.lib.basiclib.base.fragment

/**

 * @since 2018/11/3 22:16
 *
 * 支持滑动返回的Fragment,默认展示标题和返回键
 */
abstract class BaseSwipeBackFragment : BaseNavFragment() {

    /**
     * 支持滑动返回
     */
    final override fun isSwipeBackEnable(): Boolean = true
}