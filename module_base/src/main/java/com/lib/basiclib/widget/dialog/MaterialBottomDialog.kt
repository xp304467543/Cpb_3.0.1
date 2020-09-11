package com.lib.basiclib.widget.dialog

import android.content.Context
import com.google.android.material.bottomsheet.BottomSheetDialog

/**

 * @since 2019/1/3 23:10
 *
 * 底部弹出的对话框基类，用于上层包裹，如果要使用，请设置[setContentView]方法
 */
class MaterialBottomDialog(context: Context) : BottomSheetDialog(context)