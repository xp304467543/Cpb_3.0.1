package com.customer.component.activity

import android.content.Intent
import android.view.KeyEvent
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.customer.data.mine.MineApi
import com.fh.module_base_resouce.R
import com.lib.basiclib.base.activity.BaseNavActivity
import com.lib.basiclib.utils.ToastUtils
import com.lib.basiclib.widget.web.ByWebView
import com.xiaojinzi.component.anno.RouterAnno
import kotlinx.android.synthetic.main.act_web.*


/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/23
 * @ Describe 充值的web
 *
 */
@RouterAnno(host = "Base", path = "rechargeWeb")
class RechargeWebAct : BaseNavActivity() {

    private var byWebView: ByWebView? = null

    override fun getContentResID() = R.layout.act_web

    override fun isSwipeBackEnable() = true

    override fun isShowBackIconWhite() = false

    override fun initData() {
        val money = intent.getFloatExtra("MINE_INVEST_AMOUNT", 0F)
        val id = intent.getIntExtra("MINE_RECHARGE_ID", 0)
        val url = intent.getStringExtra("MINE_RECHARGE_URL")
        when {
            intent.getBooleanExtra("isRen", false) -> {
                byWebView = ByWebView
                    .with(this)
                    .setWebParent(rootWeb, LinearLayout.LayoutParams(-1, -1))
                    .useWebProgress(ContextCompat.getColor(this, R.color.text_red))
                    .loadUrl(url)
            }
            else -> getInvestUrl(money, id, url?:"")
        }
    }

    private fun getInvestUrl(amount: Float, channels: Int, route: String) {
        showPageLoadingDialog()
        MineApi.getToPayUrl(amount, channels, route) {
            onSuccess {
                byWebView = if (it.type == "2"){
                    ByWebView
                        .with(this@RechargeWebAct)
                        .setWebParent(rootWeb, LinearLayout.LayoutParams(-1, -1))
                        .useWebProgress(ContextCompat.getColor(this@RechargeWebAct, R.color.text_red))
                        .loadForm(it.form.replace("\\", "/"))
                }else{
                    ByWebView
                        .with(this@RechargeWebAct)
                        .setWebParent(rootWeb, LinearLayout.LayoutParams(-1, -1))
                        .useWebProgress(ContextCompat.getColor(this@RechargeWebAct, R.color.text_red))
                        .loadUrl(it.url.replace("\\", "/"))
                }

                hidePageLoadingDialog()
            }
            onFailed {
                ToastUtils.showToast(it.getMsg())
                hidePageLoadingDialog()
            }
        }
    }
    //===================WebChromeClient 和 WebViewClient===========================//

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