package com.bet.game

import android.annotation.SuppressLint
import android.os.Build
import android.view.Gravity
import android.widget.RadioButton
import android.widget.RadioGroup
import com.bet.R
import com.customer.component.dialog.GlobalDialog
import com.customer.data.lottery.LotteryApi
import com.customer.data.mine.MineApi
import com.lib.basiclib.base.mvp.BaseMvpPresenter
import com.lib.basiclib.utils.ToastUtils
import com.lib.basiclib.utils.ViewUtils
import kotlinx.android.synthetic.main.game_bet_fragment1.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/9/15
 * @ Describe
 *
 */
class GameLotteryBetFragment1Presenter : BaseMvpPresenter<GameLotteryBetFragment1>() {


    fun getPlayList(id: String) {
        LotteryApi.getGuessPlayList(id) {
            if (mView.isAdded) {
                onSuccess {
                    mView.modifyData(it.toMutableList())
                }
            }
        }
    }

    @SuppressLint("ResourceType")
    fun getPlayMoney(){
        LotteryApi.lotteryBetMoney {
            if (mView.isAdded) {
                onSuccess {
                    if (mView.activity != null) {
                        mView.selectMoneyList = arrayListOf()
                        for ( res in it) {
                            val radio = RadioButton(mView.context)
                            radio.buttonDrawable = null
                            radio.background =
                                ViewUtils.getDrawable(R.drawable.lottery_bet_radio)
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) radio.setTextColor(
                                mView.context?.getColorStateList(R.drawable.color_radio_bet)
                            )
                            radio.textSize = ViewUtils.dp2px(2.5f)
                            radio.gravity = Gravity.CENTER
                            radio.text = res.play_sum_name
                            radio.id = res.play_sum_num ?: 0
                            radio.setOnCheckedChangeListener { buttonView, isChecked ->
                                if (isChecked) {
                                    mView.etGameBetPlayMoney.setText(buttonView.id.toString())
                                    mView.etGameBetPlayMoney.setSelection(buttonView.id.toString().length)
                                    mView.betTotalMoney = buttonView.id
                                    setTotal()
                                }
                            }
                            mView.selectMoneyList?.add(radio)
                            mView.radioGroupLayout?.addView(radio)
                            val params = radio.layoutParams as RadioGroup.LayoutParams
                            params.width = ViewUtils.dp2px(35)
                            params.height = ViewUtils.dp2px(35)
                            params.setMargins(ViewUtils.dp2px(5), 0, ViewUtils.dp2px(5), 0)
                            radio.layoutParams = params
                        }
                    }
                }
                onFailed {
                    ToastUtils.showToast(it.getMsg())
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
     fun setTotal() {
        if (mView.rightTop.contains("二中") || mView.rightTop.contains("三中")){
                mView.betCount = 1
            }else mView.betCount = if (mView.betList.isEmpty()) 1 else (mView.betList.size)
        mView.tvGameBetCount.text = "共" + (mView.betCount) + "注"
        mView.tvGameTotalMoney.text = (mView.betTotalMoney * (mView.betCount)).toString()
    }


    /**
     * 获取钻石
     */
    private fun getUserDiamond() {
        try {
            MineApi.getUserDiamond {
                if (mView.isAdded) {
                    onSuccess {
                        mView.userDiamond = it.diamond
                        if (mView.is_bl_play == 0) {
                            if ( mView.tvUserDiamond != null)  mView.tvUserDiamond.text =  mView.userDiamond
                        }
                    }
                }
            }
        } catch (e: Exception) {
            ToastUtils.showToast(e.toString())
        }

    }

    //获取余额
    @SuppressLint("SetTextI18n")
    fun getUserBalance() {
        MineApi.getUserBalance {
            onSuccess {
                mView.userBalance = it.balance.toString()
                if (mView.is_bl_play == 1) {
                    if ( mView.tvUserDiamond != null)  mView.tvUserDiamond.text =  mView.userBalance
                }
            }
            onFailed {
                GlobalDialog.showError(mView.requireActivity(), it)
            }
        }
        getUserDiamond()
    }

}