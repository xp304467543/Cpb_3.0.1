package com.customer.component.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.customer.ApiRouter
import com.customer.data.LoginOut
import com.fh.module_base_resouce.R
import com.hwangjr.rxbus.RxBus
import com.lib.basiclib.utils.ViewUtils
import com.xiaojinzi.component.impl.Router
import kotlinx.android.synthetic.main.dialog_login_tips.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/27
 * @ Describe
 *
 */
class DialogLoginTips (context: Context, val horizontal: Boolean) : Dialog(context) {


    init {
        window!!.setWindowAnimations(R.style.BaseDialogAnim)
        setContentView(R.layout.dialog_login_tips)
        val lp = window!!.attributes
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        lp.height = ViewUtils.dp2px(330)
        window!!.attributes = lp
        initEvent()
    }

    private fun initEvent() {
        imgDialogLoginClose.setOnClickListener {
            dismiss()
        }
        btDialogLogin.setOnClickListener {
            login(1)
        }
        btDialogRegister.setOnClickListener {
            login(2)
        }
    }


    private fun login(mode: Int) {
        dismiss()
        if (!isPort()){
            scanForActivity(context)?.window?.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            setFullScreen(scanForActivity(context),false)
        }
        Router.withApi(ApiRouter::class.java).toLogin(mode)
    }


    override fun dismiss() {
        super.dismiss()
        RxBus.get().post(LoginOut(true))
        if (horizontal) {
            val decorView =scanForActivity(context)?.window?.decorView as ViewGroup
            scanForActivity(context)?.let { hideSysBar(it, decorView) }
        }
    }

    private fun scanForActivity(cont: Context?): Activity? {
        return when (cont) {
            null -> null
            is Activity -> cont
            is ContextWrapper -> scanForActivity(cont.baseContext)
            else -> null
        }

    }

    private fun isPort(): Boolean {
        return ViewUtils.getContext().resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    }

    private fun setFullScreen(activity: Activity?, isFull: Boolean) {
        val orientation = if (isFull) {
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        } else {
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        activity?.requestedOrientation = orientation
    }


    private fun hideSysBar(activity: Activity, decorView: ViewGroup) {
        var uiOptions = decorView.systemUiVisibility
        uiOptions = uiOptions or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            uiOptions = uiOptions or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        }
        decorView.systemUiVisibility = uiOptions
        activity.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }


}