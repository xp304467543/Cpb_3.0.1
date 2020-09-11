package com.mine.children.skin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.customer.data.UserInfoSp
import com.customer.data.mine.ChangeSkin
import com.customer.data.mine.MineApi
import com.customer.data.mine.MineThemSkinInfoResponse
import com.glide.GlideUtil
import com.hwangjr.rxbus.RxBus
import com.lib.basiclib.base.activity.BaseNavActivity
import com.lib.basiclib.base.xui.widget.imageview.RadiusImageView
import com.lib.basiclib.utils.ToastUtils
import com.lib.basiclib.utils.ViewUtils
import com.mine.R
import kotlinx.android.synthetic.main.act_skiin.SkinLoadView
import kotlinx.android.synthetic.main.act_skin_info.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/9/3
 * @ Describe
 *
 */
class MineSkinInfoAct : BaseNavActivity() {

    override fun getContentResID() = R.layout.act_skin_info

    override fun isShowToolBar() = false



    override fun initContentView() {
        setVisible(SkinLoadView)
        if (UserInfoSp.getThemInt() == (intent?.getStringExtra("skinId")?:"1").toInt()) {
            btSkinUser.text = "正在使用"
            btSkinUser.background = ViewUtils.getDrawable(R.drawable.button_white_background)
            btSkinUser.setTextColor(ViewUtils.getColor(R.color.grey_e6))
        }
    }

    override fun initData() {
        getSkin(intent?.getStringExtra("skinId")?:"1")
    }


    private fun upDataBanner(data: MineThemSkinInfoResponse) {
        tvSkinName.text = data.name
        GlideUtil.loadImage(data.bg_image, imgSkinBg)
        viewPageSkin.adapter = data.images?.let { GalleryAdapter( it) }
        viewPageSkin.offscreenPageLimit = 3
        val index = (data.images?.size!! / 2)
        viewPageSkin.currentItem = index
        viewPageSkin.setPageTransformer(true,
            ZoomOutPageTransformer()
        )
        viewPageSkin.addOnPageChangeListener(PageIndicator(this, linDot, data.images?.size!!))
        setGone(SkinLoadView)
    }

    private fun getSkin(id: String) {
        MineApi.getThemSKinInfo(id = id) {
                onSuccess {
                   upDataBanner(it)
                }
                onFailed {
                    ToastUtils.showToast(it.getMsg().toString())
                }
        }
    }

    override fun initEvent() {
        imgSkinBack.setOnClickListener {
            finish()
        }
        btSkinUser.setOnClickListener {
            UserInfoSp.putThem( (intent?.getStringExtra("skinId")?:"1").toInt())
            btSkinUser.text = "正在使用"
            btSkinUser.background = ViewUtils.getDrawable(R.drawable.button_white_background)
            btSkinUser.setTextColor(ViewUtils.getColor(R.color.grey_e6))
            RxBus.get().post(ChangeSkin( (intent?.getStringExtra("skinId")?:"1").toInt()))
        }
    }


    inner class GalleryAdapter(val data: List<String>) : PagerAdapter() {

        override fun getCount(): Int {
            return data.size
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }


        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val view = LayoutInflater.from(this@MineSkinInfoAct).inflate(R.layout.gallery_item, null)
            val poster = view.findViewById<RadiusImageView>(R.id.bggggg)
            GlideUtil.loadImage(data[position], poster)
            container.addView(view)
            return view
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }
    }

}