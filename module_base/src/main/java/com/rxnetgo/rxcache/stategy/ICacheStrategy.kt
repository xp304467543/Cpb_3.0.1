package com.rxnetgo.rxcache.stategy

import com.rxnetgo.rxcache.core.RxCache
import com.rxnetgo.rxcache.data.CacheResult
import io.reactivex.Flowable
import org.reactivestreams.Publisher
import java.lang.reflect.Type

/**

 * @since 2019/2/21 18:04
 * 缓存策略基类，提取实现缓存的方法
 */
interface ICacheStrategy {

    fun <T> execute(rxCache: RxCache, key: String, source: Flowable<T>, type: Type): Publisher<CacheResult<T>>
}