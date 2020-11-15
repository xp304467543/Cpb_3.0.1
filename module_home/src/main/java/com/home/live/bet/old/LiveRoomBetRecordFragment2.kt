package com.home.live.bet.old

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.customer.data.UserInfoSp
import com.customer.data.lottery.LotteryApi
import com.home.R
import com.lib.basiclib.base.round.RoundLinearLayout
import com.lib.basiclib.utils.ToastUtils
import com.lib.basiclib.utils.ViewUtils
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import cuntomer.them.AppMode
import kotlinx.android.synthetic.main.old_fragment_live_bet_record_child.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020-04-16
 * @ Describe  未结算
 *
 */

class LiveRoomBetRecordFragment2 : BaseNormalFragment() {

    private var index = 1

    var str = arrayListOf( "金额","钻石")

    var currentSel = "1" //默认钻石

    private var adapter: LiveRoomRecordAdapter? = null

    private var rvBetRecord: RecyclerView? = null

    private var smBetRecord: SmartRefreshLayout? = null

    private var tvBetRecordHolder: TextView? = null

    private var recordTop: RoundLinearLayout? = null

    override fun getLayoutRes() = R.layout.old_fragment_live_bet_record_child

    override fun initView(rootView: View?) {
        rvBetRecord = rootView?.findViewById(R.id.rvBetRecord)
        smBetRecord = rootView?.findViewById(R.id.smBetRecord)
        tvBetRecordHolder = rootView?.findViewById(R.id.tvBetRecordHolder)
        recordTop = rootView?.findViewById(R.id.recordTop)
        adapter = context?.let { LiveRoomRecordAdapter(it, 2) }
        rvBetRecord?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvBetRecord?.adapter = adapter
        if (UserInfoSp.getAppMode() == AppMode.Normal){
            ViewUtils.setGone(rootView?.findViewById(R.id.tvPure))
            ViewUtils.setVisible(rootView?.findViewById(R.id.sp_down))
        }else{
            ViewUtils.setVisible(rootView?.findViewById(R.id.tvPure))
            ViewUtils.setGone(rootView?.findViewById(R.id.sp_down))
        }
    }

    override fun initData() {
        val spAdapter: ArrayAdapter<String> = ArrayAdapter(context!!, R.layout.old_my_spinner, str)
        spAdapter.setDropDownViewResource(R.layout.old_dropdown_stytle)
        sp_down.adapter = spAdapter
        sp_down.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                index = 1
                adapter?.clear()
                currentSel = if (position == 0) {
                    "1"
                } else "0"
                getResponse()
            }

        }
        getResponse()
        smBetRecord?.setOnRefreshListener {
            index = 1
            adapter?.clear()
            rvBetRecord?.removeAllViews()
            getResponse()
        }
        smBetRecord?.setOnLoadMoreListener {
            index++
            getResponse(true)
        }
    }


    private fun getResponse(isLoadMore:Boolean = false) {
        adapter?.currentSel = currentSel
        val res = LotteryApi.getLotteryBetHistory(2, page = index, is_bl_play = currentSel)
        res.onSuccess {
            smBetRecord?.finishLoadMore()
            smBetRecord?.finishRefresh()
            if (!it.isNullOrEmpty()) {
                ViewUtils.setVisible(recordTop)
                ViewUtils.setGone(tvBetRecordHolder)
               if (isLoadMore)adapter?.loadMore(it) else adapter?.refresh(it)
            } else {
                if (index == 1) {
                    adapter?.clear()
                    rvBetRecord?.removeAllViews()
                    ViewUtils.setVisible(tvBetRecordHolder)
                } else {
                    index--
                    smBetRecord?.finishLoadMore()
                }
            }

        }
        res.onFailed {
            smBetRecord?.finishLoadMore()
            smBetRecord?.finishRefresh()
            ToastUtils.showToast(it.getMsg())
        }

    }

}