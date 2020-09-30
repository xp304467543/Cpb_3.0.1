package com.customer.component.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import com.customer.data.UserInfoSp
import com.fh.module_base_resouce.R
import com.lib.basiclib.utils.ViewUtils
import kotlinx.android.synthetic.main.dialog_red_rain.*
import kotlinx.android.synthetic.main.dialog_task.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/9/29
 * @ Describe
 *
 */
class DialogRedRain  (context: Context, var money:String) : Dialog(context) {


    init {
        window?.setWindowAnimations(R.style.BaseDialogAnim)
        setContentView(R.layout.dialog_red_rain)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setGravity(Gravity.CENTER or Gravity.CENTER)
        setCanceledOnTouchOutside(true)
        initViews()
    }

    @SuppressLint("SetTextI18n")
    private fun initViews() {
        moneyUser.text = "$money 元"
        nameUser.text = UserInfoSp.getUserNickName()+"获得"+money+"红包"
        imgClose.setOnClickListener {
            dismiss()
        }
    }
}