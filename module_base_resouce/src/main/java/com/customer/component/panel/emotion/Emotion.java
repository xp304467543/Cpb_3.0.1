package com.customer.component.panel.emotion;

import androidx.annotation.DrawableRes;

/**
 * @ Author  QinTian
 * @ Date  2020/8/22
 * @ Describe
 */
public class Emotion {

    public String text;

    @DrawableRes
    public int drawableRes;

    public Emotion(String text, @DrawableRes int drawableRes) {
        this.text = text;
        this.drawableRes = drawableRes;
    }
}