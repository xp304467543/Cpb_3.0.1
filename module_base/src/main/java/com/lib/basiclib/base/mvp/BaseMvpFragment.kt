package com.lib.basiclib.base.mvp
import com.lib.basiclib.base.basic.IMvpContract

/**

 * @since 2020/12/25 15:25
 *
 * 添加MVP的Fragment，并且添加依赖注入
 */

abstract class BaseMvpFragment<P : IMvpContract.Presenter<*>> : BaseMvpNavFragment<P>(), IMvpContract.View {

    final override fun isShowToolBar() = false

    final override fun isSwipeBackEnable() = false
}