package com.lib.basiclib.helper

import android.app.Activity
import android.graphics.Rect
import android.view.View
import android.view.ViewTreeObserver
import androidx.fragment.app.Fragment
import com.lib.basiclib.utils.StatusBarUtils
import com.lib.basiclib.utils.ViewUtils

/**

 * @since 2019/2/26 15:17
 *
 * 键盘弹起和收起助手
 */
class SoftKeyboardHelper : ViewTreeObserver.OnGlobalLayoutListener {

    private var lastSoftKeyboardHeightInPx = 0
    private var isSoftKeyboardOpened = false
    private var mRootView: View? = null
    private var onSoftKeyboardChanged: ((keyboardHeightInPx: Int) -> Unit)? = null
    private var onSoftKeyboardOpened: ((keyboardHeightInPx: Int) -> Unit)? = null
    private var onSoftKeyboardClosed: (() -> Unit)? = null

    fun registerFragment(fragment: Fragment): SoftKeyboardHelper {
        return registerView(fragment.view)
    }

    fun registerActivity(activity: Activity): SoftKeyboardHelper {
        return registerView(activity.window.decorView.findViewById(android.R.id.content))
    }

    fun registerView(view: View?): SoftKeyboardHelper {
        mRootView = view
        view?.viewTreeObserver?.addOnGlobalLayoutListener(this)
        return this
    }

    fun unregisterView() {
        mRootView?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
    }

    override fun onGlobalLayout() {
        mRootView?.apply {
            val r = Rect()
            // r will be populated with the coordinates of your view that area still visible.
            this.getWindowVisibleDisplayFrame(r)

            var bottomHeight = 0
            val context = context
            if (context is Activity) {
                bottomHeight = ViewUtils.getBottomNavigationBarHeight(context)
            }
            val heightDiff = this.rootView.height - (r.bottom - r.top) - StatusBarUtils.getStatusBarHeight() - bottomHeight
            onSoftKeyboardChanged?.invoke(heightDiff)
            if (!isSoftKeyboardOpened && heightDiff > 300) { // if more than 300 pixels, its probably a keyboard...
                isSoftKeyboardOpened = true
                onSoftKeyboardOpened?.invoke(heightDiff)
            } else if (isSoftKeyboardOpened && heightDiff < 300) {
                isSoftKeyboardOpened = false
                onSoftKeyboardClosed?.invoke()
            }
        }
    }

    fun setSoftKeyboardOpenListener(listener: (keyboardHeightInPx: Int) -> Unit): SoftKeyboardHelper {
        onSoftKeyboardOpened = listener
        return this
    }

    fun setSoftKeyboardClosedListener(listener: () -> Unit): SoftKeyboardHelper {
        onSoftKeyboardClosed = listener
        return this
    }

    fun setSoftKeyboardChangeListener(listener: (keyboardHeightInPx: Int) -> Unit): SoftKeyboardHelper {
        onSoftKeyboardChanged = listener
        return this
    }

    /**
     * Default value is zero (0)
     *
     * @return last saved keyboard height in px
     */
    fun getSoftKeyboardHeightInpx(): Int {
        return lastSoftKeyboardHeightInPx
    }

    /**
     * 软键盘是否在展示
     */
    fun isSoftKeyboardOpened(): Boolean {
        return isSoftKeyboardOpened
    }
}