package com.customer.component.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.customer.component.htmltextview.HtmlHttpImageGetter
import com.customer.component.htmltextview.HtmlTextView
import com.customer.data.UserInfoSp
import com.customer.data.home.SystemNotice
import com.customer.data.home.SystemNoticeChild
import com.fh.module_base_resouce.R
import com.lib.basiclib.base.recycle.BaseRecyclerAdapter
import com.lib.basiclib.base.recycle.RecyclerViewHolder
import com.lib.basiclib.base.round.RoundTextView
import com.lib.basiclib.utils.ViewUtils
import kotlinx.android.synthetic.main.dialog_system_notice.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/9/5
 * @ Describe
 *
 */
class DialogSystemNotice(context: Context) : Dialog(context) {

    private var recyclerView: RecyclerView? = null
    private var recyclerViewContent: RecyclerView? = null
    private var adapter: NoticeAdapter? = null
    private var adapter1: NoticeAdapterContent? = null

    init {
        window?.setWindowAnimations(R.style.BaseDialogAnim)
        setContentView(R.layout.dialog_system_notice)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setGravity(Gravity.CENTER or Gravity.CENTER)
        val lp = window?.attributes
//      lp.alpha = 0.7f // 透明度
        window?.attributes = lp
        setCanceledOnTouchOutside(true)
        recyclerView = findViewById(R.id.tvTypeNotice)
        recyclerViewContent = findViewById(R.id.tvTypeNoticeContent)
        adapter = NoticeAdapter()
        recyclerView?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView?.adapter = adapter
        adapter1 = NoticeAdapterContent()
        recyclerViewContent?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerViewContent?.adapter = adapter1
        imgClose.setOnClickListener {
            dismiss()
        }
    }


    fun setContent(string: List<SystemNotice>?) {
        if (!string.isNullOrEmpty()){
            adapter?.refresh(string)
        }
    }

    inner class NoticeAdapter : BaseRecyclerAdapter<SystemNotice>() {
        var current = 0
        var first = true
        override fun getItemLayoutId(viewType: Int) = R.layout.adapter_dialog_system_notice
        override fun bindData(holder: RecyclerViewHolder, position: Int, data: SystemNotice?) {
            val type = holder.findViewById<AppCompatTextView>(R.id.tvNoticeType)
            val red = holder.findViewById<RoundTextView>(R.id.tvNoticeRound)
            type.text = data?.name
            val layout = holder.findViewById<ConstraintLayout>(R.id.itemLayout)
            if (current == position){
                layout.background = ViewUtils.getDrawable(R.drawable.background_gradient)
                type.setTextColor(ViewUtils.getColor(R.color.white))
            }else {
                layout.background = null
                type.setTextColor(ViewUtils.getColor(R.color.color_333333))
            }
           if (first && position == 0) {
               adapter1?.refresh(data?.data)
           }
            if (position!=0){
                val notice = UserInfoSp.getSystemNotice(data?.name?:"1")
                if (notice== 0){
                    ViewUtils.setVisible(red)
                }else{
                    if (notice != data?.data?.size?:0){
                        ViewUtils.setVisible(red)
                    }else ViewUtils.setGone(red)
                }
            }
            holder.itemView.setOnClickListener {
                current= position
                first = false
                adapter1?.refresh(getData()[position].data)
                UserInfoSp.putSystemNotice(data?.name?:"1",data?.data?.size?:0)
                notifyDataSetChanged()
                UserInfoSp
            }
        }
    }

    inner class NoticeAdapterContent : BaseRecyclerAdapter<SystemNoticeChild>() {
        override fun getItemLayoutId(viewType: Int) = R.layout.adapter_dialog_system_notice_content
        override fun bindData(holder: RecyclerViewHolder, position: Int, data: SystemNoticeChild?) {
            holder.text(R.id.tvContentTitle, data?.title)
            val htmlText = holder.findViewById<HtmlTextView>(R.id.tvContent)
            htmlText.setHtml(data?.content ?: "",HtmlHttpImageGetter(htmlText))
        }
    }
}