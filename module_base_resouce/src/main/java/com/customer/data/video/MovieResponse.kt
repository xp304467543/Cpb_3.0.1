package com.customer.data.video

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/29
 * @ Describe
 *
 */

@Parcelize
data class MovieType(
    val id: Int?,
    val pid: Int?,
    val name: String?,
    val children: ArrayList<MovieTypeChild>?
) : Parcelable

@Parcelize
data class MovieTypeChild(
    val id: Int?,
    val pid: Int?,
    val name: String?,
    val weigh: Int?,
    val state: Int?,
    val created: Long?,
    val updated: Long?
) : Parcelable


data class MovieClassification(
    val id: Int?,
    val pid: Int?,
    val name: String?,
    val weigh: Int?,
    val state: Int?,
    val created: Long?,
    val updated: Long?, var list: List<ClassificationChild>
)

data class ClassificationChild(
    val id: Int?,
    val author: String?,
    val title: String?,
    val tag: String?,
    val cover: String?,
    val play_time: String?,
    val play_url: String?,
    val reads: String?,
    val praise: String?,
    val tread: String?,
    val shelftime: String?
)

data class VideoBanner(
    val banner_movie_home: List<VideoBannerChild>?,
    val banner_movie_list: List<VideoBannerChild>?,
    val banner_movie_type_list: VideoBannerChild2,
    val banner_movie_play: List<VideoBannerChild>?
)

data class VideoBannerChild(
    val type: String?,
    val title: String?,
    val name: String?,
    val image_url: String?,
    val content: String?,
    val url: String?
)

data class VideoBannerChild2(
    val banner_movie_type_5: VideoBannerChild?,
    val banner_movie_type_4: VideoBannerChild?,
    val banner_movie_type_3: VideoBannerChild?,
    val banner_movie_type_2: VideoBannerChild?,
    val banner_movie_type_1: VideoBannerChild?
)



@Parcelize
data class VideoBannerForDataChild(val url1: String?,val url2: String?, val link: String) : Parcelable

//换一批
//data class VideoMore(val count:Int?,val list:List<ClassificationChild>?)

data class VideoHot(val count:Int,val list:List<VideoMoreChild>)

data class VideoMoreChild(
    val id: Int?,
    val type: Int?,
    val cid: String?,
    val author: String?,
    val title: String?,
    val desc: String?,
    val tag: String?,
    val cover: String?,
    val play_time: String?,
    val down_url: String?,
    val reads: String?,
    val praise: String?,
    val tread: String?,
    val comments: String?,
    val downnum: String?,
    val recommendlevel: String?,
    val ishd: String?,
    val istop: String?,
    val created: String?,
    val updated: String?,
    val shelftime: String?

)

data class VideoMore(val count:Int=0,val list:List<VideoMoreChild>)

data class VideoPlay(val id: Int?,
                     val cover: String?,
                     val play_url: String?,
                     val title:String?,
                     val reads: String?,
                     val state: String?,
                     val praise: String?,
                     val shelftime:Long?,
                     val tread: String?,val list:List<VideoMoreChild>?)


data class MovieZan(val praise:Int?,val tread:Int?,val state:Int?,val msg:String?)