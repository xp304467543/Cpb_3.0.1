package com.customer.component.dialog.live

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.customer.component.BottomGiftPageGridView
import com.customer.data.UserInfoSp
import com.customer.data.home.HomeLiveAnimatorBean
import com.customer.data.home.HomeLiveGiftList
import com.fh.module_base_resouce.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import com.hwangjr.rxbus.RxBus
import com.lib.basiclib.utils.ToastUtils
import kotlinx.android.synthetic.main.dialog_chat_bottom_gif.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/27
 * @ Describe
 *
 */

class BottomHorGiftWindow(context: Context) : BottomSheetDialog(context) {


    var viewPager: ViewPager? = null
    var liveGiftNumPop: LiveGiftNumPop? = null
    private var pagerAdapter: BottomGiftAdapter? = null
    private var viewList = arrayListOf<BottomGiftPageGridView>()
    private var homeLiveGiftListBean: HomeLiveGiftList? = null

    init {
        setContentView(R.layout.dialog_chat_bottom_hor_gif)
        val root = delegate?.findViewById<View>(R.id.design_bottom_sheet)
//        BottomSheetBehavior.from(root).isHideable = false
        delegate?.findViewById<View>(R.id.design_bottom_sheet)
            ?.setBackgroundColor(Color.TRANSPARENT)
        initViews()
        iniEvent()
    }

    private fun iniEvent() {
        tvGiftMount.setOnClickListener {
            liveGiftNumPop = LiveGiftNumPop(context)
            liveGiftNumPop?.showAtLocationTop(tvGiftMount, 5f)
            liveGiftNumPop?.getUserDiamondSuccessListener {
                tvGiftMount.text = it
            }
        }
        //送礼物
        tvSvgaGiftSend.setOnClickListener {
            if (homeLiveGiftListBean == null) {
                ToastUtils.showToast("请选择礼物")
                return@setOnClickListener
            }
            showLading()
            RxBus.get().post(
                HomeLiveAnimatorBean(
                    homeLiveGiftListBean?.id!!,
                    homeLiveGiftListBean?.name!!,
                    homeLiveGiftListBean?.icon!!,
                    "" + UserInfoSp.getUserId(),
                    UserInfoSp.getUserPhoto().toString(),
                    UserInfoSp.getUserNickName().toString(),
                    tvGiftMount.text.toString()
                )
            )
        }
        tvGiftSend.setOnClickListener {
            if (homeLiveGiftListBean == null) {
                ToastUtils.showToast("请选择礼物")
                return@setOnClickListener
            }
            hideLoading()
            RxBus.get().post(
                HomeLiveAnimatorBean(
                    homeLiveGiftListBean?.id!!,
                    homeLiveGiftListBean?.name!!,
                    homeLiveGiftListBean?.icon!!,
                    "" + UserInfoSp.getUserId(),
                    UserInfoSp.getUserPhoto().toString(),
                    UserInfoSp.getUserNickName().toString(),
                    tvGiftMount.text.toString()
                )
            )
        }
    }

    fun setDiamond(dia: String) {
        tvDiamondTotal.text = dia
    }


    private fun initViews() {
        viewPager = findViewById(R.id.giftViewPager)
    }

    fun showLading() {
        loadingView.visibility = View.VISIBLE
    }

    fun hideLoading() {
        loadingView.visibility = View.GONE
    }

    fun setData(title: List<String>, content: List<List<HomeLiveGiftList>>) {
        if (!title.isNullOrEmpty() && !content.isNullOrEmpty()) {
            for ((index, tabText) in title.withIndex()) {
                chatGifTabView.addTab(chatGifTabView.newTab().setText(tabText))
                val view = BottomGiftPageGridView(context)
                view.setData(content[index])
                view.setOnItemClickListener { position, homeLiveGiftList ->
                    tvGiftMount.text = "1"
                    homeLiveGiftListBean = homeLiveGiftList
                    notifyAllData(homeLiveGiftList.name.toString(), homeLiveGiftList)
                }
                viewList.add(view)
            }
            pagerAdapter = BottomGiftAdapter(viewList)
            viewPager?.adapter = pagerAdapter
            viewPager?.offscreenPageLimit = 10
            viewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {}
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                }

                override fun onPageSelected(position: Int) {
                    if (chatGifTabView.getTabAt(position) != null) chatGifTabView.getTabAt(position)!!
                        .select()
                }

            })
            chatGifTabView.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabReselected(p0: TabLayout.Tab?) {}
                override fun onTabUnselected(p0: TabLayout.Tab?) {}
                override fun onTabSelected(p0: TabLayout.Tab?) {
                    viewPager?.currentItem = title.indexOf(p0?.text.toString())
                }

            })


            hideLoading()
        }
    }


    private fun notifyAllData(name: String, homeLiveGiftList: HomeLiveGiftList) {
        if (viewList.isNotEmpty()) {
            for (view in viewList) {
                view.notifyAllData(name)
            }
        }

        when (homeLiveGiftList.grade) {
            "middle", "high" -> {
                countLin.visibility = View.GONE
                tvSvgaGiftSend.visibility = View.VISIBLE
            }
            else -> {
                countLin.visibility = View.VISIBLE
                tvSvgaGiftSend.visibility = View.GONE
            }
        }

    }


    override fun onStart() {
        super.onStart()
        val mBehavior = delegate?.findViewById<View>(R.id.design_bottom_sheet)
            ?.let { BottomSheetBehavior.from(it) }
        mBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
    }

//    override fun show() {
//        NavigationBarUtil.focusNotAle(window)
//        super.show()
//        NavigationBarUtil.hideNavigationBar(window)
//        NavigationBarUtil.clearFocusNotAle(window)
//    }

    inner class BottomGiftAdapter(private val mViewList: List<View>?) : PagerAdapter() {

        override fun getItemPosition(`object`: Any): Int {
            return super.getItemPosition(`object`)
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(mViewList!![position])
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val view = mViewList!![position]
            container.addView(view)
            return view
        }

        override fun getCount(): Int {
            return mViewList?.size ?: 0
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }
    }

}