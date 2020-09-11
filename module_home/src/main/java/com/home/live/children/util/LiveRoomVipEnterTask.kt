package com.home.live.children.util

import android.annotation.SuppressLint
import android.content.Context
import android.widget.TextView
import com.customer.component.spanlite.SpanBuilder
import com.customer.component.spanlite.SpanLite
import com.home.live.children.util.task.AnimUtils
import com.home.live.children.util.task.BaseTask
import java.util.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/9/1
 * @ Describe
 *
 */
class LiveRoomVipEnterTask(var context: Context, var data: String, var view: TextView?) : BaseTask() {

    //如果这个Task的执行时间是不确定的，比如上传图片，那么在上传成功后需要手动调用
    //unLockBlock方法解除阻塞

    @SuppressLint("SetTextI18n")
    override fun doTask() {
        super.doTask()
        val res = data.split(",")
        SpanLite.with(view).append(SpanBuilder.Builder("VIP"+res[0]).drawTextColor("#FF513E").drawTypeFaceBold().drawTypeFaceItalic().build())
            .append(SpanBuilder.Builder(" 贵族 ").drawTextColor("#333333").build())
            .append(SpanBuilder.Builder(res[1]).drawTextColor("#D3904E").build())
            .append(SpanBuilder.Builder(" 驾临直播间 ").drawTextColor("#333333").build()).active()
        view?.let { AnimUtils.getInAnimation(context, it) }
        Timer().schedule(object : TimerTask() {
            override fun run() {
                if (view != null) {
                    view?.post {
                        AnimUtils.getOutAnimation(context, view!!)
                        unLockBlock()
                        cancel()
                    }
                }
            }
        }, 5000)
    }

    //任务执行完的回调，可以做些释放资源或者埋点之类的操作
    override fun finishTask() {
        super.finishTask()
//        LogUtils.e("LogTask", "--finishTask-" + name);
    }


}