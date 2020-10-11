package com.mine.children

import android.text.TextUtils
import androidx.core.text.HtmlCompat
import com.customer.ApiRouter
import com.lib.basiclib.base.activity.BaseNavActivity
import com.mine.R
import com.customer.data.mine.MineApi
import com.customer.data.mine.MineMessageCenter
import com.xiaojinzi.component.anno.RouterAnno
import com.xiaojinzi.component.impl.Router
import com.customer.component.dialog.GlobalDialog
import com.customer.data.UserInfoSp
import cuntomer.them.AppMode
import kotlinx.android.synthetic.main.act_message_center.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/25
 * @ Describe
 *
 */
@RouterAnno(host = "Mine", path = "message")
class MineMessageCenterAct : BaseNavActivity() {

    private var list1: List<MineMessageCenter>? = null
    private var list2: List<MineMessageCenter>? = null
    private var list3: List<MineMessageCenter>? = null

    override fun isShowBackIconWhite() = false

    override fun isSwipeBackEnable() = true

    override fun getPageTitle() = "消息中心"

    override fun getContentResID() = R.layout.act_message_center


    override fun initContentView() {
        val msg1 = intent?.getStringExtra("msg1") ?: "0"
        val msg2 = intent?.getStringExtra("msg2") ?: "0"
        val msg3 = intent?.getStringExtra("msg3") ?: "0"
        if (!TextUtils.isEmpty(msg2) && msg2 != "0") {
            setVisible(tvMessageNum)
            tvMessageNum.text = msg2
        }
        if (!TextUtils.isEmpty(msg3) && msg3 != "0") {
            setVisible(tvMessageNum2)
            tvMessageNum2.text = msg3
        }
        if (!TextUtils.isEmpty(msg1) && msg1 != "0") {
            setVisible(tvMessageNum3)
            tvMessageNum3.text = msg1
        }
        when (UserInfoSp.getAppMode()) {
            AppMode.Pure -> {
                setGone(lin1)
                setGone(viewLin)
            }
        }

    }


    override fun initData() {
        showPageLoadingDialog()
        getSystemMsg()
    }

    private fun getSystemMsg() {
        MineApi.getMessageTips("0") {
            onSuccess {
                if (!it.isNullOrEmpty()) {
                    tv3_time.text = it[0].createtime_txt
                    tv3_content.text =
                        HtmlCompat.fromHtml(it[0].content, HtmlCompat.FROM_HTML_MODE_COMPACT)
                    list3 = it
                }
            }
            onFailed {
                GlobalDialog.showError(this@MineMessageCenterAct, it)
            }

        }

        MineApi.getMessageTips("2") {
            onSuccess {
                if (!it.isNullOrEmpty()) {
                    tv1_time.text = it[0].createtime_txt
                    tv1_content.text =
                        HtmlCompat.fromHtml(it[0].content, HtmlCompat.FROM_HTML_MODE_COMPACT)
                    list1 = it
                }
            }
            onFailed {
                GlobalDialog.showError(this@MineMessageCenterAct, it)
            }

        }

        MineApi.getMessageTips("3") {
            onSuccess {
                if (!it.isNullOrEmpty()) {
                    tv2_time.text = it[0].createtime_txt
                    tv2_content.text =
                        HtmlCompat.fromHtml(it[0].content, HtmlCompat.FROM_HTML_MODE_COMPACT)
                    list2 = it
                }
            }
            onFailed {
                GlobalDialog.showError(this@MineMessageCenterAct, it)
            }
            hidePageLoadingDialog()
        }
    }


    override fun initEvent() {
        lin1.setOnClickListener {
            setGone(tvMessageNum)
            Router.withApi(ApiRouter::class.java).toMineMessageInfo(0)
        }
        lin2.setOnClickListener {
            setGone(tvMessageNum2)
            Router.withApi(ApiRouter::class.java).toMineMessageInfo(1)
        }
        lin3.setOnClickListener {
            setGone(tvMessageNum3)
            Router.withApi(ApiRouter::class.java).toMineMessageInfo(2)
        }
    }

}