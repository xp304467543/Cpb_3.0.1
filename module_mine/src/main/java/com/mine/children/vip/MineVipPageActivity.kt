package com.mine.children.vip

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.customer.data.mine.VipBean
import com.lib.basiclib.base.mvp.BaseMvpActivity
import com.lib.basiclib.base.recycle.BaseRecyclerAdapter
import com.lib.basiclib.base.recycle.RecyclerViewHolder
import com.lib.basiclib.base.round.RoundTextView
import com.lib.basiclib.utils.FastClickUtil
import com.lib.basiclib.utils.StatusBarUtils
import com.lib.basiclib.utils.ToastUtils
import com.lib.basiclib.utils.ViewUtils
import com.mine.R
import kotlinx.android.synthetic.main.act_mine_vip_page.*

/**
 *
 * @ Author  QinTian
 * @ Date  1/19/21
 * @ Describe
 *
 */
class MineVipPageActivity : BaseMvpActivity<MineVipPagePresenter>() {

    var currentVip = 0

    override fun attachView() = mPresenter.attachView(this)

    override fun attachPresenter() = MineVipPagePresenter()


    override fun isShowToolBar() = false

    override fun isSwipeBackEnable() = true

    override fun getContentResID() = R.layout.act_mine_vip_page

    override fun initContentView() {
        StatusBarUtils.setStatusBarHeight(vipPageStateView)
    }

    override fun initData() {
        showPageLoadingDialog()
        mPresenter.getPageInfo()
    }

    override fun initEvent() {
        vipBack.setOnClickListener {
            finish()
        }
        tvVipInfo.setOnClickListener {
            if (!FastClickUtil.isFastClick()) {
                val intent = Intent(this, MineVipInfoActivity::class.java)
                intent.putExtra("vipLevel", currentVip)
                startActivity(intent)
            }
        }
    }

    var adapter: RvAdapter? = null
    fun unDateBanner(vip: Int) {
        val list = getList(vip)
        adapter = RvAdapter()
        rvLineView.adapter = adapter
        rvLineView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        adapter?.refresh(getVip(vip))
        viewPageVip.adapter = GalleryAdapter(list)
        viewPageVip.offscreenPageLimit = 8
        viewPageVip.setPageTransformer(false, ZoomOutVipPageTransformer())
        viewPageVip.currentItem = 0
        viewPageVip.pageMargin = -60
        viewPageVip.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                adapter?.setData(position)
                if (position > 1) {
                    setVisible(linVipHigh)
                    setGone(linVipLow)
                } else {
                    setGone(linVipHigh)
                    setVisible(linVipLow)
                }
            }
        })
    }

    inner class RvAdapter : BaseRecyclerAdapter<VipBean>() {

        override fun getItemLayoutId(viewType: Int) = R.layout.adapter_vip_item

        override fun bindData(holder: RecyclerViewHolder, position: Int, data: VipBean?) {
            val line = holder.findViewById<TextView>(R.id.tvLine)
            val text = holder.findViewById<RoundTextView>(R.id.tvVip)
            if (position == 0) setVisible(holder.findView(R.id.tvHolder)) else setGone(
                holder.findView(
                    R.id.tvHolder
                )
            )
            if (position == (getData().size - 1)) {
                ViewUtils.setGone(line)
            } else ViewUtils.setVisible(line)
            text.text = data?.data
            if (data?.isSelect == true) {
                text.delegate.backgroundColor = ViewUtils.getColor(R.color.color_696D84)
                text.setTextColor(ViewUtils.getColor(R.color.white))
            } else {
                text.delegate.backgroundColor = ViewUtils.getColor(R.color.color_CFCFDE)
                text.setTextColor(ViewUtils.getColor(R.color.color_666666))
            }

            holder.itemView.setOnClickListener {
                viewPageVip.setCurrentItem(position, true)
            }
        }

        fun setData(int: Int) {
            for ((index, item) in data.withIndex()) {
                data[index].isSelect = index == int
            }
            rvLineView.scrollToPosition(int)
            notifyDataSetChanged()
        }

    }


    inner class GalleryAdapter(val data: List<Int>) : PagerAdapter() {

        override fun getCount(): Int {
            return data.size
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }


        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val view = LayoutInflater.from(this@MineVipPageActivity)
                .inflate(R.layout.gallery_item_card, null)
            val poster = view.findViewById<AppCompatImageView>(R.id.cardBg)
            poster.setImageResource(data[position])
            container.addView(view)
            return view
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }
    }


    private fun getVip(int: Int): List<VipBean> {
        val list = arrayListOf<VipBean>()
        repeat(8) {
            if (int == (it + 1)) {
                list.add(VipBean(true, "VIP" + (it + 1)))
            } else list.add(VipBean(false, "VIP" + (it + 1)))
        }
        return list
    }

    private fun getList(int: Int): List<Int> {
        return when (int) {
            1 -> arrayListOf(
                R.drawable.card_vip_1,
                R.drawable.card_vip_n_2,
                R.drawable.card_vip_n_3,
                R.drawable.card_vip_n_4,
                R.drawable.card_vip_n_5,
                R.drawable.card_vip_n_6,
                R.drawable.card_vip_n_7,
                R.drawable.card_vip_n_8
            )
            2 -> arrayListOf(
                R.drawable.card_vip_n_1,
                R.drawable.card_vip_2,
                R.drawable.card_vip_n_3,
                R.drawable.card_vip_n_4,
                R.drawable.card_vip_n_5,
                R.drawable.card_vip_n_6,
                R.drawable.card_vip_n_7,
                R.drawable.card_vip_n_8
            )
            3 -> arrayListOf(
                R.drawable.card_vip_n_1,
                R.drawable.card_vip_n_2,
                R.drawable.card_vip_3,
                R.drawable.card_vip_n_4,
                R.drawable.card_vip_n_5,
                R.drawable.card_vip_n_6,
                R.drawable.card_vip_n_7,
                R.drawable.card_vip_n_8
            )
            4 -> arrayListOf(
                R.drawable.card_vip_n_1,
                R.drawable.card_vip_n_2,
                R.drawable.card_vip_n_3,
                R.drawable.card_vip_4,
                R.drawable.card_vip_n_5,
                R.drawable.card_vip_n_6,
                R.drawable.card_vip_n_7,
                R.drawable.card_vip_n_8
            )
            5 -> arrayListOf(
                R.drawable.card_vip_n_1,
                R.drawable.card_vip_n_2,
                R.drawable.card_vip_n_3,
                R.drawable.card_vip_n_4,
                R.drawable.card_vip_5,
                R.drawable.card_vip_n_6,
                R.drawable.card_vip_n_7,
                R.drawable.card_vip_n_8
            )
            6 -> arrayListOf(
                R.drawable.card_vip_n_1,
                R.drawable.card_vip_n_2,
                R.drawable.card_vip_n_3,
                R.drawable.card_vip_n_4,
                R.drawable.card_vip_n_5,
                R.drawable.card_vip_6,
                R.drawable.card_vip_n_7,
                R.drawable.card_vip_n_8
            )
            7 -> arrayListOf(
                R.drawable.card_vip_n_1,
                R.drawable.card_vip_n_2,
                R.drawable.card_vip_n_3,
                R.drawable.card_vip_n_4,
                R.drawable.card_vip_n_5,
                R.drawable.card_vip_n_6,
                R.drawable.card_vip_7,
                R.drawable.card_vip_n_8
            )
            8 -> arrayListOf(
                R.drawable.card_vip_n_1,
                R.drawable.card_vip_n_2,
                R.drawable.card_vip_n_3,
                R.drawable.card_vip_n_4,
                R.drawable.card_vip_n_5,
                R.drawable.card_vip_n_6,
                R.drawable.card_vip_n_7,
                R.drawable.card_vip_8
            )
            else -> arrayListOf(
                R.drawable.card_vip_n_1,
                R.drawable.card_vip_n_2,
                R.drawable.card_vip_n_3,
                R.drawable.card_vip_n_4,
                R.drawable.card_vip_n_5,
                R.drawable.card_vip_n_6,
                R.drawable.card_vip_n_7,
                R.drawable.card_vip_n_8
            )
        }
    }
}