package com.bet

import com.customer.ApiRouter
import com.customer.data.game.GameApi
import com.lib.basiclib.base.mvp.BaseMvpPresenter
import com.lib.basiclib.utils.ToastUtils
import com.xiaojinzi.component.impl.Router
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 *
 * @ Author  QinTian
 * @ Date  2020/9/13
 * @ Describe
 *
 */
class GameMainChildFragmentPresenter : BaseMvpPresenter<GameMainChildFragment>() {

    fun getAllGame(isUpDateTop:Boolean=false){
        if (mView.isActive()){
            val uiScope = CoroutineScope(Dispatchers.Main)

            uiScope.launch {

                val getLotteryType = async { GameApi.getAllGame() }

                val resultGetLotteryType = getLotteryType.await()

                resultGetLotteryType.onSuccess {
                    if (it.isNotEmpty()) {
                        mView.initHot(it[0],isUpDateTop)
                    }
                }
            }

        }
    }


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

    fun getBgFish(game_id: String) {
        GameApi.getBgFish(game_id) {
            if (mView.isAdded) {
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

    fun getBgSx() {
        GameApi.getBgSx {
            if (mView.isAdded) {
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

    fun getSb(game_id: String) {
        GameApi.getSb(game_id) {
            if (mView.isAdded) {
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

    fun getKy(game_id: String) {
        GameApi.getKy(game_id) {
            if (mView.isAdded) {
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