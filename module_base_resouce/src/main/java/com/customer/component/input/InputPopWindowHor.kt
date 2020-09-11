package com.customer.component.input

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.view.WindowManager
import android.widget.EditText
import android.widget.PopupWindow
import com.fh.module_base_resouce.R
import com.customer.component.panel.PanelSwitchHelper
import com.customer.component.panel.emotion.EmotionSpanBuilder
import com.customer.component.panel.interfaces.listener.OnPanelChangeListener
import com.customer.component.panel.view.panel.IPanelView
import com.customer.component.panel.view.panel.PanelView
import com.lib.basiclib.utils.ToastUtils

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/27
 * @ Describe
 *
 */
class InputPopWindowHor(activity: Activity) : PopupWindow(activity) {
    private var mHelper: PanelSwitchHelper? = null
    private val editTextPanel: EditText
    init {
        val view = LayoutInflater.from(activity).inflate(R.layout.input_hor_layout, null, false)
        isFocusable = true
        editTextPanel = view.findViewById(R.id.edit_text)
        width = WindowManager.LayoutParams.MATCH_PARENT
        height = WindowManager.LayoutParams.MATCH_PARENT
        isOutsideTouchable = true
        val dw = ColorDrawable(Color.TRANSPARENT)
        setBackgroundDrawable(dw)
        inputMethodMode = INPUT_METHOD_NEEDED
        softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
        contentView = view
        view.findViewById<View>(R.id.send).setOnClickListener { _: View? ->
                if (editTextPanel.text.toString().trim { it <= ' ' }.isNotEmpty()) {
                    setOnTextSendListener?.invoke(editTextPanel.text.toString())
                    editTextPanel.setText("")
                } else ToastUtils.showToast("请输入内容")
            }
        if (mHelper == null) {
            mHelper = PanelSwitchHelper.Builder(
                activity.window,
                contentView
            )
                .addPanelChangeListener(object : OnPanelChangeListener {
                    override fun onKeyboard() {
                        contentView.findViewById<View>(R.id.add_btn).isSelected = false
                    }

                    override fun onNone() {
                        contentView.findViewById<View>(R.id.add_btn).isSelected = false
                        dismiss()
                    }

                    override fun onPanel(panelView: IPanelView?) {
                        contentView.findViewById<View>(R.id.add_btn).isSelected = true
                    }

                    override fun onPanelSizeChange(
                        panelView: IPanelView?,
                        portrait: Boolean,
                        oldWidth: Int,
                        oldHeight: Int,
                        width: Int,
                        height: Int
                    ) {
                        if (panelView is PanelView) {
                            if (panelView.id == R.id.panel_input) {
                                val pagerView: EmotionPageGrid = view.findViewById(R.id.emotionView)
                                val root = view.findViewById<View>(R.id.inPut)
                                val layoutParams = root.layoutParams as MarginLayoutParams
                                if (layoutParams.width != width || layoutParams.height == height) {
                                    layoutParams.width = width
                                    layoutParams.height = height
                                    root.layoutParams = layoutParams
                                }
                                pagerView.setOnTextSendListener {
                                    val start: Int = editTextPanel.selectionStart
                                   val editable = editTextPanel.editableText
                                   val emotionSpannable = EmotionSpanBuilder.buildEmotionSpannable(activity,it)
                                    editable.insert(start, emotionSpannable)
                                }
                            }
                        }
                    }
                })
                .logTrack(true)
                .build(false)
        }
    }

    private var setOnTextSendListener: ((str: String) -> Unit)? = null


    fun setOnTextSendListener(listener: (str: String) -> Unit) {
        setOnTextSendListener = listener
    }

    fun showKeyboard() {
        if (mHelper != null) {
            mHelper?.toKeyboardState()
        }
    }

    override fun dismiss() {
        if (mHelper != null && mHelper?.hookSystemBackByPanelSwitcher() ==true) {
            return
        }
        super.dismiss()
    }

}
