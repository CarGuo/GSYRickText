/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.shuyu.textutillib.utils;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.Spannable.Factory;
import android.text.SpannableStringBuilder;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;

import com.shuyu.textutillib.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 表情工具类
 */
public class SmileUtils {
    public static final String e1 = "[e1]";
    public static final String e2 = "[e2]";
    public static final String e3 = "[e3]";
    public static final String e4 = "[e4]";
    public static final String e5 = "[e5]";
    public static final String e6 = "[e6]";
    public static final String e7 = "[e7]";
    public static final String e8 = "[e8]";
    public static final String e9 = "[e9]";
    public static final String e10 = "[e10]";
    public static final String e11 = "[e11]";
    public static final String e12 = "[e12]";
    public static final String e13 = "[e13]";
    public static final String e14 = "[e14]";
    public static final String e15 = "[e15]";
    public static final String e16 = "[e16]";
    public static final String e17 = "[e17]";
    public static final String e18 = "[e18]";
    public static final String e19 = "[e19]";
    public static final String e20 = "[e20]";
    public static final String e21 = "[e21]";
    public static final String e22 = "[e22]";
    public static final String e23 = "[e23]";
    public static final String e24 = "[e24]";
    public static final String e25 = "[e25]";
    public static final String e26 = "[e26]";
    public static final String e27 = "[e27]";
    public static final String e28 = "[e28]";
    public static final String e29 = "[e29]";
    public static final String e30 = "[e30]";
    public static final String e31 = "[e31]";
    public static final String e32 = "[e32]";
    public static final String e33 = "[e33]";
    public static final String e34 = "[e34]";
    public static final String e35 = "[e35]";
    public static final String e36 = "[e36]";
    public static final String e37 = "[e37]";
    public static final String e38 = "[e38]";
    public static final String e39 = "[e39]";
    public static final String e40 = "[e40]";
    public static final String e41 = "[e41]";
    public static final String e42 = "[e42]";
    public static final String e43 = "[e43]";
    public static final String e44 = "[e44]";
    public static final String e45 = "[e45]";
    public static final String e46 = "[e46]";
    public static final String e47 = "[e47]";
    public static final String e48 = "[e48]";
    public static final String e49 = "[e49]";
    public static final String e50 = "[e50]";
    public static final String e51 = "[e51]";
    public static final String e52 = "[e52]";
    public static final String e53 = "[e53]";
    public static final String e54 = "[e54]";
    public static final String e55 = "[e55]";
    public static final String e56 = "[e56]";
    public static final String e57 = "[e57]";
    public static final String e58 = "[e58]";
    public static final String e59 = "[e59]";
    public static final String e60 = "[e60]";
    public static final String e61 = "[e61]";
    public static final String e62 = "[e62]";
    public static final String e63 = "[e63]";

    private static final Factory spannableFactory = Factory
            .getInstance();

    private static final Map<Pattern, Integer> emoticons = new HashMap<Pattern, Integer>();

    public Map<Pattern, Integer> getEmotions() {
        return emoticons;
    }

    static {

        // 如果考虑国际化 addPattern(emoticons, ee_1[index], R.drawable.ee_1);
        addPattern(emoticons, e1, R.drawable.e1);
        addPattern(emoticons, e2, R.drawable.e2);
        addPattern(emoticons, e3, R.drawable.e3);
        addPattern(emoticons, e4, R.drawable.e4);
        addPattern(emoticons, e5, R.drawable.e5);
        addPattern(emoticons, e6, R.drawable.e6);
        addPattern(emoticons, e7, R.drawable.e7);
        addPattern(emoticons, e8, R.drawable.e8);
        addPattern(emoticons, e9, R.drawable.e9);
        addPattern(emoticons, e10, R.drawable.e10);
        addPattern(emoticons, e11, R.drawable.e11);
        addPattern(emoticons, e12, R.drawable.e12);
        addPattern(emoticons, e13, R.drawable.e13);
        addPattern(emoticons, e14, R.drawable.e14);
        addPattern(emoticons, e15, R.drawable.e15);
        addPattern(emoticons, e16, R.drawable.e16);
        addPattern(emoticons, e17, R.drawable.e17);
        addPattern(emoticons, e18, R.drawable.e18);
        addPattern(emoticons, e19, R.drawable.e19);
        addPattern(emoticons, e20, R.drawable.e20);
        addPattern(emoticons, e21, R.drawable.e21);
        addPattern(emoticons, e22, R.drawable.e22);
        addPattern(emoticons, e23, R.drawable.e23);
        addPattern(emoticons, e24, R.drawable.e24);
        addPattern(emoticons, e25, R.drawable.e25);
        addPattern(emoticons, e26, R.drawable.e26);
        addPattern(emoticons, e27, R.drawable.e27);
        addPattern(emoticons, e28, R.drawable.e28);
        addPattern(emoticons, e29, R.drawable.e29);
        addPattern(emoticons, e30, R.drawable.e30);
        addPattern(emoticons, e31, R.drawable.e31);
        addPattern(emoticons, e32, R.drawable.e32);
        addPattern(emoticons, e33, R.drawable.e33);
        addPattern(emoticons, e34, R.drawable.e34);
        addPattern(emoticons, e35, R.drawable.e35);
        addPattern(emoticons, e36, R.drawable.e36);
        addPattern(emoticons, e37, R.drawable.e37);
        addPattern(emoticons, e38, R.drawable.e38);
        addPattern(emoticons, e39, R.drawable.e39);
        addPattern(emoticons, e40, R.drawable.e40);
        addPattern(emoticons, e41, R.drawable.e41);
        addPattern(emoticons, e42, R.drawable.e42);
        addPattern(emoticons, e43, R.drawable.e43);
        addPattern(emoticons, e44, R.drawable.e44);
        addPattern(emoticons, e45, R.drawable.e45);
        addPattern(emoticons, e46, R.drawable.e46);
        addPattern(emoticons, e47, R.drawable.e47);
        addPattern(emoticons, e48, R.drawable.e48);
        addPattern(emoticons, e49, R.drawable.e49);
        addPattern(emoticons, e50, R.drawable.e50);
        addPattern(emoticons, e51, R.drawable.e51);
        addPattern(emoticons, e52, R.drawable.e52);
        addPattern(emoticons, e53, R.drawable.e53);
        addPattern(emoticons, e54, R.drawable.e54);
        addPattern(emoticons, e55, R.drawable.e55);
        addPattern(emoticons, e56, R.drawable.e56);
        addPattern(emoticons, e57, R.drawable.e57);
        addPattern(emoticons, e58, R.drawable.e58);
        addPattern(emoticons, e59, R.drawable.e59);
        addPattern(emoticons, e60, R.drawable.e60);
        addPattern(emoticons, e61, R.drawable.e61);
        addPattern(emoticons, e62, R.drawable.e62);
        addPattern(emoticons, e63, R.drawable.e63);

    }

    private static void addPattern(Map<Pattern, Integer> map, String smile,
                                   int resource) {
        map.put(Pattern.compile(Pattern.quote(smile)), resource);
    }

    /**
     * replace existing spannable with smiles
     *
     * @param context
     * @param spannable
     * @return
     */
    public static boolean addSmiles(Context context, Spannable spannable) {
        boolean hasChanges = false;
        for (Entry<Pattern, Integer> entry : emoticons.entrySet()) {
            Matcher matcher = entry.getKey().matcher(spannable);
            while (matcher.find()) {
                boolean set = true;
                for (ImageSpan span : spannable.getSpans(matcher.start(),
                        matcher.end(), ImageSpan.class))
                    if (spannable.getSpanStart(span) >= matcher.start()
                            && spannable.getSpanEnd(span) <= matcher.end())
                        spannable.removeSpan(span);
                    else {
                        set = false;
                        break;
                    }
                if (set) {
                    hasChanges = true;
                    spannable.setSpan(new ImageSpan(context, entry.getValue()),
                            matcher.start(), matcher.end(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }
        return hasChanges;
    }

    public static Spannable getSmiledText(Context context, CharSequence text) {
        Spannable spannable = spannableFactory.newSpannable(text);
        addSmiles(context, spannable);
        return spannable;
    }

    public static boolean containsKey(String key) {
        boolean b = false;
        for (Entry<Pattern, Integer> entry : emoticons.entrySet()) {
            Matcher matcher = entry.getKey().matcher(key);
            if (matcher.find()) {
                b = true;
                break;
            }
        }

        return b;
    }

    public static Map<Pattern, Integer> getEmoticons() {
        return emoticons;
    }

    public static String stringToUnicode(String string) {

        StringBuffer unicode = new StringBuffer();

        for (int i = 0; i < string.length(); i++) {

            // 取出每一个字符
            char c = string.charAt(i);

            // 转换为unicode
            unicode.append("\\u" + String.format("%04", Integer.toHexString(c)));
        }

        return "[" + unicode.toString() + "]";
    }

    public static String unicode2String(String unicode) {

        StringBuffer string = new StringBuffer();

        String[] hex = unicode.split("\\\\u");

        for (int i = 1; i < hex.length; i++) {

            // 转换出每一个代码点
            int data = Integer.parseInt(hex[i], 16);

            // 追加成string
            string.append((char) data);
        }

        return string.toString();
    }

    public static String[] specials = {"\\", "\\/", "*", ".", "?", "+", "$",
            "^", "[", "]", "(", ")", "{", "}", "|"};

    public static SpannableStringBuilder highlight(String text, String target) {
        SpannableStringBuilder spannable = new SpannableStringBuilder(text);
        CharacterStyle span = null;
        for (int i = 0; i < specials.length; i++) {
            if (target.contains(specials[i])) {
                target = target.replace(specials[i], "\\" + specials[i]);
            }
        }
        Pattern p = Pattern.compile(target.toLowerCase());
        Matcher m = p.matcher(text.toLowerCase());
        while (m.find()) {
            span = new ForegroundColorSpan(Color.rgb(253, 113, 34));// 需要重复！
            spannable.setSpan(span, m.start(), m.end(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannable;
    }

    public static SpannableStringBuilder highlight(Spannable text, String target) {
        SpannableStringBuilder spannable = new SpannableStringBuilder(text);
        CharacterStyle span = null;
        Pattern p = Pattern.compile(target);
        Matcher m = p.matcher(text);
        while (m.find()) {
            span = new ForegroundColorSpan(Color.rgb(253, 113, 34));// 需要重复！
            spannable.setSpan(span, m.start(), m.end(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannable;
    }

    public static SpannableStringBuilder highlight(String text) {
        SpannableStringBuilder spannable = new SpannableStringBuilder(text);
        CharacterStyle span = null;
        span = new ForegroundColorSpan(Color.rgb(253, 113, 34));// 需要重复！
        spannable.setSpan(span, 0, text.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    public static Spannable unicodeToEmojiName(Context context, String content) {
        Spannable spannable = getSmiledText(context, content);
        return spannable;
    }
}
