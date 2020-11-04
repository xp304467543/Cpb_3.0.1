package com.fh.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import com.customer.ApiRouter
import com.customer.data.UserInfoSp
import com.customer.data.home.HomeApi
import com.customer.data.mine.MineApi
import com.customer.data.urlCustomer
import com.fh.R
import com.glide.GlideUtil
import com.lib.basiclib.utils.FastClickUtil
import com.lib.basiclib.utils.StatusBarUtils
import com.lib.basiclib.utils.ViewUtils
import com.xiaojinzi.component.impl.Router
import cuntomer.api.WebUrlProvider
import cuntomer.constant.ApiConstant
import kotlinx.android.synthetic.main.activity_splash.*
import me.jessyan.autosize.internal.CancelAdapt

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/20
 * @ Describe 开屏页面，启动时会有图片背景，不需要使用适配
 *
 */
class SplashActivity : Activity(), CancelAdapt {

    var goToURL = ""

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            //设置坚屏 一定要放到try catch里面，否则会崩溃
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } catch (e: Exception) {
        }
        setContentView(R.layout.activity_splash)
        StatusBarUtils.setStatusBarByFlags(this)
        initContent()
    }

    private fun initContent() {
        btEnter?.setOnClickListener {
            if (!FastClickUtil.isFastClickActivity(this.localClassName)) {
                startActivity(Intent(baseContext, MainActivity::class.java))
                finish()
            }
        }
        startImg?.setOnClickListener {
            if (goToURL != "") {
                Router.withApi(ApiRouter::class.java).toGlobalWeb(goToURL)
            }
        }
//        tvDaoJiShi.setOnClickListener {
//            if (FastClickUtils.isFastClick1000()) {
//                isTurn = false
//                timer?.cancel()
//                startActivity(Intent(baseContext, MainActivity::class.java))
//                finish()
//            }
//        }
//        timer = object : CountDownTimer(4000, 1000) {
//            override fun onFinish() {
//                if (isTurn) {
//                    startActivity(Intent(baseContext, MainActivity::class.java))
//                    finish()
//                }
//            }
//
//            override fun onTick(millisUntilFinished: Long) {
//                //文字显示3秒跳转
//                tvDaoJiShi.text = (millisUntilFinished / 1000).toString() + "秒跳转"
//            }
//        }
        initSysTemUrl()
        initSome()
    }


    private fun initSysTemUrl() {
        HomeApi.getSystemUrl {
            onSuccess { systemUrl ->
                ApiConstant.API_URL_DEV_LIVE_S = systemUrl.live_api
                ApiConstant.API_URL_DEV_USER_S = systemUrl.user_api
                ApiConstant.API_MOMENTS_FORM_S = systemUrl.forum_api
                ApiConstant.API_LOTTERY_S = systemUrl.lottery_api
                ApiConstant.API_VIDEO = systemUrl.movie_api
                WebUrlProvider.ALL_URL_WEB_SOCKET_MAIN_S = systemUrl.notice_url
                WebUrlProvider.API_URL_WEB_SOCKET_MAIN_S = systemUrl.chat_url
                ViewUtils.setGone(btWaite)
                ViewUtils.setVisible(btEnter)
            }
        }
        MineApi.getLotteryUrl {
            onSuccess {
                UserInfoSp.putCustomer(it.customer ?: urlCustomer)
            }
            onFailed {
                UserInfoSp.putCustomer(urlCustomer)
            }
        }
    }

    private fun initSome() {
        HomeApi.getLotteryUrl {
            onSuccess {
                UserInfoSp.putCustomer(it.customer)
                if (!isDestroyed) GlideUtil.splashLoad(
                    this@SplashActivity,
                    it.app_start_banner?.image_url.toString(),
                    startImg
                )
//                timer?.start()
                goToURL = it.app_start_banner?.url ?: ""
            }
            onFailed { }
        }

    }
}