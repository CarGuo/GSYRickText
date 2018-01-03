package com.shuyu.textutillib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.shuyu.textutillib.listener.OnEditTextUtilJumpListener;
import com.shuyu.textutillib.model.TopicModel;
import com.shuyu.textutillib.model.UserModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 富文本设置 话题、at某人、表情
 * Created by guoshuyu on 2017/8/18.
 */

public class RichEditText extends MentionEditText {

    /**
     * 默认最长输入
     */
    private int richMaxLength = 9999;

    /**
     * 表情大小
     */
    private int richIconSize;

    /**
     * 是否可以在列表增加触摸滑动
     */
    private boolean isRequestTouchInList = false;

    /**
     * 用户at
     */
    private List<UserModel> nameList;

    /**
     * 话题
     */
    private List<TopicModel> topicList;

    /**
     * 输入监控回调
     */
    private OnEditTextUtilJumpListener editTextAtUtilJumpListener;
    /**
     * At颜色
     */
    private String colorTopic = "#0000FF";
    /**
     * 话题颜色
     */
    private String colorAtUser = "#f77521";

    private boolean deleteByEnter;


    public RichEditText(Context context) {
        super(context);
        init(context, null);
    }

    public RichEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RichEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {

        if (isInEditMode())
            return;

        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RichEditText);
            int textLength = array.getInteger(R.styleable.RichEditText_richMaxLength, 9999);
            float iconSize = (int) array.getDimension(R.styleable.RichEditText_richIconSize, 0);
            String colorAtUser = array.getString(R.styleable.RichEditText_richEditColorAtUser);
            String colorTopic = array.getString(R.styleable.RichEditText_richEditColorTopic);
            richMaxLength = textLength;
            InputFilter[] filters = {new InputFilter.LengthFilter(richMaxLength)};
            setFilters(filters);
            if (iconSize == 0) {
                richIconSize = dip2px(context, 20);
            }
            if (!TextUtils.isEmpty(colorAtUser)) {
                this.colorAtUser = colorAtUser;
            }
            if (!TextUtils.isEmpty(colorTopic)) {
                this.colorTopic = colorTopic;
            }
            array.recycle();
        }

        resolveAtPersonEditText();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        getParent().requestDisallowInterceptTouchEvent(isRequestTouchInList);
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }
        return super.onTouchEvent(event);
    }


    /**
     * dip转为PX
     */
    private int dip2px(Context context, float dipValue) {
        float fontScale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * fontScale + 0.5f);
    }


    /**
     * 删除@某人里面的缓存列表
     */
    private void resolveDeleteName() {
        int selectionStart = getSelectionStart();
        int lastPos = 0;
        for (int i = 0; i < nameList.size(); i++) { //循环遍历整个输入框的所有字符
            if ((lastPos = getText().toString().indexOf(nameList.get(i).getUser_name().replace("\b", ""), lastPos)) != -1) {
                if (selectionStart > lastPos && selectionStart <= (lastPos + nameList.get(i).getUser_name().length())) {
                    nameList.remove(i);
                    return;
                } else {
                    lastPos++;
                }
            } else {
                lastPos += (nameList.get(i)).getUser_name().length();
            }
        }
    }

    /**
     * 删除@某人里面的缓存列表
     */
    private void resolveDeleteTopic() {
        if (topicList == null) {
            return;
        }
        int selectionStart = getSelectionStart();
        int lastPos = 0;
        for (int i = 0; i < topicList.size(); i++) { //循环遍历整个输入框的所有字符
            if ((lastPos = getText().toString().indexOf(topicList.get(i).getTopicName(), lastPos)) != -1) {
                if (selectionStart > lastPos && selectionStart <= (lastPos + topicList.get(i).getTopicName().length())) {
                    topicList.remove(i);
                    return;
                } else {
                    lastPos++;
                }
            } else {
                lastPos += (topicList.get(i)).getTopicName().length();
            }
        }
    }

    private void resolveDeleteList(String text, int startP, int endP) {
        if (topicList != null && topicList.size() > 0) {
            int lastMentionIndex = -1;
            Matcher matcher = mTopicPattern.matcher(text);
            while (matcher.find()) {
                String mentionText = matcher.group();
                int start;
                if (lastMentionIndex != -1) {
                    start = getText().toString().indexOf(mentionText, lastMentionIndex);
                } else {
                    start = getText().toString().indexOf(mentionText);
                }
                int end = start + mentionText.length();
                lastMentionIndex = end;
                for (int i = 0; i < topicList.size(); i++) {
                    TopicModel topicModel = topicList.get(i);
                    if (topicModel.getTopicName().equals(mentionText)
                            && getRangeOfClosestMentionString(start, end) != null) {
                        topicList.remove(topicModel);
                        ForegroundColorSpan[] spannable = getText().getSpans(startP, endP, ForegroundColorSpan.class);
                        if (spannable != null && spannable.length > 0) {
                            getText().removeSpan(spannable[0]);
                        }
                        break;
                    }
                }
            }
        }

        if (nameList != null && nameList.size() > 0) {
            int lastMentionIndex = -1;
            Matcher matcher = mPattern.matcher(text);
            while (matcher.find()) {
                String mentionText = matcher.group();
                int start;
                if (lastMentionIndex != -1) {
                    start = getText().toString().indexOf(mentionText, lastMentionIndex);
                } else {
                    start = getText().toString().indexOf(mentionText);
                }
                int end = start + mentionText.length();
                mentionText = mentionText.substring(mentionText.lastIndexOf("@"), mentionText.length());
                lastMentionIndex = end;
                for (int i = 0; i < nameList.size(); i++) {
                    UserModel userModel = nameList.get(i);
                    if (userModel.getUser_name().replace("\b", "").equals(mentionText.replace("\b", ""))
                            && getRangeOfClosestMentionString(start, end) != null) {
                        nameList.remove(userModel);
                        break;
                    }
                }
            }
        }
    }

    /**
     * 处理光标不插入在AT某人字段上
     */
    private void resolveEditTextClick() {
        if (TextUtils.isEmpty(getText()))
            return;
        int selectionStart = getSelectionStart();
        if (selectionStart > 0) {
            int lastPos = 0;
            boolean success = false;
            for (int i = 0; i < nameList.size(); i++) {
                if ((lastPos = getText().toString().indexOf(
                        nameList.get(i).getUser_name(), lastPos)) != -1) {
                    if (selectionStart >= lastPos && selectionStart <= (lastPos + nameList.get(i).getUser_name().length())) {
                        setSelection(lastPos + nameList.get(i).getUser_name().length());
                        success = true;
                    }
                    lastPos += (nameList.get(i)).getUser_name().length();
                }
            }

            if (!success && topicList != null) {
                lastPos = 0;
                for (int i = 0; i < topicList.size(); i++) {
                    if ((lastPos = getText().toString().indexOf(
                            topicList.get(i).getTopicName(), lastPos)) != -1) {
                        if (selectionStart >= lastPos && selectionStart <= (lastPos + topicList.get(i).getTopicName().length())) {
                            setSelection(lastPos + topicList.get(i).getTopicName().length());
                        }
                        lastPos += (topicList.get(i)).getTopicName().length();
                    }
                }
            }
        }
    }

    /**
     * 监听字符变化与点击事件
     */
    private void resolveAtPersonEditText() {
        addTextChangedListener(new TextWatcher() {

            private int length = 0;
            private int delIndex = -1;
            private int beforeCount;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                beforeCount = s.toString().length();
                if (count == 1) {
                    String deleteSb = s.toString().substring(start, start + 1);
                    if ("\b".equals(deleteSb)) {
                        delIndex = s.toString().lastIndexOf("@", start);
                        length = start - delIndex;
                    } else if ("#".equals(deleteSb) && !deleteByEnter) {
                        delIndex = s.toString().lastIndexOf("#", start - 1);
                        length = start - delIndex;
                    }
                    deleteByEnter = false;
                } else if (after < count && (count - after) > 1) {
                    // 大批量删除处理的列表处理
                    resolveDeleteList(s.toString().substring(start, start + count), start, start + count);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String setMsg = s.toString();
                if (delIndex != -1) {
                    if (length > 1) {
                        resolveDeleteName();
                        resolveDeleteTopic();
                        int position = delIndex;
                        delIndex = -1;
                        try {
                            getText().replace(position, position + length, "");
                            setSelection(position);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    if (setMsg.length() >= beforeCount && getSelectionEnd() > 0 && setMsg.charAt(getSelectionEnd() - 1) == '@') {
                        if (editTextAtUtilJumpListener != null) {
                            editTextAtUtilJumpListener.notifyAt();
                        }
                    } else if (setMsg.length() >= beforeCount && getSelectionEnd() > 0 && setMsg.charAt(getSelectionEnd() - 1) == '#') {
                        if (editTextAtUtilJumpListener != null) {
                            editTextAtUtilJumpListener.notifyTopic();
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resolveEditTextClick();
            }
        });

    }

    /**
     * 处理话题和表情
     *
     * @param context   上下文
     * @param text      输入文本
     * @param color     颜色
     * @param listTopic 话题列表
     * @return Spannable
     */
    private static Spannable resolveTopicInsert(Context context, String text, String color, List<TopicModel> listTopic) {
        Spannable spannable;
        if (listTopic != null && listTopic.size() > 0) {
            Map<String, String> topics = new HashMap<>();
            for (TopicModel topicModel : listTopic) {
                topics.put(topicModel.getTopicName(), topicModel.getTopicName());
            }
            //查找##
            int length = text.length();
            Pattern pattern = Pattern.compile("#[^\\s]+?#");
            Matcher matcher = pattern.matcher(text);
            SpannableStringBuilder spannableStringBuilder =
                    new SpannableStringBuilder(text);
            for (int i = 0; i < length; i++) {
                if (matcher.find()) {
                    String name = text.substring(matcher.start(), matcher.end());
                    if (topics.containsKey(name)) {
                        //直接用span会导致后面没文字的时候新输入的一起变色
                        Spanned htmlText = Html.fromHtml(String.format("<font color='%s'>" + name + "</font>", color));
                        spannableStringBuilder.replace(matcher.start(), matcher.start() + name.length(), htmlText);
                    }
                }
            }

            spannable = spannableStringBuilder;
            SmileUtils.addSmiles(context, spannable);
        } else {
            spannable = TextCommonUtils.getEmojiText(context, text);

        }
        SmileUtils.addSmiles(context, spannable);
        return spannable;
    }

    /**
     * 处理at某人
     *
     * @param text      输入文本
     * @param spannable 处理过的文本
     * @param color     颜色
     * @param listUser  用户列表
     * @return Spannable
     */
    private Spannable resolveAtInsert(String text, Spannable spannable, String color, List<UserModel> listUser) {

        if (listUser == null || listUser.size() <= 0) {
            return spannable;
        }

        //此处保存名字的键值
        Map<String, String> names = new HashMap<>();
        if (listUser.size() > 0) {
            for (UserModel userModel : listUser) {
                names.put(userModel.getUser_name(), userModel.getUser_name());
            }
        }
        int length = spannable.length();
        Pattern pattern = Pattern.compile("@[^\\s]+\\s?");
        Matcher matcher = pattern.matcher(spannable);
        SpannableStringBuilder spannableStringBuilder =
                new SpannableStringBuilder(spannable);
        for (int i = 0; i < length; i++) {
            if (matcher.find()) {
                String name = text.substring(matcher.start(), matcher.end());
                if (names.containsKey(name.replace("\b", "").replace(" ", ""))) {
                    //直接用span会导致后面没文字的时候新输入的一起变色
                    Spanned htmlText = Html.fromHtml(String.format("<font color='%s'>" + name + "</font>", color));
                    spannableStringBuilder.replace(matcher.start(), matcher.start() + name.length(), htmlText);
                    int index = matcher.start() + htmlText.length();
                    if (index < text.length()) {
                        if (" ".equals(text.subSequence(index - 1, index))) {
                            spannableStringBuilder.replace(index - 1, index, "\b");
                        }
                    } else {
                        if (text.substring(index - 1).equals(" ")) {
                            spannableStringBuilder.replace(index - 1, index, "\b");
                        } else {
                            //如果是最后面的没有空格，补上\b
                            spannableStringBuilder.insert(index, "\b");
                        }
                    }
                }
            }
        }
        return spannableStringBuilder;
    }

    /********************公开接口**********************/

    /**
     * 设置数据列表
     *
     * @param nameList  at用户
     * @param topicList 话题
     */
    public void setModelList(List<UserModel> nameList, List<TopicModel> topicList) {
        this.nameList = nameList;
        this.topicList = topicList;
    }

    public int getRichMaxLength() {
        return richMaxLength;
    }

    /**
     * 最长输入
     *
     * @param richMaxLength
     */
    public void setRichMaxLength(int richMaxLength) {
        this.richMaxLength = richMaxLength;
    }

    public int getRichIconSize() {
        return richIconSize;
    }


    public void setRichEditTopicList(List<TopicModel> list) {
        if (list != null) {
            this.topicList = list;
        }
    }

    public void setRichEditNameList(List<UserModel> list) {
        if (list != null) {
            this.nameList = list;
        }
    }

    public void setRichEditColorAtUser(String color) {
        this.colorAtUser = color;
    }

    public void setRichEditColorTopic(String color) {
        this.colorTopic = color;
    }

    /**
     * 表情大小
     *
     * @param richIconSize
     */
    public void setRichIconSize(int richIconSize) {
        this.richIconSize = richIconSize;
    }

    /**
     * 话题颜色
     *
     * @param colorTopic 类似#f77500的颜色格式
     */
    public void setColorTopic(String colorTopic) {
        this.colorTopic = colorTopic;
    }

    /**
     * at人颜色
     *
     * @param colorAtUser 类似#f77500的颜色格式
     */
    public void setColorAtUser(String colorAtUser) {
        this.colorAtUser = colorAtUser;
    }

    /**
     * 添加了@的加入
     *
     * @param userModel 用户实体
     */
    public void resolveText(UserModel userModel) {
        String userName = userModel.getUser_name();
        userModel.setUser_name(userName + "\b");
        nameList.add(userModel);

        int index = getSelectionStart();
        SpannableStringBuilder spannableStringBuilder =
                new SpannableStringBuilder(getText());
        //直接用span会导致后面没文字的时候新输入的一起变色
        Spanned htmlText = Html.fromHtml(String.format("<font color='%s'>" + userName + "</font>", colorAtUser));
        spannableStringBuilder.insert(index, htmlText);
        spannableStringBuilder.insert(index + htmlText.length(), "\b");
        setText(spannableStringBuilder);
        setSelection(index + htmlText.length() + 1);
    }

    /**
     * 插入了话题
     *
     * @param topicModel 话题实体
     */
    public void resolveTopicText(TopicModel topicModel) {
        topicList.add(topicModel);
        int index = getSelectionStart();
        SpannableStringBuilder spannableStringBuilder =
                new SpannableStringBuilder(getText());
        //直接用span会导致后面没文字的时候新输入的一起变色
        Spanned htmlText = Html.fromHtml(String.format("<font color='%s'>" + topicModel.getTopicName() + "</font>", colorTopic));
        spannableStringBuilder.insert(index, htmlText);
        setText(spannableStringBuilder);
        setSelection(index + htmlText.length());
    }


    /**
     * 编辑框输入了@后的跳转
     *
     * @param editTextAtUtilJumpListener 跳转回调
     */
    public void setEditTextAtUtilJumpListener(OnEditTextUtilJumpListener editTextAtUtilJumpListener) {
        this.editTextAtUtilJumpListener = editTextAtUtilJumpListener;
    }

    /**
     * 初始户处理插入的文本
     *
     * @param context  上下文
     * @param text     需要处理的文本
     * @param listUser 需要处理的at某人列表
     */
    public void resolveInsertText(Context context, String text, List<UserModel> listUser, List<TopicModel> listTopic) {

        if (TextUtils.isEmpty(text))
            return;

        //设置表情和话题
        Spannable spannable = resolveTopicInsert(context, text, colorTopic, listTopic);
        setText(spannable);

        //设置@
        Spannable span = resolveAtInsert(text, spannable, colorAtUser, listUser);
        setText(span);

        setSelection(getText().length());
    }


    /**
     * 按了话题按键的数据返回处理
     *
     * @param topicModel 话题model
     */
    public void resolveTopicResult(TopicModel topicModel) {
        String topicId = topicModel.getTopicId();
        String topicName = "#" + topicModel.getTopicName() + "#";
        TopicModel topic = new TopicModel(topicName, topicId);
        resolveTopicText(topic);
    }


    /**
     * 输入了#话题按键的数据返回处理
     *
     * @param topicModel 话题model
     */
    public void resolveTopicResultByEnter(TopicModel topicModel) {
        String topicId = topicModel.getTopicId();
        deleteByEnter = true;
        if (getSelectionEnd() == 0) {
            getText().delete(0, 1);
        } else {
            int index = getText().toString().indexOf("#", getSelectionEnd() - 1);
            if (index != -1) {
                getText().delete(index, index + 1);
            }
        }
        String topicName = "#" + topicModel.getTopicName() + "#";
        TopicModel topic = new TopicModel(topicName, topicId);
        resolveTopicText(topic);

    }


    /***
     * 按了@按键的数据返回处理
     *
     * @param userModel       用户model
     */
    public void resolveAtResult(UserModel userModel) {
        String user_id = userModel.getUser_id();
        String user_name = "@" + userModel.getUser_name();
        UserModel user = new UserModel(user_name, user_id);
        resolveText(user);
    }

    /***
     * 发布的时候输入了AT的返回处理
     *
     * @param userModel       用户model
     */
    public void resolveAtResultByEnterAt(UserModel userModel) {
        String user_id = userModel.getUser_id();
        if (getSelectionEnd() == 0) {
            getText().delete(0, 1);
        } else {
            int index = getText().toString().indexOf("@", getSelectionEnd() - 1);
            if (index != -1) {
                getText().delete(index, index + 1);
            }
        }
        String user_name = "@" + userModel.getUser_name();
        UserModel user = new UserModel(user_name, user_id);
        resolveText(user);

    }

    /**
     * 插入表情
     *
     * @param name
     */
    public void insertIcon(String name) {

        String curString = getText().toString();
        if ((curString.length() + name.length()) > richMaxLength) {
            return;
        }

        int resId = SmileUtils.getRedId(name);

        Drawable drawable = this.getResources().getDrawable(resId);
        if (drawable == null)
            return;
        drawable.setBounds(0, 0, richIconSize, richIconSize);//这里设置图片的大小
        ImageSpan imageSpan = new ImageSpan(drawable);
        SpannableString spannableString = new SpannableString(name);
        spannableString.setSpan(imageSpan, 0, spannableString.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);


        int index = Math.max(getSelectionStart(), 0);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(getText());
        spannableStringBuilder.insert(index, spannableString);

        setText(spannableStringBuilder);
        setSelection(index + spannableString.length());


    }

    /**
     * 插入表情文本
     *
     * @param string
     */
    public void insertIconString(String string) {

        String curString = getText().toString();
        if ((curString.length() + string.length()) > richMaxLength) {
            return;
        }
        int index = Math.max(getSelectionStart(), 0);
        StringBuilder stringBuilder = new StringBuilder(getText());
        stringBuilder.insert(index, string);

        setText(stringBuilder);
        setSelection(index + string.length());

    }


    /**
     * 是否可以点击滑动
     *
     * @param isRequest
     */
    public void setIsRequestTouchIn(boolean isRequest) {
        this.isRequestTouchInList = isRequest;
    }

    public boolean isRequestTouchIn() {
        return isRequestTouchInList;
    }

    /**
     * 最大长度
     *
     * @param maxLength
     */
    public void setEditTextMaxLength(int maxLength) {
        this.richMaxLength = maxLength;
    }

    public int getEditTextMaxLength() {
        return richMaxLength;
    }

    /**
     * 返回真实无添加的数据
     *
     * @return
     */
    public List<UserModel> getRealUserList() {
        List<UserModel> list = new ArrayList<>();
        if (nameList == null) {
            return list;
        }
        for (UserModel userModel : nameList) {
            list.add(new UserModel(userModel.getUser_name().replace("@", "").replace("\b", "")
                    , userModel.getUser_id()));

        }
        return list;
    }

    /**
     * 返回真实无添加的数据
     *
     * @return
     */
    public List<TopicModel> getRealTopicList() {
        List<TopicModel> list = new ArrayList<>();
        if (topicList == null) {
            return list;
        }
        for (TopicModel topicModel : topicList) {
            list.add(new TopicModel(topicModel.getTopicName().replace("#", "").replace("#", "")
                    , topicModel.getTopicId()));
        }
        return list;

    }

    /**
     * 提交真实文本可以替换了\b
     *
     * @return
     */
    public String getRealText() {
        if (TextUtils.isEmpty(getText())) {
            return "";
        }
        String text = getText().toString();
        return text.replaceAll("\\u0008", " ");
    }
}
