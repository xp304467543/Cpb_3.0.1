package com.mine.children.report.team

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.customer.component.dialog.DialogGlobalTips
import com.lib.basiclib.base.mvp.BaseMvpFragment
import com.lib.basiclib.base.recycle.BaseRecyclerAdapter
import com.lib.basiclib.base.recycle.RecyclerViewHolder
import com.mine.R
import com.customer.data.mine.MineApi
import com.customer.data.mine.MineLevelList
import com.mine.dialog.DialogReport
import com.mine.dialog.DialogReportBottom
import kotlinx.android.synthetic.main.fragment_report_4.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/6/27
 * @ Describe
 *
 */
class ReportFragment4 : BaseMvpFragment<ReportFragment4P>() {

    var state = ""

    private var reportBottomDialog: DialogReportBottom? = null

    override fun attachView() = mPresenter.attachView(this)

    override fun attachPresenter() = ReportFragment4P()

    override fun getLayoutResID() = R.layout.fragment_report_4

    var adapter: LevelAdapter? = null

    override fun onSupportVisible() {
        mPresenter.getCode()
    }

    override fun initContentView() {
        adapter = LevelAdapter()
        levelList.adapter = adapter
        levelList.layoutManager = object : LinearLayoutManager(getPageActivity(), VERTICAL, false) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
    }

    override fun initEvent() {
        tvGetReportCode.setOnClickListener {
            when (state) {
                "10" -> {
                    reportBottomDialog?.show()
                }
                "9" -> {
                    DialogGlobalTips(
                        requireContext(),
                        "推广申请", "确定", "",
                        "您提交的推广申请，小姐姐们正在\n\n努力审核，请稍等"
                    ).show()
                }
                else -> {
                    val dialog = DialogReport(requireContext())
                    dialog.setConfirmClickListener {
                        MineApi.supportCode(it) {
                            onSuccess {
                                if (isActive()) {
                                    DialogGlobalTips(
                                        requireContext(),
                                        "推广申请", "确定", "",
                                        "您提交的推广申请，小姐姐们正在\n\n努力审核，请稍等"
                                    ).show()
                                    state = "9"
                                }
                            }
                            onFailed { ext ->
                                DialogGlobalTips(
                                    requireContext(),
                                    "推广申请", "确定", "",
                                    ext.getMsg() ?: "申请失败"
                                ).show()
                            }
                        }
                    }
                    dialog.show()
                }
            }
        }
    }


    fun initDialog(num: String, url: String,homeUrl:String) {
        reportBottomDialog = DialogReportBottom(requireContext(), num, url,homeUrl)
    }

    inner class LevelAdapter : BaseRecyclerAdapter<MineLevelList>() {


        override fun getItemLayoutId(viewType: Int) = R.layout.adapter_item_level

        @SuppressLint("SetTextI18n")
        override fun bindData(holder: RecyclerViewHolder, position: Int, data: MineLevelList?) {
            val name = holder.findViewById<TextView>(R.id.tvLevelName)
            when (data?.level) {
                "1" -> {
                    name.setCompoundDrawablesWithIntrinsicBounds(
                        getDrawable(R.mipmap.ic_v_1),
                        null,
                        null,
                        null
                    )
                }
                "2" -> {
                    name.setCompoundDrawablesWithIntrinsicBounds(
                        getDrawable(R.mipmap.ic_v_2),
                        null,
                        null,
                        null
                    )
                }
                "3" -> {
                    name.setCompoundDrawablesWithIntrinsicBounds(
                        getDrawable(R.mipmap.ic_v_3),
                        null,
                        null,
                        null
                    )
                }
                "4" -> {
                    name.setCompoundDrawablesWithIntrinsicBounds(
                        getDrawable(R.mipmap.ic_v_4),
                        null,
                        null,
                        null
                    )
                }
                "5" -> {
                    name.setCompoundDrawablesWithIntrinsicBounds(
                        getDrawable(R.mipmap.ic_v_5),
                        null,
                        null,
                        null
                    )
                }
                "6" -> {
                    name.setCompoundDrawablesWithIntrinsicBounds(
                        getDrawable(R.mipmap.ic_v_6),
                        null,
                        null,
                        null
                    )
                }
            }
            name.text = data?.invitee_num + "人"
            if (data?.level == "1") {
                holder.text(
                    R.id.tvLevelContent,
                    "成功邀请一人立即到账" + data.reward?.toInt() + "元-返现比例" + (data.rebate
                        ?: 0.1) * 100 + "%"
                )
            } else {
                holder.text(
                    R.id.tvLevelContent,
                    "成功邀请立即到账" + data?.reward?.toInt() + "元-返现比例" + (data?.rebate
                        ?: 0.1) * 100 + "%"
                )
            }

        }
    }
}


