package com.mine.children.report.game

import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.customer.data.UserInfoSp
import com.customer.data.mine.MineGameAgReportInfo
import com.glide.GlideUtil
import com.lib.basiclib.base.mvp.BaseMvpActivity
import com.lib.basiclib.base.recycle.BaseRecyclerAdapter
import com.lib.basiclib.base.recycle.RecyclerViewHolder
import com.lib.basiclib.utils.StatusBarUtils
import com.lib.basiclib.utils.TimeUtils
import com.lib.basiclib.utils.ViewUtils
import com.mine.R
import com.customer.data.mine.MineGameReportInfo
import com.mine.dialog.DialogDataPickDouble
import cuntomer.them.AppMode
import kotlinx.android.synthetic.main.act_mine_game_report_more.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/7/2
 * @ Describe
 *
 */
class MineGameReportMoreAct : BaseMvpActivity<MineGameReportMorePresenter>() {

    var indexGame = 1

    var index = "1"

    var start = TimeUtils.getToday()

    var end = TimeUtils.getToday()

    var lotteryAdapter: ReportAdapter? = null

    var gameAdapter:GameAdapter?=null

    private var dataDialog: DialogDataPickDouble? = null

    override fun attachView() = mPresenter.attachView(this)

    override fun isShowToolBar() = false

    override fun attachPresenter() = MineGameReportMorePresenter()

    override fun isOverride() = false

    override fun isShowBackIconWhite() = false

    override fun isSwipeBackEnable() = true

    override fun getContentResID() = R.layout.act_mine_game_report_more

    override fun initContentView() {
        StatusBarUtils.setStatusBarHeight(gameStateView)
        indexGame = intent.getIntExtra("indexGame",1)
        gamePageTitle.text = when(indexGame){
            1 ->"彩票报表"
            2 -> "乐购棋牌"
            3 -> "AG视讯游戏报表"
            4 -> "AG电子游戏报表"
            else ->""
        }
        if (indexGame == 1){
            lotteryAdapter = ReportAdapter()
            rv_game.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            rv_game.adapter = lotteryAdapter
        }else{
            gameAdapter = GameAdapter()
            rv_game.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            rv_game.adapter = gameAdapter
        }

    }

    override fun initData() {
        when(indexGame){
            1 ->{
                if (UserInfoSp.getAppMode() == AppMode.Normal){
                    setVisible(topSelected)
                }
                mPresenter.getInfo(index, start, end)
            }
            2 -> {
                setGone(topSelected)
                mPresenter.getGameInfo(indexGame, start, end)
            }
            3 -> {
                setGone(topSelected)
                mPresenter.getGameInfo(indexGame, start, end)
            }
            4 -> {
                setGone(topSelected)
                mPresenter.getGameInfo(indexGame, start, end)
            }
            else ->mPresenter.getInfo(index, start, end)
        }

    }

    override fun initEvent() {
        tv_start.setOnClickListener {
            index = "1"
            tv_start.delegate.backgroundColor = ViewUtils.getColor(R.color.color_FF513E)
            tv_start.setTextColor(ViewUtils.getColor(R.color.white))
            tv_end.delegate.backgroundColor = ViewUtils.getColor(R.color.white)
            tv_end.setTextColor(ViewUtils.getColor(R.color.color_999999))
            mPresenter.getInfo(index, start, end)
        }
        tv_end.setOnClickListener {
            index = "0"
            tv_end.delegate.backgroundColor = ViewUtils.getColor(R.color.color_FF513E)
            tv_end.setTextColor(ViewUtils.getColor(R.color.white))
            tv_start.delegate.backgroundColor = ViewUtils.getColor(R.color.white)
            tv_start.setTextColor(ViewUtils.getColor(R.color.color_999999))
            mPresenter.getInfo(index, start, end)
        }
        tv_data_1.setOnClickListener {
            tv_data_1.setTextColor(ViewUtils.getColor(R.color.color_FF513E))
            tv_data_2.setTextColor(ViewUtils.getColor(R.color.color_333333))
            tv_data_3.setTextColor(ViewUtils.getColor(R.color.color_333333))
            start = TimeUtils.getToday()
            if (indexGame ==1) mPresenter.getInfo(index, start, end) else mPresenter.getGameInfo(indexGame, start, end)
        }
        tv_data_2.setOnClickListener {
            tv_data_2.setTextColor(ViewUtils.getColor(R.color.color_FF513E))
            tv_data_1.setTextColor(ViewUtils.getColor(R.color.color_333333))
            tv_data_3.setTextColor(ViewUtils.getColor(R.color.color_333333))
            start = TimeUtils.get7before()
            if (indexGame ==1) mPresenter.getInfo(index, start, end) else mPresenter.getGameInfo(indexGame, start, end)
        }
        tv_data_3.setOnClickListener {
            tv_data_3.setTextColor(ViewUtils.getColor(R.color.color_FF513E))
            tv_data_2.setTextColor(ViewUtils.getColor(R.color.color_333333))
            tv_data_1.setTextColor(ViewUtils.getColor(R.color.color_333333))
            start = TimeUtils.get3MonthBefore()
            if (indexGame ==1) mPresenter.getInfo(index, start, end) else mPresenter.getGameInfo(indexGame, start, end)
        }
        gameImgDate.setOnClickListener {
            if (dataDialog == null) {
                dataDialog = DialogDataPickDouble(this, R.style.dialog)
                dataDialog?.setConfirmClickListener { data1, data2 ->
                    if (indexGame ==1) mPresenter.getInfo(index, data1, data2) else mPresenter.getGameInfo(indexGame, data1, data2)
                    tv_data_3.setTextColor(ViewUtils.getColor(R.color.color_333333))
                    tv_data_2.setTextColor(ViewUtils.getColor(R.color.color_333333))
                    tv_data_1.setTextColor(ViewUtils.getColor(R.color.color_333333))
                    dataDialog?.dismiss()
                }
            } else dataDialog?.show()
        }
        gameImgBack.setOnClickListener {
            finish()
        }
    }


    inner class ReportAdapter : BaseRecyclerAdapter<MineGameReportInfo>() {
        override fun getItemLayoutId(viewType: Int) = R.layout.adapter_report_lottery
        override fun bindData(
            holder: RecyclerViewHolder,
            position: Int,
            data: MineGameReportInfo?
        ) {
            holder.text(R.id.tv_lottery_name, data?.lottery_name)
            holder.text(R.id.tv_lottery_1, data?.count)
            holder.text(R.id.tv_lottery_2, data?.amount)
            holder.text(R.id.tv_lottery_3, data?.prize)
            if (index == "0") {
                holder.text(R.id.tv_t_1, "钻石注单")
                holder.text(R.id.tv_t_2, "下单钻石")
                holder.text(R.id.tv_t_3, "中奖钻石")
            } else {
                holder.text(R.id.tv_t_1, "下单注量")
                holder.text(R.id.tv_t_2, "下单金额")
                holder.text(R.id.tv_t_3, "中奖金额")
            }
            GlideUtil.loadImage(
                this@MineGameReportMoreAct,
                data?.lottery_icon,
                holder.getImageView(R.id.imgLottery)
            )
            holder.click(R.id.tvLookMore) {
                val intent =
                    Intent(this@MineGameReportMoreAct, MineGameReportMoreInfoAct::class.java)
                intent.putExtra("rLotteryId", data?.lottery_id)
                intent.putExtra("is_bl_play", index)
                intent.putExtra("startTime", start)
                intent.putExtra("endTime", end)
                intent.putExtra("gameType", indexGame)
                startActivity(intent)
            }
        }
    }


    inner class GameAdapter : BaseRecyclerAdapter<MineGameAgReportInfo>() {
        override fun getItemLayoutId(viewType: Int) = R.layout.adapter_report_lottery
        override fun bindData(
            holder: RecyclerViewHolder,
            position: Int,
            data: MineGameAgReportInfo?
        ) {
            holder.text(R.id.tv_lottery_name, data?.game_name)
            holder.text(R.id.tv_lottery_1, data?.count)
            holder.text(R.id.tv_lottery_2, data?.amount)
            holder.text(R.id.tv_lottery_3, data?.prize)
                holder.text(R.id.tv_t_1, "下单注量")
                holder.text(R.id.tv_t_2, "下单金额")
                holder.text(R.id.tv_t_3, "中奖金额")
            if (!data?.img_url.isNullOrEmpty()){
                GlideUtil.loadImage(this@MineGameReportMoreAct, data?.img_url, holder.getImageView(R.id.imgLottery))
            }else{
                if (indexGame == 3){
                    holder.getImageView(R.id.imgLottery).setBackgroundResource(R.mipmap.ic_ag_game)
                }else if (indexGame == 4){
                    holder.getImageView(R.id.imgLottery).setBackgroundResource(R.mipmap.ic_ag_game)
                }
            }
            holder.click(R.id.tvLookMore) {
                val intent =
                    Intent(this@MineGameReportMoreAct, MineGameReportMoreInfoAct::class.java)
                intent.putExtra("rLotteryId", data?.game_id)
                intent.putExtra("is_bl_play", index)
                intent.putExtra("startTime", start)
                intent.putExtra("endTime", end)
                intent.putExtra("gameType", indexGame)
                startActivity(intent)
            }
        }
    }
}