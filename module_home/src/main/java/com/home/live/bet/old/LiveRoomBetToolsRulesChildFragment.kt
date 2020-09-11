package com.home.live.bet.old

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.customer.data.lottery.LotteryBetRuleResponse
import com.home.R

/**
 *
 * @ Author  QinTian
 * @ Date  2020-04-16
 * @ Describe 规则
 *
 */

class LiveRoomBetToolsRulesChildFragment : BaseNormalFragment() {

    private var tvBetRule: TextView? = null

    private var rvRuleTypeSelect: RecyclerView? = null


    override fun getLayoutRes() = R.layout.old_fragment_live_bet_rule

    override fun initView(rootView: View?) {
        tvBetRule = rootView?.findViewById(R.id.tvBetRule)
        rvRuleTypeSelect = rootView?.findViewById(R.id.rvRuleTypeSelect)
    }

    override fun initData() {
        val data = arguments?.getSerializable("RuleContent") as ArrayList<LotteryBetRuleResponse>
        val index = arguments?.getInt("RuleIndex") ?: 0
        if (!data.isNullOrEmpty()) {
            tvBetRule?.text = HtmlCompat.fromHtml(data[index].play_rule_type_data?.get(0)?.play_rule_content
                    ?: "", HtmlCompat.FROM_HTML_MODE_COMPACT)
            if (tvBetRule != null) {
                val ruleTypeAdapter = context?.let { LotteryChildTypeAdapter(it) }
                rvRuleTypeSelect?.adapter = ruleTypeAdapter
                rvRuleTypeSelect?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                if (!data[index].play_rule_type_data.isNullOrEmpty()) {
                    val list = arrayListOf<String>()
                    for (res in data[index].play_rule_type_data!!) {
                        list.add(res.play_rule_lottery_name ?: "")
                    }
                    ruleTypeAdapter?.refresh(list)
                }
                ruleTypeAdapter?.setOnItemClickListener {_, _, position ->
                    ruleTypeAdapter.changeBackground(position)
                    tvBetRule?.text = HtmlCompat.fromHtml(data[index].play_rule_type_data?.get(position)?.play_rule_content
                            ?: "", HtmlCompat.FROM_HTML_MODE_COMPACT)
                }
            }
        }
    }

    companion object {
        fun newInstance(data: ArrayList<LotteryBetRuleResponse>?, index: Int): LiveRoomBetToolsRulesChildFragment {
            val fragment = LiveRoomBetToolsRulesChildFragment()
            val bundle = Bundle()
            bundle.putSerializable("RuleContent", data)
            bundle.putSerializable("RuleIndex", index)
            fragment.arguments = bundle
            return fragment
        }
    }

}       