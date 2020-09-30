package com.customer.component.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.view.isVisible
import com.fh.module_base_resouce.R
import kotlinx.android.synthetic.main.dialog_pass_word_hor.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/25
 * @ Describe
 *
 */
class DialogPassWordHor (context: Context) : Dialog(context) {

    init {
        setContentView(R.layout.dialog_pass_word_hor)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        window?.setGravity(Gravity.CENTER)
        setCanceledOnTouchOutside(false)
//        val lp = window?.attributes
//        lp.width = widthDia
//        lp.height = heightDia
//        window?.attributes = lp
        initDialog()
    }

    private fun initDialog() {
        relCloseDialog.setOnClickListener {
            disMissKeyBord()
            dismiss()
        }
    }

    fun setTextWatchListener(textWatcher: TextWatcher) {
        edtPassWord.addTextChangedListener(textWatcher)
    }

    fun showTipsText(string: String) {
        tvFalseTip.visibility = View.VISIBLE
        tvFalseTip.text = string
    }

    override fun dismiss() {
        disMissKeyBord()
        super.dismiss()

    }

    fun clearText() {
        edtPassWord.clearText()
    }

    fun showOrHideLoading() {
        if (edtHorLoadingContainer.isVisible) {
            edtHorLoadingContainer.visibility = View.GONE
        } else edtHorLoadingContainer.visibility = View.VISIBLE

    }


    private fun disMissKeyBord() {
        val view = currentFocus
        if (view is TextView) {
            val mInputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            mInputMethodManager.hideSoftInputFromWindow(view.windowToken, InputMethodManager.RESULT_UNCHANGED_SHOWN)
        }
    }
}

