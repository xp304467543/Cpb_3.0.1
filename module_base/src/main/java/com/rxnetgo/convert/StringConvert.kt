package com.rxnetgo.convert

import com.rxnetgo.convert.base.IConverter
import okhttp3.ResponseBody
import java.lang.reflect.Type

/**
 * 字符串转换器，直接获取ResponseBody的字符串返回即可
 */
class StringConvert : IConverter<String> {

    @Throws(Exception::class)
    override fun convertResponse(body: ResponseBody?): String? {
        val result = body?.string()
        body?.close()
        return result
    }

    override fun getType(): Type {
        return String::class.java
    }
}