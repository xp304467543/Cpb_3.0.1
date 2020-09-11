package com.customer.component.panel.emotion;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;

import androidx.core.content.ContextCompat;


import com.lib.basiclib.utils.ViewUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class EmotionSpanBuilder {

    private static Pattern sPatternEmotion = Pattern.compile("\\[([\u4e00-\u9fa5\\w])+\\]|[\\ud83c\\udc00-\\ud83c\\udfff]|[\\ud83d\\udc00-\\ud83d\\udfff]|[\\u2600-\\u27ff]|\\:(.*?)\\:");

    public static Spannable buildEmotionSpannable(Context context, String text) {
        Matcher matcherEmotion = sPatternEmotion.matcher(text);
        SpannableString spannableString = new SpannableString(text);
        while (matcherEmotion.find()) {
            String key = matcherEmotion.group();
            int imgRes = Emotions.getDrawableResByName(key);
            if (imgRes != -1) {
                int start = matcherEmotion.start();
                Drawable drawable = ContextCompat.getDrawable(context, imgRes);
                drawable.setBounds(0, 0, ViewUtils.INSTANCE.dp2px(15), ViewUtils.INSTANCE.dp2px(15));
                CenterImageSpan span = new CenterImageSpan(drawable);
                spannableString.setSpan(span, start, start + key.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        return spannableString;
    }

}
