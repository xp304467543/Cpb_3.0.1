package com.bet

import android.os.Bundle
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.customer.ApiRouter
import com.customer.base.BaseNormalFragment
import com.customer.data.LotteryResetDiamond
import com.customer.data.game.GameAll
import com.customer.data.game.GameAllChild1
import com.glide.GlideUtil
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.thread.EventThread
import com.lib.basiclib.base.recycle.BaseRecyclerAdapter
import com.lib.basiclib.base.recycle.RecyclerViewHolder
import com.lib.basiclib.base.xui.adapter.recyclerview.XLinearLayoutManager
import com.xiaojinzi.component.impl.Router
import kotlinx.android.synthetic.main.fragment_game_child.*


/**
 *
 * @ Author  QinTian
 * @ Date  2020/9/13
 * @ Describe
 *
 */
class GameMainChildFragment : BaseNormalFragment<GameMainChildFragmentPresenter>() {

    private var adapter0: Adapter0? = null

    private var adapter1: Adapter1? = null

    override fun isRegisterRxBus() = true

    override fun attachView() = mPresenter.attachView(this)

    override fun attachPresenter() = GameMainChildFragmentPresenter()

    override fun getLayoutRes() = R.layout.fragment_game_child

    override fun initData() {
        val data = arguments?.getParcelableArrayList<GameAll>("gameData")
        if (data.isNullOrEmpty()) return
        //最近使用
        adapter0 = Adapter0()
        rvGameUse.adapter = adapter0
        rvGameUse.layoutManager = XLinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        if (!data[0].list?.get(0)?.list.isNullOrEmpty()) {
            adapter0?.refresh(data[0].list?.get(0)?.list)
            setVisible(tvRecently)
            setVisible(lineView)
        }
        initRecommend(data)
    }

    fun initHot(data: ArrayList<GameAll>){
        if (!data[0].list?.get(0)?.list.isNullOrEmpty()) {
            adapter0?.refresh(data[0].list?.get(0)?.list)
            setVisible(tvRecently)
            setVisible(lineView)
        }
        initRecommend(data)
    }


    override fun initContentView() {
        smartRefreshLayoutGame.setEnableOverScrollBounce(true)//是否启用越界回弹
        smartRefreshLayoutGame.setEnableOverScrollDrag(true)//是否启用越界拖动（仿苹果效果）
        smartRefreshLayoutGame.setEnableRefresh(false)//是否启用下拉刷新功能
        smartRefreshLayoutGame.setEnableLoadMore(false)//是否启用上拉加载功能
    }


    //构造热门推荐数据
    private fun initRecommend(data: ArrayList<GameAll> ) {
        if (data[0].list.isNullOrEmpty()) return
        val result = data[0].list?.size?.let { data[0].list?.subList(1, it) }?.toMutableList()
        val listData = arrayListOf<GameAllChild1>()
        if (!result.isNullOrEmpty()) {
            for (x in result) {
                if (!x.list.isNullOrEmpty()) {
                    for ((num, bean) in x.list!!.withIndex()) {
                        if (num == 0) {
                            listData.add(GameAllChild1("", "", "", x.name, 1))
                        }
                        listData.add(bean)
                        if (num == (x.list?.size)?.minus(1) ?: -1) {
                            listData.add(GameAllChild1("", "", "", x.name, 2))
                        }
                    }
                }
            }
            adapter1 = Adapter1()
            rvGameType?.adapter = adapter1
            rvGameType?.layoutManager = object :StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL){
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
            adapter1?.refresh(listData)
        }
    }


    /**
     * 热门推荐
     * 最近使用
     */
    inner class Adapter0 : BaseRecyclerAdapter<GameAllChild1>() {
        override fun getItemLayoutId(viewType: Int) = R.layout.adapter_game_child_recently
        override fun bindData(holder: RecyclerViewHolder, position: Int, data: GameAllChild1?) {
            GlideUtil.loadImage(data?.img_url, holder.getImageView(R.id.imgRecentType))
            holder.text(R.id.tvGameRecentName, data?.name)
            holder.itemView.setOnClickListener {
                showPageLoadingDialog("加载中...")
                when(data?.type){
                    "lott" -> {
                        hidePageLoadingDialog()
                        Router.withApi(ApiRouter::class.java).toLotteryGame(data.id ?: "-1", data.name ?: "未知")
                    }
                    "fh_chess" -> mPresenter.getChessGame(data.id.toString())
                    "ag_live" -> mPresenter.getAg()
                    "ag_slot" -> mPresenter.getAgDz()
                }
            }
        }
    }

    /**
     * 热门推荐
     * 游戏推荐
     */
    inner class Adapter1 : BaseRecyclerAdapter<GameAllChild1>() {

        val HEADER = 1
        val CONTENT = 2
        val FOOTER = 3

        override fun getItemLayoutId(viewType: Int): Int {
            return when (viewType) {
                HEADER -> R.layout.adapter_game_child
                FOOTER -> R.layout.adapter_game_child_footer
                else -> R.layout.adapter_game_child_0
            }
        }

        override fun getItemViewType(position: Int): Int {
            return when (data[position].itemType) {
                1 -> HEADER
                2 -> FOOTER
                else -> CONTENT
            }
        }


        override fun bindData(holder: RecyclerViewHolder, position: Int, data: GameAllChild1?) {
            when {
                getItemViewType(position) == HEADER -> {
                    val layoutParams = StaggeredGridLayoutManager.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    layoutParams.topMargin = 30
                    layoutParams.bottomMargin = 30
                    layoutParams.isFullSpan = true
                    holder.itemView.layoutParams = layoutParams
                    holder.text(R.id.tvTitleGame, data?.name)
                }
                getItemViewType(position) == FOOTER -> {
                    val layoutParams = StaggeredGridLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 50)
                    layoutParams.topMargin = 40
                    layoutParams.isFullSpan = true
                    holder.itemView.layoutParams = layoutParams
                }
                else -> {
                    holder.text(R.id.tvGameName, data?.name)
                    GlideUtil.loadImage(data?.img_url, holder.getImageView(R.id.imgGameType))
                    holder.itemView.setOnClickListener {
                        showPageLoadingDialog("加载中...")
                        when(data?.type){
                            "lott" -> {
                                hidePageLoadingDialog()
                                Router.withApi(ApiRouter::class.java).toLotteryGame(data.id ?: "-1", data.name ?: "未知")
                            }
                            "fh_chess" -> mPresenter.getChessGame(data.id.toString())
                            "ag_live" -> mPresenter.getAg()
                            "ag_slot" -> mPresenter.getAgDz()
                        }
                    }
                }
            }
        }
    }


    //余额更新
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun lotteryBet(eventBean: LotteryResetDiamond) {
        mPresenter.getAllGame()
    }

    companion object {
        fun newInstance(index: Int, data: ArrayList<GameAll>): GameMainChildFragment {
            val fragment = GameMainChildFragment()
            val bundle = Bundle()
            bundle.putInt("indexGame", index)
            bundle.putParcelableArrayList("gameData", data)
            fragment.arguments = bundle
            return fragment
        }
    }
}