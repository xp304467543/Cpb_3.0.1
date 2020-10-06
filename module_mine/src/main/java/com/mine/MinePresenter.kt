package com.mine

import android.annotation.SuppressLint
import com.customer.data.urlCustomer
import com.hwangjr.rxbus.RxBus
import com.lib.basiclib.base.mvp.BaseMvpPresenter
import com.customer.data.mine.MineApi
import com.customer.component.dialog.GlobalDialog
import com.customer.data.UserInfoSp
import com.customer.data.mine.MineVipList
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
                    mView.tvMineUserOther.text =
                        it.following + "关注   |   " + it.followers + "粉丝   |   " + it.like + "获赞"
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
    fun getIsSetPayPassWord() {
        MineApi.getIsSetPayPass {
            onSuccess {
                UserInfoSp.putIsSetPayPassWord(true)
            }
        }
    }

    fun getUserVip() {
        MineApi.getUserVip {
            if (mView.isActive()) {
                onSuccess {
                    UserInfoSp.setVipLevel(it.vip)
                    when (it.vip) {
                        "1" -> mView.imgMineLevel.setBackgroundResource(R.mipmap.vip_1)
                        "2" -> mView.imgMineLevel.setBackgroundResource(R.mipmap.vip_2)
                        "3" -> mView.imgMineLevel.setBackgroundResource(R.mipmap.vip_3)
                        "4" -> mView.imgMineLevel.setBackgroundResource(R.mipmap.vip_4)
                        "5" -> mView.imgMineLevel.setBackgroundResource(R.mipmap.vip_5)
                        "6" -> mView.imgMineLevel.setBackgroundResource(R.mipmap.vip_6)
                        "7" -> mView.imgMineLevel.setBackgroundResource(R.mipmap.vip_7)
                        else -> mView.imgMineLevel.setBackgroundResource(0)
                    }
                    mView.setVisible(mView.imgMineLevel)
                }
                onFailed {
                    mView.setGone(mView.imgMineLevel)
                    UserInfoSp.setVipLevel("0")
                }
            }
        }
    }

    //获取余额
    @SuppressLint("SetTextI18n")
    fun getUserBalance() {
        if (mView.isActive()) {
            if (mView.isActive()){
                MineApi.getUserBalance {
                    onSuccess {
                        mView.tvBalance?.text = if (it.balance.toString() == "0")  "0.00" else it.balance.toString()
                    }
                    onFailed {error ->
//                        GlobalDialog.showError(mView.requireActivity(), it)
                        if (error.getCode() == 2001 || error.getCode() == 401 || error.getCode() == 2000 || error.getMsg().toString().contains("请登录")) {
                            GlobalDialog.notLogged(mView.requireActivity(), false)
                        }
                    }
                }
            }
        }
    }

    //获取钻石
    fun getUserDiamond() {
        MineApi.getUserDiamond {
            if (mView.isActive()){
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
                        mView.containerMessageCenter.showNewMessage(true)
                    } else {
                        mView.containerMessageCenter.showNewMessage(false)
                    }
                    mView.msg1 = it.countList.`_$0`
                    mView.msg2 = it.countList.`_$2`
                    mView.msg3 = it.countList.`_$3`
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