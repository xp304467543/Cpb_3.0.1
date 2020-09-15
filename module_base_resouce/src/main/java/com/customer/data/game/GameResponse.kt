package com.customer.data.game

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 *
 * @ Author  QinTian
 * @ Date  2020/9/13
 * @ Describe
 *
 */


@Parcelize
data class GameAll(val name:String?,val list:List<GameAllChild0>?): Parcelable


@Parcelize
data class GameAllChild0(val name:String?,val list:ArrayList<GameAllChild1>?,val type:String?,val id:String?,val img_url:String?): Parcelable

@Parcelize
data class GameAllChild1(val type:String?,val id:String?,val img_url:String?,val name:String?,val itemType:Int = 0): Parcelable