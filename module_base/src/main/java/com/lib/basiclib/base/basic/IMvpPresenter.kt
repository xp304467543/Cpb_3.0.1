package com.lib.basiclib.base.basic

/**

 * @since 18-7-25 下午4:37
 * MVP的P层基类
 */

interface IMvpPresenter<V : IMvpContract.View> {

    /**
     * 绑定视图
     */
    fun attachView(view: V)

    /**
     * 解绑视图
     */
    fun detachView()
}