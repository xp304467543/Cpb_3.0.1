package com.player.ali.alivcplayerexpand.view.dlna.manager;

import android.content.Context;

import com.player.ali.alivcplayerexpand.view.dlna.domain.IDevice;


/**
 * 设备管理类
 */

public interface IDeviceManager {

    /**
     * 获取选中设备
     */
    IDevice getSelectedDevice();

    /**
     * 设置选中设备
     */
    void setSelectedDevice(IDevice selectedDevice);

    /**
     * 取消选中设备
     */
    void cleanSelectedDevice();

    /**
     * 监听投屏端 AVTransport 回调
     */
    void registerAVTransport(Context context);

    /**
     * 监听投屏端 RenderingControl 回调
     */
    void registerRenderingControl(Context context);

    /**
     * 销毁
     */
    void destroy();
}
