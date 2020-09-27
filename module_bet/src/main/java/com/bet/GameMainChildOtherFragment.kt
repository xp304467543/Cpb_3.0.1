package com.bet

import android.os.Bundle
import com.customer.ApiRouter
import com.customer.base.BaseNormalFragment
import com.customer.component.dialog.GlobalDialog
import com.customer.data.UserInfoSp
import com.customer.data.game.GameAll
import com.customer.data.game.GameAllChild0
import com.glide.GlideUtil
import com.lib.basiclib.base.recycle.BaseRecyclerAdapter
import com.lib.basiclib.base.recycle.RecyclerViewHolder
import com.lib.basiclib.base.xui.adapter.recyclerview.XGridLayoutManager
import com.lib.basiclib.utils.FastClickUtil
import com.xiaojinzi.component.impl.Router
import kotlinx.android.synthetic.main.adapter_game_child_other.*
import kotlinx.android.synthetic.main.fragment_game_child.*


/**
 *
 * @ Author  QinTian
 * @ Date  2020/9/13
 * @ Describe
 *
 */
class GameMainChildOtherFragment : BaseNormalFragment<GameMainChildOtherFragmentPresenter>() {

    var index = -1

    private var adapter0: Adapter? = null

    override fun attachView() = mPresenter.attachView(this)

    override fun attachPresenter() = GameMainChildOtherFragmentPresenter()

    override fun getLayoutRes() = R.layout.adapter_game_child_other

    override fun initData() {
        val data = arguments?.getParcelableArrayList<GameAll>("gameData")
        index = arguments?.getInt("indexGame") ?: 0
        if (data?.get(index)?.list.isNullOrEmpty()) return
        val result = data?.get(index)?.list
        //最近使用
        adapter0 = Adapter()
        rvGame.adapter = adapter0
        rvGame.layoutManager = XGridLayoutManager(context, 3)
        adapter0?.refresh(result)
        setVisible(tvRecently)
        setVisible(lineView)
    }


    override fun initContentView() {
        smartRefreshLayoutGameOther.setEnableOverScrollBounce(true)//是否启用越界回弹
        smartRefreshLayoutGameOther.setEnableOverScrollDrag(true)//是否启用越界拖动（仿苹果效果）
        smartRefreshLayoutGameOther.setEnableRefresh(false)//是否启用下拉刷新功能
        smartRefreshLayoutGameOther.setEnableLoadMore(false)//是否启用上拉加载功能
    }


    inner class Adapter : BaseRecyclerAdapter<GameAllChild0>() {

        override fun getItemLayoutId(viewType: Int) = R.layout.adapter_game_child_0

        override fun bindData(holder: RecyclerViewHolder, position: Int, data: GameAllChild0?) {
            holder.text(R.id.tvGameName, data?.name)
            GlideUtil.loadImage(data?.img_url, holder.getImageView(R.id.imgGameType))
            holder.itemView.setOnClickListener {
                if (!FastClickUtil.isFastClick()) {
                    if (!UserInfoSp.getIsLogin()) {
                        GlobalDialog.notLogged(requireActivity())
                        return@setOnClickListener
                    }
                    showPageLoadingDialog("加载中...")
                    when (index) {
                        1 -> {
                            hidePageLoadingDialog()
                            Router.withApi(ApiRouter::class.java)
                                .toLotteryGame(data?.id ?: "-1", data?.name ?: "未知")
                        }
                        2 -> mPresenter.getChessGame(data?.id.toString())

                        3 -> mPresenter.getAg()

                        4 ->  mPresenter.getAgDz()
                    }
                }
            }
        }

    }


    companion object {
        fun newInstance(index: Int, data: ArrayList<GameAll>): GameMainChildOtherFragment {
            val fragment = GameMainChildOtherFragment()
            val bundle = Bundle()
            bundle.putInt("indexGame", index)
            bundle.putParcelableArrayList("gameData", data)
            fragment.arguments = bundle
            return fragment
        }
    }
}