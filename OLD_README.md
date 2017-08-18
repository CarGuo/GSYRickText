----------------------------------
## 1.0.6
* fix bug

## 1.0.5
* 增加新功能，@某人在选择的时候只能块选中，完美实现微博输入框效果


## 1.0.4
* 最低API调到15


## 1.0.3

* 修正了电话号码不能显示点击问题，增加了needNum接口用于显示电话号码
* 修改了demo里插入文本的更新方式


这是效果图片，GIF看起来有些不流畅

<img src="https://github.com/CarGuo/RickText/blob/master/device-2016-11-10-220253.mp4_1478787046.gif" width="240px" height="426px"/>

* <h4>实现了类似微博的编辑输入，可输入表情和@某人，优化了@某人，光标不能插入到@人的名字中间，@某人整个清除，整块文本插入处理等</h4>
* <h4>TextView支持显示表情，@某人和URL高亮可点击。</h4>


## 新修改 SmileUtils 表情处理类，支持外部自己添加表情
```
1、
//这家自己的自定义表情
List<Integer> data = new ArrayList<>();//对应本地图片资源
List<String> strings = new ArrayList<>();//对应图片资源对应名字
for (int i = 1; i < 64; i++) {
    int resId = getResources().getIdentifier("e" + i, "drawable", getPackageName());
    data.add(resId);
    strings.add("[e" + i + "]");
}
/**初始化为自己的**/
SmileUtils.addPatternAll(SmileUtils.getEmoticons(), strings, data);
···需要在显示布局之前就设置好

2、
根据文本获取对应的表情去显示再grid里面
int resId = SmileUtils.getRedId(filename);

！！注意目前删除的图片资源必须是 delete_expression.png

3、
/**往编辑框出入表情*/
/**
 * 文本转化表情处理
 *
 * @param editText  要显示的EditText
 * @param maxLength 最长高度
 * @param size      显示大小
 * @param name      需要转化的文本
 */
SmileUtils.insertIcon(editTextEmoji, 2000, ScreenUtils.dip2px(getContext(), 20), filename);


```


## EditTextAtUtils @某人的逻辑处理类

可以new一个EditTextAtUtils对象
```
/**
 * @param editText        需要对应显示的edittext
 * @param contactNameList 传入一个不变的list维护用户名
 * @param contactIdList   传入一个不变的list维护id
 */
public EditTextAtUtils(EditText editText, final List<String> contactNameList, final List<String> contactIdList)

编辑框输入了@后的跳转，跳转到好友选择之类的页面
public void setEditTextAtUtilJumpListener(EditTextAtUtilJumpListener editTextAtUtilJumpListener) {
```

## TextCommonUtils 文本逻辑处理类,显示表情，@某人和URL高亮可点击。

```
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
 *
 * @param context            上下文
 * @param text               需要处理的文本
 * @param textView           需要显示的view
 * @param color              需要显示的颜色
 * @param needNum            是否需要显示号码
 * @param spanAtUserCallBack AT某人点击的返回
 * @param spanUrlCallBack    链接点击的返回
 * @return 返回显示的spananle
 */
public static Spannable getUrlEmojiText(Context context, String text, TextView textView, int color, boolean needNum, SpanAtUserCallBack spanAtUserCallBack, SpanUrlCallBack spanUrlCallBack) {
    if (!TextUtils.isEmpty(text)) {
        return getUrlSmileText(context, text, null, textView, color, needNum, spanAtUserCallBack, spanUrlCallBack);
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
 * @param needNum            是否需要显示号码
 * @param spanAtUserCallBack AT某人点击的返回
 * @param spanUrlCallBack    链接点击的返回
 */
public static void setUrlSmileText(Context context, String string, List<UserModel> listUser, TextView textView, int color, boolean needNum, SpanAtUserCallBack spanAtUserCallBack, SpanUrlCallBack spanUrlCallBack) {
    Spannable spannable = getUrlSmileText(context, string, listUser, textView, color, needNum, spanAtUserCallBack, spanUrlCallBack);
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
 * @param needNum            是否需要显示号码
 * @param spanAtUserCallBack AT某人点击的返回
 * @return 返回显示的spananle
 */
public static Spannable getAtText(Context context, List<UserModel> listUser, String content, TextView textView, boolean clickable,
                                  int color, boolean needNum, SpanAtUserCallBack spanAtUserCallBack) {
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
 * @param needNum            是否需要显示号码
 * @param spanAtUserCallBack AT某人点击的返回
 * @param spanUrlCallBack    链接点击的返回
 * @return 返回显示的spananle
 */
public static Spannable getUrlSmileText(Context context, String string, List<UserModel> listUser, TextView textView, int color, boolean needNum, SpanAtUserCallBack spanAtUserCallBack, SpanUrlCallBack spanUrlCallBack) {
    textView.setAutoLinkMask(Linkify.WEB_URLS | Linkify.PHONE_NUMBERS);
    if (!TextUtils.isEmpty(string)) {
        string = string.replaceAll("\r", "\r\n");
        Spannable spannable = getAtText(context, listUser, string, textView, true, color, needNum, spanAtUserCallBack);
        textView.setText(spannable);
        return resolveUrlLogic(context, textView, spannable, color, needNum, spanUrlCallBack);
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
 * @param needNum         是否需要显示号码
 * @param spanUrlCallBack 链接点击的返回
 * @return 返回显示的spananle
 */
private static Spannable resolveUrlLogic(Context context, TextView textView, Spannable spannable, int color, boolean needNum, SpanUrlCallBack spanUrlCallBack) {
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
                    if (!needNum && !isMobileSimple(urlString.replace("tel:", ""))) {
                        style.setSpan(new StyleSpan(Typeface.NORMAL), sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                    } else {
                        LinkSpan linkSpan = new LinkSpan(context, url.getURL(), color, spanUrlCallBack);
                        style.setSpan(linkSpan, sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                    }
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

/**
 * @param string 待验证文本
 * @return 是否符合手机号（简单）格式
 */
public static boolean isMobileSimple(String string) {
    String phone = "^[1]\\d{10}$";
    return !TextUtils.isEmpty(string) && Pattern.matches(phone, string);
}


```