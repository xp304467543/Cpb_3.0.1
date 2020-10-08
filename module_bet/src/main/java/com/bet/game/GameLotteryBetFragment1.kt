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
import com.bet.R
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
import com.lib.basiclib.base.xui.adapter.recyclerview.XLinearLayoutManager
import com.lib.basiclib.utils.ToastUtils
import com.lib.basiclib.utils.ViewUtils
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

    var lmAdapter: AdapterLm? = null //两面

    var kjAdapter: AdapterKj? = null //快捷

    var dan1D10Adapter: AdapterDan1D10? = null //单号1-10

    var gyhAdapter: AdapterGYH? = null //亚冠和

    var zhAdapter: AdapterZH? = null //整合

    var dmAdapter: AdapterDM? = null //单码

    var betList = arrayListOf<PlaySecData>()  //投注集合


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

    override fun isRegisterRxBus() = true

    override fun attachView() = mPresenter.attachView(this)

    override fun attachPresenter() = GameLotteryBetFragment1Presenter()

    override fun initData() {
        mPresenter.getPlayList(arguments?.getString("gameBetLotteryId") ?: "0")
        mPresenter.getPlayMoney()
        mPresenter.getUserBalance()
    }

    override fun getLayoutRes() = R.layout.game_bet_fragment1

    override fun initContentView() {
        initRecycle()
        if (UserInfoSp.getAppMode() == AppMode.Normal){
            setVisible(selectRadio)
        }else{
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

        tvBetSubmit?.setOnClickListener {
            if (!UserInfoSp.getIsLogin()){
                GlobalDialog.notLogged(requireActivity())
                return@setOnClickListener
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
            if (is_bl_play == 0){
               if (userDiamond!="-1"){
                   val m2 = BigDecimal(userDiamond)
                  if (m2.compareTo(m1) == -1){
                      val tips = context?.let { it1 -> DialogGlobalTips(it1, "您的钻石余额不足,请充值", "兑换钻石", "取消", "") }
                      tips?.setConfirmClickListener {
                          RxBus.get().post(HomeJumpToMine(true))
                          tips.dismiss()
                      }
                      tips?.show()
                      return@setOnClickListener
                  }
               }else mPresenter.getUserBalance()
            }else{
                if (userBalance!="-1"){
                    val m2 = BigDecimal(userBalance)
                    if ( m2.compareTo(m1) == -1){
                        val tips = context?.let { it1 -> DialogGlobalTips(it1, "您的余额不足,请充值", "充值", "取消", "") }
                        tips?.setConfirmClickListener {
                            RxBus.get().post(HomeJumpToMine(true))
                            tips.dismiss()
                        }
                        tips?.show()
                        return@setOnClickListener
                    }
                }else mPresenter.getUserBalance()
            }
            when {
                rightTop.contains("二中") -> {
                    if (betList.size < 2) {
                        ToastUtils.showToast("二中二必须选择2个号码")
                        return@setOnClickListener
                    } else{
                        val newBetList = arrayListOf<PlaySecData>()
                        val playClassName = betList[0].play_class_name +"," +betList[1].play_class_name
                        val playClassCname = betList[0].play_class_cname  +"," + betList[1].play_class_cname
                        val bean =  PlaySecData(
                            play_class_name = playClassName,
                            play_sec_name = betList[0].play_sec_name,
                            play_sec_cname = betList[0].play_sec_cname ,
                            play_class_cname = playClassCname ,
                            play_odds = betList[0].play_odds
                        )
                        newBetList.add(bean)
                        context?.let { it1 -> BottomBetAccessDialog(it1,lotteryId,rightTop,nextIssue,is_bl_play,tvGameTotalMoney.text.toString(),newBetList).show() }
                        return@setOnClickListener
                    }
                }
                rightTop.contains("三中") -> {
                    if (betList.size < 3) {
                        ToastUtils.showToast("三中三必须选择3个号码")
                        return@setOnClickListener
                    } else{
                        val newBetList = arrayListOf<PlaySecData>()
                        val playClassName = betList[0].play_class_name + "," + betList[1].play_class_name +"," + betList[2].play_class_name
                        val playClassCname = betList[0].play_class_cname + "," + betList[1].play_class_cname  + "," + betList[2].play_class_cname
                        val bean =  PlaySecData(
                            play_class_name = playClassName,
                            play_sec_name = betList[0].play_sec_name,
                            play_sec_cname = betList[0].play_sec_cname,
                            play_class_cname =  playClassCname,
                            play_odds = betList[0].play_odds
                        )
                        newBetList.add(bean)
                        context?.let { it1 -> BottomBetAccessDialog(it1,lotteryId,rightTop,nextIssue,is_bl_play,tvGameTotalMoney.text.toString(),newBetList).show() }
                        return@setOnClickListener
                    }
                }
            }
            context?.let { it1 -> BottomBetAccessDialog(it1,lotteryId,rightTop,nextIssue,is_bl_play,tvGameTotalMoney.text.toString(),betList).show() }
        }

        etGameBetPlayMoney.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(str: Editable?) {
                if (str != null && str.isNotEmpty()) {
                    if (is_bl_play == 0) {
                        if (minMonty > 1 && str.toString().toLong() < 10) {
                            etGameBetPlayMoney.setText("10")
                            ToastUtils.showToast("请输入≥10的整数")
                            betTotalMoney = 10
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
    fun lotteryInfo(mIssue: String,nextIssue:String, mLotteryId: String, open: Boolean) {
        this.issue = mIssue
        this.lotteryId = mLotteryId
        this.isOpen = open
        this.nextIssue = nextIssue
    }

    private fun initRecycle() {
        typeAdapter = GameTypeAdapter()
        rvGameBetType.adapter = typeAdapter
        rvGameBetType.layoutManager = XLinearLayoutManager(context)
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
        if (!isActive())return
        if (rvGameBetContent == null)return
        val name = it.play_unit_name.toString()
        when (name) {
            "两面" -> {
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
            "整合", "第一球", "第二球", "第三球", "第四球", "第五球" -> {
                zhAdapter = AdapterZH()
               rvGameBetContent?.adapter = zhAdapter
                val layoutManager = GridLayoutManager(context, 5)
               rvGameBetContent?.layoutManager = layoutManager
                layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return when (zhAdapter?.getItemViewType(position)) {
                            zhAdapter?.ITEM_TYPE_HEAD -> 5
                            zhAdapter?.ITEM_TYPE_CONTENT_COUNT_5 -> 1
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
                val layoutManager = GridLayoutManager(context, 5)
               rvGameBetContent?.layoutManager = layoutManager
                layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return when (dan1D10Adapter?.getItemViewType(position)) {
                            dan1D10Adapter?.ITEM_TYPE_CONTENT_COUNT_5 -> 1
                            else -> 5
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
                rvRightTop.adapter = rightTopAdapter
                rvRightTop.layoutManager = GridLayoutManager(context, 5)
                dmAdapter = AdapterDM()
               rvGameBetContent?.adapter = dmAdapter
               rvGameBetContent?.layoutManager = GridLayoutManager(context, 5)

            }
        }
        modifyContentData(it, name)
    }

    private fun modifyContentData(data: LotteryPlayListResponse, type: String) {
        data.play_unit_data.let {
            if (it != null) {
                when (type) {
                    "两面" -> {
                        val listData = arrayListOf<PlaySecData>()
                        for ((pos, item) in it.withIndex()) {
                            if (!item.play_sec_data.isNullOrEmpty()) {
                                for ((index, result) in item.play_sec_data.withIndex()) {
                                    if (index == 0) listData.add(
                                        PlaySecData(title = item.play_sec_cname, type = "lm_full")
                                    )  //一行占满
                                    if (result.play_sec_id != 0 && pos == 0)  result.type = "lm_4"  //一行四个
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
                    "整合", "第一球", "第二球", "第三球", "第四球", "第五球" -> {
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
                                    if (result.play_sec_id != 0) {
                                        result.type = "zh_5"
                                        result.play_sec_cname = item.play_sec_cname
                                    } //一行五个
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
                                        play_odds = item.play_odds?:"-1",
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
                                        play_odds = data.play_odds?:"-1",
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
                val title =  when{
                    (rightTop.contains("一中")) -> "一中一"
                    (rightTop.contains("二中")) -> "二中二"
                    (rightTop.contains("三中")) -> "三中三"
                    else -> ""
                }
                val bean = PlaySecData(
                    play_sec_name = currentRightTop ,
                    play_class_name = data?.play_class_name ?: "-1",
                    play_sec_cname = title,
                    play_class_cname = data?.play_class_cname ?: "-1",
                    play_odds = data?.play_odds ?: "-1"
                )
                if (rightTop.contains("一中")) {
                    if (currentSingle == 1) {
                        if (!betList.contains(bean)) {
                            ToastUtils.showToast("一中一最多选1个号码")
                            return@setOnClickListener
                        } else currentSingle--
                    } else currentSingle++
                } else if (rightTop.contains("二中")) {
                    if (currentDouble == 2) {
                        if (!betList.contains(bean)) {
                            ToastUtils.showToast("二中二最多选2个号码")
                            return@setOnClickListener
                        } else currentDouble--
                    } else {
                        if (!betList.contains(bean)) currentDouble++ else currentDouble--
                    }
                } else if (rightTop.contains("三中")) {
                    if (currentTriple == 3) {
                        if (!betList.contains(bean)) {
                            ToastUtils.showToast("三中三最多选3个号码")
                            return@setOnClickListener
                        } else currentTriple--
                    } else {
                        if (!betList.contains(bean)) currentTriple++ else currentTriple--
                    }
                }
                data?.isSelected = data?.isSelected != true

                addOrDeleteBetData(
                    data?.isSelected == true,
                    currentRightTop,
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

        override fun getItemViewType(position: Int): Int {
            return when (data[position].type) {
                "zh_full" -> ITEM_TYPE_HEAD
                "zh_5" -> ITEM_TYPE_CONTENT_COUNT_5
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
            holder.itemView.setOnClickListener {
                if (currentPos == position) return@setOnClickListener
                currentPos = position
                resetAdapter()
                firstData?.get(position)?.let { it1 -> modifyContent(it1) }
                notifyDataSetChanged()
            }
        }

        fun resetData() {
            currentPos = 0
        }
    }

    //右边上部分类
    inner class RightTopAdapter : BaseRecyclerAdapter<PlayUnitData>() {
        private var currentPos = 0
        override fun getItemLayoutId(viewType: Int) = R.layout.adapter_game_bet_content
        override fun bindData(holder: RecyclerViewHolder, position: Int, data: PlayUnitData?) {
            val container = holder.findViewById<LinearLayout>(R.id.gameBetLinearLayout)
            val tv1 = holder.findViewById<TextView>(R.id.tvCname)
            val tv2 = holder.findViewById<TextView>(R.id.tvOdds)
            container.setPadding(5, 40, 5, 40)
            tv1.textSize = 9f
            ViewUtils.setGone(tv2)
            tv1.text = data?.play_sec_cname
            if (currentPos == position) {
                rightTop = data?.play_sec_cname.toString()
                data?.isSelected = true
                dmAdapter?.currentRightTop = data?.play_sec_data?.get(0)?.play_sec_name?:"-1"
            } else data?.isSelected = false
            changeBg(data?.isSelected, container, tv1, tv2)
            if (currentPos == 0) dmAdapter?.refresh(data?.play_sec_data)
            holder.itemView.setOnClickListener {
                if (currentPos == position) return@setOnClickListener
                currentPos = position
                resetAdapter(true)
                dmAdapter?.refresh(getData()?.get(currentPos)?.play_sec_data)
                notifyDataSetChanged()
            }
        }
        fun resetData() {
            for (item in this.data) {
                if (item.isSelected) item.isSelected = false
            }
            notifyDataSetChanged()
        }
    }

    fun addOrDeleteBetData(
        isAdd: Boolean,
        play_sec_name: String,
        play_class_name: String,
        play_sec_cname: String,
        play_class_cname: String,
        play_odds: String) {
        val bean = PlaySecData(
            play_sec_name = play_sec_name,
            play_class_name = play_class_name,
            play_sec_cname = play_sec_cname,
            play_class_cname = play_class_cname,
            play_odds = play_odds)
        if (isAdd) {
            betList.add(bean)
        } else betList.remove(bean)
        if (rightTop.contains("二中")) {
            if (betList.size > 1) setVisible(bottomGameBetLayout) else setGone(bottomGameBetLayout)
        } else if (rightTop.contains("三中")) {
            if (betList.size > 2) setVisible(bottomGameBetLayout) else setGone(bottomGameBetLayout)
        } else {
            if (betList.isNotEmpty()) setVisible(bottomGameBetLayout) else setGone(bottomGameBetLayout)

        }
        mPresenter.setTotal()
    }


    fun resetAdapter(boolean: Boolean = false) {
        rvGameBetContent?.removeAllViews()
        rvRightTop?.removeAllViews()
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
        if (!boolean) {
            dmAdapter?.clear()
            rightTopAdapter?.resetData()
            rightTopAdapter?.clear()
        }
        betList.clear()
        kjContentList.clear()
        kjList.clear()
        rightTop = "-1"
        setGone(bottomGameBetLayout)
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
        kjContentList.clear()
        kjList.clear()
        setGone(bottomGameBetLayout)
        currentDouble = 0
        currentSingle = 0
        currentTriple = 0
    }


    //背景变化
    fun changeBg(isSelected: Boolean?, container: LinearLayout, tv1: TextView, tv2: TextView) {
        if (isSelected == true) {
            container.background = ViewUtils.getDrawable(R.drawable.bet_select_background)
            tv1.setTextColor(ViewUtils.getColor(R.color.color_FF3131))
            tv2.setTextColor(ViewUtils.getColor(R.color.color_FF3131))
        } else {
            container.background = ViewUtils.getDrawable(R.drawable.bet_normal_background)
            tv1.setTextColor(ViewUtils.getColor(R.color.color_333333))
            tv2.setTextColor(ViewUtils.getColor(R.color.color_333333))
        }
    }


    //彩种切换
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun changeSkin(eventBean: ChangeLottery) {
        if (isAdded) {
            typeAdapter?.resetData()
            typeAdapter?.clear()
            resetAdapter()
            mPresenter.getPlayList(eventBean.lotteryId)
        }
    }


    //余额更新
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun lotteryBet(eventBean: LotteryResetDiamond) {
        mPresenter.getUserBalance()
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