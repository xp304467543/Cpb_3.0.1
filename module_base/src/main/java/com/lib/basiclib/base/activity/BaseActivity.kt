package com.lib.basiclib.base.activity

import android.content.Context
import android.content.DialogInterface
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.NonNull
import androidx.annotation.StringRes
import com.fh.basemodle.R
import com.lib.basiclib.base.basic.IView
import com.lib.basiclib.base.delegate.PageViewDelegate
import com.lib.basiclib.theme.AppTheme
import com.lib.basiclib.theme.UiUtils
import com.lib.basiclib.utils.SoftInputUtils
import com.lib.basiclib.utils.StatusBarUtils
import com.hwangjr.rxbus.RxBus


/**

 * @since 2020/1/11 0011 上午 11:31
 *
 * Activity基类，封装常用属性和方法
 * 子类实现后，实现[layoutResID]设置页面布局
 * [initView]初始化布局
 * [initEvent]初始化事件
 * [initData]初始化数据
 */
abstract class BaseActivity : SupportActivity(), IView {

    protected lateinit var mDelegate: PageViewDelegate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            //设置坚屏 一定要放到try catch里面，否则会崩溃
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } catch (e: Exception) {
        }
        initPre()
        setContentView(layoutResID)
        mDelegate = PageViewDelegate(this.window.decorView)
        initView()
        initEvent()
        initData()
    }

    /**
     * 提前初始化状态栏和主题颜色等属性
     */
    private fun initPre() {
        // 主题
        if (isSetAppTheme()) {
            UiUtils.setAppTheme(this, getAppTheme())
        }

        // 沉浸式
        if (isStatusBarTranslate()) {
            StatusBarUtils.setStatusBarTranslucent(this)
        } else {
            StatusBarUtils.setStatusBarColor(this)
        }

        // 设置状态栏前景颜色
        StatusBarUtils.setStatusBarForegroundColor(this, isStatusBarForegroundBlack())

        // 设置Fragment的默认背景颜色
        setDefaultFragmentBackground(R.color.background)

        // RxBus
        if (isRegisterRxBus()) RxBus.get().register(this)
    }

    /**
     * 页面布局的id
     */
    abstract val layoutResID: Int

    /**
     * 子类初始化View
     * 可以使用[findView]查找View的id
     */
    protected open fun initView() {}

    /**
     * 初始化控件的事件
     */
    protected open fun initEvent() {}

    /**
     * 初始化数据
     */
    protected open fun initData() {}

    /**
     * 获取菜单项资源ID
     */
    protected open fun getMenuResID(): Int = 0

    /**
     * 菜单项点击
     */
    protected open fun onMenuItemSelected(itemId: Int): Boolean = true

    /**
     * 状态栏是否沉浸
     */
    protected open fun isStatusBarTranslate(): Boolean = true

    /**
     * 状态栏前景色是否是黑色
     */
    protected open fun isStatusBarForegroundBlack(): Boolean = true

    /**
     * 是否设置App的主题,如果不需要设置主题,则重写本方法过滤
     */
    protected open fun isSetAppTheme(): Boolean = true

    /**
     * 获取App的主题，子类可以重写
     */
    protected open fun getAppTheme(): AppTheme = UiUtils.getAppTheme(this)

    /**
     * 是否注册Rxbus
     */
    protected open fun isRegisterRxBus(): Boolean = false

    override fun onClick(@NonNull view: View) {}

    override fun onClick(id: Int) {}

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            // 是否需要隐藏软键盘
            if (SoftInputUtils.isHideSoftInput(ev)) {
                SoftInputUtils.invokeOnTouchOutsideListener(this)
            }
        }
        return super.dispatchTouchEvent(ev)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return if (getMenuResID() == 0) {
            super.onCreateOptionsMenu(menu)
        } else {
            menuInflater.inflate(getMenuResID(), menu)
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        if (item.itemId == android.R.id.home) {
            // 返回按键处理
            onBackPressedSupport()
            return true
        }
        return onMenuItemSelected(item.itemId)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isRegisterRxBus()) RxBus.get().unregister(this)
    }

    protected fun startCircularRevealAnimation(rootView: View?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            rootView?.post {
                val cx = rootView.width / 2
                val cy = rootView.height / 2
                val finalRadius = Math.hypot(cx.toDouble(), cy.toDouble()).toFloat()
                val anim = ViewAnimationUtils.createCircularReveal(rootView, cx, cy, 0f, finalRadius)
                rootView.visibility = View.VISIBLE
                anim.start()
            }
        }
    }

    // --------------View常用的方法---------------
    // --------------View常用的方法---------------
    // --------------View常用的方法---------------
    override fun getContext(): Context? {
        return mDelegate.getContext()
    }

    override fun <T : View> findView(id: Int): T {
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

    protected fun showTipsDialog(msg: String, title: String? = getString(R.string.app_tips), confirmText: String? = getString(R.string.app_confirm)) {
        mDelegate.showTipsDialog(this, msg, title, confirmText)
    }

    protected fun showConfirmDialog(msg: String, title: String? = getString(R.string.app_tips), confirmText: String? = getString(R.string.app_confirm), listener: DialogInterface.OnClickListener? = null) {
        mDelegate.showConfirmDialog(this, msg, title, confirmText, listener)
    }
}
