package com.lib.basiclib.base.basic

/**

 * @since 2020/12/25 15:27
 */
interface IMvpContract {

    interface View : IMvpView

    interface Presenter<V : View> : IMvpPresenter<V>

    interface Model : IMvpModel
}