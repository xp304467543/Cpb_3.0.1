package com.fh.view

import android.content.Intent
import android.os.Bundle
import com.customer.component.ActivityDialogSuccess
import com.customer.data.LoginOut
import com.customer.data.OnLine
import com.customer.data.UserInfoSp
import com.customer.data.home.AllSocket
import com.customer.data.login.LoginSuccess
import com.customer.wsmanager.WsManager
import com.customer.wsmanager.listener.WsStatusListener
import com.fh.R
import com.hwangjr.rxbus.RxBus
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.thread.EventThread
import com.lib.basiclib.base.activity.BasePageActivity
import com.lib.basiclib.base.xui.widget.popupwindow.bar.CookieBar
import com.lib.basiclib.utils.LogUtils
import com.lib.basiclib.utils.SoftInputUtils
import com.lib.basiclib.utils.StatusBarUtils
import com.rxnetgo.RxNetGo
import com.xiaojinzi.component.anno.RouterAnno
import cuntomer.api.WebUrlProvider
import okhttp3.OkHttpClient
import okhttp3.Response
import okio.ByteString
import org.json.JSONObject
import java.util.*
import java.util.concurrent.TimeUnit

@RouterAnno(host = "App", path = "main")
class MainActivity : BasePageActivity() {

    var clientId = "-1"

    override fun getPageFragment() = MainFragment()


    override fun isRegisterRxBus() = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtils.setStatusBarForegroundColor(this, true)
        allSocket()
    }

    /**
     * 退出时取消网络相关的请求
     */
    override fun onDestroy() {
        // 取消所有网络请求的订阅
        RxNetGo.getInstance().dispose()
        // 清除软键盘中过滤的View
        SoftInputUtils.clearFilterView()
        mWsManager?.stopConnect()
        super.onDestroy()
    }


    /**
     * 全局socket
     */
    var mWsManager: WsManager? = null
    private var mWsStatusListener: WsStatusListener? = null
    private var isReconnect = false
    private var mTimer: Timer? = null
    private fun allSocket() {
        LogUtils.d("AllWsManager-----WebUrlProvider="+WebUrlProvider.getALLBaseUrl())
        initStatusListener()
        mWsManager = WsManager.Builder(this)
            .client(
                OkHttpClient().newBuilder()
                .pingInterval(1000 * 50, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build())
            .needReconnect(true).wsUrl(WebUrlProvider.getALLBaseUrl())
            .build()
        mWsManager?.setWsStatusListener(mWsStatusListener)
        mWsManager?.startConnect()
    }

    private fun initStatusListener() {
        mWsStatusListener = object : WsStatusListener() {
            override fun onOpen(response: Response) {
                super.onOpen(response)
                LogUtils.d("AllWsManager-----onOpen response=$response")

            }

            override fun onMessage(text: String) {
                super.onMessage(text)
                LogUtils.d("AllWsManager-----onOpen text=$text")
                socketMessage(text)
            }

            override fun onMessage(bytes: ByteString) {
                super.onMessage(bytes)
                LogUtils.d("AllWsManager-----onMessage$bytes")
            }

            override fun onReconnect() {
                super.onReconnect()
                isReconnect = true
                LogUtils.d("AllWsManager-----onReconnect")
            }

            override fun onClosing(code: Int, reason: String) {
                super.onClosing(code, reason)
                LogUtils.d("AllWsManager-----onClosing")
            }

            override fun onClosed(code: Int, reason: String) {
                super.onClosed(code, reason)
                LogUtils.d("AllWsManager-----onClosed")
            }

            override fun onFailure(t: Throwable?, response: Response?) {
                super.onFailure(t, response)
                LogUtils.d("AllWsManager-----onFailure$response=$t")
                if (mTimer != null) {
                    mTimer?.cancel()
                    mTimer = null
                }
            }
        }
    }

    fun socketMessage(text: String) {
        if (text.isNotEmpty()) {
            val res = WebUrlProvider.getData<AllSocket>(text, AllSocket::class.java)
            when (res?.type) {
                "connected" -> {
                    mWsManager?.sendMessage(getLogin(res.client_id ?: "0"))
                    clientId = res.client_id ?: "-1"
                }
                "login" -> {
                    mWsManager?.sendMessage(ping(clientId))
                    if (mTimer == null) mTimer = Timer()
                    mTimer?.schedule(object : TimerTask() {
                        override fun run() {
                            mWsManager?.sendMessage(ping(clientId))
                            LogUtils.d("AllWsManager-----发送了心跳")
                        }
                    }, 0, 1000 * 54)
                }
                "ServerPush" -> {
                    when (res.dataType) {
                        "open_lottery_push" -> {
                            mWsManager?.sendMessage(makeSure(res.data?.msg_id))
                            systemDialog(res.data?.msg ?: "获取消息失败")
                        }
                        "pop_result_push" -> {
                            mWsManager?.sendMessage(makeSurePop(res.data?.msg_id))
                            val intent = Intent(this, ActivityDialogSuccess::class.java)
                            intent.putExtra("msgSuccess",res.data?.msg)
                            intent.putExtra("is_success",res.data?.is_success)
                            startActivity(intent)
                        }
                        "app_online_push" -> {
                            RxBus.get().post(OnLine(res.data?.online))
                        }
                    }
                }
            }
        }
    }

    private fun systemDialog(msg: String) {
        CookieBar.builder(this)
            .setBackgroundColor(R.color.white)
            .setMessage(msg)
            .setMessageColor(R.color.color_333333)
            .setIcon(R.mipmap.ic_live_star)
            .show()
    }

    private fun getLogin(client_id: String, user_id: Int = UserInfoSp.getUserId()): String {
        val jsonObject = JSONObject()
        jsonObject.put("type", "login")
        jsonObject.put("client_id", client_id)
        jsonObject.put("user_id", user_id)
        return jsonObject.toString()
    }

    fun ping(client_id: String): String {
        val jsonObject = JSONObject()
        jsonObject.put("type", "ping")
        jsonObject.put("client_id", client_id)
        return jsonObject.toString()
    }

    private fun makeSure(msg_id: String?): String {
        val jsonObject = JSONObject()
        jsonObject.put("type", "confirm")
        jsonObject.put("event", "open_lottery_push")
        jsonObject.put("client_id", clientId)
        jsonObject.put("user_id", UserInfoSp.getUserId())
        jsonObject.put("msg_id", msg_id)
        return jsonObject.toString()
    }

    private fun makeSurePop(msg_id: String?): String {
        val jsonObject = JSONObject()
        jsonObject.put("type", "confirm")
        jsonObject.put("event", "pop_result_push")
        jsonObject.put("client_id", clientId)
        jsonObject.put("user_id", UserInfoSp.getUserId())
        jsonObject.put("msg_id", msg_id)
        return jsonObject.toString()
    }




    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun login(eventBean: LoginSuccess) {
        mWsManager?.stopConnect()
        allSocket()
        mWsManager?.sendMessage(getLogin(clientId))
    }

    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun loginOut(eventBean: LoginOut) {
        mWsManager?.stopConnect()
        allSocket()
    }

}