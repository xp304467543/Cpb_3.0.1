package com.customer.component.dialog

import android.content.Context
import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.fh.module_base_resouce.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.lib.basiclib.utils.ToastUtils
import kotlinx.android.synthetic.main.dialog_bottom_lottery.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/17
 * @ Describe
 *
 */
class DialogBottomLottery( context: Context, val list: List<BottomDialogBean>?) : BottomSheetDialog(context) {


    var bottomAdapter: BottomDialogAdapter? = null

    private var mSureClickListener: ((data: List<BottomDialogBean>) -> Unit)? = null

    init {
        setContentView(R.layout.dialog_bottom_lottery)
        val root = this.delegate?.findViewById<View>(R.id.design_bottom_sheet)
        val behavior = root?.let { BottomSheetBehavior.from(it) }
        behavior?.isHideable = false
        this.delegate?.findViewById<View>(R.id.design_bottom_sheet)?.setBackgroundColor(Color.TRANSPARENT)

        initRecycle()
        initEvent()
    }

    private fun initRecycle() {
        bottomAdapter = BottomDialogAdapter()
        val gridLayoutManager = object : GridLayoutManager(context, 4) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        rvRankSelect.adapter = bottomAdapter
        rvRankSelect.layoutManager = gridLayoutManager
        if (list != null && list.isNotEmpty()) {
            bottomAdapter!!.refresh(list)
        }

    }


    private fun initEvent() {
        tvCancel.setOnClickListener {
            dismiss()
        }
        tvConfirm.setOnClickListener {
            for ((index, _) in list!!.withIndex()) {
                if ( list[index].isSelect){
                    mSureClickListener?.invoke(bottomAdapter?.data as List<BottomDialogBean>)
                    return@setOnClickListener
                }else if (index == list.size -1){
                    ToastUtils.showToast("至少选择一项")
                }
            }

        }
        tvSelectAll.setOnClickListener {
            for ((index, _) in list!!.withIndex()) {
                list[index].isSelect = true
            }
            bottomAdapter!!.notifyDataSetChanged()
        }
        tvClearAll.setOnClickListener {
            for ((index, _) in list!!.withIndex()) {
                list[index].isSelect = false
            }
            bottomAdapter!!.notifyDataSetChanged()
        }
    }




    /**
     * 确定点击
     */
    fun setOnSureClickListener(listener: (data: List<BottomDialogBean>) -> Unit) {
        this.mSureClickListener = listener
    }
}