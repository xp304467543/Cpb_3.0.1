package com.mine.children

import android.net.Uri
import android.os.Build
import android.text.InputFilter
import android.widget.EditText
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.customer.component.dialog.IosBottomListWindow
import com.glide.GlideUtil
import com.lib.basiclib.base.mvp.BaseMvpActivity
import com.lib.basiclib.utils.FastClickUtil
import com.mine.R
import com.xiaojinzi.component.anno.RouterAnno
import kotlinx.android.synthetic.main.act_mine_presonal.*
import java.util.regex.Pattern

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/25
 * @ Describe
 *
 */
@RouterAnno(host = "Mine", path = "myPage")
class MinePersonalAct : BaseMvpActivity<MinePersonalActPresenter>() {
    override fun attachView() = mPresenter.attachView(this)

    override fun attachPresenter() = MinePersonalActPresenter()

    override fun getContentResID() = R.layout.act_mine_presonal

    override fun getPageTitle() = getString(R.string.mine_contact_personal)

    override fun isShowBackIconWhite() = false

    override fun isSwipeBackEnable() = true

    override fun initContentView() {
        mPresenter.initEditPersonal(publish_ed_desc, publish_text_num)
        setEditTextInputSpace(edUserName)
    }

    override fun initData() {
        mPresenter.getUserInfo()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun initEvent() {
        imgSetPhoto.setOnClickListener {
            mPresenter.getPhotoFromPhone(this)
            //相机或者相册选择
        }
        btUpLoadUserInfo.setOnClickListener {
            if (!FastClickUtil.isFastClick()) {
                mPresenter.upLoadPersonalInfo(edUserName.text.toString(), if (edUserSex.text.toString() == "男") 1 else 2, publish_ed_desc.text.toString())
            }
        }

        edUserSex.setOnClickListener {
            IosBottomListWindow(this)
                .setTitle("选择性别")
                .setItem("男") {
                    edUserSex.text = "男"
                }
                .setItem("女") {
                    edUserSex.text = "女"
                }
                .setCancelButton("取消")
                .show()
        }
    }

    fun setImgAvatar(uri: Uri){
        GlideUtil.loadCircleImage(this,uri,imgUserPhoto,true)
    }

    /**
     * 禁止EditText输入空格、表情和换行符以及特殊符号&&
     *
     * @param editText EditText输入框
     */
    private fun setEditTextInputSpace(editText: EditText) {
        val emoji = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
            Pattern.UNICODE_CASE or Pattern.CASE_INSENSITIVE)

        val filter = InputFilter { source, start, end, dest, dstart, dend ->
            val emojiMatcher = emoji.matcher(source)
            //禁止特殊符号
            val speChat = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]"
            val pattern = Pattern.compile(speChat)
            val matcher = pattern.matcher(source.toString())


            //禁止输入空格
            if (source == " " || source.toString().contentEquals("\n")) {
                ""
                //禁止输入表情
            } else if (emojiMatcher.find()) {
                ""
            } else if (matcher.find()) {
                ""
            } else {
                null
            }
        }
        editText.filters = arrayOf(filter, InputFilter.LengthFilter(10))
    }
}