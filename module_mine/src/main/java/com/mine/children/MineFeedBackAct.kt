package com.mine.children

import android.annotation.SuppressLint
import android.text.*
import android.widget.EditText
import android.widget.TextView
import com.lib.basiclib.base.activity.BaseNavActivity
import com.lib.basiclib.utils.FastClickUtil
import com.lib.basiclib.utils.ToastUtils
import com.mine.R
import com.customer.data.mine.MineApi
import com.xiaojinzi.component.anno.RouterAnno
import com.customer.data.UserInfoSp
import kotlinx.android.synthetic.main.act_feed_back.*
import java.util.regex.Pattern

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/24
 * @ Describe
 *
 */
@RouterAnno(host = "Mine", path = "feedBack")
class MineFeedBackAct : BaseNavActivity() {

    override fun getContentResID() = R.layout.act_feed_back

    override fun isSwipeBackEnable() = true

    override fun getPageTitle() = getString(R.string.mine_feed_back)

    override fun isShowBackIconWhite() = false

    override fun initContentView() {
        initEditFeedBack(publish_ed_desc, publish_text_num)
        publish_ed_desc.filters = arrayOf(inputFilter)
    }

    override fun initEvent() {
        btSubMit.setOnClickListener {
            if (!TextUtils.isEmpty(publish_ed_desc.text)) {
                if (!FastClickUtil.isFastClick()) {
                    subMitAdv(publish_ed_desc.text.toString())
                }
            } else {
                ToastUtils.showToast("请输入反馈内容")
            }
        }
    }


    private fun subMitAdv(content: String) {
        MineApi.feedBack(content, UserInfoSp.getUserPhone() ?: "", "", "") {
            onSuccess {
                ToastUtils.showToast(it.msg)
                publish_ed_desc.setText("")
            }

        }
    }


    private fun initEditFeedBack(editText: EditText, textView: TextView) {
        val num = 0
        val mMaxNum = 500
        editText.addTextChangedListener(object : TextWatcher {
            //记录输入的字数
            var wordNum: CharSequence? = null
            var selectionStart: Int = 0
            var selectionEnd: Int = 0

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                wordNum = s
            }

            @SuppressLint("SetTextI18n")
            override fun afterTextChanged(s: Editable?) {
                val number = num + s!!.length
                textView.text = "$number/500"
                selectionStart = editText.selectionStart
                selectionEnd = editText.selectionEnd
                //判断大于最大值
                if (wordNum!!.length > mMaxNum) {
                    s.delete(selectionStart - 1, selectionEnd)
                    val tempSelection = selectionEnd
                    editText.text = s
                    editText.setSelection(tempSelection)
                    ToastUtils.showToast("最多输入500字")
                }
            }
        })
    }


    private var inputFilter: InputFilter = object : InputFilter {
        var emoji = Pattern.compile(
            "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
            Pattern.UNICODE_CASE or Pattern.CASE_INSENSITIVE
        )

        override fun filter(
            source: CharSequence,
            start: Int,
            end: Int,
            dest: Spanned,
            dstart: Int,
            dend: Int
        ): CharSequence? {
            val emojiMatcher = emoji.matcher(source)
            if (emojiMatcher.find()) {
                //                    Toast.makeText(MainActivity.this,"不支持输入表情", 0).show();
                ToastUtils.showToast("不支持输入表情")
                return ""
            }
            return null
        }
    }
}