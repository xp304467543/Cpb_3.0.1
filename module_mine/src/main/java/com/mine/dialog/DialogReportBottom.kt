package com.mine.dialog

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.lib.basiclib.utils.FastClickUtil
import com.lib.basiclib.utils.ToastUtils
import com.lib.basiclib.utils.ViewUtils
import com.mine.R
import com.mine.children.report.team.MineReportPosterAct
import kotlinx.android.synthetic.main.dialog_bottom_report.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/24
 * @ Describe
 *
 */
class DialogReportBottom(
    context: Context,
    private var inviteNum: String,
    private var inviteUrl: String,
    private var markUrl:String
) : BottomSheetDialog(context) {

    init {
        setContentView(R.layout.dialog_bottom_report)
        val root = delegate?.findViewById<View>(R.id.design_bottom_sheet)
        val behavior = root?.let { BottomSheetBehavior.from(it) }
        behavior?.isHideable = false
        delegate?.findViewById<View>(R.id.design_bottom_sheet)
            ?.setBackgroundColor(Color.TRANSPARENT)
        initViews()
        iniEvent()
    }

    private fun iniEvent() {
        tv_report_1.setOnClickListener {
            ViewUtils.copyText(inviteUrl)
            ToastUtils.showToast("链接 已复制到剪贴板")
        }
        tv_report_2.setOnClickListener {
            ViewUtils.copyText(inviteNum)
            ToastUtils.showToast("邀请码 已复制到剪贴板")
        }
        tv_report_3.setOnClickListener {
            if (!FastClickUtil.isFastClick()) {
                val intent = Intent(context, MineReportPosterAct::class.java)
                intent.putExtra("inviteUrl", inviteUrl)
                intent.putExtra("inviteNum", inviteNum)
                intent.putExtra("markUrl", markUrl)
                context.startActivity(intent)
                dismiss()
            }
        }
    }

    private fun initViews() {
        tvInviteNum.text = inviteNum
    }
}