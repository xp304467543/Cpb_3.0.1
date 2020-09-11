package com.fh.app

import com.fh.BuildConfig
import com.lib.basiclib.app.BaseApplication
import com.lib.basiclib.base.xui.XUI
import com.lib.basiclib.utils.DebugUtils
import com.player.customize.AliPlayerFactory
import com.player.customize.player.VideoView
import com.player.customize.player.VideoViewConfig
import com.player.customize.player.VideoViewManager
import com.rxnetgo.RxNetGo
import com.tencent.bugly.Bugly
import com.tencent.smtt.sdk.QbSdk
import com.tencent.smtt.sdk.QbSdk.PreInitCallback
import com.xiaojinzi.component.Component
import com.xiaojinzi.component.Config
import com.xiaojinzi.component.impl.application.ModuleManager
import com.xiaojinzi.component.support.RxErrorIgnoreUtil


/**
 *
 * @ Author  QinTian
 * @ Date  2020/5/18
 * @ Describe
 *
 */
class CpbApplication : BaseApplication() {

    companion object {
        private lateinit var mInstance: CpbApplication
        fun getInstance(): BaseApplication {
            return mInstance
        }
    }

    override fun onCreate() {
        super.onCreate()
        mInstance = this
    }

    override fun getCurrentEnvModel() = AppConfigConstant.ENV_DEVELOP

    override fun isEnvSwitch() = AppConfigConstant.ENV_SWITCH

    override fun isEnvLog() = AppConfigConstant.ENV_LOG

    override fun initMainProcess() {
        initComponent()
        initBugly()
        initUi()
        initNetWork()
        iniPlayer()
        initQbSdk()
    }

    private fun initBugly() {
//        // 获取当前包名
//        val packageName: String = this.packageName
//        // 获取当前进程名
//        val processName = IpUtils.getProcessName(Process.myPid())
//        // 设置是否为上报进程
//        val strategy = UserStrategy(this)
//        strategy.isUploadProcess = processName == null || processName == packageName
//        Bugly.init(getApplication(), "00b7077bbf", false,strategy)
        Bugly.init(getApplication(), "398c48dd2a", false)
    }


    private fun initComponent() {
        // 初始化组件化相关
        Component.init(
            BuildConfig.DEBUG,
            Config.with(this)
                .defaultScheme("router") // 使用内置的路由重复检查的拦截器, 如果为 true,
                // 那么当两个相同的路由发生在指定的时间内后一个路由就会被拦截
                .useRouteRepeatCheckInterceptor(true) // 1000 是默认的, 表示相同路由拦截的时间间隔
                .routeRepeatCheckDuration(1000) // 是否打印日志提醒你哪些路由使用了 Application 为 Context 进行跳转
                .tipWhenUseApplication(true) // 开启启动优化, 必须配套使用 Gradle 插件
                // 如果是 false 请直接略过下一步 Gradle 的配置
                .optimizeInit(true)
                // 自动加载所有模块, 打开此开关后下面无需手动注册了
                // 但是这个依赖 optimizeInit(true) 才会生效
                .autoRegisterModule(true) // 1.7.9+
                .build()
        )
        // 注册其他业务模块,注册的字符串是上面各个业务模块配置在 build.gradle 中的 HOST
//        ModuleManager.getInstance().registerArr("App", "Home")
        // 如果你依赖了 rx 版本, 请加上这句配置. 忽略一些不想处理的错误
        // 如果不是 rx 的版本, 请忽略
        RxErrorIgnoreUtil.ignoreError()

        if (BuildConfig.DEBUG) {
            ModuleManager.getInstance().check()
        }
    }

    private fun initUi() {
        XUI.init(this) //初始化UI框架
        XUI.debug(BuildConfig.DEBUG)  //开启UI框架调试日志
    }

    private fun initNetWork() {
        RxNetGo.getInstance().init(this).debug(DebugUtils.isDebugModel())
    }

    private fun iniPlayer() {
        VideoViewManager.setConfig(
            VideoViewConfig.newBuilder()
                .setLogEnabled(BuildConfig.DEBUG) //调试的时候请打开日志，方便排错
                .setPlayerFactory(AliPlayerFactory.create())
                .setEnableOrientation(false)
                .setScreenScaleType(VideoView.SCREEN_SCALE_16_9)
                .build()
        )
    }


    private fun initQbSdk() {
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        val cb: PreInitCallback = object : PreInitCallback {
            override fun onViewInitFinished(arg0: Boolean) {
                // TODO Auto-generated method stub
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
//                LogUtils.e("initQbSdk---------> onViewInitFinished is $arg0")
            }

            override fun onCoreInitFinished() {
                // TODO Auto-generated method stub
            }
        }
        //x5内核初始化接口
        QbSdk.initX5Environment(this, cb)
    }
}