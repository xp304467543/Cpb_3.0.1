package cuntomer.constant

/**
 * 全局Api的域名管理，Path放到直接单独的Api文件中
 */
object ApiConstant {

    /**
     * 服务器获取
     */
    var API_URL_DEV_LIVE_S:String? = null //davis

    var API_URL_DEV_USER_S:String? = null //用户中心

    var API_MOMENTS_FORM_S:String? = null  //圈子

    var API_LOTTERY_S:String? = null //竞猜

    var API_VIDEO:String? = null //视频

    /**
     * 正式
     */
    const val API_URL_DEV_Main = "https://www.lgadmin561.com/" //davis

    const val API_URL_DEV_OTHER = "http://api.lgadmin561.com/userinfo/" //用户中心

    const val API_MOMENTS_MAIN = "http://api.lgadmin561.com/forum/"  //圈子

    const val API_LOTTERY_BET_MAIN = "http://lott.zhibojk.com/" //竞猜
    /**
     * 测试
     */
    const val API_URL_DEV = "http://www.cpbh5.com/"

    const val API_URL_DEV_OTHER_TEST = "http://121.127.228.235:18000/userinfo/"

    const val API_MOMENTS_TEST = "http://121.127.228.235:18000/forum/"

    const val API_LOTTERY_BET_TEST = "http://18.136.235.23:18306/"

    const val API_VIDEO_TEST ="http://47.244.212.147:18306"

    const val  isTest = true



    /**
     *  key
     */
    const val TEST_KEY = "6dd5eea85c2c5fac"

    const val MAIN_KEY = "21d29648eae80e73"

}