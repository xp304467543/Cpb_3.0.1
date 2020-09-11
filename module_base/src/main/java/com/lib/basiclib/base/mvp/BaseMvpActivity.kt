package com.lib.basiclib.base.mvp

import android.os.Bundle
import com.lib.basiclib.base.activity.BaseNavActivity
import com.lib.basiclib.base.basic.IMvpContract

/**

 * @since 2019/1/3 23:06
 */
abstract class BaseMvpActivity<P : IMvpContract.Presenter<*>> : BaseNavActivity(), IMvpContract.View {

    protected open lateinit var mPresenter: P

    override fun isActive(): Boolean = !isDestroyed

    override fun onCreate(savedInstanceState: Bundle?) {
        mPresenter = attachPresenter()
        attachView()
        super.onCreate(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }

    abstract fun attachView()

    abstract fun attachPresenter(): P
}