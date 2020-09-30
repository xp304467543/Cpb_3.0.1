package com.home.children

import android.annotation.SuppressLint
import androidx.appcompat.widget.AppCompatButton
import com.customer.ApiRouter
import com.customer.component.dialog.DialogTask
import com.customer.data.home.HomeApi
import com.customer.data.home.UserTask
import com.home.R
import com.lib.basiclib.base.activity.BaseNavActivity
import com.lib.basiclib.base.recycle.BaseRecyclerAdapter
import com.lib.basiclib.base.recycle.RecyclerViewHolder
import com.lib.basiclib.base.xui.adapter.recyclerview.XLinearLayoutManager
import com.lib.basiclib.utils.FastClickUtil
import com.lib.basiclib.utils.StatusBarUtils
import com.lib.basiclib.utils.ToastUtils
import com.lib.basiclib.utils.ViewUtils
import com.xiaojinzi.component.impl.Router
import kotlinx.android.synthetic.main.act_new_hand_task.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/9/29
 * @ Describe
 *
 */
class HomeNewHandTask : BaseNavActivity() {

    private var taskAdapter: TaskAdapter? = null

    override fun isShowToolBar() = false

    override fun isSwipeBackEnable() = true

    override fun isOverride() = true

    override val layoutResID = R.layout.act_new_hand_task

    override fun initContentView() {
       if (taskStateView!=null) StatusBarUtils.setStatusBarHeight(taskStateView)
        taskSmartRefreshLayout?.setEnableOverScrollBounce(true)//是否启用越界回弹
        taskSmartRefreshLayout?.setEnableOverScrollDrag(true)//是否启用越界拖动（仿苹果效果）
        taskSmartRefreshLayout?.setEnableRefresh(true)//是否启用下拉刷新功能
        taskSmartRefreshLayout?.setEnableLoadMore(false)//是否启用上拉加载功能
        taskAdapter = TaskAdapter()
        rvTaskList?.adapter = taskAdapter
        rvTaskList?.layoutManager = XLinearLayoutManager(this)
    }

    override fun initData() {
//        getUserTask()
    }


    override fun initEvent() {
        imgBackView.setOnClickListener {
            finish()
        }
        taskSmartRefreshLayout.setOnRefreshListener {
            getUserTask()
        }
    }


    private fun getUserTask() {
        HomeApi.userTask {
            onSuccess {
                taskAdapter?.refresh(it)
                taskSmartRefreshLayout?.finishRefresh()
            }
            onFailed {
                ToastUtils.showToast(it.getMsg())
                taskSmartRefreshLayout?.finishRefresh()
            }
        }
    }

    inner class TaskAdapter : BaseRecyclerAdapter<UserTask>() {

        override fun getItemLayoutId(viewType: Int) = R.layout.adapter_user_task

        @SuppressLint("SetTextI18n")
        override fun bindData(holder: RecyclerViewHolder, position: Int, data: UserTask?) {
            holder.text(R.id.tvTitle, data?.title)
            val btTask = holder.findViewById<AppCompatButton>(R.id.btTask)
            when {
                data?.status == -1 -> {
                    btTask?.text = "领取"
                    btTask?.background = ViewUtils.getDrawable(R.drawable.button_task_red_background)
                    btTask?.setTextColor(ViewUtils.getColor(R.color.white))
                }
                data?.status == -2 -> {
                    btTask?.text = "已完成"
                    btTask?.background = ViewUtils.getDrawable(R.drawable.button_task_grey_background)
                    btTask?.setTextColor(ViewUtils.getColor(R.color.white))
                }
                data?.status == -3 -> {
                    btTask?.text = "已过期"
                    btTask?.background = ViewUtils.getDrawable(R.drawable.button_task_grey_background)
                    btTask?.setTextColor(ViewUtils.getColor(R.color.white))
                }
                data?.status == -4 -> {
                    btTask?.text = "已领取"
                    btTask?.background = ViewUtils.getDrawable(R.drawable.button_task_grey_background)
                    btTask?.setTextColor(ViewUtils.getColor(R.color.white))
                }
                data?.status ?: 0 >= 0 -> {
                    when (data?.jump) {
                        0 -> {
                            btTask?.background = ViewUtils.getDrawable(R.drawable.button_task_red_line_background)
                            btTask?.text = data.archive + "/" + data.target
                            btTask?.setTextColor(ViewUtils.getColor(R.color.color_FF513E))
                        }
                        1-> {
                            //个人资料
                            btTask?.text = "去完成"
                            btTask?.background = ViewUtils.getDrawable(R.drawable.button_task_red_background)
                            btTask?.setTextColor(ViewUtils.getColor(R.color.white))
                        }

                        2-> {
                            //充值页面
                            btTask?.text = "去充值"
                            btTask?.background = ViewUtils.getDrawable(R.drawable.button_task_red_background)
                            btTask?.setTextColor(ViewUtils.getColor(R.color.white))
                        }
                    }
                }
            }
            btTask?.setOnClickListener {
                if (!FastClickUtil.isFastClick()) {
                    when {
                        data?.status == -1 -> {
                            //领取
                            getNewTask(data.task_id.toString())
                        }
                        data?.status ?: 0 >= 0 -> {
                            when (data?.jump) {
                                1->{
                                    //个人资料
                                    Router.withApi(ApiRouter::class.java).toMyPage()
                                }
                                2->{
                                    //充值页面
                                    Router.withApi(ApiRouter::class.java).toMineRecharge(0)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun getNewTask(id:String){
        HomeApi.getNewTask(id){
            onSuccess {
               val dialog = DialogTask(this@HomeNewHandTask,it.gift_type?:0,it.amount.toString())
                dialog.setOnDismissListener { getUserTask() }
                dialog.show()
            }
            onFailed {
                ToastUtils.showToast(it.getMsg())
            }
        }
    }

    override fun onResume() {
        getUserTask()
        super.onResume()
    }
}