package com.customer.component

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.customer.data.video.MovieType
import com.customer.data.video.MovieTypeChild
import com.fh.module_base_resouce.R
import com.lib.basiclib.base.recycle.BaseRecyclerAdapter
import com.lib.basiclib.base.recycle.RecyclerViewHolder
import com.lib.basiclib.base.xui.adapter.recyclerview.XLinearLayoutManager
import com.lib.basiclib.base.xui.widget.flowlayout.BaseTagAdapter
import com.lib.basiclib.base.xui.widget.flowlayout.FlowTagLayout
import com.lib.basiclib.widget.popup.BasePopupWindow

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/30
 * @ Describe
 *
 */
class PopWindowVideo (context: Context, private val dataList:List<MovieType>,private val select:MovieTypeChild?) : BasePopupWindow(context, R.layout.pop_video_type) {

    var rvVideoWindow: RecyclerView

    init {
        width = ViewGroup.LayoutParams.MATCH_PARENT
        height = ViewGroup.LayoutParams.WRAP_CONTENT
        rvVideoWindow = findView(R.id.rvVideoWindow)
        initRecycle()
    }


    private fun initRecycle(){
        val adapter = VideoWindowAdapter()
        rvVideoWindow.adapter = adapter
        rvVideoWindow.layoutManager = XLinearLayoutManager(context)
        adapter.refresh(dataList)

    }

    private var selectItemListener: ((parenId:String, it: MovieTypeChild?,topPos:Int,childPos:Int) -> Unit)? = null
    //收起视频
    fun seItemListener(listener: (parenId:String,it: MovieTypeChild?,topPos:Int,childPos:Int) -> Unit) {
        selectItemListener = listener
    }
    inner class VideoWindowAdapter: BaseRecyclerAdapter<MovieType>(){
        override fun getItemLayoutId(viewType: Int) = R.layout.pop_video_type_item
        override fun bindData(holder: RecyclerViewHolder, position: Int, data: MovieType?) {
           holder.text(R.id.tvTitle,data?.name)
            val mSingleFlowTagLayout =holder.findViewById<FlowTagLayout>(R.id.flowlayout)
            val tagAdapter = FlowTagAdapter(context)
            mSingleFlowTagLayout.adapter = tagAdapter
            mSingleFlowTagLayout.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_MULTI)
            mSingleFlowTagLayout.setOnTagSelectListener { _, pos, _ ->
                selectItemListener?.invoke(data?.name?:"0",data?.children?.get(pos),position,pos)
                dismiss()
            }
            tagAdapter.addTags(data?.children)
           if (select!=null) {
               mSingleFlowTagLayout.setSelectedItems(select)
           }
        }
    }

    inner class FlowTagAdapter(context: Context): BaseTagAdapter<MovieTypeChild, TextView>(context) {
        override fun newViewHolder(convertView: View?): TextView { return (convertView!!.findViewById<View>(R.id.tv_tag) as TextView) }

        override fun getLayoutId() =  R.layout.adapter_video_item_tag
        override fun convert(holder: TextView?, item: MovieTypeChild?, position: Int) {
            holder?.text = item?.name
        }
    }
}