package com.customer.component.dialog

import android.app.Activity
import android.content.Context
import com.customer.ApiRouter
import com.customer.data.UserInfoSp
import com.lib.basiclib.utils.SpUtils
import com.lib.basiclib.utils.ToastUtils
import com.rxnetgo.exception.ApiException
import com.xiaojinzi.component.impl.Router

/**
 *
 * @ Author  QinTian
 * @ Date  2020/6/5
 * @ Describe
 *
 */
object GlobalDialog {
    private var loginTipsDialog: DialogLoginTips? = null

    //未登录
    fun notLogged(context: Activity, horizontal: Boolean = false) {
      loginTipsDialog = DialogLoginTips(context, horizontal)
        if (!loginTipsDialog?.isShowing!!) { if (context.isFinishing) { return }
            loginTipsDialog?.setOnDismissListener {
                spClear()
            }
           loginTipsDialog?.show()
        }
    }

    //未设置支付密码
    fun noSetPassWord(context: Context) {
        val dialog = DialogGlobalTips(context, "您暂未设置支付密码", "去设置", "我知道了", "")
        dialog.setConfirmClickListener {
            Router.withApi(ApiRouter::class.java).toSetPassWord(1)
            dialog.dismiss()
        }
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }
    //余额不足
    private fun noMoney(activity: Activity, isHor:Boolean){
        val dialog = DialogReCharge(activity,isHor)
        dialog.setOnSendClickListener {
            dialog.dismiss()
           Router.withApi(ApiRouter::class.java).toMineRecharge(1)
        }
        dialog.show()
    }

    //登录提醒
    fun otherLogin(context: Context) {
        val dialog = DialogGlobalTips(context, "登录提醒", "确定", "", "您的账号已在其他设备登录\n"+"如非本人请联系客服")
        dialog.setConfirmClickListener {
            dialog.dismiss()
        }
        dialog.setOnDismissListener {
            spClear()
            Router.withApi(ApiRouter::class.java).toLogin()
        }
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }

    //清除所有Sp保存的值，除去已经显示过的guide
    fun spClear() {
        val one = UserInfoSp.getMainGuide()
//        val second = UserInfoSp.getOpenCodeGuide()
        val third = UserInfoSp.getMineGuide()
//        val four = UserInfoSp.getRewardnGuide()
//        val five = UserInfoSp.getAttentionGuide()
//        val six = UserInfoSp.getLiveeGuide()
//        val skin = UserInfoSp.getSkinSelect()
        SpUtils.clearAll()
        if (one) UserInfoSp.putMainGuide(true)
//        if (second) UserInfoSp.putOpenCodeGuide(true)
        if (third) UserInfoSp.putMineGuide(true)
//        if (four) UserInfoSp.putRewardnGuide(true)
//        if (five) UserInfoSp.putAttentionGuide(true)
//        if (six) UserInfoSp.putOpenWindow(true)
//        UserInfoSp.putSkinSelect(skin)
    }

    //对所有未登录处理
    fun showError(context: Activity, error: ApiException, horizontal: Boolean = false) {
        if (error.getCode() == 2001 || error.getCode() == 401 || error.getCode() == 2000 || error.getMsg().toString().contains("请登录")) {
            notLogged(context, horizontal)
        } else if (error.getCode() == 9) {
            noSetPassWord(context)
        }else ToastUtils.showToast(error.getMsg().toString())

//        else if (error.getCode() == 2)  {
//            noMoney(context, horizontal)
//        }
    }
}