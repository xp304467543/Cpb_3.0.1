package com.rxnetgo.rxcache.stategy

import com.rxnetgo.rxcache.core.CacheTarget
import com.rxnetgo.rxcache.core.RxCache
import com.rxnetgo.rxcache.core.RxCacheHelper
import com.rxnetgo.rxcache.data.CacheResult
import io.reactivex.Flowable
import org.reactivestreams.Publisher
import java.lang.reflect.Type

/**
 * 先读取缓存，然后请求网络，如果有缓存，网络会二次覆盖，如果没缓存，网络会一次覆盖
 */
class FirstCacheAndRemoteStrategy : ICacheStrategy {

    override fun <T> execute(rxCache: RxCache, key: String, source: Flowable<T>, type: Type): Publisher<CacheResult<T>> {
        val cache = RxCacheHelper.loadCacheFlowable<T>(rxCache, key, type, true)
        val remote = RxCacheHelper.loadRemoteFlowable(rxCache, key, source, CacheTarget.Disk, false)

        return Flowable.concatDelayError(listOf(cache, remote))
                .filter { result -> result.data != null }
    }
}