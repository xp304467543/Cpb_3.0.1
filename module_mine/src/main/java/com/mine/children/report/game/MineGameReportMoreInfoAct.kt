package com.mine.children.report.game

import androidx.recyclerview.widget.LinearLayoutManager
import com.lib.basiclib.base.mvp.BaseMvpActivity
import com.lib.basiclib.base.recycle.BaseRecyclerAdapter
import com.lib.basiclib.base.recycle.RecyclerViewHolder
import com.lib.basiclib.utils.StatusBarUtils
import com.lib.basiclib.utils.TimeUtils
import com.lib.basiclib.utils.ViewUtils
import com.mine.R
import com.customer.data.mine.LotteryBetHistoryResponse
import com.mine.dialog.DialogDataPickDouble
import kotlinx.android.synthetic.main.act_mine_game_report_more_info.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/7/3
 * @ Describe
 *
 */
class MineGameReportMoreInfoAct : BaseMvpActivity<MineGameReportMoreInfoPresenter>() {


    private var dataDialog: DialogDataPickDouble? = null


    var currentSel = "0" //默认钻石

    var index = 1

    var pos = 2

    var state = 0

    var lotteryId = ""

    var st = ""
    var et = ""

    var adapter: LiveRoomRecordAdapter? = null

    override fun attachView() = mPresenter.attachView(this)


    override fun attachPresenter() = MineGameReportMoreInfoPresenter()

    override fun isOverride() = false

    override fun isShowBackIconWhite() = false

    override fun isSwipeBackEnable() = true

    override fun isShowToolBar() = false

    override fun getContentResID() = R.layout.act_mine_game_report_more_info


    override fun initContentView() {
        StatusBarUtils.setStatusBarHeight(gameInfoStateView)
        gameInfoPageTitle.text = "注单详情"
        adapter = LiveRoomRecordAdapter()
        rvGameReportInfo.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvGameReportInfo.adapter = adapter
        lotteryId = intent.getStringExtra("rLotteryId") ?: "0"
        currentSel = intent.getStringExtra("is_bl_play") ?: "0"
        st = intent.getStringExtra("startTime") ?: ""
        et = intent.getStringExtra("endTime") ?: ""
    }

    override fun initData() {
        mPresenter.getResponse(state, lotteryId, st, et, currentSel)
        smBetRecord_1?.setOnRefreshListener {
            index = 1
            mPresenter.getResponse(state, lotteryId, st, et, currentSel)
        }
        smBetRecord_1?.setOnLoadMoreListener {
            index++
            mPresenter.getResponse(state, lotteryId, st, et, currentSel)
        }
    }

    override fun initEvent() {
        tv_all.setOnClickListener {
            tv_all.setBackgroundResource(R.drawable.button_red_background)
            tv_01.setBackgroundResource(R.drawable.button_grey_background)
            tv_02.setBackgroundResource(R.drawable.button_grey_background)
            tv_all.setTextColor(ViewUtils.getColor(R.color.white))
            tv_01.setTextColor(ViewUtils.getColor(R.color.color_333333))
            tv_02.setTextColor(ViewUtils.getColor(R.color.color_333333))
            state = 0
            pos = 2
            index = 1
            adapter?.clear()
            mPresenter.getResponse(state, lotteryId, st, et, currentSel)
        }
        tv_01.setOnClickListener {
            tv_01.setBackgroundResource(R.drawable.button_red_background)
            tv_all.setBackgroundResource(R.drawable.button_grey_background)
            tv_02.setBackgroundResource(R.drawable.button_grey_background)
            tv_01.setTextColor(ViewUtils.getColor(R.color.white))
            tv_all.setTextColor(ViewUtils.getColor(R.color.color_333333))
            tv_02.setTextColor(ViewUtils.getColor(R.color.color_333333))
            state = 1
            pos = 1
            index = 1
            adapter?.clear()
            mPresenter.getResponse(state, lotteryId, st, et, currentSel)
        }
        tv_02.setOnClickListener {
            tv_02.setBackgroundResource(R.drawable.button_red_background)
            tv_01.setBackgroundResource(R.drawable.button_grey_background)
            tv_all.setBackgroundResource(R.drawable.button_grey_background)
            tv_02.setTextColor(ViewUtils.getColor(R.color.white))
            tv_01.setTextColor(ViewUtils.getColor(R.color.color_333333))
            tv_all.setTextColor(ViewUtils.getColor(R.color.color_333333))
            state = 2
            pos = 0
            index = 1
            adapter?.clear()
            mPresenter.getResponse(state, lotteryId, st, et, currentSel)
        }
        gameInfoImgDate.setOnClickListener {
            if (dataDialog == null) {
                dataDialog = DialogDataPickDouble(this, R.style.dialog)
                dataDialog?.setConfirmClickListener { data1, data2 ->
                    index = 1
                    st = data1
                    et = data2
                    adapter?.clear()
                    mPresenter.getResponse(state, lotteryId, data1, data2, currentSel)
                    dataDialog?.dismiss()
                }
            } else dataDialog?.show()
        }
        gameInfoImgBack.setOnClickListener {
            finish()
        }
    }


    inner class LiveRoomRecordAdapter : BaseRecyclerAdapter<LotteryBetHistoryResponse>() {

        override fun getItemLayoutId(viewType: Int) = R.layout.adapter_bet_history

        override fun bindData(
            holder: RecyclerViewHolder,
            position: Int,
            data: LotteryBetHistoryResponse?
        ) {
            holder.text(R.id.tvBetTime, TimeUtils.longToDateString(data?.play_bet_time ?: 0))
            holder.text(R.id.tvBetName, data?.play_bet_lottery_name)
            holder.text(R.id.tvBetIssue, data?.play_bet_issue + " 期")
            holder.text(R.id.tvBetCodeName, data?.play_sec_name)
            holder.text(R.id.tvBetCode, data?.play_class_name)
            holder.text(R.id.tvBetOdds, data?.play_odds)
            when (pos) {
                1 -> {
                    holder.text(R.id.tvBetMoney, data?.play_bet_sum)
                    holder.textColorId(R.id.tvBetMoney, R.color.color_333333)
                }
                0 -> {
                    holder.text(R.id.tvBetMoney, data?.play_bet_score)
                    if (data?.play_bet_score?.contains("+") == true) {
                        holder.textColorId(R.id.tvBetMoney, R.color.color_FF513E)
                    } else holder.textColorId(R.id.tvBetMoney, R.color.color_333333)
                }
                2 -> {
                    if (data?.play_bet_score == "0") {
                        holder.text(R.id.tvBetMoney, data.play_bet_sum)
                        holder.textColorId(R.id.tvBetMoney, R.color.color_333333)
                    } else {
                        holder.text(R.id.tvBetMoney, data?.play_bet_score)
                        if (data?.play_bet_score?.contains("+") == true) {
                            holder.textColorId(R.id.tvBetMoney, R.color.color_FF513E)
                        } else holder.textColorId(R.id.tvBetMoney, R.color.color_333333)
                    }
                }
                else -> {
                    holder.text(R.id.tvBetMoney, data?.play_bet_sum)
                    holder.textColorId(R.id.tvBetMoney, R.color.color_333333)
                }
            }
        }

    }
}