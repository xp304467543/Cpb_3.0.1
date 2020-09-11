package com.customer.utils.countdowntimer

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/16
 * @ Describe
 *
 */
interface ITimerSupport {
    fun start()

    fun pause()

    fun resume()

    fun stop()

    fun reset()
}