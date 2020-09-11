package com.mine.children

import androidx.recyclerview.widget.LinearLayoutManager
import com.customer.ApiRouter
import com.glide.GlideUtil
import com.lib.basiclib.base.activity.BaseNavActivity
import com.lib.basiclib.base.recycle.BaseRecyclerAdapter
import com.lib.basiclib.base.recycle.RecyclerViewHolder
import com.lib.basiclib.utils.FastClickUtil
import com.lib.basiclib.utils.ToastUtils
import com.mine.R
import com.customer.data.mine.MineApi
import com.customer.data.mine.MineGroup
import com.xiaojinzi.component.anno.RouterAnno
import com.xiaojinzi.component.impl.Router
import kotlinx.android.synthetic.main.act_content_group.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/23
 * @ Describe
 *
 */
@RouterAnno(host = "Mine", path = "contentGroup")
class MineContentGroupAct : BaseNavActivity() {

    override fun getContentResID() = R.layout.act_content_group

    override fun getPageTitle() = "官方交流群"

    override fun isSwipeBackEnable() = true

    override fun isShowBackIconWhite() = false

    private lateinit var groupAdapter: GroupAdapter

    override fun initContentView() {
        groupAdapter = GroupAdapter()
        rvGroup.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL, false
        )
        rvGroup.adapter = groupAdapter
    }

    override fun initData() {
        MineApi.getContentGroup {
            onSuccess {
                groupAdapter.refresh(it)
                groupAdapter.setOnItemClickListener { _, item, _ ->
                    if (!FastClickUtil.isFastClick()) {
                        Router.withApi(ApiRouter::class.java).toGlobalWeb(item.url)
                    }
                }
            }
            onFailed {
                ToastUtils.showToast("获取失败")
            }
        }
    }


    inner class GroupAdapter : BaseRecyclerAdapter<MineGroup>() {
        override fun getItemLayoutId(viewType: Int) = R.layout.adapter_group_item
        override fun bindData(holder: RecyclerViewHolder, position: Int, data: MineGroup?) {
            holder.text(R.id.tvGroupName, data?.title)
            GlideUtil.loadCircleImage(
                this@MineContentGroupAct,
                data?.icon,
                holder.getImageView(R.id.imgIcon)
            )
        }

    }
}