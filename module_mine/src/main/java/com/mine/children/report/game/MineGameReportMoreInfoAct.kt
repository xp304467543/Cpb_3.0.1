package com.mine.children.report.game

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.customer.data.game.GameAg
import com.customer.data.game.GameAgLive
import com.customer.data.game.GameChess
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
import java.lang.Exception

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
    var gameType = 1

    var adapter: LiveRoomRecordAdapter? = null

     var gameAdapter:GameChessApter?=null

    var gameAgLiveAdapter:GameAgLiveAdapter?=null

    var gameAgGameAdapter:GameAgAdapter?=null

    override fun attachView() = mPresenter.attachView(this)


    override fun attachPresenter() = MineGameReportMoreInfoPresenter()

    override fun isOverride() = false

    override fun isShowBackIconWhite() = false

    override fun isSwipeBackEnable() = true

    override fun isShowToolBar() = false

    override fun getContentResID() = R.layout.act_mine_game_report_more_info


    @SuppressLint("SetTextI18n")
    override fun initContentView() {
        StatusBarUtils.setStatusBarHeight(gameInfoStateView)

        rvGameReportInfo.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        gameType = intent.getIntExtra("gameType",1)
        when(gameType){
            1 ->{
                adapter = LiveRoomRecordAdapter()
                rvGameReportInfo.adapter = adapter
                gameInfoPageTitle.text = "注单详情"
            }
            2 ->{
                setGone(topSelect)
                gameAdapter = GameChessApter()
                rvGameReportInfo.adapter = gameAdapter
                gameInfoPageTitle.text = "乐购棋牌注单详情"
            }
            3->{
                setGone(topSelect)
                gameAgLiveAdapter = GameAgLiveAdapter()
                rvGameReportInfo.adapter = gameAgLiveAdapter
                gameInfoPageTitle.text = "AG视讯注单详情"
            }
            4->{
                setGone(topSelect)
                gameAgGameAdapter = GameAgAdapter()
                rvGameReportInfo.adapter = gameAgGameAdapter
                gameInfoPageTitle.text = "AG游戏注单详情"
            }
        }
        lotteryId = intent.getStringExtra("rLotteryId") ?: "0"
        currentSel = intent.getStringExtra("is_bl_play") ?: "0"
        st = intent.getStringExtra("startTime") ?: ""
        et = intent.getStringExtra("endTime") ?: ""

    }

    override fun initData() {
        when(gameType){
            1 -> {
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

            2->{
                mPresenter.getGameResponse(lotteryId,st,et,index)
                smBetRecord_1?.setOnRefreshListener {
                    index = 1
                    mPresenter.getGameResponse(lotteryId,st,et,index)
                }
                smBetRecord_1?.setOnLoadMoreListener {
                    index++
                    mPresenter.getGameResponse(lotteryId,st,et,index)
                }
            }

            3 ->{
                mPresenter.getGameAgLive(lotteryId,st,et,index)
                smBetRecord_1?.setOnRefreshListener {
                    index = 1
                    mPresenter.getGameAgLive(lotteryId,st,et,index)
                }
                smBetRecord_1?.setOnLoadMoreListener {
                    index++
                    mPresenter.getGameAgLive(lotteryId,st,et,index)
                }
            }
            4 ->{
                mPresenter.getGameAgGame(lotteryId,st,et,index)
                smBetRecord_1?.setOnRefreshListener {
                    index = 1
                    mPresenter.getGameAgGame(lotteryId,st,et,index)
                }
                smBetRecord_1?.setOnLoadMoreListener {
                    index++
                    mPresenter.getGameAgGame(lotteryId,st,et,index)
                }
            }
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
            when(gameType){
                1 ->mPresenter.getResponse(state, lotteryId, st, et, currentSel)
                2 ->mPresenter.getGameResponse(lotteryId,st,et,index)
                3 ->mPresenter.getGameAgLive(lotteryId,st,et,index)
                4 ->mPresenter.getGameAgGame(lotteryId,st,et,index)
            }
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
            when(gameType){
                1 ->mPresenter.getResponse(state, lotteryId, st, et, currentSel)
                2 ->mPresenter.getGameResponse(lotteryId,st,et,index)
                3 ->mPresenter.getGameAgLive(lotteryId,st,et,index)
                4 ->mPresenter.getGameAgGame(lotteryId,st,et,index)
            }
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
            when(gameType){
                1 ->mPresenter.getResponse(state, lotteryId, st, et, currentSel)
                2 ->mPresenter.getGameResponse(lotteryId,st,et,index)
                3 ->mPresenter.getGameAgLive(lotteryId,st,et,index)
                4 ->mPresenter.getGameAgGame(lotteryId,st,et,index)
            }
        }
        gameInfoImgDate.setOnClickListener {
            if (dataDialog == null) {
                dataDialog = DialogDataPickDouble(this, R.style.dialog)
                dataDialog?.setConfirmClickListener { data1, data2 ->
                    index = 1
                    st = data1
                    et = data2
                    adapter?.clear()
                    when(gameType){
                        1 ->mPresenter.getResponse(state, lotteryId, data1, data2, currentSel)
                        2 ->mPresenter.getGameResponse(lotteryId,st,et,index)
                        3 ->mPresenter.getGameAgLive(lotteryId,st,et,index)
                        4 ->mPresenter.getGameAgGame(lotteryId,st,et,index)
                    }
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


    inner class GameChessApter :BaseRecyclerAdapter<GameChess>(){
        override fun getItemLayoutId(viewType: Int) = R.layout.adapter_game_info
        override fun bindData(holder: RecyclerViewHolder, position: Int, data: GameChess?) {
           try {
              holder.text(R.id.tv_1,TimeUtils.longToDateString(data?.play_time?:0))
               holder.text(R.id.tv_2,data?.sz_server_name)
               holder.text(R.id.tv_3,data?.amount)
               val text4 =  holder.findViewById<TextView>(R.id.tv_4)
               if (data?.prize?.contains("+") == true){
                   text4.setTextColor(ViewUtils.getColor(R.color.color_FF513E))
               }else  text4.setTextColor(ViewUtils.getColor(R.color.color_333333))
               text4.text = data?.prize
           }catch (e:Exception){}
        }
    }


    inner class GameAgLiveAdapter :BaseRecyclerAdapter<GameAgLive>(){
        override fun getItemLayoutId(viewType: Int) = R.layout.adapter_game_info
        override fun bindData(holder: RecyclerViewHolder, position: Int, data: GameAgLive?) {
            try {
                holder.text(R.id.tv_1,TimeUtils.longToDateString(data?.bet_time?:0))
                holder.text(R.id.tv_2,data?.game_name)
                holder.text(R.id.tv_3,data?.amount)
                val text4 =  holder.findViewById<TextView>(R.id.tv_4)
                if (data?.prize?.contains("+") == true){
                    text4.setTextColor(ViewUtils.getColor(R.color.color_FF513E))
                }else  text4.setTextColor(ViewUtils.getColor(R.color.color_333333))
                text4.text = data?.prize
            }catch (e:Exception){}
        }
    }


    inner class GameAgAdapter :BaseRecyclerAdapter<GameAg>(){
        override fun getItemLayoutId(viewType: Int) = R.layout.adapter_game_info
        override fun bindData(holder: RecyclerViewHolder, position: Int, data: GameAg?) {
            try {
                holder.text(R.id.tv_1,TimeUtils.longToDateString(data?.billtime?:0))
                holder.text(R.id.tv_2,data?.game_name)
                holder.text(R.id.tv_3,data?.amount)
                val text4 =  holder.findViewById<TextView>(R.id.tv_4)
                if (data?.prize?.contains("+") == true){
                    text4.setTextColor(ViewUtils.getColor(R.color.color_FF513E))
                }else  text4.setTextColor(ViewUtils.getColor(R.color.color_333333))
                text4.text = data?.prize
            }catch (e:Exception){}
        }
    }


}