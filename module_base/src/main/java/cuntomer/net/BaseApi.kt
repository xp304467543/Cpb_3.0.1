package cuntomer.net

import cuntomer.constant.ApiConstant.API_LOTTERY_BET_MAIN
import cuntomer.constant.ApiConstant.API_LOTTERY_S
import cuntomer.constant.ApiConstant.API_LOTTERY_BET_TEST
import cuntomer.constant.ApiConstant.API_MOMENTS_MAIN
import cuntomer.constant.ApiConstant.API_MOMENTS_FORM_S
import cuntomer.constant.ApiConstant.API_MOMENTS_TEST
import cuntomer.constant.ApiConstant.API_URL_DEV
import cuntomer.constant.ApiConstant.API_URL_DEV_Main
import cuntomer.constant.ApiConstant.API_URL_DEV_LIVE_S
import cuntomer.constant.ApiConstant.API_URL_DEV_OTHER
import cuntomer.constant.ApiConstant.API_URL_DEV_USER_S
import cuntomer.constant.ApiConstant.API_URL_DEV_OTHER_TEST
import cuntomer.constant.ApiConstant.MAIN_KEY
import cuntomer.constant.ApiConstant.TEST_KEY
import cuntomer.constant.ApiConstant.isTest
import com.rxnetgo.RxNetGo
import cuntomer.constant.ApiConstant.API_VIDEO
import cuntomer.constant.ApiConstant.API_VIDEO_MAIN
import cuntomer.constant.ApiConstant.API_VIDEO_TEST

/**
 * 网络请求基类
 */
interface BaseApi {

    /**
     * 获取URL  davis
     */
    fun getBaseUrl(): String {
        return if (isTest) {
            API_URL_DEV
        } else {
            if (API_URL_DEV_LIVE_S.isNullOrEmpty()) {
                API_URL_DEV_Main
            } else "$API_URL_DEV_LIVE_S/"
        }
    }

    /**
     * 获取URL  bill
     */
    fun getBaseUrlMe(): String {
        return if (isTest) {
            API_URL_DEV_OTHER_TEST
        } else {
            if (API_URL_DEV_USER_S.isNullOrEmpty()) {
                API_URL_DEV_OTHER
            } else "$API_URL_DEV_USER_S/"
        }
    }

    fun getLotteryBet(): String {
        return if (isTest) {
            API_LOTTERY_BET_TEST
        } else {
            if (API_LOTTERY_S.isNullOrEmpty()) {
                API_LOTTERY_BET_MAIN
            }else "$API_LOTTERY_S/"
        }

    }

    /**
     * 影视
     */
    fun getMovie():String{
        return if (isTest) {
            API_VIDEO_TEST
        }else{
            if (API_VIDEO.isNullOrEmpty()) {
                API_VIDEO_MAIN
            }else "$API_VIDEO/"
        }
    }

    /**
     * 圈子
     */
    fun getBaseUrlMoments(): String {
        return if (isTest) {
            API_MOMENTS_TEST
        } else {
            if (API_MOMENTS_FORM_S.isNullOrEmpty()) {
                API_MOMENTS_MAIN
            }else "$API_MOMENTS_FORM_S/"
        }
    }

    fun getBase64Key(): String {
        return if (isTest) {
            TEST_KEY
        } else MAIN_KEY
    }

    /**
     * 服务器地址获取
     */
    fun getSystemApi(): RxNetGo {
        return RxNetGo.getInstance().getRetrofitService(getBaseUrl())
    }

    /**
     * 获取默认的Service
     * 需要子类绑定URL
     */
    fun getApi(): RxNetGo {
        return RxNetGo.getInstance().getRetrofitService(getBaseUrl())
    }

    /**
     * 登录其他的BaseUrl
     */
    fun getApiOther(): RxNetGo {
        return RxNetGo.getInstance().getRetrofitService(getBaseUrlMe())
    }


    /**
     * 圈子Api
     */
    fun getApiLottery(): RxNetGo {
        return RxNetGo.getInstance().getRetrofitService(getBaseUrlMoments())
    }

    /**
     * 开奖
     */
    fun getApiOpenLottery(): RxNetGo {
        return RxNetGo.getInstance().getRetrofitService(getLotteryBet())
    }

    /**
     * 电影
     */
    fun getApiMovie(): RxNetGo {
        return RxNetGo.getInstance().getRetrofitService(getMovie())
    }

    /**
     *  其他的BaseUrl 是否是测试库
     */
//    fun getApiOtherTest(): String = "forum"


    /**
     *  其他的User 是否是测试库
     */
//    fun getApiOtherUserTest(): String = "userinfo"


}