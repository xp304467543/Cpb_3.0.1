package com.rxnetgo.rxcache.stategy

import com.rxnetgo.rxcache.core.CacheTarget
import com.rxnetgo.rxcache.core.RxCache
import com.rxnetgo.rxcache.core.RxCacheHelper
import com.rxnetgo.rxcache.data.CacheResult
import io.reactivex.Flowable
import org.reactivestreams.Publisher
import java.lang.reflect.Type

/**

 * @since 2019/2/21 18:18
 *
 * 只读取远程请求的策略
 */
class OnlyRemoteStrategy : ICacheStrategy {

    override fun <T> execute(rxCache: RxCache, key: String, source: Flowable<T>, type: Type): Publisher<CacheResult<T>> {
        return RxCacheHelper.loadRemoteFlowable(rxCache, key, source, CacheTarget.Disk, true)
    }
}