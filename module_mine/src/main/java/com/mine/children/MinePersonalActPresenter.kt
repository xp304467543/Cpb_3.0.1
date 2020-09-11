package com.mine.children

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.customer.component.dialog.IosBottomListWindow
import com.customer.component.utakephoto.crop.CropOptions
import com.customer.component.utakephoto.exception.TakeException
import com.customer.component.utakephoto.manager.ITakePhotoResult
import com.customer.component.utakephoto.manager.UTakePhoto
import com.customer.data.UserInfoSp
import com.glide.GlideUtil
import com.google.gson.JsonParser
import com.hwangjr.rxbus.RxBus
import com.lib.basiclib.base.mvp.BaseMvpPresenter
import com.lib.basiclib.utils.ToastUtils
import com.lib.basiclib.utils.ViewUtils
import com.mine.R
import com.customer.data.mine.MineApi
import com.customer.data.mine.UpDateUserPhoto
import com.customer.component.dialog.GlobalDialog
import kotlinx.android.synthetic.main.act_mine_presonal.*
import okhttp3.*
import java.io.ByteArrayOutputStream
import java.io.IOException

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/25
 * @ Describe
 *
 */
class MinePersonalActPresenter : BaseMvpPresenter<MinePersonalAct>() {



    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun getPhotoFromPhone(activity: Activity) {
        if (mView.isActive()) {
            IosBottomListWindow(activity)
                .setTitle("添加头像")
                .setItem("拍摄", ViewUtils.getColor(R.color.black)) {
                    openOp(1)
                }
                .setItem("从手机相册选择") {
                    openOp(2)
                }
                .setCancelButton("取消")
                .show()
        }

    }

    private fun openOp(mode: Int) {
        val cropOptions = CropOptions.Builder()
            .setAspectX(1).setAspectY(1)
            .setOutputX(100).setOutputY(1)
            .setWithOwnCrop(true)//使用系统裁剪还是自带裁剪
            .create()
        when (mode) {
            1 -> {
                UTakePhoto.with(mView).openCamera().setCrop(cropOptions).build(object : ITakePhotoResult {
                    override fun takeSuccess(uriList: MutableList<Uri>?) {
                        val uri = uriList?.get(0)!!
                        mView.setImgAvatar(uriList[0])
                        val bitmap = BitmapFactory.decodeStream(mView.contentResolver.openInputStream(uri))
                        upLoadPersonalAvatar(bitmap)
                    }

                    override fun takeFailure(ex: TakeException?) {
                    }

                    override fun takeCancel() {
                    }

                })
            }

            2 -> {
                UTakePhoto.with(mView).openAlbum().setCrop(cropOptions).build(object : ITakePhotoResult {
                    override fun takeSuccess(uriList: MutableList<Uri>?) {
                        val uri = uriList?.get(0)!!
                        mView.setImgAvatar(uriList[0])
                        val bitmap = BitmapFactory.decodeStream(mView.contentResolver.openInputStream(uri))
                        upLoadPersonalAvatar(bitmap)

                    }

                    override fun takeFailure(ex: TakeException?) {
                    }

                    override fun takeCancel() {
                    }

                })
            }
        }
    }

    //上传个人资料
    fun upLoadPersonalInfo(nickName: String, gender: Int, profile: String) {
        MineApi.upLoadPersonalInfo(nickName, gender, profile) {
            mView.showPageLoadingDialog()
            onSuccess {
                mView.hidePageLoadingDialog()
                UserInfoSp.putUserNickName(nickName)
                UserInfoSp.putUserSex(gender)
                UserInfoSp.putUserProfile(profile)
                ToastUtils.showToast("信息修改成功")
                mView.pop()
            }
            onFailed {
                mView.hidePageLoadingDialog()
                GlobalDialog.showError(mView, it)
            }
        }
    }

    //上传个人头像
    fun upLoadPersonalAvatar(bitmap: Bitmap) {
        mView.showPageLoadingDialog()
        val builder = MultipartBody.Builder()
            .setType(MultipartBody.FORM)//表单类型
            .addFormDataPart("avatar", "data:image/png;base64," + bitMapToBase64(bitmap))
        val requestBody = builder.build()
        val request = Request.Builder()
            .url(MineApi.getBaseUrlMe() + MineApi.USER_UPLOAD_AVATAR)
            .addHeader("Authorization", UserInfoSp.getTokenWithBearer().toString())
            .post(requestBody)
            .build()
        val client = OkHttpClient.Builder().build()
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (mView.isActive()) {
                    Looper.prepare()
                    try {
                        val json = JsonParser.parseString(response.body?.string()!!).asJsonObject
                        if (json.get("code").asInt == 1) {
                            mView.hidePageLoadingDialog()
                            ToastUtils.showToast("头像修改成功")
                            val uel =json.get("data").asJsonObject.get("avatar").asString
                            RxBus.get().post(UpDateUserPhoto(uel))
//                            mView.setImgAvatar(uel)
                        } else {
                            mView.hidePageLoadingDialog()
                            ToastUtils.showToast(json.get("msg").asString)
                        }
                    } catch (e: Exception) {
                        mView.hidePageLoadingDialog()
                        e.printStackTrace()
                        ToastUtils.showToast("服务器异常,上传失败")
                    }
                    Looper.loop()
                }

            }

            override fun onFailure(call: Call, e: IOException) {
                Looper.prepare()
                mView.hidePageLoadingDialog()
                ToastUtils.showToast(e.toString())
                Looper.loop()
            }
        })
    }


    //获取用户信息
    fun getUserInfo() {
        mView.showPageLoadingDialog()
        MineApi.getUserInfo {
            onSuccess {
                if (mView.isActive()) {
                    UserInfoSp.putUserName(it.username?:"")
                    UserInfoSp.putUserPhoto(it.avatar?:"")
                    UserInfoSp.putUserNickName(it.nickname?:"")
                    UserInfoSp.putUserSex(it.gender)
                    UserInfoSp.putUserPhone(it.phone?:"")
                    UserInfoSp.putUserProfile(it.profile?:""?:"")
                    GlideUtil.loadCircleImage(mView,it.avatar, mView.imgUserPhoto,true)
                    mView.edUserName.setText(it.nickname)
                    when (it.gender) {
                        1 -> mView.edUserSex.text = "男"
                        2 -> mView.edUserSex.text = "女"
                        else -> mView.edUserSex.text = "未知"
                    }
                    mView.publish_ed_desc.setText(it.profile)
                }
                mView.hidePageLoadingDialog()
            }
            onFailed {
                mView.hidePageLoadingDialog()
                GlobalDialog.showError(mView, it)
            }
        }

    }

    private fun bitMapToBase64(bitmap: Bitmap): String {
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)//第二个入参表示图片压缩率，如果是100就表示不压缩
        val bytes = bos.toByteArray()
        return Base64.encodeToString(bytes, Base64.NO_WRAP)
    }

    //输入限制
    fun initEditPersonal(editText: EditText, textView: TextView) {
        val num = 0
        val mMaxNum = 50
        editText.addTextChangedListener(object : TextWatcher {
            //记录输入的字数
            var wordNum: CharSequence? = null
            var selectionStart: Int = 0
            var selectionEnd: Int = 0

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                wordNum = s
            }

            @SuppressLint("SetTextI18n")
            override fun afterTextChanged(s: Editable?) {
                val number = num + s!!.length
                textView.text = "$number/50"
                selectionStart = editText.selectionStart
                selectionEnd = editText.selectionEnd
                //判断大于最大值
                if (wordNum!!.length > mMaxNum) {
                    s.delete(selectionStart - 1, selectionEnd)
                    val tempSelection = selectionEnd
                    editText.text = s
                    editText.setSelection(tempSelection)
                    ToastUtils.showToast("最多输入50字")
                }
            }
        })
    }
}