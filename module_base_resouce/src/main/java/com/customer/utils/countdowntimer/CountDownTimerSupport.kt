package com.customer.utils.countdowntimer

import android.os.Handler
import java.util.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/16
 * @ Describe
 *
 */
class CountDownTimerSupport : ITimerSupport {
    private var mTimer: Timer? = null
    private var mHandler: Handler

    /**
     * 倒计时时间
     */
    private var mMillisInFuture: Long = 0

    /**
     * 间隔时间
     */
    private var mCountDownInterval: Long = 1000 //默认1秒

    /**
     * 倒计时剩余时间
     */
    private var mMillisUntilFinished: Long = 0
    private var mOnCountDownTimerListener: OnCountDownTimerListener? = null
    private var mTimerState = TimerState.FINISH

    constructor() {
        mHandler = Handler()
    }

    constructor(millisInFuture: Long, countDownInterval: Long) {
        setMillisInFuture(millisInFuture)
        setCountDownInterval(countDownInterval)
        mHandler = Handler()
    }

    override fun start() {
        //防止重复启动 重新启动要先reset再start
        if (mTimer == null && mTimerState !== TimerState.START) {
            mTimer = Timer()
            mTimer?.scheduleAtFixedRate(createTimerTask(), 0, mCountDownInterval)
            mTimerState = TimerState.START
        }
    }

    override fun pause() {
        if (mTimer != null && mTimerState === TimerState.START) {
            cancelTimer()
            mTimerState = TimerState.PAUSE
        }
    }

    override fun resume() {
        if (mTimerState === TimerState.PAUSE) {
            start()
        }
    }

    override fun stop() {
        stopTimer(true)
    }

    override fun reset() {
        if (mTimer != null) {
            cancelTimer()
        }
        mMillisUntilFinished = mMillisInFuture
        mTimerState = TimerState.FINISH
    }

    private fun stopTimer(cancel: Boolean) {
        if (mTimer != null) {
            cancelTimer()
            mMillisUntilFinished = mMillisInFuture
            mTimerState = TimerState.FINISH
            mHandler.post {
                if (mOnCountDownTimerListener != null) {
                    if (cancel) {
                        mOnCountDownTimerListener!!.onCancel()
                    } else {
                        mOnCountDownTimerListener!!.onFinish()
                    }
                }
            }
        }
    }

    private fun cancelTimer() {
        mTimer?.cancel()
        mTimer?.purge()
        mTimer = null
    }

    fun isStart(): Boolean {
        return mTimerState === TimerState.START
    }

    fun isFinish(): Boolean {
        return mTimerState === TimerState.FINISH
    }

    /**
     * @param millisInFuture
     */
     fun setMillisInFuture(millisInFuture: Long) {
        mMillisInFuture = millisInFuture
        mMillisUntilFinished = mMillisInFuture
    }

    /**
     * @param countDownInterval
     */
    fun setCountDownInterval(countDownInterval: Long) {
        mCountDownInterval = countDownInterval
    }

    fun setOnCountDownTimerListener(listener: OnCountDownTimerListener?) {
        mOnCountDownTimerListener = listener
    }

    fun getMillisUntilFinished(): Long {
        return mMillisUntilFinished
    }

    fun getTimerState(): TimerState {
        return mTimerState
    }

     private fun createTimerTask(): TimerTask {
        return object : TimerTask() {
            private var startTime: Long = -1
           override fun run() {
                if (startTime < 0) {
                    //第一次回调 记录开始时间
                    startTime = scheduledExecutionTime() - (mMillisInFuture - mMillisUntilFinished)
                    mHandler.post(Runnable {
                        if (mOnCountDownTimerListener != null) {
                            mOnCountDownTimerListener!!.onTick(mMillisUntilFinished)
                        }
                    })
                } else {
                    //剩余时间
                    mMillisUntilFinished = mMillisInFuture - (scheduledExecutionTime() - startTime)
                    mHandler.post(Runnable {
                        if (mOnCountDownTimerListener != null) {
                            mOnCountDownTimerListener!!.onTick(mMillisUntilFinished)
                        }
                    })
                    if (mMillisUntilFinished <= 0) {
                        //如果没有剩余时间 就停止
                        stopTimer(false)
                    }
                }
            }
        }
    }
}