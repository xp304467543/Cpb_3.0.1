package com.mine

import android.annotation.SuppressLint
import android.graphics.Typeface
import com.customer.component.dialog.GlobalDialog
import com.customer.data.UserInfoSp
import com.customer.data.mine.MineApi
import com.customer.data.urlCustomer
import com.hwangjr.rxbus.RxBus
import com.lib.basiclib.base.mvp.BaseMvpPresenter
import com.lib.basiclib.utils.ToastUtils
import kotlinx.android.synthetic.main.act_mine_vip_info.*
import kotlinx.android.synthetic.main.fragment_mine.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/5/22
 * @ Describe
 *
 */
class MinePresenter : BaseMvpPresenter<MineFragment>() {


    //获取用户信息
    @SuppressLint("SetTextI18n")
    fun getUserInfo() {
        MineApi.getUserInfo {
            onSuccess {
                if (mView.isSupportVisible) {
                    mView.spText1?.setCenterTopString(it.following)
                    mView.spText2?.setCenterTopString(it.followers)
                    mView.spText3?.setCenterTopString(it.like)
                    mView.tvMineMove?.text = "" + it.free_watch_nums + "/" + it.sum_watch_nums
                    mView.tvMineReport?.text = "推广码 /" + it.market_code
                    UserInfoSp.setVipLevel(it.vip)
                    UserInfoSp.setNobleLevel(it.noble)
                    when (it.vip) {
                        0 -> mView.setGone(mView.imgMineLevelVip)
                        1 -> mView.imgMineLevelVip?.setImageResource(R.mipmap.vip1)
                        2 -> mView.imgMineLevelVip?.setImageResource(R.mipmap.vip2)
                        3 -> mView.imgMineLevelVip?.setImageResource(R.mipmap.vip3)
                        4 -> mView.imgMineLevelVip?.setImageResource(R.mipmap.vip4)
                        5 -> mView.imgMineLevelVip?.setImageResource(R.mipmap.vip5)
                        6 -> mView.imgMineLevelVip?.setImageResource(R.mipmap.vip6)
                        7 -> mView.imgMineLevelVip?.setImageResource(R.mipmap.vip7)
                        8 -> mView.imgMineLevelVip?.setImageResource(R.mipmap.vip8)
                    }
                    when (it.noble) {
                        0 -> mView.setGone(mView.imgMineLevel)
                        1 -> mView.imgMineLevel?.setImageResource(R.mipmap.svip_1)
                        2 -> mView.imgMineLevel?.setImageResource(R.mipmap.svip_2)
                        3 -> mView.imgMineLevel?.setImageResource(R.mipmap.svip_3)
                        4 -> mView.imgMineLevel?.setImageResource(R.mipmap.svip_4)
                        5 -> mView.imgMineLevel?.setImageResource(R.mipmap.svip_5)
                        6 -> mView.imgMineLevel?.setImageResource(R.mipmap.svip_6)
                        7 -> mView.imgMineLevel?.setImageResource(R.mipmap.svip_7)
                    }
                    if (!UserInfoSp.getUserProfile()
                            .isNullOrEmpty() && UserInfoSp.getUserProfile() != "null"
                    ) {
                        mView.tvMineProfile.text = it.profile
                    } else mView.tvMineProfile.text = "说点什么吧..."

                }
            }
        }

    }

    //查询是否设置支付密码
    fun getIsSetPayPassWord(boolean: Boolean = false) {
        MineApi.getIsSetPayPass {
            onSuccess {
                UserInfoSp.putIsSetPayPassWord(true)
            }
            onFailed {
                if (boolean) {
                    if (it.getCode() == 10) {
                        ToastUtils.showToast(it.getMsg())
                    } else {
                        GlobalDialog.noSetPassWord(mView.requireActivity())
                    }
                }
            }
        }
    }

//    fun getUserVip() {
//        MineApi.getUserVip {
//            if (mView.isActive()) {
//                onSuccess {
//                    UserInfoSp.setVipLevel(it.vip)
//                    when (it.vip) {
//                        "1" -> mView.imgMineLevel.setBackgroundResource(R.mipmap.vip1)
//                        "2" -> mView.imgMineLevel.setBackgroundResource(R.mipmap.vip2)
//                        "3" -> mView.imgMineLevel.setBackgroundResource(R.mipmap.vip3)
//                        "4" -> mView.imgMineLevel.setBackgroundResource(R.mipmap.vip4)
//                        "5" -> mView.imgMineLevel.setBackgroundResource(R.mipmap.vip5)
//                        "6" -> mView.imgMineLevel.setBackgroundResource(R.mipmap.vip6)
//                        "7" -> mView.imgMineLevel.setBackgroundResource(R.mipmap.vip7)
//                        else -> mView.imgMineLevel.setBackgroundResource(0)
//                    }
//                    mView.setVisible(mView.imgMineLevel)
//                }
//                onFailed {
//                    mView.setGone(mView.imgMineLevel)
//                    UserInfoSp.setVipLevel("0")
//                }
//            }
//        }
//    }

    //获取余额
    @SuppressLint("SetTextI18n")
    fun getUserBalance() {
        if (mView.isActive()) {
            if (mView.isActive()) {
                MineApi.getUserBalance {
                    onSuccess {
                        mView.tvBalance?.text =
                            if (it.balance.toString() == "0") "0.00" else it.balance.toString()
                    }
                    onFailed { error ->
                        GlobalDialog.showError(mView.requireActivity(), error)
                    }
                }
            }
        }
    }

    //获取钻石
    fun getUserDiamond() {
        MineApi.getUserDiamond {
            if (mView.isActive()) {
                onSuccess {
                    RxBus.get().post(it)
                }
                onFailed {
//                getUserDiamondFailedListener?.invoke(it)
                }
            }
        }
    }

    //获取新消息
    fun getNewMsg() {
        MineApi.getIsNewMessage {
            if (mView.isActive()) {
                onSuccess {
                    if (it.msgCount > 0) {
//                        mView.containerMessageCenter.showNewMessage(true)
                    } else {
//                        mView.containerMessageCenter.showNewMessage(false)
                    }
                    mView.msg1 = it.countList.`_$0`
                    mView.msg2 = it.countList.`_$2`
                    mView.msg3 = it.countList.`_$3`
                    mView.msg4 = it.countList.`_$5`
                }
            }
        }
    }

    fun getCustomerUrl() {
        MineApi.getLotteryUrl {
            onSuccess {
                UserInfoSp.putCustomer(it.customer ?: urlCustomer)
            }
            onFailed {
                UserInfoSp.putCustomer(urlCustomer)
            }
        }
    }

}