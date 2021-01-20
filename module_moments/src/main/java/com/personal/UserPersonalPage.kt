package com.personal

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.ViewGroup
import android.widget.LinearLayout
import com.customer.data.UserInfoSp
import com.customer.data.home.HomeApi
import com.customer.data.moments.PersonalApi
import com.customer.data.moments.UserPageGift
import com.customer.data.moments.UserPageResponse
import com.glide.GlideUtil
import com.lib.basiclib.base.activity.BaseNavActivity
import com.lib.basiclib.base.round.RoundTextView
import com.lib.basiclib.utils.FastClickUtil
import com.lib.basiclib.utils.TimeUtils
import com.lib.basiclib.utils.ToastUtils
import com.lib.basiclib.utils.ViewUtils
import com.moment.R
import com.xiaojinzi.component.anno.RouterAnno
import com.customer.component.dialog.GlobalDialog
import cuntomer.constant.UserConstant
import kotlinx.android.synthetic.main.act_presonal_expert.*
import kotlinx.android.synthetic.main.act_presonal_user.*
import kotlinx.android.synthetic.main.act_presonal_user.imgBack
import kotlinx.android.synthetic.main.act_presonal_user.imgUserPhoto
import kotlinx.android.synthetic.main.act_presonal_user.tvUserDescription
import kotlinx.android.synthetic.main.act_presonal_user.tvUserName
import kotlin.random.Random

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/27
 * @ Describe
 *
 */
@RouterAnno(host = "Moment", path = "UserPersonalPage")
class UserPersonalPage : BaseNavActivity() {

    var attentionNum = 0

    override fun getContentResID() = R.layout.act_presonal_user

    override fun isShowToolBar() = false

    override fun isSwipeBackEnable() = true

    override fun initContentView() {
        if (intent != null) {
            if (UserInfoSp.getUserId().toString() == intent.getStringExtra(UserConstant.FOLLOW_ID) ?: "0") setGone(btUserAttention)
        }
    }

    override fun initData() {
        if (intent != null) {
            showPageLoadingDialog()
            getUserInfo(intent.getStringExtra(UserConstant.FOLLOW_ID) ?: "0")
        }
    }


    private fun initInfo(data: UserPageResponse) {
        attentionNum = data.fans!!
        GlideUtil.loadCircleImage(this,data.avatar,imgUserPhoto,true)
        tvUserName.text = data.nickname
        tvUserDescription.text = data.profile
        tvAttention.text = data.follow.toString()
        tvGiftNum.text = data.all_gift
        tvZan.text = data.zan
        tvFans.text = data.fans.toString()
        tvUserId.text = data.unique_id
        initGiftView(data.gift)
        when (data.vip) {
            "1" -> imgLevel.background = ViewUtils.getDrawable(R.mipmap.vip1)
            "2" -> imgLevel.background = ViewUtils.getDrawable(R.mipmap.vip2)
            "3" -> imgLevel.background = ViewUtils.getDrawable(R.mipmap.vip3)
            "4" -> imgLevel.background = ViewUtils.getDrawable(R.mipmap.vip4)
            "5" -> imgLevel.background = ViewUtils.getDrawable(R.mipmap.vip5)
            "6" -> imgLevel.background = ViewUtils.getDrawable(R.mipmap.vip6)
            "7" -> imgLevel.background = ViewUtils.getDrawable(R.mipmap.vip7)
        }
        when (data.gender) {
            "1" -> userSex.text = "男"
            "2" -> userSex.text = "女"
            else -> userSex.text = "未知"
        }
        tvEnterTime.text = TimeUtils.longToDateString(data.created.toLong())
        if (data.is_follow) {
            btUserAttention.background = ViewUtils.getDrawable(R.drawable.button_grey_background)
            btUserAttention.setTextColor(ViewUtils.getColor(R.color.grey_e6))
            btUserAttention.text = "已关注"
        }
        hidePageLoadingDialog()
    }

    override fun initEvent() {
        imgBack.setOnClickListener {
            finish()
        }
        btUserAttention.setOnClickListener {
            if (!FastClickUtil.isFastClick()) {
                if (!UserInfoSp.getIsLogin()) {
                    GlobalDialog.notLogged(this, false)
                    return@setOnClickListener
                }
                HomeApi.attentionAnchorOrUser("", intent.getStringExtra(UserConstant.FOLLOW_ID)!!) {
                    onSuccess {
                        if (!it.isFollow) {
                            btUserAttention.background =
                                ViewUtils.getDrawable(R.drawable.button_red_background)
                            btUserAttention.text = "+ 关注"
                            btUserAttention.setTextColor(ViewUtils.getColor(R.color.white))
                            if (attentionNum > 0) attentionNum -= 1
                            tvFans.text = attentionNum.toString()
                        } else {
                            btUserAttention.background =
                                ViewUtils.getDrawable(R.drawable.button_grey_background)
                            btUserAttention.setTextColor(ViewUtils.getColor(R.color.grey_e6))
                            btUserAttention.text = "已关注"
                            attentionNum += 1
                            tvFans.text = attentionNum.toString()
                        }
                    }

                    onFailed {
                        GlobalDialog.showError(this@UserPersonalPage, it)
                    }
                }
            }
        }

        imgBack.setOnClickListener {
            finish()
        }
    }

    //送出的礼物
    private fun initGiftView(data: List<UserPageGift>?) {
        val color = arrayOf("#FFEFED", "#FFF4E3", "#E9F8FF", "#E9F8FF", "#FFF4E3", "#FFEFED")
        if (!data.isNullOrEmpty()) {
            //往容器内添加TextView数据
            val layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            layoutParams.setMargins(15, 20, 15, 5)
            if (flowLayout != null) {
                flowLayout.removeAllViews()
            }
            for (i in data) {
                val tv = RoundTextView(this)
                tv.setPadding(28, 10, 28, 10)
                val builder = SpannableStringBuilder(i.gift_name + "  ")
                val length = builder.length
                builder.append("x" + i.num)
                builder.setSpan(
                    ForegroundColorSpan(ViewUtils.getColor(R.color.color_FF513E)),
                    length,
                    builder.length,
                    Spannable.SPAN_EXCLUSIVE_INCLUSIVE
                )
                tv.text = builder
                tv.maxEms = 10
                tv.setSingleLine()
                tv.textSize = 12f
                tv.setTextColor(ViewUtils.getColor(R.color.color_333333))
                tv.delegate.cornerRadius = 15
                tv.layoutParams = layoutParams
                tv.delegate.backgroundColor = Color.parseColor(color[Random.nextInt(5)])
                flowLayout.addView(tv, layoutParams)
            }
        }
    }

    private fun getUserInfo(follId: String) {
        PersonalApi.getUserPage(follId) {
            onSuccess {
                initInfo(it)
            }
            onFailed { ToastUtils.showToast(it.getMsg().toString()) }
        }
    }
}