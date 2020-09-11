package com.moment.children

import com.lib.basiclib.base.mvp.BaseMvpPresenter
import com.lib.basiclib.utils.ToastUtils
import com.customer.data.moments.MomentsApi
import kotlinx.android.synthetic.main.fragment_moments_hot_discuss.*
import java.util.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/19
 * @ Describe
 *
 */
class MomentsHotDiscussPresenter : BaseMvpPresenter<MomentsHotDiscussFragment>() {


    //热门讨论
    fun getHotDiscuss(isRefresh: Boolean) {
        MomentsApi.getHotDiscuss("10", mView.page) {
            if (mView.isActive()) {
                onSuccess {
                    mView.setGone(mView.PlaceHolder)
                    if (it.isNotEmpty()) mView.initNineView(it, isRefresh) else {
                        if (!isRefresh) mView.smartRefreshLayoutHotDiscuss.finishLoadMoreWithNoMoreData()
                    }
                    if (mView.smartRefreshLayoutHotDiscuss != null) {
                        mView.smartRefreshLayoutHotDiscuss.finishLoadMore()
                        mView.smartRefreshLayoutHotDiscuss.finishRefresh()
                    }
                }
                onFailed {
                    mView.setGone(mView.PlaceHolder)
                    if (mView.smartRefreshLayoutHotDiscuss != null) {
                        mView.smartRefreshLayoutHotDiscuss.finishLoadMore()
                        mView.smartRefreshLayoutHotDiscuss.finishRefresh()
                    }
                    if (mView.page != 1) mView.page--
                    ToastUtils.showToast("数据获取失败")
                }
            }
        }
    }


    /**
     * 拆分集合
     *
     * @param <T>
     * @param resList 要拆分的集合
     * @param count   每个集合的元素个数
     * @return 返回拆分后的各个集合
    </T> */
    fun <T> split(
        resList: List<T>?,
        count: Int
    ): List<List<T>>? {
        if (resList == null || count < 1) {
            return null
        }
        val ret: MutableList<List<T>> =
            ArrayList()
        val size = resList.size
        if (size <= count) { //数据量不足count指定的大小
            ret.add(resList)
        } else {
            val pre = size / count
            val last = size % count
            //前面pre个集合，每个大小都是count个元素
            for (i in 0 until pre) {
                val itemList: MutableList<T> = ArrayList()
                for (j in 0 until count) {
                    itemList.add(resList[i * count + j])
                }
                ret.add(itemList)
            }
            //last的进行处理
            if (last > 0) {
                val itemList: MutableList<T> = ArrayList()
                for (i in 0 until last) {
                    itemList.add(resList[pre * count + i])
                }
                ret.add(itemList)
            }
        }
        return ret
    }
}