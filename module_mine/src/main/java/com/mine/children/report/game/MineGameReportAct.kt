package com.mine.children.report.game

import android.content.Intent
import com.customer.data.UserInfoSp
import com.lib.basiclib.base.mvp.BaseMvpActivity
import com.lib.basiclib.utils.FastClickUtil
import com.lib.basiclib.utils.TimeUtils
import com.lib.basiclib.utils.ViewUtils
import com.mine.R
import com.xiaojinzi.component.anno.RouterAnno
import cuntomer.them.AppMode
import kotlinx.android.synthetic.main.act_mine_game_report.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/6/27
 * @ Describe
 *
 */
@RouterAnno(host = "Mine", path = "reportGame")
class MineGameReportAct : BaseMvpActivity<MineGameReportPresenter>() {

    var currentIndex = 0

    override fun attachView()  = mPresenter.attachView(this)

    override fun getPageTitle() = "游戏报表"

    override fun attachPresenter() = MineGameReportPresenter()

    override fun isShowBackIconWhite() = false

    override fun isSwipeBackEnable() = true

    override fun getContentResID() = R.layout.act_mine_game_report

    override fun initContentView() {
        if (UserInfoSp.getAppMode() == AppMode.Pure){
            setGone(tv_01)
            setGone(tvTotal1)
            setGone(tv_02)
            setGone(tvTotal_1)
            setGone(lin_2)
            setGone(lin_1)
            setGone(zLin1)
            setGone(zLin2)
            setGone(line_3)
            setGone(tvTotal_04)
            setGone(tvTotal_4)
            setGone(lin_4)
        }
    }


    override fun initData() {
        mPresenter.getReport(TimeUtils.getToday(),TimeUtils.getToday())
    }

    override fun initEvent() {
        tv_1.setOnClickListener {
            change(1)
            currentIndex = 0
        }
        tv_2.setOnClickListener {
            change(2)
            currentIndex = 1
        }
        tv_3.setOnClickListener {
            change(3)
            currentIndex = 2
        }
        tv_show_more.setOnClickListener {
            if (!FastClickUtil.isFastClick()){
                val intent = Intent(this, MineGameReportMoreAct::class.java)
                intent.putExtra("indexGame",1)
                intent.putExtra("currentIndex", currentIndex)
                startActivity(intent)
            }
        }
        tv_show_more_qp.setOnClickListener {
            if (!FastClickUtil.isFastClick()){
                val intent = Intent(this, MineGameReportMoreAct::class.java)
                intent.putExtra("indexGame",2)
                intent.putExtra("currentIndex", currentIndex)
                startActivity(intent)
            }
        }
        tv_show_more_agsx.setOnClickListener {
            if (!FastClickUtil.isFastClick()){
                val intent = Intent(this, MineGameReportMoreAct::class.java)
                intent.putExtra("indexGame",3)
                intent.putExtra("currentIndex", currentIndex)
                startActivity(intent)
            }
        }
        tv_show_more_agdz.setOnClickListener {
            if (!FastClickUtil.isFastClick()){
                val intent = Intent(this, MineGameReportMoreAct::class.java)
                intent.putExtra("indexGame",4)
                intent.putExtra("currentIndex", currentIndex)
                startActivity(intent)
            }
        }
        tv_show_more_bgsx.setOnClickListener {
            if (!FastClickUtil.isFastClick()){
                val intent = Intent(this, MineGameReportMoreAct::class.java)
                intent.putExtra("indexGame",5)
                intent.putExtra("currentIndex", currentIndex)
                startActivity(intent)
            }
        }
        tv_show_more_bg_fish.setOnClickListener {
            if (!FastClickUtil.isFastClick()){
                val intent = Intent(this, MineGameReportMoreAct::class.java)
                intent.putExtra("indexGame",6)
                intent.putExtra("currentIndex", currentIndex)
                startActivity(intent)
            }
        }
        tv_show_more_kyqp.setOnClickListener {
            if (!FastClickUtil.isFastClick()){
                val intent = Intent(this, MineGameReportMoreAct::class.java)
                intent.putExtra("indexGame",7)
                intent.putExtra("currentIndex", currentIndex)
                startActivity(intent)
            }
        }
        tv_show_more_sbty.setOnClickListener {
            if (!FastClickUtil.isFastClick()){
                val intent = Intent(this, MineGameReportMoreAct::class.java)
                intent.putExtra("indexGame",8)
                intent.putExtra("currentIndex", currentIndex)
                startActivity(intent)
            }
        }
    }

    private fun change(index:Int){
        when (index) {
            1 -> {
                tv_1.setBackgroundResource(R.drawable.button_red_background)
                tv_2.setBackgroundResource(R.drawable.button_grey_background)
                tv_3.setBackgroundResource(R.drawable.button_grey_background)
                tv_1.setTextColor(ViewUtils.getColor(R.color.white))
                tv_2.setTextColor(ViewUtils.getColor(R.color.color_333333))
                tv_3.setTextColor(ViewUtils.getColor(R.color.color_333333))
                mPresenter.getReport(TimeUtils.getToday(),TimeUtils.getToday())
            }
            2 -> {
                tv_2.setBackgroundResource(R.drawable.button_red_background)
                tv_1.setBackgroundResource(R.drawable.button_grey_background)
                tv_3.setBackgroundResource(R.drawable.button_grey_background)
                tv_2.setTextColor(ViewUtils.getColor(R.color.white))
                tv_1.setTextColor(ViewUtils.getColor(R.color.color_333333))
                tv_3.setTextColor(ViewUtils.getColor(R.color.color_333333))
                mPresenter.getReport(TimeUtils.get7before(),TimeUtils.getToday())
            }
            else -> {
                tv_3.setBackgroundResource(R.drawable.button_red_background)
                tv_1.setBackgroundResource(R.drawable.button_grey_background)
                tv_2.setBackgroundResource(R.drawable.button_grey_background)
                tv_3.setTextColor(ViewUtils.getColor(R.color.white))
                tv_2.setTextColor(ViewUtils.getColor(R.color.color_333333))
                tv_1.setTextColor(ViewUtils.getColor(R.color.color_333333))
                mPresenter.getReport(TimeUtils.get3MonthBefore(),TimeUtils.getToday())
            }
        }
    }
}