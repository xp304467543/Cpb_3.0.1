package com.home.live.children.util.task

import android.content.Context
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.AnimationUtils
import com.home.R

/**
 *
 * @ Author  QinTian
 * @ Date  2020/9/1
 * @ Describe
 *
 */
object AnimUtils {

    /**
     * 获取Vip入场动画
     *
     */
    fun getInAnimation(context: Context, view: View) {
        view.visibility = View.VISIBLE
        val anim = AnimationUtils.loadAnimation(context, R.anim.vip_in)
        view.startAnimation(anim)

    }

    /**
     * 获取Vip出场动画
     *
     */
    fun getOutAnimation(context: Context, view: View) {
        val anim = AnimationUtils.loadAnimation(context, R.anim.vip_out) as AnimationSet
        anim.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationRepeat(animation: Animation?) {
            }
            override fun onAnimationStart(animation: Animation?) {
            }
            override fun onAnimationEnd(animation: Animation?) {
                view.visibility = View.GONE
            }
        })
        view.startAnimation(anim)
    }

    /**
     * 获取直播主播开奖入场动画
     *
     */
    fun getLotteryInAnimation(context: Context, view: View) {
        view.visibility = View.VISIBLE
        val anim = AnimationUtils.loadAnimation(context, R.anim.live_lottery_in)
        view.startAnimation(anim)

    }

    /**
     * 获取直播主播开奖出场动画
     *
     */
    fun getLotteryOutAnimation(context: Context, view: View) {
        val anim = AnimationUtils.loadAnimation(context, R.anim.live_lottery_out) as AnimationSet
        anim.setAnimationListener(object :Animation.AnimationListener{
            override fun onAnimationRepeat(animation: Animation?) {
            }
            override fun onAnimationStart(animation: Animation?) {
            }
            override fun onAnimationEnd(animation: Animation?) {
                view.visibility = View.GONE
            }
        })
        view.startAnimation(anim)
    }


}