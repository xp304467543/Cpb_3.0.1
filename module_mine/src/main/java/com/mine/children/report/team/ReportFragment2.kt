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
import kotlinx.android.synthetic.main.fragment_report_2.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/6/27
 * @ Describe
 *
 */
class ReportFragment2 : BaseMvpFragment<ReportFragment2P>() {

    var adapter: LevelAdapter? = null

    var page = 1

    override fun attachView() = mPresenter.attachView(this)

    override fun attachPresenter() = ReportFragment2P()

    override fun getLayoutResID() = R.layout.fragment_report_2


    override fun initContentView() {

        adapter = LevelAdapter()
        rvVip.adapter = adapter
        rvVip.layoutManager =
            LinearLayoutManager(getPageActivity(), LinearLayoutManager.VERTICAL, false)
        smartVip.setOnRefreshListener {
            page = 1
            mPresenter.getVip(page)
        }
        smartVip.setOnLoadMoreListener {
            mPresenter.getVip(page)
        }
    }


    override fun initData() {
        mPresenter.getVip(page)
    }

    override fun initEvent() {

    }


    inner class LevelAdapter : BaseRecyclerAdapter<MineVipList>() {


        override fun getItemLayoutId(viewType: Int) = R.layout.adapter_item_vip

        override fun bindData(holder: RecyclerViewHolder, position: Int, data: MineVipList?) {
            GlideUtil.loadCircleImage(
                requireContext(),
                data?.avatar,
                holder.getImageView(R.id.imgPhotoUser),
                true
            )
            val userLevel = holder.findViewById<TextView>(R.id.tvNameUser)
            val imgLevel = holder.findViewById<ImageView>(R.id.imgLevel)
            holder.text(
                R.id.tvTimeUser,
                TimeUtils.getYearMonthDay((data?.created?.times(1000)) ?: 0)
            )
            holder.text(R.id.tv1_vip, data?.recharge)
            holder.text(R.id.tv2_vip, data?.exchange)
            holder.text(R.id.tv3_vip, data?.brokerage)
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