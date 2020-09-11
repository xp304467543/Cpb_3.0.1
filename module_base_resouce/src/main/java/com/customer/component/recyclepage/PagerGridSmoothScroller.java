package com.customer.component.recyclepage;

import android.util.DisplayMetrics;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @ Author  QinTian
 * @ Date  2020/8/12
 * @ Describe
 */
public class PagerGridSmoothScroller extends LinearSmoothScroller {
    private RecyclerView mRecyclerView;

    private  float sMillisecondsPreInch = 60f;    // 每一个英寸滚动需要的微秒数，数值越大，速度越慢

    public PagerGridSmoothScroller(@NonNull RecyclerView recyclerView) {
        super(recyclerView.getContext());
        mRecyclerView = recyclerView;
    }

    @Override
    protected void onTargetFound(View targetView, RecyclerView.State state, Action action) {
        RecyclerView.LayoutManager manager = mRecyclerView.getLayoutManager();
        if (null == manager) return;
        if (manager instanceof PagerGridLayoutManager) {
            PagerGridLayoutManager layoutManager = (PagerGridLayoutManager) manager;
            int pos = mRecyclerView.getChildAdapterPosition(targetView);
            int[] snapDistances = layoutManager.getSnapOffset(pos);
            final int dx = snapDistances[0];
            final int dy = snapDistances[1];
            final int time = calculateTimeForScrolling(Math.max(Math.abs(dx), Math.abs(dy)));
            if (time > 0) {
                action.update(dx, dy, time, mDecelerateInterpolator);
            }
        }
    }

    @Override
    protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
        return sMillisecondsPreInch / displayMetrics.densityDpi;
    }
}