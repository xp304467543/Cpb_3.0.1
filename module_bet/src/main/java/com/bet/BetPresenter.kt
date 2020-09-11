package com.bet

import com.customer.data.LineCheck
import com.customer.data.home.HomeApi
import com.lib.basiclib.base.mvp.BaseMvpPresenter
import com.lib.basiclib.utils.LogUtils
import com.lib.basiclib.utils.ToastUtils
import kotlinx.android.synthetic.main.fragment_bet.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/5/22
 * @ Describe
 *
 */
class BetPresenter : BaseMvpPresenter<BetFragment>() {


    fun getUrl() {
        HomeApi.getLotteryUrl {
            if (mView.isActive()){
                onSuccess {
                    LogUtils.e("--????-get")
                    if (mView.isActive()) {
                        mView.lotteryUrl = it.bettingArr?.get(0)?:it.betting
                        mView.gameUrl = it.chessArr?.get(0)?:it.gameUrl
                        if (!it.bettingArr.isNullOrEmpty()){
                            mView.listCheck = arrayListOf()
                                for ( (index,i1) in it.bettingArr!!.withIndex()){
                                    if (index ==0 )
                                        mView.listCheck!!.add(LineCheck(i1,true))
                                    else
                                        mView.listCheck!!.add(LineCheck(i1,false))
                            }
                        }
                        if (!it.chessArr.isNullOrEmpty()){
                            mView.listCheck2 = arrayListOf()
                            for ( (index,i2) in it.chessArr!!.withIndex()){
                                if (index ==0 )
                                    mView.listCheck2!!.add(LineCheck(i2,true))
                                else
                                    mView.listCheck2!!.add(LineCheck(i2,false))
                            }
                        }
                        if (mView.initCurrent ==1){
                            mView.loadPing(it.bettingArr?.get(0)?:it.betting)
                            if (mView.lotteryWeb!=null)mView.lotteryWeb?.loadUrl(it.bettingArr?.get(0)?:it.betting)
                            mView.isLoad = true
                        } else{
                            mView.loadPing(it.chessArr?.get(0)?:it.gameUrl)
                            if (mView.gameWeb!=null) mView.gameWeb?.loadUrl(it.chessArr?.get(0)?:it.gameUrl)
                            mView.isLoad2 = true
                        }
                    }
                }
                onFailed {
                    ToastUtils.showToast(it.getMsg())
                }
            }
        }
    }
}