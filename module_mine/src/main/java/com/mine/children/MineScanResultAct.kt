package com.mine.children

import com.customer.data.MineUserScanLoginOut
import com.customer.data.mine.MineApi
import com.hwangjr.rxbus.RxBus
import com.lib.basiclib.base.activity.BaseNavActivity
import com.lib.basiclib.utils.FastClickUtil
import com.lib.basiclib.utils.ToastUtils
import com.mine.R
import kotlinx.android.synthetic.main.act_scan_result.*

/**
 * @ Author  QinTian
 * @ Date  2020/11/10
 * @ Describe
 */
class MineScanResultAct : BaseNavActivity() {

    override fun getContentResID() = R.layout.act_scan_result

    override fun getPageTitle() = "登录确定"

    override fun isShowBackIconWhite() = false


    override fun initData() {
        val code = intent.getStringExtra("resultCode")
        tvLoginAccess.setOnClickListener {
            if (!FastClickUtil.isFastClick()) {
                MineApi.scanLogin(code ?: "") {
                    onSuccess {
                        finish()
                        RxBus.get().post(MineUserScanLoginOut("0"))
                    }
                    onFailed {
                        ToastUtils.showToast(it.getMsg())
                    }
                }
            }
        }
    }
}