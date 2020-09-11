package com.mine.children.report.game

import android.content.Intent
import com.lib.basiclib.base.mvp.BaseMvpActivity
import com.lib.basiclib.utils.TimeUtils
import com.lib.basiclib.utils.ViewUtils
import com.mine.R
import com.xiaojinzi.component.anno.RouterAnno
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

    override fun attachView()  = mPresenter.attachView(this)

    override fun getPageTitle() = "游戏报表"

    override fun attachPresenter() = MineGameReportPresenter()

    override fun isShowBackIconWhite() = false

    override fun isSwipeBackEnable() = true

    override fun getContentResID() = R.layout.act_mine_game_report


    override fun initData() {
        mPresenter.getReport(TimeUtils.getToday(),TimeUtils.getToday())
    }

    override fun initEvent() {
        tv_1.setOnClickListener {
            change(1)
        }
        tv_2.setOnClickListener {
            change(2)
        }
        tv_3.setOnClickListener {
            change(3)
        }
        tv_show_more.setOnClickListener {
            startActivity(Intent(this, MineGameReportMoreAct::class.java))
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