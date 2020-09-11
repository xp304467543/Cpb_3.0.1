package com.customer.component.panel

import android.content.Context
import android.os.Build
import android.text.Editable
import android.text.Selection
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.fh.basemodle.R
import com.customer.component.panel.emotion.EmotionSpanBuilder
import com.lib.basiclib.utils.ToastUtils
import com.lib.basiclib.utils.ViewUtils

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/22
 * @ Describe
 *
 */
object EditedRaul {

     fun setEdiTextRul(context: Context, etLiveRoomChat: EditText, sendBtn : TextView) {
        etLiveRoomChat.addTextChangedListener(object : TextWatcher {
            @RequiresApi(Build.VERSION_CODES.M)
            override fun afterTextChanged(s: Editable?) {
                if (s?.length!! > 196) {
                    ToastUtils.showToast("最多输入200个字")
                    return
                }
                if (s.isNotEmpty()) {
                    sendBtn.background = ViewUtils.getDrawable(R.drawable.button_red_background)
                    sendBtn.setTextColor(ViewUtils.getColor(R.color.white))
                } else {
                    sendBtn.background =ViewUtils.getDrawable(R.drawable.button_grey_background)
                    sendBtn.setTextColor(ViewUtils.getColor(R.color.grey_95))
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                var editable = etLiveRoomChat.text
                val len = editable.length
                if (len > 196) {
                    var selEndIndex = Selection.getSelectionEnd(editable)
                    val str = editable.toString()
                    //截取新字符串
                    val newStr = str.substring(0, start)
                    etLiveRoomChat.setText(EmotionSpanBuilder.buildEmotionSpannable(context,newStr))
                    editable = etLiveRoomChat.text
                    //新字符串的长度
                    val newLen = editable.length
                    //旧光标位置超过字符串长度
                    if (selEndIndex > newLen) {
                        selEndIndex = editable.length
                    }
                    //设置新光标所在的位置
                    Selection.setSelection(editable, selEndIndex)
                }
            }
        })
    }
}