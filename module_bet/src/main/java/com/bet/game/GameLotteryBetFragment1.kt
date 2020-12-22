package com.bet.game

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bet.R
import com.customer.ApiRouter
import com.customer.base.BaseNormalFragment
import com.customer.component.dialog.BottomBetAccessDialog
import com.customer.component.dialog.DialogGlobalTips
import com.customer.component.dialog.GlobalDialog
import com.customer.data.ChangeLottery
import com.customer.data.HomeJumpToMine
import com.customer.data.LotteryResetDiamond
import com.customer.data.UserInfoSp
import com.customer.data.lottery.LotteryPlayListResponse
import com.customer.data.lottery.PlaySecData
import com.customer.data.lottery.PlaySecDataKj
import com.customer.data.lottery.PlayUnitData
import com.hwangjr.rxbus.RxBus
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.thread.EventThread
import com.lib.basiclib.base.recycle.BaseRecyclerAdapter
import com.lib.basiclib.base.recycle.RecyclerViewHolder
import com.lib.basiclib.utils.FastClickUtil
import com.lib.basiclib.utils.LogUtils
import com.lib.basiclib.utils.ToastUtils
import com.lib.basiclib.utils.ViewUtils
import com.xiaojinzi.component.impl.Router
import cuntomer.them.AppMode
import kotlinx.android.synthetic.main.game_bet_fragment1.*
import java.math.BigDecimal

/**
 *
 * @ Author  QinTian
 * @ Date  2020/9/15
 * @ Describe
 *
 */
class GameLotteryBetFragment1 : BaseNormalFragment<GameLotteryBetFragment1Presenter>() {

    var firstData: MutableList<LotteryPlayListResponse>? = null

    private var typeAdapter: GameTypeAdapter? = null

    var rightTopAdapter: RightTopAdapter? = null

    var rightTopXgcAdapter: RightTopXgcAdapter? = null //(香港彩)

    var lmAdapter: AdapterLm? = null //两面

    var kjAdapter: AdapterKj? = null //快捷

    var dan1D10Adapter: AdapterDan1D10? = null //单号1-10

    var gyhAdapter: AdapterGYH? = null //亚冠和

    var zhAdapter: AdapterZH? = null //整合

    var dmAdapter: AdapterDM? = null //单码

    var xgcTmAdapter: AdapterXgcTm? = null //特码(香港彩)

    var betList = mutableListOf<PlaySecData>()  //投注集合

    var selectMoneyList: ArrayList<RadioButton>? = null  //投注金额Raido

    var is_bl_play = 1 //是否余额投注，默认0不是，1是

    var issue = "-1" //奖期

    var nextIssue = "-1" //投注奖期

    var lotteryId = "-1" //彩种ID

    var isOpen = false //是否封盘

    var betTotalMoney = 10 //投注金额

    var betCount = 1 //注数

    var userDiamond = "-1" //用户钻石

    var userBalance = "-1" //用户余额

    private var minMonty = 1 //最小投注金额

    var rightTop = "-1"

    var currentLeft = "-1"

    override fun isRegisterRxBus() = true

    override fun attachView() = mPresenter.attachView(this)

    override fun attachPresenter() = GameLotteryBetFragment1Presenter()

    override fun initData() {
        lotteryId = arguments?.getString("gameBetLotteryId") ?: "0"
        mPresenter.getPlayList(lotteryId)
        mPresenter.getPlayMoney()
        mPresenter.getUserBalance()
    }

    override fun getLayoutRes() = R.layout.game_bet_fragment1

    override fun initContentView() {
        initRecycle()
        if (UserInfoSp.getAppMode() == AppMode.Normal) {
            setVisible(selectRadio)
        } else {
            setGone(selectRadio)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun initEvent() {
        rb_1?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                imgIcon.setBackgroundResource(R.mipmap.old_ic_ye_tz)
                tvEnd?.text = "元"
                if (tvUserDiamond != null) tvUserDiamond?.text = userBalance
                is_bl_play = 1
                minMonty = 1
                betTotalMoney = 1
                mPresenter.setTotal()
                etGameBetPlayMoney?.setText("1")
                etGameBetPlayMoney?.setSelection("1".length)
                clearRadio(true)
            }
        }
        rb_2?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                imgIcon?.setBackgroundResource(R.mipmap.ic_diamond_big)
                tvEnd?.text = "钻"
                if (tvUserDiamond != null) tvUserDiamond?.text = userDiamond
                is_bl_play = 0
                minMonty = 10
                betTotalMoney = 10
                mPresenter.setTotal()
                etGameBetPlayMoney?.setText("10")
                etGameBetPlayMoney?.setSelection("10".length)
                clearRadio(false)
            }
        }
        rb_1?.isChecked = true


        tvReset?.setOnClickListener {
            btReset()
        }
        tvUserDiamond?.setOnClickListener {
            if (!FastClickUtil.isFastClick()) {
                tvUserDiamond.text = "加载中"
                if (is_bl_play == 1) {
                    mPresenter.getUserBalance()
                } else mPresenter.getUserDiamond()
            }
        }

        tvBetSubmit?.setOnClickListener {
            if (!UserInfoSp.getIsLogin()) {
                GlobalDialog.notLogged(requireActivity())
                return@setOnClickListener
            }
            if (etGameBetPlayMoney.text.isNullOrEmpty()) {
                ToastUtils.showToast("请输入投注金额")
                return@setOnClickListener
            }
            if (is_bl_play == 0) {
                if (BigDecimal(etGameBetPlayMoney.text.toString()).compareTo(BigDecimal(10)) == -1) {
                    ToastUtils.showToast("请输入≥10的整数")
                    return@setOnClickListener
                }
            }
            if (!isOpen) {
                ToastUtils.showToast("当前期已封盘或已开奖，请购买下一期")
                return@setOnClickListener
            }
            if (betList.isEmpty()) {
                ToastUtils.showToast("未选择任何玩法或投注金额,请选择后再提交")
                return@setOnClickListener
            }
            //余额不足
            val m1 = BigDecimal(tvGameTotalMoney.text.toString())
            if (is_bl_play == 0) {
                if (etGameBetPlayMoney.text.toString().isNotEmpty())
                    if (userDiamond != "-1") {
                        val m2 = BigDecimal(userDiamond)
                        if (m2.compareTo(m1) == -1) {
                            val tips = context?.let { it1 ->
                                DialogGlobalTips(
                                    it1,
                                    "您的钻石余额不足,请充值",
                                    "兑换钻石",
                                    "取消",
                                    ""
                                )
                            }
                            tips?.setConfirmClickListener {
                                RxBus.get().post(HomeJumpToMine(true))
                                tips.dismiss()
                            }
                            tips?.show()
                            return@setOnClickListener
                        }
                    } else mPresenter.getUserBalance()
            } else {
                if (userBalance != "-1") {
                    val m2 = BigDecimal(userBalance)
                    if (m2.compareTo(m1) == -1) {
                        val tips = context?.let { it1 ->
                            DialogGlobalTips(
                                it1,
                                "您的余额不足,请充值",
                                "充值",
                                "取消",
                                ""
                            )
                        }
                        tips?.setConfirmClickListener {
                            Router.withApi(ApiRouter::class.java).toMineRecharge(0)
                            tips.dismiss()
                        }
                        tips?.show()
                        return@setOnClickListener
                    }
                } else mPresenter.getUserBalance()
            }
            when {
                rightTop.contains("二中二") -> {
                    if (betList.size < 2) {
                        ToastUtils.showToast("二中二必须选择2个号码")
                        return@setOnClickListener
                    } else {
                        val newBetList = arrayListOf<PlaySecData>()
                        val playClassName =
                            betList[0].play_class_name + "," + betList[1].play_class_name
                        val playClassCname =
                            betList[0].play_class_cname + "," + betList[1].play_class_cname
                        val bean = PlaySecData(
                            play_class_name = playClassName,
                            play_sec_name = betList[0].play_sec_name,
                            play_sec_cname = betList[0].play_sec_cname,
                            play_class_cname = playClassCname,
                            play_odds = betList[0].play_odds
                        )
                        newBetList.add(bean)
                        context?.let { it1 ->
                            BottomBetAccessDialog(
                                it1,
                                lotteryId,
                                rightTop,
                                nextIssue,
                                is_bl_play,
                                tvGameTotalMoney.text.toString(),
                                newBetList
                            ).show()
                        }
                        return@setOnClickListener
                    }
                }
                rightTop.contains("三中三") -> {
                    if (betList.size < 3) {
                        ToastUtils.showToast("三中三必须选择3个号码")
                        return@setOnClickListener
                    } else {
                        val newBetList = arrayListOf<PlaySecData>()
                        val playClassName =
                            betList[0].play_class_name + "," + betList[1].play_class_name + "," + betList[2].play_class_name
                        val playClassCname =
                            betList[0].play_class_cname + "," + betList[1].play_class_cname + "," + betList[2].play_class_cname
                        val bean = PlaySecData(
                            play_class_name = playClassName,
                            play_sec_name = betList[0].play_sec_name,
                            play_sec_cname = betList[0].play_sec_cname,
                            play_class_cname = playClassCname,
                            play_odds = betList[0].play_odds
                        )
                        newBetList.add(bean)
                        context?.let { it1 ->
                            BottomBetAccessDialog(
                                it1,
                                lotteryId,
                                rightTop,
                                nextIssue,
                                is_bl_play,
                                tvGameTotalMoney.text.toString(),
                                newBetList
                            ).show()
                        }
                        return@setOnClickListener
                    }
                }
            }
            LogUtils.e("====>1" + betList)
            context?.let { it1 ->
                BottomBetAccessDialog(
                    it1,
                    lotteryId,
                    rightTop,
                    nextIssue,
                    is_bl_play,
                    tvGameTotalMoney.text.toString(),
                    betList
                ).show()
            }
        }

        etGameBetPlayMoney.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(str: Editable?) {
                if (str != null && str.isNotEmpty()) {
                    if (is_bl_play == 0) {
                        if (minMonty > 1 && str.toString().toLong() < 10) {
//                            etGameBetPlayMoney.setText("10")
                            ToastUtils.showToast("请输入≥10的整数")
//                            betTotalMoney = 10
                        }
                    }
                    betTotalMoney = if (str.length > 9) {
                        etGameBetPlayMoney.setText(str.substring(0, 9)); //截取前x位
                        str.substring(0, 9).toInt()
                    } else str.toString().toInt()
                    etGameBetPlayMoney.requestFocus()
                    etGameBetPlayMoney.setSelection(etGameBetPlayMoney.text.length)
                    mPresenter.setTotal()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun clearRadio(boolean: Boolean) {
        if (selectMoneyList.isNullOrEmpty()) return
        radioGroupLayout?.removeAllViews()
        for ((index, radio) in selectMoneyList!!.withIndex()) {
            radio.isChecked = !boolean && index == 0
            radioGroupLayout.addView(radio)
            val params = radio.layoutParams as RadioGroup.LayoutParams
            params.width = ViewUtils.dp2px(35)
            params.height = ViewUtils.dp2px(35)
            params.setMargins(ViewUtils.dp2px(5), 0, ViewUtils.dp2px(5), 0)
            radio.layoutParams = params
        }
    }

    //当前开奖状态，封盘等等
    fun lotteryInfo(mIssue: String, nextIssue: String, mLotteryId: String, open: Boolean) {
        this.issue = mIssue
        this.lotteryId = mLotteryId
        this.isOpen = open
        this.nextIssue = nextIssue
    }

    private var leftLayoutManager: LinearLayoutManager? = null
    private fun initRecycle() {
        typeAdapter = GameTypeAdapter()
        rvGameBetType.adapter = typeAdapter
        leftLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvGameBetType.layoutManager = leftLayoutManager
    }


    fun modifyData(it: MutableList<LotteryPlayListResponse>) {
        if (it.isNullOrEmpty()) return
        firstData = it
        val typeList = arrayListOf<String>()
        for (item in firstData!!) {
            typeList.add(item.play_unit_name ?: "未知")
        }
        typeAdapter?.refresh(typeList)
        firstData?.get(0)?.let { it1 -> modifyContent(it1) }
    }

    private fun modifyContent(it: LotteryPlayListResponse) {
        if (!isActive()) return
        if (rvGameBetContent == null) return
        val name = if (lotteryId != "8") {
            it.play_unit_name.toString()
        } else it.play_unit_name + "xgc"
        when (name) {
            "两面", "两面xgc" -> {
                lmAdapter = AdapterLm()
                rvGameBetContent?.adapter = lmAdapter
                val layoutManager = GridLayoutManager(context, 12)
                rvGameBetContent?.layoutManager = layoutManager
                layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return when (lmAdapter?.getItemViewType(position)) {
                            lmAdapter?.ITEM_TYPE_HEAD -> 12
                            lmAdapter?.ITEM_TYPE_CONTENT_COUNT_4 -> 3
                            else -> 4
                        }
                    }
                }
            }
            "整合", "第一球", "第二球", "第三球", "第四球", "第五球", "正码1-6xgc", "色波xgc", "平特一肖尾数xgc", "特肖xgc" -> {
                zhAdapter = AdapterZH()
                rvGameBetContent?.adapter = zhAdapter
                val layoutManager = when (name) {
                    "色波xgc" -> GridLayoutManager(context, 12)
                    else -> GridLayoutManager(context, 4)
                }
                rvGameBetContent?.layoutManager = layoutManager
                layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return when (zhAdapter?.getItemViewType(position)) {
                            zhAdapter?.ITEM_TYPE_HEAD -> {
                                when (name) {
                                    "色波xgc" -> 12
                                    else -> 4
                                }
                            }
                            zhAdapter?.ITEM_TYPE_CONTENT_COUNT_5 -> {
                                when (name) {
                                    "色波xgc" -> 3
                                    else -> 1
                                }
                            }
                            zhAdapter?.ITEM_TYPE_CONTENT_COUNT_3 -> 4
                            else -> 1
                        }
                    }
                }
            }
            "快捷" -> {
                kjAdapter = AdapterKj()
                rvGameBetContent?.adapter = kjAdapter
                val layoutManager = GridLayoutManager(context, 10)
                rvGameBetContent?.layoutManager = layoutManager
                layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return when (kjAdapter?.getItemViewType(position)) {
                            kjAdapter?.ITEM_TYPE_CONTENT_COUNT_5 -> 2
                            kjAdapter?.ITEM_TYPE_CONTENT -> 10
                            else -> 5
                        }
                    }
                }
            }
            "单号1-10" -> {
                dan1D10Adapter = AdapterDan1D10()
                rvGameBetContent?.adapter = dan1D10Adapter
                val layoutManager = GridLayoutManager(context, 4)
                rvGameBetContent?.layoutManager = layoutManager
                layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return when (dan1D10Adapter?.getItemViewType(position)) {
                            dan1D10Adapter?.ITEM_TYPE_CONTENT_COUNT_5 -> 1
                            else -> 4
                        }
                    }
                }
            }
            "冠亚军组合" -> {
                gyhAdapter = AdapterGYH()
                rvGameBetContent?.adapter = gyhAdapter
                val layoutManager = GridLayoutManager(context, 4)
                rvGameBetContent?.layoutManager = layoutManager
            }
            "单码", "连码", "斗牛" -> {
                rightTopAdapter = RightTopAdapter()
                rvRightTop.setPadding(0, 0, 10, 0)
                rvRightTop.adapter = rightTopAdapter
                rvRightTop.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                dmAdapter = AdapterDM()
                rvGameBetContent?.adapter = dmAdapter
                rvGameBetContent?.layoutManager = GridLayoutManager(context, 4)
            }
            "特码xgc", "正码特xgc", "连码xgc", "连肖连尾xgc", "自选不中xgc" -> {
                rightTopXgcAdapter = RightTopXgcAdapter()
                rvRightTop.adapter = rightTopXgcAdapter
                rvRightTop.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                xgcTmAdapter = AdapterXgcTm()
                rvGameBetContent?.adapter = xgcTmAdapter
                val layoutManager = GridLayoutManager(context, 4)
                rvGameBetContent?.layoutManager = layoutManager
                layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return when (xgcTmAdapter?.getItemViewType(position)) {
                            xgcTmAdapter?.TYPE_1, xgcTmAdapter?.TYPE_2 -> 1
                            xgcTmAdapter?.TYPE_6 -> 2
                            else -> 4
                        }
                    }
                }
            }
            "正码xgc", "7色波xgc", "头尾数xgc", "总肖xgc", "正肖xgc", "五行xgc", "合肖xgc" -> {
                xgcTmAdapter = AdapterXgcTm()
                rvGameBetContent?.adapter = xgcTmAdapter
                val layoutManager = GridLayoutManager(context, 4)
                rvGameBetContent?.layoutManager = layoutManager
                layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return when (xgcTmAdapter?.getItemViewType(position)) {
                            xgcTmAdapter?.TYPE_1, xgcTmAdapter?.TYPE_2 -> 1
                            xgcTmAdapter?.TYPE_7 -> 2
                            else -> 4
                        }
                    }
                }
            }

        }
        modifyContentData(it, name)
    }

    private fun modifyContentData(data: LotteryPlayListResponse, type: String) {
        data.play_unit_data.let {
            if (it != null) {
                when (type) {
                    "两面", "两面xgc" -> {
                        val listData = arrayListOf<PlaySecData>()
                        for ((pos, item) in it.withIndex()) {
                            if (!item.play_sec_data.isNullOrEmpty()) {
                                for ((index, result) in item.play_sec_data.withIndex()) {
                                    if (index == 0) listData.add(
                                        PlaySecData(title = item.play_sec_cname, type = "lm_full")
                                    )  //一行占满
                                    if (result.play_sec_id != 0 && pos == 0) result.type =
                                        "lm_4"  //一行四个
                                    result.title = item.play_sec_cname
                                    listData.add(result)
                                }
                            }
                        }
                        repeat(10) {
                            listData.add(PlaySecData(title = "", type = "lm_full"))  //添加footer 一行占满
                        }
                        lmAdapter?.refresh(listData)
                    }
                    "整合", "第一球", "第二球", "第三球", "第四球", "第五球", "正码1-6xgc", "色波xgc", "平特一肖尾数xgc", "特肖xgc" -> {
                        val listData = arrayListOf<PlaySecData>()
                        for (item in it) {
                            if (!item.play_sec_data.isNullOrEmpty()) {
                                for ((index, result) in item.play_sec_data.withIndex()) {
                                    if (index == 0) listData.add(
                                        PlaySecData(
                                            title = item.play_sec_cname,
                                            type = "zh_full"
                                        )
                                    )  //一行占满
                                    if (type == "色波xgc") {
                                        if (result.play_sec_id != 0) {
                                            if (result.play_class_cname == "红" || result.play_class_cname == "绿" || result.play_class_cname == "蓝") {
                                                result.type = "zh_3"
                                                result.play_sec_cname = item.play_sec_cname
                                            } else {
                                                result.type = "zh_5"
                                                result.play_sec_cname = item.play_sec_cname
                                            }
                                        }
                                    } else {
                                        if (result.play_sec_id != 0) {
                                            result.type = "zh_5"
                                            result.play_sec_cname = item.play_sec_cname
                                        } //一行五个
                                    }
                                    listData.add(result)
                                }
                            }
                        }
                        repeat(10) {
                            listData.add(PlaySecData(title = "", type = "zh_full"))  //添加footer 一行占满
                        }
                        zhAdapter?.refresh(listData)
                    }
                    "快捷" -> {
                        val listData = arrayListOf<PlaySecDataKj>()
                        for ((index, item) in it.withIndex()) {
                            if (!item.play_sec_data.isNullOrEmpty()) {
                                listData.add(
                                    PlaySecDataKj(
                                        play_sec_merge_name = item.play_sec_merge_name,
                                        play_sec_cname = item.play_sec_cname,
                                        type = "kj_5"
                                    )
                                )
                                if (index == it.size - 1) {
                                    for (child in item.play_sec_data) {
                                        listData.add(
                                            PlaySecDataKj(
                                                play_sec_id = child.play_sec_id,
                                                play_sec_name = child.play_sec_name,
                                                play_class_id = child.play_class_id,
                                                play_class_name = child.play_class_name,
                                                play_class_cname = child.play_class_cname,
                                                play_odds = child.play_odds
                                            )
                                        )
                                    }
                                }
                            }
                        }
                        repeat(10) {
                            listData.add(PlaySecDataKj(type = "kj_full"))  //添加footer 一行占满
                        }
                        kjAdapter?.refresh(listData)
                    }
                    "单号1-10" -> {
                        val listData = arrayListOf<PlaySecData>()
                        for (item in it) {
                            if (!item.play_sec_data.isNullOrEmpty()) {
                                for ((index, result) in item.play_sec_data.withIndex()) {
                                    if (index == 0) listData.add(
                                        PlaySecData(
                                            title = item.play_sec_cname,
                                            type = "full"
                                        )
                                    )  //一行占满
                                    if (result.play_sec_id != 0) {
                                        result.title = item.play_sec_cname
                                        result.type = "dh_10_5"
                                    } //一行五个
                                    listData.add(result)
                                }
                            }
                        }
                        repeat(10) {
                            listData.add(PlaySecData(title = "", type = "full"))  //添加footer 一行占满
                        }
                        dan1D10Adapter?.refresh(listData)
                    }
                    "冠亚军组合" -> {
                        val listData = arrayListOf<PlaySecData>()
                        for (item in it) {
                            if (!item.play_sec_data.isNullOrEmpty()) {
                                for (result in item.play_sec_data) {
                                    result.playName = it[0].play_sec_cname
                                    listData.add(result)
                                }
                            }
                        }
                        gyhAdapter?.refresh(listData)
                    }
                    "单码", "连码", "斗牛" -> {
                        val listData = arrayListOf<PlayUnitData>()
                        for (item in it) {
                            if (!item.play_sec_data.isNullOrEmpty()) {
                                listData.add(item)
                            }
                        }
                        rightTopAdapter?.refresh(listData)
                    }
                    "特码xgc" -> {
                        val listData = arrayListOf<PlayUnitData>()
                        for ((num, item) in it.withIndex()) {
                            if (!item.play_sec_data.isNullOrEmpty()) {
                                for ((index, result) in item.play_sec_data.withIndex()) {
                                    if (result.play_class_name == "num_49") {
                                        result.type = "xgc_tm_full"
                                    } else {
                                        if (result.play_sec_id == 150 || result.play_sec_id == 151) {
                                            result.type = "xgc_tm_nor"
                                        } else if (result.play_sec_id == 147) result.type =
                                            "xgc_tm_ct"
                                    }
                                    result.play_sec_cname = it[num].play_sec_cname
                                }
                                if (item.play_sec_data.size == 65) {
                                    item.play_sec_data.add(
                                        PlaySecData(
                                            title = "",
                                            type = "xgc_full"
                                        )
                                    )  //添加footer 一行占满
                                }
                            }
                            listData.add(item)
                        }
                        rightTopXgcAdapter?.refresh(listData)
                    }
                    "正码xgc", "7色波xgc", "头尾数xgc", "总肖xgc", "正肖xgc", "五行xgc", "合肖xgc" -> {
                        val listData = arrayListOf<PlaySecData>()
                        for ((num, item) in it.withIndex()) {
                            if (!item.play_sec_data.isNullOrEmpty()) {
                                for ((index, result) in item.play_sec_data.withIndex()) {
                                    if (result.play_class_name == "num_49") {
                                        result.type = "xgc_tm_full"
                                    } else {
                                        if (result.play_sec_id == 152) {
                                            result.type = "xgc_tm_nor"
                                        } else if (result.play_sec_id == 149 || result.play_sec_id == 174 ||
                                            result.play_sec_id == 175 || result.play_sec_id == 176
                                            || result.play_sec_id == 177 || result.play_sec_id == 190 ||
                                            result.play_sec_id == 191
                                        ) {
                                            result.type = "xgc_tm_ct"
                                        } else if (result.play_sec_name == "lhc_hx") {
                                            result.type = "xgc_tm_hx"
                                            result.play_sec_options = item.play_sec_options
                                        }

                                    }
                                    result.play_sec_cname = it[num].play_sec_cname
                                    listData.add(result)
                                }
                            }
                        }
                        listData.add(
                            PlaySecData(
                                title = "",
                                type = "xgc_full"
                            )
                        )
                        xgcTmAdapter?.refresh(listData)
                    }
                    "正码特xgc" -> {
                        val listData = arrayListOf<PlayUnitData>()
                        for ((num, item) in it.withIndex()) {
                            if (!item.play_sec_data.isNullOrEmpty()) {
                                for ((index, result) in item.play_sec_data.withIndex()) {
                                    if (result.play_class_name == "num_49") {
                                        result.type = "xgc_tm_full"
                                    } else {
                                        if (result.play_sec_id == 153) {
                                            result.type = "xgc_tm_nor"
                                        } else if (result.play_sec_id == 159) result.type =
                                            "xgc_tm_ct"
                                    }
                                    result.play_sec_cname = it[num].play_sec_cname
                                }
                                if (item.play_sec_data.size == 64) {
                                    item.play_sec_data.add(
                                        PlaySecData(
                                            title = "",
                                            type = "xgc_full"
                                        )
                                    )  //添加footer 一行占满
                                }
                            }
                            listData.add(item)
                        }
                        rightTopXgcAdapter?.refresh(listData)
                    }
                    "连码xgc" -> {
                        val listData = arrayListOf<PlayUnitData>()
                        for ((num, item) in it.withIndex()) {
                            if (!item.play_sec_data.isNullOrEmpty()) {
                                if (item.play_sec_data.size == 49) {
                                    item.play_sec_data.add(
                                        0, PlaySecData(
                                            title = "",
                                            type = "xgc_tm_title",
                                            play_odds = it[num].play_sec_options?.get(0)?.play_odds
                                                ?: "null"
                                        )
                                    )  //添加footer 一行占满
                                }
                                for ((index, result) in item.play_sec_data.withIndex()) {
                                    if (index != 0 && index < 50) {
                                        result.type = "xgc_tm_nor"
                                        result.play_sec_cname = it[num].play_sec_cname
                                        result.play_odds =
                                            it[num].play_sec_options?.get(0)?.play_odds ?: "null"
                                    }
                                }
                                if (item.play_sec_data.size == 50) {
                                    item.play_sec_data.add(
                                        PlaySecData(
                                            title = "",
                                            type = "xgc_full"
                                        )
                                    )  //添加footer 一行占满
                                }
                            }
                            listData.add(item)
                        }
                        rightTopXgcAdapter?.refresh(listData)
                    }
                    "连肖连尾xgc" -> {
                        val listData = arrayListOf<PlayUnitData>()
                        for ((num, item) in it.withIndex()) {
                            if (!item.play_sec_data.isNullOrEmpty()) {
                                for ((index, result) in item.play_sec_data.withIndex()) {
                                    if (index < 12) {
                                        result.type = "xgc_tm_lx"
                                        result.play_sec_cname = it[num].play_sec_cname
                                    }
                                }
                                if (item.play_sec_data.size == 12) {
                                    item.play_sec_data.add(
                                        PlaySecData(
                                            title = "",
                                            type = "xgc_full"
                                        )
                                    )  //添加footer 一行占满
                                }
                            }
                            listData.add(item)
                        }
                        rightTopXgcAdapter?.refresh(listData)
                    }
                    "自选不中xgc" -> {
                        val listData = arrayListOf<PlaySecData>()
                        for ((num, item) in it.withIndex()) {
                            if (!item.play_sec_data.isNullOrEmpty()) {
                                for ((index, result) in item.play_sec_data.withIndex()) {
                                    result.type = "xgc_tm_nor"
                                    result.play_sec_options = item.play_sec_options
                                    result.play_sec_cname = it[num].play_sec_cname
                                    listData.add(result)
                                }
                            }
                        }
                        listData.add(
                            PlaySecData(
                                title = "",
                                type = "xgc_full"
                            )
                        )
                        xgcTmAdapter?.refresh(listData)
                    }
                }
            }
        }
    }

    /**
     * 两面适配
     */
    inner class AdapterLm : BaseRecyclerAdapter<PlaySecData>() {
        val ITEM_TYPE_HEAD = 1
        val ITEM_TYPE_CONTENT_COUNT_4 = 2
        val ITEM_TYPE_CONTENT_COUNT_3 = 3

        override fun getItemViewType(position: Int): Int {
            return when (data[position].type) {
                "lm_full" -> ITEM_TYPE_HEAD
                "lm_4" -> ITEM_TYPE_CONTENT_COUNT_4
                else -> ITEM_TYPE_CONTENT_COUNT_3
            }
        }

        override fun getItemLayoutId(viewType: Int): Int {
            return when (viewType) {
                ITEM_TYPE_HEAD -> R.layout.adapter_game_bet_title
                else -> R.layout.adapter_game_bet_content
            }
        }

        override fun bindData(holder: RecyclerViewHolder, position: Int, data: PlaySecData?) {
            if (getItemViewType(position) == ITEM_TYPE_HEAD) {
                val view = holder.findViewById<TextView>(R.id.tvGameType)
                if (lotteryId != "8") {
                    view.text = data?.title
                } else {
                    if (position == 0) {
                        val param = holder.itemView.layoutParams
                        param.height = 0
                        param.width = 0
                        ViewUtils.setGone(holder.itemView)
                    }
                }
            } else {
                val container = holder.findViewById<LinearLayout>(R.id.gameBetLinearLayout)
                val tv1 = holder.findViewById<TextView>(R.id.tvCname)
                val tv2 = holder.findViewById<TextView>(R.id.tvOdds)
                tv1.text = data?.play_class_cname
                tv2.text = data?.play_odds.toString()
                changeBg(data?.isSelected, container, tv1, tv2)
                holder.itemView.setOnClickListener {
                    data?.isSelected = data?.isSelected != true
                    addOrDeleteBetData(
                        data?.isSelected == true,
                        play_sec_name = data?.play_sec_name ?: "-1",
                        play_class_name = data?.play_class_name ?: "-1",
                        play_sec_cname = data?.title ?: "-1",
                        play_class_cname = data?.play_class_cname ?: "-1",
                        play_odds = data?.play_odds ?: "-1"
                    )
                    notifyItemChanged(position)
                }
            }
        }

        fun resetData() {
            for (item in this.data) {
                if (item.isSelected) item.isSelected = false
            }
            notifyDataSetChanged()
        }
    }

    /**
     * 快捷适配
     */
    var kjList = arrayListOf<PlaySecData>()  //快捷集合需要特殊处理  快捷的头部type选择
    var kjContentList = arrayListOf<PlaySecData>()  //快捷集合需要特殊处理  快捷的内容选择

    inner class AdapterKj : BaseRecyclerAdapter<PlaySecDataKj>() {
        val ITEM_TYPE_CONTENT_COUNT_5 = 1
        val ITEM_TYPE_CONTENT_COUNT_2 = 2
        val ITEM_TYPE_CONTENT = 3
        override fun getItemViewType(position: Int): Int {
            return when (data[position].type) {
                "kj_5" -> ITEM_TYPE_CONTENT_COUNT_5
                "kj_full" -> ITEM_TYPE_CONTENT
                else -> ITEM_TYPE_CONTENT_COUNT_2
            }
        }

        override fun getItemLayoutId(viewType: Int): Int {
            return when (viewType) {
                ITEM_TYPE_CONTENT -> R.layout.adapter_game_bet_title
                else -> R.layout.adapter_game_bet_content
            }
        }

        override fun bindData(holder: RecyclerViewHolder, position: Int, data: PlaySecDataKj?) {
            if (getItemViewType(position) != ITEM_TYPE_CONTENT) {
                val tv1 = holder.findViewById<TextView>(R.id.tvCname)
                val tv2 = holder.findViewById<TextView>(R.id.tvOdds)
                val container = holder.findViewById<LinearLayout>(R.id.gameBetLinearLayout)
                if (getItemViewType(position) == ITEM_TYPE_CONTENT_COUNT_5) {
                    tv1.text = data?.play_sec_cname
                    tv1.textSize = 9f
                    tv1.setPadding(0, 15, 0, 15)
                    val layoutParams = GridLayoutManager.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    layoutParams.marginStart = 20
                    if (position < 5) layoutParams.topMargin = 60 else {
                        layoutParams.topMargin = 20
                    }
                    holder.itemView.layoutParams = layoutParams
                    ViewUtils.setGone(tv2)
                } else if (getItemViewType(position) == ITEM_TYPE_CONTENT_COUNT_2) {
                    ViewUtils.setVisible(tv2)
                    tv1.text = data?.play_class_cname
                    tv2.text = data?.play_odds.toString()
                }
                changeBg(data?.isSelected, container, tv1, tv2)
                holder.itemView.setOnClickListener {
                    if (getItemViewType(position) == ITEM_TYPE_CONTENT_COUNT_5) {
                        if (data?.isSelected == true && kjList.size == 1) {
                            ToastUtils.showToast("至少选择一种玩法")
                            return@setOnClickListener
                        }
                        data?.isSelected = data?.isSelected != true
                        notifyItemChanged(position)
                        if (data?.isSelected == true)
                            kjList.add(
                                PlaySecData(
                                    play_sec_name = data.play_sec_merge_name ?: "-1",
                                    play_sec_cname = data.play_sec_cname ?: "-1"
                                )
                            )
                        else
                            kjList.remove(
                                PlaySecData(
                                    play_sec_name = data?.play_sec_merge_name ?: "-1",
                                    play_sec_cname = data?.play_sec_cname ?: "-1"
                                )
                            )
                        if (data?.isSelected == true) {
                            for (item in kjContentList) {
                                betList.add(
                                    PlaySecData(
                                        play_sec_name = data.play_sec_merge_name ?: "-1",
                                        play_class_name = item.play_class_name ?: "-1",
                                        play_sec_cname = data.play_sec_cname ?: "-1",
                                        play_class_cname = item.play_class_cname ?: "-1",
                                        play_odds = item.play_odds ?: "-1",
                                        title = data.play_sec_cname ?: "-1"

                                    )
                                )
                            }
                        } else {
                            val intList = arrayListOf<PlaySecData>()
                            for (res in betList) {
                                if (res.play_sec_name == data?.play_sec_merge_name) intList.add(res)
                            }
                            for (count in intList) {
                                betList.remove(count)
                            }
                        }


                    } else if (getItemViewType(position) == ITEM_TYPE_CONTENT_COUNT_2) {
                        if (kjList.isEmpty()) {
                            ToastUtils.showToast("请选择玩法")
                            return@setOnClickListener
                        }
                        data?.isSelected = data?.isSelected != true
                        notifyItemChanged(position)
                        if (data?.isSelected == true)
                            kjContentList.add(
                                PlaySecData(
                                    play_class_name = data.play_class_name,
                                    play_class_cname = data.play_class_cname,
                                    play_odds = data.play_odds
                                )
                            )
                        else
                            kjContentList.remove(
                                PlaySecData(
                                    play_class_name = data?.play_class_name,
                                    play_class_cname = data?.play_class_cname,
                                    play_odds = data?.play_odds
                                )
                            )

                        if (data?.isSelected == true) {
                            for (item in kjList) {
                                betList.add(
                                    PlaySecData(
                                        play_sec_name = item.play_sec_name ?: "-1",
                                        play_class_name = data.play_class_name ?: "-1",
                                        play_sec_cname = item.play_sec_cname ?: "-1",
                                        play_class_cname = data.play_class_cname ?: "-1",
                                        play_odds = data.play_odds ?: "-1",
                                        title = item.play_sec_cname ?: "-1"

                                    )
                                )
                            }
                        } else {
                            val intList = arrayListOf<PlaySecData>()
                            for (res in betList) {
                                if (res.play_class_name == data?.play_class_name) {
                                    intList.add(res)
                                }
                            }
                            for (count in intList) {
                                betList.remove(count)
                            }
                        }
                    }
                    if (betList.isNotEmpty()) setVisible(bottomGameBetLayout) else setGone(
                        bottomGameBetLayout
                    )
                    mPresenter.setTotal()
                }
            }
        }

        fun resetData() {
            for (item in this.data) {
                if (item.isSelected) item.isSelected = false
            }
            notifyDataSetChanged()
        }
    }

    /**
     *  单号1-10
     */
    inner class AdapterDan1D10 : BaseRecyclerAdapter<PlaySecData>() {
        val ITEM_TYPE_CONTENT_COUNT_5 = 1
        val ITEM_TYPE_CONTENT_COUNT_1 = 2

        override fun getItemViewType(position: Int): Int {
            return when (data[position].type) {
                "dh_10_5" -> ITEM_TYPE_CONTENT_COUNT_5
                else -> ITEM_TYPE_CONTENT_COUNT_1
            }
        }

        override fun getItemLayoutId(viewType: Int): Int {
            return when (viewType) {
                ITEM_TYPE_CONTENT_COUNT_1 -> R.layout.adapter_game_bet_title
                else -> R.layout.adapter_game_bet_content
            }
        }

        override fun bindData(holder: RecyclerViewHolder, position: Int, data: PlaySecData?) {
            if (getItemViewType(position) == ITEM_TYPE_CONTENT_COUNT_1) {
                val view = holder.findViewById<TextView>(R.id.tvGameType)
                view.text = data?.title
            } else {
                val container = holder.findViewById<LinearLayout>(R.id.gameBetLinearLayout)
                val tv1 = holder.findViewById<TextView>(R.id.tvCname)
                val tv2 = holder.findViewById<TextView>(R.id.tvOdds)
                tv1.text = data?.play_class_cname
                tv2.text = data?.play_odds.toString()
                changeBg(data?.isSelected, container, tv1, tv2)
                holder.itemView.setOnClickListener {
                    data?.isSelected = data?.isSelected != true
                    addOrDeleteBetData(
                        data?.isSelected == true,
                        data?.play_sec_name ?: "-1",
                        data?.play_class_name ?: "-1",
                        data?.title ?: "-1",
                        data?.play_class_cname ?: "-1",
                        data?.play_odds ?: "-1"

                    )
                    notifyItemChanged(position)
                }
            }
        }

        fun resetData() {
            for (item in this.data) {
                if (item.isSelected) item.isSelected = false
            }
            notifyDataSetChanged()
        }
    }

    /**
     * 冠亚和
     */
    inner class AdapterGYH : BaseRecyclerAdapter<PlaySecData>() {
        override fun getItemLayoutId(viewType: Int) = R.layout.adapter_game_bet_content

        override fun bindData(holder: RecyclerViewHolder, position: Int, data: PlaySecData?) {
            val container = holder.findViewById<LinearLayout>(R.id.gameBetLinearLayout)
            val tv1 = holder.findViewById<TextView>(R.id.tvCname)
            val tv2 = holder.findViewById<TextView>(R.id.tvOdds)
            tv1.text = data?.play_class_cname
            tv2.text = data?.play_odds.toString()
            changeBg(data?.isSelected, container, tv1, tv2)
            holder.itemView.setOnClickListener {
                data?.isSelected = data?.isSelected != true
                addOrDeleteBetData(
                    data?.isSelected == true,
                    data?.play_sec_name ?: "-1",
                    data?.play_class_name ?: "-1",
                    data?.playName ?: "-1",
                    data?.play_class_cname ?: "-1",
                    data?.play_odds ?: "-1"
                )
                notifyItemChanged(position)
            }
        }

        fun resetData() {
            for (item in this.data) {
                if (item.isSelected) item.isSelected = false
            }
            notifyDataSetChanged()
        }
    }

    /**
     * 单码，连码，牛牛
     */
    var currentSingle = 0  //记录一中一选了几个
    var currentDouble = 0 //记录二中二选了几个
    var currentTriple = 0 //记录三中三选了几个

    inner class AdapterDM : BaseRecyclerAdapter<PlaySecData>() {
        var currentRightTop = "-1"

        override fun getItemLayoutId(viewType: Int) = R.layout.adapter_game_bet_content

        override fun bindData(holder: RecyclerViewHolder, position: Int, data: PlaySecData?) {
            val container = holder.findViewById<LinearLayout>(R.id.gameBetLinearLayout)
            val tv1 = holder.findViewById<TextView>(R.id.tvCname)
            val tv2 = holder.findViewById<TextView>(R.id.tvOdds)
            tv1.text = data?.play_class_cname
            tv2.text = data?.play_odds.toString()
            changeBg(data?.isSelected, container, tv1, tv2)
            holder.itemView.setOnClickListener {
                val title = when {
                    (rightTop.contains("一中一")) -> "一中一"
                    (rightTop.contains("一中二")) -> "一中二"
                    (rightTop.contains("一中三")) -> "一中三"
                    (rightTop.contains("一中四")) -> "一中四"
                    (rightTop.contains("一中五")) -> "一中五"
                    (rightTop.contains("二中")) -> "二中二"
                    (rightTop.contains("三中")) -> "三中三"
                    else -> ""
                }
                val bean = PlaySecData(
                    play_sec_name = data?.play_sec_name,
                    play_class_name = data?.play_class_name ?: "-1",
                    play_sec_cname = title,
                    play_class_cname = data?.play_class_cname ?: "-1",
                    play_odds = data?.play_odds ?: "-1"
                )
                if (rightTop.contains("一中")) {
                    if (currentSingle == 1) {
                        if (!betList.contains(bean)) {
                            when {
                                rightTop.contains("一中一") -> {
                                    ToastUtils.showToast("该玩法最多选1个号码")
                                }
                                rightTop.contains("一中二") -> {
                                    ToastUtils.showToast("该玩法最多选1个号码")
                                }
                                rightTop.contains("一中三") -> {
                                    ToastUtils.showToast("该玩法最多选1个号码")
                                }
                                rightTop.contains("一中四") -> {
                                    ToastUtils.showToast("该玩法最多选1个号码")
                                }
                                rightTop.contains("一中五") -> {
                                    ToastUtils.showToast("该玩法最多选1个号码")
                                }
                            }
                            return@setOnClickListener
                        } else currentSingle--
                    } else currentSingle++
                } else if (rightTop.contains("二中二")) {
                    if (currentDouble == 2) {
                        if (!betList.contains(bean)) {
                            ToastUtils.showToast("该玩法最多选2个号码")
                            return@setOnClickListener
                        } else currentDouble--
                    } else {
                        if (!betList.contains(bean)) currentDouble++ else currentDouble--
                    }
                } else if (rightTop.contains("三中三")) {
                    if (currentTriple == 3) {
                        if (!betList.contains(bean)) {
                            ToastUtils.showToast("该玩法最多选3个号码")
                            return@setOnClickListener
                        } else currentTriple--
                    } else {
                        if (!betList.contains(bean)) currentTriple++ else currentTriple--
                    }
                }
                data?.isSelected = data?.isSelected != true

                addOrDeleteBetData(
                    data?.isSelected == true,
                    data?.play_sec_name.toString(),
                    play_class_name = data?.play_class_name ?: "-1",
                    play_sec_cname = title,
                    play_class_cname = data?.play_class_cname ?: "-1",
                    play_odds = data?.play_odds ?: "-1"

                )
                notifyItemChanged(position)
            }
        }

        fun resetData() {
            for (item in this.data) {
                if (item.isSelected) item.isSelected = false
            }
            notifyDataSetChanged()
        }
    }

    /**
     * 整合、1-5球
     */
    inner class AdapterZH : BaseRecyclerAdapter<PlaySecData>() {
        val ITEM_TYPE_HEAD = 1
        val ITEM_TYPE_CONTENT_COUNT_5 = 2
        val ITEM_TYPE_CONTENT_COUNT_3 = 3

        override fun getItemViewType(position: Int): Int {
            return when (data[position].type) {
                "zh_full" -> ITEM_TYPE_HEAD
                "zh_5" -> ITEM_TYPE_CONTENT_COUNT_5
                "zh_3" -> ITEM_TYPE_CONTENT_COUNT_3
                else -> ITEM_TYPE_CONTENT_COUNT_5
            }
        }

        override fun getItemLayoutId(viewType: Int): Int {
            return when (viewType) {
                ITEM_TYPE_HEAD -> R.layout.adapter_game_bet_title
                else -> R.layout.adapter_game_bet_content
            }
        }

        override fun bindData(holder: RecyclerViewHolder, position: Int, data: PlaySecData?) {
            if (getItemViewType(position) == ITEM_TYPE_HEAD) {
                val view = holder.findViewById<TextView>(R.id.tvGameType)
                view.text = data?.title
            } else {
                val container = holder.findViewById<LinearLayout>(R.id.gameBetLinearLayout)
                val tv1 = holder.findViewById<TextView>(R.id.tvCname)
                val tv2 = holder.findViewById<TextView>(R.id.tvOdds)
                tv1.text = data?.play_class_cname
                tv2.text = data?.play_odds.toString()
                changeBg(data?.isSelected, container, tv1, tv2)
                holder.itemView.setOnClickListener {
                    data?.isSelected = data?.isSelected != true
                    addOrDeleteBetData(
                        data?.isSelected == true,
                        data?.play_sec_name ?: "-1",
                        data?.play_class_name ?: "-1",
                        data?.play_sec_cname ?: "-1",
                        data?.play_class_cname ?: "-1",
                        data?.play_odds ?: "-1"

                    )
                    notifyItemChanged(position)
                }
            }
        }

        fun resetData() {
            for (item in this.data) {
                if (item.isSelected) item.isSelected = false
            }
            notifyDataSetChanged()
        }
    }

    /**
     * 香港彩 特码 连码....
     */
    inner class AdapterXgcTm : BaseRecyclerAdapter<PlaySecData>() {
        val TYPE_1 = 1
        val TYPE_2 = 2
        val TYPE_3 = 3
        val TYPE_4 = 4
        val TYPE_5 = 5
        val TYPE_6 = 6
        val TYPE_7 = 7
        override fun getItemViewType(position: Int): Int {
            return when (data[position].type) {
                "xgc_tm_nor" -> TYPE_1
                "xgc_tm_ct" -> TYPE_2
                "xgc_tm_full" -> TYPE_3
                "xgc_full" -> TYPE_4
                "xgc_tm_title" -> TYPE_5
                "xgc_tm_lx" -> TYPE_6
                "xgc_tm_hx" -> TYPE_7
                else -> TYPE_4
            }
        }

        override fun getItemLayoutId(viewType: Int): Int {
            return when (viewType) {
                TYPE_1 -> R.layout.adapter_game_xgc_tm
                TYPE_2 -> R.layout.adapter_game_bet_content
                TYPE_3 -> R.layout.adapter_game_xgc_tm_full
                TYPE_4 -> R.layout.adapter_game_xgc_holder
                TYPE_5 -> R.layout.adapter_game_bet_title
                TYPE_6 -> R.layout.adapter_game_xgc_tm
                TYPE_7 -> R.layout.adapter_game_xgc_tm
                else -> R.layout.adapter_game_xgc_holder
            }
        }

        @SuppressLint("SetTextI18n")
        override fun bindData(holder: RecyclerViewHolder, position: Int, data: PlaySecData?) {
            val container = holder.findViewById<LinearLayout>(R.id.gameBetLinearLayout)
            when (getItemViewType(position)) {
                TYPE_1 -> {
                    val tv1 = holder.findViewById<TextView>(R.id.tvXgcCname)
                    val tv2 = holder.findViewById<TextView>(R.id.tvXgcOdds)
                    tv1.text = data?.play_class_cname
                    tv2.text = data?.play_odds

                    when (data?.play_sec_name) {
                        "lhc_sze", "lhc_eqz", "lhc_sqz", "lhc_ezt", "lhc_tc", "lhc_siqz", "lhc_zxbz" -> {
                            setVisibility(tv2, false)
                        }
                        else -> setVisible(tv2)
                    }
                    when (data?.play_class_cname) {
                        "1", "2", "7", "8", "12", "13", "18", "19", "23", "24", "29", "30", "34", "35", "40", "45", "46" -> {
                            tv1.background = ViewUtils.getDrawable(R.drawable.code_9)
                        }
                        "3", "4", "9", "10", "14", "15", "20", "25", "26", "31", "36", "37", "41", "42", "47", "48" -> {
                            tv1.background = ViewUtils.getDrawable(R.drawable.xcode_blue)
                        }
                        else -> {
                            tv1.background = ViewUtils.getDrawable(R.drawable.code_10)
                        }
                    }
                    changeBgXgc(data?.isSelected, container)
                    container.setOnClickListener {
                        when (data?.play_sec_name) {
                            //连码处理(香港六合彩)
                            "lhc_sze", "lhc_eqz", "lhc_sqz", "lhc_ezt", "lhc_tc", "lhc_siqz" -> {
                                xgcLm(this, position, data)
                            }
                            "lhc_zxbz" -> {
                                cgcHx(this, position, data)
                            }
                            else -> {
                                data?.isSelected = data?.isSelected != true
                                addOrDeleteBetData(
                                    data?.isSelected == true,
                                    data?.play_sec_name ?: "-1",
                                    data?.play_class_name ?: "-1",
                                    data?.play_sec_cname ?: "-1",
                                    data?.play_class_cname ?: "-1",
                                    data?.play_odds ?: "-1"

                                )
                                notifyItemChanged(position)
                            }
                        }

                    }
                }
                TYPE_2 -> {
                    val tv1 = holder.findViewById<TextView>(R.id.tvCname)
                    val tv2 = holder.findViewById<TextView>(R.id.tvOdds)
                    tv1.text = data?.play_class_cname
                    tv2.text = data?.play_odds
                    changeBg(data?.isSelected, container, tv1, tv2)
                    holder.itemView.setOnClickListener {
                        data?.isSelected = data?.isSelected != true
                        addOrDeleteBetData(
                            data?.isSelected == true,
                            data?.play_sec_name ?: "-1",
                            data?.play_class_name ?: "-1",
                            data?.play_sec_cname ?: "-1",
                            data?.play_class_cname ?: "-1",
                            data?.play_odds ?: "-1"

                        )
                        notifyItemChanged(position)
                    }
                }
                TYPE_3 -> {
                    val tv1 = holder.findViewById<TextView>(R.id.tvXgcCname)
                    val tv2 = holder.findViewById<TextView>(R.id.tvXgcOdds)
                    tv1.text = data?.play_class_cname
                    tv2.text = data?.play_odds
                    tv1.background = ViewUtils.getDrawable(R.drawable.code_10)
                    changeBgXgc(data?.isSelected, container)
                    container.setOnClickListener {
                        data?.isSelected = data?.isSelected != true
                        addOrDeleteBetData(
                            data?.isSelected == true,
                            data?.play_sec_name ?: "-1",
                            data?.play_class_name ?: "-1",
                            data?.play_sec_cname ?: "-1",
                            data?.play_class_cname ?: "-1",
                            data?.play_odds ?: "-1"

                        )
                        notifyItemChanged(position)
                    }
                }
                TYPE_5 -> {
                    //连码
                    val tv = holder.findViewById<TextView>(R.id.tvGameType)
                    tv.setTextColor(ViewUtils.getColor(R.color.color_FF513E))
                    tv.text = "赔率：" + data?.play_odds
                }
                TYPE_6 -> {
                    val tv1 = holder.findViewById<TextView>(R.id.tvXgcCname)
                    val tv2 = holder.findViewById<TextView>(R.id.tvXgcOdds)
                    tv1.text = data?.play_class_cname
                    tv2.text = data?.play_odds
                    tv1.background = ViewUtils.getDrawable(R.drawable.xcode_blue)
                    changeBgXgc(data?.isSelected, container)
                    container.setOnClickListener {
                        when (data?.play_sec_name) {
                            //连肖连尾(香港六合彩)
                            "lhc_lx2", "lhc_lx3", "lhc_lx4", "lhc_lx5", "lhc_lw2", "lhc_lw3"
                                , "lhc_lw4", "lhc_lw5" -> {
                                xgcLm(this, position, data)
                            }
                            else -> {
                                data?.isSelected = data?.isSelected != true
                                addOrDeleteBetData(
                                    data?.isSelected == true,
                                    data?.play_sec_name ?: "-1",
                                    data?.play_class_name ?: "-1",
                                    data?.play_sec_cname ?: "-1",
                                    data?.play_class_cname ?: "-1",
                                    data?.play_odds ?: "-1"

                                )
                                notifyItemChanged(position)
                            }
                        }
                    }
                }
                TYPE_7 -> {
                    val tv1 = holder.findViewById<TextView>(R.id.tvXgcCname)
                    val tv2 = holder.findViewById<TextView>(R.id.tvXgcOdds)
                    tv1.text = data?.play_class_cname
                    tv2.text = data?.play_odds
                    setGone(tv2)
                    tv1.background = ViewUtils.getDrawable(R.drawable.xcode_blue)
                    changeBgXgc(data?.isSelected, container)
                    container.setOnClickListener {
                        //合肖处理
                        if (data?.play_sec_name == "lhc_hx") {
                            cgcHx(this, position, data)
                        } else {
                            data?.isSelected = data?.isSelected != true
                            addOrDeleteBetData(
                                data?.isSelected == true,
                                data?.play_sec_name ?: "-1",
                                data?.play_class_name ?: "-1",
                                data?.play_sec_cname ?: "-1",
                                data?.play_class_cname ?: "-1",
                                data?.play_odds ?: "-1"
                            )
                            notifyItemChanged(position)
                        }

                    }
                }
                else -> {

                }
            }
        }

        fun resetData() {
            for (item in this.data) {
                if (item.isSelected) item.isSelected = false
            }
            notifyDataSetChanged()
        }
    }

    //左边分类
    inner class GameTypeAdapter : BaseRecyclerAdapter<String>() {
        private var currentPos = 0
        override fun getItemLayoutId(viewType: Int) = R.layout.adapter_game_bet_type
        override fun bindData(holder: RecyclerViewHolder, position: Int, data: String?) {
            holder.text(R.id.tvGameType, data)
            if (position == currentPos) {
                holder.findViewById<TextView>(R.id.tvGameType)
                    .setTextColor(ViewUtils.getColor(R.color.color_FF513E))
            } else holder.findViewById<TextView>(R.id.tvGameType)
                .setTextColor(ViewUtils.getColor(R.color.color_333333))
            currentLeft = data.toString()
            holder.itemView.setOnClickListener {
                if (currentPos == position) return@setOnClickListener
                currentPos = position
                resetAdapter()
                firstData?.get(position)?.let { it1 -> modifyContent(it1) }
                notifyDataSetChanged()
                leftLayoutManager?.scrollToPositionWithOffset(position, 400)
            }
        }

        fun resetData() {
            currentPos = 0
        }
    }

    //右边上部分类
    inner class RightTopAdapter : BaseRecyclerAdapter<PlayUnitData>() {
        private var currentPos = 0
        private var isFirst = true
        override fun getItemLayoutId(viewType: Int) = R.layout.adapter_game_bet_right_top
        override fun bindData(holder: RecyclerViewHolder, position: Int, data: PlayUnitData?) {
            val container = holder.findViewById<LinearLayout>(R.id.gameBetLinearLayout)
            val tv1 = holder.findViewById<TextView>(R.id.tvCname)
            container.setPadding(5, 20, 5, 20)
            tv1.text = data?.play_sec_cname
            rightTop = getData()[currentPos]?.play_sec_cname.toString()
            dmAdapter?.currentRightTop = getData()[currentPos]?.play_sec_cname.toString()
            data?.isSelected = currentPos == position
            changeBg(data?.isSelected, container, tv1, null, true)
            if (isFirst) dmAdapter?.refresh(getData()[currentPos]?.play_sec_data)
            holder.itemView.setOnClickListener {
                isFirst = false
                if (currentPos == position) return@setOnClickListener
                currentPos = position
                resetAdapter(true)
                dmAdapter?.refresh(getData()[currentPos]?.play_sec_data)
                notifyDataSetChanged()
            }
        }
    }

    //右边上部分类 香港彩
    inner class RightTopXgcAdapter : BaseRecyclerAdapter<PlayUnitData>() {
        private var currentPos = 0
        private var isFirst = true
        override fun getItemLayoutId(viewType: Int) = R.layout.adapter_game_bet_right_top
        override fun bindData(holder: RecyclerViewHolder, position: Int, data: PlayUnitData?) {
            val container = holder.findViewById<LinearLayout>(R.id.gameBetLinearLayout)
            val tv1 = holder.findViewById<TextView>(R.id.tvCname)
            tv1.text = data?.play_sec_cname
            data?.isSelected = currentPos == position
            rightTop = getData()[currentPos]?.play_sec_cname.toString()
            changeBg(data?.isSelected, container, tv1, null, true)
            if (isFirst) xgcTmAdapter?.refresh(getData()[currentPos]?.play_sec_data)
            holder.itemView.setOnClickListener {
                isFirst = false
                if (currentPos == position) return@setOnClickListener
                currentPos = position
                xgcTmAdapter?.resetData()
                xgcTmAdapter?.clear()
                rvGameBetContent?.removeAllViews()
                betList.clear()
                xgcLmSelectList.clear()
                setGone(bottomGameBetLayout)
                xgcTmAdapter?.refresh(getData()[currentPos]?.play_sec_data)
                this.notifyDataSetChanged()
            }
        }
    }

    /**
     * 连码（香港彩）处理
     */
    private var xgcLmSelectList = ArrayList<String>()
    fun xgcLm(adapter: BaseRecyclerAdapter<PlaySecData>, position: Int, data: PlaySecData?) {
        var maxSelect = -1
        var minSelect = -1
        when (rightTop) {
            "三中二" -> {
                maxSelect = 7
                minSelect = 2
            }
            "二全中", "二中特", "特串" -> {
                maxSelect = 7
                minSelect = 1
            }
            "三全中" -> {
                maxSelect = 10
                minSelect = 2
            }
            "四全中" -> {
                maxSelect = 4
                minSelect = 3
            }
            "二连肖", "二连尾" -> {
                maxSelect = 11
                minSelect = 1
            }
            "三连肖", "三连尾" -> {
                maxSelect = 11
                minSelect = 2
            }
            "四连肖", "四连尾" -> {
                maxSelect = 11
                minSelect = 3
            }
            "五连肖", "五连尾" -> {
                maxSelect = 11
                minSelect = 4
            }
        }
        if (data?.isSelected != true) {
            if (xgcLmSelectList.size < maxSelect) {
                xgcLmSelectList.add(data?.play_class_cname.toString())
                if (xgcLmSelectList.size > minSelect) {
                    betList.clear()
                    val resultList = CalculationGame.combination(xgcLmSelectList, minSelect + 1)
                    for (item in resultList) {
                        val numCname = when (minSelect) {
                            1 -> item[0] + "," + item[1]
                            2 -> item[0] + "," + item[1] + "," + item[2]
                            3 -> item[0] + "," + item[1] + "," + item[2] + "," + item[3]
                            4 -> item[0] + "," + item[1] + "," + item[2] + "," + item[3] + "," + item[4]
                            else -> {
                                ToastUtils.showToast("内部错误")
                                return
                            }
                        }
                        val numName = when (minSelect) {
                            1 -> judgeName(item[0]?:"") + "," + judgeName(item[1]?:"")
                            2 -> judgeName(item[0]?:"") + "," +  judgeName(item[1]?:"") + "," +  judgeName(item[2]?:"")
                            3 -> judgeName(item[0]?:"") + "," +  judgeName(item[1]?:"") + "," +  judgeName(item[2]?:"") + "," +judgeName(item[3]?:"")
                            4 ->judgeName(item[0]?:"") + "," +  judgeName(item[1]?:"") + "," +  judgeName(item[2]?:"") + "," + judgeName(item[3]?:"")  + "," + judgeName(item[4]?:"")
                            else -> {
                                ToastUtils.showToast("内部错误")
                                return
                            }
                        }
                        addOrDeleteBetData(
                            true,
                            data?.play_sec_name.toString(),
                            numName,
                            data?.play_sec_cname.toString(),
                            numCname,
                            data?.play_odds.toString()
                        )
                    }
                }
                data?.isSelected = true
                adapter.notifyItemChanged(position)
            } else ToastUtils.showToast("当前玩法最多可选择" + maxSelect + "个号码!")
        } else {
            betList.clear()
            xgcLmSelectList.remove(data.play_class_cname)
            if (xgcLmSelectList.size > minSelect) {
                val resultList = CalculationGame.combination(xgcLmSelectList, minSelect + 1)
                for (item in resultList) {
                    val num = when (minSelect) {
                        1 -> item[0] + "," + item[1]
                        2 -> item[0] + "," + item[1] + "," + item[2]
                        3 -> item[0] + "," + item[1] + "," + item[2] + "," + item[3]
                        else -> {
                            ToastUtils.showToast("内部错误")
                            return
                        }
                    }
                    val numName = when (minSelect) {
                        1 -> judgeName(item[0]?:"") + "," + judgeName(item[1]?:"")
                        2 -> judgeName(item[0]?:"") + "," +  judgeName(item[1]?:"") + "," +  judgeName(item[2]?:"")
                        3 -> judgeName(item[0]?:"") + "," +  judgeName(item[1]?:"") + "," +  judgeName(item[2]?:"") + "," +judgeName(item[3]?:"")
                        4 ->judgeName(item[0]?:"") + "," +  judgeName(item[1]?:"") + "," +  judgeName(item[2]?:"") + "," + judgeName(item[3]?:"")  + "," + judgeName(item[4]?:"")
                        else -> {
                            ToastUtils.showToast("内部错误")
                            return
                        }
                    }
                    addOrDeleteBetData(
                        true,
                        data.play_sec_name.toString(),
                        num,
                        data.play_sec_cname.toString(),
                        numName,
                        data.play_odds.toString()
                    )
                }
            } else setGone(bottomGameBetLayout)
            data.isSelected = false
            adapter.notifyItemChanged(position)
        }
    }

    fun judgeName(name: String): String {
        return when (name) {
            "鼠" -> "mouse"
            "牛" -> "ox"
            "虎" -> "tiger"
            "兔" -> "rabbit"
            "龙" -> "dragon"
            "蛇" -> "snake"
            "马" -> "horse"
            "羊" -> "sheep"
            "猴" -> "monkey"
            "鸡" -> "rooster"
            "狗" -> "dog"
            "猪" -> "pig"
            else -> ""
        }
    }

    /**
     * 合肖（香港彩）处理
     */
    private var xgcHxSelectList = ArrayList<String>()

    @SuppressLint("SetTextI18n")
    fun cgcHx(adapter: BaseRecyclerAdapter<PlaySecData>, position: Int, data: PlaySecData?) {
        var maxSelect = -1
        var minSelect = -1
        when (data?.play_sec_name) {
            "lhc_hx" -> {
                maxSelect = 10
                minSelect = 1
            }
            "lhc_zxbz" -> {
                maxSelect = 12
                minSelect = 4
            }
        }
        if (data?.isSelected != true) {
            if (xgcLmSelectList.size < maxSelect) {
                xgcLmSelectList.add(data?.play_class_cname.toString())
                xgcHxSelectList.add(data?.play_class_name.toString())
                if (xgcLmSelectList.size > minSelect) {
                    val result = data?.play_sec_options?.get(xgcLmSelectList.size - (minSelect + 1))
                    setVisible(tvHx)
                    tvHx.text = "赔率：" + result?.play_odds
                    betList.clear()
                    val num = CalculationGame.listToString(xgcLmSelectList).toString()
                    val numName = CalculationGame.listToString(xgcHxSelectList).toString()
                    addOrDeleteBetData(
                        true,
                        data?.play_sec_name.toString(),
                        numName,
                        data?.play_sec_cname.toString(),
                        num,
                        result?.play_odds.toString()
                    )
                } else setGone(tvHx)
                data?.isSelected = true
                adapter.notifyItemChanged(position)
            } else ToastUtils.showToast("当前玩法最多可选择" + maxSelect + "个号码!")
        } else {
            betList.clear()
            xgcLmSelectList.remove(data.play_class_cname)
            xgcHxSelectList.remove(data.play_class_name)
            if (xgcLmSelectList.size > minSelect) {
                val result = data.play_sec_options?.get(xgcLmSelectList.size - (minSelect + 1))
                setVisible(tvHx)
                tvHx.text = "赔率：" + result?.play_odds
                val num = CalculationGame.listToString(xgcLmSelectList).toString()
                val numName = CalculationGame.listToString(xgcHxSelectList).toString()
                addOrDeleteBetData(
                    true,
                    data.play_sec_name.toString(),
                    numName,
                    data.play_sec_cname.toString(),
                    num,
                    result?.play_odds.toString()
                )
            } else {
                setGone(bottomGameBetLayout)
                setGone(tvHx)
            }
            data.isSelected = false
            adapter.notifyItemChanged(position)
        }
    }


    fun addOrDeleteBetData(
        isAdd: Boolean,
        play_sec_name: String,
        play_class_name: String,
        play_sec_cname: String,
        play_class_cname: String,
        play_odds: String
    ) {
        val bean = PlaySecData(
            play_sec_name = play_sec_name,
            play_class_name = play_class_name,
            play_sec_cname = play_sec_cname,
            play_class_cname = play_class_cname,
            play_odds = play_odds
        )

        if (isAdd) {
            betList.add(bean)
        } else removeElement(betList, bean)
        if (rightTop.contains("二中二")) {
            if (betList.size > 1) setVisible(bottomGameBetLayout) else setGone(bottomGameBetLayout)
        } else if (rightTop.contains("三中三")) {
            if (betList.size > 2) setVisible(bottomGameBetLayout) else setGone(bottomGameBetLayout)
        } else {
            if (betList.isNotEmpty()) setVisible(bottomGameBetLayout) else setGone(
                bottomGameBetLayout
            )
        }
        mPresenter.setTotal()
    }

    //自定义删除方法,自带的remove有毒
    private fun removeElement(mutableList: MutableList<PlaySecData>, bean: PlaySecData) {
        var index = -1
        for ((num, item) in mutableList.withIndex()) {
            if (item.play_sec_name == bean.play_sec_name && item.play_class_name == bean.play_class_name) {
                index = num
            }
        }
        try {
            mutableList.removeAt(index)
        } catch (e: Exception) {
        }
    }

    fun resetAdapter(boolean: Boolean = false) {
        rvGameBetContent?.removeAllViews()
        rvGameBetType?.removeAllViews()
        lmAdapter?.resetData()
        lmAdapter?.clear()
        kjAdapter?.resetData()
        kjAdapter?.clear()
        dan1D10Adapter?.resetData()
        dan1D10Adapter?.clear()
        gyhAdapter?.resetData()
        gyhAdapter?.clear()
        zhAdapter?.resetData()
        zhAdapter?.clear()
        dmAdapter?.resetData()
        xgcTmAdapter?.resetData()
        xgcTmAdapter?.clear()
        if (!boolean) {
            dmAdapter?.clear()
            rightTopAdapter?.clear()
            rvRightTop?.removeAllViews()
            rightTopXgcAdapter?.clear()
        }
        betList.clear()
        kjContentList.clear()
        xgcLmSelectList.clear()
        xgcHxSelectList.clear()
        kjList.clear()
        rightTop = "-1"
        setGone(bottomGameBetLayout)
        setGone(tvHx)
        currentDouble = 0
        currentSingle = 0
        currentTriple = 0
    }

    //按钮重置
    private fun btReset() {
        lmAdapter?.resetData()
        kjAdapter?.resetData()
        dan1D10Adapter?.resetData()
        gyhAdapter?.resetData()
        zhAdapter?.resetData()
        dmAdapter?.resetData()
        kjContentList.clear()
        betList.clear()
        xgcLmSelectList.clear()
        xgcHxSelectList.clear()
        kjContentList.clear()
        kjList.clear()
        xgcTmAdapter?.resetData()
        setGone(bottomGameBetLayout)
        setGone(tvHx)
        currentDouble = 0
        currentSingle = 0
        currentTriple = 0
    }


    //背景变化
    fun changeBg(
        isSelected: Boolean?,
        container: LinearLayout,
        tv1: TextView,
        tv2: TextView? = null,
        isSpecial: Boolean = false
    ) {
        if (isSelected == true) {
            if (isSpecial) {
                container.background =
                    ViewUtils.getDrawable(R.drawable.bet_special_selected_background)
            } else container.background = ViewUtils.getDrawable(R.drawable.bet_select_background)
            tv1.setTextColor(ViewUtils.getColor(R.color.color_FF3131))
            tv2?.setTextColor(ViewUtils.getColor(R.color.color_FF3131))
        } else {
            if (isSpecial) {
                container.background =
                    ViewUtils.getDrawable(R.drawable.bet_special_normal_background)
            } else container.background = ViewUtils.getDrawable(R.drawable.bet_normal_background)
            tv1.setTextColor(ViewUtils.getColor(R.color.color_333333))
            tv2?.setTextColor(ViewUtils.getColor(R.color.color_FF3131))
        }
    }

    //背景变化香港彩
    fun changeBgXgc(
        isSelected: Boolean?,
        container: LinearLayout
    ) {
        if (isSelected == true) {
            container.background = ViewUtils.getDrawable(R.drawable.bet_select_background)
        } else {
            container.background = ViewUtils.getDrawable(R.drawable.bet_normal_background)
        }
    }

    //彩种切换
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun changeSkin(eventBean: ChangeLottery) {
        if (isAdded) {
            typeAdapter?.resetData()
            typeAdapter?.clear()
            resetAdapter()
            this.lotteryId = eventBean.lotteryId
            mPresenter.getPlayList(eventBean.lotteryId)
        }
    }


    //余额更新
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun lotteryBet(eventBean: LotteryResetDiamond) {
        mPresenter.getUserBalance()
        btReset()
    }

    companion object {
        fun newInstance(lotteryId: String): GameLotteryBetFragment1 {
            val fragment = GameLotteryBetFragment1()
            val bundle = Bundle()
            bundle.putString("gameBetLotteryId", lotteryId)
            fragment.arguments = bundle
            return fragment
        }
    }


}