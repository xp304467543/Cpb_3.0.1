package com.lib.basiclib.base.xui.widget.spinner.editspinner;

/**
 * 监听输入并过滤
 *

 * @since 2019/1/14 下午10:14
 */
public interface EditSpinnerFilter {
    /**
     * editText输入监听
     * @param keyword
     * @return
     */
    boolean onFilter(String keyword);
}
