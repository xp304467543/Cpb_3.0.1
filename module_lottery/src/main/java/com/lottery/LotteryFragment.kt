package com.lottery

import androidx.recyclerview.widget.LinearLayoutManager
import com.customer.data.AppChangeMode
import com.customer.data.UserInfoSp
import com.fh.module_lottery.R
import com.lib.basiclib.base.mvp.BaseMvpFragment
import com.lottery.adapter.LotteryTypeAdapter
import com.lottery.children.*
import com.customer.data.lottery.LotteryTypeResponse
import com.customer.data.mine.ChangeSkin
import com.customer.data.mine.LotteryToLiveRoom
import com.hwangjr.rxbus.RxBus
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.thread.EventThread
import com.lib.basiclib.utils.FastClickUtil
import com.lib.basiclib.utils.LogUtils
import com.lib.basiclib.utils.ViewUtils
import com.xiaojinzi.component.anno.RouterAnno
import cuntomer.them.AppMode
import cuntomer.them.ITheme
import cuntomer.them.Theme
import kotlinx.android.synthetic.main.fragment_lottery.*

@RouterAnno(
    host = "Lottery",
    path = "main"
)
class LotteryFragment : BaseMvpFragment<LotteryPresenter>(), ITheme {

    var currentLotteryId = "1"

    var isInitBottom = false

    override fun attachView() = mPresenter.attachView(this)

    override fun attachPresenter() = LotteryPresenter()

    override fun getLayoutResID() = R.layout.fragment_lottery

    override fun isRegisterRxBus() = true

    override fun initContentView() {
        setStatusBarHeight(stateViewLottery)
        smartLottery.setEnableRefresh(false)//是否启用下拉刷新功能
        smartLottery.setEnableLoadMore(false)//是否启用上拉加载功能
        smartLottery.setEnableOverScrollBounce(true)//是否启用越界回弹
        smartLottery.setEnableOverScrollDrag(true)//是否启用越界拖动（仿苹果效果）
        setTheme(UserInfoSp.getThem())
        //加载底部
        loadRootFragment(R.id.childContainer, LotteryBaseFragment())
        if (UserInfoSp.getAppMode() == AppMode.Pure) {
            setGone(imgVideo)
        }
    }

    override fun initData() {
        mPresenter.getLotteryType()
    }


    override fun initEvent() {
        imgVideo.setOnClickListener {
            if (!FastClickUtil.isFastClick()) RxBus.get().post(LotteryToLiveRoom(currentLotteryId))
        }
    }

    // ===== 彩种类型 =====
    var lotteryTypeAdapter: LotteryTypeAdapter? = null
    fun initLotteryType(it: List<LotteryTypeResponse>) {
        lotteryTypeAdapter = LotteryTypeAdapter()
        val layoutManager =
            LinearLayoutManager(getPageActivity(), LinearLayoutManager.HORIZONTAL, false)
        rvLotteryType.layoutManager = layoutManager
        rvLotteryType.adapter = lotteryTypeAdapter
        lotteryTypeAdapter?.refresh(it)
        currentLotteryId = it[0].lottery_id ?: "1"
        mPresenter.getLotteryOpenCode(currentLotteryId)
        lotteryTypeAdapter?.setOnItemClickListener { itemView, item, position ->
            currentLotteryId = item.lottery_id ?: "1"
//            topText.text = item.cname
            lotteryTypeAdapter?.changeBackground(position)
            smoothToCenter(layoutManager, position)
            isInitBottom = false
            mPresenter.getLotteryOpenCode(item.lottery_id ?: "1")
        }
    }

    private fun smoothToCenter(layoutManager: LinearLayoutManager, position: Int) {
        val firstPosition: Int = layoutManager.findFirstVisibleItemPosition()
        val lastPosition: Int = layoutManager.findLastVisibleItemPosition()
        val left: Int = rvLotteryType.getChildAt(position - firstPosition).left
        val right: Int = rvLotteryType.getChildAt(lastPosition - position).left
        rvLotteryType.smoothScrollBy((left - right) / 2, 0)
    }

    //主题
    override fun setTheme(theme: Theme) {
        when (theme) {
            Theme.Default -> {
                imgLotteryBg.setImageResource(R.drawable.ic_them_default_top)
            }
            Theme.NewYear -> {
                imgLotteryBg.setImageResource(R.drawable.ic_them_newyear_top)
            }
            Theme.MidAutumn -> {
                imgLotteryBg.setImageResource(R.drawable.ic_them_middle_top)
            }
            Theme.LoverDay -> {
                imgLotteryBg.setImageResource(R.drawable.ic_them_love_top)
            }
            Theme.NationDay -> {
                imgLotteryBg.setImageResource(R.drawable.ic_them_gq_top)
            }
            Theme.ChristmasDay -> {
                imgLotteryBg.setImageResource(R.drawable.ic_them_sd_top)
            }
        }
        lotteryTypeAdapter?.notifyDataSetChanged()

    }

    //换肤
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun changeSkin(eventBean: ChangeSkin) {
        when (eventBean.id) {
            1 -> setTheme(Theme.Default)
            2 -> setTheme(Theme.NewYear)
            3 -> setTheme(Theme.MidAutumn)
            4 -> setTheme(Theme.LoverDay)
            5 -> setTheme(Theme.NationDay)
            6 -> setTheme(Theme.ChristmasDay)
        }
    }

    //纯净版切换
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun changeMode(eventBean: AppChangeMode) {
        if (isActive()) {
            if (eventBean.mode == AppMode.Normal) {
                setVisible(imgVideo)
            } else {
                setGone(imgVideo)
            }
        }

    }

}
