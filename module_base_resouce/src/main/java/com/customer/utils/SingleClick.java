package com.customer.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ Author  QinTian
 * @ Date  2020/8/20
 * @ Describe 防止View被连续点击
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SingleClick {

    long DEFAULT_INTERVAL_MILLIS = 1000;

    /**
     * @return 快速点击的间隔（ms），默认是1000ms
     */
    long value() default DEFAULT_INTERVAL_MILLIS;
}