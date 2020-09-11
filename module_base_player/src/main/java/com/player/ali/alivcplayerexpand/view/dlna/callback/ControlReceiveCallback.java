package com.player.ali.alivcplayerexpand.view.dlna.callback;


import com.player.ali.alivcplayerexpand.view.dlna.domain.IResponse;

/**
 * 手机端接收投屏端信息回调
 */

public interface ControlReceiveCallback extends ControlCallback{

    void receive(IResponse response);
}
