package com.bet

import com.customer.ApiRouter
import com.customer.data.game.GameApi
import com.lib.basiclib.base.mvp.BaseMvpPresenter
import com.lib.basiclib.utils.ToastUtils
import com.xiaojinzi.component.impl.Router

/**
 *
 * @ Author  QinTian
 * @ Date  2020/9/13
 * @ Describe
 *
 */
class GameMainChildOtherFragmentPresenter : BaseMvpPresenter<GameMainChildOtherFragment>() {


    fun getChessGame(game_id:String){
        GameApi.get060(game_id) {
            if (mView.isAdded){
                onSuccess {
                    Router.withApi(ApiRouter::class.java).toGlobalWeb(it.url.toString())
                    mView.hidePageLoadingDialog()
                }
                onFailed {
                    ToastUtils.showToast(it.getMsg())
                    mView.hidePageLoadingDialog()
                }
            }

        }
    }

    fun getAg(){
        GameApi.getAg  {
            if (mView.isAdded){
                onSuccess {
                    Router.withApi(ApiRouter::class.java).toGlobalWeb(it.url.toString())
                    mView.hidePageLoadingDialog()
                }
                onFailed {
                    ToastUtils.showToast(it.getMsg())
                    mView.hidePageLoadingDialog()
                }
            }

        }
    }


    fun getAgDz(){
        GameApi.getAgDZ {
            if (mView.isAdded){
                onSuccess {
                    Router.withApi(ApiRouter::class.java).toGlobalWeb(it.url.toString())
                    mView.hidePageLoadingDialog()
                }
                onFailed {
                    ToastUtils.showToast(it.getMsg())
                    mView.hidePageLoadingDialog()
                }
            }

        }
    }
}