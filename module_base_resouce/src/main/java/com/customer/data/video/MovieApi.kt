package com.customer.data.video

import com.customer.data.UserInfoSp
import cuntomer.api.ApiSubscriber
import cuntomer.net.BaseApi

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/29
 * @ Describe
 *
 */
object MovieApi : BaseApi {


    //视频分类
    private const val VIDEO_TYPE = "VideoAPI/getVideoClass"

    //视频分类子项
    private const val VIDEO_TYPE_DATA = "VideoAPI/classification"

    //banner
    private const val VIDEO_BANNER = "api/v1_1/user/get_movie_banner"

    //更多视频
   private const val VIDEO_TYPE_MORE = "VideoAPI/domestic/"

    //搜索视频
    private const val VIDEO_SEARCH =  "VideoAPI/findVideo"

    //搜索视频
    private const val VIDEO_HOT =  "VideoAPI/MostSeries"

    //播放页面
    private const val VIDEO_PLAY ="VideoAPI/playPage"

    //播放页面详细
    private const val VIDEO_PLAY_INFO ="VideoAPI/playFullPage"

    //猜你喜欢
    private const val VIDEO_GUSS_LIKE ="VideoAPI/guessLike"

    //赞
    private const val VIDEO_ZAN ="VideoAPI/ISPraise"



    fun getMovieType(function: ApiSubscriber<List<MovieType>>.() -> Unit){
        val subscriber = object : ApiSubscriber<List<MovieType>>() {}
        subscriber.function()
        getApiMovie().post<List<MovieType>>(VIDEO_TYPE).subscribe(subscriber)
    }

    fun getMovieTypeData( typeId:Int, limit:Int, page:Int,perPage:Int,function: ApiSubscriber<List<MovieClassification>>.() -> Unit){
        val subscriber = object : ApiSubscriber<List<MovieClassification>>() {}
        subscriber.function()
        getApiMovie().post<List<MovieClassification>>(VIDEO_TYPE_DATA)
            .params("typeId",typeId)
            .params("limit",limit)
            .params("page",page)
            .params("perPage",perPage)
            .subscribe(subscriber)
    }

    fun getMovieBanner(function: ApiSubscriber<VideoBanner>.() -> Unit){
        val subscriber = object : ApiSubscriber<VideoBanner>() {}
        subscriber.function()
        getApi().get<VideoBanner>(VIDEO_BANNER)
            .subscribe(subscriber)
    }

    /**
     * 换一批  更多 视频
     * column：updated=最新 reads=最多观看 praise=最多喜欢
     * tag typeId不为null时 此参数可选，指定标签类型。
     */
    fun getVideoChange(typeId:Int, cid:Int, num:Int, isMore:Boolean, function: ApiSubscriber<List<ClassificationChild>>.() -> Unit){
        val subscriber = object : ApiSubscriber<List<ClassificationChild>>() {}
        subscriber.function()
        getApiMovie().post<List<ClassificationChild>>(VIDEO_TYPE_MORE)
            .params("typeId",typeId)
            .params("cid",cid)
            .params("num",num)
            .params("isMore",isMore)
            .subscribe(subscriber)
    }

    /**
     * 更多视频
     * column：updated=最新 reads=最多观看 praise=最多喜欢
     * tag typeId不为null时 此参数可选，指定标签类型。
     */
    fun getVideoMore(typeId:Int, cid:Int, num:Int, isMore:Boolean,column:String,page:Int,prePage:Int ,function: ApiSubscriber<List<VideoMoreChild>>.() -> Unit){
        val subscriber = object : ApiSubscriber<List<VideoMoreChild>>() {}
        subscriber.function()
        getApiMovie().post<List<VideoMoreChild>>(VIDEO_TYPE_MORE)
            .params("typeId",typeId)
            .params("cid",cid)
            .params("num",num)
            .params("column",column)
            .params("isMore",isMore)
            .params("page",page)
            .params("prePage",prePage)
            .subscribe(subscriber)
    }



    /**
     * 热门推荐
     */
    fun getVideoHot(function: ApiSubscriber<VideoHot>.() -> Unit){
        val subscriber = object : ApiSubscriber<VideoHot>() {}
        subscriber.function()
        getApiMovie().post<VideoHot>(VIDEO_HOT)
            .params("typeId","")
            .params("cid","")
            .params("tag","")
            .params("num",6)
            .params("column","updated")
            .params("isMore",true)
            .params("page",1)
            .params("perPage",6)
            .subscribe(subscriber)
    }

    /**
     * 搜索视频
     */
    fun getSearchVideo(name:String,function: ApiSubscriber<List<VideoMoreChild>>.() -> Unit){
        val subscriber = object : ApiSubscriber<List<VideoMoreChild>>() {}
        subscriber.function()
        getApiMovie().post<List<VideoMoreChild>>(VIDEO_SEARCH)
            .params("name",name)
            .subscribe(subscriber)
    }

    /**
     * 播放页面
     */
    fun getPlayUrl(moviesid:Int,function: ApiSubscriber<List<VideoPlay>>.() -> Unit){
        val subscriber = object : ApiSubscriber<List<VideoPlay>>() {}
        subscriber.function()
        getApiMovie().post<List<VideoPlay>>(VIDEO_PLAY)
            .headers("token",UserInfoSp.getToken())
            .params("moviesid",moviesid)
            .subscribe(subscriber)
    }

    /**
     * 播放页面详细
     */
    fun getPlayInfo(moviesid:Int,function: ApiSubscriber<VideoPlay>.() -> Unit){
        val subscriber = object : ApiSubscriber<VideoPlay>() {}
        subscriber.function()
        getApiMovie().post<VideoPlay>(VIDEO_PLAY_INFO)
            .headers("token",UserInfoSp.getToken())
            .params("moviesid",moviesid)
            .subscribe(subscriber)
    }
    /**
     * 猜你喜欢
     */
    fun getYouLike(moviesid:Int,function: ApiSubscriber<List<VideoMoreChild>>.() -> Unit){
        val subscriber = object : ApiSubscriber<List<VideoMoreChild>>() {}
        subscriber.function()
        getApiMovie().post<List<VideoMoreChild>>(VIDEO_GUSS_LIKE)
            .params("moviesid",moviesid)
            .subscribe(subscriber)
    }


    /**
     * ZAN
     */
    fun getZan(moviesid:Int,praise:Int,function: ApiSubscriber<MovieZan>.() -> Unit){
        val subscriber = object : ApiSubscriber<MovieZan>() {}
        subscriber.function()
        getApiMovie().post<MovieZan>(VIDEO_ZAN)
            .headers("token",UserInfoSp.getToken())
            .params("moviesid",moviesid)
            .params("praise",praise)
            .subscribe(subscriber)
    }
}