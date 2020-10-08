package cuntomer.them

/**
 *
 * @ Author  QinTian
 * @ Date  2020/8/10
 * @ Describe 皮肤接口。用于变换UI的皮肤。
 *
 */
interface IMode {
    /**
     * 设置主题
     * @param mode 支持的主题
     */
    fun setMode(mode: AppMode)
}
