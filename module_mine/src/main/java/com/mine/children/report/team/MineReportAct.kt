package com.mine.children.report.team

import android.content.Intent
import com.customer.component.dialog.DialogSearch
import com.lib.basiclib.base.fragment.BaseFragment
import com.lib.basiclib.base.mvp.BaseMvpActivity
import com.lib.basiclib.base.xui.utils.StatusBarUtils
import com.lib.basiclib.utils.StatusBarUtils.setStatusBarHeight
import com.mine.R
import com.xiaojinzi.component.anno.RouterAnno
import kotlinx.android.synthetic.main.act_mine_report.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/24
 * @ Describe
 *
 */
@RouterAnno(host = "Mine", path = "report")
class MineReportAct : BaseMvpActivity<MineReportActPresenter>() {

    override fun attachView() = mPresenter.attachView(this)

    override fun getPageTitle() = "团队统计"

    override fun attachPresenter() = MineReportActPresenter()

    override fun isOverride() = false

    override fun isShowBackIconWhite() = false

    override fun isSwipeBackEnable() = true

    override fun isShowToolBar() = false

    override fun getContentResID() = R.layout.act_mine_report

    private val mFragments = arrayListOf<BaseFragment>()

    override fun initContentView() {
        setStatusBarHeight(reportStateView)
        setVisible(imgSearch)
        mFragments.add(ReportFragment1())
        mFragments.add(ReportFragment2())
        mFragments.add(ReportFragment3())
        mFragments.add(ReportFragment4())

        loadMultipleRootFragment(
            R.id.reportContainer, 0,
            mFragments[0], mFragments[1], mFragments[2], mFragments[3]
        )
    }


    override fun initData() {
        mPresenter.getNew("0")
    }

    override fun initEvent() {
        imgBack.setOnClickListener {
            finish()
        }
        tab1.setOnClickListener {
            setVisible(containerTop)
            pageTitle.text = ("团队统计")
            tvTopTitle1.text = "会员人数"
            mPresenter.getNew("0")
            showHideFragment(mFragments[0])
        }
        tab2.setOnClickListener {
            setVisible(containerTop)
            pageTitle.text = ("会员报表")
            tvTopTitle1.text = "会员人数"
            mPresenter.getNew("1")
            showHideFragment(mFragments[1])
            setVisible(imgSearch)

        }
        tab3.setOnClickListener {
            setVisible(containerTop)
            pageTitle.text = ("会员下级报表")
            tvTopTitle1.text = "团队人数"
            mPresenter.getNew("2")
            showHideFragment(mFragments[2])
        }
        tab4.setOnClickListener {
            setGone(containerTop)
            pageTitle.text = ("邀请")
            tvTopTitle1.text = "会员人数"
            showHideFragment(mFragments[3])
            setGone(imgSearch)
        }
        imgSearch.setOnClickListener {
            val dialog = DialogSearch(this)
            dialog.setConfirmClickListener {
                val intent = Intent(this, MineReportSearchAct::class.java)
                intent.putExtra("searchName", it)
                startActivity(intent)
                dialog.dismiss()
            }
            dialog.show()
        }
    }
}