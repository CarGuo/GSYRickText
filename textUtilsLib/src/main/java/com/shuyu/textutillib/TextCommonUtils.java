package com.shuyu.textutillib;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.widget.EditText;
import android.widget.TextView;


import com.shuyu.textutillib.listener.SpanAtUserCallBack;
import com.shuyu.textutillib.listener.SpanUrlCallBack;
import com.shuyu.textutillib.model.UserModel;
import com.shuyu.textutillib.span.ClickAtUserSpan;
import com.shuyu.textutillib.span.LinkSpan;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by shuyu on 2016/11/10.
 */

public class TextCommonUtils {
    /**
     * 单纯emoji表示
     *
     * @param context 上下文
     * @param text    包含emoji的字符串
     * @param tv      显示的textview
     */
    public static void setEmojiText(Context context, String text, TextView tv) {
        if (TextUtils.isEmpty(text)) {
            tv.setText("");
        }
        Spannable spannable = SmileUtils.unicodeToEmojiName(context, text);
        tv.setText(spannable);
    }

    /**
     * 单纯获取emoji表示
     *
     * @param context 上下文
     * @param text    需要处理的文本
     * @return 返回显示的spananle
     */
    public static Spannable getEmojiText(Context context, String text) {
        if (TextUtils.isEmpty(text)) {
            return new SpannableString("");
        }
        return SmileUtils.unicodeToEmojiName(context, text);

    }


    /**
     * 显示emoji和url高亮
     * @param context            上下文
     * @param text               需要处理的文本
     * @param textView           需要显示的view
     * @param color              需要显示的颜色
     * @param spanAtUserCallBack AT某人点击的返回
     * @param spanUrlCallBack    链接点击的返回
     * @return 返回显示的spananle
     */
    public static Spannable getUrlEmojiText(Context context, String text, TextView textView, int color, SpanAtUserCallBack spanAtUserCallBack, SpanUrlCallBack spanUrlCallBack) {
        if (!TextUtils.isEmpty(text)) {
            return getUrlSmileText(context, text, null, textView, color, spanAtUserCallBack, spanUrlCallBack);
        } else {
            return new SpannableString(" ");
        }
    }

    /**
     * 设置带高亮可点击的Url和表情的textview文本
     *
     * @param context            上下文
     * @param string             需要处理的文本
     * @param listUser           需要显示的AT某人
     * @param textView           需要显示的view
     * @param color              需要显示的颜色
     * @param spanAtUserCallBack AT某人点击的返回
     * @param spanUrlCallBack    链接点击的返回
     */
    public static void setUrlSmileText(Context context, String string, List<UserModel> listUser, TextView textView, int color, SpanAtUserCallBack spanAtUserCallBack, SpanUrlCallBack spanUrlCallBack) {
        Spannable spannable = getUrlSmileText(context, string, listUser, textView, color, spanAtUserCallBack, spanUrlCallBack);
        textView.setText(spannable);
    }

    /**
     * AT某人的跳转
     *
     * @param context            上下文
     * @param listUser           需要显示的AT某人
     * @param content            需要处理的文本
     * @param textView           需要显示的view
     * @param clickable          AT某人是否可以点击
     * @param color              需要显示的颜色
     * @param spanAtUserCallBack AT某人点击的返回
     * @return 返回显示的spananle
     */
    public static Spannable getAtText(Context context, List<UserModel> listUser, String content, TextView textView, boolean clickable,
                                      int color, SpanAtUserCallBack spanAtUserCallBack) {
        if (listUser == null || listUser.size() <= 0)
            return getEmojiText(context, content);
        Spannable spannableString = new SpannableString(content);
        int indexStart = 0;
        int lenght = content.length();
        boolean hadHighLine = false;
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < listUser.size(); i++) {
            int index = content.indexOf(listUser.get(i).getUser_name(), indexStart);
            if (index < 0 && indexStart > 0) {
                index = content.indexOf(listUser.get(i).getUser_name());
                if (map.containsKey("" + index)) {
                    int tmpIndexStart = (indexStart < lenght) ? Integer.parseInt(map.get("" + index)) : lenght - 1;
                    if (tmpIndexStart != indexStart) {
                        indexStart = tmpIndexStart;
                        i--;
                        continue;
                    }
                }
            }
            if (index > 0) {
                map.put(index + "", index + "");
                int mathStart = index - 1;
                int indexEnd = index + listUser.get(i).getUser_name().length();
                boolean hadAt = "@".equals(content.substring(mathStart, index));
                int matchEnd = indexEnd + 1;
                if (hadAt && (matchEnd <= lenght || indexEnd == lenght)) {
                    if ((indexEnd == lenght) || " ".equals(content.substring(indexEnd, indexEnd + 1)) || "\b".equals(content.substring(indexEnd, indexEnd + 1))) {
                        if (indexEnd > indexStart) {
                            indexStart = indexEnd;
                        }
                        hadHighLine = true;
                        spannableString.setSpan(new ClickAtUserSpan(context, listUser.get(i), color, spanAtUserCallBack), mathStart, (indexEnd == lenght) ? lenght : matchEnd, Spanned.SPAN_MARK_POINT);

                    }
                }
            }
        }
        SmileUtils.addSmiles(context, spannableString);
        if (!(textView instanceof EditText) && clickable && hadHighLine)
            textView.setMovementMethod(LinkMovementMethod.getInstance());
        return spannableString;
    }


    /**
     * 设置带高亮可点击的Url和表情的textview文本
     *
     * @param context            上下文
     * @param string             需要处理的文本
     * @param listUser           需要显示的AT某人
     * @param textView           需要显示的view
     * @param color              需要显示的颜色
     * @param spanAtUserCallBack AT某人点击的返回
     * @param spanUrlCallBack    链接点击的返回
     * @return 返回显示的spananle
     */
    public static Spannable getUrlSmileText(Context context, String string, List<UserModel> listUser, TextView textView, int color, SpanAtUserCallBack spanAtUserCallBack, SpanUrlCallBack spanUrlCallBack) {
        textView.setAutoLinkMask(Linkify.WEB_URLS | Linkify.PHONE_NUMBERS);
        if (!TextUtils.isEmpty(string)) {
            string = string.replaceAll("\r", "\r\n");
            Spannable spannable = getAtText(context, listUser, string, textView, true, color, spanAtUserCallBack);
            textView.setText(spannable);
            return resolveUrlLogic(context, textView, spannable, color, spanUrlCallBack);
        } else {
            return new SpannableString(" ");
        }
    }

    /**
     * 处理带URL的逻辑
     *
     * @param context         上下文
     * @param textView        需要显示的view
     * @param spannable       显示的spananle
     * @param color           需要显示的颜色
     * @param spanUrlCallBack 链接点击的返回
     * @return 返回显示的spananle
     */
    private static Spannable resolveUrlLogic(Context context, TextView textView, Spannable spannable, int color, SpanUrlCallBack spanUrlCallBack) {
        CharSequence charSequence = textView.getText();
        if (charSequence instanceof Spannable) {
            int end = charSequence.length();
            Spannable sp = (Spannable) textView.getText();
            URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);
            ClickAtUserSpan[] atSpan = sp.getSpans(0, end, ClickAtUserSpan.class);
            if (urls.length > 0) {
                SpannableStringBuilder style = new SpannableStringBuilder(charSequence);
                style.clearSpans();// should clear old spans
                for (URLSpan url : urls) {
                    String urlString = url.getURL();
                    if (isNumeric(urlString.replace("tel:", ""))) {
                        style.setSpan(new StyleSpan(Typeface.NORMAL), sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                    } else if (isTopURL(urlString.toLowerCase())) {
                        LinkSpan linkSpan = new LinkSpan(context, url.getURL(), color, spanUrlCallBack);
                        style.setSpan(linkSpan, sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                    } else {
                        style.setSpan(new StyleSpan(Typeface.NORMAL), sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                    }

                }
                for (ClickAtUserSpan atUserSpan : atSpan) {
                    style.setSpan(atUserSpan, sp.getSpanStart(atUserSpan), sp.getSpanEnd(atUserSpan), Spanned.SPAN_MARK_POINT);
                }
                SmileUtils.addSmiles(context, style);
                textView.setAutoLinkMask(0);
                return style;
            } else {
                return spannable;
            }
        } else {
            return spannable;
        }
    }


    /**
     * 顶级域名判断；如果要忽略大小写，可以直接在传入参数的时候toLowerCase()再做判断
     * 处理1. 2. 3.识别成链接的问题
     *
     * @param str
     * @return 是否符合url
     */
    public static boolean isTopURL(String str) {
        String ss[] = str.split("\\.");
        if (ss.length < 3)
            return false;

        return true;

    }

    /**
     * 是否数字
     *
     * @param str
     * @return 是否数字
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }


}
