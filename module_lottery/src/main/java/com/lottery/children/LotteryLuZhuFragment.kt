package com.lottery.children

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.customer.base.BaseNormalFragment
import com.customer.component.dialog.BottomDialogBean
import com.customer.component.dialog.DialogBottomLottery
import com.customer.utils.JsonUtils
import com.customer.utils.countdowntimer.lotter.LotteryConstant
import com.fh.module_lottery.R
import com.lib.basiclib.utils.TimeUtils
import com.lib.basiclib.utils.ViewUtils
import com.lottery.adapter.LotteryChildLuZhuAdapter
import com.lottery.adapter.LotteryChildTypeAdapter
import com.customer.data.lottery.LotteryCodeLuZhuResponse
import kotlinx.android.synthetic.main.child_fragment_lu_zhu.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/16
 * @ Describe
 *
 */

class LotteryLuZhuFragment : BaseNormalFragment<LotteryLuZhuFragmentPresenter>() {

    var lotteryId = "-1"

    var bottomDialog: DialogBottomLottery? = null

    private var lotteryTypeAdapter: LotteryChildTypeAdapter? = null

    private var rankList: ArrayList<BottomDialogBean>? = null

    var luZhuRecycleAdapter: LotteryChildLuZhuAdapter? = null

    var selectType = LotteryConstant.TYPE_LUZHU_2

    var time = ""//昨天 今天 明天


    override fun attachView() = mPresenter.attachView(this)

    override fun attachPresenter() = LotteryLuZhuFragmentPresenter()

    override fun getLayoutRes() = R.layout.child_fragment_lu_zhu


    override fun initContentView() {
        smartRefreshLayoutLotteryLuZhuType.setEnableRefresh(false)//是否启用下拉刷新功能
        smartRefreshLayoutLotteryLuZhuType.setEnableLoadMore(false)//是否启用上拉加载功能
        smartRefreshLayoutLotteryLuZhuType.setEnableOverScrollBounce(true)//是否启用越界回弹
        smartRefreshLayoutLotteryLuZhuType.setEnableOverScrollDrag(true)//是否启用越界拖动（仿苹果效果）
        val value = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        lotteryTypeAdapter = LotteryChildTypeAdapter()
        rvLuZhuTypeSelect.layoutManager = value
        rvLuZhuTypeSelect.adapter = lotteryTypeAdapter
        luZhuRecycleAdapter =
            LotteryChildLuZhuAdapter(context!!, arguments?.getString("lotteryId")!!)
        rvLotteryLuZhu.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvLotteryLuZhu.adapter = luZhuRecycleAdapter
        rvLotteryLuZhu.setItemViewCacheSize(10)
    }

    override fun initData() {
        lotteryId = arguments?.getString("lotteryId") ?: "-1"
        setType(lotteryId)
        mPresenter.getLuZhuData(lotteryId, selectType)
    }


    override fun initEvent() {
        tvSelectAll.setOnClickListener {
            initBottomDialog()
        }
        tvToDay.setOnClickListener {
            tvToDay.setTextColor(getColor(R.color.color_FF513E))
            tvYesterday.setTextColor(getColor(R.color.color_333333))
            tvBeforeYesterday.setTextColor(getColor(R.color.color_333333))
            mPresenter.getLuZhuData(arguments?.getString("lotteryId")!!, selectType)
            time = ""
        }
        tvYesterday.setOnClickListener {

            tvToDay.setTextColor(getColor(R.color.color_333333))
            tvYesterday.setTextColor(getColor(R.color.color_FF513E))
            tvBeforeYesterday.setTextColor(getColor(R.color.color_333333))
            mPresenter.getLuZhuData(
                arguments?.getString("lotteryId")!!,
                selectType,
                TimeUtils.getYesterday()
            )
            time = TimeUtils.getYesterday()
        }
        tvBeforeYesterday.setOnClickListener {
            tvToDay.setTextColor(getColor(R.color.color_333333))
            tvYesterday.setTextColor(getColor(R.color.color_333333))
            tvBeforeYesterday.setTextColor(getColor(R.color.color_FF513E))
            mPresenter.getLuZhuData(
                arguments?.getString("lotteryId")!!,
                selectType,
                TimeUtils.getBeforeYesterday()
            )
            time = TimeUtils.getBeforeYesterday()
        }
    }

    private fun setType(lotteryID: String) {
        val data: Array<String>?
        if (lotteryID == "7" || lotteryID == "9" || lotteryID == "11" || lotteryID == "26" || lotteryID == "27") {
            data = arrayOf(
                LotteryConstant.TYPE_2,
                LotteryConstant.TYPE_3,
                LotteryConstant.TYPE_8,
                LotteryConstant.TYPE_9,
                LotteryConstant.TYPE_10
            )
            rankList = arrayListOf(
                BottomDialogBean("冠军"), BottomDialogBean("亚军"), BottomDialogBean("第三名"),
                BottomDialogBean("第四名"), BottomDialogBean("第五名"), BottomDialogBean("第六名"),
                BottomDialogBean("第七名"), BottomDialogBean("第八名"), BottomDialogBean("第九名"),
                BottomDialogBean("第十名")
            )
        } else if (lotteryID == "8") {
            data = arrayOf(
                LotteryConstant.TYPE_2,
                LotteryConstant.TYPE_3,
                LotteryConstant.TYPE_12,
                LotteryConstant.TYPE_5,
                LotteryConstant.TYPE_15,
                LotteryConstant.TYPE_16
            )
            rankList = arrayListOf(
                BottomDialogBean("正码一"), BottomDialogBean("正码二"), BottomDialogBean("正码三"),
                BottomDialogBean("正码四"), BottomDialogBean("正码五"), BottomDialogBean("正码六"),
                BottomDialogBean("特码")
            )
            tvSelectAll.text = "筛选号码"
            setGone(tvToDay)
            setGone(tvYesterday)
            setGone(tvBeforeYesterday)
        } else {
            data = arrayOf(
                LotteryConstant.TYPE_2,
                LotteryConstant.TYPE_3,
                LotteryConstant.TYPE_8,
                LotteryConstant.TYPE_11,
                LotteryConstant.TYPE_5
            )
            rankList = arrayListOf(
                BottomDialogBean("第一球"), BottomDialogBean("第二球"), BottomDialogBean("第三球"),
                BottomDialogBean("第四球"), BottomDialogBean("第五球")
            )
            tvSelectAll.text = "筛选号码"
        }
        lotteryTypeAdapter?.clear()
        lotteryTypeAdapter?.refresh(data)
        lotteryTypeAdapter?.setOnItemClickListener { _,item,position ->
            if (!ViewUtils.isFastClick()) {
                lotteryTypeAdapter?.changeBackground(position)
                luZhuRecycleAdapter!!.clearList()
                selectType = mPresenter.getType(item)
                if (time == "") mPresenter.getLuZhuData(arguments?.getString("lotteryId")!!, mPresenter.getType(item))
                else mPresenter.getLuZhuData(arguments?.getString("lotteryId")!!, mPresenter.getType(item), time)
                when (selectType) {
                    LotteryConstant.TYPE_LUZHU_5, LotteryConstant.TYPE_LUZHU_8, LotteryConstant.TYPE_LUZHU_10 -> setGone(tvSelectAll)
                    else -> setVisible(tvSelectAll)
                }
            }
        }
    }

    //设置露珠数据
    fun getLuZhuView(it: String, type: String) {
        setGone(tvLuZhuPlaceHolder)
        val bean = JsonUtils.fromJson(it, LotteryCodeLuZhuResponse::class.java)
        if (luZhuRecycleAdapter != null) {
            luZhuRecycleAdapter!!.total = bean.total
            luZhuRecycleAdapter!!.clear()
            luZhuRecycleAdapter!!.type = type
            luZhuRecycleAdapter!!.refresh(bean.data)
            val lotteryID = arguments?.getString("lotteryId")!!
            if (rankList == null) rankList = arrayListOf()
            if (bean.data.isNullOrEmpty()) return
            rankList?.clear()
            if (lotteryID == "7" || lotteryID == "9" || lotteryID == "11" || lotteryID == "26" || lotteryID == "27") {
                for ((index, l1) in bean.data!!.withIndex()) {
                    val be = BottomDialogBean()
                    when (index) {
                        0 -> {
                            be.str = "冠军"
                        }
                        1 -> {
                            be.str = "亚军"
                        }
                        else -> {
                            be.str = "第" + (index + 1) + "名"
                        }
                    }
                    rankList?.add(be)
                }
            } else if (lotteryID == "8") {
                for ((index, l1) in bean.data!!.withIndex()) {
                    val be = BottomDialogBean()
                    when (index) {
                        bean.data!!.size -> {
                            be.str = "特码"
                        }
                        else -> {
                            be.str = "正码" + (index + 1)
                        }
                    }
                    rankList?.add(be)
                }
            } else {
                if (selectType != LotteryConstant.TYPE_LUZHU_11){
                    for ((index, l1) in bean.data!!.withIndex()) {
                        val be = BottomDialogBean()
                        be.str = "第" + (index + 1) + "球"
                        rankList?.add(be)
                    }
                }else{
                    for ((index, l1) in bean.data!!.withIndex()) {
                        val be = BottomDialogBean()
                        be.str = "号码 $index"
                        rankList?.add(be)
                    }
                }

            }
            resetDialog()
        }
    }

    /**
     * reset
     */
    private fun resetDialog() {
        if (rankList.isNullOrEmpty()) return
        bottomDialog = DialogBottomLottery(requireContext(),rankList)
        bottomDialog!!.bottomAdapter!!.setOnItemClickListener { _,data, position ->
            data.isSelect = !data.isSelect
            bottomDialog!!.bottomAdapter!!.notifyItemChanged(position)
        }
        bottomDialog!!.setOnSureClickListener {
            val selectList = ArrayList<Boolean>()
            for (s in it) {
                selectList.add(s.isSelect)
            }
            luZhuRecycleAdapter!!.notifyHideItem(selectList)
            bottomDialog!!.dismiss()
        }
    }
    //底部选择弹框
    private fun initBottomDialog() {
        if (bottomDialog == null) {
            bottomDialog = DialogBottomLottery(requireContext(), rankList)
            bottomDialog!!.bottomAdapter!!.setOnItemClickListener { _, item, position ->
                item.isSelect = !item.isSelect
                bottomDialog!!.bottomAdapter!!.notifyItemChanged(position)
            }
            bottomDialog!!.setOnSureClickListener {
                val selectList = ArrayList<Boolean>()
                for (s in it) {
                    selectList.add(s.isSelect)
                }
                luZhuRecycleAdapter!!.notifyHideItem(selectList)
                bottomDialog!!.dismiss()
            }
            bottomDialog!!.show()
        } else bottomDialog!!.show()
    }

    companion object {
        fun newInstance(lotteryId: String, issue: String): LotteryLuZhuFragment {
            val fragment = LotteryLuZhuFragment()
            val bundle = Bundle()
            bundle.putString("lotteryId", lotteryId)
            bundle.putString("issue", issue)
            fragment.arguments = bundle
            return fragment
        }
    }

}