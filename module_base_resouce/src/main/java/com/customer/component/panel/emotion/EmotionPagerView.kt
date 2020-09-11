package com.customer.component.panel.emotion

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.fh.basemodle.R
import com.customer.component.panel.IndicatorView
import com.lib.basiclib.utils.ViewUtils
import java.util.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/21
 * @ Describe
 *
 */
class EmotionPagerView constructor(
    context: Context,
    attrs: AttributeSet? = null
) :
    ViewPager(context, attrs) {
    private var currentWidth = -1
    private var currentHeight = -1
    private var mAdapter: Adapter? = null
    fun buildEmotionViews(
        indicatorView: IndicatorView?,
        editText: EditText?,
        data: List<Emotion>?,
        data2: List<Emotion>?,
        width: Int,
        height: Int,
        emo1:LinearLayout,
        emo2:LinearLayout
    ) {
        if (data == null || data.isEmpty() || data2 == null || data2.isEmpty() || editText == null) {
            return
        }
        if (currentWidth == width && currentHeight == height) {
            return
        }
        currentWidth = width
        currentHeight = height
        val emotionViewContainSize: Int = EmotionView.calSizeForContainEmotion(context, currentWidth, currentHeight)
        val emotionViewContainSizeFh: Int = EmotionViewFh.calSizeForContainEmotion(context, currentWidth, currentHeight)
        if (emotionViewContainSize == 0) {
            return
        }
        var index = 0
        val emotionViews: MutableList<View> = ArrayList()
        for (i in 0 until 2) {
            val emotionView = EmotionView(context, editText)
            var end = (i + 1) * emotionViewContainSize
            if (end > data.size) {
                end = data.size
            }
            emotionView.buildEmotions(data.subList(index, end))
            emotionViews.add(emotionView)
            index = end
        }
        var index2 = 0
        for (i in 0 until 1) {
            val emotionView2 = EmotionViewFh(context, editText)
            var end = (i + 1) * emotionViewContainSizeFh
            if (end > data2.size) {
                end = data2.size
            }
            emotionView2.buildEmotions(data2.subList(index2, end))
            emotionViews.add(emotionView2)
            index2 = end
        }
        mAdapter = Adapter(emotionViews)
        indicatorView?.mItemCount = 3
        adapter = mAdapter
        addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                indicatorView?.mCurSelect = position
                if (position == 0 || position == 1){
                    emo1.setBackgroundColor(ViewUtils.getColor(R.color.grey_f5f7fa))
                    emo2.setBackgroundColor(ViewUtils.getColor(R.color.white))
                }else if (position == 2){
                    emo1.setBackgroundColor(ViewUtils.getColor(R.color.white))
                    emo2.setBackgroundColor(ViewUtils.getColor(R.color.grey_f5f7fa))
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
        emo1.setOnClickListener {
            currentItem = 0
        }
        emo2.setOnClickListener {
            currentItem = 2
        }
    }

    class Adapter(private val mList: List<View>) : PagerAdapter() {
        override fun getCount(): Int {
            return mList.size
        }

        override fun isViewFromObject(
            view: View,
            `object`: Any
        ): Boolean {
            return `object` === view
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            container.addView(mList[position])
            return mList[position]
        }

        override fun destroyItem(
            container: ViewGroup,
            position: Int,
            `object`: Any
        ) {
            container.removeView(mList[position])
        }

    }
}
