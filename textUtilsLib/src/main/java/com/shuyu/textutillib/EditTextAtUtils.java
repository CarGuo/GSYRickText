package com.shuyu.textutillib;


import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.shuyu.textutillib.listener.EditTextAtUtilJumpListener;
import com.shuyu.textutillib.model.TopicModel;
import com.shuyu.textutillib.model.UserModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 处理At某人的逻辑
 * Created by shuyu on 2016/11/10.
 */
public class EditTextAtUtils {
    private EditText editText;
    private List<String> nameList;
    private List<String> idList;
    private List<String> idTopicList;
    private List<String> nameTopicList;
    private EditTextAtUtilJumpListener editTextAtUtilJumpListener;

    /**
     * @param editText 需要对应显示的edittext
     * @param nameList 传入一个不变名字的list
     * @param idList   传入一个不变的list的id
     */
    public EditTextAtUtils(EditText editText, final List<String> nameList, final List<String> idList) {
        this.nameList = nameList;
        this.idList = idList;
        this.editText = editText;
        resolveAtPersonEditText();
    }

    /**
     * @param editText 需要对应显示的edittext
     * @param nameList 传入一个不变名字的list
     * @param idList   传入一个不变的list的id
     */
    public EditTextAtUtils(EditText editText, final List<String> nameList, final List<String> idList, final List<String> nameTopicList, final List<String> idTopicList) {

        this.nameList = nameList;
        this.idList = idList;
        this.nameTopicList = nameTopicList;
        this.idTopicList = idTopicList;
        this.editText = editText;
        resolveAtPersonEditText();
    }


    /**
     * 删除@某人里面的缓存列表
     */
    private void resolveDeleteName() {
        int selectionStart = editText.getSelectionStart();
        int lastPos = 0;
        for (int i = 0; i < nameList.size(); i++) { //循环遍历整个输入框的所有字符
            if ((lastPos = editText.getText().toString().indexOf(nameList.get(i).replace("\b", ""), lastPos)) != -1) {
                if (selectionStart > lastPos && selectionStart <= (lastPos + nameList.get(i).length())) {
                    nameList.remove(i);
                    idList.remove(i);
                    return;
                } else {
                    lastPos++;
                }
            } else {
                lastPos += (nameList.get(i)).length();
            }
        }
    }

    /**
     * 删除@某人里面的缓存列表
     */
    private void resolveDeleteTopic() {
        if (nameTopicList == null) {
            return;
        }
        int selectionStart = editText.getSelectionStart();
        int lastPos = 0;
        for (int i = 0; i < nameTopicList.size(); i++) { //循环遍历整个输入框的所有字符
            if ((lastPos = editText.getText().toString().indexOf(nameTopicList.get(i), lastPos)) != -1) {
                if (selectionStart > lastPos && selectionStart <= (lastPos + nameTopicList.get(i).length())) {
                    nameTopicList.remove(i);
                    idTopicList.remove(i);
                    return;
                } else {
                    lastPos++;
                }
            } else {
                lastPos += (nameTopicList.get(i)).length();
            }
        }
    }

    /**
     * 处理光标不插入在AT某人字段上
     */
    private void resolveEditTextClick() {
        if (TextUtils.isEmpty(editText.getText()))
            return;
        int selectionStart = editText.getSelectionStart();
        if (selectionStart > 0) {
            int lastPos = 0;
            boolean success = false;
            for (int i = 0; i < nameList.size(); i++) {
                if ((lastPos = editText.getText().toString().indexOf(
                        nameList.get(i), lastPos)) != -1) {
                    if (selectionStart >= lastPos && selectionStart <= (lastPos + nameList.get(i).length())) {
                        editText.setSelection(lastPos + nameList.get(i).length());
                        success = true;
                    }
                    lastPos += (nameList.get(i)).length();
                }
            }

            if (!success && nameTopicList != null) {
                lastPos = 0;
                for (int i = 0; i < nameTopicList.size(); i++) {
                    if ((lastPos = editText.getText().toString().indexOf(
                            nameTopicList.get(i), lastPos)) != -1) {
                        if (selectionStart >= lastPos && selectionStart <= (lastPos + nameTopicList.get(i).length())) {
                            editText.setSelection(lastPos + nameTopicList.get(i).length());
                        }
                        lastPos += (nameTopicList.get(i)).length();
                    }
                }
            }
        }
    }

    /**
     * 监听字符变化与点击事件
     */
    private void resolveAtPersonEditText() {
        editText.addTextChangedListener(new TextWatcher() {

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
                    } else if ("#".equals(deleteSb)) {
                        delIndex = s.toString().lastIndexOf("#", start - 1);
                        length = start - delIndex;
                    }
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String setMsg = s.toString();
                if (delIndex != -1) {
                    resolveDeleteName();
                    resolveDeleteTopic();
                    int position = delIndex;
                    delIndex = -1;
                    editText.getText().replace(position, position + length, "");
                    editText.setSelection(position);
                } else {
                    if (setMsg.length() >= beforeCount && editText.getSelectionEnd() > 0 && setMsg.charAt(editText.getSelectionEnd() - 1) == '@') {
                        if (editTextAtUtilJumpListener != null) {
                            editTextAtUtilJumpListener.notifyAt();
                        }
                    } else if (setMsg.length() >= beforeCount && editText.getSelectionEnd() > 0 && setMsg.charAt(editText.getSelectionEnd() - 1) == '#') {
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

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resolveEditTextClick();
            }
        });

    }

    /**
     * 添加了@的加入
     *
     * @param user_id   用户id
     * @param user_name 用户名
     * @param color     类似#f77500的颜色格式
     */
    public void resolveText(String user_id, String user_name, String color) {
        nameList.add(user_name + "\b");
        idList.add(user_id);

        int index = editText.getSelectionStart();
        SpannableStringBuilder spannableStringBuilder =
                new SpannableStringBuilder(editText.getText());
        //直接用span会导致后面没文字的时候新输入的一起变色
        Spanned htmlText = Html.fromHtml(String.format("<font color='%s'>" + user_name + "</font>", color));
        spannableStringBuilder.insert(index, htmlText);
        spannableStringBuilder.insert(index + htmlText.length(), "\b");
        editText.setText(spannableStringBuilder);
        editText.setSelection(index + htmlText.length() + 1);
    }

    public void resolveTopicText(String topicId, String topicName, String color) {
        nameTopicList.add(topicName);
        idTopicList.add(topicId);

        int index = editText.getSelectionStart();
        SpannableStringBuilder spannableStringBuilder =
                new SpannableStringBuilder(editText.getText());
        //直接用span会导致后面没文字的时候新输入的一起变色
        Spanned htmlText = Html.fromHtml(String.format("<font color='%s'>" + topicName + "</font>", color));
        spannableStringBuilder.insert(index, htmlText);
        editText.setText(spannableStringBuilder);
        editText.setSelection(index + htmlText.length());
    }


    /**
     * 编辑框输入了@后的跳转
     *
     * @param editTextAtUtilJumpListener 跳转回调
     */
    public void setEditTextAtUtilJumpListener(EditTextAtUtilJumpListener editTextAtUtilJumpListener) {
        this.editTextAtUtilJumpListener = editTextAtUtilJumpListener;
    }

    /**
     * 处理插入的文本
     *
     * @param context  上下文
     * @param text     需要处理的文本
     * @param listUser 需要处理的at某人列表
     * @param editText 需要被插入的editText
     * @param color    类似#f77500的颜色格式
     */
    public static void resolveInsertText(Context context, String text, List<UserModel> listUser, List<TopicModel> listTopic, String color, EditText editText) {

        //此处保存名字的键值
        Map<String, String> names = new HashMap<>();
        if (listUser != null && listUser.size() > 0) {
            for (UserModel userModel : listUser) {
                names.put("@" + userModel.getUser_name(), userModel.getUser_name());
            }
        }
        if (TextUtils.isEmpty(text))
            return;
        //设置表情
        Spannable spannable;
        if (listTopic != null && listTopic.size() > 0) {
            Map<String, String> topics = new HashMap<>();
            for (TopicModel topicModel : listTopic) {
                topics.put("#" + topicModel.getTopicName() + "#", "#" + topicModel.getTopicName() + "#");
            }
            //查找##
            int length = text.length();
            Pattern pattern = Pattern.compile("#.*?#");
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
        editText.setText(spannable);

        //查找@
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
        editText.setText(spannableStringBuilder);
        editText.setSelection(editText.getText().length());
    }


    public static void resolveTopicResult(EditTextAtUtils editTextAtUtils, String color, TopicModel topicModel) {
        String topicId = topicModel.getTopicId();
        String topicName = "#" + topicModel.getTopicName() + "#";
        editTextAtUtils.resolveTopicText(topicId, topicName, color);
    }


    public static void resolveTopicResultByEnter(EditText editText, EditTextAtUtils editTextAtUtils, String color, TopicModel topicModel) {
        String topicId = topicModel.getTopicId();
        editText.getText().delete(editText.getSelectionEnd() - 1,
                editText.getSelectionEnd());
        String topicName = "#" + topicModel.getTopicName() + "#";
        editTextAtUtils.resolveTopicText(topicId, topicName, color);

    }


    /***
     * 按了@按键的数据返回处理
     *
     * @param editTextAtUtils 已经new好的 editTextAtUtils对象
     * @param userModel       用户model
     * @param color           颜色
     */
    public static void resolveAtResult(EditTextAtUtils editTextAtUtils, String color, UserModel userModel) {
        String user_id = userModel.getUser_id();
        String user_name = "@" + userModel.getUser_name();
        editTextAtUtils.resolveText(user_id, user_name, color);
    }

    /***
     * 发布的时候输入了AT的返回处理
     *
     * @param color           颜色
     * @param editText        显示的editTEXT
     * @param editTextAtUtils 已经new好的 editTextAtUtils对象
     * @param userModel       用户model
     */
    public static void resolveAtResultByEnterAt(EditText editText, EditTextAtUtils editTextAtUtils, String color, UserModel userModel) {
        String user_id = userModel.getUser_id();
        editText.getText().delete(editText.getSelectionEnd() - 1,
                editText.getSelectionEnd());
        String user_name = "@" + userModel.getUser_name();
        editTextAtUtils.resolveText(user_id, user_name, color);

    }

}
