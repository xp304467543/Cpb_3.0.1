package com.mine.children.report.team

import com.lib.basiclib.base.mvp.BaseMvpFragment
import com.lib.basiclib.base.xui.widget.picker.widget.builder.TimePickerBuilder
import com.lib.basiclib.base.xui.widget.picker.widget.configure.TimePickerType
import com.lib.basiclib.base.xui.widget.picker.widget.listener.OnTimeSelectListener
import com.lib.basiclib.utils.TimeUtils
import com.lib.basiclib.utils.ToastUtils
import com.mine.R
import com.mine.dialog.DialogDataPick
import kotlinx.android.synthetic.main.act_bank_recharge.*
import kotlinx.android.synthetic.main.fragment_report_1.*
import java.text.SimpleDateFormat
import java.util.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/6/27
 * @ Describe
 *
 */
class ReportFragment1 : BaseMvpFragment<ReportFragment1P>() {

    private var dataPickDialog: DialogDataPick? = null


    override fun attachView() = mPresenter.attachView(this)

    override fun attachPresenter() = ReportFragment1P()

    override fun getLayoutResID() = R.layout.fragment_report_1


    override fun initContentView() {
        val today = TimeUtils.getToday()
        tv_data1.text = today
        tv_data2.text = today

    }

    override fun initData() {

        mPresenter.getReport()
    }

    override fun initEvent() {
        tv_data1.setOnClickListener {
            timePicker(0)
        }
        tv_data2.setOnClickListener {
            timePicker(1)
        }



        tvCheck.setOnClickListener {
            if (tv_data1.text.isNullOrEmpty() && tv_data2.text.isNullOrEmpty()) {
                ToastUtils.showToast("请选择日期")
                return@setOnClickListener
            }
            val t1 = tv_data1.text.toString().trim()
            val t2 = tv_data2.text.toString().trim()
            if (t1 == t2) {
                mPresenter.getReport(t1, t2)
            } else {
                val boolean =
                    TimeUtils.compareDate(tv_data1.text.toString(), tv_data2.text.toString())
                if (boolean) {
                    mPresenter.getReport(t1, t2)
                } else ToastUtils.showToast("起始时间必须大于截止时间!")
            }

        }
    }


    private fun timePicker(int: Int) {
        val calendar: Calendar = Calendar.getInstance()
        calendar.time = Date()
        val mTimePicker = TimePickerBuilder(context,
            OnTimeSelectListener { date, v ->
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val str: String = sdf.format(date)
                if (int == 0) tv_data1.text = str else tv_data2.text = str
            })
            .setType(TimePickerType.DEFAULT)
            .setTitleText("时间选择")
            .isDialog(false)
            .setOutSideCancelable(true)
            .setDate(calendar)
            .build()
        mTimePicker.show()
    }
}