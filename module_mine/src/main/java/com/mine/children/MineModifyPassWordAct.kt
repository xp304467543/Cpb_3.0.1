package com.mine.children

import android.text.TextUtils
import android.widget.TextView
import com.customer.ApiRouter
import com.customer.component.dialog.DialogSuccess
import com.customer.data.LoginOut
import com.customer.utils.CountDownTimerUtils
import com.customer.utils.JsonUtils
import com.hwangjr.rxbus.RxBus
import com.lib.basiclib.base.activity.BaseNavActivity
import com.lib.basiclib.utils.FastClickUtil
import com.lib.basiclib.utils.ToastUtils
import com.mine.R
import com.customer.data.mine.MineApi
import com.customer.data.mine.MineApi.modifyPassWord
import com.customer.data.mine.MinePassWordTime
import com.xiaojinzi.component.anno.RouterAnno
import com.xiaojinzi.component.impl.Router
import com.customer.component.dialog.GlobalDialog
import com.customer.data.UserInfoSp
import kotlinx.android.synthetic.main.act_modify_pass_word.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/23
 * @ Describe
 *
 */
@RouterAnno(host = "Mine", path = "modifyPassWord")
class MineModifyPassWordAct : BaseNavActivity() {

    private var isGetSingCode = false


    override fun getContentResID() = R.layout.act_modify_pass_word

    override fun isSwipeBackEnable() = true

    override fun getPageTitle() = "密码修改"

    override fun isShowBackIconWhite() = false


    override fun initEvent() {
        modifyByPhone.setOnClickListener {
            setGone(mode_1)
            setVisible(mode_2)
        }
        modifyByPass.setOnClickListener {
            setGone(mode_2)
            setVisible(mode_1)
        }



        btModify.setOnClickListener {
            if (!FastClickUtil.isFastClick()) {
                if (TextUtils.isEmpty(tvNewPassOld.text.toString())) {
                    ToastUtils.showToast("请填写旧密码")
                    return@setOnClickListener
                }
                if (tvNewPassOld.text.length < 6) {
                    ToastUtils.showToast("密码长度最少为6位")
                    return@setOnClickListener
                }
                if (TextUtils.isEmpty(tvNewPass1.text.toString())) {
                    ToastUtils.showToast("请填写新密码")
                    return@setOnClickListener
                }
                if (tvNewPass1.text.length < 6) {
                    ToastUtils.showToast("密码长度最少为6位")
                    return@setOnClickListener
                }
                if (TextUtils.isEmpty(tvNewPass2.text.toString())) {
                    ToastUtils.showToast("请确认新密码")
                    return@setOnClickListener
                }
                if (tvNewPass2.text.length < 6) {
                    ToastUtils.showToast("密码长度最少为6位")
                    return@setOnClickListener
                }
                if (tvNewPassOld.text.toString() == tvNewPass2.text.toString() || tvNewPassOld.text.toString() == tvNewPass1.text.toString()) {
                    ToastUtils.showToast("新密码与旧密码不能相同")
                    return@setOnClickListener
                }
                if (tvNewPass1.text.toString() == tvNewPass2.text.toString()) {
                    showPageLoadingDialog()
                    modifyPassWord(tvNewPassOld.text.toString(), tvNewPass1.text.toString()) {
                        onSuccess {
                            hidePageLoadingDialog()
                            val dialog = DialogSuccess(this@MineModifyPassWordAct, "修改成功", R.mipmap.ic_dialog_success)
                            dialog.setOnDismissListener {
                                GlobalDialog.spClear()
                                RxBus.get().post(LoginOut(true))
                                finish()
                                Router.withApi(ApiRouter::class.java).toLogin()
                            }
                            dialog.show()
                        }
                        onFailed {
                            if (it.getCode() == 1002) {
                                ToastUtils.showToast(
                                    it.getMsg().toString() + "," + "您还有" + JsonUtils.fromJson(
                                        it.getDataCode().toString(), MinePassWordTime::class.java
                                    ).remain_times.toString() + "次机会"
                                )
                            } else ToastUtils.showToast(it.getMsg())
                            hidePageLoadingDialog()
                        }
                    }
                } else ToastUtils.showToast("两次新密码输入不一致")
            }
        }

        /**
         * 手机验证码修改
         */
        tvGetIdentifyCodePass.setOnClickListener {
            if (isMobileNumber(etPhonePass.text.toString())) {
                if (UserInfoSp.getUserPhone() == etPhonePass.text.toString()) {
                    MineApi.userGetCode(etPhonePass.text.toString(), "chg_pwd") {
                        onSuccess {
                            time(tvGetIdentifyCodePass)
                            isGetSingCode = true
                        }
                        onFailed { ToastUtils.showToast(it.getMsg()) }
                    }
                } else ToastUtils.showToast("该手机号与当前用户不匹配")
            } else ToastUtils.showToast("请输入正确11位手机号码")

        }

        btNext.setOnClickListener {
            if (!TextUtils.isEmpty(etPhonePass.text)) {
                if (UserInfoSp.getUserPhone() == etPhonePass.text.toString()) {
                    if (isGetSingCode) {
                        if (!TextUtils.isEmpty(etPhoneSignNum.text)) {
                            setGone(mode_1)
                            setGone(mode_2)
                            setVisible(mode_3)
                        } else ToastUtils.showToast("请输验证码")
                    } else ToastUtils.showToast("请先获取验证码")
                } else ToastUtils.showToast("请确认手机号是否正确")
            } else ToastUtils.showToast("请输手机号")
        }


        btSetPassWord.setOnClickListener {

            if (TextUtils.isEmpty(etPhoneNewPass11.text.toString())) {
                ToastUtils.showToast("请输入新密码")
                return@setOnClickListener
            }
            if (etPhoneNewPass11.text.length < 6) {
                ToastUtils.showToast("密码长度最少为6位")
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(etNewPass11Sure.text.toString())) {
                ToastUtils.showToast("请输入新密码")
                return@setOnClickListener
            }
            if (etNewPass11Sure.text.length < 6) {
                ToastUtils.showToast("密码长度最少为6位")
                return@setOnClickListener
            }
            if (etNewPass11Sure.text.toString() != etPhoneNewPass11.text.toString()) {
                ToastUtils.showToast("两次新密码输入不一致")
                return@setOnClickListener
            }
            showPageLoadingDialog()
            modifyPassWord(
                etPhonePass.text.toString(),
                etPhoneSignNum.text.toString(),
                etPhoneNewPass11.text.toString(),
                1
            ) {
                onSuccess {
                    val dialog = DialogSuccess(
                        this@MineModifyPassWordAct,
                        "修改成功",
                        R.mipmap.ic_dialog_success
                    )
                    dialog.setOnDismissListener {
                        GlobalDialog.spClear()
                        RxBus.get().post(LoginOut(true))
                        finish()
                        Router.withApi(ApiRouter::class.java).toLogin()
                    }
                    dialog.show()
                    hidePageLoadingDialog()
                }
                onFailed {
                    ToastUtils.showToast("修改失败:${it.getMsg()}")
                    hidePageLoadingDialog()
                }
            }
        }
    }

    /**
     * 倒计时
     */

    fun time(textView: TextView) {
        val mCountDownTimerUtils = CountDownTimerUtils(textView, 120000, 1000)
        mCountDownTimerUtils.start()
    }

    /**
     * 验证手机号码是否合法
     * 176, 177, 178;
     * 180, 181, 182, 183, 184, 185, 186, 187, 188, 189;
     * 145, 147;
     * 130, 131, 132, 133, 134, 135, 136, 137, 138, 139;
     * 150, 151, 152, 153, 155, 156, 157, 158, 159;
     *
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