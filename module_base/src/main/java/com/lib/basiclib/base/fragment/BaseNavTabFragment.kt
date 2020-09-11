package com.lib.basiclib.base.fragment

import android.os.Build
import androidx.appcompat.widget.Toolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.fh.basemodle.R
import com.lib.basiclib.base.adapter.BaseFragmentPageAdapter
import kotlinx.android.synthetic.main.base_nav_page.*
import kotlinx.android.synthetic.main.base_nav_tab.*
import kotlinx.android.synthetic.main.base_nav_tab.appBarLayout
import kotlinx.android.synthetic.main.base_nav_tab.placeholder
import kotlinx.android.synthetic.main.base_nav_tab.statusView
import kotlinx.android.synthetic.main.base_nav_tab.toolbar

/**

 * @since 2019/3/4 17:20
 */
open class BaseNavTabFragment : BaseFragment() {

    override fun getLayoutResID(): Int = R.layout.base_nav_tab

    final override fun initView() {
        // 设置是否展示标题栏
        setShowToolBar(isShowToolBar())

        // 设置状态栏高度
        setStatusBarHeight(statusView, isSetStatusBarHeight())

        // 设置导航栏文字等
        if (isShowToolBar()) {
            // 设置标题
            setPageTitle(getPageTitle())

            // 设置Toolbar标题样式
            mDelegate.setToolbarStyle(isMainPage(), toolbar)

            // 左侧返回按钮
            mDelegate.setBackIcon(toolbar, isMainPage(), isShowBackIcon(), isShowBackIconWhite()) { onBackClick() }

            // menu
            mDelegate.inflateMenu(toolbar, getMenuResID()) { onMenuItemSelected(it) }
        }

        showPageLoading()

        // 初始化容器View
        initContentView()
    }

    override fun initData() {
        setTabAdapter(viewPager, tabLayout, getTabFragments(), getTabTitles())
        showPageContent()
    }


    /**
     * 给之类设置数据
     */
    protected fun setTabAdapter(fragments: List<BaseFragment>, titles: List<String>) {
        if (fragments.isNotEmpty()) {
            val adapter = BaseFragmentPageAdapter(childFragmentManager, fragments, titles)
            viewPager?.adapter = adapter
            viewPager?.offscreenPageLimit = fragments.size
            tabLayout?.setupWithViewPager(viewPager)
        }
    }

    /**
     * 设置Tab样式
     */
    protected fun setTabMode(@TabLayout.Mode mode: Int) {
        tabLayout?.tabMode = mode
    }

    /**
     * Tab的标题
     */
    protected open fun getTabTitles(): List<String> = listOf()

    /**
     * Tab填充的页面
     */
    protected open fun getTabFragments(): List<BaseFragment> = listOf()

    /**
     * 给子类初始化View使用
     */
    protected open fun initContentView() {

    }

    /**
     * 获取页面标题，进入页面后会调用该方法获取标题，设置给ToolBar
     * 调用该方法返回Title，则会使用默认的Title样式，如果需要设置样式
     * 请调用setPageTitle()
     */
    protected open fun getPageTitle(): String? = mDelegate.getPageTitle()

    /**
     * 获取菜单项资源ID
     */
    protected open fun getMenuResID(): Int = 0

    /**
     * 菜单项点击
     */
    protected open fun onMenuItemSelected(itemId: Int): Boolean = true


    /**
     * 执行返回操作
     * 默认是Fragment弹栈，然后退出Activity
     * 如果栈内只有一个Fragment，则退出Activity
     */
    protected open fun onBackClick() {
        getPageActivity()?.onBackPressedSupport()
    }

    /**
     * 是否设置状态栏高度，如果设置的话，默认会自动调整状态栏的高度。
     * 如果为false则状态栏高度为0，默认状态栏有高度。
     */
    protected open fun isSetStatusBarHeight(): Boolean = true

    /**
     * 是否展示ToolBar，如果设置为false则不展示。
     * 如果不展示标题栏，则状态栏也不会展示。
     */
    protected open fun isShowToolBar(): Boolean = true

    /**
     * 是否可以返回，如果可以则展示返回按钮，并且设置返回事件
     * 默认可以返回
     */
    protected open fun isShowBackIcon(): Boolean = true

    /**
     * 是不是Main页面
     * 如果是的话，ToolBar设置粗体的样式
     */
    protected open fun isMainPage(): Boolean = false


    /**
     * 获取标题栏对象，让子类主动去设置样式
     */
    protected fun getToolBar(): Toolbar? {
        return toolbar
    }

    /**
     * 主动设置页面标题，给子类调用
     * Toolbar的标题默认靠左，这里使用Toolbar包裹一个TextView的形式
     * 呈现居中的标题
     */
    protected fun setPageTitle(title: String?) {
        toolbar.title = ""
        mDelegate.setPageTitle(tvTitle, title)
    }

    /**
     * 设置ToolBar的展示状态
     * @param isShow 是否展示
     */
    protected fun setShowToolBar(isShow: Boolean) {
        mDelegate.setShowToolBar(appBarLayout, isShow)
    }

    /**
     * 设置AppBarLayout 的阴影，默认为0
     */
    protected fun setAppBarLayoutElevation(elevation: Float = 0f) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            appBarLayout.stateListAnimator = null
            appBarLayout.elevation = elevation
        }
    }
    /**
     * 是否展示白色返回图标
     * true返回白色 fleas返回黑色
     */
    protected open fun isShowBackIconWhite(): Boolean = true

    // --------------------页面状态的相关处理--------------------
    // --------------------页面状态的相关处理--------------------
    // --------------------页面状态的相关处理--------------------
    /**
     * 展示加载对话框
     * 适用于页面UI已经绘制了，需要再加载数据更新的情况
     */
    protected fun showPageLoadingDialog(msg: String? = getString(R.string.app_loading)) {
        mDelegate.showPageLoadingDialog(context, msg)
    }

    /**
     * 隐藏加载对话框
     */
    protected fun hidePageLoadingDialog() {
        mDelegate.hidePageLoadingDialog()
    }

    /**
     * 展示加载中的占位图
     */
    protected fun showPageLoading() {
        mDelegate.showPageLoading(placeholder)
    }

    protected fun hidePageLoading() {
        mDelegate.hidePageLoading(placeholder)
    }

    /**
     * 展示空数据的占位图
     */
    protected fun showPageEmpty(msg: String? = null) {
        showEmpty(placeholder, msg)
    }

    /**
     * 展示加载错误的占位图
     */
    protected fun showPageError(msg: String? = null) {
        showError(placeholder, msg)
    }

    /**
     * 设置页面加载错误重连的监听
     */
    protected fun setPageErrorRetryListener(listener: () -> Unit) {
        setErrorRetryListener(placeholder, listener)
    }

    /**
     * 展示加载完成，要显示的内容
     */
    protected fun showPageContent() {
        mDelegate.showPageContent(placeholder)
    }

    /**
     * 获取悬浮按钮
     */
    protected fun getFloatActionButton(): FloatingActionButton? {
        return floatingActionButton
    }

    /**
     * 支持滑动返回
     */
    override fun isSwipeBackEnable() = true
}