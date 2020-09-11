package com.customer.player.video.pip;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.customer.player.LiveGestureVideoController;
import com.player.customize.ui.videocontroller.component.CompleteView;
import com.player.customize.ui.videocontroller.component.ErrorView;

/**
 * @ Author  QinTian
 * @ Date  2020/8/28
 * @ Describe 悬浮播放控制器
 */
public class FloatController extends LiveGestureVideoController {

    public FloatController(@NonNull Context context) {
        super(context);
    }

    public FloatController(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void initView() {
        super.initView();
        addControlComponent(new CompleteView(getContext()));
        addControlComponent(new ErrorView(getContext()));
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }
}
