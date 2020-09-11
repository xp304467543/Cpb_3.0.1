package com.home.video.search

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import com.customer.component.dialog.DialogGlobalTips
import com.customer.data.UserInfoSp
import com.home.R
import com.home.video.adapter.VideoAdapter
import com.home.video.adapter.VideoSearchAdapter
import com.lib.basiclib.base.mvp.BaseMvpActivity
import com.lib.basiclib.base.xui.adapter.recyclerview.XGridLayoutManager
import com.lib.basiclib.base.xui.adapter.recyclerview.XLinearLayoutManager
import com.lib.basiclib.base.xui.widget.flowlayout.BaseTagAdapter
import com.lib.basiclib.utils.StatusBarUtils
import com.lib.basiclib.utils.ToastUtils
import com.lib.basiclib.utils.ViewUtils
import com.xiaojinzi.component.anno.RouterAnno
import kotlinx.android.synthetic.main.act_video_search.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/30
 * @ Describe
 *
 */
@RouterAnno(host = "Home", path = "videoSearch")
class VideoSearchAct : BaseMvpActivity<VideoSearchActPresenter>() {

    private var tagAdapter: SearchFlowTagAdapter? = null

    var adapter: VideoSearchAdapter? = null

    var adapterHot: VideoAdapter? = null

    override fun attachView() = mPresenter.attachView(this)

    override fun attachPresenter() = VideoSearchActPresenter()

    override fun isSwipeBackEnable() = true

    override fun isOverride() = true

    override val layoutResID = R.layout.act_video_search

    override fun initContentView() {
        StatusBarUtils.setStatusBarHeight(searchState)
        smartSearch.setEnableRefresh(false)//是否启用下拉刷新功能
        smartSearch.setEnableLoadMore(false)//是否启用上拉加载功能
        smartSearch.setEnableOverScrollBounce(true)//是否启用越界回弹
        smartSearch.setEnableOverScrollDrag(true)//是否启用越界拖动（仿苹果效果）
        initFlowLayout()
        rvVideoSearch.layoutManager = XLinearLayoutManager(this)
        rvVideoRecommend.layoutManager = XGridLayoutManager(this,3)
        adapter = VideoSearchAdapter(this)
        adapterHot = VideoAdapter(this)
        rvVideoSearch.adapter = adapter
        rvVideoRecommend.adapter = adapterHot
    }

    override fun initData() {
        mPresenter.getHot()
    }

    override fun initEvent() {
        videoSearchBack.setOnClickListener {
            finish()
        }
        tvSearch.setOnClickListener {
            if (TextUtils.isEmpty(editSearchVideo.text.toString().trim())) {
                ToastUtils.showToast("请输入搜索内容")
                return@setOnClickListener
            }
            saveSearch()
        }
        imgClear.setOnClickListener {
            val dialog = DialogGlobalTips(this, "温馨提示", "确定", "取消", "是否删除搜索记录")
            dialog.setConfirmClickListener {
                UserInfoSp.clearVideoSearChTag()
                tagAdapter?.clearData()
            }
            dialog.show()
        }
        editSearchVideo.addTextChangedListener(editListener)
        imgTextClear.setOnClickListener {
            editSearchVideo.setText("")
        }
    }

    private fun saveSearch() {
        val text = editSearchVideo.text.toString().trim()
        mPresenter.searchVideo(text)
        val data = tagAdapter?.items
        if (data != null) {
            when {
                data.size <= 0 -> {
                    tagAdapter?.addElement(text)
                    tagAdapter?.items?.toSet()?.let { UserInfoSp.putVideoSearChTag(it) }
                }
                data.size < 8 -> {
                    for ((index, result) in data.withIndex()) {
                        if (result == text) return else if (index == data.size - 1) {
                            tagAdapter?.addElement(text)
                            tagAdapter?.items?.toSet()?.let { UserInfoSp.putVideoSearChTag(it) }
                        }
                    }
                }
                data.size == 8 -> {
                    tagAdapter?.updateElement(text, 0)
                    tagAdapter?.items?.toSet()?.let { UserInfoSp.putVideoSearChTag(it) }
                }
            }
        }
    }

    private fun initFlowLayout() {
        tagAdapter = SearchFlowTagAdapter()
        searchFlowLayout.adapter = tagAdapter
        if (!UserInfoSp.getVideoSearChTag().isNullOrEmpty()) searchFlowLayout.addTags(UserInfoSp.getVideoSearChTag())
        searchFlowLayout.setOnTagClickListener { _, _, position ->
            editSearchVideo.setText( tagAdapter?.getItem(position))
            tagAdapter?.getItem(position)?.let { mPresenter.searchVideo(it) }
        }
    }

    inner class SearchFlowTagAdapter : BaseTagAdapter<String, TextView>(this) {
        override fun newViewHolder(convertView: View?): TextView {
            return (convertView!!.findViewById<View>(R.id.tv_tag) as TextView)
        }

        override fun getLayoutId() = R.layout.adapter_video_search_tag

        @SuppressLint("SetTextI18n")
        override fun convert(holder: TextView?, item: String?, position: Int) {
            holder?.text = "# $item #"
        }
    }

    private val editListener = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            if (TextUtils.isEmpty(s)) {
                ViewUtils.setGone(imgTextClear)
                ViewUtils.setGone(searchHolder)
                ViewUtils.setVisible(searchContainer)
                ViewUtils.setVisible(recommendContainer)
                ViewUtils.setGone(smartSearch)
            }else  ViewUtils.setVisible(imgTextClear)
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

    }
}