package com.mine.children.recharge

import android.annotation.SuppressLint
import android.content.Intent
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import com.customer.data.UserInfoSp
import com.glide.GlideUtil
import com.hwangjr.rxbus.RxBus
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.thread.EventThread
import com.lib.basiclib.base.activity.BaseNavActivity
import com.lib.basiclib.base.recycle.BaseRecyclerAdapter
import com.lib.basiclib.base.recycle.RecyclerViewHolder
import com.lib.basiclib.utils.FastClickUtil
import com.lib.basiclib.utils.ToastUtils
import com.lib.basiclib.utils.ViewUtils
import com.mine.R
import com.customer.data.mine.MineApi
import com.customer.data.mine.MineUpDateBank
import com.customer.data.mine.MineUserBankList
import com.customer.data.MineSaveBank
import kotlinx.android.synthetic.main.act_mine_bank_card_list.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/25
 * @ Describe
 *
 */
class MineUserBankCardListAct : BaseNavActivity() {

    var mineItemAdapter: MineUserBankCardListAdapter? = null


    override fun getContentResID() = R.layout.act_mine_bank_card_list

    override fun getPageTitle() = getString(R.string.mine_card_list)

    override fun isShowBackIconWhite() = false

    override fun isSwipeBackEnable() = true

    override fun isOverride() = false

    override fun isRegisterRxBus() = true

    override fun initData() {
        getBankList()
    }

    override fun initEvent() {
        rlAddBank.setOnClickListener {
            if (!FastClickUtil.isFastClick()){
                val intent = Intent(this@MineUserBankCardListAct, MineAddBankCardAct::class.java)
                startActivity(intent)
            }
        }
    }


    @SuppressLint("SetTextI18n")
    fun getBankList() {
        showPageLoadingDialog()
        MineApi.getUserBankList {
            onSuccess {
                mineItemAdapter = MineUserBankCardListAdapter(intent.getStringExtra("cardID")!!.toString())
                mineItemAdapter?.refresh(it)
                rvCardList.adapter = mineItemAdapter
                val value = LinearLayoutManager(this@MineUserBankCardListAct)
                rvCardList.layoutManager = value
                hidePageLoadingDialog()
            }
            onFailed {
                ToastUtils.showToast(it.getMsg().toString())
                hidePageLoadingDialog()
            }
        }
    }

    inner class MineUserBankCardListAdapter(var cardId: String) : BaseRecyclerAdapter<MineUserBankList>() {

        override fun getItemLayoutId(viewType: Int) = R.layout.adapter_mine_bank_card_list

        override fun bindData(holder: RecyclerViewHolder, position: Int, data: MineUserBankList?) {

            holder.text(R.id.tvBankName, data?.bank_name)
            holder.text(
                R.id.tvBankCode,
                "尾号" + data?.card_num?.substring(
                    data.card_num.length - 4,
                    data.card_num.length
                ) + "储蓄卡"
            )
            GlideUtil.loadImage(data?.bank_img, holder.findViewById(R.id.imgBank))

            if (cardId == data?.card_num) {
                holder.findViewById<ImageView>(R.id.cbCard).background =
                    ViewUtils.getDrawable(R.mipmap.ic_mine_checked)
            } else holder.findViewById<ImageView>(R.id.cbCard).background =
                ViewUtils.getDrawable(R.mipmap.ic_mine_check)

            holder.itemView.setOnClickListener {
                RxBus.get().post(data?.let { MineSaveBank(it) })
            }
        }
    }


    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun saveUserBankSelect(event: MineSaveBank) {
        UserInfoSp.putSelectBankCard(event.data)
        finish()

    }

    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun upDateUserBankSelect(event: MineUpDateBank) {
        getBankList()
    }
}