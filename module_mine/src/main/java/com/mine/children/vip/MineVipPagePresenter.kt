package com.mine.children.vip

import android.annotation.SuppressLint
import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.TypefaceSpan
import com.customer.data.mine.MineApi
import com.glide.GlideUtil
import com.lib.basiclib.base.mvp.BaseMvpPresenter
import com.lib.basiclib.utils.ToastUtils
import com.mine.R
import kotlinx.android.synthetic.main.act_mine_vip_page.*

/**
 *
 * @ Author  QinTian
 * @ Date  1/19/21
 * @ Describe
 *
 */
class MineVipPagePresenter : BaseMvpPresenter<MineVipPageActivity>() {


    @SuppressLint("SetTextI18n")
    fun getPageInfo() {
        MineApi.getVipCardInfo {
            onSuccess {
                if (mView.isActive()) {
                    mView.hidePageLoadingDialog()
                    mView.unDateBanner(it.user_growth?.vip?:0)
                    GlideUtil.loadCircleImage(mView, it.user_growth?.avatar, mView.userPhoto, true)
                    mView.userNickName?.text = it.user_growth?.nickname
                    mView.userVipLevel?.text = it.user_growth?.vip.toString()
                    mView.currentVip = it.user_growth?.vip ?: 0
                    mView.tvCurrentVip?.text = "VIP" + it.user_growth?.vip
                    mView.tvNextVip?.text = "VIP" + it.user_growth?.next_vip
                    val n = it.user_growth?.total_recharge ?: 1.0
                    val x = it.user_growth?.next_cz_total ?: 1.0
                    mView.user_level.progress = ((n / x) * 100).toInt()

                    val spannableString3 = SpannableString(
                        "当前存款 (元) ：" + it.user_growth?.total_recharge +
                                "  (" + it.user_growth?.total_recharge + "/" + it.user_growth?.next_cz_total + ")"
                    )
                    spannableString3.setSpan(
                        ForegroundColorSpan(Color.parseColor("#FFC27D")),
                        0,
                        ("当前存款 (元) ：" + it.user_growth?.total_recharge).length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    spannableString3.setSpan(
                        ForegroundColorSpan(Color.parseColor("#ffffff")),
                        ("当前存款 (元) ：" + it.user_growth?.total_recharge).length,
                        spannableString3.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    spannableString3.setSpan(
                        RelativeSizeSpan(0.8f),
                        ("当前存款 (元) ：" + it.user_growth?.total_recharge).length,
                        spannableString3.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    mView.tvTitle1.text = spannableString3


                    val spannableString4 = SpannableString(
                        "当前流水 (元) ：" + it.user_growth?.total_flow +
                                "  (" + it.user_growth?.total_flow + "/" + it.user_growth?.next_flow_total + ")"
                    )
                    spannableString4.setSpan(
                        ForegroundColorSpan(Color.parseColor("#FFC27D")),
                        0,
                        ("当前流水 (元) ：" + it.user_growth?.total_flow).length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    spannableString4.setSpan(
                        ForegroundColorSpan(Color.parseColor("#ffffff")),
                        ("当前流水 (元) ：" + it.user_growth?.total_flow).length,
                        spannableString4.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    spannableString4.setSpan(
                        RelativeSizeSpan(0.8f), ("当前流水 (元) ：" + it.user_growth?.total_flow).length,
                        spannableString4.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    mView.tvTitle2.text = spannableString4
                    when (it.user_growth?.vip) {
                        0 -> mView.imgVipLevel?.setImageResource(R.mipmap.vip0)
                        1 -> mView.imgVipLevel?.setImageResource(R.mipmap.vip1)
                        2 -> mView.imgVipLevel?.setImageResource(R.mipmap.vip2)
                        3 -> mView.imgVipLevel?.setImageResource(R.mipmap.vip3)
                        4 -> mView.imgVipLevel?.setImageResource(R.mipmap.vip4)
                        5 -> mView.imgVipLevel?.setImageResource(R.mipmap.vip5)
                        6 -> mView.imgVipLevel?.setImageResource(R.mipmap.vip6)
                        7 -> mView.imgVipLevel?.setImageResource(R.mipmap.vip7)
                        8 -> mView.imgVipLevel?.setImageResource(R.mipmap.vip8)
                    }
                }
            }
            onFailed {
                mView.hidePageLoadingDialog()
                ToastUtils.showToast(it.getMsg())
            }
        }
    }
}