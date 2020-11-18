package com.home.live.bet.new

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.ViewPager
import com.customer.component.dialog.BottomBetAccessDialog
import com.customer.component.dialog.BottomDialogFragment
import com.customer.component.dialog.DialogGlobalTips
import com.customer.component.dialog.GlobalDialog
import com.customer.data.HomeJumpToMine
import com.customer.data.LotteryResetDiamond
import com.customer.data.UserInfoSp
import com.customer.data.lottery.LotteryApi
import com.customer.data.lottery.LotteryPlayListResponse
import com.customer.data.lottery.LotteryTypeResponse
import com.customer.data.lottery.PlaySecData
import com.customer.data.mine.MineApi
import com.customer.utils.SoundPoolHelper
import com.customer.utils.countdowntimer.lotter.LotteryTypeSelectUtil
import com.flyco.tablayout.SlidingTabLayout
import com.github.ybq.android.spinkit.SpinKitView
import com.glide.GlideUtil
import com.home.R
import com.home.live.bet.old.*
import com.hwangjr.rxbus.RxBus
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.thread.EventThread
import com.lib.basiclib.base.adapter.BaseFragmentPageAdapter
import com.lib.basiclib.utils.FastClickUtil
import com.lib.basiclib.utils.ToastUtils
import com.lib.basiclib.utils.ViewUtils
import kotlinx.android.synthetic.main.fragment_live_room_bet_content.*
import kotlinx.android.synthetic.main.old_dialog_lottery_select.*
import java.math.BigDecimal

/**
 *
 * @ Author  QinTian
 * @ Date  2020/11/16
 * @ Describe
 *
 */
class LiveRoomBetFragment : BottomDialogFragment() {

    var imgBetCLose: AppCompatImageView? = null
    var tvRedBall: AppCompatImageView? = null
    var imgPlaySound: AppCompatImageView? = null
    var imgIcon: AppCompatImageView? = null
    var linLotteryOpenCode: LinearLayout? = null
    var tvOpenCodePlaceHolder: SpinKitView? = null
    var tvLotterySelectType: AppCompatTextView? = null
    var tabGuss: SlidingTabLayout? = null
    var tvBetTools: AppCompatTextView? = null
    var tvOpenCount: AppCompatTextView? = null
    var tvCloseTime: AppCompatTextView? = null
    var tvOpenTime: AppCompatTextView? = null
    var tvUserDiamond: AppCompatTextView? = null
    var tvReset: AppCompatTextView? = null
    var tvBetSubmit: AppCompatTextView? = null
    var tvBetRecord: AppCompatTextView? = null
    var tvBetCount: AppCompatTextView? = null
    var tvDiamond: AppCompatTextView? = null
    var tvEnd: AppCompatTextView? = null
    var bottomBetLayout: ConstraintLayout? = null
    var selectRadio: RadioGroup? = null
    var rb_1: RadioButton? = null
    var rb_2: RadioButton? = null
    var etBetPlayMoney: AppCompatEditText? = null
    var radioHor: LinearLayout? = null
    var radioGroupLayout: RadioGroup? = null
    var vpGuss: ViewPager? = null
    private var fragmentList:ArrayList<LiveRoomBetFragmentContent>?=null
    private var ivPlaySound: AppCompatImageView? = null
    private var userDiamond = "-1"
    private var userBalance = "-1"
    private var currentLotteryId = "8"
    private var resultList: ArrayList<LotteryTypeResponse>? = null
    private var currentIndex = 0
    private var nextIssue = ""
    private var isOpenCode = false
    private var minMonty = 1 //最小投注金额
    private var is_bl_play = 1 //是否余额投注，默认0不是，1是
    private var selectMoneyList: ArrayList<RadioButton>? = null
    private var opt1SelectedPosition = 0
    private var liveRoomBetToolsFragment: LiveRoomBetToolsFragment? = null
    private var liveRoomBetRecordFragment: LiveRoomBetRecordFragment? = null
    private var viewPagerAdapter: BaseFragmentPageAdapter? = null
    override val resetHeight: Int = 0

    override val layoutResId: Int = R.layout.fragment_live_room_bet

    override fun isShowTop() = false

    override fun canceledOnTouchOutside() = true

    override fun initView() {
        imgBetCLose = rootView?.findViewById(R.id.imgBetCLose)
        tvRedBall = rootView?.findViewById(R.id.tvRedBall)
        imgPlaySound = rootView?.findViewById(R.id.imgPlaySound)
        imgIcon = rootView?.findViewById(R.id.imgIcon)
        linLotteryOpenCode = rootView?.findViewById(R.id.linLotteryOpenCode)
        tvOpenCodePlaceHolder = rootView?.findViewById(R.id.tvOpenCodePlaceHolder)
        tvLotterySelectType = rootView?.findViewById(R.id.tvLotterySelectType)
        tabGuss = rootView?.findViewById(R.id.tabGuss)
        vpGuss = rootView?.findViewById(R.id.vpGuss)
        tvBetTools = rootView?.findViewById(R.id.tvBetTools)
        tvOpenCount = rootView?.findViewById(R.id.tvOpenCount)
        tvCloseTime = rootView?.findViewById(R.id.tvCloseTime)
        tvOpenTime = rootView?.findViewById(R.id.tvOpenTime)
        tvUserDiamond = rootView?.findViewById(R.id.tvUserDiamond)
        tvReset = rootView?.findViewById(R.id.tvReset)
        tvBetSubmit = rootView?.findViewById(R.id.tvBetSubmit)
        tvBetRecord = rootView?.findViewById(R.id.tvBetRecord)
        tvBetCount = rootView?.findViewById(R.id.tvBetCount)
        tvDiamond = rootView?.findViewById(R.id.tvDiamond)
        tvEnd = rootView?.findViewById(R.id.tvEnd)
        bottomBetLayout = rootView?.findViewById(R.id.bottomBetLayout)
        selectRadio = rootView?.findViewById(R.id.selectRadio)
        rb_1 = rootView?.findViewById(R.id.rb_1)
        rb_2 = rootView?.findViewById(R.id.rb_2)
        etBetPlayMoney = rootView?.findViewById(R.id.etBetPlayMoney)
        radioHor = rootView?.findViewById(R.id.radioHor)
        radioGroupLayout = rootView?.findViewById(R.id.radioGroupLayout)
        ivPlaySound = rootView?.findViewById(R.id.imgPlaySound)
        tvReset?.setOnClickListener {
            ViewUtils.setGone(bottomBetLayout)
            if (radioGroupLayout?.getChildAt(0) != null)
                (radioGroupLayout?.getChildAt(0) as RadioButton).isChecked = true
            reSetData()
            RxBus.get().post(LotteryReset(true))
        }
    }

    override fun initData() {
        if (isAdded) {
            RxBus.get().register(this)
            val id = arguments?.getString("LIVE_ROOM_LOTTERY_ID") ?: "8"
            currentLotteryId = if (id == "" || id == "-1") "8" else id
            val type = LotteryApi.getLotteryBetType()
            type.onSuccess {
                val title = arrayListOf<String>()
                resultList = arrayListOf()
                for ((index, data) in it.withIndex()) {
                    resultList?.add(data)
                    title.add(data.cname ?: "")
                    if (id == data.lottery_id) {
                        currentIndex = index
                    }
                }
                if (tvRedBall != null) GlideUtil.loadImage(it[currentIndex].logo_url, tvRedBall!!)
                tvLotterySelectType?.text = it[currentIndex].cname
                if (!title.isNullOrEmpty()) initDialog(title, resultList!!)
            }
            getLotteryNewCode(if (id == "" || id == "-1") "8" else id)//默认加香港彩  8
            setTabLayout(if (id == "" || id == "-1") "8" else id)
            getPlayMoney()
            getUserDiamond()
            getUserBalance()
            clearRadio(true)
        }
    }


    override fun initFragment() {
        if (UserInfoSp.getIsPlaySound()) {
            ivPlaySound?.setImageResource(R.mipmap.old_lb_hong)
        } else ivPlaySound?.setImageResource(R.mipmap.old_lb_hui)
        ivPlaySound?.setOnClickListener {
            if (UserInfoSp.getIsPlaySound()) {
                UserInfoSp.putIsPlaySound(false)
                ivPlaySound?.setImageResource(R.mipmap.old_lb_hui)
            } else {
                UserInfoSp.putIsPlaySound(true)
                ivPlaySound?.setImageResource(R.mipmap.old_lb_hong)
            }
        }
        rootView?.findViewById<TextView>(R.id.tvBetTools)?.setOnClickListener {
            liveRoomBetToolsFragment =
                LiveRoomBetToolsFragment.newInstance(lotteryID = currentLotteryId, isssue = "")
            fragmentManager?.let { it1 ->
                liveRoomBetToolsFragment?.show(
                    it1,
                    "LiveRoomBetToolsFragment"
                )
            }
        }
        radioGroupLayout = rootView?.findViewById(R.id.radioGroupLayout)
        rootView?.findViewById<TextView>(R.id.tvBetRecord)?.setOnClickListener {
            liveRoomBetRecordFragment = LiveRoomBetRecordFragment()
            fragmentManager?.let { it1 ->
                liveRoomBetRecordFragment?.show(
                    it1,
                    "liveRoomBetRecordFragment"
                )
            }
        }
        rootView?.findViewById<ImageView>(R.id.imgBetCLose)?.setOnClickListener {
            dismiss()
        }
        rootView?.findViewById<ImageView>(R.id.imgIcon)
            ?.setBackgroundResource(R.mipmap.old_ic_ye_tz)
        etBetPlayMoney?.addTextChangedListener(object : TextWatcher {
            @SuppressLint("SetTextI18n")
            override fun afterTextChanged(str: Editable?) {
                if (str != null && str.isNotEmpty()) {
                    betTotalMoney = if (str.length > 9) {
                        etBetPlayMoney?.setText(str.substring(0, 9)) //截取前x位
                        etBetPlayMoney?.requestFocus()
                        etBetPlayMoney?.setSelection(etBetPlayMoney?.text?.length ?: 0) //光标移动到最后
                        str.substring(0, 9).toInt()
                    } else {
                        str.toString().toInt()
                    }
                    setTotal()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        initEvent()
    }


    private fun initEvent() {
        rb_1?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                imgIcon?.setBackgroundResource(R.mipmap.old_ic_ye_tz)
                tvEnd?.text = "元"
                if (tvUserDiamond != null) tvUserDiamond?.text = userBalance
                is_bl_play = 1
                minMonty = 1
                betTotalMoney = 1
                setTotal()
                etBetPlayMoney?.setText("1")
                etBetPlayMoney?.setSelection("1".length)
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
                setTotal()
                etBetPlayMoney?.setText("10")
                etBetPlayMoney?.setSelection("10".length)
                clearRadio(false)
            }
        }
        rb_1?.isChecked = true
        tvBetSubmit?.setOnClickListener {
            if (!FastClickUtil.isFastClick()) {
                if (!UserInfoSp.getIsLogin()) {
                    GlobalDialog.notLogged(requireActivity())
                    return@setOnClickListener
                }
                if (etBetPlayMoney?.text.isNullOrEmpty()) {
                    ToastUtils.showToast("请输入投注金额")
                    return@setOnClickListener
                }
                if (is_bl_play == 0 && etBetPlayMoney?.text.toString().toLong() < 10) {
                    ToastUtils.showToast("请输入≥10的整数")
                    return@setOnClickListener
                }
                if (!isOpenCode) {
                    ToastUtils.showToast("当前期已封盘或已开奖，请购买下一期")
                    return@setOnClickListener
                }
                if (betList.isEmpty()) {
                    ToastUtils.showToast("未选择任何玩法或投注金额,请选择后再提交")
                    return@setOnClickListener
                }
                //余额不足
                val m1 = BigDecimal(tvDiamond?.text.toString())
                if (is_bl_play == 0) {
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
                    } else getUserBalance()
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
                                RxBus.get().post(HomeJumpToMine(true))
                                tips.dismiss()
                            }
                            tips?.show()
                            return@setOnClickListener
                        }
                    } else getUserBalance()
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
                                    currentLotteryId,
                                    rightTop,
                                    nextIssue,
                                    is_bl_play,
                                    tvDiamond?.text.toString(),
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
                                    currentLotteryId,
                                    rightTop,
                                    nextIssue,
                                    is_bl_play,
                                    tvDiamond?.text.toString(),
                                    newBetList
                                ).show()
                            }
                            return@setOnClickListener
                        }
                    }
                }
                context?.let { it1 ->
                    BottomBetAccessDialog(
                        it1,
                        currentLotteryId,
                        rightTop,
                        nextIssue,
                        is_bl_play,
                        tvDiamond?.text.toString(),
                        betList
                    ).show()
                }
            } else ToastUtils.showToast("请勿重复点击")
        }
    }

    private fun clearRadio(isBoolean: Boolean) {
        if (selectMoneyList.isNullOrEmpty()) {
            return
        }
        radioGroupLayout?.removeAllViews()
        for ((index, radio) in selectMoneyList!!.withIndex()) {
            radio.isChecked = !isBoolean && index == 0
            radioGroupLayout?.addView(radio)
            val params = radio.layoutParams as RadioGroup.LayoutParams
            params.width = ViewUtils.dp2px(35)
            params.height = ViewUtils.dp2px(35)
            params.setMargins(ViewUtils.dp2px(5), 0, ViewUtils.dp2px(5), 0)
            radio.layoutParams = params
        }
    }


    //底部弹框
    var dialog: BottomLotterySelectDialog? = null
    private fun initDialog(title: ArrayList<String>, list: ArrayList<LotteryTypeResponse>) {
        tvLotterySelectType?.setOnClickListener {
            val lotterySelectDialog = BottomLotterySelectDialog(context!!, title)
            lotterySelectDialog.setCanceledOnTouchOutside(false)
            tvLotterySelectType?.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.mipmap.select_top,
                0
            )
            lotterySelectDialog.setOnDismissListener {
                tvLotterySelectType?.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.mipmap.select_bottom,
                    0
                )
            }
            lotterySelectDialog.tvLotteryWheelSure.setOnClickListener {
                isPlay = false
                if (runnable != null) handler?.removeCallbacks(runnable!!)
                tvLotterySelectType?.text =
                    lotterySelectDialog.lotteryPickerView.opt1SelectedData as String
                opt1SelectedPosition = lotterySelectDialog.lotteryPickerView.opt1SelectedPosition
                currentLotteryId = list[opt1SelectedPosition].lottery_id ?: ""
                getLotteryNewCode(list[opt1SelectedPosition].lottery_id ?: "")
                setTabLayout(list[opt1SelectedPosition].lottery_id ?: "")
                tvRedBall?.let {
                    GlideUtil.loadImage(list[opt1SelectedPosition].logo_url, it)
                }
                timer?.cancel()
                timerClose?.cancel()
                if (tvCloseTime != null) tvCloseTime?.text = "--:--"
                if (tvOpenTime != null) tvOpenTime!!.text = "--:--"
                reSetData()
                lotterySelectDialog.dismiss()
            }
            lotterySelectDialog.lotteryPickerView.opt1SelectedPosition = opt1SelectedPosition
            lotterySelectDialog.show()

        }
    }

    private var playList = ArrayList<LotteryPlayListResponse>()

    //彩种选择
    private fun setTabLayout(lottery_id: String) {
        if (isAdded && activity != null) {
            LotteryApi.getGuessPlayList(lottery_id) {
                try {
                    onSuccess {
                        playList.clear()
                        playList.addAll(it)
                        if (isAdded) {
                            setTabData(playList)
                        }

                    }
                } catch (e: Exception) {
                    ToastUtils.showToast("出了点小问题,请关闭后重试")
                }
            }
        }
    }

    //设置玩法列表
    private fun setTabData(data: ArrayList<LotteryPlayListResponse>) {
        fragmentList = arrayListOf()
        val title = arrayListOf<String>()
        for ((index, item) in data.withIndex()) {
            title.add(item.play_unit_name.toString())
            fragmentList?.add(
                LiveRoomBetFragmentContent.newInstance(
                    currentLotteryId,
                    item.play_unit_name.toString(),
                    index,
                    data
                )
            )
        }
        if (!fragmentList.isNullOrEmpty()){
            viewPagerAdapter = BaseFragmentPageAdapter(childFragmentManager, fragmentList!!, title)
            vpGuss?.adapter = viewPagerAdapter
            vpGuss?.offscreenPageLimit = 10
            tabGuss?.setViewPager(vpGuss)
            //可用于切换 整合 单码之类的tab时清空
            vpGuss?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {}
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
                override fun onPageSelected(position: Int) {
                    reSetData()
                }
            })
        }else ToastUtils.showToast("内部错误")
    }

    //开奖结果
    private fun getLotteryNewCode(lottery_id: String) {
        LotteryApi.getLotteryNewCode(lottery_id) {
            onSuccess {
                if (isVisible) {
                    if (it.next_lottery_time?.toInt() ?: 0 > 0 && it.next_lottery_end_time ?: 0 > 0) {
                        nextIssue = it.next_issue ?: "0"
                        tvOpenCount?.text = (it.issue + " 期开奖结果   ")
                        countDownTime(it.next_lottery_time?.toString() ?: "0", lottery_id)
                        //更新最新开奖数据
                        setContainerCode(lottery_id, it.code)
                        tvOpenCodePlaceHolder?.visibility = View.GONE
                        countDownTimerClose(it.next_lottery_end_time ?: 0)
                        isOpenCode = true
                        if (isPlay) {
                            if (UserInfoSp.getIsPlaySound()) {
                                if (handlerPlay == null) handlerPlay = Handler()
                                handlerPlay?.post {
                                    SoundPoolHelper(context).playSoundWithRedId(R.raw.ring)
                                }
                            }
                        }
                    } else {
                        if (timer != null) timer?.cancel()
                        tvOpenCodePlaceHolder?.visibility = View.VISIBLE
                        tvOpenTime?.text = "--:--"
                        tvOpenCount?.text = ("- - - -" + "期开奖结果   ")
                        tvCloseTime?.text = "--:--"
                        getNewResult(it.lottery_id ?: "0")
                        nextIssue = ""
                    }
                }
            }
            onFailed {
                getNewResult(lottery_id)
            }
        }
    }

    private fun setContainerCode(lotteryId: String?, code: String?) {
        when (lotteryId) {
            "8" -> {
                val tbList = code?.split(",") as java.util.ArrayList<String>
                tbList.add(6, "+")
                linLotteryOpenCode?.let {
                    LotteryTypeSelectUtil.addOpenCode(
                        this.requireContext(),
                        it,
                        tbList,
                        lotteryId
                    )
                }
            }
            else -> {
                linLotteryOpenCode?.let {
                    LotteryTypeSelectUtil.addOpenCode(
                        this.requireContext(),
                        it,
                        code?.split(","),
                        lotteryId
                    )
                }
            }
        }
    }

    //封盘倒计时
    private var timerClose: CountDownTimer? = null
    private fun countDownTimerClose(millisUntilFinished: Long) {
        if (timerClose != null) timerClose?.cancel()
        val timeCountDown = millisUntilFinished * 1000
        timerClose = object : CountDownTimer(timeCountDown, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                val day: Long = millisUntilFinished / (1000 * 60 * 60 * 24)/*单位 天*/
                val hour: Long =
                    (millisUntilFinished - day * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60)/*单位 时*/
                val minute: Long =
                    (millisUntilFinished - day * (1000 * 60 * 60 * 24) - hour * (1000 * 60 * 60)) / (1000 * 60)/*单位 分*/
                val second: Long =
                    (millisUntilFinished - day * (1000 * 60 * 60 * 24) - hour * (1000 * 60 * 60) - minute * (1000 * 60)) / 1000 /*单位 秒*/
                if (tvCloseTime != null) {
                    when {
                        day > 0 -> tvCloseTime?.text =
                            dataLong(day) + "天" + dataLong(hour) + ":" + dataLong(minute) + ":" + dataLong(
                                second
                            )
                        hour > 0 -> tvCloseTime?.text =
                            dataLong(hour) + ":" + dataLong(minute) + ":" + dataLong(second)
                        else -> tvCloseTime?.text = dataLong(minute) + ":" + dataLong(second)
                    }
                }
            }

            override fun onFinish() {
                isOpenCode = false
                if (tvCloseTime != null) tvCloseTime?.text = "--:--"
            }
        }
        if (timerClose != null) timerClose?.start()
    }

    // ===== 开奖倒计时 =====
    var timer: CountDownTimer? = null
    var handler: Handler? = null
    var handlerPlay: Handler? = null
    var runnable: Runnable? = null
    var isPlay = false

    private fun countDownTime(millisUntilFinished: String, lotteryId: String) {
        if (timer != null) timer?.cancel()
        runnable?.let {
            handler?.removeCallbacks(it)
        }
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
                if (tvCloseTime != null) {
                    when {
                        day > 0 -> tvOpenTime?.text =
                            dataLong(day) + "天" + dataLong(hour) + ":" + dataLong(minute) + ":" + dataLong(
                                second
                            )
                        hour > 0 -> tvOpenTime?.text =
                            dataLong(hour) + ":" + dataLong(minute) + ":" + dataLong(second)
                        else -> tvOpenTime?.text = dataLong(minute) + ":" + dataLong(second)
                    }
                }
            }

            override fun onFinish() {
                if (isVisible) {
                    isPlay = true
                    tvOpenTime!!.text = "开奖中..."
                    tvOpenCodePlaceHolder?.visibility = View.VISIBLE
                }
                getNewResult(lotteryId)
            }
        }
        if (timer != null) timer?.start()
    }


    fun getNewResult(lotteryId: String) {
        handler = Handler()
        runnable = Runnable {
            getLotteryNewCode(lotteryId)
        }
        runnable?.let {
            handler?.postDelayed(it, 3000)
        }
    }


    // 这个方法是保证时间两位数据显示，如果为1点时，就为01
    fun dataLong(c: Long): String {
        return if (c >= 10)
            c.toString()
        else "0$c"
    }

    /**
     * 获取钻石
     */
    private fun getUserDiamond() {
        try {
            MineApi.getUserDiamond {
                if (isAdded) {
                    onSuccess {
                        userDiamond = it.diamond
                        if (is_bl_play != 1) {
                            if (tvUserDiamond != null) tvUserDiamond?.text = userDiamond
                        }
                    }
                    onFailed {
                        userDiamond = "0"
                        tvUserDiamond?.text = userDiamond
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
//                    mView.setBalance(it.balance.toString())
                userBalance = it.balance.toString()
                if (is_bl_play == 1) {
                    if (tvUserDiamond != null) tvUserDiamond?.text = userBalance
                }
            }
            onFailed {
                userBalance = "0"
                tvUserDiamond?.text = userBalance
                ToastUtils.showToast(it.getMsg() ?: "")
            }
        }
    }

    @SuppressLint("ResourceType")
    fun getPlayMoney() {
        LotteryApi.lotteryBetMoney {
            if (isAdded) {
                onSuccess {
                    if (activity != null) {
                        selectMoneyList = arrayListOf()
                        for (res in it) {
                            val radio = RadioButton(context)
                            radio.buttonDrawable = null
                            radio.background =
                                ViewUtils.getDrawable(R.drawable.lottery_bet_radio)
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) radio.setTextColor(
                                context?.getColorStateList(R.drawable.color_radio_bet)
                            )
                            radio.textSize = ViewUtils.dp2px(2.5f)
                            radio.gravity = Gravity.CENTER
                            radio.text = res.play_sum_name
                            radio.id = res.play_sum_num ?: 0
                            radio.setOnCheckedChangeListener { buttonView, isChecked ->
                                if (isChecked) {
                                    etBetPlayMoney?.setText(buttonView.id.toString())
                                    etBetPlayMoney?.setSelection(buttonView.id.toString().length)
                                    betTotalMoney = buttonView.id
                                    setTotal()
                                }
                            }
                            selectMoneyList?.add(radio)
                            radioGroupLayout?.addView(radio)
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

    //重置页面
    @SuppressLint("SetTextI18n")
    private fun reSetData() {
        ViewUtils.setGone(bottomBetLayout)
        betCount = 1 //注数
        betTotalMoney = 1 //投注金额
        etBetPlayMoney?.setText("1")
        is_bl_play = 1
        rb_1?.isChecked = true
        setTotal()
        betList.clear()
        fragmentList?.get(vpGuss?.currentItem?:0)?.resetAllAdapter()
    }


    override fun dismiss() {
        super.dismiss()
        runnable?.let {
            handler?.removeCallbacks(it)
        }
    }

    private var betList = mutableListOf<PlaySecData>()

    private var betCount = 1 //注数

    private var betTotalMoney = 1 //投注金额

    private var rightTop = ""

    @SuppressLint("SetTextI18n")
    private fun setTotal() {
        betCount = if (rightTop.contains("二中二") || rightTop.contains("三中三")) {
            1
        } else {
            if (betList.isEmpty()) 1 else (betList.size)
        }
        tvBetCount?.text = "共" + (betCount) + "注"
        tvDiamond?.text = (betTotalMoney * (betCount)).toString()
    }

    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun lotteryCurrent(eventBean: LotteryLiveBet) {
        rightTop = eventBean.rightTop
        betList = eventBean.betList
        if (eventBean.rightTop.contains("二中二")) {
            if (betList.size > 1) ViewUtils.setVisible(bottomBetLayout) else ViewUtils.setGone(
                bottomBetLayout
            )
        } else if (eventBean.rightTop.contains("三中三")) {
            if (betList.size > 2) ViewUtils.setVisible(bottomBetLayout) else ViewUtils.setGone(
                bottomBetLayout
            )
        } else {
            if (betList.isNotEmpty()) ViewUtils.setVisible(bottomBetLayout) else ViewUtils.setGone(
                bottomBetLayout
            )
        }
        setTotal()
    }


    //余额更新
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun lotteryBet(eventBean: LotteryResetDiamond) {
      if (is_bl_play == 1)  getUserBalance() else getUserDiamond()
        fragmentList?.get(vpGuss?.currentItem?:0)?.resetAllAdapter()
        ViewUtils.setGone(bottomBetLayout)
    }


    override fun onDestroy() {
        RxBus.get().unregister(this)
        super.onDestroy()
    }

    companion object {
        fun newInstance(lotteryId: String) = LiveRoomBetFragment().apply {
            arguments = Bundle(1).apply {
                putString("LIVE_ROOM_LOTTERY_ID", lotteryId) //isFollow
            }
        }
    }
}