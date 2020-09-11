package com.customer.component.panel.interfaces.listener


interface OnKeyboardStateListener {
    fun onKeyboardChange(visible: Boolean, height: Int)
}
private typealias OnKeyboardChange = (visible: Boolean, height: Int) -> Unit

class OnKeyboardStateListenerBuilder : OnKeyboardStateListener {

    private var onKeyboardChange: OnKeyboardChange? = null

    override fun onKeyboardChange(visible: Boolean, height: Int) {
        onKeyboardChange?.invoke(visible, height)
    }

    fun onKeyboardChange(onKeyboardChange: OnKeyboardChange) {
        this.onKeyboardChange = onKeyboardChange
    }
}