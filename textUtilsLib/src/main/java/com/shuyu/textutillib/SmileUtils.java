package com.shuyu.textutillib;

/**
 * 表情工具类
 * Created by shuyu on 2016/11/14.
 */

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.widget.EditText;


import com.shuyu.textutillib.span.CenteredImageSpan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.text.style.DynamicDrawableSpan.ALIGN_BOTTOM;

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

    /***
     * 文本对应的资源
     *
     * @param string 需要转化文本
     * @return
     */
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
     * 文本转化表情处理
     *
     * @param editText  要显示的EditText
     * @param maxLength 最长高度
     * @param size      显示大小
     * @param name      需要转化的文本
     */
    public static void insertIcon(EditText editText, int maxLength, int size, String name) {

        String curString = editText.toString();
        if ((curString.length() + name.length()) > maxLength) {
            return;
        }

        int resId = SmileUtils.getRedId(name);

        Drawable drawable = editText.getResources().getDrawable(resId);
        if (drawable == null)
            return;

        drawable.setBounds(0, 0, size, size);//这里设置图片的大小
        CenteredImageSpan CenteredImageSpan = new CenteredImageSpan(drawable);
        SpannableString spannableString = new SpannableString(name);
        spannableString.setSpan(CenteredImageSpan, 0, spannableString.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);


        int index = Math.max(editText.getSelectionStart(), 0);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(editText.getText());
        spannableStringBuilder.insert(index, spannableString);

        editText.setText(spannableStringBuilder);
        editText.setSelection(index + spannableString.length());
    }


    /**
     * replace existing spannable with smiles
     *
     * @param context   上下文
     * @param spannable 显示的span
     * @return 是否添加
     */
    public static boolean addSmiles(Context context, Spannable spannable) {
        return addSmiles(context, -1, spannable);
    }

    /**
     * replace existing spannable with smiles
     *
     * @param context   上下文
     * @param size      大小
     * @param spannable 显示的span
     * @return 是否添加
     */
    public static boolean addSmiles(Context context, int size, Spannable spannable) {
        return addSmiles(context, size, ALIGN_BOTTOM, spannable);
    }


    /**
     * replace existing spannable with smiles
     *
     * @param context           上下文
     * @param size              大小
     * @param spannable         显示的span
     * @param verticalAlignment 垂直方向
     * @return 是否添加
     */
    public static boolean addSmiles(Context context, int size, int verticalAlignment, Spannable spannable) {
        boolean hasChanges = false;
        for (Map.Entry<Pattern, Integer> entry : emoticons.entrySet()) {
            Matcher matcher = entry.getKey().matcher(spannable);
            while (matcher.find()) {
                boolean set = true;
                for (CenteredImageSpan span : spannable.getSpans(matcher.start(),
                        matcher.end(), CenteredImageSpan.class))
                    if (spannable.getSpanStart(span) >= matcher.start()
                            && spannable.getSpanEnd(span) <= matcher.end())
                        spannable.removeSpan(span);
                    else {
                        set = false;
                        break;
                    }
                if (set) {
                    hasChanges = true;
                    if (size <= 0) {
                        spannable.setSpan(new CenteredImageSpan(context, entry.getValue(), verticalAlignment),
                                matcher.start(), matcher.end(),
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else {
                        Drawable drawable = context.getResources().getDrawable(entry.getValue());
                        if (drawable != null) {
                            drawable.setBounds(0, 0, size, size);//这里设置图片的大小
                            CenteredImageSpan CenteredImageSpan = new CenteredImageSpan(drawable, verticalAlignment);
                            spannable.setSpan(CenteredImageSpan,
                                    matcher.start(), matcher.end(),
                                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                    }
                }
            }
        }
        return hasChanges;
    }


    public static Spannable getSmiledText(Context context, CharSequence text) {
        return getSmiledText(context, text, -1);
    }

    public static Spannable getSmiledText(Context context, CharSequence text, int size) {
        return getSmiledText(context, text, size, ALIGN_BOTTOM);
    }

    public static Spannable getSmiledText(Context context, CharSequence text, int size, int verticalAlignment) {
        Spannable spannable = spannableFactory.newSpannable(text);
        addSmiles(context, size, verticalAlignment, spannable);
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

    public static Spannable unicodeToEmojiName(Context context, String content, int size, int verticalAlignment) {
        Spannable spannable = getSmiledText(context, content, size, verticalAlignment);
        return spannable;
    }

    public static Spannable unicodeToEmojiName(Context context, String content, int size) {
        Spannable spannable = getSmiledText(context, content, size);
        return spannable;
    }

    public static Spannable unicodeToEmojiName(Context context, String content) {
        Spannable spannable = getSmiledText(context, content, -1);
        return spannable;
    }

    public static List<String> getTextList() {
        return textList;
    }
}
