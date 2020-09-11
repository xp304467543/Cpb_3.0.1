package com.customer.component.input

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.customer.component.recyclepage.PagerGridLayoutManager
import com.customer.component.recyclepage.PagerGridSnapHelper
import com.fh.module_base_resouce.R
import com.customer.component.panel.emotion.Emotions

/**
 *
 * @ Author  QinTian
 * @ Date  2020/9/1
 * @ Describe
 *
 */
class EmotionPageGrid : LinearLayout {

    private var mContext: Context? = null
    private var vpEmotionView: ViewPager? = null
    private var emotion1: ImageView? = null
    private var emotion2: ImageView? = null

    constructor(context: Context) : super(context) {
        init(context)

    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)

    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context)

    }


    private fun init(context: Context) {
        this.mContext = context
        //加载布局文件，与setContentView()效果一样
        LayoutInflater.from(context).inflate(R.layout.input_hor_panel_view, this)
        vpEmotionView = findViewById(R.id.vpEmotionView)
        emotion1 = findViewById(R.id.emotion1)
        emotion2 = findViewById(R.id.emotion2)
        initRecycle()
    }
    private var setOnTextSendListener: ((str: String) -> Unit)? = null
    fun setOnTextSendListener(listener: (str: String) -> Unit) {
        setOnTextSendListener = listener
    }
     fun initRecycle() {
        val list = arrayListOf<RecyclerView>()
        repeat(2) {
            val recyclerView = mContext?.let { it1 -> RecyclerView(it1) }
            recyclerView?.layoutManager =
                PagerGridLayoutManager(4, 13, PagerGridLayoutManager.HORIZONTAL)
            val pageSnapHelper = PagerGridSnapHelper()
            pageSnapHelper.attachToRecyclerView(recyclerView)
            val adapter = EmotionAdapter(context)
            recyclerView?.adapter = adapter
            adapter.setOnItemClickListener { _, data, _ ->
                setOnTextSendListener?.invoke(data.text)
            }
            if (it == 0) {
                adapter.refresh(Emotions.getEmotions() )
            } else adapter.refresh(Emotions.getEmotionsFh())
            recyclerView?.let { it1 -> list.add(it1) }
        }
         val title = arrayListOf("emotion1", "emotion2")
         val adapter = ViewPagerAdapter(title, list)
         vpEmotionView?.adapter = adapter
         emotion1?.setOnClickListener {
             vpEmotionView?.currentItem = 0
         }
         emotion2?.setOnClickListener {
             vpEmotionView?.currentItem = 1
         }
    }


    inner class ViewPagerAdapter(title: List<String>, list: List<View>) : PagerAdapter() {

        private var mFragments = list
        private var mTitles = title


        override fun getCount(): Int {
            return mFragments.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return mTitles[position]
        }

        override fun isViewFromObject(view: View, obj: Any): Boolean {
            return view == obj
        }


        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val imageView: View = mFragments[position]
            container.addView(imageView)
            return imageView

        }


        override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
            container.removeView(obj as View?)
        }

    }
}