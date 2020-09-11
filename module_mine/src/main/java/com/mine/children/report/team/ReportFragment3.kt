package com.mine.children.report.team

import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.glide.GlideUtil
import com.lib.basiclib.base.mvp.BaseMvpFragment
import com.lib.basiclib.base.recycle.BaseRecyclerAdapter
import com.lib.basiclib.base.recycle.RecyclerViewHolder
import com.lib.basiclib.utils.TimeUtils
import com.lib.basiclib.utils.ViewUtils
import com.mine.R
import com.customer.data.mine.MineVipList
import kotlinx.android.synthetic.main.fragment_report_3.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/6/27
 * @ Describe
 *
 */
class ReportFragment3 : BaseMvpFragment<ReportFragment3P>() {

    var adapter: LevelNextAdapter? = null

    var page = 1

    override fun attachView() = mPresenter.attachView(this)

    override fun attachPresenter() = ReportFragment3P()

    override fun getLayoutResID() = R.layout.fragment_report_3


    override fun initContentView() {

        adapter = LevelNextAdapter()
        rvReport3.adapter = adapter
        rvReport3.layoutManager = LinearLayoutManager(getPageActivity(), LinearLayoutManager.VERTICAL, false)
        smartVip3.setOnRefreshListener {
            page = 1
            mPresenter.getNextVip(page)
        }
        smartVip3.setOnLoadMoreListener {
            mPresenter.getNextVip(page)
        }
    }


    override fun initData() {
        mPresenter.getNextVip(page)
    }


    inner class LevelNextAdapter : BaseRecyclerAdapter<MineVipList>() {

        override fun getItemLayoutId(viewType: Int) = R.layout.adapter_item_vip

        override fun bindData(holder: RecyclerViewHolder, position: Int, data: MineVipList?) {
            holder.text(R.id.tv_title_1, "团队人数")
            holder.text(R.id.tv_title_2, "兑换金额")
            holder.text(R.id.tv_title_3, "返点金额")
            GlideUtil.loadCircleImage(requireContext(),data?.avatar,holder.findViewById(R.id.imgPhotoUser),true)
            val userLevel = holder.findViewById<TextView>(R.id.tvNameUser)
            holder.text(R.id.tvTimeUser, TimeUtils.getYearMonthDay(data?.created?.times(1000) ?: 0))
            holder.text(R.id.tv1_vip, data?.invitee_num)
            holder.text(R.id.tv2_vip, data?.exchange)
            holder.text(R.id.tv3_vip, data?.brokerage)
            val imgLevel = holder.findViewById<ImageView>(R.id.imgLevel)
            when (data?.level) {
                "1" -> {
                    ViewUtils.setVisible(imgLevel)
                    imgLevel.setBackgroundResource(R.mipmap.ic_v_1)
                }
                "2" -> {
                    ViewUtils.setVisible(imgLevel)
                    imgLevel.setBackgroundResource(R.mipmap.ic_v_2)
                }
                "3" -> {
                    ViewUtils.setVisible(imgLevel)
                    imgLevel.setBackgroundResource(R.mipmap.ic_v_3)
                }
                "4" -> {
                    ViewUtils.setVisible(imgLevel)
                    imgLevel.setBackgroundResource(R.mipmap.ic_v_4)
                }
                "5" -> {
                    ViewUtils.setVisible(imgLevel)
                    imgLevel.setBackgroundResource(R.mipmap.ic_v_5)
                }
                "6" -> {
                    ViewUtils.setVisible(imgLevel)
                    imgLevel.setBackgroundResource(R.mipmap.ic_v_6)
                }
                else -> {
                    ViewUtils.setGone(imgLevel)
                }
            }
            userLevel.text = data?.nickname
        }
    }
}