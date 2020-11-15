package com.mine.children

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.customer.component.dialog.DialogLoginExpired
import com.customer.data.MineUserDiamond
import com.customer.data.mine.MineApi
import com.hwangjr.rxbus.RxBus
import com.king.zxing.CaptureActivity
import com.king.zxing.DecodeFormatManager
import com.king.zxing.camera.FrontLightMode
import com.lib.basiclib.utils.ToastUtils
import com.mine.R
import kotlinx.android.synthetic.main.act_scan.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/11/10
 * @ Describe
 *
 */
class MineScanAct : CaptureActivity() {


    override fun getLayoutId() = R.layout.act_scan

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //获取CaptureHelper，里面有扫码相关的配置设置
        captureHelper.playBeep(false)//播放音效
            .vibrate(true)//震动
            .supportVerticalCode(false)//支持扫垂直条码，建议有此需求时才使用。
            .decodeFormats(DecodeFormatManager.QR_CODE_FORMATS)//设置只识别二维码会提升速度
//                .framingRectRatio(0.9f)//设置识别区域比例，范围建议在0.625 ~ 1.0之间。非全屏识别时才有效
//                .framingRectVerticalOffset(0)//设置识别区域垂直方向偏移量，非全屏识别时才有效
//                .framingRectHorizontalOffset(0)//设置识别区域水平方向偏移量，非全屏识别时才有效
            .frontLightMode(FrontLightMode.AUTO)//设置闪光灯模式
            .tooDarkLux(45f)//设置光线太暗时，自动触发开启闪光灯的照度值
            .brightEnoughLux(100f)//设置光线足够明亮时，自动触发关闭闪光灯的照度值
            .continuousScan(false)//是否连扫
            .supportLuminanceInvert(true)//是否支持识别反色码（黑白反色的码），增加识别率

        scanBack.setOnClickListener {
            finish()
        }
    }

    override fun getIvTorchId() = 0

    override fun onResultCallback(result: String?): Boolean {
        try {
            val id = result?.split("=")?.get(1)
            MineApi.scanLogin(id ?: "","1") {
                onSuccess {
                    val intent = Intent(this@MineScanAct, MineScanResultAct::class.java)
                    intent.putExtra("resultCode", id)
                    startActivity(intent)
                    finish()
                }
                onFailed {
                    //过期
                    val dialog =  DialogLoginExpired(this@MineScanAct)
                    dialog.setOnDismissListener {
                        captureHelper.restartPreviewAndDecode()
                        dialog.dismiss()
                    }
                    dialog.show()
                }
            }
        } catch (e: Exception) {
            ToastUtils.showToast("获取qrid失败")
        }

        return true
    }



}