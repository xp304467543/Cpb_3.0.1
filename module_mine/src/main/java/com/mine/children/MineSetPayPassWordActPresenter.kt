package com.mine.children

import android.annotation.SuppressLint
import com.customer.component.dialog.DialogSuccess
import com.customer.data.UserInfoSp
import com.customer.utils.JsonUtils
import com.lib.basiclib.base.mvp.BaseMvpPresenter
import com.mine.R
import com.customer.data.mine.MineApi
import com.customer.data.mine.MinePassWordTime
import kotlinx.android.synthetic.main.act_pay_pass_word.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/23
 * @ Describe
 *
 */
class MineSetPayPassWordActPresenter : BaseMvpPresenter<MineSetPayPassWordAct>() {


    //验证旧密码
    @SuppressLint("SetTextI18n")
    fun verifyPass() {
        mView.showPageLoadingDialog()
        MineApi.verifyPayPass(mView.edit_pay_solid.text.toString()) {
            if (mView.isActive()) {
                onSuccess {
                    mView.hidePageLoadingDialog()
                    mView.edit_pay_solid.clearText()
                    mView.tvTitlePass.text = "请输入新密码"
                    mView.tvSetError.text = ""
                }
                onFailed {
                    mView.hidePageLoadingDialog()
                    mView.edit_pay_solid.clearText()
                    if (it.getCode() == 1002) {
                        mView.tvSetError.text = "”旧支付密码错误，您还有" + JsonUtils.fromJson(
                            it.getDataCode().toString(),
                            MinePassWordTime::class.java
                        ).remain_times.toString() + "次机会“。"
                    } else mView.tvSetError.text = it.getMsg()
                }
            }
        }
    }


    //修改密码
    fun setPassWord(old: String) {
        mView.showPageLoadingDialog()
        MineApi.getSettingPayPassword(old, mView.edit_pay_solid.text.toString()) {
            onSuccess {
                mView.hidePageLoadingDialog()
                val dialog = DialogSuccess(mView, "支付密码设置成功", R.mipmap.ic_dialog_success)
                UserInfoSp.putIsSetPayPassWord(true)
                dialog.setOnDismissListener { mView.finish() }
                dialog.show()

            }
            onFailed {
                mView.hidePageLoadingDialog()
                mView.tvSetError.text = it.getMsg()
            }
        }
    }
}