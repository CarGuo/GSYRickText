<h4>支持类似微博的编辑框，可输入表情和@某人。支持TextView显示表情，链接，@某人，有点击效果</h4>
======================
```
<dependency>
  <groupId>com.shuyu</groupId>
  <artifactId>textutillib</artifactId>
  <version>1.0.2</version>
  <type>pom</type>
</dependency>
```

<h4>compile 'com.shuyu:textutillib:1.0.2'</h4>

<a href="http://www.jianshu.com/p/cd9e197a5c04">**** 这是简书入口，点我点我 ****</a>

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
/**x
 * 单纯emoji表示
 *
 * @param context
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
 * @param context
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
 * @param context
 * @param text               需要处理的文本
 * @param textView           需要显示的view
 * @param color              需要显示的颜色
 * @param spanAtUserCallBack @某人点击的返回
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
 * @param context
 * @param string             需要处理的文本
 * @param listUser           需要显示的@某人
 * @param textView           需要显示的view
 * @param color              需要显示的颜色
 * @param spanAtUserCallBack @某人点击的返回
 * @param spanUrlCallBack    链接点击的返回
 */
public static void setUrlSmileText(Context context, String string, List<UserModel> listUser, TextView textView, int color, SpanAtUserCallBack spanAtUserCallBack, SpanUrlCallBack spanUrlCallBack) {
    Spannable spannable = getUrlSmileText(context, string, listUser, textView, color, spanAtUserCallBack, spanUrlCallBack);
    textView.setText(spannable);
}

/**
 * AT某人的跳转
 *
 * @param context
 * @param listUser           需要显示的@某人
 * @param content            需要处理的文本
 * @param textView           需要显示的view
 * @param clickable          @某人是否可以点击
 * @param color              需要显示的颜色
 * @param spanAtUserCallBack @某人点击的返回
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
 * @param context
 * @param string             需要处理的文本
 * @param listUser           需要显示的@某人
 * @param textView           需要显示的view
 * @param color              需要显示的颜色
 * @param spanAtUserCallBack @某人点击的返回
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

```
