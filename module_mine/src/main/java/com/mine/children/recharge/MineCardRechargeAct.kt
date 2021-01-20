package com.mine.children.recharge

import android.content.Intent
import com.customer.component.dialog.DialogRechargeSuccess
import com.hwangjr.rxbus.RxBus
import com.lib.basiclib.base.activity.BaseNavActivity
import com.lib.basiclib.utils.FastClickUtil
import com.lib.basiclib.utils.ToastUtils
import com.mine.R
import com.customer.data.mine.MineApi
import com.customer.data.mine.MineUpDateMoney
import kotlinx.android.synthetic.main.act_card_recharge.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/25
 * @ Describe 卡密充值
 *
 */
class MineCardRechargeAct : BaseNavActivity() {


    override fun getContentResID() = R.layout.act_card_recharge

    override fun isSwipeBackEnable() = true

    override fun getPageTitle() = "卡密充值"

    override fun isShowBackIconWhite() = false

    override fun initEvent() {
        bt_go_recharge.setOnClickListener {
                    if (!FastClickUtil.isFastClick()) {
                if (et_code.text.length < 18) {
                    ToastUtils.showToast("请输入正确18位数账号")
                    return@setOnClickListener
                }
                showPageLoadingDialog()
                MineApi.cardRecharge(et_code.text.toString(), et_pass.text.toString()) {
                    onSuccess {
                        et_code.setText("")
                        et_pass.setText("")
                        DialogRechargeSuccess(this@MineCardRechargeAct, "充值成功", R.mipmap.ic_dialog_success).show()
                        RxBus.get().post(MineUpDateMoney("", false, isDiamond = true))
                        hidePageLoadingDialog()
                    }
                    onFailed {
                        DialogRechargeSuccess(this@MineCardRechargeAct, it.getMsg()
                            ?: "充值失败", R.mipmap.recharge_failed).show()
                        hidePageLoadingDialog()
                    }

                }
            }
        }
        goBuyCard.setOnClickListener {
            if (!FastClickUtil.isFastClick()){
                startActivity(Intent(this,MineRechargeCardContactAct::class.java))
            }
        }
    }

}