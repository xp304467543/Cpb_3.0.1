package com.personal

import android.annotation.SuppressLint
import android.os.CountDownTimer
import androidx.recyclerview.widget.LinearLayoutManager
import com.customer.component.dialog.GlobalDialog
import com.customer.data.UserInfoSp
import com.customer.data.home.HomeApi
import com.customer.data.moments.ExpertPageHistory
import com.customer.data.moments.ExpertPageInfo
import com.glide.GlideUtil
import com.google.gson.JsonParser
import com.lib.basiclib.base.mvp.BaseMvpActivity
import com.lib.basiclib.base.recycle.BaseRecyclerAdapter
import com.lib.basiclib.base.recycle.RecyclerViewHolder
import com.lib.basiclib.utils.FastClickUtil
import com.lib.basiclib.utils.TimeUtils
import com.lib.basiclib.utils.ViewUtils
import com.moment.R
import com.xiaojinzi.component.anno.RouterAnno
import cuntomer.constant.UserConstant
import kotlinx.android.synthetic.main.act_presonal_expert.*
import java.math.BigDecimal

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/27
 * @ Describe
 *
 */
@RouterAnno(host = "Moment", path = "ExpertPersonalPage")
class ExpertPersonalPage : BaseMvpActivity<ExpertPersonalPagePresenter>() {

    private var limit = "10"

    private lateinit var adapter: ExpertHistoryAdapter

    private var lottery_id = "-1"

    override fun attachView() = mPresenter.attachView(this)

    override fun attachPresenter() = ExpertPersonalPagePresenter()

    override fun isSwipeBackEnable() = true

    override fun isOverride() = true

    override val layoutResID = R.layout.act_presonal_expert


    override fun initContentView() {

        adapter = ExpertHistoryAdapter()
        expertHistory.adapter = adapter
        expertHistory.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        imgBack.setOnClickListener { finish() }
    }

    override fun initData() {
        if (intent != null) {
            showPageLoadingDialog()
            lottery_id = intent.getStringExtra(UserConstant.FOLLOW_lottery_ID) ?: "-1"
            if (lottery_id == "-1") mPresenter.getExpertInfo(
                intent.getStringExtra(UserConstant.FOLLOW_ID)!!,
                ""
            ) else mPresenter.getExpertInfo(
                intent.getStringExtra(UserConstant.FOLLOW_ID)!!,
                lottery_id
            )

        }
    }


    @SuppressLint("SetTextI18n")
    fun initExpert(data: ExpertPageInfo?) {
        if (data != null) {
            if (lottery_id == "-1") lottery_id = data.lottery_id
            mPresenter.getExpertHistory(
                intent.getStringExtra(UserConstant.FOLLOW_ID)!!,
                lottery_id,
                limit
            )
            mPresenter.getNextTime(lottery_id)
            GlideUtil.loadCircleImage(this, data.avatar, imgUserPhoto, true)
            tvUserName.text = data.nickname
            tvUserDescription.text = data.profile
            tvExpertAttention.text = data.following
            tvExpertFans.text = data.followers
            tvExpertZan.text = data.like
            tvExpertWinRate.text =
                BigDecimal(data.win_rate).setScale(4, BigDecimal.ROUND_HALF_UP).multiply(
                    BigDecimal(100)
                ).stripTrailingZeros().toPlainString() + " %"
            tvExpertWinPre.text =
                BigDecimal(data.profit_rate).setScale(4, BigDecimal.ROUND_HALF_UP).multiply(
                    BigDecimal(100)
                ).stripTrailingZeros().toPlainString() + " %"
            tvExpertWinAdd.text = data.winning
            tvLotteryName.text = data.lottery_name
            if (data.is_followed == "1") {
                btAttentionExpert.background =
                    ViewUtils.getDrawable(R.drawable.button_grey_background)
                btAttentionExpert.setTextColor(ViewUtils.getColor(R.color.grey_e6))
                btAttentionExpert.text = "已关注"
            }
        }

    }

    override fun initEvent() {
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rb1 -> limit = "10"
                R.id.rb2 -> limit = "30"
                R.id.rb3 -> limit = "60"
                R.id.rb4 -> limit = "100"
            }
            mPresenter.getExpertHistory(
                intent.getStringExtra(UserConstant.FOLLOW_ID)!!,
                lottery_id,
                limit
            )
        }


        btAttentionExpert.setOnClickListener {
            if (!FastClickUtil.isFastClick()) {
                if (!UserInfoSp.getIsLogin()) {
                    GlobalDialog.notLogged(this, false)
                    return@setOnClickListener
                }
                HomeApi.attentionExpert(intent.getStringExtra(UserConstant.FOLLOW_ID) ?: "0") {
                    onSuccess {
                        val json =
                            JsonParser.parseString(it).asJsonObject.get("is_followed").asString
                        if (json != "1") {
                            btAttentionExpert.background =
                                ViewUtils.getDrawable(R.drawable.button_blue_background)
                            btAttentionExpert.text = "+ 关注"
                            btAttentionExpert.setTextColor(ViewUtils.getColor(R.color.white))
                        } else {
                            btAttentionExpert.background =
                                ViewUtils.getDrawable(R.drawable.button_grey_background)
                            btAttentionExpert.setTextColor(ViewUtils.getColor(R.color.grey_e6))
                            btAttentionExpert.text = "已关注"
                        }
                    }
                    onFailed { GlobalDialog.showError(this@ExpertPersonalPage, it) }
                }
            }
        }
        imgBack.setOnClickListener {
            finish()
        }
    }


    fun initExpertHistory(data: List<ExpertPageHistory>) {
        if (data.isNullOrEmpty()) tvDescription.text = "暂无历史记录！" else {
            adapter.refresh(data)
        }
    }


    // ===== 倒计时 =====
    var timer: CountDownTimer? = null

    fun countDownTime(millisUntilFinished: String, lotteryId: String) {
        tvOpenTimePersonal.text = getString(R.string.lottery_next)
        if (timer != null) timer?.cancel()
        val timeCountDown = millisUntilFinished.toLong() * 1000
        timer = object : CountDownTimer(timeCountDown, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                val day: Long = millisUntilFinished / (1000 * 60 * 60 * 24)/*单位 天*/
                val hour: Long =
                    (millisUntilFinished - day * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60)/*单位 时*/
                val minute: Long =
                    (millisUntilFinished - day * (1000 * 60 * 60 * 24) - hour * (1000 * 60 * 60)) / (1000 * 60)/*单位 分*/
                val second: Long =
                    (millisUntilFinished - day * (1000 * 60 * 60 * 24) - hour * (1000 * 60 * 60) - minute * (1000 * 60)) / 1000 /*单位 秒*/
                if (tvOpenTimePersonal != null) {
                    when {
                        day > 0 -> tvOpenTimePersonal.text =
                            "距下次  " + dataLong(day) + "天" + dataLong(hour) + ":" + dataLong(minute) + ":" + dataLong(
                                second
                            )
                        hour > 0 -> tvOpenTimePersonal.text =
                            "距下次  " + dataLong(hour) + ":" + dataLong(minute) + ":" + dataLong(
                                second
                            )
                        else -> tvOpenTimePersonal.text =
                            "距下次  " + dataLong(minute) + ":" + dataLong(second)
                    }
                }
            }

            override fun onFinish() {
                tvOpenTimePersonal!!.text = "距下次  ----"
                mPresenter.getNextTime(lotteryId)
            }
        }
        if (timer != null) timer?.start()
    }

    // 这个方法是保证时间两位数据显示，如果为1点时，就为01
    fun dataLong(c: Long): String {
        return if (c >= 10)
            c.toString()
        else "0$c"
    }

    inner class ExpertHistoryAdapter : BaseRecyclerAdapter<ExpertPageHistory>() {
        override fun getItemLayoutId(viewType: Int) = R.layout.adapter_expert_history

        override fun bindData(holder: RecyclerViewHolder, position: Int, data: ExpertPageHistory?) {
            holder.text(R.id.tvIssue, data?.issue)
            holder.text(R.id.tvTime, TimeUtils.longToDateStringTime(data?.created?.toLong() ?: 0))
            if (data?.open_code != "") setText(
                R.id.tvOpenCode,
                data?.open_code
            ) else holder.text(R.id.tvOpenCode, "待开奖")
            holder.text(R.id.tvMethod, data?.method)
            holder.text(R.id.tvCode, data?.code)
            val text = when (data?.is_right) {
                "0" -> "未开奖"
                "1" -> "无"
                "2" -> "输"
                "3" -> "赢"
                else -> "无"
            }
            holder.text(R.id.tvResult, text)
        }
    }

}