package com.login

import android.graphics.Color
import android.text.InputType
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.text.style.ForegroundColorSpan
import androidx.core.content.ContextCompat
import com.customer.ApiRouter
import com.customer.AppConstant
import com.customer.component.dialog.DialogLoginWeb
import com.customer.data.UserInfoSp
import com.customer.data.login.LoginApi
import com.customer.data.login.LoginInfoResponse
import com.customer.data.urlCustomer
import com.home.R
import com.hwangjr.rxbus.RxBus
import com.lib.basiclib.base.mvp.BaseMvpActivity
import com.lib.basiclib.utils.FastClickUtil
import com.lib.basiclib.utils.StatusBarUtils
import com.lib.basiclib.utils.ToastUtils
import com.xiaojinzi.component.anno.RouterAnno
import com.xiaojinzi.component.impl.Router
import kotlinx.android.synthetic.main.act_login.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/23
 * @ Describe
 *
 */
@RouterAnno(host = "Home", path = "login")
class LoginAct : BaseMvpActivity<LoginPresenter>() {



    var loadMode = 1 //  0-验证码登录  1-密码登录

    var registerMode = 0 // 0-注册 1-登录

    var isGetIdentify = false //是否获取过登录验证码

    var isGetRegisterIdentify = false //是否获取过登录验证码

    private var isGetSingCode = false

    override val layoutResID = R.layout.act_login

    override fun attachView() = mPresenter.attachView(this)

    override fun attachPresenter() = LoginPresenter()

    override fun isOverride() = true

    override fun isStatusBarTranslate() = false

    override fun isRegisterRxBus() = true


    override fun initContentView() {
        StatusBarUtils.setStatusBarForegroundColor(this, true)
        val spannableString = SpannableString("登录或注册即表示您同意《乐购协议》")
        spannableString.setSpan(
            ForegroundColorSpan(Color.parseColor("#5393f8")),
            11,
            spannableString.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        tvLoginDescription.text = spannableString

        if(AppConstant.isMain){
            etRegisterInviteNum.hint = "邀请码(必填)"
        }else  etRegisterInviteNum.hint = "邀请码(选填)"

        if (intent != null) {
            if (intent.getIntExtra("dialogLogin", 0) == 1) {
                loginType()
            } else if (intent.getIntExtra("dialogLogin", 0) == 2) {
                loginOrRegister()
            }
        }
//        if (isReport) {
//            setVisible(etRegisterInviteNum)
//            setVisible(linLast2)
//        } else {
//            setVisibility(etRegisterInviteNum, false)
//            setVisibility(linLast2, false)
//        }
    }

    override fun initEvent() {
        tvForgetWithMode.setOnClickListener {
            if (!FastClickUtil.isFastClick()) Router.withApi(ApiRouter::class.java)
                .toGlobalWeb(UserInfoSp.getCustomer() ?: urlCustomer)
        }
        tvChangeInWithMode.setOnClickListener {
            if (!FastClickUtil.isFastClick()) Router.withApi(ApiRouter::class.java)
                .toGlobalWeb(UserInfoSp.getCustomer() ?: urlCustomer)
        }
        tvLContactCustomer.setOnClickListener {
            if (!FastClickUtil.isFastClick()) Router.withApi(ApiRouter::class.java)
                .toGlobalWeb(UserInfoSp.getCustomer() ?: urlCustomer)
        }
        imgLoginClose.setOnClickListener {
            finish()
        }
        //协议
        tvLoginDescription.setOnClickListener {
            DialogLoginWeb(getContext()!!).show()
        }

        // 登录-----
        tvLoginIn.setOnClickListener {

            if (etLoginPhone.text.isEmpty()) {
                ToastUtils.showToast("请输入手机号")
                return@setOnClickListener
            }
            if (etLoginPhone.text.length < 11) {
                ToastUtils.showToast("请输入正确11位手机号码")
                return@setOnClickListener
            }
            if (loadMode == 0) {
                if (etLoginPassWord.text.isEmpty()) {
                    ToastUtils.showToast("请输入密码")
                    return@setOnClickListener
                }
                if (etLoginPassWord.text.length < 6) {
                    ToastUtils.showToast("密码长度不得小于6位")
                    return@setOnClickListener
                }
            } else {
                if (!isGetIdentify) {
                    ToastUtils.showToast("请先获取验证码")
                    return@setOnClickListener
                }
                if (etLoginPassWord.text.isEmpty() || etLoginPassWord.text.length < 4) {
                    ToastUtils.showToast("请输入4位验证码")
                    return@setOnClickListener
                }
            }
            showPageLoadingDialog("登录中..")
            if (loadMode == 0) {
                mPresenter.userLoginWithPassWord(
                    etLoginPhone.text.toString(),
                    etLoginPassWord.text.toString(),
                    "1"
                )
            } else mPresenter.userLoginWithIdentify(
                etLoginPhone.text.toString(),
                etLoginPassWord.text.toString(),
                1
            )
        }

        tvLoginWithMode.setOnClickListener {
            etLoginPassWord.setText("")
            loginType()

        }
        tvForgetPassWord.setOnClickListener {
            if (loadMode == 0) {
                //忘记密码
                setVisible(containerForget)
                setGone(containerLoginRegister)
                setGone(containerLogin)
                registerMode = 1
                tvRegister.text = "立即登录"
            } else {
                if (etLoginPhone.text.isEmpty()) {
                    ToastUtils.showToast("请输入手机号")
                    return@setOnClickListener
                }
                if (etLoginPhone.text.length < 11) {
                    ToastUtils.showToast("请输入正确11位手机号码")
                    return@setOnClickListener
                }
                mPresenter.userGetCode(tvForgetPassWord, etLoginPhone.text.toString(), "login")
            }
        }
        //登录Or注册
        tvRegister.setOnClickListener {
            loginOrRegister()
        }
        //注册--验证码
        tvRegisterIdentify.setOnClickListener {
            if (etRegisterPhone.text.isEmpty()) {
                ToastUtils.showToast("请输入手机号")
                return@setOnClickListener
            }
            if (etRegisterPhone.text.length < 11) {
                ToastUtils.showToast("请输入正确11位手机号码")
                return@setOnClickListener
            }
            mPresenter.userGetCode(tvRegisterIdentify, etRegisterPhone.text.toString(), "reg")
        }
        //注册
        tvRegisterIn.setOnClickListener {
            if (etRegisterPhone.text.isEmpty()) {
                ToastUtils.showToast("请输入手机号")
                return@setOnClickListener
            }
            if (etRegisterPhone.text.length < 11) {
                ToastUtils.showToast("请输入正确11位手机号码")
                return@setOnClickListener
            }
            if (!isGetRegisterIdentify) {
                ToastUtils.showToast("请先获取验证码")
                return@setOnClickListener
            }
            if (etRegisterIdentify.text.isEmpty() || etRegisterIdentify.text.length < 4) {
                ToastUtils.showToast("请输入4位验证码")
                return@setOnClickListener
            }
            if (etRegisterPassWord.text.isEmpty()) {
                ToastUtils.showToast("请输入密码")
                return@setOnClickListener
            }
            if (etRegisterPassWord.text.length < 6) {
                ToastUtils.showToast("密码长度不得小于6位")
                return@setOnClickListener
            }
            if (AppConstant.isMain) {
                if (etRegisterInviteNum.text.isEmpty()) {
                    ToastUtils.showToast("请输入邀请码")
                    return@setOnClickListener
                }
            }
//            if (isReport) {
//                mPresenter.checkMarkCode(etRegisterPhone.text.toString(), etRegisterIdentify.text.toString(), etRegisterInviteNum.text.toString())
//            } else {
            showPageLoadingDialog("注册中..")
            mPresenter.userRegister(
                etRegisterPhone.text.toString(),
                etRegisterIdentify.text.toString(),
                etRegisterPassWord.text.toString(),
                "1",
                etRegisterInviteNum.text.toString()
            )
        }
        /**
         * 忘记密码
         */
        tvForget.setOnClickListener {
            if (isMobileNumber(etForgetPhone.text.toString())) {
                LoginApi.userGetCode(etForgetPhone.text.toString(), "retrieve_pwd") {
                    onSuccess {
                        isGetSingCode = true
                        mPresenter.time(tvForget)
                    }
                    onFailed { ToastUtils.showToast(it.getMsg()) }
                }
            } else ToastUtils.showToast("请输入正确11位手机号码")
        }

        tvForgetIn.setOnClickListener {
            if (isMobileNumber(etForgetPhone.text.toString())) {
                if (isGetSingCode) {
                    if (!TextUtils.isEmpty(etForgetPassWord.text.toString())) {
                        setGone(containerForget)
                        setVisible(containerChange)
                    } else ToastUtils.showToast("请输入验证码")
                } else ToastUtils.showToast("请获取验证码")
            } else ToastUtils.showToast("请输入正确11位手机号码")
        }
        /**
         * 找回登录密码
         */
        tvChangeIn.setOnClickListener {
            if (TextUtils.isEmpty(etChange1.text.toString())) {
                ToastUtils.showToast("请填写新密码")
                return@setOnClickListener
            }
            if (etChange1.text.length < 6) {
                ToastUtils.showToast("密码长度最少为6位")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(etChange2.text.toString())) {
                ToastUtils.showToast("请确认新密码")
                return@setOnClickListener
            }
            if (etChange1.text.toString() != etChange2.text.toString()) {
                ToastUtils.showToast("两次新密码输入不一致")
                return@setOnClickListener
            }
            mPresenter.getPass(
                etForgetPhone.text.toString(),
                etForgetPassWord.text.toString(),
                etChange1.text.toString()
            )
        }
    }


    //登录方式
    private fun loginType() {
        if (loadMode == 0) {
            loadMode = 1
            etLoginPassWord.hint = getString(R.string.login_profile)
            tvForgetPassWord.text = getString(R.string.login_get_profile)
            etLoginPassWord.inputType = InputType.TYPE_CLASS_NUMBER
            //选中显示密码
            etLoginPassWord.transformationMethod = HideReturnsTransformationMethod.getInstance()
            tvForgetPassWord.setTextColor(ContextCompat.getColor(this, R.color.color_FF513E))
            tvLoginWithMode.text = "使用账号密码登录"
        } else {
            loadMode = 0
            etLoginPassWord.hint = getString(R.string.login_password)
            tvForgetPassWord.text = getString(R.string.login_forget_password)
            etLoginPassWord.inputType = InputType.TYPE_CLASS_TEXT
            //隐藏密码
            etLoginPassWord.transformationMethod = PasswordTransformationMethod.getInstance()
            tvForgetPassWord.setTextColor(ContextCompat.getColor(this, R.color.color_999999))
            tvLoginWithMode.text = "使用验证码登录"
        }
    }

    //登录Or注册
    private fun loginOrRegister() {
        if (registerMode == 0) {
            registerMode = 1
            tvRegister.text = "立即登录"
            setGone(containerLogin)
            setVisible(containerLoginRegister)
            setGone(containerForget)
        } else {
            registerMode = 0
            tvRegister.text = "立即注册"
            setGone(containerLoginRegister)
            setVisible(containerLogin)
            setGone(containerForget)
        }
    }


    fun setUserInfo(result: LoginInfoResponse) {
        RxBus.get().post(result)
        result.user_id?.let { UserInfoSp.putUserId(it) }
        UserInfoSp.putUserUniqueId(result.unique_id)
        result.phone?.let { UserInfoSp.putUserPhone(it) }
        UserInfoSp.putUserSex(result.gender)
        result.nickname?.let { UserInfoSp.putUserNickName(it) }
        result.username?.let { UserInfoSp.putUserName(it) }
        result.profile?.let { UserInfoSp.putUserProfile(it) }
        result.avatar?.let { UserInfoSp.putUserPhoto(it) }
        UserInfoSp.putIsLogin(true)//更新登录状态
        UserInfoSp.putUserFans(result.following + "," + result.followers + "," + result.like)
    }

    /**
     * 验证手机号码是否合法
     * 176, 177, 178;
     * 180, 181, 182, 183, 184, 185, 186, 187, 188, 189;
     * 145, 147;
     * 130, 131, 132, 133, 134, 135, 136, 137, 138, 139;
     * 150, 151, 152, 153, 155, 156, 157, 158, 159;
     * "13"代表前两位为数字13,
     * "[0-9]"代表第二位可以为0-9中的一个,
     * "[^4]" 代表除了4
     * "\\d{8}"代表后面是可以是0～9的数字, 有8位。
     */
    private fun isMobileNumber(mobiles: String): Boolean {
        val telRegex =
            "^[1](([3|5|8][\\d])|([4][4,5,6,7,8,9])|([6][2,5,6,7])|([7][^9])|([9][1,8,9]))[\\d]{8}$"
        return !TextUtils.isEmpty(mobiles) && mobiles.matches(telRegex.toRegex())
    }

}