package com.customer.component.input

import android.content.Context
import com.fh.module_base_resouce.R
import com.lib.basiclib.base.recycle.BaseRecyclerAdapter
import com.lib.basiclib.base.recycle.RecyclerViewHolder
import com.customer.component.panel.emotion.Emotion

/**
 *
 * @ Author  QinTian
 * @ Date  2020/9/1
 * @ Describe
 *
 */
class EmotionAdapter(val context: Context) : BaseRecyclerAdapter<Emotion>() {
    override fun getItemLayoutId(viewType: Int) = R.layout.vh_emotion_item_layout

    override fun bindData(holder: RecyclerViewHolder, position: Int, data: Emotion?) {
        data?.drawableRes?.let { holder.getImageView(R.id.image).setImageResource(it) }
    }
}