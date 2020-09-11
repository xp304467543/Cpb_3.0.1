package com.customer.base

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.*
import androidx.fragment.app.Fragment
import com.hwangjr.rxbus.RxBus
import com.lib.basiclib.app.BaseApplication
import com.lib.basiclib.base.basic.IMvpContract
import com.lib.basiclib.base.delegate.PageViewDelegate

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/19
 * @ Describe
 *
 */
abstract class BaseNormalFragment<P : IMvpContract.Presenter<*>> : Fragment(), IMvpContract.View {

    private var isLoaded = false

    private var rootView: View? = null

    protected lateinit var mPresenter: P

    private lateinit var mDelegate: PageViewDelegate

    override fun onCreateView(
        @NonNull inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mPresenter = attachPresenter()
        attachView()
        super.onCreateView(inflater, container, savedInstanceState)
        if (rootView == null) {
            rootView = inflater.inflate(getLayoutRes(), container, false)
        }
        mDelegate = PageViewDelegate(rootView)
        if (isRegisterRxBus()) { RxBus.get().register(this) }
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initContentView()
        initEvent()
        context
    }


    override fun onResume() {
        super.onResume()
        if (!isLoaded && !isHidden) {
            initData()
            isLoaded = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mPresenter.detachView()
        isLoaded = false

    }

    protected abstract fun attachView()

    protected abstract fun attachPresenter(): P

    override fun isActive(): Boolean = isAdded

    /**
     *
     *懒加载
     *
     */
    abstract fun initData()


    /**
     * 是否注册Rxbus
     */
    open fun isRegisterRxBus(): Boolean = false



    /**
     * 返回布局 resId
     *
     * @return layoutId
     */
    protected abstract fun getLayoutRes(): Int


    /**
     * 初始化view
     */
    protected abstract fun initContentView()

    protected open fun initEvent() {}


    // --------------View常用的方法---------------
    // --------------View常用的方法---------------
    // --------------View常用的方法---------------
     fun <T : View> findView(@IdRes id: Int): T {
        return mDelegate.findView(id)
    }

     fun setGone(@IdRes id: Int) {
        mDelegate.setGone(id)
    }

     fun setGone(view: View?) {
        mDelegate.setGone(view)
    }

     fun setVisible(@IdRes id: Int) {
        mDelegate.setVisible(id)
    }

     fun setVisible(view: View?) {
        mDelegate.setVisible(view)
    }

     fun setVisibility(@IdRes id: Int, visibility: Int) {
        mDelegate.setVisibility(id, visibility)
    }

     fun setVisibility(view: View?, visibility: Int) {
        mDelegate.setVisibility(view, visibility)
    }

     fun setVisibility(id: Int, isVisible: Boolean) {
        mDelegate.setVisibility(id, isVisible)
    }

     fun setVisibility(view: View?, isVisible: Boolean) {
        mDelegate.setVisibility(view, isVisible)
    }



    override fun showToast(content: String?) {
        mDelegate.showToast(content)
    }

    override fun showToast(@StringRes resId: Int) {
        mDelegate.showToast(resId)
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

    override fun showPageContent() {

    }

    override fun hidePageLoading() {

    }

    override fun hidePageLoadingDialog() {

    }

    override fun showPageEmpty(msg: String?) {

    }

    override fun showPageError(msg: String?) {

    }

    override fun showPageLoadingDialog(msg: String?) {

    }

    override fun onDestroy() {
        super.onDestroy()
        if (isRegisterRxBus()) {
            RxBus.get().unregister(this)
        }
    }

}