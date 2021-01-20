package com.mine.children.message

import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.customer.ApiRouter
import com.customer.bean.image.ImageViewInfo
import com.customer.component.dialog.GlobalDialog
import com.customer.data.mine.MineApi
import com.customer.data.mine.MineMessageNew
import com.glide.GlideUtil
import com.lib.basiclib.base.fragment.BaseNavFragment
import com.lib.basiclib.base.recycle.BaseRecyclerAdapter
import com.lib.basiclib.base.recycle.RecyclerViewHolder
import com.lib.basiclib.base.round.RoundTextView
import com.lib.basiclib.utils.FastClickUtil
import com.lib.basiclib.utils.TimeUtils
import com.lib.basiclib.utils.ToastUtils
import com.lib.basiclib.utils.ViewUtils
import com.mine.R
import com.xiaojinzi.component.impl.Router
import kotlinx.android.synthetic.main.fragment_message.*

/**
 *
 * @ Author  QinTian
 * @ Date  11/20/20
 * @ Describe
 *
 */

class MineMessageActContent : BaseNavFragment() {

    var type = "0"

    var currentIndex = 1

    var adapter: AdapterMessage? = null

    override fun isSwipeBackEnable() = false

    override fun isShowToolBar() = false

    override fun getContentResID() = R.layout.fragment_message

    override fun initContentView() {
        messageRefresh?.setEnableRefresh(true)//是否启用下拉刷新功能
        messageRefresh?.setEnableLoadMore(true)//是否启用上拉加载功能
        messageRefresh?.setEnableOverScrollBounce(true)//是否启用越界回弹
        messageRefresh?.setEnableOverScrollDrag(true)//是否启用越界拖动（仿苹果效果）
        type = arguments?.getString("messageType") ?: "0"
        adapter = AdapterMessage()
        rvMessageNew?.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rvMessageNew?.adapter = adapter
        messageRefresh.setOnRefreshListener {
            currentIndex = 1
            getList()
        }
        messageRefresh.setOnLoadMoreListener {
            currentIndex++
            getList()
        }
    }


    override fun initData() {
        messageRefresh.autoRefresh()
    }

    private fun getList(){
        MineApi.getMessageList(type,currentIndex) {
            onSuccess {
                if (isAdded){
                    if (it.isNullOrEmpty()) {
                        if (currentIndex == 1) {
                            setVisible(holderText)
                            messageRefresh.finishRefresh()
                        } else {
                            messageRefresh.finishLoadMoreWithNoMoreData()
                        }
                    } else {
                        setGone(holderText)
                        if (currentIndex == 1)  {
                            adapter?.refresh(it)
                            messageRefresh.finishRefresh()
                        } else {
                            adapter?.loadMore(it)
                            messageRefresh.finishLoadMore()
                        }

                    }
                }
            }
            onFailed {
                if (isAdded) {
                    setVisible(holderText)
                }
            }
        }
    }

    inner class AdapterMessage : BaseRecyclerAdapter<MineMessageNew>() {
        override fun getItemLayoutId(viewType: Int) = R.layout.adapter_message_new

        override fun bindData(holder: RecyclerViewHolder, position: Int, data: MineMessageNew?) {
            holder.text(R.id.tv1, data?.title)
            holder.text(R.id.tv1_content, data?.content)
            holder.text(R.id.tv1_time,  data?.createtime_txt)
            val flag = holder.findViewById<RoundTextView>(R.id.tvMessageNum1)
            val img = holder.findViewById<ImageView>(R.id.img1)
            if (data?.readflag == 0) {
                ViewUtils.setVisible(flag)
            } else ViewUtils.setGone(flag)
            when (type) {
                "0" -> {
                    img.setBackgroundResource(R.mipmap.ic_message_gf)
                }
                "2" -> {
                    holder.text(R.id.tv1, data?.nickname)
                    holder.text(R.id.tv1_content, data?.content)
                    GlideUtil.loadCircleImage(requireContext(), data?.avatar, img)
                }
                "3" -> {
                    img.setBackgroundResource(R.mipmap.ic_message_me)
                }
                "5" -> {
                    img.setBackgroundResource(R.mipmap.ic_message_xt)
                }
            }
            holder.findViewById<TextView>(R.id.tvDelete).setOnClickListener {
                if (!FastClickUtil.isFastClick()) {
                    MineApi.getMessageInfo(data?.msg_id.toString(), true) {
                        onSuccess {
                            adapter?.delete(position)
                        }
                        onFailed {
                            GlobalDialog.showError(requireActivity(), it)
                        }
                    }
                } else ToastUtils.showToast("请勿频繁点击")
            }

            holder.findViewById<LinearLayout>(R.id.layoutMessage).setOnClickListener {
                if (!FastClickUtil.isFastClick()) {
                    when (type) {
                        "2" -> {
                            showPageLoadingDialog()
                            if (data?.apiType == "1") {
                                MineApi.getAnchorMoments(data.dynamic_id ?: "-1") {
                                    onSuccess {
                                        Router.withApi(ApiRouter::class.java).toMineAnchorInfo(
                                            id = it[0].dynamic_id ?: "0",
                                            userId = it[0].anchor_id ?: "",
                                            name = it[0].nickname ?: "",
                                            gender = it[0].sex ?: 1,
                                            avatar = it[0].avatar ?: "",
                                            time = it[0].create_time.toString(),
                                            content = it[0].text ?: "",
                                            commentNum = it[0].pls ?: "0",
                                            imgList = getPicture(it[0].media),
                                            liveState = it[0].live_status ?: "0"
                                        )
                                        hidePageLoadingDialog()
                                    }
                                    onFailed {
                                        ToastUtils.showToast("获取数据失败")
                                        hidePageLoadingDialog()
                                    }
                                }
                            } else if (data?.apiType == "2") {
                                MineApi.getHotDiscussSingle(data.dynamic_id ?: "-1") {
                                    onSuccess {
                                        Router.withApi(ApiRouter::class.java).toMineHotDiscussInfo(
                                            id = it.id,
                                            userId = it.user_id,
                                            name = it.nickname,
                                            gender = it.gender ?: 1,
                                            avatar = it.avatar,
                                            time = it.created.toString(),
                                            content = it.title,
                                            commentNum = it.comment_nums,
                                            imgList = getPicture(it.images)
                                        )
                                        hidePageLoadingDialog()
                                    }
                                    onFailed {
                                        ToastUtils.showToast("获取数据失败")
                                        hidePageLoadingDialog()
                                    }
                                }

                            }
                        }
                        else -> {
                            data?.readflag = 1
                            notifyItemChanged(position)
                            Router.withApi(ApiRouter::class.java)
                                .toGlobalWeb("", true, data?.msg_id ?: "-1")
                        }
                    }
                } else ToastUtils.showToast("请勿频繁点击")
            }
        }

    }

    private fun getPicture(url: MutableList<String>?): ArrayList<ImageViewInfo> {
        val list: ArrayList<ImageViewInfo> = ArrayList()
        if (url != null) {
            for (i in url) {
                list.add(ImageViewInfo(i))
            }
        }
        return list
    }

    companion object {
        fun newInstance(messageType: String): MineMessageActContent {
            val fragment = MineMessageActContent()
            val bundle = Bundle()
            bundle.putString("messageType", messageType)
            fragment.arguments = bundle
            return fragment
        }
    }

}