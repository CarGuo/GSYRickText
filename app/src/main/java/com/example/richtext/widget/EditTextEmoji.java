package com.example.richtext.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.example.richtext.utils.ScreenUtils;
import com.shuyu.textutillib.SmileUtils;

import com.shuyu.textutillib.MentionEditText;


/**
 * Created by GUO on 2015/12/2.
 */


public class EditTextEmoji extends MentionEditText {

    private int maxLength = 2000;
    private int size;

    public EditTextEmoji(Context context) {
        super(context);
    }

    public EditTextEmoji(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (isInEditMode())
            return;

        InputFilter[] filters = {new InputFilter.LengthFilter(maxLength)};
        setFilters(filters);

        size = ScreenUtils.dip2px(context, 20);
    }

    public void insertIcon(String name) {

        String curString = getText().toString();
        if ((curString.length() + name.length()) > maxLength) {
            return;
        }

        int resId = SmileUtils.getRedId(name);

        Drawable drawable = this.getResources().getDrawable(resId);
        if (drawable == null)
            return;
        drawable.setBounds(0, 0, size, size);//这里设置图片的大小
        ImageSpan imageSpan = new ImageSpan(drawable);
        SpannableString spannableString = new SpannableString(name);
        spannableString.setSpan(imageSpan, 0, spannableString.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);


        int index = Math.max(getSelectionStart(), 0);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(getText());
        spannableStringBuilder.insert(index, spannableString);

        setText(spannableStringBuilder);
        setSelection(index + spannableString.length());


    }

    public void insertIconString(String string) {

        String curString = getText().toString();
        if ((curString.length() + string.length()) > maxLength) {
            return;
        }
        int index = Math.max(getSelectionStart(), 0);
        StringBuilder stringBuilder = new StringBuilder(getText());
        stringBuilder.insert(index, string);

        setText(stringBuilder);
        setSelection(index + string.length());

    }

    private boolean isRequest = false;

    public boolean isRequest() {
        return isRequest;
    }

    //是否可以点击滑动
    public void setIsRequest(boolean isRequest) {
        this.isRequest = isRequest;
    }


    public int getEditTextMaxLength() {
        return maxLength;
    }

    //最大可输入长度
    public void setEditTextMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        getParent().requestDisallowInterceptTouchEvent(isRequest);
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }
        return super.onTouchEvent(event);
    }

}