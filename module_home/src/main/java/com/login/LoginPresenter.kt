package com.login

import android.widget.TextView
import com.customer.data.UserInfoSp
import com.customer.data.login.LoginApi
import com.customer.data.login.LoginResponse
import com.customer.data.login.LoginSuccess
import com.customer.data.login.RegisterSuccess
import com.customer.utils.AESUtils
import com.customer.utils.CountDownTimerUtils
import com.customer.utils.JsonUtils
import com.google.gson.Gson
import com.hwangjr.rxbus.RxBus
import com.lib.basiclib.base.mvp.BaseMvpPresenter
import com.lib.basiclib.utils.ToastUtils
import cuntomer.net.BaseApi
import kotlinx.android.synthetic.main.act_login.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/23
 * @ Describe
 *
 */
class LoginPresenter : BaseMvpPresenter<LoginAct>(), BaseApi {


    /**
     * 倒计时
     */

    fun time(textView: TextView) {
        val mCountDownTimerUtils = CountDownTimerUtils(textView, 120000, 1000)
        mCountDownTimerUtils.start()
    }


    /**
     * 获取验证码
     */

    fun userGetCode(textView: TextView, phone: String, type: String) {
        LoginApi.userGetCode(phone, type) {
            if (mView.isActive()) {
                onSuccess {
                    time(textView)
                    ToastUtils.showToast("验证码已发送")
                    if (type == "login") mView.isGetIdentify = true
                    if (type == "reg") mView.isGetRegisterIdentify = true
                }
                onFailed {
                    ToastUtils.showToast(it.getMsg())
                }
            }
        }
    }

    /**
     * 密码登录
     */
    fun userLoginWithPassWord(phone: String, passWord: String, loadMode: String) {
        LoginApi.userLoginWithPassWord(phone, passWord, loadMode) {
            onSuccess {
                it.data?.asJsonObject?.get("user_type")?.asString?.let { it1 -> UserInfoSp.setUserType(it1) }
                val str = AESUtils.decrypt(getBase64Key(), it.encryption)
                val res = str?.let { it1 -> JsonUtils.fromJson(it1, LoginResponse::class.java) }
                res?.let { result ->
                    UserInfoSp.putToken(result.token)
                    getLoginInfo("Bearer ${result.token}")
                    UserInfoSp.putRandomStr(result.random_str)
                }
            }
            onFailed {
                ToastUtils.showToast(it.getMsg())
                mView.hidePageLoadingDialog()
            }
        }


    }

    /**
     * 验证码登录
     */
    fun userLoginWithIdentify(phoneNum: String, captcha: String, isAutoLogin: Int) {
        if (mView.isActive()) {
            LoginApi.userLoginWithIdentify(phoneNum, captcha, isAutoLogin) {
                onSuccess {
                    it.data?.asJsonObject?.get("user_type")?.asString?.let { it1 ->
                        UserInfoSp.setUserType(
                            it1
                        )
                    }
                    val str = AESUtils.decrypt(getBase64Key(), it.encryption)
                    val last = str?.let { it1 -> JsonUtils.fromJson(it1, LoginResponse::class.java) }
                    last?.let { result ->
                        UserInfoSp.putToken(result.token)
                        getLoginInfo("Bearer ${result.token}")
                        UserInfoSp.putRandomStr(result.random_str)
                    }
                }
                onFailed {
                    ToastUtils.showToast(it.getMsg())
                    mView.hidePageLoadingDialog()
                }
            }
        }
    }

    /**
     * 登录成功信息获取
     */
    private fun getLoginInfo(token: String, isReg: Boolean = false) {
        LoginApi.getLoginInfo(token) {
            if (mView.isActive()) {
                onSuccess {
                    mView.hidePageLoadingDialog()
                    mView.setUserInfo(it)
                    if (isReg) RxBus.get().post(RegisterSuccess(true))
                    RxBus.get().post(LoginSuccess(""))
                    mView.finish()
                }
                onFailed {
                    mView.hidePageLoadingDialog()
                    ToastUtils.showToast(it.getMsg())
                }
            }
        }
    }


    /**
     * 注册
     */
    fun userRegister(
        phone: String,
        code: String,
        password: String,
        is_auto_login: String,
        market_code: String
    ) {
        if (mView.isActive()) {

            LoginApi.userRegister(phone, code, password, is_auto_login, market_code) {
                if (mView.isActive()) {
                    onSuccess {
                        it.data?.asJsonObject?.get("user_type")?.asString?.let { it1 -> UserInfoSp.setUserType(it1) }
                        val str = AESUtils.decrypt(getBase64Key(), it.encryption?:"99999999999")
                        val last = str?.let { it1 -> JsonUtils.fromJson(it1, LoginResponse::class.java) }
                        last?.let { result ->
                            UserInfoSp.putToken(result.token)
                            UserInfoSp.putRandomStr(result.random_str)
                            getLoginInfo("Bearer ${result.token}", true)
                        }
                    }
                    onFailed {
                        mView.hidePageLoadingDialog()
                        ToastUtils.showToast(it.getMsg())
                    }
                }
            }
        }
    }

    /**
     * 找回密码
     */
    fun getPass(phone: String, captcha: String, new_pwd: String) {
        LoginApi.getLoginPass(phone, captcha, new_pwd) {
            if (mView.isActive()) {
                onSuccess {
                    mView.etChange1.setText("")
                    mView.etChange2.setText("")
                    mView.etForgetPassWord.setText("")
                    mView.etForgetPhone.setText("")
                    mView.registerMode = 0
                    mView.tvRegister.text = "立即注册"
                    mView.setVisible(mView.containerLogin)
                    mView.setGone(mView.containerLoginRegister)
                    mView.setGone(mView.containerForget)
                    mView.setGone(mView.containerChange)

                }
                onFailed { ToastUtils.showToast(it.getMsg().toString()) }
            }
        }
    }

    /**
     * 检测邀请码
     */
    fun checkMarkCode(phone: String, captcha: String, market_code: String) {
        val map = hashMapOf<String, Any>()
        map["phone"] = phone
        map["captcha"] = captcha
        map["market_code"] = market_code
        map["need_market_code"] = 0
        map["is_auto_login"] = 1
        map["client_type"] = 3
        AESUtils.encrypt(LoginApi.getBase64Key(), Gson().toJson(map))?.let { it ->
            LoginApi.checkMarkCode(it) {
                onSuccess {
                    if (mView.isActive()) {
//                        userRegister(phone, captcha, mView.etRegisterPassWord.text.toString(), "1")
                    }
                }
                onFailed { err ->
                    ToastUtils.showToast(err.getMsg() ?: "服务器异常")
                }
            }
        }
    }
}