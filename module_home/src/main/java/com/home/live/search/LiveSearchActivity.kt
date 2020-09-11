package com.home.live.search

import android.text.TextUtils
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.customer.ApiRouter
import com.customer.data.home.HomeAnchorRecommend
import com.customer.data.home.HomeAnchorSearch
import com.customer.data.home.HomeApi
import com.home.R
import com.home.adapter.HomeHotLiveAdapter
import com.lib.basiclib.base.activity.BaseNavActivity
import com.lib.basiclib.base.round.RoundTextView
import com.lib.basiclib.utils.FastClickUtil
import com.lib.basiclib.utils.ToastUtils
import com.lib.basiclib.utils.ViewUtils
import com.xiaojinzi.component.impl.Router
import kotlinx.android.synthetic.main.act_live_search.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/9/2
 * @ Describe
 *
 */
class LiveSearchActivity : BaseNavActivity() {

    private lateinit var resultAdapter: HomeHotLiveAdapter

    private lateinit var relatedAdapter: HomeHotLiveAdapter

    override fun getContentResID() = R.layout.act_live_search

    override fun isShowToolBar() = false


    override fun initContentView() {
        //查询结果
        resultAdapter = HomeHotLiveAdapter()
        rvResult.adapter = resultAdapter
        rvResult.layoutManager = GridLayoutManager(this, 2)

        //查询推荐
        relatedAdapter = HomeHotLiveAdapter()
        rvRelated.adapter = relatedAdapter
        rvRelated.layoutManager = GridLayoutManager(this, 2)
    }

    override fun initData() {
        getAnchorPop()
    }


    override fun initEvent() {
        imgBack.setOnClickListener {
            finish()
        }

        tvSearch.setOnClickListener {
            val text = etSearch.text.toString()
            if (!TextUtils.isEmpty(text)) {
                setGone(initRecommend)
                search(text)
            } else ToastUtils.showToast("请输入内容")

        }
    }

    private fun getAnchorPop() {
        HomeApi.getPopAnchor {
            onSuccess {
                    initAnchorPop(it)
            }
        }
    }

    //主播推荐
    private fun initAnchorPop(data: List<HomeAnchorRecommend>) {
        if (!data.isNullOrEmpty()){
            //往容器内添加TextView数据
            val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            layoutParams.setMargins(15, 20, 15, 5)
            if (flowLayout != null) {
                flowLayout.removeAllViews()
            }
            for (i in data) {
                val tv = RoundTextView(this)
                tv.setPadding(28, 10, 28, 10)
                tv.text = i.nickname
                tv.maxEms = 10
                tv.setSingleLine()
                tv.textSize = 12f
                tv.setTextColor(ViewUtils.getColor(R.color.color_333333))
                tv.delegate.cornerRadius = 15
                tv.delegate.backgroundColor = ViewUtils.getColor(R.color.color_F5F7FA)
                tv.layoutParams = layoutParams
                tv.setOnClickListener {
                    if (!FastClickUtil.isFastClick()) {
                        Router.withApi(ApiRouter::class.java).toLive(i.id?:"1","",
                                i.nickname?:"未知","-1","-1",
                                "-1","-1","-1")

                    }
                }
                flowLayout.addView(tv, layoutParams)
            }
        }
    }


    private fun search(search_content: String) {
        showPageLoadingDialog()
        HomeApi.getSearchAnchor(search_content) {
            onSuccess {
                hidePageLoadingDialog()
               upDateView(it)
            }
            onFailed {
                ToastUtils.showToast(it.getMsg().toString())
                hidePageLoadingDialog()
            }
        }
    }


    //搜索结果
    private fun upDateView(data: HomeAnchorSearch) {
        setVisible(resultSearch)
        if (!data.result.isNullOrEmpty()) {
            setGone(tvNoResult)
            setVisible(searchResult)
            rvResult.removeAllViews()
            resultAdapter.clear()
            resultAdapter.notifyDataSetChanged()
            resultAdapter.refresh(data.result)

        }else{
            setVisible(tvNoResult)
            setGone(searchResult)
        }
        if (!data.related.isNullOrEmpty()) {
            rvRelated.removeAllViews()
            relatedAdapter.clear()
            relatedAdapter.notifyDataSetChanged()
            relatedAdapter.refresh(data.related)
        }
    }
}