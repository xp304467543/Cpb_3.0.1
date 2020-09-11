package com.lottery.children

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.customer.base.BaseNormalFragment
import com.customer.data.lottery.LotteryCodeTrendResponse
import com.customer.utils.countdowntimer.lotter.LotteryComposeUtil
import com.customer.utils.countdowntimer.lotter.LotteryConstant
import com.fh.module_lottery.R
import com.lib.basiclib.base.xui.widget.picker.widget.OptionsPickerView
import com.lib.basiclib.base.xui.widget.picker.widget.builder.OptionsPickerBuilder
import com.lib.basiclib.utils.TimeUtils
import com.lib.basiclib.utils.ViewUtils
import com.lottery.adapter.LotteryChildTypeAdapter
import kotlinx.android.synthetic.main.child_fragment_trend.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/16
 * @ Describe
 *
 */

class LotteryTrendFragment : BaseNormalFragment<LotteryTrendPresenter>() {

    private var typeSelect: String? = null

    var lotteryId = "-1"

    var numTenYg = "11" //10码 亚冠和走势
    var numTenBase = "1"//10码 号码走势

    var numFiveBase = "1"//5码基本走势
    var numFiveForm = "7"//5码形态走势
    var numFiveTiger = "6"//5码龙虎走势

    var limit = "10"

    var date = TimeUtils.getToday()

    var lotteryTypeAdapter : LotteryChildTypeAdapter?=null

    private var bottomWheelViewDialog: OptionsPickerView<String>? = null
    private var bottomIssueWheelViewDialog: OptionsPickerView<String>? = null
    private var bottomFormWheelViewDialog: OptionsPickerView<String>? = null

    override fun getLayoutRes() = R.layout.child_fragment_trend

    override fun attachView() = mPresenter.attachView(this)

    override fun attachPresenter() = LotteryTrendPresenter()

    override fun initContentView() {
        smartRefreshLayoutLotteryTrend.setEnableRefresh(false)//是否启用下拉刷新功能
        smartRefreshLayoutLotteryTrend.setEnableLoadMore(false)//是否启用上拉加载功能
        smartRefreshLayoutLotteryTrend.setEnableOverScrollBounce(true)//是否启用越界回弹
        smartRefreshLayoutLotteryTrend.setEnableOverScrollDrag(true)//是否启用越界拖动（仿苹果效果）
        lotteryId = arguments?.getString("lotteryId") ?: "-1"
        val value = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        lotteryTypeAdapter= LotteryChildTypeAdapter()
        rvTrendSelect.layoutManager = value
        rvTrendSelect.adapter = lotteryTypeAdapter
    }

    override fun initData()  {
        setType(lotteryId)
        initOption()
        initType(lotteryId)
        mPresenter.getTrendData(lotteryId)
    }

    override fun initEvent() {
        tvToDay.setOnClickListener {
            tvToDay.setTextColor(getColor(R.color.color_FF513E))
            tvYesterday.setTextColor(getColor(R.color.color_333333))
            tvBeforeYesterday.setTextColor(getColor(R.color.color_333333))
            this.date = TimeUtils.getToday()
            getTypeTrendData()
        }
        tvYesterday.setOnClickListener {
            tvToDay.setTextColor(getColor(R.color.color_333333))
            tvYesterday.setTextColor(getColor(R.color.color_FF513E))
            tvBeforeYesterday.setTextColor(getColor(R.color.color_333333))
            date = TimeUtils.getYesterday()
            getTypeTrendData()
        }
        tvBeforeYesterday.setOnClickListener {
            tvToDay.setTextColor(getColor(R.color.color_333333))
            tvYesterday.setTextColor(getColor(R.color.color_333333))
            tvBeforeYesterday.setTextColor(getColor(R.color.color_FF513E))
            date = TimeUtils.getBeforeYesterday()
            getTypeTrendData()
        }

        tvTrendSelectAll.setOnClickListener {
            if (bottomWheelViewDialog != null) {
                bottomWheelViewDialog?.show()
            }
        }
        tvTrendSelectIssue.setOnClickListener {
            if (bottomIssueWheelViewDialog != null) {
                bottomIssueWheelViewDialog?.show()
            }
        }
        tvTrendSelectForm.setOnClickListener {
            if (bottomFormWheelViewDialog != null) {
                bottomFormWheelViewDialog?.show()
            }
        }
    }

    private fun initType(lotteryId: String) {
        when (lotteryId) {
            "7", "9", "11", "26", "27" -> { //10码
                this.limit = "100"
                this.typeSelect = LotteryConstant.TYPE_17
                tvTrendSelectAll.text = "冠军"
            }
            "1", "10" -> { //5码
                this.limit = "10"
                this.typeSelect = LotteryConstant.TYPE_19
            }
        }
    }

    private fun initOption() {
        when (lotteryId) {
            "7", "9", "11", "26", "27" -> {  //10码
                val list = arrayListOf("冠军", "第二名", "第三名", "第四名", "第五名", "第六名", "第七名", "第八名", "第九名", "第十名")
                bottomWheelViewDialog =OptionsPickerBuilder(context) { v, options1, options2, options3 ->
                    this.numTenBase = (options1+1).toString()
                    mPresenter.getTrendData(lotteryId, this.numTenBase, this.limit, this.date)
                    tvTrendSelectAll.text = list[options1]
                    bottomWheelViewDialog?.dismiss()
                    false
                }
                    .setSelectOptions(0)
                    .build()
                bottomWheelViewDialog?.setPicker(list)
            }
            "1", "10" -> { //5码
                val listForm = arrayListOf("前三形态", "中三形态", "后三形态")
                bottomFormWheelViewDialog = OptionsPickerBuilder(context){v, options1, options2, options3 ->
                    this.typeSelect = listForm[options1]
                    when (options1) {
                        0 -> {
                            this.numFiveForm = "7"
                        }
                        1 -> {
                            this.numFiveForm = "8"
                        }
                        2 -> {
                            this.numFiveForm = "9"
                        }
                    }
                    mPresenter.getFormData(lotteryId, this.numFiveForm, this.limit, this.date)
                    tvTrendSelectForm.text = listForm[options1]
                    bottomFormWheelViewDialog?.dismiss()
                    false
                }
                    .setSelectOptions(0)
                    .build()
                bottomFormWheelViewDialog?.setPicker(listForm)


                val list = arrayListOf("第1球", "第2球", "第3球", "第4球", "第5球")
                bottomWheelViewDialog = OptionsPickerBuilder(context) { v, options1, options2, options3 ->
                    this.numFiveBase = (options1 + 1).toString()
                    mPresenter.getTrendData(lotteryId, this.numFiveBase, this.limit, this.date)
                    tvTrendSelectAll.text = list[options1]
                    bottomWheelViewDialog?.dismiss()
                    false
                }
                    .setSelectOptions(0)
                    .build()
                bottomWheelViewDialog?.setPicker(list)


                val list2 = arrayListOf("近10期", "近30期", "近60期", "近90期")
                bottomIssueWheelViewDialog =  OptionsPickerBuilder(context) { v, options1, options2, options3 ->
                    when (options1) {
                        0 -> this.limit = "10"
                        1 -> this.limit = "30"
                        2 -> this.limit = "60"
                        3 -> this.limit = "90"
                    }
                    getTypeTrendData()
                    tvTrendSelectIssue.text = list2[options1]
                    bottomIssueWheelViewDialog?.dismiss()
                    false
                }
                    .setSelectOptions(0)
                    .build()
                bottomIssueWheelViewDialog?.setPicker(list2)
            }
        }
    }


    // 获取各种走势数据数据
    private fun getTypeTrendData() {
        when (typeSelect) {
            LotteryConstant.TYPE_20, "前三形态", "中三形态", "后三形态" ->
                mPresenter.getFormData(lotteryId, this.numFiveForm, this.limit, this.date)
            LotteryConstant.TYPE_17 ->
                mPresenter.getTrendData(lotteryId, this.numTenBase, this.limit, this.date)
            LotteryConstant.TYPE_18 ->
                mPresenter.getTrendData(lotteryId, this.numTenYg, this.limit, this.date)
            LotteryConstant.TYPE_19 ->
                mPresenter.getTrendData(lotteryId, this.numFiveBase, this.limit, this.date)
            LotteryConstant.TYPE_21 ->
                mPresenter.getTrendData(lotteryId, this.numFiveTiger, this.limit, this.date)
        }

    }

    //走势图 号码走势，亚冠和走势筛选
    private fun setType(lotteryID:String){
        var data: Array<String>? = null
        if (lotteryID == "7" || lotteryID == "9" || lotteryID == "11" || lotteryID == "26" || lotteryID == "27") {
            data = arrayOf(LotteryConstant.TYPE_17, LotteryConstant.TYPE_18)
            setGone(tvTrendSelectIssue)
            tvTrendSelectAll.text = "冠军"
        } else if (lotteryID != "8") {
            data = arrayOf(LotteryConstant.TYPE_19, LotteryConstant.TYPE_20, LotteryConstant.TYPE_21)
            setVisible(tvTrendSelectIssue)
            tvTrendSelectAll.text = "第一球"
        }
        if (data != null && data.isNotEmpty()){
            lotteryTypeAdapter?.clear()
            lotteryTypeAdapter?.refresh(data)
            lotteryTypeAdapter?.setOnItemClickListener { itemView, item, position ->
                if (!ViewUtils.isFastClick()) {
                    lotteryTypeAdapter?.changeBackground(position)
                    setVisible(linTrendLoading)
                    this.typeSelect = item
                    when (item) {
                        LotteryConstant.TYPE_17 -> {
                            setVisible(tvTrendSelectAll)
                            mPresenter.getTrendData(lotteryID, this.numTenBase, this.limit, this.date)
                        }
                        LotteryConstant.TYPE_18 -> {
                            setGone(tvTrendSelectAll)
                            mPresenter.getTrendData(lotteryID, this.numTenYg, this.limit, this.date)
                        }
                        LotteryConstant.TYPE_19 -> {
                            setVisible(tvTrendSelectAll)
                            setGone(tvTrendSelectForm)
                            mPresenter.getTrendData(lotteryID, this.numFiveBase, this.limit, this.date)
                        }
                        LotteryConstant.TYPE_20 -> {
                            setVisible(tvTrendSelectForm)
                            setGone(tvTrendSelectAll)
                            mPresenter.getFormData(lotteryID, this.numFiveForm, this.limit, this.date)
                        }
                        LotteryConstant.TYPE_21 -> {
                            setGone(tvTrendSelectAll)
                            setGone(tvTrendSelectForm)
                            mPresenter.getTrendData(lotteryID, this.numFiveTiger, this.limit, this.date)
                        }
                    }
                    trendScrollView.scrollTo(0, 0)
                }
            }
        }

    }

    // 走势图 只需要获取一次数据
    fun initLineChart(data: List<LotteryCodeTrendResponse>?, dataFrom: List<LotteryCodeTrendResponse>?) {
        if (typeSelect != null) {
            LotteryComposeUtil.getNumTrendMap(this.typeSelect!!, data, dataFrom, trendViewHead, trendViewContent, trendContainer, linTrendLoading)
        }
    }

    companion object {
        fun newInstance(lotteryId: String, issue: String): LotteryTrendFragment {
            val fragment = LotteryTrendFragment()
            val bundle = Bundle()
            bundle.putString("lotteryId", lotteryId)
            bundle.putString("issue", issue)
            fragment.arguments = bundle
            return fragment
        }
    }
}
