package com.mine.children

import com.customer.ApiRouter
import com.customer.component.dialog.DialogGlobalTips
import com.customer.data.LoginOut
import com.hwangjr.rxbus.RxBus
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.thread.EventThread
import com.lib.basiclib.base.activity.BaseNavActivity
import com.mine.R
import com.customer.data.mine.MineApi
import com.xiaojinzi.component.anno.RouterAnno
import com.xiaojinzi.component.impl.Router
import com.customer.component.dialog.GlobalDialog
import com.customer.data.AppMode
import com.customer.data.UserInfoSp
import com.lib.basiclib.utils.ToastUtils
import com.lib.basiclib.utils.ViewUtils
import kotlinx.android.synthetic.main.act_setting.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/23
 * @ Describe
 *
 */
@RouterAnno(host = "Mine", path = "setting")
class MineSettingAct : BaseNavActivity() {

    var mode = 0

    var code = 0 //等于10就是钱包冻结

    override fun getContentResID() = R.layout.act_setting

    override fun isSwipeBackEnable() = true

    override fun getPageTitle() = getString(R.string.ic_mine_setting)

    override fun isRegisterRxBus() = true

    override fun isShowBackIconWhite() = false


    override fun initContentView() {
        videoSwitch.isChecked = UserInfoSp.getOpenWindow()
    }

    override fun initData() {
        if (!UserInfoSp.getIsSetPayPassWord()) getIsSetPayPassWord() else tvPayPassWordSet.text = "支付密码修改"
    }

    override fun initEvent() {
        btExitLogin.setOnClickListener {
            val dialog = DialogGlobalTips(this, "是否要退出登录!", "确定", "取消", "")
            dialog.setConfirmClickListener {
                GlobalDialog.spClear()
                RxBus.get().post(LoginOut(true))
                dialog.dismiss()

            }
            dialog.show()
        }

        //支付密码
        linSetPayPassWord.setOnClickListener {
                if (code != 10) {
                    if (tvPayPassWordSet.text.toString().contains("设置")){
                        Router.withApi(ApiRouter::class.java).toSetPassWord(1)
                    }else  Router.withApi(ApiRouter::class.java).toSetPassWord(0)
                } else ToastUtils.showToast("钱包已被冻结")
        }

        //密码修改
        setPassWord.setOnClickListener {
            Router.withApi(ApiRouter::class.java).toModifyPassWord()
        }

        videoSwitch.setOnCheckedChangeListener { _, isChecked ->
            UserInfoSp.putOpenWindow(isChecked)
        }

        appModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            RxBus.get().post(AppMode(isChecked))
//            if (isChecked){
//                UserInfoSp.putAppMode(UserInfoSp.AppMode.Pure)
//            }else UserInfoSp.putAppMode(UserInfoSp.AppMode.Normal)

        }
    }

    override fun onResume() {
        super.onResume()
        if (UserInfoSp.getIsSetPayPassWord()){
            tvPayPassWordSet.text = "支付密码修改"
            setGone(tvPayPassWordNotSet)
        }
    }

    //查询是否设置支付密码
    private fun getIsSetPayPassWord() {
        showPageLoadingDialog()
        MineApi.getIsSetPayPass {
            onSuccess {
                hidePageLoadingDialog()
                UserInfoSp.putIsSetPayPassWord(true)
                tvPayPassWordSet.text = "支付密码修改"
                setGone(tvPayPassWordNotSet)
            }
            onFailed {
                hidePageLoadingDialog()
                mode = 1
                if (tvPayPassWordNotSet != null) {
                    tvPayPassWordNotSet.text = it.getMsg()
                    tvPayPassWordNotSet.setTextColor(ViewUtils.getColor(R.color.text_red))
                    code = it.getCode()
                }
            }
        }
    }


    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun loginOut(eventBean: LoginOut) {
        finish()
    }
}