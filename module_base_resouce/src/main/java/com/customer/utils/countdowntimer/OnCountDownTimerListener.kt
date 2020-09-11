package com.customer.utils.countdowntimer

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/16
 * @ Describe
 *
 */
interface OnCountDownTimerListener {
    /**
     * 间隔的回调
     * @param millisUntilFinished 剩余的时间
     */
    fun onTick(millisUntilFinished: Long)

    /**
     * 倒计时完成后的回调
     *
     */
    fun onFinish()

    /**
     * 手动停止计时器的回调
     */
    fun onCancel()
}