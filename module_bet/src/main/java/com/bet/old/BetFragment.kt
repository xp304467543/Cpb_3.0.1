package com.bet.old

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.bet.R
import com.customer.component.web.ZpWebChromeClient
import com.customer.data.LineCheck
import com.customer.data.UserInfoSp
import com.customer.data.WebSelect
import com.customer.data.mine.ChangeSkin
import com.customer.utils.RxPermissionHelper
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.thread.EventThread
import com.lib.basiclib.base.mvp.BaseMvpFragment
import com.lib.basiclib.utils.StatusBarUtils
import com.lib.basiclib.utils.ToastUtils
import com.lib.basiclib.utils.ViewUtils
import com.xiaojinzi.component.anno.RouterAnno
import cuntomer.them.ITheme
import cuntomer.them.Theme
import kotlinx.android.synthetic.main.fragment_bet.*


@RouterAnno(host = "BetMain", path = "main")
class BetFragment : BaseMvpFragment<BetPresenter>(), ITheme {

    var initCurrent = 1

    var lotteryUrl = "-1"

    var gameUrl = "-1"

    var isLoad = false

    var isLoad2 = false

    override fun attachView() = mPresenter.attachView(this)

    override fun attachPresenter() = BetPresenter()

    override fun getLayoutResID() = R.layout.fragment_bet

    override fun isRegisterRxBus() = true

    override fun initContentView() {
        setSwipeBackEnable(false)
        StatusBarUtils.setStatusBarHeight(betStateView)
        setTheme(UserInfoSp.getThem())
        initWeb()
    }


    override fun initEvent() {
        tvLineDelay.setOnClickListener {
            showLinePop()
        }
        betRefresh.setOnClickListener {
            if (initCurrent == 1) {
                lotteryWeb.reload()
            } else gameWeb.reload()
        }
    }

    var mLDNetPingService: NetPingManager? = null
    fun loadPing(url: String) {
        try {
            var real = url.split("//")[1]
            if (real.contains("/")) real = real.split("/")[0]
            mLDNetPingService = NetPingManager(
                context,
                real,
                object : NetPingManager.IOnNetPingListener {
                    @SuppressLint("SetTextI18n")
                    override fun ontDelay(log: Long) {
                        mLDNetPingService?.release()
                        post {
                            tvLineDelay.text = (log / 5).toString() + "ms"
                            if ((log / 5) > 100) {
                                tvLineDelay.setTextColor(ViewUtils.getColor(R.color.colorYellow))
                            } else {
                                tvLineDelay.setTextColor(ViewUtils.getColor(R.color.colorGreen))
                            }
                        }
                    }

                    override fun onError() {
                        mLDNetPingService?.release()
                    }

                })
            mLDNetPingService?.getDelay()
        } catch (e: Exception) {
        }
    }

    var linePop: LotteryLinePop? = null
    var listCheck: ArrayList<LineCheck>? = null
    var listCheck2: ArrayList<LineCheck>? = null
    var itemPosChecked = 0
    var itemPosChecked1 = 0

    @SuppressLint("SetTextI18n")
    private fun showLinePop() {
        linePop = if (initCurrent == 1) {
            context?.let {
                LotteryLinePop(
                    it,
                    listCheck!!,
                    itemPosChecked
                )
            }
        } else {
            context?.let {
                LotteryLinePop(
                    it,
                    listCheck2!!,
                    itemPosChecked1
                )
            }
        }
        linePop?.showAtLocationBottom(tvLineDelay, 0f)
        linePop?.setSelectListener { it, pos, ms ->
            tvLineOffset.text = "线路" + (pos + 1)
            tvLineDelay.text = ms + "ms"
            if (ms.toInt() > 100) {
                tvLineDelay.setTextColor(ViewUtils.getColor(R.color.colorYellow) )
            } else {
                tvLineDelay.setTextColor(ViewUtils.getColor(R.color.colorGreen) )
            }
            if (initCurrent == 1) {
                lotteryWeb?.loadUrl(it)
            } else {
                gameWeb?.loadUrl(it)
            }
            if (initCurrent == 1) itemPosChecked = pos else itemPosChecked1 = pos
        }
        linePop?.showAtLocationBottom(tvLineDelay, 10f)
    }


    private var mUploadMsg: ValueCallback<Uri>? = null
    private var mUploadMsgs: ValueCallback<Array<Uri>>? = null
    private fun initWeb() {
        lotteryWeb.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                try {
                    if (url.startsWith("weixin://") || url.startsWith("alipays://") ||
                        url.startsWith("mailto://") || url.startsWith("tel://") || url.startsWith("tel:") || url.startsWith(
                            "mqq://"
                        )
                    ) {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        startActivity(intent)
                        return true
                    }//其他自定义的scheme
                } catch (e: Exception) { //防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)
                    return false
                }
                return false
            }
        }
        gameWeb.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                try {
                    if (url.startsWith("weixin://") || url.startsWith("alipays://") ||
                        url.startsWith("mailto://") || url.startsWith("tel://") || url.startsWith("tel:") || url.startsWith(
                            "mqq://"
                        )
                    ) {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        startActivity(intent)
                        return true
                    }//其他自定义的scheme
                } catch (e: Exception) { //防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)
                    return false
                }
                return false
            }
        }
        lotteryWeb.setOpenFileChooserCallBack(object : ZpWebChromeClient.OpenFileChooserCallBack {
            override fun openFileChooserCallBack(
                uploadMsg: ValueCallback<Uri>,
                acceptType: String
            ) {
                mUploadMsg = uploadMsg
                checkPermission()

            }

            override fun showFileChooserCallBack(
                filePathCallback: ValueCallback<Array<Uri>>,
                fileChooserParams: WebChromeClient.FileChooserParams
            ) {
                if (mUploadMsgs != null) {
                    mUploadMsgs!!.onReceiveValue(null)
                }
                mUploadMsgs = filePathCallback
                checkPermission()
            }
        })
        gameWeb.setOpenFileChooserCallBack(object : ZpWebChromeClient.OpenFileChooserCallBack {
            override fun openFileChooserCallBack(
                uploadMsg: ValueCallback<Uri>,
                acceptType: String
            ) {
                mUploadMsg = uploadMsg
                checkPermission()

            }

            override fun showFileChooserCallBack(
                filePathCallback: ValueCallback<Array<Uri>>,
                fileChooserParams: WebChromeClient.FileChooserParams
            ) {
                if (mUploadMsgs != null) {
                    mUploadMsgs!!.onReceiveValue(null)
                }
                mUploadMsgs = filePathCallback
                checkPermission()
            }
        })
    }


    /**
     * 选择图片弹框
     */
    val REQUEST_SELECT_FILE_CODE = 100
    private val REQUEST_FILE_CHOOSER_CODE = 101

    //检测权限
    fun checkPermission() {
        if (RxPermissionHelper.checkPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            try {
                val getIntent = Intent(Intent.ACTION_GET_CONTENT)
                getIntent.type = "image/*"
                val pickIntent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                pickIntent.type = "image/*"
                val chooserIntent = Intent.createChooser(getIntent, "Select Image")
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))
                startActivityForResult(chooserIntent, REQUEST_SELECT_FILE_CODE)
            } catch (e: ActivityNotFoundException) {
                mUploadMsgs = null
                e.printStackTrace()
            }
        } else {
            if (RxPermissionHelper.request(this, android.Manifest.permission.CAMERA).isDisposed) {
                ToastUtils.showToast("权限拒绝")
            } else {
                if (mUploadMsgs != null) {
                    mUploadMsgs?.onReceiveValue(null)
                    mUploadMsgs = null
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_SELECT_FILE_CODE -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (mUploadMsgs == null) {
                        return
                    }
                    mUploadMsgs?.onReceiveValue(
                        android.webkit.WebChromeClient.FileChooserParams.parseResult(
                            resultCode,
                            data
                        )
                    )
                    mUploadMsgs = null
                }
            }
            REQUEST_FILE_CHOOSER_CODE -> {
                if (mUploadMsg == null) {
                    return
                }
                val result =
                    if (data == null || resultCode != Activity.RESULT_OK) null else data.data
                mUploadMsg?.onReceiveValue(result)
                mUploadMsg = null
            }
        }
    }


    override fun onBackClick() {
        if (initCurrent == 1) {
            if (lotteryWeb.canGoBack()) {
                lotteryWeb.goBack()
            } else super.onBackClick()
        } else if (initCurrent == 2) {
            if (gameWeb.canGoBack()) {
                gameWeb.goBack()
            } else super.onBackClick()
        }
    }




    override fun onPause() {
        lotteryWeb.onPause()
        gameWeb.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        if (lotteryWeb != null) {
            lotteryWeb?.removeAllViews()
            lotteryWeb?.destroy()
        }
        if (gameWeb != null) {
            gameWeb.removeAllViews()
            gameWeb?.destroy()
        }
        super.onDestroy()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        if (hidden){
            lotteryWeb.onPause()
            gameWeb.onPause()
        }
        super.onHiddenChanged(hidden)
    }


    //主题
    @SuppressLint("ResourceType")
    override fun setTheme(theme: Theme) {
        when (theme) {
            Theme.Default -> imgBetBg.setImageResource(R.drawable.ic_them_default_top)
            Theme.NewYear -> imgBetBg.setImageResource(R.drawable.ic_them_newyear_top)
            Theme.MidAutumn -> imgBetBg.setImageResource(R.drawable.ic_them_middle_top)
            Theme.LoverDay -> imgBetBg.setImageResource(R.drawable.ic_them_love_top)
        }
    }


    //换肤
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun changeSkin(eventBean: ChangeSkin) {
        when (eventBean.id) {
            1 -> setTheme(Theme.Default)
            2 -> setTheme(Theme.NewYear)
            3 -> setTheme(Theme.MidAutumn)
            4 -> setTheme(Theme.LoverDay)
            5 ->setTheme(Theme.NationDay)
        }

    }


    /**
     * 选择web
     */
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun webSelect(eventBean: WebSelect) {
        initCurrent = eventBean.pos
        if (isActive()) {
            if (initCurrent == 1) {
                ViewUtils.setVisible(lotteryWeb)
                ViewUtils.setGone(gameWeb)
                if (listCheck.isNullOrEmpty()) {
                    mPresenter.getUrl()
                }else{
                    if (!isLoad) {
                        if (listCheck?.get(0)?.url!=null)   lotteryWeb.loadUrl(listCheck?.get(0)?.url)
                        isLoad = true
                    }else  lotteryWeb.onResume()
                }
                if (isLoad2)gameWeb.onPause()
            } else {
                ViewUtils.setVisible(gameWeb)
                ViewUtils.setGone(lotteryWeb)
                if (listCheck2.isNullOrEmpty()){
                    mPresenter.getUrl()
                }else {
                    if (!isLoad2) {
                      if (listCheck2?.get(0)?.url!=null)  gameWeb.loadUrl(listCheck2?.get(0)?.url)
                        isLoad2 = true
                    }else gameWeb.onResume()
                }
                if (isLoad)lotteryWeb.onPause()
            }
        }
    }

}
