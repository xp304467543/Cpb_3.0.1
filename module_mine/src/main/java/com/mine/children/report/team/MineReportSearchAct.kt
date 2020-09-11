package com.mine.children.report.team

import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.glide.GlideUtil
import com.lib.basiclib.base.activity.BaseNavActivity
import com.lib.basiclib.base.recycle.BaseRecyclerAdapter
import com.lib.basiclib.base.recycle.RecyclerViewHolder
import com.lib.basiclib.utils.StatusBarUtils.setStatusBarHeight
import com.lib.basiclib.utils.TimeUtils
import com.mine.R
import com.customer.data.mine.MineApi
import com.customer.data.mine.MineVipList
import kotlinx.android.synthetic.main.act_mine_report_search.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/24
 * @ Describe
 *
 */
class MineReportSearchAct : BaseNavActivity() {

    var adapter: LevelAdapter? = null

    override fun isOverride() = false

    override fun isShowBackIconWhite() = false

    override fun isSwipeBackEnable() = true

    override fun getContentResID() = R.layout.act_mine_report_search

    override fun isShowToolBar() = false


    override fun initContentView() {
        setStatusBarHeight(stateViewSearch)
        adapter = LevelAdapter()
        rvSearch.adapter = adapter
        rvSearch.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        etSearchName.setText(intent.getStringExtra("searchName") ?: "")
    }

    override fun initData() {
        if ((intent.getStringExtra("searchName") ?: "").isNotEmpty()) {
            MineApi.getVipLevel(
                sub_nickname = intent.getStringExtra("searchName")
                    ?: "", page = 1, is_sub = 1
            ) {
                onSuccess {
                    if (!it.isNullOrEmpty()) {
                        setGone(linSearchHolder)
                        adapter?.refresh(it)
                    } else setVisible(linSearchHolder)
                }
                onFailed {
                    adapter?.clear()
                    rvSearch.removeAllViews()
                    setVisible(linSearchHolder)
                }
            }
        }

    }

    override fun initEvent() {

        etSearchName.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (!etSearchName.text.isNullOrEmpty()) {
                    MineApi.getVipLevel(
                        sub_nickname = etSearchName.text.toString(),
                        page = 1,
                        is_sub = 1
                    ) {
                        onSuccess {
                            if (!it.isNullOrEmpty()) {
                                setGone(linSearchHolder)
                                adapter?.refresh(it)
                            } else setVisible(linSearchHolder)
                        }
                        onFailed {
                            adapter?.clear()
                            rvSearch.removeAllViews()
                            setVisible(linSearchHolder)
                        }
                    }
                }
            }
            false
        }
        tvActCancel.setOnClickListener {
            finish()
        }

    }


    inner class LevelAdapter : BaseRecyclerAdapter<MineVipList>() {


        override fun getItemLayoutId(viewType: Int) = R.layout.adapter_item_vip

        override fun bindData(holder: RecyclerViewHolder, position: Int, data: MineVipList?) {
            GlideUtil.loadCircleImage(
                this@MineReportSearchAct,
                data?.avatar,
                holder.getImageView(R.id.imgPhotoUser),
                true
            )
            val userLevel = holder.findViewById<TextView>(R.id.tvNameUser)
            holder.text(
                R.id.tvTimeUser,
                TimeUtils.getYearMonthDay((data?.created?.times(1000)) ?: 0)
            )
            holder.text(R.id.tv1_vip, data?.recharge)
            holder.text(R.id.tv2_vip, data?.exchange)
            holder.text(R.id.tv3_vip, data?.brokerage)
            val imgLevel = holder.findViewById<ImageView>(R.id.imgLevel)
            when (data?.level) {
                "1", "0" -> {
                    imgLevel.setBackgroundResource(R.mipmap.ic_v_1)
                }
                "2" -> {
                    imgLevel.setBackgroundResource(R.mipmap.ic_v_2)
                }
                "3" -> {
                    imgLevel.setBackgroundResource(R.mipmap.ic_v_3)
                }
                "4" -> {
                    imgLevel.setBackgroundResource(R.mipmap.ic_v_4)
                }
                "5" -> {
                    imgLevel.setBackgroundResource(R.mipmap.ic_v_5)
                }
                "6" -> {
                    imgLevel.setBackgroundResource(R.mipmap.ic_v_6)
                }
            }
            userLevel.text = data?.nickname
        }
    }

}