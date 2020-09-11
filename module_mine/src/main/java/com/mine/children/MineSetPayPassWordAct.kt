package com.mine.children

import android.text.Editable
import android.text.TextWatcher
import com.lib.basiclib.base.mvp.BaseMvpActivity
import com.lib.basiclib.utils.ToastUtils
import com.mine.R
import com.xiaojinzi.component.anno.RouterAnno
import kotlinx.android.synthetic.main.act_pay_pass_word.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/23
 * @ Describe
 *
 */
@RouterAnno(host = "Mine", path = "setPassWord")
class MineSetPayPassWordAct : BaseMvpActivity<MineSetPayPassWordActPresenter>() {

//    // 0x1 支付密码设置
    var firstPassWord = ""
    var newPassWord = ""
    var loadMode: Int = 1

    override fun attachView() = mPresenter.attachView(this)

    override fun attachPresenter() = MineSetPayPassWordActPresenter()

    override fun getContentResID() = R.layout.act_pay_pass_word

    override fun isShowBackIconWhite() = false

    override fun initContentView() {

        loadMode = intent.getIntExtra("loadMode", 1)

        if (loadMode == 1) {
            setPageTitle("支付密码设置")
            tvTitlePass.text = "请设置支付密码"
        } else {
            setPageTitle("支付密码修改")
            tvTitlePass.text = "请输入旧密码"
        }
    }

    override fun initEvent() {
        edit_pay_solid.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (p0?.length == 6) {
                    if (loadMode != 1) {
                        when (tvTitlePass.text) {
                            "请输入旧密码" -> {
                                firstPassWord = edit_pay_solid.text.toString()
                                mPresenter.verifyPass()
                            }
                            "请输入新密码" -> {
                                if (edit_pay_solid.text.toString() != firstPassWord) {
                                    newPassWord = edit_pay_solid.text.toString()
                                    edit_pay_solid.clearText()
                                    tvTitlePass.text = "请确认新密码"
                                } else {
                                    edit_pay_solid.clearText()
                                    ToastUtils.showToast("新密码不能与旧密码相同")
                                }
                            }
                            "请确认新密码" -> {
                                if (newPassWord == edit_pay_solid.text.toString()) {
                                    mPresenter.setPassWord(firstPassWord)
                                } else {
                                    edit_pay_solid.clearText()
                                    ToastUtils.showToast("两次密码输入不一致")
                                }

                            }
                        }
                    } else {
                        if (firstPassWord == "") {
                            firstPassWord = p0.toString()
                            edit_pay_solid.clearText()
                            tvTitlePass.text = "请确认支付密码"
                        } else {
                            if (firstPassWord == p0.toString()) {
                                mPresenter.setPassWord("")
                            } else tvSetError.text = "两次密码输入不一致"
                        }
                    }

                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                tvSetError.text = ""
            }
        })
    }



}