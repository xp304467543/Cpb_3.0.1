package com.personal

import android.annotation.SuppressLint
import androidx.viewpager.widget.ViewPager
import com.customer.ApiRouter
import com.customer.data.UserInfoSp
import com.customer.data.home.HomeApi
import com.customer.data.moments.AnchorPageInfoBean
import com.customer.data.moments.PersonalApi
import com.glide.GlideUtil
import com.google.android.material.tabs.TabLayout
import com.lib.basiclib.base.activity.BaseNavActivity
import com.lib.basiclib.base.adapter.BaseFragmentPageAdapter
import com.lib.basiclib.base.fragment.BaseFragment
import com.lib.basiclib.utils.FastClickUtil
import com.lib.basiclib.utils.ToastUtils
import com.lib.basiclib.utils.ViewUtils
import com.moment.R
import com.xiaojinzi.component.anno.RouterAnno
import com.customer.component.dialog.GlobalDialog
import com.customer.data.AnchorAttention
import com.hwangjr.rxbus.RxBus
import com.xiaojinzi.component.impl.Router
import cuntomer.constant.UserConstant
import kotlinx.android.synthetic.main.act_presonal_anchor.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/27
 * @ Describe
 *
 */
@RouterAnno(host = "Moment", path = "AnchorPersonalPage")
class AnchorPersonalPage : BaseNavActivity() {

    var attentionNum = 0

    override fun getContentResID() = R.layout.act_presonal_anchor

    override fun isShowToolBar() = false

    override fun isSwipeBackEnable() = true

    override fun initContentView() {
        anchorPageTab.addTab(anchorPageTab.newTab().setText("资料"))
        anchorPageTab.addTab(anchorPageTab.newTab().setText("动态"))
        imgBackAnchor.setOnClickListener { finish() }
    }

    override fun initData() {
        if (intent != null) {
            showPageLoadingDialog()
           getAnchorInfo(intent.getStringExtra(UserConstant.FOLLOW_ID)?:"0")
        }
    }

    private fun getAnchorInfo(anchor_id: String) {
        PersonalApi.getAnchorPage(anchor_id) {
                onSuccess { initAnchor(it) }
                onFailed { ToastUtils.showToast(it.getMsg().toString()) }
        }
    }
    @SuppressLint("SetTextI18n")
    private fun initAnchor(data: AnchorPageInfoBean) {
        initViewPager(data)
        attentionNum = data.fans
        tvUserName.text = data.nickname
        tvAnchorAttention.text = data.follow_num.toString()
        tvAnchorFans.text = data.fans.toString()
        tvAnchorZan.text = data.zan.toString()
        tvUserDescription.text = data.sign
        anchorId.text = "房间号: "+data.anchor_id
        if (data.sex == "1") {
            imgSex.background = ViewUtils.getDrawable(R.mipmap.ic_live_anchor_boy)
        } else imgSex.background = ViewUtils.getDrawable(R.mipmap.ic_live_anchor_girl)
        imgAge.text = data.age.toString()
        imgLevel.text = data.level
        GlideUtil.loadCircleImage(this,data.avatar,imgUserPhoto,true)
        if (data.liveStatus == "1") {
            circleWave.setInitialRadius(70f)
            circleWave.start()
        }
        if (data.isFollow) {
            btAttention.background = ViewUtils.getDrawable(R.drawable.button_grey_background)
            btAttention.setTextColor(ViewUtils.getColor(R.color.grey_e6))
            btAttention.text = "已关注"
        }
        hidePageLoadingDialog()
        linToLive.setOnClickListener {
            if (!FastClickUtil.isFastClick()){
                Router.withApi(ApiRouter::class.java).toLive(data.anchor_id,"1",data.nickname,data.liveStatus,"50","1",data.nickname,data.avatar)
            }
        }
    }

    private fun initViewPager(data: AnchorPageInfoBean) {
        val list = arrayListOf<BaseFragment>(AnchorPersonalPageData.newInstance(data), AnchorPersonalPageData2.newInstance(data.anchor_id))
        xViewPage.adapter = BaseFragmentPageAdapter(supportFragmentManager, list)

        anchorPageTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                xViewPage.currentItem = p0?.position!!
            }

        })
        xViewPage.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                anchorPageTab.getTabAt(position)!!.select()
            }

        })

    }

    override fun initEvent() {
        btAttention.setOnClickListener {
            if (!UserInfoSp.getIsLogin()) {
                GlobalDialog.notLogged(this, false)
                return@setOnClickListener
            }
            if (!FastClickUtil.isFastClick()) {
                HomeApi.attentionAnchorOrUser(intent.getStringExtra(UserConstant.FOLLOW_ID)?:"0", ""){
                    onSuccess {
                        if (!it.isFollow) {
                            btAttention.background = ViewUtils.getDrawable(R.mipmap.ic_anchor_bt_bg)
                            btAttention.text = "+ 关注"
                            btAttention.setTextColor(ViewUtils.getColor(R.color.white))
                            if (attentionNum > 0) attentionNum -= 1
                            tvAnchorFans.text = attentionNum.toString()
                        } else {
                            btAttention.background = ViewUtils.getDrawable(R.drawable.button_grey_background)
                            btAttention.setTextColor(ViewUtils.getColor(R.color.grey_e6))
                            btAttention.text = "已关注"
                            attentionNum += 1
                            tvAnchorFans.text = attentionNum.toString()
                        }
                        RxBus.get().post(AnchorAttention(intent.getStringExtra(UserConstant.FOLLOW_ID)?:"0",it.isFollow))
                    }
                    onFailed {
                        GlobalDialog.showError(this@AnchorPersonalPage, it)
                    }
                }
            }
        }
        imgBackAnchor.setOnClickListener {
            finish()
        }
    }

}