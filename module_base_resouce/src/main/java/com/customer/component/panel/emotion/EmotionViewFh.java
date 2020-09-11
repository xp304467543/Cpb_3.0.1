package com.customer.component.panel.emotion;

import android.content.Context;
import android.text.Editable;
import android.text.Spannable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

import com.fh.basemodle.R;
import com.lib.basiclib.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @ Author  QinTian
 * @ Date  2020/8/22
 * @ Describe
 */
public class EmotionViewFh extends GridView {

    private static int sNumColumns = 0;
    private static int sNumRows = 0;
    private static int sPadding = 0;
    private static int sEmotionSize = 0;
    private EditText mEditText;

    public static int calSizeForContainEmotion(Context context, int width, int height) {
        sPadding = ViewUtils.INSTANCE.dp2px(5);
        sEmotionSize =  ViewUtils.INSTANCE.dp2px(50);
        sNumColumns = width / sEmotionSize;
        sNumRows = height / sEmotionSize;
        return sNumColumns * sNumRows;
    }

    public EmotionViewFh(Context context, EditText editText) {
        super(context);
        this.mEditText = editText;
    }

    public void buildEmotions(final List<Emotion> data) {
        setNumColumns(sNumColumns);
        setPadding(sPadding, sPadding, sPadding, sPadding);
        setClipToPadding(false);
        setAdapter(new EmotionAdapter(getContext(), data));
        setOnItemClickListener((parent, view, position, id) -> {
            Emotion emotion = data.get(position);
            int start = mEditText.getSelectionStart();
            Editable editable = mEditText.getEditableText();
            Spannable emotionSpannable = EmotionSpanBuilder.buildEmotionSpannable(getContext(), emotion.text);
            editable.insert(start, emotionSpannable);
        });
    }

    public static class EmotionAdapter extends BaseAdapter {

        public List<Emotion> mEmotions;
        private Context mContext;

        public EmotionAdapter(Context context, List<Emotion> emotions) {
            if (emotions == null) {
                emotions = new ArrayList<>();
            }
            mEmotions = emotions;
            mContext = context;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if (view == null) {
                view = LayoutInflater.from(mContext).inflate(R.layout.vh_emotion_item_layout, parent, false);
            }
            ImageView imageView = view.findViewById(R.id.image);
            imageView.setImageResource(((Emotion) getItem(position)).drawableRes);
            return view;
        }

        @Override
        public int getCount() {
            return mEmotions.size();
        }

        @Override
        public Object getItem(int position) {
            return mEmotions.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

    }
}
