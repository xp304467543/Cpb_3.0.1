package com.fh.provider

import android.annotation.SuppressLint
import androidx.core.content.FileProvider

/**
 *
 * @ Author  QinTian
 * @ Date  2020/7/31
 * @ Describe Android 7.0 禁止在应用外部公开 file:// URI，所以我们必须使用 content:// 替代
 *
 */

@SuppressLint("Registered")
class CpbProvider : FileProvider()
