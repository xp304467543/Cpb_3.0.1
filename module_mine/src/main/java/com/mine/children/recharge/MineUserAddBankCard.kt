package com.mine.children.recharge

import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.widget.EditText
import com.customer.data.BankAddSuccess
import com.customer.data.mine.MineApi
import com.hwangjr.rxbus.RxBus
import com.lib.basiclib.base.activity.BaseNavActivity
import com.lib.basiclib.utils.FastClickUtil
import com.lib.basiclib.utils.ToastUtils
import com.lib.basiclib.utils.ViewUtils
import com.mine.R
import kotlinx.android.synthetic.main.act_user_add_bank_card.*
import java.util.regex.Pattern


/**
 *
 * @ Author  QinTian
 * @ Date  2020/10/14
 * @ Describe
 *
 */

class MineUserAddBankCard : BaseNavActivity() {

    var isEditName = false

    var isEditNum = false

    override fun getContentResID() = R.layout.act_user_add_bank_card

    override fun getPageTitle() = "添加账号"

    override fun isShowBackIconWhite() = false

    override fun isSwipeBackEnable() = true

    override fun isShowToolBar() = true

    override fun initContentView() {
        etCardUserName.filters = arrayOf(filter, InputFilter.LengthFilter(20))
        setEditTextInputSpeChat(etCardUserDes)
    }

    override fun initEvent() {
        etCardUserNum.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                isEditNum = etCardUserNum.length() > 12
                judgeInfo()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        etCardUserName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                isEditName = etCardUserName.length() > 1
                judgeInfo()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        btCardCommit.setOnClickListener {
            if (!FastClickUtil.isFastClick()) {
                if (etCardUserName?.text.toString().trim().isEmpty()) {
                    ToastUtils.showToast("请填写持卡人姓名")
                    return@setOnClickListener
                }
                if (etCardUserNum?.text.toString().trim().isEmpty()) {
                    ToastUtils.showToast("请填写卡号")
                    return@setOnClickListener
                }
                if (etCardUserNum?.text?.length?:0 < 12) {
                    ToastUtils.showToast("卡号长度错误")
                    return@setOnClickListener
                }
                addBankCard(etCardUserName.text.toString().trim(),etCardUserNum.text.toString().trim(),etCardUserDes.text.toString().trim())
            }
        }
    }

    fun judgeInfo() {
        if (isEditName && isEditNum ) {
            btCardCommit.background = ViewUtils.getDrawable(R.drawable.button_red_background)
            btCardCommit.setTextColor(ViewUtils.getColor(R.color.white))
        } else {
            btCardCommit.background = ViewUtils.getDrawable(R.drawable.button_grey_background)
            btCardCommit.setTextColor(ViewUtils.getColor(R.color.color_8899AA))
        }
    }

    private fun addBankCard(name:String, no:String, remark:String){
        showPageLoadingDialog("请稍后..")
        MineApi.addUserBankCard(name,no,remark){
            onSuccess {
                hidePageLoadingDialog()
                ToastUtils.showToast("添加成功")
                RxBus.get().post(BankAddSuccess(true))
                finish()
            }
            onFailed {
                hidePageLoadingDialog()
                ToastUtils.showToast(it.getMsg())
            }
        }
    }

    /**
     * EditText只能输入中文
     */
    private var filter = InputFilter { source, start, end, dest, dstart, dend ->
        val speChat = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";

        val pattern = Pattern.compile(speChat)

        val matcher = pattern.matcher(source.toString())

        if (matcher.find()) return@InputFilter ""

        for (i in start until end) {
            if (!isChinese(source[i])) {
                return@InputFilter ""
            }
        }
        null
    }

    /**
     * 禁止EditText输入特殊字符
     *
     * @param editText EditText输入框
     */
    private fun setEditTextInputSpeChat(editText: EditText) {
        val filter = InputFilter { source, start, end, dest, dstart, dend ->
            val speChat =
                "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]"
            val pattern = Pattern.compile(speChat)
            val matcher = pattern.matcher(source.toString())
            if (matcher.find()) {
                ""
            } else {
                null
            }
        }
        editText.filters = arrayOf(filter)
    }

    /**
     * 只能输入中文的判断
     *
     * @param c
     * @return
     */
    private fun isChinese(c: Char): Boolean {
        val ub = Character.UnicodeBlock.of(c)
        return ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS ||
                ub === Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS ||
                ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A ||
                ub === Character.UnicodeBlock.GENERAL_PUNCTUATION ||
                ub === Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION ||
                ub === Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
    }
}