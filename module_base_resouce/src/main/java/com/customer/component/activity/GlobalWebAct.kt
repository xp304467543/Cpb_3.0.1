package com.customer.component.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.view.KeyEvent
import android.webkit.WebSettings
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.customer.data.home.HomeApi
import com.fh.module_base_resouce.R
import com.lib.basiclib.base.activity.BaseNavActivity
import com.lib.basiclib.utils.TimeUtils
import com.lib.basiclib.utils.ToastUtils
import com.lib.basiclib.widget.web.ByWebView
import com.lib.basiclib.widget.web.OnTitleProgressCallback
import com.xiaojinzi.component.anno.RouterAnno
import kotlinx.android.synthetic.main.act_web.*


/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/23
 * @ Describe
 *
 */
@RouterAnno(host = "Base", path = "web")
class GlobalWebAct : BaseNavActivity() {

    private var byWebView: ByWebView? = null

    override fun getContentResID() = R.layout.act_web

    override fun isSwipeBackEnable() = true

    override fun isShowBackIconWhite() = false


    override fun initContentView() {
        if (!intent.getBooleanExtra("isNews",false)){
            byWebView = ByWebView
                .with(this)
                .setWebParent(rootWeb, LinearLayout.LayoutParams(-1, -1))
                .useWebProgress(ContextCompat.getColor(this, R.color.text_red))
                .setOnTitleProgressCallback(object : OnTitleProgressCallback(){
                    override fun onReceivedTitle(title: String?) {
                        setPageTitle(title?:"")
                    }
                })
                .loadUrl(getUrl())
        }else initNewView()

    }

    //-----更多资讯内容
    private fun initNewView(){
        setVisible(titleNews)
        setPageTitle("最新资讯")
        getNewsInfo()
    }

    @SuppressLint("SetTextI18n", "SetJavaScriptEnabled")
    private fun getNewsInfo() {
        HomeApi.getNewsInfo(intent?.getStringExtra("infoId") ?: "") {
            onSuccess {
                if (!it.isNullOrEmpty()) {
                        tvNewsTitle.text = it[0].title
                        tvNewsInfo.text = it[0].source + "   " + it[0].timegap + "    " + TimeUtils.longToDateStringTime(it[0].createtime?.toLong()?:0)
                        web_copy.loadDataWithBaseURL(null, it[0].detail, "text/html", "utf-8", null)
                        web_copy.settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
                        web_copy.settings.javaScriptEnabled = true
                        web_copy.setBackgroundColor(0)
                } else ToastUtils.showToast("暂无内容")
            }
            onFailed {
                ToastUtils.showToast(it.getMsg().toString())
            }
        }
    }


    //===================WebChromeClient 和 WebViewClient===========================//
    /**
     * 页面空白，请检查scheme是否加上， scheme://host:port/path?query&query 。
     *
     * @return mUrl
     */
    private fun getUrl(): String? {
        return intent.getStringExtra("webActUrl")
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        byWebView?.handleFileChooser(requestCode, resultCode, data)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (byWebView?.handleKeyEvent(keyCode, event) == true) {
            true
        } else {
            super.onKeyDown(keyCode, event)
        }
    }


    override fun onPause() {
        super.onPause()
        byWebView?.onPause()
    }

    override fun onResume() {
        super.onResume()
        byWebView?.onResume()
    }

    override fun onDestroy() {
        byWebView?.onDestroy()
        super.onDestroy()
    }
}