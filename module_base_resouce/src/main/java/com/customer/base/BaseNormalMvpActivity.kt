package com.customer.base

import android.content.Context
import android.content.DialogInterface
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.fh.basemodle.R
import com.hwangjr.rxbus.RxBus
import com.lib.basiclib.base.basic.IMvpContract
import com.lib.basiclib.base.delegate.PageViewDelegate


/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/27
 * @ Describe
 *
 */
abstract class BaseNormalMvpActivity<P : IMvpContract.Presenter<*>> : AppCompatActivity(), IMvpContract.View {

    protected lateinit var mDelegate: PageViewDelegate

    protected open lateinit var mPresenter: P

    override fun isActive(): Boolean = !isDestroyed

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            //设置坚屏 一定要放到try catch里面，否则会崩溃
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } catch (e: Exception) {
        }

        mPresenter = attachPresenter()
        attachView()
        super.onCreate(savedInstanceState)
        // RxBus
        if (isRegisterRx()) RxBus.get().register(this)
        setContentView(getContentResID())
        mDelegate = PageViewDelegate(this.window.decorView)
        initContentView()
        initEvent()
        initData()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
        if (isRegisterRx()) {
            RxBus.get().unregister(this)
        }
    }

    abstract fun attachView()

    abstract fun attachPresenter(): P


    open fun isRegisterRx(): Boolean = false
    /**
     * 子类继续填充内容容器布局
     */
    protected open fun getContentResID(): Int = 0

    /**
     * 给子类初始化View使用
     */
    protected open fun initContentView() {

    }
    protected open fun initData() {

    }
    protected open fun initEvent() {

    }

    // --------------View常用的方法---------------
    // --------------View常用的方法---------------
    // --------------View常用的方法---------------
    override fun getContext(): Context? {
        return mDelegate.getContext()
    }



    override fun showToast(content: String?) {
        mDelegate.showToast(content)
    }

    override fun showToast(@StringRes resId: Int) {
        mDelegate.showToast(resId)
    }

    protected fun isFullScreen(): Boolean {
        return mDelegate.isFullScreen()
    }

    protected fun isPortScreen(): Boolean {
        return mDelegate.isPortScreen()
    }

    protected fun setFullScreen(isFullScreen: Boolean) {
        mDelegate.setFullScreen(this, isFullScreen)
    }

    protected fun startActivity(clazz: Class<*>) {
        mDelegate.startActivity(this, clazz)
    }

    protected fun showTipsDialog(msg: String, title: String? = getString(R.string.app_tips), confirmText: String? = getString(
        R.string.app_confirm)) {
        mDelegate.showTipsDialog(this, msg, title, confirmText)
    }

    protected fun showConfirmDialog(msg: String, title: String? = getString(R.string.app_tips), confirmText: String? = getString(
        R.string.app_confirm), listener: DialogInterface.OnClickListener? = null) {
        mDelegate.showConfirmDialog(this, msg, title, confirmText, listener)
    }

    protected fun setTextColor(textView: TextView?, color: Int) {
        mDelegate.setTextColor(textView, color)
    }
    protected fun setGone(@IdRes id: Int) {
        mDelegate.setGone(id)
    }

    protected fun setGone(view: View?) {
        mDelegate.setGone(view)
    }

    protected fun setVisible(@IdRes id: Int) {
        mDelegate.setVisible(id)
    }

    protected fun setVisible(view: View?) {
        mDelegate.setVisible(view)
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

}