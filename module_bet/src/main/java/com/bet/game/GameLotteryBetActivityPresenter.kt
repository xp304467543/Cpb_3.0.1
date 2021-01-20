package com.bet.game

import android.annotation.SuppressLint
import android.os.CountDownTimer
import android.os.Handler
import com.bet.R
import com.customer.data.UserInfoSp
import com.customer.data.lottery.LotteryApi
import com.customer.utils.SoundPoolHelper
import com.customer.utils.countdowntimer.lotter.LotteryTypeSelectUtil
import com.lib.basiclib.base.mvp.BaseMvpPresenter
import com.lib.basiclib.utils.ToastUtils
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.act_game_lottery_bet.*
import java.util.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/9/14
 * @ Describe
 *
 */
class GameLotteryBetActivityPresenter : BaseMvpPresenter<GameLotteryBetActivity>() {


    fun getLottery() {
        val result = LotteryApi.getLotteryType()
        if (mView.isActive()) {
            result.onSuccess {
                mView.showPickerView(it)
            }
            result.onFailed {
                ToastUtils.showToast(it.getMsg()+": 彩种获取失败")
            }
        }
    }

    // ===== 开奖倒计时 =====
    var timer: CountDownTimer? = null
    private var handler: Handler? = null
    private var runnable: Runnable? = null
    var handlerPlay: Handler? = null


    private fun countDownTime(millisUntilFinished: String, lotteryId: String) {
        if (timer != null) timer?.cancel()
        handler?.removeCallbacks(runnable)
        val timeCountDown = millisUntilFinished.toLong() * 1000
        timer = object : CountDownTimer(timeCountDown, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                setTime(millisUntilFinished)
            }

            override fun onFinish() {
                if (mView.tvOpenTime != null) mView.tvOpenTime.text = "--:--"
                mView.isPlay = true
                getLotteryOpenCode(lotteryId)
                mView.setVisible(mView.tvOpenCodePlaceHolder)
            }
        }
        if (timer != null) timer?.start()
    }


    var apiResult:Disposable?=null
    @SuppressLint("SetTextI18n")
    fun getLotteryOpenCode(lotteryId: String) {
        handler?.removeCallbacks(runnable)
        apiResult?.dispose()
        if (mView.tvCloseTime != null) mView.tvCloseTime?.text = "--:--"
        if (timerClose != null) timerClose?.cancel()
       apiResult = LotteryApi.getLotteryNewCode(lotteryId) {
            onSuccess {
                if (mView.isActive()) {
                    if (it.next_lottery_time?.toInt() ?: 0 > 1) {
                        countDownTime(it.next_lottery_time.toString(),lotteryId)
                        mView.tvOpenCount.text = it.issue + " 期开奖结果"
                        setContainerCode(it.lottery_id, it.code)
                        mView.setGone(mView.tvOpenCodePlaceHolder)
                        mView.betFragment?.lotteryInfo(it.issue?:"-1",it.next_issue?:"-1", lotteryId,true)
                        countDownTimerClose(it.issue?:"-1",it.next_issue?:"-1", lotteryId,it.next_lottery_end_time ?: 0)
                        isOpenCode = true
                        if (mView.isPlay) {
                            if (UserInfoSp.getIsPlaySound()) {
                                if (handlerPlay == null) handlerPlay = Handler()
                                handlerPlay?.post {
                                    SoundPoolHelper(mView).playSoundWithRedId(R.raw.ring)
                                }
                            }
                        }
                    } else {
                        if (timer != null) timer?.cancel()
                        if (!mView.isDestroyed) {
                            mView.tvOpenTime.text = "--:--"
                            mView.tvOpenCount.text = ("- - - -" + "期开奖结果   ")
                            mView.tvCloseTime.text = "--:--"
                            mView.setVisible(mView.tvOpenCodePlaceHolder)
                        }
                        getNewResult(lotteryId)
                    }
                }
            }
           onFailed {
               if (timer != null) timer?.cancel()
               if (!mView.isDestroyed) {
                   mView.tvOpenTime.text = "--:--"
                   mView.tvOpenCount.text = ("- - - -" + "期开奖结果   ")
                   mView.tvCloseTime.text = "--:--"
                   mView.setVisible(mView.tvOpenCodePlaceHolder)
               }
               getNewResult(lotteryId)
           }
        }

    }

//    @SuppressLint("SetTextI18n")
//    fun getLotteryOpenCode(lotteryId: String) {
//        handler?.removeCallbacks(runnable)
//        mTimer?.stop()
//        if (mView.tvCloseTime != null) mView.tvCloseTime?.text = "--:--"
//        if (timerClose != null) timerClose?.cancel()
//         LotteryApi.getLotteryNewCode(lotteryId) {
//            onSuccess {
//                if (mView.isActive()) {
//                    if (it.next_lottery_time?.toInt() ?: 0 > 1) {
//                        if (mTimer == null) {
//                            mTimer = CountDownTimerSupport()
//                            mTimer?.setOnCountDownTimerListener(object : OnCountDownTimerListener {
//                                override fun onTick(millisUntilFinished: Long) {
//                                    setTime(millisUntilFinished)
//                                }
//
//                                override fun onFinish() {
//                                    if (mView.tvOpenTime != null) mView.tvOpenTime.text = "--:--"
//                                    mView.isPlay = true
//                                    getLotteryOpenCode(lotteryId)
//                                    mView.setVisible(mView.tvOpenCodePlaceHolder)
//                                }
//
//                                override fun onCancel() {
//
//                                }
//                            })
//                        } else mTimer?.stop()
//                        mView.tvOpenCount.text = it.issue + " 期开奖结果"
//                        //总时长 间隔时间
//                        mTimer?.setMillisInFuture((it.next_lottery_time ?: 0) * 1000)
//
//                        mTimer?.start()
//                        setContainerCode(it.lottery_id, it.code)
//                        mView.setGone(mView.tvOpenCodePlaceHolder)
//                        mView.betFragment?.lotteryInfo(it.issue?:"-1",it.next_issue?:"-1", lotteryId,true)
//                        countDownTimerClose(it.issue?:"-1",it.next_issue?:"-1", lotteryId,it.next_lottery_end_time ?: 0)
//                        isOpenCode = true
//                        if (mView.isPlay) {
//                            if (UserInfoSp.getIsPlaySound()) {
//                                if (handlerPlay == null) handlerPlay = Handler()
//                                handlerPlay?.post {
//                                    SoundPoolHelper(mView).playSoundWithRedId(R.raw.ring)
//                                }
//                            }
//                        }
//                    } else {
//                        mTimer?.stop()
//                        if (!mView.isDestroyed) {
//                            mView.tvOpenTime.text = "--:--"
//                            mView.tvOpenCount.text = ("- - - -" + "期开奖结果   ")
//                            mView.tvCloseTime.text = "--:--"
//                            mView.setVisible(mView.tvOpenCodePlaceHolder)
//                        }
//                        getNewResult(lotteryId)
//                    }
//                }
//            }
//        }
//
//    }


    fun getNewResult(lotteryId: String) {
        handler = Handler()
        runnable = Runnable {
            getLotteryOpenCode(lotteryId)
        }
        handler?.postDelayed(runnable, 3000)
    }


    private fun setContainerCode(lotteryId: String?, code: String?) {
        when (lotteryId) {
            "8" -> {
                val tbList = code?.split(",") as ArrayList<String>
                tbList.add(6, "+")
                LotteryTypeSelectUtil.addOpenCode(
                    mView,
                    mView.linLotteryOpenCode,
                    tbList,
                    lotteryId
                )
            }
            else -> {
                LotteryTypeSelectUtil.addOpenCode(
                    mView,
                    mView.linLotteryOpenCode,
                    code?.split(","),
                    lotteryId
                )
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun setTime(millisUntilFinished: Long) {
        val day: Long = millisUntilFinished / (1000 * 60 * 60 * 24)/*单位 天*/
        val hour: Long =
            (millisUntilFinished - day * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60)/*单位 时*/
        val minute: Long =
            (millisUntilFinished - day * (1000 * 60 * 60 * 24) - hour * (1000 * 60 * 60)) / (1000 * 60)/*单位 分*/
        val second: Long =
            (millisUntilFinished - day * (1000 * 60 * 60 * 24) - hour * (1000 * 60 * 60) - minute * (1000 * 60)) / 1000 /*单位 秒*/
        if (mView.tvOpenTime != null) {
            when {
                day > 0 -> mView.tvOpenTime.text =
                    dataLong(day) + "天" + dataLong(hour) + ":" + dataLong(minute) + ":" + dataLong(
                        second
                    )
                hour > 0 -> mView.tvOpenTime.text =
                    dataLong(hour) + ":" + dataLong(minute) + ":" + dataLong(second)
                else -> mView.tvOpenTime.text = dataLong(minute) + ":" + dataLong(second)
            }
        }
    }


    //封盘倒计时
    private var isOpenCode = false  //封盘 判断
    private var timerClose: CountDownTimer? = null
    private fun countDownTimerClose(issue:String,nextIssue:String,lotteryId: String,millisUntilFinished: Long) {
        if (timerClose != null) timerClose?.cancel()
        val timeCountDown = millisUntilFinished * 1000
        timerClose = object : CountDownTimer(timeCountDown, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                val day: Long = millisUntilFinished / (1000 * 60 * 60 * 24)/*单位 天*/
                val hour: Long =
                    (millisUntilFinished - day * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60)/*单位 时*/
                val minute: Long =
                    (millisUntilFinished - day * (1000 * 60 * 60 * 24) - hour * (1000 * 60 * 60)) / (1000 * 60)/*单位 分*/
                val second: Long =
                    (millisUntilFinished - day * (1000 * 60 * 60 * 24) - hour * (1000 * 60 * 60) - minute * (1000 * 60)) / 1000 /*单位 秒*/
                if (mView.tvCloseTime != null) {
                    when {
                        day > 0 -> mView.tvCloseTime.text =
                            dataLong(day) + "天" + dataLong(hour) + ":" + dataLong(minute) + ":" + dataLong(
                                second
                            )
                        hour > 0 -> mView.tvCloseTime.text =
                            dataLong(hour) + ":" + dataLong(minute) + ":" + dataLong(second)
                        else -> mView.tvCloseTime.text = dataLong(minute) + ":" + dataLong(second)
                    }
                }
            }

            override fun onFinish() {
                isOpenCode = false
                mView.betFragment?.lotteryInfo(issue, nextIssue,lotteryId,false)
                if (mView.tvCloseTime != null) mView.tvCloseTime.text = "--:--"
            }
        }
        if (timerClose != null) timerClose?.start()
    }

    // 这个方法是保证时间两位数据显示，如果为1点时，就为01
    private fun dataLong(c: Long): String {
        return if (c >= 10)
            c.toString()
        else "0$c"
    }
}