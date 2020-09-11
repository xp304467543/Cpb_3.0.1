package com.customer.component.input

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import com.customer.component.panel.EditedRaul
import com.fh.module_base_resouce.R
import com.customer.component.panel.IndicatorView
import com.customer.component.panel.PanelSwitchHelper
import com.customer.component.panel.emotion.EmotionPagerView
import com.customer.component.panel.emotion.Emotions
import com.customer.component.panel.interfaces.listener.OnPanelChangeListener
import com.customer.component.panel.view.panel.IPanelView
import com.customer.component.panel.view.panel.PanelView
import com.lib.basiclib.utils.ToastUtils.showToast
import com.lib.basiclib.utils.ViewUtils.dp2px

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/27
 * @ Describe
 *
 */
class InputPopWindow(activity: Activity, isRab:Boolean=false) : PopupWindow(activity) {
    private var mHelper: PanelSwitchHelper? = null
    private var setOnTextSendListener: ((str: String) -> Unit)? = null
    val editTextPanel: EditText
    val send:TextView
    fun showKeyboard() {
        if (mHelper != null) {
            mHelper?.toKeyboardState()
        }
    }

    fun setOnTextSendListener(listener: (str: String) -> Unit) {
        setOnTextSendListener = listener
    }

    init {
        val view: View = LayoutInflater.from(activity).inflate(R.layout.input_live_room, null, false)
        isFocusable = true
        width = WindowManager.LayoutParams.MATCH_PARENT
        height = WindowManager.LayoutParams.MATCH_PARENT
        isOutsideTouchable = true
        val dw = ColorDrawable(Color.TRANSPARENT)
        setBackgroundDrawable(dw)
        inputMethodMode = INPUT_METHOD_NEEDED
        softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING
        contentView = view
        editTextPanel = view.findViewById(R.id.edit_text)
        send = view.findViewById(R.id.send)
        EditedRaul.setEdiTextRul(activity, editTextPanel, send)
        view.findViewById<View>(R.id.send)
            .setOnClickListener {
                if (editTextPanel.text.toString().trim { it <= ' ' }.isNotEmpty()) {
                    setOnTextSendListener?.invoke(editTextPanel.text.toString())
                    editTextPanel.setText("")
                } else showToast("请输入内容")
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
                            if (panelView.id == R.id.panel_bilibili) {
                                val pagerView: EmotionPagerView = view.findViewById(R.id.vpEmotion)
                                val indicatorView: IndicatorView = view.findViewById(R.id.indicatorViewPanel)
                                val em1 =
                                    view.findViewById<LinearLayout>(R.id.emotion1)
                                val em2 =
                                    view.findViewById<LinearLayout>(R.id.emotion2)
                                val viewPagerSize = height - dp2px(30)
                                val layoutParams = pagerView.layoutParams as LinearLayout.LayoutParams
                                if (layoutParams.width != width || layoutParams.height == height) {
                                    layoutParams.width = width
                                    layoutParams.height = height
                                    pagerView.layoutParams = layoutParams
                                    pagerView.buildEmotionViews(
                                        indicatorView, editTextPanel, Emotions.getEmotions(),
                                        Emotions.getEmotionsFh(), width, viewPagerSize, em1, em2
                                    )
                                }
                            }
                        }
                    }
                }).logTrack(false)
                .contentScrollOutsideEnable(false)
                .build(false)
        }
    }

    private var inputIsShow: ((boolean: Boolean) -> Unit)? = null


    override fun dismiss() {
        if (mHelper != null && mHelper?.hookSystemBackByPanelSwitcher() == true) {
            return
        }
        super.dismiss()
    }

}
