package com.lib.basiclib.base.fragment

import android.app.Application
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.*
import androidx.viewpager.widget.ViewPager
import com.fh.basemodle.R
import com.lib.basiclib.base.activity.BaseActivity
import com.lib.basiclib.base.adapter.BaseFragmentPageAdapter
import com.lib.basiclib.base.basic.IView
import com.lib.basiclib.base.delegate.PageViewDelegate
import com.lib.basiclib.widget.placeholder.PlaceholderView
import com.google.android.material.tabs.TabLayout
import com.hwangjr.rxbus.RxBus
import com.lib.basiclib.app.BaseApplication

/**

 * @since 2018/1/13 23:52
 *
 * 基类Fragment，封装视图，将布局id抽取出来，让子类去实现。
 * 提供初始化View,点击事件，数据加载的方法。
 * 实现IView，提供操作View的基本方法。
 */
abstract class BaseFragment : SupportFragment(), IView {

    private lateinit var mRootView: View
    protected lateinit var mDelegate: PageViewDelegate

    private var isLoaded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isRegisterRxBus()) {
            RxBus.get().register(this)
        }
    }

    final override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mRootView = inflater.inflate(getLayoutResID(), container, false)
        mDelegate = PageViewDelegate(mRootView)
        return if (isSwipeBackEnable()) attachToSwipeBack(mRootView) else mRootView
    }

    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initEvent()
    }

    /**
     * Fragment提供的懒加载方法，使用final修饰，不让子类使用。
     * 子类请使用[initData]加载使用
     */
    final override fun onLazyInitView(savedInstanceState: Bundle?) {
        initData()
    }

    override fun onResume() {
        super.onResume()
        if (!isLoaded && !isHidden) {
            lazyInit()
            isLoaded = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isLoaded = false
    }

    /**
     * 获取控件ID
     * @return 控件ID，子类返回
     */
    abstract fun getLayoutResID(): Int

    /**
     * 初始化View，在Fragment的onViewCreated()方法中调用，
     * 如果是在ViewPager中使用Fragment，则该方法在Fragment初始化时就会被调用。
     * 子类可以在该方法中使用kotlin直接使用View的id引用。
     * 也可以调用[findView]方法查找View的id。
     */
    protected open fun initView() {}



    /**
     * 初始化事件，Fragment中所有的点击事件都可以在里面实现。
     * 可以直接调用[setOnClick]方法，然后再重写[onClick]方法实现点击事件。
     * 该方法在Fragment的onViewCreated()方法中调用。
     */
    protected open fun initEvent() {}

    /**
     * 初始化数据，只有当前的Fragment第一次可见才会被调用，用于延迟加载数据。
     * 在[onSupportVisible]方法之后调用。
     * Fragment多次可见时，只有第一次才会加载数据。
     */
    protected open fun initData() {}
    protected open fun lazyInit(){}
    /**
     * 是否支持侧滑返回
     * 默认是不支持侧滑返回的
     */
    protected open fun isSwipeBackEnable() = false

    /**
     * 是否注册Rxbus
     */
    protected open fun isRegisterRxBus(): Boolean = false


    override fun onClick(@NonNull view: View) {}

    override fun onClick(id: Int) {}


    // --------------View常用的方法---------------
    // --------------View常用的方法---------------
    // --------------View常用的方法---------------
    override fun <T : View> findView(@IdRes id: Int): T {
        return mDelegate.findView(id)
    }

    override fun setOnClick(view: View?) {
        mDelegate.setOnClick(view, this)
    }

    override fun setOnClick(@IdRes id: Int) {
        mDelegate.setOnClick(id, this)
    }

    override fun setGone(@IdRes id: Int) {
        mDelegate.setGone(id)
    }

    override fun setGone(view: View?) {
        mDelegate.setGone(view)
    }

    override fun setVisible(@IdRes id: Int) {
        mDelegate.setVisible(id)
    }

    override fun setVisible(view: View?) {
        mDelegate.setVisible(view)
    }

    override fun setVisibility(@IdRes id: Int, visibility: Int) {
        mDelegate.setVisibility(id, visibility)
    }

    override fun setVisibility(view: View?, visibility: Int) {
        mDelegate.setVisibility(view, visibility)
    }

    override fun setVisibility(id: Int, isVisible: Boolean) {
        mDelegate.setVisibility(id, isVisible)
    }

    override fun setVisibility(view: View?, isVisible: Boolean) {
        mDelegate.setVisibility(view, isVisible)
    }

    override fun setText(@IdRes id: Int, text: CharSequence?) {
        mDelegate.setText(id, text)
    }

    override fun setText(@IdRes id: Int, @StringRes resId: Int) {
        mDelegate.setText(id, resId)
    }

    override fun setText(textView: TextView?, @StringRes resId: Int) {
        mDelegate.setText(textView, resId)
    }

    override fun setText(textView: TextView?, text: CharSequence?) {
        mDelegate.setText(textView, text)
    }

    override fun setTextColor(textView: TextView?, color: Int) {
        mDelegate.setTextColor(textView, color)
    }

    override fun setTextColor(id: Int, color: Int) {
        mDelegate.setTextColor(id, color)
    }

    override fun setTextSize(id: Int, size: Float) {
        mDelegate.setTextSize(id, size)
    }

    override fun setTextSize(textView: TextView?, size: Float) {
        mDelegate.setTextSize(textView, size)
    }

    override fun setImageResource(imageView: ImageView?, @DrawableRes resId: Int) {
        mDelegate.setImageResource(imageView, resId)
    }

    override fun setImageBitmap(imageView: ImageView?, bitmap: Bitmap?) {
        mDelegate.setImageBitmap(imageView, bitmap)
    }

    override fun setImageDrawable(imageView: ImageView?, drawable: Drawable?) {
        mDelegate.setImageDrawable(imageView, drawable)
    }

    override fun showToast(content: String?) {
        mDelegate.showToast(content)
    }

    override fun showToast(@StringRes resId: Int) {
        mDelegate.showToast(resId)
    }

    override fun showLongToast(content: String?) {
        mDelegate.showLongToast(content)
    }

    override fun showLongToast(@StringRes resId: Int) {
        mDelegate.showLongToast(resId)
    }

    protected fun getApplication(): BaseApplication {
        return mDelegate.getApplication()
    }

    protected fun startActivity(clazz: Class<*>) {
        mDelegate.startActivity(context, clazz)
    }

    protected fun getColor(@ColorRes id: Int): Int {
        return mDelegate.getColor(id)
    }

    protected fun getDrawable(@DrawableRes id: Int): Drawable? {
        return mDelegate.getDrawable(id)
    }


    /**
     * 设置ViewPager和TabLayout，提供给子类调用
     * 防止加载数据后采取设置TabLayout的情况
     */
    protected fun setTabAdapter(viewPager: ViewPager, tabLayout: TabLayout, fragments: List<BaseFragment>, titles: List<String>) {
        if (fragments.isNotEmpty()) {
            val adapter = BaseFragmentPageAdapter(childFragmentManager, fragments, titles)
            viewPager.adapter = adapter
            viewPager.offscreenPageLimit = fragments.size
            tabLayout.setupWithViewPager(viewPager)
        }
    }

    /**
     * 获取页面的Activity
     */
    protected fun getPageActivity(): BaseActivity? {
        if (context !is BaseActivity) {
            return null
        }
        return context as BaseActivity
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isRegisterRxBus()) {
            RxBus.get().unregister(this)
        }
    }

    // ------------- 给之类使用的方法 -----------------
    // ------------- 给之类使用的方法 -----------------
    // ------------- 给之类使用的方法 -----------------
    protected fun showTipsDialog(msg: String, title: String? = getString(R.string.app_tips), confirmText: String? = getString(R.string.app_confirm)) {
        mDelegate.showTipsDialog(context, msg, title, confirmText)
    }

    protected fun showConfirmDialog(msg: String, title: String? = getString(R.string.app_tips), confirmText: String? = getString(R.string.app_confirm), listener: DialogInterface.OnClickListener? = null) {
        mDelegate.showConfirmDialog(context, msg, title, confirmText, listener)
    }

    protected fun setStatusBarHeight(statusView: View?, isSetStatus: Boolean = true) {
        mDelegate.setStatusBarHeight(statusView, isSetStatus)
    }

    protected fun showLoading(placeholder: PlaceholderView?) {
        mDelegate.showPageLoading(placeholder)
    }

    protected fun showEmpty(placeholder: PlaceholderView?, msg: String? = null) {
        mDelegate.showPageEmpty(placeholder, msg)
    }

    protected fun showError(placeholder: PlaceholderView?, msg: String? = null) {
        mDelegate.showPageError(placeholder, msg)
    }

    protected fun showContent(placeholder: PlaceholderView?) {
        mDelegate.showPageContent(placeholder)
    }

    protected fun setErrorRetryListener(placeholder: PlaceholderView?, listener: () -> Unit) {
        mDelegate.setPageErrorRetryListener(placeholder, listener)
    }

    protected fun setFullScreen(isFullScreen: Boolean) {
        mDelegate.setFullScreen(getPageActivity(), isFullScreen)
    }

    protected fun setBackgroundColor(@ColorInt color: Int) {
        mRootView.setBackgroundColor(color)
    }

    protected fun isPortScreen(): Boolean {
        return mDelegate.isPortScreen()
    }

    protected fun isFullScreen(): Boolean {
        return !isPortScreen()
    }

}