package com.lib.basiclib.widget.banner

import android.content.Context
import android.util.AttributeSet
import com.fh.basemodle.R
import com.lib.basiclib.base.xui.widget.banner.widget.banner.base.BaseImageBanner
import com.lib.basiclib.utils.ViewUtils

/**
 *
 * @ Author  QinTian
 * @ Date  2020/6/1
 * @ Describe
 *
 */
class RadiusImageBanner :
    BaseImageBanner<RadiusImageBanner?> {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)

    /**
     * @return 轮播布局的ID
     */
    override fun getItemLayoutId(): Int {
        return R.layout.adapter_radius_image
    }

    /**
     * @return 图片控件的ID
     */
    override fun getImageViewId(): Int {
        return R.id.riv
    }

    override fun getItemWidth(): Int {
        //需要距离边一点距离
        return super.getItemWidth() - ViewUtils.dp2px(32)
    }
}