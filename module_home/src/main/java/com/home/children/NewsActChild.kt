package com.home.children

import android.graphics.Color
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.customer.ApiRouter
import com.glide.GlideUtil
import com.home.R
import com.customer.data.home.HomeApi
import com.customer.data.home.HomeNewsResponse
import com.lib.basiclib.base.fragment.BaseContentFragment
import com.lib.basiclib.base.recycle.BaseRecyclerAdapter
import com.lib.basiclib.base.recycle.RecyclerViewHolder
import com.lib.basiclib.base.round.RoundTextView
import com.lib.basiclib.utils.FastClickUtil
import com.lib.basiclib.utils.ToastUtils
import com.xiaojinzi.component.impl.Router
import kotlinx.android.synthetic.main.fragment_news_child.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/25
 * @ Describe
 *
 */
class NewsActChild : BaseContentFragment() {

    lateinit var adapter: NewsAdapter

    override fun getContentResID() = R.layout.fragment_news_child

    override fun isShowBackIconWhite() = false

    override fun isSwipeBackEnable() = false


    override fun initContentView() {
        smartRefreshLayoutNews.setEnableRefresh(false)//是否启用下拉刷新功能
        smartRefreshLayoutNews.setEnableLoadMore(false)//是否启用上拉加载功能
        smartRefreshLayoutNews.setEnableOverScrollBounce(true)//是否启用越界回弹
        smartRefreshLayoutNews.setEnableOverScrollDrag(true)//是否启用越界拖动（仿苹果效果）
    }

    override fun lazyInit() {
        getNewsList()
        adapter = NewsAdapter()
        rvNewsPublish.adapter = adapter
        rvNewsPublish.layoutManager =
            LinearLayoutManager(getPageActivity(), LinearLayoutManager.VERTICAL, false)
    }


    private fun getNewsList() {
        HomeApi.getNewsList(type = arguments?.getString("LIVE_ROOM_LOTTERY_TYPE", "1") ?: "1") {
            onSuccess {
                if (it.isNullOrEmpty()) setVisible(noData) else adapter.refresh(it)
                setGone(pageLoading)
            }
            onFailed {
                ToastUtils.showToast(it.getMsg().toString())
                setGone(pageLoading)
            }
        }
    }

    internal enum class ITEM_TYPE {
        HOLDER_1,
        HOLDER_2,
        HOLDER_3,
        HOLDER_4
    }


    inner class NewsAdapter : BaseRecyclerAdapter<HomeNewsResponse>() {


        override fun getItemViewType(position: Int): Int {
            return when (data[position].settype) {
                "1" -> ITEM_TYPE.HOLDER_1.ordinal
                "2" -> ITEM_TYPE.HOLDER_2.ordinal
                "3" -> ITEM_TYPE.HOLDER_3.ordinal
                else -> ITEM_TYPE.HOLDER_4.ordinal
            }
        }

        override fun getItemLayoutId(viewType: Int): Int {
            return when (viewType) {
                ITEM_TYPE.HOLDER_1.ordinal -> R.layout.adapter_public_news1
                ITEM_TYPE.HOLDER_2.ordinal -> R.layout.adapter_public_news2
                ITEM_TYPE.HOLDER_3.ordinal -> R.layout.adapter_public_news3
                else -> R.layout.adapter_public_news4
            }
        }

        override fun bindData(holder: RecyclerViewHolder, position: Int, data: HomeNewsResponse?) {
            when (getItemViewType(position)) {
                ITEM_TYPE.HOLDER_1.ordinal -> {
                    holder.text(R.id.tvTitle_h1, data?.title)
                    holder.text(R.id.tvTime_h1, data?.timegap)
                    if (!data?.cover_img.isNullOrEmpty()) context?.let {
                        GlideUtil.loadGrayscaleImage(
                            it, data?.cover_img!![0], holder.getImageView(R.id.contentImg_h1), 15
                        )
                    }
                    data?.type?.let {
                        getTagBg(
                            it,
                            holder.findViewById(R.id.tvTag_h1),
                            data.type_txt.toString()
                        )
                    }
                }
                ITEM_TYPE.HOLDER_2.ordinal -> {
                    holder.text(R.id.tvTitle_h2, data?.title)
                    holder.text(R.id.tvTime_h2, data?.timegap)
                    if (!data?.cover_img.isNullOrEmpty() && data?.cover_img?.size!! >= 2) {
                        context?.let {
                            GlideUtil.loadGrayscaleImage(
                                it,
                                data.cover_img!![0], holder.getImageView(R.id.img_1), 15
                            )
                        }
                        context?.let {
                            GlideUtil.loadGrayscaleImage(
                                it,
                                data.cover_img!![1], holder.getImageView(R.id.img_2), 15
                            )
                        }
                    } else if (data?.cover_img?.size == 1) {
                        context?.let {
                            GlideUtil.loadGrayscaleImage(
                                it,
                                data.cover_img!![0], holder.getImageView(R.id.img_1), 15
                            )
                        }
                    }
                    data?.type?.let {
                        getTagBg(
                            it,
                            holder.findViewById(R.id.tvTag_h2),
                            data.type_txt.toString()
                        )
                    }
                }
                ITEM_TYPE.HOLDER_3.ordinal -> {
                    holder.text(R.id.tvTitle_h3, data?.title)
                    holder.text(R.id.tvTime_h3, data?.timegap)
                    data?.type?.let {
                        getTagBg(
                            it,
                            holder.findViewById(R.id.tvTag_h3),
                            data.type_txt.toString()
                        )
                    }

                }
                ITEM_TYPE.HOLDER_4.ordinal -> {
                    holder.text(R.id.tvTitle_h4, data?.title)
                    holder.text(R.id.tvTime_h4, data?.timegap)
                    data?.type?.let {
                        getTagBg(
                            it,
                            holder.findViewById(R.id.tvTag_h4),
                            data.type_txt.toString()
                        )
                    }
                }
            }
            holder.itemView.setOnClickListener {
                if (!FastClickUtil.isFastClick()) Router.withApi(ApiRouter::class.java)
                    .toGlobalWeb("", true, data?.info_id ?: "")
            }
        }
    }

    private fun getTagBg(type: String, view: RoundTextView, text: String) {
        when (type) {
            "1" -> {
                view.delegate.backgroundColor = Color.parseColor("#EDE5F9")
                view.setTextColor(Color.parseColor("#D444F3"))
            }
            "2" -> {
                view.delegate.backgroundColor = Color.parseColor("#FFF9E8")
                view.setTextColor(Color.parseColor("#FD8208"))
            }
            "3" -> {
                view.delegate.backgroundColor = Color.parseColor("#F5F7FA")
                view.setTextColor(Color.parseColor("#79818C"))
            }
            "4" -> {
                view.delegate.backgroundColor = Color.parseColor("#FFECE8")
                view.setTextColor(Color.parseColor("#FF513E"))
            }
        }
        view.text = text
    }


    companion object {
        fun newInstance(type: String): NewsActChild {
            val fragment = NewsActChild()
            val bundle = Bundle()
            bundle.putString("LIVE_ROOM_LOTTERY_TYPE", type)
            fragment.arguments = bundle
            return fragment
        }
    }
}