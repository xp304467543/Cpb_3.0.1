package com.customer.utils.okutil;

/**
 * @ Author  QinTian
 * @ Date  1/4/21
 * @ Describe
 */
public interface ProgressListener {
    /**
     * 显示进度
     *
     * @param mProgress
     */
    public void onProgress(int mProgress,long contentSize);

    /**
     * 完成状态
     *
     * @param totalSize
     */
    public void onDone(long totalSize);
}