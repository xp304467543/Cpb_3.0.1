package com.customer.component.panel.emotion;

import android.text.TextUtils;

import com.fh.basemodle.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 编码参考 http://www.oicqzone.com/tool/emoji/
 */
public class Emotions {

    public static Map<String, Integer> EMOTIONS = new LinkedHashMap<>();

    public static Map<String, Integer> EMOTIONS_ONE = new LinkedHashMap<>();

    static {
        EMOTIONS.put(":G1:", R.drawable.g1);
        EMOTIONS.put(":G2:",R.drawable.g2);
        EMOTIONS.put(":G3:",R.drawable.g3);
        EMOTIONS.put(":G4:",R.drawable.g4);
        EMOTIONS.put(":G5:",R.drawable.g5);
        EMOTIONS.put(":G6:",R.drawable.g6);
        EMOTIONS.put(":G7:",R.drawable.g7);
        EMOTIONS.put(":G8:",R.drawable.g8);
        EMOTIONS.put(":G9:",R.drawable.g9);
        EMOTIONS.put(":G10:",R.drawable.g10);
        EMOTIONS.put(":G11:",R.drawable.g11);
        EMOTIONS.put(":G12:",R.drawable.g12);
        EMOTIONS.put(":G13:",R.drawable.g13);
        EMOTIONS.put(":G14:",R.drawable.g14);
        EMOTIONS.put(":G15:",R.drawable.g15);
        EMOTIONS.put(":G16:",R.drawable.g16);
        EMOTIONS.put(":G17:",R.drawable.g17);
        EMOTIONS.put(":G18:",R.drawable.g18);
        EMOTIONS.put(":G19:",R.drawable.g19);
        EMOTIONS.put(":G20:",R.drawable.g20);
        EMOTIONS.put(":G21:",R.drawable.g21);
        EMOTIONS.put(":G22:",R.drawable.g22);
        EMOTIONS.put(":G23:",R.drawable.g23);
        EMOTIONS.put(":G24:",R.drawable.g24);
        EMOTIONS.put(":G25:",R.drawable.g25);
        EMOTIONS.put(":G26:",R.drawable.g26);
        EMOTIONS.put(":G27:",R.drawable.g27);
        EMOTIONS.put(":G28:",R.drawable.g28);
        EMOTIONS.put(":G29:",R.drawable.g29);
        EMOTIONS.put(":G30:",R.drawable.g30);
        EMOTIONS.put(":G31:",R.drawable.g31);
        EMOTIONS.put(":G32:",R.drawable.g32);
        EMOTIONS.put(":G33:",R.drawable.g33);
        EMOTIONS.put(":G34:",R.drawable.g34);
        EMOTIONS.put(":G35:",R.drawable.g35);
        EMOTIONS.put(":G36:",R.drawable.g36);
        EMOTIONS.put(":G37:",R.drawable.g37);
        EMOTIONS.put(":G38:",R.drawable.g38);


        EMOTIONS_ONE.put(":bowtie:", R.drawable.bowtie);
        EMOTIONS_ONE.put(":smiley:", R.drawable.smiley);
        EMOTIONS_ONE.put(":kissing_heart:", R.drawable.kissing_heart);
        EMOTIONS_ONE.put(":satisfied:", R.drawable.satisfied);
        EMOTIONS_ONE.put(":stuck_out_tongue_closed_eyes:", R.drawable.stuck_out_tongue_closed_eyes);
        EMOTIONS_ONE.put(":stuck_out_tongue:", R.drawable.stuck_out_tongue);

        EMOTIONS_ONE.put(":smile:", R.drawable.smile);
        EMOTIONS_ONE.put(":relaxed:", R.drawable.relaxed);
        EMOTIONS_ONE.put(":kissing_closed_eyes:", R.drawable.kissing_closed_eyes);
        EMOTIONS_ONE.put(":grin:", R.drawable.grin);
        EMOTIONS_ONE.put(":grinning:", R.drawable.grinning);
        EMOTIONS_ONE.put(":sleeping:", R.drawable.sleeping);

        EMOTIONS_ONE.put(":laughing:", R.drawable.laughing);
        EMOTIONS_ONE.put(":smirk:", R.drawable.smirk);
        EMOTIONS_ONE.put(":flushed:", R.drawable.flushed);
        EMOTIONS_ONE.put(":wink:", R.drawable.wink);
        EMOTIONS_ONE.put(":kissing:", R.drawable.kissing);
        EMOTIONS_ONE.put(":worried:", R.drawable.worried);

        EMOTIONS_ONE.put(":blush:", R.drawable.blush);
        EMOTIONS_ONE.put(":heart_eyes:", R.drawable.heart_eyes);
        EMOTIONS_ONE.put(":relieved:", R.drawable.relieved);
        EMOTIONS_ONE.put(":stuck_out_tongue_winking_eye:", R.drawable.stuck_out_tongue_winking_eye);
        EMOTIONS_ONE.put(":kissing_smiling_eyes:", R.drawable.kissing_smiling_eyes);
        EMOTIONS_ONE.put(":frowning:", R.drawable.frowning);
    }

    private static String emotionCode2String(int code) {
        return new String(Character.toChars(code));
    }

    public static int getDrawableResByName(String emotionName) {
        if (!TextUtils.isEmpty(emotionName) && EMOTIONS.containsKey(emotionName)) {
          return EMOTIONS.get(emotionName);
        }
        if (!TextUtils.isEmpty(emotionName) && EMOTIONS_ONE.containsKey(emotionName)) {
            return EMOTIONS_ONE.get(emotionName);
        }
        return -1;
    }

    public static List<Emotion> getEmotions() {
        List<Emotion> emotions = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : EMOTIONS.entrySet()) {
            emotions.add(new Emotion(entry.getKey(), entry.getValue()));
        }
        return emotions;
    }

    public static List<Emotion> getEmotionsFh() {
        List<Emotion> emotions = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : EMOTIONS_ONE.entrySet()) {
            emotions.add(new Emotion(entry.getKey(), entry.getValue()));
        }
        return emotions;
    }
}
