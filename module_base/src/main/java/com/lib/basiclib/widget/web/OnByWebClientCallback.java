package com.lib.basiclib.widget.web;

import android.graphics.Bitmap;

import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.WebView;


/**
 * Created by jingbin on 2020/6/30.
 */
public abstract class OnByWebClientCallback {

    public void onPageStarted(WebView view, String url, Bitmap favicon) {

    }

    public void onPageFinished(WebView view, String url) {

    }

    public boolean isOpenThirdApp(String url) {
        return !url.startsWith("http:") && !url.startsWith("https:");
    }

    /**
     * @return true 表示是自己处理的
     */
    public boolean onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        return false;
    }
}
