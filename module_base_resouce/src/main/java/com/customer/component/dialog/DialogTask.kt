package com.customer.component.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import com.fh.module_base_resouce.R
import com.lib.basiclib.utils.ViewUtils
import kotlinx.android.synthetic.main.dialog_task.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/9/29
 * @ Describe
 *
 */
class DialogTask  (context: Context,var type:Int,var  count: String) : Dialog(context) {


    init {
        setContentView(R.layout.dialog_task)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setGravity(Gravity.CENTER or Gravity.CENTER)
        setCanceledOnTouchOutside(true)
        initViews()
    }

    private fun initViews() {
        when(type){
            //礼物
            1 ->{
                tvDiamond.text = count
                imgType?.setBackgroundResource(R.mipmap.ic_task_gift)
            }
            //红包
            2 ->{
                tvDiamond.text = count
                tvType.text = "元"
                imgType?.setBackgroundResource(R.mipmap.ic_task_red)
            }
            //彩金
            3 ->{
                tvDiamond.text = count
                tvType.text = "彩金"
                imgType?.setBackgroundResource(R.mipmap.ic_task_red)
            }

        }
    }
}