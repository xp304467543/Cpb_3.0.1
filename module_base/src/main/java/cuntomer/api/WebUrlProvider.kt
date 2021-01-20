package cuntomer.api
import android.text.TextUtils.isEmpty
import com.lib.basiclib.utils.LogUtils
import com.rxnetgo.utils.JsonUtils
import cuntomer.constant.ApiConstant
import java.lang.reflect.Type

/**
 * Web页面的接口管理
 */
object WebUrlProvider {

    /**
     * 服务器获取
     */
    var API_URL_WEB_SOCKET_MAIN_S: String? = null

    var ALL_URL_WEB_SOCKET_MAIN_S: String? = null

    //    生产环境： wss://www.cpbadmin.com/wss
    //    测试环境： ws://www.cpbh5.com/wss
    private const val API_URL_WEB_SOCKET = "ws://52.220.233.230/wss"

    private const val API_URL_WEB_SOCKET_MAIN = "wss://api.zhibojk.com/wss"


    //    生产环境： wss://www.cpbadmin.com/wss_notice 暂定
    //    测试环境： ws://www.cpbh5.com/wss_notice

    private const val ALL_URL_WEB_SOCKET = "ws://52.220.233.230/wss_notice"

    private const val ALL_URL_WEB_SOCKET_MAIN = "wss://api.zhibojk.com/wss_notice"

    fun getBaseUrl(): String {
        return if (ApiConstant.isTest) {
            API_URL_WEB_SOCKET
        } else {
            API_URL_WEB_SOCKET_MAIN_S ?: API_URL_WEB_SOCKET_MAIN
        }
    }

    fun getALLBaseUrl(): String {
        return if (ApiConstant.isTest) {
            ALL_URL_WEB_SOCKET
        } else {
            ALL_URL_WEB_SOCKET_MAIN_S ?: ALL_URL_WEB_SOCKET_MAIN
        }
    }

    fun <T> getData(message: String?, type: Type): T? {
        try {
            val content = message ?: ""
            return if (isEmpty(content)) null else {
                JsonUtils.fromJson(content, type)
            }
        } catch (e: Exception) {
            LogUtils.e(e.message)
        }
        return null
    }

}