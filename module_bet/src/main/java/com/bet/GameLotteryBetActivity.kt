package com.bet

import android.content.Intent
import com.customer.data.lottery.LotteryTypeResponse
import com.customer.data.mine.MineBankList
import com.lib.basiclib.base.mvp.BaseMvpActivity
import com.lib.basiclib.base.xui.widget.picker.widget.OptionsPickerView
import com.lib.basiclib.base.xui.widget.picker.widget.builder.OptionsPickerBuilder
import com.lib.basiclib.utils.FastClickUtil
import com.lib.basiclib.utils.StatusBarUtils
import com.xiaojinzi.component.anno.RouterAnno
import kotlinx.android.synthetic.main.act_game_lottery_bet.*

/**
 * @ Author  QinTian
 * @ Date  2020/9/14
 * @ Describe
 */
@RouterAnno(host = "BetMain", path = "gameLotteryBet")
class GameLotteryBetActivity : BaseMvpActivity<GameLotteryBetActivityPresenter>() {

    var lotteryId = "-1"

    var index = 0

    var isInitBottom = false

    private var gameOptions: OptionsPickerView<String>? = null

    override fun attachView() = mPresenter.attachView(this)

    override fun attachPresenter() = GameLotteryBetActivityPresenter()

    override fun isOverride() = true

    override fun isSwipeBackEnable() = true

    override val layoutResID = R.layout.act_game_lottery_bet

    override fun initContentView() {
        StatusBarUtils.setStatusBarHeight(gameBetStateView)
        tvGameLotteryName.text = intent.getStringExtra("gameLotteryName")
        lotteryId = intent.getStringExtra("gameLotteryId") ?: "-1"
    }

    override fun initData() {
        mPresenter.getLottery()
        mPresenter.getLotteryOpenCode(lotteryId)
    }

    override fun initEvent() {
        imgGameBack.setOnClickListener { finish() }
        tvGameLotteryName.setOnClickListener {
            gameOptions?.show()
        }
        imgGameTools.setOnClickListener {
            if (!FastClickUtil.isFastClick()) {
                val intent = Intent(this, GameLotteryBetToolsActivity::class.java)
                intent.putExtra("gameLotteryToolsId", lotteryId)
                startActivity(intent)
            }
        }
    }


    //底部弹框
    fun showPickerView(list: List<LotteryTypeResponse>) {
        val final = arrayListOf<String>()
        for (res in list) {
            final.add(res.cname ?: "未知")
        }
        index = final.indexOf(intent.getStringExtra("gameLotteryName") ?: "")
        gameOptions = OptionsPickerBuilder(this) { _, options1, _, _ ->
            tvGameLotteryName.text = list[options1].cname
            lotteryId = list[options1].lottery_id ?: "-1"
            index = options1
            false
        }
            .setTitleText("")
            .setSelectOptions(index)
            .setContentTextSize(18)
            .build()
        gameOptions?.setPicker(final)
    }
}