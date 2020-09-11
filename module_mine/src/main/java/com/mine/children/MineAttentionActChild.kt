package com.mine.children

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.customer.ApiRouter
import com.customer.component.WaveView
import com.customer.data.UpDatePreView
import com.glide.GlideUtil
import com.hwangjr.rxbus.RxBus
import com.lib.basiclib.base.fragment.BaseContentFragment
import com.lib.basiclib.base.recycle.BaseRecyclerAdapter
import com.lib.basiclib.base.recycle.RecyclerViewHolder
import com.lib.basiclib.utils.FastClickUtil
import com.lib.basiclib.utils.ToastUtils
import com.lib.basiclib.utils.ViewUtils
import com.mine.R
import com.customer.data.mine.MineApi
import com.customer.data.mine.MineExpertBean
import com.customer.data.mine.MineUserAttentionBean
import com.customer.component.dialog.GlobalDialog
import com.customer.data.mine.UpDatePre
import com.xiaojinzi.component.impl.Router
import kotlinx.android.synthetic.main.fragment_attention_child.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/25
 * @ Describe
 *
 */
class MineAttentionActChild : BaseContentFragment() {

    var page = 1

    var isChange:Boolean = false

    lateinit var anchorAdapter: AnchorAdapter

    lateinit var userAdapter: UserAdapter

    lateinit var expertAdapter: ExpertAdapter

    override fun getContentResID() = R.layout.fragment_attention_child

    override fun isSwipeBackEnable() = false

    override fun initContentView() {
        attentionSmartRefreshLayout.setEnableRefresh(true)//是否启用下拉刷新功能
        attentionSmartRefreshLayout.setEnableLoadMore(false)//是否启用上拉加载功能
        attentionSmartRefreshLayout.setEnableOverScrollBounce(true)//是否启用越界回弹
        attentionSmartRefreshLayout.setEnableOverScrollDrag(true)//是否启用越界拖动（仿苹果效果）
        rvAttention.layoutManager =
            LinearLayoutManager(getPageActivity(), LinearLayoutManager.VERTICAL, false)
        when (arguments?.getInt("AttentionFragmentChildType")) {
            1 -> {
                anchorAdapter = AnchorAdapter()
                rvAttention.adapter = anchorAdapter
            }
            2 -> {
                userAdapter = UserAdapter()
                rvAttention.adapter = userAdapter
            }
            else -> {
                expertAdapter = ExpertAdapter()
                rvAttention.adapter = expertAdapter
            }
        }
        attentionSmartRefreshLayout.setOnRefreshListener {
            page = 1
            getData()
        }
        attentionSmartRefreshLayout.setOnLoadMoreListener {
            page++
            getData()
        }
    }

    override fun lazyInit() {
//        attentionSmartRefreshLayout.autoRefresh()
        getData()
    }

    private fun getData() {
        when (arguments?.getInt("AttentionFragmentChildType")) {
            1 -> MineApi.getAttentionList("0", page) {
                onSuccess {
                    if (!it.isNullOrEmpty()) {
                        if (page == 1) anchorAdapter.refresh(it) else anchorAdapter.loadMore(it)
                        attentionSmartRefreshLayout.setEnableLoadMore(false)
                    } else {
                        if (page == 1) {
                            setVisible(tvHolder)
                            tvHolder.text = "您还没有关注任何主播哦~"
                        } else attentionSmartRefreshLayout.finishLoadMoreWithNoMoreData()

                    }
                    attentionSmartRefreshLayout.finishLoadMore()
                    attentionSmartRefreshLayout.finishRefresh()
                }
                onFailed {
                    ToastUtils.showToast(it.getMsg())
                    attentionSmartRefreshLayout.finishLoadMore()
                    attentionSmartRefreshLayout.finishRefresh()
                }
            }
            2 -> MineApi.getAttentionList("1", page) {
                onSuccess {
                    if (!it.isNullOrEmpty()) {
                        if (page == 1) userAdapter.refresh(it) else userAdapter.loadMore(it)
                         attentionSmartRefreshLayout.setEnableLoadMore(false)
                    } else {
                        if (page == 1) {
                            setVisible(tvHolder)
                            tvHolder.text = "您还没有关注任何用户哦~"
                        } else attentionSmartRefreshLayout.finishLoadMoreWithNoMoreData()
                    }
                    attentionSmartRefreshLayout.finishLoadMore()
                    attentionSmartRefreshLayout.finishRefresh()
                }
                onFailed {
                    ToastUtils.showToast(it.getMsg())
                    attentionSmartRefreshLayout.finishLoadMore()
                    attentionSmartRefreshLayout.finishRefresh()
                }
            }
            else -> MineApi.getAttentionList(page) {
                onSuccess {
                    if (!it.isNullOrEmpty()) {
                        if (page == 1) expertAdapter.refresh(it) else expertAdapter.loadMore(it)
                        if (it.size > 9) attentionSmartRefreshLayout.setEnableLoadMore(true)
                    } else {
                        if (page == 1) {
                            setVisible(tvHolder)
                            tvHolder.text = "您还没有关注任何专家哦~"
                        } else attentionSmartRefreshLayout.finishLoadMoreWithNoMoreData()

                    }
                    attentionSmartRefreshLayout.finishLoadMore()
                    attentionSmartRefreshLayout.finishRefresh()
                }
                onFailed {
                    ToastUtils.showToast(it.getMsg())
                    attentionSmartRefreshLayout.finishLoadMore()
                    attentionSmartRefreshLayout.finishRefresh()
                }
            }

        }
    }


    inner class AnchorAdapter : BaseRecyclerAdapter<MineUserAttentionBean>() {

        override fun getItemLayoutId(viewType: Int) = R.layout.adapter_mine_attention

        override fun bindData(
            holder: RecyclerViewHolder,
            position: Int,
            data: MineUserAttentionBean?
        ) {
            ViewUtils.setVisible(holder.findViewById(R.id.tvEndAttention))
            if (data?.live_status == "1") {
                holder.findViewById<WaveView>(R.id.circleWave).setInitialRadius(60f)
                holder.findViewById<WaveView>(R.id.circleWave).start()
            }
            context?.let {
                GlideUtil.loadCircleImage(
                    it,
                    data?.avatar,
                    holder.getImageView(R.id.imgPhoto),
                    true
                )
            }
            holder.text(R.id.tvName, data?.nickname)
            holder.text(R.id.tvSing, data?.sign)

            holder.click(R.id.btnDelete) {
                if (!FastClickUtil.isFastClick()) {
                    attention(data?.anchor_id ?: "", "", position)
                }
            }
            holder.click(R.id.imgPhoto) {
                //个人主页
                if (!FastClickUtil.isFastClick()) Router.withApi(ApiRouter::class.java).toAnchorPage(data?.anchor_id.toString())
            }
            holder.click(R.id.tvEndAttention){
                if (!FastClickUtil.isFastClick()){
                    Router.withApi(ApiRouter::class.java).toLive(
                        data?.anchor_id ?: "-1",
                        data?.lottery_id ?: "1",
                        data?.nickname ?: "",
                        data?.live_status ?: "0",
                        "50",
                        "-1",
                        data?.nickname ?: "",
                        data?.avatar ?: ""
                    )
                }
            }
        }
    }

    /**
     * 用户 adapter
     */
    inner class UserAdapter : BaseRecyclerAdapter<MineUserAttentionBean>() {
        override fun getItemLayoutId(viewType: Int) = R.layout.adapter_mine_attention
        override fun bindData(
            holder: RecyclerViewHolder,
            position: Int,
            data: MineUserAttentionBean?
        ) {
            context?.let {
                GlideUtil.loadCircleImage(
                    it,
                    data?.avatar,
                    holder.getImageView(R.id.imgPhoto),
                    true
                )
            }
            holder.text(R.id.tvName, data?.nickname)
            holder.text(R.id.tvSing, data?.sign)
            holder.click(R.id.btnDelete) {
                if (!FastClickUtil.isFastClick()) {
                    attention("", data?.user_id ?: "", position)
                }
            }
            holder.click(R.id.imgPhoto) {
                //个人主页
                if (!FastClickUtil.isFastClick()) Router.withApi(ApiRouter::class.java).toUserPage(data?.user_id.toString())
            }
        }
    }

    /**
     * 专家 adapter
     */
    inner class ExpertAdapter : BaseRecyclerAdapter<MineExpertBean>() {
        override fun getItemLayoutId(viewType: Int) = R.layout.adapter_mine_attention
        override fun bindData(holder: RecyclerViewHolder, position: Int, data: MineExpertBean?) {
            context?.let {
                GlideUtil.loadCircleImage(
                    it,
                    data?.avatar,
                    holder.getImageView(R.id.imgPhoto),
                    true
                )
            }
            holder.text(R.id.tvName, data?.nickname)
            holder.text(R.id.tvSing, data?.profile)
            holder.click(R.id.btnDelete) {
                if (!FastClickUtil.isFastClick()) {
                    attentionExpert(data?.expert_id ?: "", position)
                }
            }
            holder.click(R.id.imgPhoto) {
                //个人主页
                if (!FastClickUtil.isFastClick()) Router.withApi(ApiRouter::class.java).toUserPage(data?.expert_id.toString())
            }
        }
    }

    fun attention(anchor_id: String, follow_id: String, position: Int) {
        showPageLoadingDialog()
        MineApi.attentionAnchorOrUser(anchor_id = anchor_id, follow_id = follow_id) {
            onSuccess {
                when (arguments?.getInt("AttentionFragmentChildType")) {
                    1 -> {
                        anchorAdapter.delete(position)
                        isChange = true
                    }

                    2 -> userAdapter.delete(position)

                }
                RxBus.get().post(UpDatePreView(true))
                hidePageLoadingDialog()
            }
            onFailed {
                hidePageLoadingDialog()
                GlobalDialog.showError(requireActivity(), it)
            }
        }
    }

    fun attentionExpert(expert_id: String, position: Int) {
        MineApi.attentionExpert(expert_id) {
            onSuccess {
                expertAdapter.delete(position)
            }
            onFailed { GlobalDialog.showError(requireActivity(), it) }
        }
    }

    override fun onDestroy() {
        if (isChange) RxBus.get().post(UpDatePre(true))
        super.onDestroy()
    }


    companion object {
        fun newInstance(type: Int): MineAttentionActChild {
            val fragment = MineAttentionActChild()
            val bundle = Bundle()
            bundle.putInt("AttentionFragmentChildType", type)
            fragment.arguments = bundle
            return fragment
        }
    }
}