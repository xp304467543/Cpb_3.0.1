package com.home.live.bet.new

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.customer.data.lottery.LotteryPlayListResponse
import com.customer.data.lottery.PlaySecData
import com.customer.data.lottery.PlaySecDataKj
import com.customer.data.lottery.PlayUnitData
import com.home.R
import com.home.live.bet.old.BaseNormalFragment
import com.home.live.bet.old.LotteryLiveBet
import com.hwangjr.rxbus.RxBus
import com.lib.basiclib.base.recycle.BaseRecyclerAdapter
import com.lib.basiclib.base.recycle.RecyclerViewHolder
import com.lib.basiclib.utils.LogUtils
import com.lib.basiclib.utils.ToastUtils
import com.lib.basiclib.utils.ViewUtils
import kotlinx.android.synthetic.main.fragment_live_room_bet_content.*
import okhttp3.internal.notify

/**
 *
 * @ Author  QinTian
 * @ Date  11/18/20
 * @ Describe
 *
 */
class LiveRoomBetFragmentContent : BaseNormalFragment() {

    private var lotteryPlayListResponse: ArrayList<LotteryPlayListResponse>? = null
    var firstData: MutableList<LotteryPlayListResponse>? = null

    var rightTopAdapter: RightTopAdapter? = null

    var rightTopXgcAdapter: RightTopXgcAdapter? = null //(香港彩)

    var lmAdapter: AdapterLm? = null //两面

    var kjAdapter: AdapterKj? = null //快捷

    var dan1D10Adapter: AdapterDan1D10? = null //单号1-10

    var gyhAdapter: AdapterGYH? = null //亚冠和

    var zhAdapter: AdapterZH? = null //整合

    var dmAdapter: AdapterDM? = null //单码

    var xgcTmAdapter: AdapterXgcTm? = null //特码(香港彩)

    /**
     * 福彩3D
     */
    var fc3DAdapter: Fc3dAdapter? = null
    var fc3DTopAdapter: Right3DTopAdapter? = null

    var betList = mutableListOf<PlaySecData>()  //投注集合

    var lotteryId = "-1" //彩种ID

    var rightTop = "-1"

    var currentLeft = "-1"

    private var rvType: RecyclerView? = null
    private var rvContent: RecyclerView? = null
    private var layoutOdds: LinearLayout? = null
    private var layout3D: LinearLayout? = null
    private var layout3D3z: LinearLayout? = null
    private var tv3d3z1: TextView? = null
    private var tv3d3z2: TextView? = null
    private var tv3d3z3: TextView? = null
    private var tv3dRight: TextView? = null
    private var tv3dLeft: TextView? = null
    private var tvHx: TextView? = null
    private var tvTitle: TextView? = null
    private var contentHolder:TextView?=null


    override fun getLayoutRes(): Int = R.layout.fragment_live_room_bet_content

    override fun initView(rootView: View?) {
        rvType = rootView?.findViewById(R.id.rvType)
        rvContent = rootView?.findViewById(R.id.rvContent)
        tvHx = rootView?.findViewById(R.id.tvHx)
        layoutOdds = rootView?.findViewById(R.id.layoutOdds)
        layout3D = rootView?.findViewById(R.id.layout3D)
        layout3D3z = rootView?.findViewById(R.id.layout3D3z)
        tv3d3z1 = rootView?.findViewById(R.id.tv3d3z1)
        tv3d3z2 = rootView?.findViewById(R.id.tv3d3z2)
        tv3d3z3 = rootView?.findViewById(R.id.tv3d3z3)
        tv3dRight = rootView?.findViewById(R.id.tv3dRight)
        tv3dLeft = rootView?.findViewById(R.id.tv3dLeft)
//        tvTitle = rootView?.findViewById(R.id.tvTitle)
        contentHolder = rootView?.findViewById(R.id.contentHolder)
    }


    override fun initData() {
        lotteryPlayListResponse = arguments?.getParcelableArrayList("LotteryPlayListResponse")
        lotteryId = arguments?.getString("lotteryId") ?: "null"
        currentLeft = arguments?.getString("currentType") ?: "null"
        lotteryPlayListResponse?.let { modifyData(it) }
        rvContent?.isFocusable = false
        rvContent?.isNestedScrollingEnabled = false
    }


    private fun modifyData(it: MutableList<LotteryPlayListResponse>) {
        if (it.isNullOrEmpty()) return
        firstData = it
        val typeList = arrayListOf<String>()
        for (item in firstData!!) {
            typeList.add(item.play_unit_name ?: "未知")
        }
        firstData?.get(arguments?.getInt("currentIndex") ?: 0)?.let { it1 -> modifyContent(it1) }
    }


    private fun modifyContent(it: LotteryPlayListResponse) {
        if (!isAdded) return
        if (rvContent == null) return
        val name = if (lotteryId != "8") {
            it.play_unit_name.toString()
        } else it.play_unit_name + "xgc"
        when (name) {
            "两面", "两面xgc" -> {
                lmAdapter = AdapterLm()
                rvContent?.adapter = lmAdapter
                val layoutManager = GridLayoutManager(context, 12)
                rvContent?.layoutManager = layoutManager
                layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return when (lmAdapter?.getItemViewType(position)) {
                            lmAdapter?.ITEM_TYPE_HEAD -> 12
                            lmAdapter?.ITEM_TYPE_CONTENT_COUNT_4 -> 3
                            else -> 4
                        }
                    }
                }
            }
            "整合", "第一球", "第二球", "第三球", "第四球", "第五球", "正码1-6xgc", "色波xgc", "平特一肖尾数xgc", "特肖xgc" -> {
                zhAdapter = AdapterZH()
                rvContent?.adapter = zhAdapter
                val layoutManager = when (name) {
                    "色波xgc" -> GridLayoutManager(context, 12)
                    else -> GridLayoutManager(context, 4)
                }
                rvContent?.layoutManager = layoutManager
                layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return when (zhAdapter?.getItemViewType(position)) {
                            zhAdapter?.ITEM_TYPE_HEAD -> {
                                when (name) {
                                    "色波xgc" -> 12
                                    else -> 4
                                }
                            }
                            zhAdapter?.ITEM_TYPE_CONTENT_COUNT_5 -> {
                                when (name) {
                                    "色波xgc" -> 3
                                    else -> 1
                                }
                            }
                            zhAdapter?.ITEM_TYPE_CONTENT_COUNT_3 -> 4
                            else -> 1
                        }
                    }
                }
            }
            "快捷" -> {
                kjAdapter = AdapterKj()
                rvContent?.adapter = kjAdapter
                val layoutManager = GridLayoutManager(context, 10)
                rvContent?.layoutManager = layoutManager
                layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return when (kjAdapter?.getItemViewType(position)) {
                            kjAdapter?.ITEM_TYPE_CONTENT_COUNT_5 -> 2
                            kjAdapter?.ITEM_TYPE_CONTENT -> 10
                            else -> 5
                        }
                    }
                }
            }
            "单号1-10" -> {
                dan1D10Adapter = AdapterDan1D10()
                rvContent?.adapter = dan1D10Adapter
                val layoutManager = GridLayoutManager(context, 4)
                rvContent?.layoutManager = layoutManager
                layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return when (dan1D10Adapter?.getItemViewType(position)) {
                            dan1D10Adapter?.ITEM_TYPE_CONTENT_COUNT_5 -> 1
                            else -> 4
                        }
                    }
                }
            }
            "冠亚军组合" -> {
                gyhAdapter = AdapterGYH()
                rvContent?.adapter = gyhAdapter
                val layoutManager = GridLayoutManager(context, 4)
                rvContent?.layoutManager = layoutManager
            }
            "单码", "连码", "斗牛" -> {
                rightTopAdapter = RightTopAdapter()
                rvType?.setPadding(0, 0, 10, 0)
                rvType?.adapter = rightTopAdapter
                rvType?.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                dmAdapter = AdapterDM()
                rvContent?.adapter = dmAdapter
                rvContent?.layoutManager = GridLayoutManager(context, 4)
            }
            "特码xgc", "正码特xgc", "连码xgc", "连肖连尾xgc", "自选不中xgc" -> {
                rightTopXgcAdapter = RightTopXgcAdapter()
                rvType?.adapter = rightTopXgcAdapter
                rvType?.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                xgcTmAdapter = AdapterXgcTm()
                rvContent?.adapter = xgcTmAdapter
                val layoutManager = GridLayoutManager(context, 4)
                rvContent?.layoutManager = layoutManager
                layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return when (xgcTmAdapter?.getItemViewType(position)) {
                            xgcTmAdapter?.TYPE_1, xgcTmAdapter?.TYPE_2 -> 1
                            xgcTmAdapter?.TYPE_6 -> 2
                            else -> 4
                        }
                    }
                }
            }
            "主势盘", "一字组合", "二字组合", "三字组合", "一字定位", "三字和数", "跨度" -> {
                fc3DTopAdapter = Right3DTopAdapter()
                rvType?.adapter = fc3DTopAdapter
                rvType?.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                fc3DAdapter = Fc3dAdapter()
                rvContent?.adapter = fc3DAdapter
                val layoutManager = GridLayoutManager(context, 12)
                rvContent?.layoutManager = layoutManager
                layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return when (fc3DAdapter?.getItemViewType(position)) {
                            fc3DAdapter?.TYPE_1 -> 12
                            fc3DAdapter?.TYPE_2 -> 6
                            fc3DAdapter?.TYPE_3 -> 3
                            fc3DAdapter?.TYPE_4 -> 4
                            else -> 12
                        }
                    }
                }
            }
            "正码xgc", "7色波xgc", "头尾数xgc", "总肖xgc", "正肖xgc", "五行xgc", "合肖xgc", "组选三", "组选六" -> {
                xgcTmAdapter = AdapterXgcTm()
                rvContent?.adapter = xgcTmAdapter
                val layoutManager = GridLayoutManager(context, 4)
                rvContent?.layoutManager = layoutManager
                layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return when (xgcTmAdapter?.getItemViewType(position)) {
                            xgcTmAdapter?.TYPE_1, xgcTmAdapter?.TYPE_2, xgcTmAdapter?.TYPE_8 -> 1
                            xgcTmAdapter?.TYPE_7 -> 2
                            else -> 4
                        }
                    }
                }
            }
//            "一字组合", "二字组合", "三字组合", "一字定位", "三字和数", "跨度" -> {
//                fc3DAdapter = Fc3dAdapter()
//                rvContent?.adapter = fc3DAdapter
//                val layoutManager = GridLayoutManager(context, 12)
//                rvContent?.layoutManager = layoutManager
//                layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
//                    override fun getSpanSize(position: Int): Int {
//                        return when (fc3DAdapter?.getItemViewType(position)) {
//                            fc3DAdapter?.TYPE_1 -> 12
//                            fc3DAdapter?.TYPE_2 -> 6
//                            fc3DAdapter?.TYPE_3 -> 3
//                            fc3DAdapter?.TYPE_4 -> 4
//                            else -> 12
//                        }
//                    }
//                }
//            }
            "二字定位" -> {
                fc3DTopAdapter = Right3DTopAdapter(true)
                rvType?.adapter = fc3DTopAdapter
                rvType?.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                fc3DAdapter = Fc3dAdapter()
                rvContent?.adapter = fc3DAdapter
                val layoutManager = GridLayoutManager(context, 2)
                rvContent?.layoutManager = layoutManager
            }
            "三字定位" -> {
                if (fc3DAdapter == null) fc3DAdapter = Fc3dAdapter()
                rvContent?.adapter = fc3DAdapter
                val layoutManager = GridLayoutManager(context, 3)
                rvContent?.layoutManager = layoutManager
            }
            "二字和数" -> {
                fc3DTopAdapter = Right3DTopAdapter()
                rvType?.adapter = fc3DTopAdapter
                rvType?.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                fc3DAdapter = Fc3dAdapter()
                rvContent?.adapter = fc3DAdapter
                val layoutManager = GridLayoutManager(context, 4)
                rvContent?.layoutManager = layoutManager
                layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return when (fc3DAdapter?.getItemViewType(position)) {
                            fc3DAdapter?.TYPE_1 -> 4
                            fc3DAdapter?.TYPE_3 -> 1
                            else -> 4
                        }
                    }
                }
            }
        }
        modifyContentData(it, name)
    }

    private fun modifyContentData(data: LotteryPlayListResponse, type: String) {
        data.play_unit_data.let {
            if (it != null) {
                when (type) {
                    "两面", "两面xgc" -> {
                        val listData = arrayListOf<PlaySecData>()
                        for ((pos, item) in it.withIndex()) {
                            if (!item.play_sec_data.isNullOrEmpty()) {
                                for ((index, result) in item.play_sec_data!!.withIndex()) {
                                    if (index == 0) listData.add(
                                        PlaySecData(
                                            title = item.play_sec_cname ?: "null",
                                            type = "lm_full"
                                        )
                                    )  //一行占满
                                    if (result.play_sec_id != 0 && pos == 0) result.type =
                                        "lm_4"  //一行四个
                                    result.title = item.play_sec_cname ?: "null"
                                    listData.add(result)
                                }
                            }
                        }
                        repeat(10) {
                            listData.add(PlaySecData(title = "", type = "lm_full"))  //添加footer 一行占满
                        }
                        lmAdapter?.refresh(listData)
                    }
                    "整合", "第一球", "第二球", "第三球", "第四球", "第五球", "正码1-6xgc", "色波xgc", "平特一肖尾数xgc", "特肖xgc" -> {
                        val listData = arrayListOf<PlaySecData>()
                        for (item in it) {
                            if (!item.play_sec_data.isNullOrEmpty()) {
                                for ((index, result) in item.play_sec_data.withIndex()) {
                                    if (index == 0) listData.add(
                                        PlaySecData(
                                            title = item.play_sec_cname,
                                            type = "zh_full"
                                        )
                                    )  //一行占满
                                    if (type == "色波xgc") {
                                        if (result.play_sec_id != 0) {
                                            if (result.play_class_cname == "红" || result.play_class_cname == "绿" || result.play_class_cname == "蓝") {
                                                result.type = "zh_3"
                                                result.play_sec_cname = item.play_sec_cname
                                            } else {
                                                result.type = "zh_5"
                                                result.play_sec_cname = item.play_sec_cname
                                            }
                                        }
                                    } else {
                                        if (result.play_sec_id != 0) {
                                            result.type = "zh_5"
                                            result.play_sec_cname = item.play_sec_cname
                                        } //一行五个
                                    }
                                    listData.add(result)
                                }
                            }
                        }
//                        repeat(10) {
//                            listData.add(PlaySecData(title = "", type = "zh_full"))  //添加footer 一行占满
//                        }
                        zhAdapter?.refresh(listData)
                    }
                    "快捷" -> {
                        val listData = arrayListOf<PlaySecDataKj>()
                        for ((index, item) in it.withIndex()) {
                            if (!item.play_sec_data.isNullOrEmpty()) {
                                listData.add(
                                    PlaySecDataKj(
                                        play_sec_merge_name = item.play_sec_merge_name,
                                        play_sec_cname = item.play_sec_cname,
                                        type = "kj_5"
                                    )
                                )
                                if (index == it.size - 1) {
                                    if (!item.play_sec_data.isNullOrEmpty()) {
                                        for (child in item.play_sec_data) {
                                            listData.add(
                                                PlaySecDataKj(
                                                    play_sec_id = child.play_sec_id,
                                                    play_sec_name = child.play_sec_name,
                                                    play_class_id = child.play_class_id,
                                                    play_class_name = child.play_class_name,
                                                    play_class_cname = child.play_class_cname,
                                                    play_odds = child.play_odds
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        }
//                        repeat(10) {
//                            listData.add(PlaySecDataKj(type = "kj_full"))  //添加footer 一行占满
//                        }
                        kjAdapter?.refresh(listData)
                    }
                    "单号1-10" -> {
                        val listData = arrayListOf<PlaySecData>()
                        for (item in it) {
                            if (!item.play_sec_data.isNullOrEmpty()) {
                                for ((index, result) in item.play_sec_data.withIndex()) {
                                    if (index == 0) listData.add(
                                        PlaySecData(
                                            title = item.play_sec_cname,
                                            type = "full"
                                        )
                                    )  //一行占满
                                    if (result.play_sec_id != 0) {
                                        result.title = item.play_sec_cname
                                        result.type = "dh_10_5"
                                    } //一行五个
                                    listData.add(result)
                                }
                            }
                        }
//                        repeat(10) {
//                            listData.add(PlaySecData(title = "", type = "full"))  //添加footer 一行占满
//                        }
                        dan1D10Adapter?.refresh(listData)
                    }
                    "冠亚军组合" -> {
                        val listData = arrayListOf<PlaySecData>()
                        for (item in it) {
                            if (!item.play_sec_data.isNullOrEmpty()) {
                                for (result in item.play_sec_data) {
                                    result.playName = it[0].play_sec_cname
                                    listData.add(result)
                                }
                            }
                        }
                        gyhAdapter?.refresh(listData)
                    }
                    "单码", "连码", "斗牛" -> {
                        val listData = arrayListOf<PlayUnitData>()
                        for (item in it) {
                            if (!item.play_sec_data.isNullOrEmpty()) {
                                listData.add(item)
                            }
                        }
                        rightTopAdapter?.refresh(listData)
                    }
                    "特码xgc" -> {
                        val listData = arrayListOf<PlayUnitData>()
                        for ((num, item) in it.withIndex()) {
                            if (!item.play_sec_data.isNullOrEmpty()) {
                                for ((index, result) in item.play_sec_data.withIndex()) {
                                    if (result.play_class_name == "num_49") {
                                        result.type = "xgc_tm_full"
                                    } else {
                                        if (result.play_sec_id == 150 || result.play_sec_id == 151) {
                                            result.type = "xgc_tm_nor"
                                        } else if (result.play_sec_id == 147) result.type =
                                            "xgc_tm_ct"
                                    }
                                    result.play_sec_cname = it[num].play_sec_cname
                                }
//                                if (item.play_sec_data.size == 65) {
//                                    item.play_sec_data.add(
//                                        PlaySecData(
//                                            title = "",
//                                            type = "xgc_full"
//                                        )
//                                    )  //添加footer 一行占满
//                                }
                            }
                            listData.add(item)
                        }
                        rightTopXgcAdapter?.refresh(listData)
                    }
                    "正码xgc", "7色波xgc", "头尾数xgc", "总肖xgc", "正肖xgc", "五行xgc", "合肖xgc", "组选三", "组选六" -> {
                        val listData = arrayListOf<PlaySecData>()
                        for ((num, item) in it.withIndex()) {
                            if (!item.play_sec_data.isNullOrEmpty()) {
                                for ((index, result) in item.play_sec_data.withIndex()) {
                                    if (result.play_class_name == "num_49") {
                                        result.type = "xgc_tm_full"
                                    } else {
                                        if (result.play_sec_id == 152) {
                                            result.type = "xgc_tm_nor"
                                        } else if (result.play_sec_id == 149 || result.play_sec_id == 174 ||
                                            result.play_sec_id == 175 || result.play_sec_id == 176
                                            || result.play_sec_id == 177 || result.play_sec_id == 190 ||
                                            result.play_sec_id == 191
                                        ) {
                                            result.type = "xgc_tm_ct"
                                        } else if (result.play_sec_name == "lhc_hx") {
                                            result.type = "xgc_tm_hx"
                                            result.play_sec_options = item.play_sec_options
                                        } else if (result.play_sec_name == "3d_zxs" || result.play_sec_name == "3d_zxl") {
                                            result.type = "fc3d_zss"
                                            result.play_sec_options = item.play_sec_options
                                        }

                                    }
                                    result.play_sec_cname = it[num].play_sec_cname
                                    listData.add(result)
                                }
                            }
                        }
//                        listData.add(
//                            PlaySecData(
//                                title = "",
//                                type = "xgc_full"
//                            )
//                        )
                        xgcTmAdapter?.refresh(listData)
                    }
                    "正码特xgc" -> {
                        val listData = arrayListOf<PlayUnitData>()
                        for ((num, item) in it.withIndex()) {
                            if (!item.play_sec_data.isNullOrEmpty()) {
                                for ((index, result) in item.play_sec_data.withIndex()) {
                                    if (result.play_class_name == "num_49") {
                                        result.type = "xgc_tm_full"
                                    } else {
                                        if (result.play_sec_id == 153) {
                                            result.type = "xgc_tm_nor"
                                        } else if (result.play_sec_id == 159) result.type =
                                            "xgc_tm_ct"
                                    }
                                    result.play_sec_cname = it[num].play_sec_cname
                                }
//                                if (item.play_sec_data.size == 64) {
//                                    item.play_sec_data.add(
//                                        PlaySecData(
//                                            title = "",
//                                            type = "xgc_full"
//                                        )
//                                    )  //添加footer 一行占满
//                                }
                            }
                            listData.add(item)
                        }
                        rightTopXgcAdapter?.refresh(listData)
                    }
                    "连码xgc" -> {
                        val listData = arrayListOf<PlayUnitData>()
                        for ((num, item) in it.withIndex()) {
                            if (!item.play_sec_data.isNullOrEmpty()) {
                                if (item.play_sec_data.size == 49) {
                                    item.play_sec_data.add(
                                        0, PlaySecData(
                                            title = "",
                                            type = "xgc_tm_title",
                                            play_odds = it[num].play_sec_options?.get(0)?.play_odds
                                                ?: "null"
                                        )
                                    )  //添加footer 一行占满
                                }
                                for ((index, result) in item.play_sec_data.withIndex()) {
                                    if (index != 0 && index < 50) {
                                        result.type = "xgc_tm_nor"
                                        result.play_sec_cname = it[num].play_sec_cname
                                        result.play_odds =
                                            it[num].play_sec_options?.get(0)?.play_odds ?: "null"
                                    }
                                }
//                                if (item.play_sec_data.size == 50) {
//                                    item.play_sec_data.add(
//                                        PlaySecData(
//                                            title = "",
//                                            type = "xgc_full"
//                                        )
//                                    )  //添加footer 一行占满
//                                }
                            }
                            listData.add(item)
                        }
                        rightTopXgcAdapter?.refresh(listData)
                    }
                    "连肖连尾xgc" -> {
                        val listData = arrayListOf<PlayUnitData>()
                        for ((num, item) in it.withIndex()) {
                            if (!item.play_sec_data.isNullOrEmpty()) {
                                for ((index, result) in item.play_sec_data.withIndex()) {
                                    if (index < 12) {
                                        result.type = "xgc_tm_lx"
                                        result.play_sec_cname = it[num].play_sec_cname
                                    }
                                }
//                                if (item.play_sec_data.size == 12) {
//                                    item.play_sec_data.add(
//                                        PlaySecData(
//                                            title = "",
//                                            type = "xgc_full"
//                                        )
//                                    )  //添加footer 一行占满
//                                }
                            }
                            listData.add(item)
                        }
                        rightTopXgcAdapter?.refresh(listData)
                    }
                    "自选不中xgc" -> {
                        val listData = arrayListOf<PlaySecData>()
                        for ((num, item) in it.withIndex()) {
                            if (!item.play_sec_data.isNullOrEmpty()) {
                                for ((index, result) in item.play_sec_data.withIndex()) {
                                    result.type = "xgc_tm_nor"
                                    result.play_sec_options = item.play_sec_options
                                    result.play_sec_cname = it[num].play_sec_cname
                                    listData.add(result)
                                }
                            }
                        }
//                        listData.add(
//                            PlaySecData(
//                                title = "",
//                                type = "xgc_full"
//                            )
//                        )
                        xgcTmAdapter?.refresh(listData)
                    }
                    "主势盘", "一字组合", "二字组合", "三字组合", "一字定位", "三字和数", "跨度" -> {
                        val listData = arrayListOf<PlayUnitData>()
                        for ((num, item) in it.withIndex()) {
                            if (!item.play_sec_data.isNullOrEmpty()) {
                                for ((index, result) in item.play_sec_data.withIndex()) {
                                    when (item.play_sec_merge_name) {
                                        "3d_yzzh" -> {
                                            result.type = "3d_4"
                                        }
                                        "3d_b_lm", "3d_s_lm", "3d_g_lm" -> {
                                            result.type = "3d_3"
                                        }
                                        "3d_bsh_lm", "3d_bgh_lm", "3d_sgh_lm" -> {
                                            result.type = "3d_2"
                                        }
                                        "3d_bshw_lm", "3d_bghw_lm", "3d_bsgh_lm", "3d_ezzh",
                                        "3d_szzh", "3d_yzdw_b", "3d_yzdw_s", "3d_yzdw_g",
                                        "3d_bsghw_lm", "3d_sghw_lm", "3d_szhs", "3d_szhsw", "3d_kd" -> {
                                            result.type = "3d_4"
                                        }
                                    }
                                    result.title = item.play_sec_cname
                                }
//                                item.play_sec_data.add(PlaySecData(title = "", type = ""))
                                listData.add(item)
                            }
                        }
                        fc3DTopAdapter?.refresh(listData)
                    }
//                    -> {
//                        val listData = arrayListOf<PlaySecData>()
//                        for ((num, item) in it.withIndex()) {
//                            if (!item.play_sec_data.isNullOrEmpty()) {
//                                for ((index, result) in item.play_sec_data.withIndex()) {
//                                    if (index == 0) listData.add(
//                                        PlaySecData(
//                                            title = item.play_sec_cname,
//                                            type = "3d_full"
//                                        )
//                                    )
//
//                                    result.title = item.play_sec_cname
//                                    listData.add(result)
//                                }
//                            }
//                        }
//                        listData.add(
//                            PlaySecData(
//                                title = "",
//                                type = ""
//                            )
//                        )
//                        fc3DAdapter?.refresh(listData)
//
//                    }
                    "二字定位" -> {
                        val listData = arrayListOf<PlayUnitData>()
                        for ((num, item) in it.withIndex()) {
                            if (!item.play_sec_data.isNullOrEmpty()) {
                                val childData = arrayListOf<PlaySecData>()
                                for ((index, result) in item.play_sec_data.withIndex()) {
                                    childData.add(
                                        PlaySecData(
                                            type = "3d_ezdw",
                                            title = item.play_sec_cname,
                                            play_odds = item.play_sec_options?.get(0)?.play_odds
                                                ?: "0",
                                            play_sec_name = result.play_sec_name,
                                            play_class_name = result.play_class_name,
                                            play_class_cname = result.play_class_cname,
                                            rzPosition = 0
                                        )
                                    )
                                    childData.add(
                                        PlaySecData(
                                            type = "3d_ezdw",
                                            title = item.play_sec_cname,
                                            play_odds = item.play_sec_options?.get(0)?.play_odds
                                                ?: "0",
                                            play_sec_name = result.play_sec_name,
                                            play_class_name = result.play_class_name,
                                            play_class_cname = result.play_class_cname,
                                            rzPosition = 1
                                        )
                                    )
//                                    if (index == item.play_sec_data.size - 1) {
//                                        childData.add(
//                                            PlaySecData(type = "")
//                                        )
//                                    }
                                }

                                listData.add(
                                    PlayUnitData(
                                        play_sec_cname = item.play_sec_cname,
                                        play_sec_combo = item.play_sec_combo,
                                        play_sec_merge_name = item.play_sec_merge_name,
                                        play_sec_name = item.play_sec_name,
                                        play_sec_data = childData,
                                        play_sec_id = item.play_sec_id,
                                        play_sec_info = item.play_sec_info,
                                        play_sec_options = item.play_sec_options
                                    )
                                )
                            }
                        }
                        fc3DTopAdapter?.refresh(listData)
                    }
                    "三字定位" -> {
                        val listData = arrayListOf<PlaySecData>()
                        for ((num, item) in it.withIndex()) {
                            if (!item.play_sec_data.isNullOrEmpty()) {
                                for ((index, result) in item.play_sec_data.withIndex()) {
                                    listData.add(
                                        PlaySecData(
                                            type = "3d_szdw",
                                            title = item.play_sec_cname,
                                            play_odds = item.play_sec_options?.get(0)?.play_odds
                                                ?: "0",
                                            play_sec_name = result.play_sec_name,
                                            play_class_name = result.play_class_name,
                                            play_class_cname = result.play_class_cname,
                                            rzPosition = 0
                                        )
                                    )
                                    listData.add(
                                        PlaySecData(
                                            type = "3d_szdw",
                                            title = item.play_sec_cname,
                                            play_odds = item.play_sec_options?.get(0)?.play_odds
                                                ?: "0",
                                            play_sec_name = result.play_sec_name,
                                            play_class_name = result.play_class_name,
                                            play_class_cname = result.play_class_cname,
                                            rzPosition = 1
                                        )
                                    )
                                    listData.add(
                                        PlaySecData(
                                            type = "3d_szdw",
                                            title = item.play_sec_cname,
                                            play_odds = item.play_sec_options?.get(0)?.play_odds
                                                ?: "0",
                                            play_sec_name = result.play_sec_name,
                                            play_class_name = result.play_class_name,
                                            play_class_cname = result.play_class_cname,
                                            rzPosition = 2
                                        )
                                    )
//                                    if (index == item.play_sec_data.size - 1) {
//                                        listData.add(
//                                            PlaySecData(type = "")
//                                        )
//                                    }
                                }
                            }
                        }
                        fc3DAdapter?.refresh(listData)
//                        tvTitle?.text = "三字定位"
                        try {
                            tvHx?.text = it[0].play_sec_options?.get(0)?.play_odds ?: "null"
                            tv3d3z1?.text = it[0].play_sec_info?.get(0) ?: "null"
                            tv3d3z2?.text = it[0].play_sec_info?.get(1) ?: "null"
                            tv3d3z3?.text = it[0].play_sec_info?.get(2) ?: "null"
                        } catch (e: Exception) {
                            ToastUtils.showToast("数据错误 !!!")
                        }
                    }
                    "二字和数" -> {
                        val listData = arrayListOf<PlayUnitData>()
                        for ((num, item) in it.withIndex()) {
                            if (!item.play_sec_data.isNullOrEmpty()) {
                                val childData = arrayListOf<PlaySecData>()
                                for ((index, result) in item.play_sec_data.withIndex()) {
                                    if (!result.play_sec_data.isNullOrEmpty()) {
                                        for ((pos, child) in result.play_sec_data!!.withIndex()) {
                                            if (pos == 0) {
                                                childData.add(
                                                    PlaySecData(
                                                        type = "3d_full",
                                                        title = result.play_sec_cname ?: "null"
                                                    )
                                                )
                                            }
                                            childData.add(
                                                PlaySecData(
                                                    type = "3d_4",
                                                    title = result.play_sec_cname ?: "null",
                                                    play_odds = child.play_odds ?: "0",
                                                    play_sec_name = child.play_sec_name,
                                                    play_class_name = child.play_class_name,
                                                    play_class_cname = child.play_class_cname
                                                )
                                            )
                                        }
                                    }
                                }

                                listData.add(
                                    PlayUnitData(
                                        play_sec_cname = item.play_sec_cname,
                                        play_sec_combo = item.play_sec_combo,
                                        play_sec_merge_name = item.play_sec_merge_name,
                                        play_sec_name = item.play_sec_name,
                                        play_sec_data = childData,
                                        play_sec_id = item.play_sec_id,
                                        play_sec_info = null,
                                        play_sec_options = null
                                    )
                                )

                            }
                        }
                        fc3DTopAdapter?.refresh(listData)
                    }
                }
            }
        }
    }


    /**
     * 两面适配
     */
    inner class AdapterLm : BaseRecyclerAdapter<PlaySecData>() {
        val ITEM_TYPE_HEAD = 1
        val ITEM_TYPE_CONTENT_COUNT_4 = 2
        val ITEM_TYPE_CONTENT_COUNT_3 = 3

        override fun getItemViewType(position: Int): Int {
            return when (data[position].type) {
                "lm_full" -> ITEM_TYPE_HEAD
                "lm_4" -> ITEM_TYPE_CONTENT_COUNT_4
                else -> ITEM_TYPE_CONTENT_COUNT_3
            }
        }

        override fun getItemLayoutId(viewType: Int): Int {
            return when (viewType) {
                ITEM_TYPE_HEAD -> R.layout.adapter_game_bet_title
                else -> R.layout.adapter_game_bet_content
            }
        }

        override fun bindData(holder: RecyclerViewHolder, position: Int, data: PlaySecData?) {
            if (getItemViewType(position) == ITEM_TYPE_HEAD) {
                val view = holder.findViewById<TextView>(R.id.tvGameType)
                if (lotteryId != "8") {
                    view.text = data?.title
                } else {
                    if (position == 0) {
                        val param = holder.itemView.layoutParams
                        param.height = 0
                        param.width = 0
                        ViewUtils.setGone(holder.itemView)
                    }
                }
            } else {
                val container = holder.findViewById<LinearLayout>(R.id.gameBetLinearLayout)
                val tv1 = holder.findViewById<TextView>(R.id.tvCname)
                val tv2 = holder.findViewById<TextView>(R.id.tvOdds)
                tv1.text = data?.play_class_cname
                tv2.text = data?.play_odds.toString()
                changeBg(data?.isSelected, container, tv1, tv2)
                holder.itemView.setOnClickListener {
                    data?.isSelected = data?.isSelected != true
                    addOrDeleteBetData(
                        data?.isSelected == true,
                        play_sec_name = data?.play_sec_name ?: "-1",
                        play_class_name = data?.play_class_name ?: "-1",
                        play_sec_cname = data?.title ?: "-1",
                        play_class_cname = data?.play_class_cname ?: "-1",
                        play_odds = data?.play_odds ?: "-1"
                    )
                    notifyItemChanged(position)
                }
            }
        }

        fun resetData() {
            for (item in this.data) {
                if (item.isSelected) item.isSelected = false
            }
            notifyDataSetChanged()
        }
    }

    /**
     * 快捷适配
     */
    var kjList = arrayListOf<PlaySecData>()  //快捷集合需要特殊处理  快捷的头部type选择
    var kjContentList = arrayListOf<PlaySecData>()  //快捷集合需要特殊处理  快捷的内容选择

    inner class AdapterKj : BaseRecyclerAdapter<PlaySecDataKj>() {
        val ITEM_TYPE_CONTENT_COUNT_5 = 1
        val ITEM_TYPE_CONTENT_COUNT_2 = 2
        val ITEM_TYPE_CONTENT = 3
        override fun getItemViewType(position: Int): Int {
            return when (data[position].type) {
                "kj_5" -> ITEM_TYPE_CONTENT_COUNT_5
                "kj_full" -> ITEM_TYPE_CONTENT
                else -> ITEM_TYPE_CONTENT_COUNT_2
            }
        }

        override fun getItemLayoutId(viewType: Int): Int {
            return when (viewType) {
                ITEM_TYPE_CONTENT -> R.layout.adapter_game_bet_title
                else -> R.layout.adapter_game_bet_content
            }
        }

        override fun bindData(holder: RecyclerViewHolder, position: Int, data: PlaySecDataKj?) {
            if (getItemViewType(position) != ITEM_TYPE_CONTENT) {
                val tv1 = holder.findViewById<TextView>(R.id.tvCname)
                val tv2 = holder.findViewById<TextView>(R.id.tvOdds)
                val container = holder.findViewById<LinearLayout>(R.id.gameBetLinearLayout)
                if (getItemViewType(position) == ITEM_TYPE_CONTENT_COUNT_5) {
                    tv1.text = data?.play_sec_cname
                    tv1.textSize = 9f
                    tv1.setPadding(0, 5, 0, 5)
                    val layoutParams = GridLayoutManager.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    layoutParams.marginStart = 20
                    layoutParams.marginEnd = 20
                    if (position < 5) layoutParams.topMargin = 10 else {
                        layoutParams.topMargin = 20
                    }
                    holder.itemView.layoutParams = layoutParams
                    ViewUtils.setGone(tv2)
                    changeBg(data?.isSelected, container, tv1, tv2, true)
                } else if (getItemViewType(position) == ITEM_TYPE_CONTENT_COUNT_2) {
                    ViewUtils.setVisible(tv2)
                    tv1.text = data?.play_class_cname
                    tv2.text = data?.play_odds.toString()
                    changeBg(data?.isSelected, container, tv1, tv2)
                }

                holder.itemView.setOnClickListener {
                    if (getItemViewType(position) == ITEM_TYPE_CONTENT_COUNT_5) {
                        if (data?.isSelected == true && kjList.size == 1) {
                            ToastUtils.showToast("至少选择一种玩法")
                            return@setOnClickListener
                        }
                        data?.isSelected = data?.isSelected != true
                        notifyItemChanged(position)
                        if (data?.isSelected == true)
                            kjList.add(
                                PlaySecData(
                                    play_sec_name = data.play_sec_merge_name ?: "-1",
                                    play_sec_cname = data.play_sec_cname ?: "-1"
                                )
                            )
                        else
                            kjList.remove(
                                PlaySecData(
                                    play_sec_name = data?.play_sec_merge_name ?: "-1",
                                    play_sec_cname = data?.play_sec_cname ?: "-1"
                                )
                            )
                        if (data?.isSelected == true) {
                            for (item in kjContentList) {
                                betList.add(
                                    PlaySecData(
                                        play_sec_name = data.play_sec_merge_name ?: "-1",
                                        play_class_name = item.play_class_name ?: "-1",
                                        play_sec_cname = data.play_sec_cname ?: "-1",
                                        play_class_cname = item.play_class_cname ?: "-1",
                                        play_odds = item.play_odds ?: "-1",
                                        title = data.play_sec_cname ?: "-1"

                                    )
                                )
                            }
                        } else {
                            val intList = arrayListOf<PlaySecData>()
                            for (res in betList) {
                                if (res.play_sec_name == data?.play_sec_merge_name) intList.add(res)
                            }
                            for (count in intList) {
                                betList.remove(count)
                            }
                        }


                    } else if (getItemViewType(position) == ITEM_TYPE_CONTENT_COUNT_2) {
                        if (kjList.isEmpty()) {
                            ToastUtils.showToast("请选择玩法")
                            return@setOnClickListener
                        }
                        data?.isSelected = data?.isSelected != true
                        notifyItemChanged(position)
                        if (data?.isSelected == true)
                            kjContentList.add(
                                PlaySecData(
                                    play_class_name = data.play_class_name,
                                    play_class_cname = data.play_class_cname,
                                    play_odds = data.play_odds
                                )
                            )
                        else
                            kjContentList.remove(
                                PlaySecData(
                                    play_class_name = data?.play_class_name,
                                    play_class_cname = data?.play_class_cname,
                                    play_odds = data?.play_odds
                                )
                            )

                        if (data?.isSelected == true) {
                            for (item in kjList) {
                                betList.add(
                                    PlaySecData(
                                        play_sec_name = item.play_sec_name ?: "-1",
                                        play_class_name = data.play_class_name ?: "-1",
                                        play_sec_cname = item.play_sec_cname ?: "-1",
                                        play_class_cname = data.play_class_cname ?: "-1",
                                        play_odds = data.play_odds ?: "-1",
                                        title = item.play_sec_cname ?: "-1"

                                    )
                                )
                            }
                        } else {
                            val intList = arrayListOf<PlaySecData>()
                            for (res in betList) {
                                if (res.play_class_name == data?.play_class_name) {
                                    intList.add(res)
                                }
                            }
                            for (count in intList) {
                                betList.remove(count)
                            }
                        }
                    }
//                    if (betList.isNotEmpty()) ViewUtils.setVisible(bottomGameBetLayout) else ViewUtils.setGone(bottomGameBetLayout)
//                    setTotal()
                    RxBus.get().post(LotteryLiveBet(rightTop, betList))
                }
            }
        }

        fun resetData() {
            for (item in this.data) {
                if (item.isSelected) item.isSelected = false
            }
            notifyDataSetChanged()
        }
    }

    /**
     *  单号1-10
     */
    inner class AdapterDan1D10 : BaseRecyclerAdapter<PlaySecData>() {
        val ITEM_TYPE_CONTENT_COUNT_5 = 1
        val ITEM_TYPE_CONTENT_COUNT_1 = 2

        override fun getItemViewType(position: Int): Int {
            return when (data[position].type) {
                "dh_10_5" -> ITEM_TYPE_CONTENT_COUNT_5
                else -> ITEM_TYPE_CONTENT_COUNT_1
            }
        }

        override fun getItemLayoutId(viewType: Int): Int {
            return when (viewType) {
                ITEM_TYPE_CONTENT_COUNT_1 -> R.layout.adapter_game_bet_title
                else -> R.layout.adapter_game_bet_content
            }
        }

        override fun bindData(holder: RecyclerViewHolder, position: Int, data: PlaySecData?) {
            if (getItemViewType(position) == ITEM_TYPE_CONTENT_COUNT_1) {
                val view = holder.findViewById<TextView>(R.id.tvGameType)
                view.text = data?.title
            } else {
                val container = holder.findViewById<LinearLayout>(R.id.gameBetLinearLayout)
                val tv1 = holder.findViewById<TextView>(R.id.tvCname)
                val tv2 = holder.findViewById<TextView>(R.id.tvOdds)
                tv1.text = data?.play_class_cname
                tv2.text = data?.play_odds.toString()
                changeBg(data?.isSelected, container, tv1, tv2)
                holder.itemView.setOnClickListener {
                    data?.isSelected = data?.isSelected != true
                    addOrDeleteBetData(
                        data?.isSelected == true,
                        data?.play_sec_name ?: "-1",
                        data?.play_class_name ?: "-1",
                        data?.title ?: "-1",
                        data?.play_class_cname ?: "-1",
                        data?.play_odds ?: "-1"

                    )
                    notifyItemChanged(position)
                }
            }
        }

        fun resetData() {
            for (item in this.data) {
                if (item.isSelected) item.isSelected = false
            }
            notifyDataSetChanged()
        }
    }

    /**
     * 冠亚和
     */
    inner class AdapterGYH : BaseRecyclerAdapter<PlaySecData>() {
        override fun getItemLayoutId(viewType: Int) = R.layout.adapter_game_bet_content

        override fun bindData(holder: RecyclerViewHolder, position: Int, data: PlaySecData?) {
            val container = holder.findViewById<LinearLayout>(R.id.gameBetLinearLayout)
            val tv1 = holder.findViewById<TextView>(R.id.tvCname)
            val tv2 = holder.findViewById<TextView>(R.id.tvOdds)
            tv1.text = data?.play_class_cname
            tv2.text = data?.play_odds.toString()
            changeBg(data?.isSelected, container, tv1, tv2)
            holder.itemView.setOnClickListener {
                data?.isSelected = data?.isSelected != true
                addOrDeleteBetData(
                    data?.isSelected == true,
                    data?.play_sec_name ?: "-1",
                    data?.play_class_name ?: "-1",
                    data?.playName ?: "-1",
                    data?.play_class_cname ?: "-1",
                    data?.play_odds ?: "-1"
                )
                notifyItemChanged(position)
            }
        }

        fun resetData() {
            for (item in this.data) {
                if (item.isSelected) item.isSelected = false
            }
            notifyDataSetChanged()
        }
    }

    /**
     * 单码，连码，牛牛
     */
    var currentSingle = 0  //记录一中一选了几个
    var currentDouble = 0 //记录二中二选了几个
    var currentTriple = 0 //记录三中三选了几个

    inner class AdapterDM : BaseRecyclerAdapter<PlaySecData>() {
        var currentRightTop = "-1"

        override fun getItemLayoutId(viewType: Int) = R.layout.adapter_game_bet_content

        override fun bindData(holder: RecyclerViewHolder, position: Int, data: PlaySecData?) {
            val container = holder.findViewById<LinearLayout>(R.id.gameBetLinearLayout)
            val tv1 = holder.findViewById<TextView>(R.id.tvCname)
            val tv2 = holder.findViewById<TextView>(R.id.tvOdds)
            tv1.text = data?.play_class_cname
            tv2.text = data?.play_odds.toString()
            changeBg(data?.isSelected, container, tv1, tv2)
            holder.itemView.setOnClickListener {
                val title = when {
                    (rightTop.contains("一中一")) -> "一中一"
                    (rightTop.contains("一中二")) -> "一中二"
                    (rightTop.contains("一中三")) -> "一中三"
                    (rightTop.contains("一中四")) -> "一中四"
                    (rightTop.contains("一中五")) -> "一中五"
                    (rightTop.contains("二中")) -> "二中二"
                    (rightTop.contains("三中")) -> "三中三"
                    else -> ""
                }
                val bean = PlaySecData(
                    play_sec_name = data?.play_sec_name,
                    play_class_name = data?.play_class_name ?: "-1",
                    play_sec_cname = title,
                    play_class_cname = data?.play_class_cname ?: "-1",
                    play_odds = data?.play_odds ?: "-1"
                )
                if (rightTop.contains("一中")) {
                    if (currentSingle == 1) {
                        if (!betList.contains(bean)) {
                            when {
                                rightTop.contains("一中一") -> {
                                    ToastUtils.showToast("该玩法最多选1个号码")
                                }
                                rightTop.contains("一中二") -> {
                                    ToastUtils.showToast("该玩法最多选1个号码")
                                }
                                rightTop.contains("一中三") -> {
                                    ToastUtils.showToast("该玩法最多选1个号码")
                                }
                                rightTop.contains("一中四") -> {
                                    ToastUtils.showToast("该玩法最多选1个号码")
                                }
                                rightTop.contains("一中五") -> {
                                    ToastUtils.showToast("该玩法最多选1个号码")
                                }
                            }
                            return@setOnClickListener
                        } else currentSingle--
                    } else currentSingle++
                } else if (rightTop.contains("二中二")) {
                    if (currentDouble == 2) {
                        if (!betList.contains(bean)) {
                            ToastUtils.showToast("该玩法最多选2个号码")
                            return@setOnClickListener
                        } else currentDouble--
                    } else {
                        if (!betList.contains(bean)) currentDouble++ else currentDouble--
                    }
                } else if (rightTop.contains("三中三")) {
                    if (currentTriple == 3) {
                        if (!betList.contains(bean)) {
                            ToastUtils.showToast("该玩法最多选3个号码")
                            return@setOnClickListener
                        } else currentTriple--
                    } else {
                        if (!betList.contains(bean)) currentTriple++ else currentTriple--
                    }
                }
                data?.isSelected = data?.isSelected != true

                addOrDeleteBetData(
                    data?.isSelected == true,
                    play_sec_name = data?.play_sec_name.toString(),
                    play_class_name = data?.play_class_name ?: "-1",
                    play_sec_cname = title,
                    play_class_cname = data?.play_class_cname ?: "-1",
                    play_odds = data?.play_odds ?: "-1"

                )
                notifyItemChanged(position)
            }
        }

        fun resetData() {
            for (item in this.data) {
                if (item.isSelected) item.isSelected = false
            }
            notifyDataSetChanged()
        }
    }

    /**
     * 整合、1-5球
     */
    inner class AdapterZH : BaseRecyclerAdapter<PlaySecData>() {
        val ITEM_TYPE_HEAD = 1
        val ITEM_TYPE_CONTENT_COUNT_5 = 2
        val ITEM_TYPE_CONTENT_COUNT_3 = 3

        override fun getItemViewType(position: Int): Int {
            return when (data[position].type) {
                "zh_full" -> ITEM_TYPE_HEAD
                "zh_5" -> ITEM_TYPE_CONTENT_COUNT_5
                "zh_3" -> ITEM_TYPE_CONTENT_COUNT_3
                else -> ITEM_TYPE_CONTENT_COUNT_5
            }
        }

        override fun getItemLayoutId(viewType: Int): Int {
            return when (viewType) {
                ITEM_TYPE_HEAD -> R.layout.adapter_game_bet_title
                else -> R.layout.adapter_game_bet_content
            }
        }

        override fun bindData(holder: RecyclerViewHolder, position: Int, data: PlaySecData?) {
            if (getItemViewType(position) == ITEM_TYPE_HEAD) {
                val view = holder.findViewById<TextView>(R.id.tvGameType)
                view.text = data?.title
            } else {
                val container = holder.findViewById<LinearLayout>(R.id.gameBetLinearLayout)
                val tv1 = holder.findViewById<TextView>(R.id.tvCname)
                val tv2 = holder.findViewById<TextView>(R.id.tvOdds)
                tv1.text = data?.play_class_cname
                tv2.text = data?.play_odds.toString()
                changeBg(data?.isSelected, container, tv1, tv2)
                holder.itemView.setOnClickListener {
                    data?.isSelected = data?.isSelected != true
                    addOrDeleteBetData(
                        data?.isSelected == true,
                        data?.play_sec_name ?: "-1",
                        data?.play_class_name ?: "-1",
                        data?.play_sec_cname ?: "-1",
                        data?.play_class_cname ?: "-1",
                        data?.play_odds ?: "-1"

                    )
                    notifyItemChanged(position)
                }
            }
        }

        fun resetData() {
            for (item in this.data) {
                if (item.isSelected) item.isSelected = false
            }
            notifyDataSetChanged()
        }
    }

    /**
     * 香港彩 特码 连码....
     */
    inner class AdapterXgcTm : BaseRecyclerAdapter<PlaySecData>() {
        val TYPE_1 = 1
        val TYPE_2 = 2
        val TYPE_3 = 3
        val TYPE_4 = 4
        val TYPE_5 = 5
        val TYPE_6 = 6
        val TYPE_7 = 7
        val TYPE_8 = 8
        override fun getItemViewType(position: Int): Int {
            return when (data[position].type) {
                "xgc_tm_nor" -> TYPE_1
                "xgc_tm_ct" -> TYPE_2
                "xgc_tm_full" -> TYPE_3
                "xgc_full" -> TYPE_4
                "xgc_tm_title" -> TYPE_5
                "xgc_tm_lx" -> TYPE_6
                "xgc_tm_hx" -> TYPE_7
                "fc3d_zss" -> TYPE_8
                else -> TYPE_4
            }
        }

        override fun getItemLayoutId(viewType: Int): Int {
            return when (viewType) {
                TYPE_1 -> R.layout.adapter_game_xgc_tm
                TYPE_2 -> R.layout.adapter_game_bet_content
                TYPE_3 -> R.layout.adapter_game_xgc_tm_full
                TYPE_4 -> R.layout.adapter_game_xgc_holder
                TYPE_5 -> R.layout.adapter_game_bet_title
                TYPE_6 -> R.layout.adapter_game_xgc_tm
                TYPE_7, TYPE_8 -> R.layout.adapter_game_xgc_tm
                else -> R.layout.adapter_game_xgc_holder
            }
        }

        @SuppressLint("SetTextI18n")
        override fun bindData(holder: RecyclerViewHolder, position: Int, data: PlaySecData?) {
            val container = holder.findViewById<LinearLayout>(R.id.gameBetLinearLayout)
            when (getItemViewType(position)) {
                TYPE_1 -> {
                    val tv1 = holder.findViewById<TextView>(R.id.tvXgcCname)
                    val tv2 = holder.findViewById<TextView>(R.id.tvXgcOdds)
                    tv1.text = data?.play_class_cname
                    tv2.text = data?.play_odds

                    when (data?.play_sec_name) {
                        "lhc_sze", "lhc_eqz", "lhc_sqz", "lhc_ezt", "lhc_tc", "lhc_siqz", "lhc_zxbz" -> {
                            ViewUtils.setGone(tv2)
                        }
                        else -> ViewUtils.setVisible(tv2)
                    }
                    when (data?.play_class_cname) {
                        "1", "2", "7", "8", "12", "13", "18", "19", "23", "24", "29", "30", "34", "35", "40", "45", "46" -> {
                            tv1.background = ViewUtils.getDrawable(R.drawable.code_9)
                        }
                        "3", "4", "9", "10", "14", "15", "20", "25", "26", "31", "36", "37", "41", "42", "47", "48" -> {
                            tv1.background = ViewUtils.getDrawable(R.drawable.xcode_blue)
                        }
                        else -> {
                            tv1.background = ViewUtils.getDrawable(R.drawable.code_10)
                        }
                    }
                    changeBgXgc(data?.isSelected, container)
                    container.setOnClickListener {
                        when (data?.play_sec_name) {
                            //连码处理(香港六合彩)
                            "lhc_sze", "lhc_eqz", "lhc_sqz", "lhc_ezt", "lhc_tc", "lhc_siqz" -> {
                                xgcLm(this, position, data)
                            }
                            "lhc_zxbz" -> {
                                cgcHx(this, position, data)
                            }
                            else -> {
                                data?.isSelected = data?.isSelected != true
                                addOrDeleteBetData(
                                    data?.isSelected == true,
                                    data?.play_sec_name ?: "-1",
                                    data?.play_class_name ?: "-1",
                                    data?.play_sec_cname ?: "-1",
                                    data?.play_class_cname ?: "-1",
                                    data?.play_odds ?: "-1"

                                )
                                notifyItemChanged(position)
                            }
                        }

                    }
                }
                TYPE_2 -> {
                    val tv1 = holder.findViewById<TextView>(R.id.tvCname)
                    val tv2 = holder.findViewById<TextView>(R.id.tvOdds)
                    tv1.text = data?.play_class_cname
                    tv2.text = data?.play_odds
                    changeBg(data?.isSelected, container, tv1, tv2)
                    holder.itemView.setOnClickListener {
                        data?.isSelected = data?.isSelected != true
                        addOrDeleteBetData(
                            data?.isSelected == true,
                            data?.play_sec_name ?: "-1",
                            data?.play_class_name ?: "-1",
                            data?.play_sec_cname ?: "-1",
                            data?.play_class_cname ?: "-1",
                            data?.play_odds ?: "-1"

                        )
                        notifyItemChanged(position)
                    }
                }
                TYPE_3 -> {
                    val tv1 = holder.findViewById<TextView>(R.id.tvXgcCname)
                    val tv2 = holder.findViewById<TextView>(R.id.tvXgcOdds)
                    tv1.text = data?.play_class_cname
                    tv2.text = data?.play_odds
                    tv1.background = ViewUtils.getDrawable(R.drawable.code_10)
                    changeBgXgc(data?.isSelected, container)
                    container.setOnClickListener {
                        data?.isSelected = data?.isSelected != true
                        addOrDeleteBetData(
                            data?.isSelected == true,
                            data?.play_sec_name ?: "-1",
                            data?.play_class_name ?: "-1",
                            data?.play_sec_cname ?: "-1",
                            data?.play_class_cname ?: "-1",
                            data?.play_odds ?: "-1"

                        )
                        notifyItemChanged(position)
                    }
                }
                TYPE_5 -> {
                    //连码
                    val tv = holder.findViewById<TextView>(R.id.tvGameType)
                    tv.setTextColor(ViewUtils.getColor(R.color.color_FF513E))
                    tv.text = "赔率：" + data?.play_odds
                }
                TYPE_6 -> {
                    val tv1 = holder.findViewById<TextView>(R.id.tvXgcCname)
                    val tv2 = holder.findViewById<TextView>(R.id.tvXgcOdds)
                    tv1.text = data?.play_class_cname
                    tv2.text = data?.play_odds
                    tv1.background = ViewUtils.getDrawable(R.drawable.xcode_blue)
                    changeBgXgc(data?.isSelected, container)
                    container.setOnClickListener {
                        when (data?.play_sec_name) {
                            //连肖连尾(香港六合彩)
                            "lhc_lx2", "lhc_lx3", "lhc_lx4", "lhc_lx5", "lhc_lw2", "lhc_lw3"
                                , "lhc_lw4", "lhc_lw5" -> {
                                xgcLm(this, position, data)
                            }
                            else -> {
                                data?.isSelected = data?.isSelected != true
                                addOrDeleteBetData(
                                    data?.isSelected == true,
                                    data?.play_sec_name ?: "-1",
                                    data?.play_class_name ?: "-1",
                                    data?.play_sec_cname ?: "-1",
                                    data?.play_class_cname ?: "-1",
                                    data?.play_odds ?: "-1"

                                )
                                notifyItemChanged(position)
                            }
                        }
                    }
                }
                TYPE_7 -> {
                    val tv1 = holder.findViewById<TextView>(R.id.tvXgcCname)
                    val tv2 = holder.findViewById<TextView>(R.id.tvXgcOdds)
                    tv1.text = data?.play_class_cname
                    tv2.text = data?.play_odds
                    ViewUtils.setGone(tv2)
                    tv1.background = ViewUtils.getDrawable(R.drawable.xcode_blue)
                    changeBgXgc(data?.isSelected, container)
                    container.setOnClickListener {
                        //合肖处理
                        if (data?.play_sec_name == "lhc_hx") {
                            cgcHx(this, position, data)
                        } else {
                            data?.isSelected = data?.isSelected != true
                            addOrDeleteBetData(
                                data?.isSelected == true,
                                data?.play_sec_name ?: "-1",
                                data?.play_class_name ?: "-1",
                                data?.play_sec_cname ?: "-1",
                                data?.play_class_cname ?: "-1",
                                data?.play_odds ?: "-1"
                            )
                            notifyItemChanged(position)
                        }

                    }
                }
                TYPE_8 -> {
                    val tv1 = holder.findViewById<TextView>(R.id.tvXgcCname)
                    val tv2 = holder.findViewById<TextView>(R.id.tvXgcOdds)
                    tv1.text = data?.play_class_cname
                    tv2.text = data?.play_odds
                    ViewUtils.setGone(tv2)
                    GamePlay.changeBg(data?.isSelected, container, tv1)
                    container.setOnClickListener {
                        cgcHx(this, position, data)
                    }
                }
                else -> {

                }
            }
        }

        fun resetData() {
            for (item in this.data) {
                if (item.isSelected) item.isSelected = false
            }
            notifyDataSetChanged()
        }
    }

    //右边上部分类
    inner class RightTopAdapter : BaseRecyclerAdapter<PlayUnitData>() {
        private var currentPos = 0
        override fun getItemLayoutId(viewType: Int) = R.layout.adapter_game_bet_right_top
        override fun bindData(holder: RecyclerViewHolder, position: Int, data: PlayUnitData?) {
            val container = holder.findViewById<LinearLayout>(R.id.gameBetLinearLayout)
            val tv1 = holder.findViewById<TextView>(R.id.tvCname)
            container.setPadding(5, 20, 5, 20)
            tv1.text = data?.play_sec_cname
            data?.isSelected = currentPos == position
            rightTop = getData()[currentPos]?.play_sec_cname.toString()
            dmAdapter?.currentRightTop = getData()[currentPos]?.play_sec_cname.toString()
            changeBg(data?.isSelected, container, tv1, null, true)
            dmAdapter?.refresh(getData()[currentPos]?.play_sec_data)
            holder.itemView.setOnClickListener {
                if (currentPos == position) return@setOnClickListener
                currentPos = position
                resetAdapter(true)
                this.notifyDataSetChanged()
            }
        }

        fun resetData() {
            currentPos = 0
            notifyDataSetChanged()
        }

    }

    //右上福彩3D 体彩排列三  (二字定位，三字定位)
    inner class Right3DTopAdapter(private var isShowOdds: Boolean = false) :
        BaseRecyclerAdapter<PlayUnitData>() {
        private var currentPos = 0
        private var isFirst = true
        override fun getItemLayoutId(viewType: Int) = R.layout.adapter_game_bet_right_top
        override fun bindData(holder: RecyclerViewHolder, position: Int, data: PlayUnitData?) {
            val container = holder.findViewById<LinearLayout>(R.id.gameBetLinearLayout)
            val tv1 = holder.findViewById<TextView>(R.id.tvCname)
            container.setPadding(5, 10, 5, 10)
            tv1.text = data?.play_sec_cname
            data?.isSelected = currentPos == position
            changeBg(data?.isSelected, container, tv1, null, true)
            if (isFirst) fc3DAdapter?.refresh(getData()[currentPos]?.play_sec_data)
            if (isShowOdds) {
                tvHx?.text = data?.play_sec_options?.get(0)?.play_odds ?: "0"
                tv3dRight?.text = getData()[currentPos]?.play_sec_info?.get(0) ?: "null"
                tv3dLeft?.text = getData()[currentPos]?.play_sec_info?.get(1) ?: "null"
            }
            holder.itemView.setOnClickListener {
                isFirst = false
                if (currentPos == position) return@setOnClickListener
                currentPos = position
                fc3DAdapter?.refresh(getData()[currentPos]?.play_sec_data)
                notifyDataSetChanged()

            }
        }

        fun resetSelect() {
            for (res in data) {
                if (!res.play_sec_data.isNullOrEmpty()) {
                    for (result in res.play_sec_data) {
                        if (result.isSelected) {
                            result.isSelected = false
                        }
                    }
                }
            }
            notifyDataSetChanged()
        }
    }


    //右边上部分类 香港彩
    inner class RightTopXgcAdapter : BaseRecyclerAdapter<PlayUnitData>() {
        private var currentPos = 0
        override fun getItemLayoutId(viewType: Int) = R.layout.adapter_game_bet_right_top
        override fun bindData(holder: RecyclerViewHolder, position: Int, data: PlayUnitData?) {
            val container = holder.findViewById<LinearLayout>(R.id.gameBetLinearLayout)
            val tv1 = holder.findViewById<TextView>(R.id.tvCname)
            container.setPadding(5, 20, 5, 20)
            tv1.text = data?.play_sec_cname
            data?.isSelected = currentPos == position
            rightTop = getData()[currentPos]?.play_sec_cname.toString()
            changeBg(data?.isSelected, container, tv1, null, true)
            xgcTmAdapter?.refresh(getData()[currentPos]?.play_sec_data)
            holder.itemView.setOnClickListener {
                if (currentPos == position) return@setOnClickListener
                currentPos = position
                if (currentLeft == "连码" || currentLeft == "连肖连尾") {
                    xgcTmAdapter?.resetData()
                    xgcTmAdapter?.clear()
                    betList.clear()
                    RxBus.get().post(LotteryLiveBet("", betList))
                    xgcLmSelectList.clear()
                }
                rvContent?.removeAllViews()
                xgcTmAdapter?.refresh(getData()[currentPos]?.play_sec_data)
                this.notifyDataSetChanged()
            }
        }

        fun resetData() {
            currentPos = 0
            notifyDataSetChanged()
        }

        fun resetSelect() {
            for (res in data) {
                if (!res.play_sec_data.isNullOrEmpty()) {
                    for (result in res.play_sec_data) {
                        if (result.isSelected) {
                            result.isSelected = false
                        }
                    }
                }
            }
            notifyDataSetChanged()
        }
    }

    /**
     * 连码（香港彩）处理
     */
    private var xgcLmSelectList = ArrayList<String>()
    fun xgcLm(adapter: BaseRecyclerAdapter<PlaySecData>, position: Int, data: PlaySecData?) {
        var maxSelect = -1
        var minSelect = -1
        when (rightTop) {
            "三中二" -> {
                maxSelect = 7
                minSelect = 2
            }
            "二全中", "二中特", "特串" -> {
                maxSelect = 7
                minSelect = 1
            }
            "三全中" -> {
                maxSelect = 10
                minSelect = 2
            }
            "四全中" -> {
                maxSelect = 4
                minSelect = 3
            }
            "二连肖", "二连尾" -> {
                maxSelect = 11
                minSelect = 1
            }
            "三连肖", "三连尾" -> {
                maxSelect = 11
                minSelect = 2
            }
            "四连肖", "四连尾" -> {
                maxSelect = 11
                minSelect = 3
            }
            "五连肖", "五连尾" -> {
                maxSelect = 11
                minSelect = 4
            }
        }
        if (data?.isSelected != true) {
            if (xgcLmSelectList.size < maxSelect) {
                xgcLmSelectList.add(data?.play_class_cname.toString())
                if (xgcLmSelectList.size > minSelect) {
                    betList.clear()
                    val resultList = CalculationGame.combination(xgcLmSelectList, minSelect + 1)
                    for (item in resultList) {
                        val numCname = when (minSelect) {
                            1 -> item[0] + "," + item[1]
                            2 -> item[0] + "," + item[1] + "," + item[2]
                            3 -> item[0] + "," + item[1] + "," + item[2] + "," + item[3]
                            4 -> item[0] + "," + item[1] + "," + item[2] + "," + item[3] + "," + item[4]
                            else -> {
                                ToastUtils.showToast("内部错误")
                                return
                            }
                        }
                        val numName = when (minSelect) {
                            1 -> judgeName(item[0] ?: "") + "," + judgeName(item[1] ?: "")
                            2 -> judgeName(item[0] ?: "") + "," + judgeName(
                                item[1] ?: ""
                            ) + "," + judgeName(item[2] ?: "")
                            3 -> judgeName(item[0] ?: "") + "," + judgeName(
                                item[1] ?: ""
                            ) + "," + judgeName(item[2] ?: "") + "," + judgeName(item[3] ?: "")
                            4 -> judgeName(item[0] ?: "") + "," + judgeName(
                                item[1] ?: ""
                            ) + "," + judgeName(item[2] ?: "") + "," + judgeName(
                                item[3] ?: ""
                            ) + "," + judgeName(item[4] ?: "")
                            else -> {
                                ToastUtils.showToast("内部错误")
                                return
                            }
                        }
                        addOrDeleteBetData(
                            true,
                            data?.play_sec_name.toString(),
                            numName,
                            data?.play_sec_cname.toString(),
                            numCname,
                            data?.play_odds.toString()
                        )
                    }
                }
                data?.isSelected = true
                adapter.notifyItemChanged(position)
            } else ToastUtils.showToast("当前玩法最多可选择" + maxSelect + "个号码!")
        } else {
            betList.clear()
            xgcLmSelectList.remove(data.play_class_cname)
            if (xgcLmSelectList.size > minSelect) {
                val resultList = CalculationGame.combination(xgcLmSelectList, minSelect + 1)
                for (item in resultList) {
                    val num = when (minSelect) {
                        1 -> item[0] + "," + item[1]
                        2 -> item[0] + "," + item[1] + "," + item[2]
                        3 -> item[0] + "," + item[1] + "," + item[2] + "," + item[3]
                        else -> {
                            ToastUtils.showToast("内部错误")
                            return
                        }
                    }
                    val numName = when (minSelect) {
                        1 -> judgeName(item[0] ?: "") + "," + judgeName(item[1] ?: "")
                        2 -> judgeName(item[0] ?: "") + "," + judgeName(
                            item[1] ?: ""
                        ) + "," + judgeName(item[2] ?: "")
                        3 -> judgeName(item[0] ?: "") + "," + judgeName(
                            item[1] ?: ""
                        ) + "," + judgeName(item[2] ?: "") + "," + judgeName(item[3] ?: "")
                        4 -> judgeName(item[0] ?: "") + "," + judgeName(
                            item[1] ?: ""
                        ) + "," + judgeName(item[2] ?: "") + "," + judgeName(
                            item[3] ?: ""
                        ) + "," + judgeName(item[4] ?: "")
                        else -> {
                            ToastUtils.showToast("内部错误")
                            return
                        }
                    }
                    addOrDeleteBetData(
                        true,
                        data.play_sec_name.toString(),
                        numName,
                        data.play_sec_cname.toString(),
                        num,
                        data.play_odds.toString()
                    )
                }
            }
//            else ViewUtils.setGone(bottomGameBetLayout)
            data.isSelected = false
            adapter.notifyItemChanged(position)
        }
    }

    private fun judgeName(name: String): String {
        return when (name) {
            "鼠" -> "mouse"
            "牛" -> "ox"
            "虎" -> "tiger"
            "兔" -> "rabbit"
            "龙" -> "dragon"
            "蛇" -> "snake"
            "马" -> "horse"
            "羊" -> "sheep"
            "猴" -> "monkey"
            "鸡" -> "rooster"
            "狗" -> "dog"
            "猪" -> "pig"
            else -> name
        }
    }

    /**
     * 合肖（香港彩）处理
     */
    private var xgcHxSelectList = ArrayList<String>()

    @SuppressLint("SetTextI18n")
    fun cgcHx(adapter: BaseRecyclerAdapter<PlaySecData>, position: Int, data: PlaySecData?) {
        var maxSelect = -1
        var minSelect = -1
        when (data?.play_sec_name) {
            "lhc_hx" -> {
                maxSelect = 10
                minSelect = 1
            }
            "lhc_zxbz" -> {
                maxSelect = 12
                minSelect = 4
            }
            "3d_zxs" -> {
                maxSelect = 10
                minSelect = 4
            }
            "3d_zxl" -> {
                maxSelect = 8
                minSelect = 3
            }
        }
        if (data?.isSelected != true) {
            if (xgcLmSelectList.size < maxSelect) {
                xgcLmSelectList.add(data?.play_class_cname.toString())
                xgcHxSelectList.add(data?.play_class_name.toString())
                if (xgcLmSelectList.size > minSelect) {
                    val result = data?.play_sec_options?.get(xgcLmSelectList.size - (minSelect + 1))
                    ViewUtils.setVisible(layoutOdds)
                    tvHx?.text = result?.play_odds
                    betList.clear()
                    val num = CalculationGame.listToString(xgcLmSelectList).toString()
                    val numName = CalculationGame.listToString(xgcHxSelectList).toString()
                    addOrDeleteBetData(
                        true,
                        data?.play_sec_name.toString(),
                        numName,
                        data?.play_sec_cname.toString(),
                        num,
                        result?.play_odds.toString()
                    )
                } else ViewUtils.setGone(layoutOdds)
                data?.isSelected = true
                adapter.notifyItemChanged(position)
            } else ToastUtils.showToast("当前玩法最多可选择" + maxSelect + "个号码!")
        } else {
            betList.clear()
            xgcLmSelectList.remove(data.play_class_cname)
            xgcHxSelectList.remove(data.play_class_name)
            if (xgcLmSelectList.size > minSelect) {
                val result = data.play_sec_options?.get(xgcLmSelectList.size - (minSelect + 1))
                ViewUtils.setVisible(layoutOdds)
                tvHx?.text = result?.play_odds
                val num = CalculationGame.listToString(xgcLmSelectList).toString()
                val numName = CalculationGame.listToString(xgcHxSelectList).toString()
                addOrDeleteBetData(
                    true,
                    data.play_sec_name.toString(),
                    numName,
                    data.play_sec_cname.toString(),
                    num,
                    result?.play_odds.toString()
                )
            } else {
//                setGone(bottomGameBetLayout)
                ViewUtils.setGone(layoutOdds)
            }
            data.isSelected = false
            adapter.notifyItemChanged(position)
        }
    }


    /**
     * 福彩3D
     */
    inner class Fc3dAdapter : BaseRecyclerAdapter<PlaySecData>() {
        val TYPE_1 = 1
        val TYPE_2 = 2
        val TYPE_3 = 3
        val TYPE_4 = 4
        val TYPE_5 = 5
        val TYPE_6 = 6
        val TYPE_7 = 7

        override fun getItemViewType(position: Int): Int {
            return when (data[position].type) {
                "3d_full" -> TYPE_1
                "3d_2" -> TYPE_2
                "3d_4" -> TYPE_3
                "3d_3" -> TYPE_4
                "3d_ezdw" -> TYPE_6
                "3d_szdw" -> TYPE_7
                else -> TYPE_5
            }
        }

        override fun getItemLayoutId(viewType: Int): Int {
            return when (viewType) {
                TYPE_1 -> R.layout.adapter_game_bet_title
                TYPE_2, TYPE_3, TYPE_4 -> R.layout.adapter_game_bet_content
                TYPE_6, TYPE_7 -> R.layout.adapter_game_xgc_tm
                else -> R.layout.adapter_game_xgc_holder
            }
        }

        override fun bindData(holder: RecyclerViewHolder, position: Int, data: PlaySecData?) {
            when (getItemViewType(position)) {
                TYPE_1 -> {
                    holder.text(R.id.tvGameType, data?.title)
                }
                TYPE_2, TYPE_3, TYPE_4 -> {
                    val container = holder.findViewById<LinearLayout>(R.id.gameBetLinearLayout)
                    val tv1 = holder.findViewById<TextView>(R.id.tvCname)
                    val tv2 = holder.findViewById<TextView>(R.id.tvOdds)
                    tv1.text = data?.play_class_cname
                    tv2.text = data?.play_odds.toString()
                    changeBg(data?.isSelected, container, tv1, tv2)
                    holder.itemView.setOnClickListener {
                        data?.isSelected = data?.isSelected != true
                        addOrDeleteBetData(
                            data?.isSelected == true,
                            data?.play_sec_name ?: "-1",
                            data?.play_class_name ?: "-1",
                            data?.title ?: "-1",
                            data?.play_class_cname ?: "-1",
                            data?.play_odds ?: "-1"
                        )
                        notifyItemChanged(position)
                    }
                }
                TYPE_5->{}
                TYPE_6 -> {
                    val container = holder.findViewById<LinearLayout>(R.id.gameBetLinearLayout)
                    val tv1 = holder.findViewById<TextView>(R.id.tvXgcCname)
                    val tv2 = holder.findViewById<TextView>(R.id.tvXgcOdds)
                    GamePlay.changeBg(data?.isSelected, container, tv1)
                    tv1.text = data?.play_class_cname
                    ViewUtils.setGone(tv2)
                    tv1.text = data?.play_class_cname
                    holder.itemView.setOnClickListener {
                        data?.isSelected = data?.isSelected != true
                        calculationEz(
                            data?.isSelected == true,
                            data?.rzPosition ?: -1,
                            data?.play_class_name ?: "null",
                            data?.play_sec_name ?: "null",
                            data?.title ?: "null",
                            data?.play_odds ?: "null"
                        )
                        notifyItemChanged(position)
                    }
                }
                TYPE_7 -> {
                    val container = holder.findViewById<LinearLayout>(R.id.gameBetLinearLayout)
                    val tv1 = holder.findViewById<TextView>(R.id.tvXgcCname)
                    val tv2 = holder.findViewById<TextView>(R.id.tvXgcOdds)
                    GamePlay.changeBg(data?.isSelected, container, tv1)
                    tv1.text = data?.play_class_cname
                    ViewUtils.setGone(tv2)
                    tv1.text = data?.play_class_cname
                    holder.itemView.setOnClickListener {
                        data?.isSelected = data?.isSelected != true
                        calculationSz(
                            data?.isSelected == true,
                            data?.rzPosition ?: -1,
                            data?.play_class_name ?: "null",
                            data?.play_sec_name ?: "null",
                            data?.title ?: "null",
                            data?.play_odds ?: "null"
                        )
                        notifyItemChanged(position)
                    }
                }
            }
        }

        fun resetData() {
            for (item in this.data) {
                if (item.isSelected) item.isSelected = false
            }
            notifyDataSetChanged()
        }
    }

    /**
     * 二字定位
     */
    var ezLeft = arrayListOf<String>()
    var ezRight = arrayListOf<String>()
    fun calculationEz(
        isAdd: Boolean,
        index: Int,
        data: String,
        play_sec_name: String,
        play_sec_cname: String,
        odds: String
    ) {
        if (index == 0) {
            if (isAdd) ezLeft.add(data) else ezLeft.remove(data)
        } else {
            if (isAdd) ezRight.add(data) else ezRight.remove(data)
        }
        if (ezLeft.isNotEmpty() && ezRight.isNotEmpty()) {
            val list3: ArrayList<List<String>> = ArrayList()
            list3.add(ezLeft)
            list3.add(ezRight)
            val result = CalculationGame.getPermutations(list3)
            if (!result.isNullOrEmpty()) {
                betList.clear()
                for (item in result) {
                    addOrDeleteBetData(
                        true,
                        play_class_cname = item,
                        play_class_name = item,
                        play_sec_name = play_sec_name,
                        play_odds = odds,
                        play_sec_cname = play_sec_cname
                    )
                }
            }
        }
    }

    /**
     * 三字定位
     */
    var sz1 = arrayListOf<String>()
    var sz2 = arrayListOf<String>()
    var sz3 = arrayListOf<String>()
    fun calculationSz(
        isAdd: Boolean,
        index: Int,
        data: String,
        play_sec_name: String,
        play_sec_cname: String,
        odds: String
    ) {
        if (index == 0) {
            if (isAdd) sz1.add(data) else sz1.remove(data)
        } else if (index == 1) {
            if (isAdd) sz2.add(data) else sz2.remove(data)
        } else {
            if (isAdd) sz3.add(data) else sz3.remove(data)
        }
        if (sz1.isNotEmpty() && sz2.isNotEmpty() && sz3.isNotEmpty()) {
            val list3: ArrayList<List<String>> = ArrayList()
            list3.add(sz1)
            list3.add(sz2)
            list3.add(sz3)
            val result = CalculationGame.getPermutations(list3)
            LogUtils.e("======>" + result)
            if (!result.isNullOrEmpty()) {
                betList.clear()
                for (item in result) {
                    addOrDeleteBetData(
                        true,
                        play_class_cname = item,
                        play_class_name = item,
                        play_sec_name = play_sec_name,
                        play_odds = odds,
                        play_sec_cname = play_sec_cname
                    )
                }
            }
        }else{
            betList.clear()
            RxBus.get().post(LotteryLiveBet(rightTop, betList))
        }
    }


    fun addOrDeleteBetData(
        isAdd: Boolean,
        play_sec_name: String,
        play_class_name: String,
        play_sec_cname: String,
        play_class_cname: String,
        play_odds: String
    ) {
        val bean = PlaySecData(
            play_sec_name = play_sec_name,
            play_class_name = play_class_name,
            play_sec_cname = play_sec_cname,
            play_class_cname = play_class_cname,
            play_odds = play_odds
        )

        if (isAdd) {
            betList.add(bean)
        } else removeElement(betList, bean)
        RxBus.get().post(LotteryLiveBet(rightTop, betList))
        if (betList.isNullOrEmpty())ViewUtils.setGone(contentHolder) else ViewUtils.setVisible(contentHolder)
    }


    fun resetAdapter(boolean: Boolean = false) {
        rvContent?.removeAllViews()
        rvType?.removeAllViews()
        lmAdapter?.resetData()
        lmAdapter?.clear()
        kjAdapter?.resetData()
        kjAdapter?.clear()
        dan1D10Adapter?.resetData()
        dan1D10Adapter?.clear()
        gyhAdapter?.resetData()
        gyhAdapter?.clear()
        zhAdapter?.resetData()
        zhAdapter?.clear()
        dmAdapter?.resetData()
        xgcTmAdapter?.resetData()
        xgcTmAdapter?.clear()
        fc3DAdapter?.resetData()
        fc3DAdapter?.clear()
        if (!boolean) {
            dmAdapter?.clear()
            rightTopAdapter?.clear()
            rightTopXgcAdapter?.clear()
            fc3DTopAdapter?.clear()
        }
        betList.clear()
        kjContentList.clear()
        xgcLmSelectList.clear()
        xgcHxSelectList.clear()
        kjList.clear()
        ezLeft.clear()
        ezRight.clear()
        sz1.clear()
        sz2.clear()
        sz3.clear()
        rightTop = "-1"
        currentDouble = 0
        currentSingle = 0
        currentTriple = 0
        if (currentLeft == "二字定位") {
            ViewUtils.setVisible(layout3D)
        } else {
            ViewUtils.setGone(layout3D)
        }
        if (currentLeft == "三字定位") {
            ViewUtils.setVisible(layout3D3z)
        } else {
            ViewUtils.setGone(layout3D3z)
        }
        if (currentLeft == "二字定位" || currentLeft == "三字定位") {
            ViewUtils.setVisible(layoutOdds)
        } else {
            ViewUtils.setGone(layoutOdds)
        }
    }

    override fun onFragmentPause() {
        super.onFragmentPause()
        resetAllAdapter()
    }

    fun resetAllAdapter() {
        lmAdapter?.resetData()
        kjAdapter?.resetData()
        dan1D10Adapter?.resetData()
        gyhAdapter?.resetData()
        zhAdapter?.resetData()
        xgcTmAdapter?.resetData()
        dmAdapter?.resetData()
        fc3DAdapter?.resetData()
        rightTopXgcAdapter?.resetSelect()
        fc3DTopAdapter?.resetSelect()
        currentDouble = 0
        currentSingle = 0
        currentTriple = 0
        kjList.clear()
        ezLeft.clear()
        ezRight.clear()
        sz1.clear()
        sz2.clear()
        sz3.clear()
        rightTop = "-1"
        betList.clear()
        kjContentList.clear()
        xgcLmSelectList.clear()
        xgcHxSelectList.clear()
        rightTopAdapter?.resetData()
        rightTopXgcAdapter?.resetData()
        if (currentLeft == "二字定位") {
            ViewUtils.setVisible(layout3D)
        } else {
            ViewUtils.setGone(layout3D)
        }
        if (currentLeft == "三字定位") {
            ViewUtils.setVisible(layout3D3z)
        } else {
            ViewUtils.setGone(layout3D3z)
        }
        if (currentLeft == "二字定位" || currentLeft == "三字定位") {
            ViewUtils.setVisible(layoutOdds)
        } else {
            ViewUtils.setGone(layoutOdds)
        }
        myScrollView.scrollTo(0,0)
    }

    //背景变化香港彩
    fun changeBgXgc(
        isSelected: Boolean?,
        container: LinearLayout
    ) {
        if (isSelected == true) {
            container.background = ViewUtils.getDrawable(R.drawable.bet_select_background)
        } else {
            container.background = ViewUtils.getDrawable(R.drawable.home_bet_normal_background)
        }
    }

    //背景变化
    fun changeBg(
        isSelected: Boolean?,
        container: LinearLayout,
        tv1: TextView,
        tv2: TextView? = null,
        isSpecial: Boolean = false
    ) {
        if (isSelected == true) {
            if (isSpecial) {
                container.background =
                    ViewUtils.getDrawable(R.drawable.bet_special_selected_background)
            } else container.background = ViewUtils.getDrawable(R.drawable.bet_select_background)
            tv1.setTextColor(ViewUtils.getColor(R.color.color_FF3131))
            tv2?.setTextColor(ViewUtils.getColor(R.color.color_FF3131))
        } else {
            if (isSpecial) {
                container.background =
                    ViewUtils.getDrawable(R.drawable.bet_special_normal_background)
            } else container.background =
                ViewUtils.getDrawable(R.drawable.home_bet_normal_background)
            tv1.setTextColor(ViewUtils.getColor(R.color.color_333333))
            tv2?.setTextColor(ViewUtils.getColor(R.color.color_FF3131))
        }
    }

    //自定义删除方法,自带的remove有毒
    private fun removeElement(mutableList: MutableList<PlaySecData>, bean: PlaySecData) {
        var index = -1
        for ((num, item) in mutableList.withIndex()) {
            if (item.play_sec_name == bean.play_sec_name && item.play_class_name == bean.play_class_name) {
                index = num
            }
        }
        try {
            mutableList.removeAt(index)
        } catch (e: Exception) {
        }
    }

    companion object {
        fun newInstance(
            lotteryId: String,
            type: String,
            position: Int,
            data: ArrayList<LotteryPlayListResponse>
        ) = LiveRoomBetFragmentContent().apply {
            arguments = Bundle(1).apply {
                putParcelableArrayList("LotteryPlayListResponse", data)
                putString("lotteryId", lotteryId)
                putString("currentType", type)
                putInt("currentIndex", position)
            }
        }
    }

}