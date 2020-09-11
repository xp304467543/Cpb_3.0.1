package com.moment.activity

import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.view.Gravity
import com.customer.ApiRouter
import com.customer.bean.image.ImageViewInfo
import com.customer.component.dialog.DialogCommentsSuccess
import com.customer.component.dialog.GlobalDialog
import com.customer.component.input.InputPopWindow
import com.customer.data.UserInfoSp
import com.customer.data.moments.MomentsApi
import com.glide.GlideUtil
import com.lib.basiclib.base.mvp.BaseMvpActivity
import com.lib.basiclib.base.xui.adapter.recyclerview.XLinearLayoutManager
import com.lib.basiclib.base.xui.widget.imageview.nine.NineGridImageView
import com.lib.basiclib.utils.FastClickUtil
import com.lib.basiclib.utils.ToastUtils
import com.moment.R
import com.moment.adapter.MomentsAnchorInfoAdapter
import com.xiaojinzi.component.anno.RouterAnno
import com.xiaojinzi.component.impl.Router
import kotlinx.android.synthetic.main.act_hotdiscuss_info.*
import java.util.*


/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/20
 * @ Describe
 *
 */
@RouterAnno(host = "Moment", path = "AnchorInfo")
class MomentAnchorInfoActivity : BaseMvpActivity<MomentAnchorInfoActivityPresenter>() {

    var nineGridImageView: NineGridImageView<ImageViewInfo>? = null

    private var mPage = 1

    var anchorInfoAdapter: MomentsAnchorInfoAdapter? = null

    private var inputPopWindow: InputPopWindow? = null

    override fun isRegisterRxBus() = true

    override fun isSwipeBackEnable() = true

    override fun isShowBackIconWhite() = false

    override fun getPageTitle() = "评论"

    override fun getContentResID() = R.layout.act_hotdiscuss_info

    override fun attachView() = mPresenter.attachView(this)

    override fun attachPresenter() = MomentAnchorInfoActivityPresenter()


    override fun initContentView() {
        GlideUtil.loadCircleImage(this, intent.getStringExtra("avatar"), commentAnchorPhoto, true)
        commentAnchorName.text = intent.getStringExtra("name")
        commentAnchorTime.text = intent.getStringExtra("time")
        commentAnchorContent.text = intent.getStringExtra("content")
        val spannableString = SpannableString("评论 (" + intent.getStringExtra("commentNum") + ")")
        spannableString.setSpan(
            ForegroundColorSpan(Color.parseColor("#999999")),
            2,
            spannableString.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        tvTotalComment.text = spannableString
        when (intent.getIntExtra("gender", -1)) {
            1 -> imgInfoAnchorSex?.setImageResource(R.mipmap.ic_live_anchor_boy)
            2 -> imgInfoAnchorSex?.setImageResource(R.mipmap.ic_live_anchor_girl)
        }
        if (intent.getStringExtra("liveState") == "1") {
            circleWave?.setInitialRadius(50f)
            circleWave?.start()
        }
        initComment()
    }


    override fun initData() {
        val imgList = intent.getParcelableArrayListExtra<ImageViewInfo>("imgList")
        if (!imgList.isNullOrEmpty()) mPresenter.initNineImg(imgList) else setGone(
            hotDiscussInfoNine
        )
        smartRefreshLayoutHotDiscussInfo.autoRefresh()
    }

    override fun initEvent() {
        tvInput.setOnClickListener {
            if (!UserInfoSp.getIsLogin()) {
                GlobalDialog.notLogged(this@MomentAnchorInfoActivity)
                return@setOnClickListener
            }
            showInput()
        }
        commentAnchorPhoto.setOnClickListener {
            if (!FastClickUtil.isFastClick()){
                if (intent.getStringExtra("liveState") == "1"){
                    Router.withApi(ApiRouter::class.java).toLive(intent.getStringExtra("id")?:"1","",intent.getStringExtra("name")?:"1",
                        intent.getStringExtra("liveState")?:"-" ,"0","0","",intent.getStringExtra("avatar")?:"1")
                }else  Router.withApi(ApiRouter::class.java).toAnchorPage(intent.getStringExtra("id")?:"1")
            }
        }

    }

    //输入框
    private fun showInput(text: String = "") {
        if (inputPopWindow == null) {
            inputPopWindow = InputPopWindow(this)
            inputPopWindow?.setOnTextSendListener {
                //消息
                goCommend(it)
                inputPopWindow?.dismiss()
            }
        }
        if (!TextUtils.isEmpty(text)) inputPopWindow?.editTextPanel?.setText(text)
        inputPopWindow?.showAtLocation(this.window?.decorView?.rootView, Gravity.NO_GRAVITY, 0, 0)
        inputLayout.postDelayed(
            {
                if (inputPopWindow != null) {
                    inputPopWindow?.showKeyboard()
                }
            }, 50
        )
        inputPopWindow?.setOnDismissListener {
        }
    }

    private fun initComment() {
        rvHotDiscussInfo.layoutManager = XLinearLayoutManager(this)
        anchorInfoAdapter = MomentsAnchorInfoAdapter(this, intent.getStringExtra("userId") ?: "")
        rvHotDiscussInfo.adapter = anchorInfoAdapter
        smartRefreshLayoutHotDiscussInfo.setOnRefreshListener {
            mPage = 1
            mPresenter.getCommentOnList(intent.getStringExtra("id") ?: "0", mPage, true)
        }
        smartRefreshLayoutHotDiscussInfo.setOnLoadMoreListener {
            mPage++
            mPresenter.getCommentOnList(intent.getStringExtra("id") ?: "0", mPage)
        }
    }

    private fun goCommend(str: String) {
        val dialogCommentSuccess = DialogCommentsSuccess(this)
        MomentsApi.setDavisCommentReplay(intent.getStringExtra("id") ?: "-1", "", str) {
            onSuccess {
                dialogCommentSuccess.show()
                Timer().schedule(object : TimerTask() {
                    override fun run() {
                      if (dialogCommentSuccess.isShowing)  dialogCommentSuccess.dismiss()
                        this.cancel()
                    }

                }, 2000)
            }
            onFailed {   ToastUtils.showToast(it.getMsg())}
        }
    }

}