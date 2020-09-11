package com.mine.children.skin

import android.content.Intent
import androidx.recyclerview.widget.GridLayoutManager
import com.customer.data.UserInfoSp
import com.customer.data.mine.ChangeSkin
import com.customer.data.mine.MineApi
import com.customer.data.mine.MineThemSkinResponse
import com.glide.GlideUtil
import com.hwangjr.rxbus.RxBus
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.thread.EventThread
import com.lib.basiclib.base.activity.BaseNavActivity
import com.lib.basiclib.base.recycle.BaseRecyclerAdapter
import com.lib.basiclib.base.recycle.RecyclerViewHolder
import com.lib.basiclib.utils.ToastUtils
import com.lib.basiclib.utils.ViewUtils
import com.mine.R
import kotlinx.android.synthetic.main.act_skiin.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/9/3
 * @ Describe
 *
 */
class MineSkinAct : BaseNavActivity() {

    private var skinAdapter: MineFragmentThemSkinAdapter? = null

    override fun getContentResID() = R.layout.act_skiin

    override fun isSwipeBackEnable() = true

    override fun getPageTitle() = getString(R.string.mine_them_skin)

    override fun isRegisterRxBus() = true

    override fun isShowBackIconWhite() = false

    override fun initContentView() {
        setVisible(SkinLoadView)
        skinAdapter = MineFragmentThemSkinAdapter()
        rvThemSkin.layoutManager = GridLayoutManager(this, 2)
        rvThemSkin.adapter = skinAdapter
        if (rvThemSkin.itemDecorationCount == 0) {
            rvThemSkin.addItemDecoration(GridItemSpaceDecoration(2, topAndBottomSpace = ViewUtils.dp2px(0), startAndEndSpace = ViewUtils.dp2px(10), itemSpace = ViewUtils.dp2px(15)))
        }
    }

    override fun initData() {
        MineApi.getThemSKin {
            onSuccess {
                skinAdapter?.refresh(it)
                setGone(SkinLoadView)
            }
            onFailed {
                ToastUtils.showToast(it.getMsg().toString())
            }
        }
    }

    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun changeSkin(eventBean: ChangeSkin) {
        skinAdapter?.notifyDataSetChanged()

    }

   inner class MineFragmentThemSkinAdapter : BaseRecyclerAdapter<MineThemSkinResponse>() {

       override fun getItemLayoutId(viewType: Int) = R.layout.adapter_mine_them_skin

       override fun bindData(
           holder: RecyclerViewHolder,
           position: Int,
           data: MineThemSkinResponse?
       ) {
           GlideUtil.loadImage(data?.cover, holder.getImageView(R.id.imgSkin))
           holder.text(R.id.tvSkinName, data?.name)
           holder.text(R.id.tvSkinContentDescription, data?.users + " 人使用")
           if (UserInfoSp.getThemInt() == data?.id?.toInt()) {
               setVisible(holder.findView(R.id.linSelect))
           } else setGone(holder.findView(R.id.linSelect))

           holder.itemView.setOnClickListener {
               if (data?.id != "1") {
                   val intent = Intent(this@MineSkinAct, MineSkinInfoAct::class.java)
                   intent.putExtra("skinId",data?.id)
                   startActivity(intent)
               } else if ( UserInfoSp.getThemInt() != 1){
                   UserInfoSp.putThem(data.id?.toInt()?:1)
                   RxBus.get().post(ChangeSkin(data.id?.toInt()?:1))
                   notifyDataSetChanged()
               }
               data?.isSelect = !data?.isSelect!!
           }
       }

   }
}