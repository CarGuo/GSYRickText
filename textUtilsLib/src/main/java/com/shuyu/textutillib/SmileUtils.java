package com.shuyu.textutillib;

/**
 * 表情工具类
 * Created by shuyu on 2016/11/14.
 */

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmileUtils {

    private static final Spannable.Factory spannableFactory = Spannable.Factory
            .getInstance();

    private static final Map<Pattern, Integer> emoticons = new HashMap<Pattern, Integer>();

    private static final List<String> textList = new ArrayList<>();

    public Map<Pattern, Integer> getEmotions() {
        return emoticons;
    }

    /**
     * 添加到map
     *
     * @param map      map
     * @param smile    文本
     * @param resource 显示图片表情列表
     */
    private static void addPattern(Map<Pattern, Integer> map, String smile,
                                   int resource) {
        map.put(Pattern.compile(Pattern.quote(smile)), resource);
    }

    /**
     * 使用你自己的map
     *
     * @param map      map
     * @param smile    文本列表
     * @param resource 显示图片表情列表
     */
    public static void addPatternAll(Map<Pattern, Integer> map, List<String> smile,
                                     List<Integer> resource) {

        map.clear();
        textList.clear();
        if (smile.size() != resource.size()) {
            try {
                throw new Exception("**********文本与图片list不相等");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        textList.addAll(smile);
        for (int i = 0; i < smile.size(); i++) {
            map.put(Pattern.compile(Pattern.quote(smile.get(i))), resource.get(i));
        }
    }


    public static int getRedId(String string) {
        for (Map.Entry<Pattern, Integer> entry : emoticons.entrySet()) {
            Matcher matcher = entry.getKey().matcher(string);
            while (matcher.find()) {
                return entry.getValue();
            }
        }
        return -1;
    }

    /**
     * replace existing spannable with smiles
     *
     * @param context   上下文
     * @param spannable 显示的span
     * @return 是否添加
     */
    public static boolean addSmiles(Context context, Spannable spannable) {
        boolean hasChanges = false;
        for (Map.Entry<Pattern, Integer> entry : emoticons.entrySet()) {
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
        for (Map.Entry<Pattern, Integer> entry : emoticons.entrySet()) {
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

    public static List<String> getTextList() {
        return textList;
    }
}
