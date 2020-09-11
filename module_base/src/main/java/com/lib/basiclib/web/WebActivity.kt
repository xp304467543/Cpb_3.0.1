package com.lib.basiclib.web

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import com.lib.basiclib.base.activity.BaseSwipeBackActivity
import com.lib.basiclib.base.fragment.BaseFragment
import com.lib.basiclib.utils.AppUtils

/**

 * @since 18-7-25 下午2:18
 *
 * 跳转Web页面的中转，只用来分发H5Fragment页面
 */

class WebActivity : BaseSwipeBackActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.addFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED)
    }

    override fun getContentFragment(): BaseFragment {
        return WebNavFragment()
    }

    override fun isShowToolBar(): Boolean = false

    /**
     * 跳转暂时使用传统方法
     */
    companion object {
        fun start(context: Context?, url: String?, title: String? = null, webBack: Boolean = true) {
            var contextTemp = context
            if (context == null) contextTemp = AppUtils.getContext()
            val intent = Intent(contextTemp, WebActivity::class.java)
            if (context == null) intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra(WebConstant.KEY_WEB_URL, url)
            intent.putExtra(WebConstant.KEY_WEB_TITLE, title)
            intent.putExtra(WebConstant.KEY_WEB_BACK, webBack)
            intent.putExtra(WebConstant.KEY_WEB_SWIPE_BACK, false)
            contextTemp?.startActivity(intent)
        }
    }

}