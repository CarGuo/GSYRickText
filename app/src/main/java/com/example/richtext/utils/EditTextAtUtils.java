package com.example.richtext.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.example.richtext.MainActivity;
import com.example.richtext.UserListActivity;
import com.example.richtext.model.UserModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 处理At某人
 */
public class EditTextAtUtils {
    private Activity activity;
    private EditText editText;
    private List<String> contactNameList;
    private List<String> contactIdList;

    public EditTextAtUtils(Activity activity, EditText editText,
                           List<String> contactNameList, List<String> contactIdList) {
        super();
        this.activity = activity;
        this.contactNameList = contactNameList;
        this.contactIdList = contactIdList;
        this.editText = editText;
        resolveAtPersonEditText();
    }

    /**
     * 删除@某人里面的缓存列表
     */
    private void resolveDeleteName() {
        int selectionStart = editText.getSelectionStart();
        int lastPos = 0;
        for (int i = 0; i < contactNameList.size(); i++) { //循环遍历整个输入框的所有字符
            if ((lastPos = editText.getText().toString().indexOf(contactNameList.get(i).replace("\b", ""), lastPos)) != -1) {
                if (selectionStart > lastPos && selectionStart <= (lastPos + contactNameList.get(i).length())) {
                    contactNameList.remove(i);
                    contactIdList.remove(i);
                    return;
                } else {
                    lastPos++;
                }
            } else {
                lastPos += (contactNameList.get(i)).length();
            }
        }
    }

    /**
     * 处理光标不插入在@某人字段上
     */
    private void resolveEditTextClick() {
        if (TextUtils.isEmpty(editText.getText()))
            return;
        int selectionStart = editText.getSelectionStart();
        if (selectionStart > 0) {
            int lastPos = 0;
            for (int i = 0; i < contactNameList.size(); i++) {
                if ((lastPos = editText.getText().toString().indexOf(
                        contactNameList.get(i), lastPos)) != -1) {
                    if (selectionStart >= lastPos && selectionStart <= (lastPos + contactNameList.get(i).length())) {
                        editText.setSelection(lastPos + contactNameList.get(i).length());
                    }
                    lastPos += (contactNameList.get(i)).length();
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
                    }
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String setMsg = s.toString();
                if (delIndex != -1) {
                    resolveDeleteName();
                    int position = delIndex;
                    delIndex = -1;
                    editText.getText().replace(position, position + length, "");
                    editText.setSelection(position);
                } else {
                    if (setMsg.length() >= beforeCount && editText.getSelectionEnd() > 0 && setMsg.charAt(editText.getSelectionEnd() - 1) == '@') {
                        JumpUtil.goToUserList(activity, MainActivity.REQUEST_USER_CODE_INPUT);
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
     * @param user_id
     * @param user_name
     */
    public void resolveText(String user_id, String user_name) {
        contactNameList.add(user_name + "\b");
        contactIdList.add(user_id);

        int index = editText.getSelectionStart();
        SpannableStringBuilder spannableStringBuilder =
                new SpannableStringBuilder(editText.getText());
        //直接用span会导致后面没文字的时候新输入的一起变色
        Spanned htmlText = Html.fromHtml("<font color='#f77500'>" + user_name + "</font>");
        spannableStringBuilder.insert(index, htmlText);
        spannableStringBuilder.insert(index + htmlText.length(), "\b");
        editText.setText(spannableStringBuilder);
        editText.setSelection(index + htmlText.length() + 1);
    }

    /**
     * 处理插入的文本
     *
     * @param context
     * @param text
     * @param listUser
     * @param editText
     */
    public static void resolveInsertText(Context context, String text, List<UserModel> listUser, EditText editText) {

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
        Spannable spannable = TextCommonUtils.getEmojiText(context, text);
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
                    Spanned htmlText = Html.fromHtml("<font color='#f77500'>" + name + "</font>");
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


    /***
     * 发布的时候按了@按键
     */
    public static void resolveAtResult(Intent data, EditText editText, Activity activity, EditTextAtUtils editTextAtUtils) {
        UserModel userModel = (UserModel) data.getSerializableExtra(UserListActivity.DATA);
        String user_id = userModel.getUser_id();
        String user_name = "@" + userModel.getUser_name();
        editTextAtUtils.resolveText(user_id, user_name);
    }

    /***
     * 发布的时候输入了@
     */
    public static void resolveAtResultByEnterAt(Intent data, EditText editText, Activity activity, EditTextAtUtils editTextAtUtils) {

        UserModel userModel = (UserModel) data.getSerializableExtra(UserListActivity.DATA);
        String user_id = userModel.getUser_id();
        editText.getText().delete(editText.getSelectionEnd() - 1,
                editText.getSelectionEnd());
        String user_name = "@" + userModel.getUser_name();
        editTextAtUtils.resolveText(user_id, user_name);

    }

}
