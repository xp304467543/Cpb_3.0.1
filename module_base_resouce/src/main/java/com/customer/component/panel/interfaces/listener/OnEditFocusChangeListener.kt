package com.customer.component.panel.interfaces.listener
import android.view.View

/**
 * listen to  [android.widget.EditText] focus change
 */
interface OnEditFocusChangeListener {
    fun onFocusChange(view: View?, hasFocus: Boolean)
}
private typealias OnFocusChange = (view: View?, hasFocus: Boolean) -> Unit

class OnEditFocusChangeListenerBuilder : OnEditFocusChangeListener {

    private var onFocusChange: OnFocusChange? = null

    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        onFocusChange?.invoke(view, hasFocus)
    }

    fun onFocusChange(onFocusChange: OnFocusChange) {
        this.onFocusChange = onFocusChange
    }
}
