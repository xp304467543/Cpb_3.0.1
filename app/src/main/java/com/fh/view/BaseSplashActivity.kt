package com.fh.view

import android.os.Bundle
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.lib.basiclib.base.xui.utils.Utils

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/29
 * @ Describe
 *
 */
abstract class BaseSplashActivity : AppCompatActivity() {
    protected var mWelcomeLayout: LinearLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initSplashView(getSplashImgResId())
        onCreateActivity()
    }

    private fun initView() {
        mWelcomeLayout = LinearLayout(this)
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        mWelcomeLayout!!.layoutParams = params
        mWelcomeLayout!!.orientation = LinearLayout.VERTICAL
        setContentView(mWelcomeLayout)
    }

    /**
     * 初始化启动界面
     *
     * @param splashImgResId 背景资源图片资源ID
     */
    protected fun initSplashView(splashImgResId: Int) {
        if (splashImgResId != 0) {
            Utils.setBackground(this, mWelcomeLayout, splashImgResId)
        }
    }

    /**
     * 初始化启动界面背景图片
     *
     * @return 背景图片资源ID
     */
    protected fun getSplashImgResId(): Int {
        return 0
    }

    /**
     * activity启动后的初始化
     */
    protected abstract fun onCreateActivity()

    /**
     * 启动页结束后的动作
     */
    protected abstract fun onSplashFinished()

    /**
     * @return 启动页持续的时间
     */
    protected fun getSplashDurationMillis(): Long {
        return DEFAULT_SPLASH_DURATION_MILLIS.toLong()
    }

    /**
     * 开启过渡
     *
     * @param enableAlphaAnim 是否启用渐近动画
     */
    protected fun startSplash(enableAlphaAnim: Boolean) {
        if (enableAlphaAnim) {
            startSplashAnim(AlphaAnimation(0.2f, 1.0f))
        } else {
            startSplashAnim(AlphaAnimation(1.0f, 1.0f))
        }
    }

    /**
     * 开启引导过渡动画
     *
     * @param anim
     */
    private fun startSplashAnim(anim: Animation) {
        Utils.checkNull(anim, "Splash Animation can not be null")
        anim.duration = getSplashDurationMillis()
        anim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationRepeat(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                onSplashFinished()
            }
        })
        mWelcomeLayout!!.startAnimation(anim)
    }

    override fun onDestroy() {
        Utils.recycleBackground(mWelcomeLayout)
        super.onDestroy()
    }

    companion object {
        /**
         * 默认启动页过渡时间
         */
        private const val DEFAULT_SPLASH_DURATION_MILLIS = 8000
    }
}
