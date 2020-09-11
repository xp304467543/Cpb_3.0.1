package com.customer.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.provider.MediaStore
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import java.io.OutputStream
import java.util.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/25
 * @ Describe
 *
 */
object ZingUtil {
    /**
     * 创建二维码
     *
     * @param content   content
     * @param widthPix  widthPix
     * @param heightPix heightPix
     * @param logoBm    logoBm
     * @return 二维码
     */
    fun createQRCode(content: String?, widthPix: Int, heightPix: Int, logoBm: Bitmap?): Bitmap? {
        try {
            if (content == null || "" == content) {
                return null
            }
            // 配置参数
            val hints: MutableMap<EncodeHintType, Any?> = HashMap()
            hints[EncodeHintType.CHARACTER_SET] = "utf-8"
            // 容错级别
            hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.H
            // 图像数据转换，使用了矩阵转换
            val bitMatrix = QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, widthPix,
                heightPix, hints)
            val pixels = IntArray(widthPix * heightPix)
            // 下面这里按照二维码的算法，逐个生成二维码的图片，
            // 两个for循环是图片横列扫描的结果
            for (y in 0 until heightPix) {
                for (x in 0 until widthPix) {
                    if (bitMatrix[x, y]) {
                        pixels[y * widthPix + x] = -0x1000000
                    } else {
                        pixels[y * widthPix + x] = -0x1
                    }
                }
            }
            // 生成二维码图片的格式，使用ARGB_8888
            var bitmap = Bitmap.createBitmap(widthPix, heightPix, Bitmap.Config.ARGB_8888)
            bitmap!!.setPixels(pixels, 0, widthPix, 0, 0, widthPix, heightPix)
            if (logoBm != null) {
                bitmap = addLogo(bitmap, logoBm)
            }
            //必须使用compress方法将bitmap保存到文件中再进行读取。直接返回的bitmap是没有任何压缩的，内存消耗巨大！
            return bitmap
        } catch (e: WriterException) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 在二维码中间添加Logo图案
     */
    private fun addLogo(src: Bitmap?, logo: Bitmap?): Bitmap? {
        if (src == null) {
            return null
        }
        if (logo == null) {
            return src
        }
        //获取图片的宽高
        val srcWidth = src.width
        val srcHeight = src.height
        val logoWidth = logo.width
        val logoHeight = logo.height
        if (srcWidth == 0 || srcHeight == 0) {
            return null
        }
        if (logoWidth == 0 || logoHeight == 0) {
            return src
        }
        //logo大小为二维码整体大小的1/5
        val scaleFactor = srcWidth * 1.0f / 5 / logoWidth
        var bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888)
        try {
            val canvas = Canvas(bitmap!!)
            canvas.drawBitmap(src, 0f, 0f, null)
            canvas.scale(scaleFactor, scaleFactor, srcWidth / 2.toFloat(), srcHeight / 2.toFloat())
            canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2.toFloat(), (srcHeight - logoHeight) / 2.toFloat(), null)
            canvas.save()
            canvas.restore()
        } catch (e: Exception) {
            bitmap = null
            e.stackTrace
        }
        return bitmap
    }


    /**
     * 保存图片到picture 目录，Android Q适配，最简单的做法就是保存到公共目录，不用SAF存储
     *
     */
    fun addPictureToAlbum(
        context: Context,
        bitmap: Bitmap,
        fileName: String?
    ): Uri? {
        val contentValues = ContentValues()
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, fileName)
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        val uri = context.contentResolver
            .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        var outputStream: OutputStream? = null
        try {
            outputStream = context.contentResolver.openOutputStream(uri!!)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream!!.close()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            return null
        }
        return uri
    }

}