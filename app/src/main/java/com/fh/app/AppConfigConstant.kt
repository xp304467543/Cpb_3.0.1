package com.fh.app

import com.fh.BuildConfig


/**
 * 项目的配置常量，和一些第三方的Key等
- ENV_LOG：是否支持打印日志
- ENV_SWITCH：是否支持测试环境和正式环境切换
- ENV_DEVELOP：打包环境，1为正式环境，2为灰度环境，3为测试环境，4为开发环境
- VERSION_NAME：版本名
- VERSION_CODE：版本号
 */
object AppConfigConstant {

    // 开发环境
    val ENV_DEVELOP: Int = BuildConfig.ENV_DEVELOP
    val ENV_SWITCH: Boolean = BuildConfig.ENV_SWITCH
    val ENV_LOG: Boolean = BuildConfig.ENV_LOG



}