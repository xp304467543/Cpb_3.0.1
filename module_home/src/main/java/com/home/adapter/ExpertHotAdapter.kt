package com.home.adapter

import android.content.Context
import android.graphics.Typeface
import android.widget.LinearLayout
import android.widget.TextView
import com.customer.ApiRouter
import com.glide.GlideUtil
import com.home.R
import com.customer.data.home.HomeExpertList
import com.lib.basiclib.base.recycle.BaseRecyclerAdapter
import com.lib.basiclib.base.recycle.RecyclerViewHolder
import com.lib.basiclib.utils.FastClickUtil
import com.lib.basiclib.utils.ViewUtils
import com.lib.basiclib.utils.ViewUtils.getColor
import com.xiaojinzi.component.impl.Router
import java.math.BigDecimal

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/14
 * @ Describe
 *
 */
class ExpertHotAdapter(var context: Context?) : BaseRecyclerAdapter<HomeExpertList>() {
    override fun getItemLayoutId(viewType: Int) = R.layout.adapter_home_expert_hot

    override fun bindData(holder: RecyclerViewHolder, position: Int, data: HomeExpertList?) {
        holder.text(R.id.tvExpertName, data?.nickname)
        holder.text(R.id.tvLotteryTpe, data?.lottery_name)
        if (data?.win_rate != "加载中...") holder.text(
            R.id.rtvExpertWinPresent,
            "胜率 " + BigDecimal(data?.win_rate).setScale(2, BigDecimal.ROUND_HALF_UP).multiply(
                BigDecimal(100)
            ).stripTrailingZeros().toPlainString() + "%"
        )
        else holder.text(R.id.rtvExpertWinPresent, data.win_rate + "%")
        holder.text(R.id.rtvExpertWinAdd, data?.winning + " 连红")
        if (data?.profit_rate != "加载中...") holder.text(
            R.id.tvPercentage,
            BigDecimal(data?.profit_rate).setScale(4, BigDecimal.ROUND_HALF_UP).multiply(
                BigDecimal(100)
            ).stripTrailingZeros().toPlainString()
        )
        else holder.text(R.id.tvPercentage, data.profit_rate)
        context?.let { GlideUtil.loadCircleImage(it, data?.avatar, holder.getImageView(R.id.expertAvatar),true) }
        if (data != null) {
            setTextList(data, holder.findViewById(R.id.linTenList))
        }
        holder.click(R.id.expertAvatar){
            if (!FastClickUtil.isFastClick())Router.withApi(ApiRouter::class.java).toExpertPage(data?.id.toString(),data?.lottery_id.toString())
        }
    }

    //修改字体颜色
    private fun setTextList(data: HomeExpertList, linearLayout: LinearLayout) {
        when {
            data.last_10_games == null || data.last_10_games!!.isEmpty() -> {
                linearLayout.removeAllViews()
                val textView = TextView(context)
                textView.textSize = 14f
                textView.setTextColor(getColor(R.color.color_333333))
                textView.text = "暂无"
                linearLayout.addView(textView)
            }
            else -> {
                linearLayout.removeAllViews()
                val width = (ViewUtils.getScreenWidth() - ViewUtils.dp2px(100)) / 10
                for (result in data.last_10_games!!) {
                    val textView = TextView(context)
                    textView.textSize = 14f
                    textView.typeface = Typeface.DEFAULT_BOLD
                    if (result == "1") {
                        textView.text = "胜"
                        textView.setTextColor(getColor(R.color.color_FF513E))
                    } else {
                        textView.text = "负"
                        textView.setTextColor(getColor(R.color.color_999999))
                    }
                    linearLayout.addView(textView)
                    val params = textView.layoutParams as LinearLayout.LayoutParams
                    params.width = width

                }
            }
        }
    }
}