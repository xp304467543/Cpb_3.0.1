package com.mine.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Typeface
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import com.lib.basiclib.utils.TimeUtils
import com.lib.basiclib.utils.ToastUtils
import com.lib.basiclib.utils.ViewUtils
import com.mine.R
import kotlinx.android.synthetic.main.pop_double_data.*
import java.text.SimpleDateFormat
import java.util.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/24
 * @ Describe
 *
 */
class DialogDataPickDouble (context: Context, them: Int) : Dialog(context, them) {

    private var tvData1: TextView
    private var tvData2: TextView
    private var tvQuery: TextView
    private var mCalendarView1: CalendarView
    private var mCalendarView2: CalendarView
    private var mListener: ((data1: String, data2: String) -> Unit)? = null
    var selectData = ""
    var selectData2 = ""
    fun setConfirmClickListener(listener: (data1: String, data2: String) -> Unit) {
        mListener = listener
    }

    init {
        setContentView(R.layout.pop_double_data)
        window?.setGravity(Gravity.TOP)
        val lp = window?.attributes
        lp?.width = ViewGroup.LayoutParams.MATCH_PARENT
        lp?.verticalMargin = 0.06F
        tvData1 = findViewById(R.id.tv_data_1)
        tvData2 = findViewById(R.id.tv_data_2)
        tvQuery = findViewById(R.id.tv_query)
        mCalendarView1 = findViewById(R.id.mCalendarView_1)
        mCalendarView2 = findViewById(R.id.mCalendarView_2)
        tvData1.text = TimeUtils.getToday()
        tvData2.text = TimeUtils.getToday()
        initDataView()
        initDataView2()
        initDataCalendar()
        initDataCalendar2()
        initEvent()
    }

    private fun initDataView() {
        // 指定显示的日期, 如当前月的下个月
        val calendar: Calendar = mCalendarView1.calendar
//        calendar.add(Calendar.MONTH, 1)
        mCalendarView1.calendar = calendar
        // 设置字体
        mCalendarView1.setTypeface(Typeface.SERIF)

        // 设置日期状态改变监听
        mCalendarView1.setOnDateChangeListener { view, select, year, month, day ->
            if (select) {
//                ToastUtils.show("选中了：" + year + "年" + (month + 1) + "月" + day + "日")
                selectData = year.toString() + "-" + (month + 1) + "-" + day

//            } else {
//                ToastUtils.show("取消选中了：" + year + "年" + (month + 1) + "月" + day + "日")
            }
        }
        // 设置是否能够改变日期状态
        mCalendarView1.isChangeDateStatus = true

        // 设置是否能够点击
        mCalendarView1.isClickable = true
        setCurDate1()
    }

    private fun initDataView2() {
        val calendar: Calendar = mCalendarView2.calendar
        mCalendarView2.calendar = calendar
        mCalendarView2.setTypeface(Typeface.SERIF)
        mCalendarView2.setOnDateChangeListener { view, select, year, month, day ->
            if (select) {
                selectData2 = year.toString() + "-" + (month + 1) + "-" + day
            }
        }
        mCalendarView2.isChangeDateStatus = true
        mCalendarView2.isClickable = true
        setCurDate2()
    }

    private fun initDataCalendar(): List<String>? {
        val dates: MutableList<String> = ArrayList()
        val calendar = Calendar.getInstance(Locale.CHINA)
        val sdf = SimpleDateFormat(mCalendarView1.dateFormatPattern, Locale.CHINA)
        sdf.format(calendar.time)
        dates.add(sdf.format(calendar.time))
        return dates
    }

    private fun initDataCalendar2(): List<String>? {
        val dates: MutableList<String> = ArrayList()
        val calendar = Calendar.getInstance(Locale.CHINA)
        val sdf = SimpleDateFormat(mCalendarView2.dateFormatPattern, Locale.CHINA)
        sdf.format(calendar.time)
        dates.add(sdf.format(calendar.time))
        return dates
    }


    var view1 = false
    var view2 = false
    private fun initEvent() {
        tvData1.setOnClickListener {
            if (!view1){
                view1 = true
                tvData1.setCompoundDrawablesWithIntrinsicBounds(null, null,ViewUtils. getDrawable(R.mipmap.select_bottom), null)
                tvData2.setCompoundDrawablesWithIntrinsicBounds(null, null,ViewUtils. getDrawable(R.mipmap.select_top), null)
                ViewUtils. setVisible(layout1)
                ViewUtils. setGone(layout2)
            }else {
                view1 = false
                tvData1.setCompoundDrawablesWithIntrinsicBounds(null, null, ViewUtils.getDrawable(R.mipmap.select_top), null)
                ViewUtils. setGone(layout1)
            }

        }
        tvData2.setOnClickListener {
            if (!view2){
                view2 = true
                tvData2.setCompoundDrawablesWithIntrinsicBounds(null, null, ViewUtils.getDrawable(R.mipmap.select_bottom), null)
                tvData1.setCompoundDrawablesWithIntrinsicBounds(null, null, ViewUtils.getDrawable(R.mipmap.select_top), null)
                ViewUtils. setGone(layout1)
                ViewUtils.setVisible(layout2)
            }else {
                view2 = false
                tvData2.setCompoundDrawablesWithIntrinsicBounds(null, null, ViewUtils.getDrawable(R.mipmap.select_top), null)
                ViewUtils. setGone(layout2)
            }
        }

        tvQuery.setOnClickListener {
            if (tvData1.text.isNullOrEmpty() && tvData2.text.isNullOrEmpty()) {
                ToastUtils.showToast("请选择日期")
                return@setOnClickListener
            }
            val t1 = tvData1.text.toString().trim()
            val t2 = tvData2.text.toString().trim()
            if (t1 == t2) {
                mListener?.invoke(t1, t2)
            } else {
                val boolean = TimeUtils.compareDate(tvData1.text.toString(), tvData2.text.toString())
                if (boolean) {
                    mListener?.invoke(t1, t2)
                } else ToastUtils.showToast("起始时间必须大于截止时间!")
            }
        }
        imgLeft_1.setOnClickListener {
                mCalendarView1.lastMonth()
            setCurDate1()
        }
        imgLeft_2.setOnClickListener {
            mCalendarView2.lastMonth()
            setCurDate2()
        }
        imgRight_1.setOnClickListener {
            mCalendarView1.nextMonth()
            setCurDate1()
        }
        imgRight_2.setOnClickListener {
                mCalendarView2.nextMonth()
            setCurDate2()
        }
        tvSure_1.setOnClickListener {
                if (selectData != "") tvData1.text = selectData else ToastUtils.showToast("请选择日期")

        }
        tvSure_2.setOnClickListener {
                if (selectData2 != "") tvData2.text = selectData2 else ToastUtils.showToast("请选择日期")
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setCurDate1() {
            tvTopData_1.text = (mCalendarView1.month + 1).toString() + "月      " + mCalendarView1.year.toString()
            tvTopData_1.typeface = Typeface.SERIF
    }

    @SuppressLint("SetTextI18n")
    private fun setCurDate2() {
        tvTopData_2.text = (mCalendarView2.month + 1).toString() + "月      " + mCalendarView2.year.toString()
        tvTopData_2.typeface = Typeface.SERIF
    }

}