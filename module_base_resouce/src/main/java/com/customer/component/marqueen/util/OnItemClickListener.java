package com.customer.component.marqueen.util;

import android.view.View;


public interface OnItemClickListener<V extends View, E> {
    void onItemClickListener(V mView, E mData, int mPosition);
}
