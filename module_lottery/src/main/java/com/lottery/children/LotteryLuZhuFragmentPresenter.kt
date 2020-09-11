package com.lottery.children

import android.os.Parcel
import android.os.Parcelable
import com.customer.utils.countdowntimer.lotter.LotteryConstant
import com.lib.basiclib.base.mvp.BaseMvpPresenter
import com.lib.basiclib.utils.TimeUtils
import com.customer.data.lottery.LotteryApi
import kotlinx.android.synthetic.main.child_fragment_lu_zhu.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/18
 * @ Describe
 *
 */

class LotteryLuZhuFragmentPresenter() : BaseMvpPresenter<LotteryLuZhuFragment>(), Parcelable {


    constructor(parcel: Parcel) : this() {
    }

    //获取露珠数据
    fun getLuZhuData(lotteryId: String, typeTitle: String,time:String = TimeUtils.getToday()) {
        mView.setVisible(mView.tvLuZhuPlaceHolder)
        LotteryApi.getLotteryLuZhu(lotteryId, typeTitle,time ) {
            onSuccess {
                if (mView.isActive()) {
                    mView.getLuZhuView(it,typeTitle)
                }
            }
            onFailed {
                if (mView.isActive()){
                    mView.setVisible(mView.tvHolder)
                    mView. setGone(mView.tvLuZhuPlaceHolder)
                }
            }
        }
    }


    fun getType(typeTitle: String): String {
        return when (typeTitle) {
            LotteryConstant.TYPE_2 -> LotteryConstant.TYPE_LUZHU_2
            LotteryConstant.TYPE_3 -> LotteryConstant.TYPE_LUZHU_3
            LotteryConstant.TYPE_5 -> LotteryConstant.TYPE_LUZHU_5
            LotteryConstant.TYPE_8 -> LotteryConstant.TYPE_LUZHU_8
            LotteryConstant.TYPE_9 -> LotteryConstant.TYPE_LUZHU_9
            LotteryConstant.TYPE_10 -> LotteryConstant.TYPE_LUZHU_10
            LotteryConstant.TYPE_11 -> LotteryConstant.TYPE_LUZHU_11
            LotteryConstant.TYPE_15 -> LotteryConstant.TYPE_LUZHU_15
            LotteryConstant.TYPE_16 -> LotteryConstant.TYPE_LUZHU_16
            LotteryConstant.TYPE_12 -> LotteryConstant.TYPE_LUZHU_12
            else -> "daxiao"
        }

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LotteryLuZhuFragmentPresenter> {
        override fun createFromParcel(parcel: Parcel): LotteryLuZhuFragmentPresenter {
            return LotteryLuZhuFragmentPresenter(parcel)
        }

        override fun newArray(size: Int): Array<LotteryLuZhuFragmentPresenter?> {
            return arrayOfNulls(size)
        }
    }

}