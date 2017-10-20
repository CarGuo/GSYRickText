/*
 * Copyright 2016 Andy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.shuyu.textutillib;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.widget.EditText;

import com.shuyu.textutillib.span.ClickTopicSpan;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 原项目 https://github.com/luckyandyzhang/MentionEditText
 * <p>
 * MentionEditText adds some useful features for mention string(@xxxx), such as highlight,
 * intelligent deletion, intelligent selection and '@' input detection, etc.
 *
 * @author Andy
 */
class MentionEditText extends AppCompatEditText {
    //public static final String DEFAULT_MENTION_PATTERN = "@[\\u4e00-\\u9fa5\\w\\-]+";

    public static final String DEFAULT_MENTION_PATTERN = "@[^(?!@)\\s]+?\\u0008";
    public static final String TOPIC_MENTION_PATTERN = "#[^^(?!#)\\s]+?#";

    protected Pattern mPattern;
    protected Pattern mTopicPattern;

    private Range mLastSelectedRange;
    protected List<Range> mRangeArrayList;

    private OnMentionInputListener mOnMentionInputListener;

    private boolean mIsSelected;

    public MentionEditText(Context context) {
        super(context);
        init();
    }

    public MentionEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MentionEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        return new HackInputConnection(super.onCreateInputConnection(outAttrs), true, this);
    }


    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        colorMentionString();
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        super.onSelectionChanged(selStart, selEnd);
        //avoid infinite recursion after calling setSelection()
        if (mLastSelectedRange != null && mLastSelectedRange.isEqual(selStart, selEnd)) {
            return;
        }

        //if user cancel a selection of mention string, reset the state of 'mIsSelected'
        Range closestRange = getRangeOfClosestMentionString(selStart, selEnd);
        if (closestRange != null && closestRange.to == selEnd) {
            mIsSelected = false;
        }

        Range nearbyRange = getRangeOfNearbyMentionString(selStart, selEnd);
        //if there is no mention string nearby the cursor, just skip
        if (nearbyRange == null) {
            return;
        }

        //forbid cursor located in the mention string.
        if (selStart == selEnd) {
            setSelection(nearbyRange.getAnchorPosition(selStart));
        } else {
            if (selEnd < nearbyRange.to) {
                setSelection(selStart, nearbyRange.to);
            }
            if (selStart > nearbyRange.from) {
                setSelection(nearbyRange.from, selEnd);
            }
        }
    }

    private void init() {
        mRangeArrayList = new ArrayList<>(5);
        mPattern = Pattern.compile(DEFAULT_MENTION_PATTERN);
        mTopicPattern = Pattern.compile(TOPIC_MENTION_PATTERN);
        //disable suggestion
        //setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        //addTextChangedListener(new MentionTextWatcher());
    }

    private void colorMentionString() {
        //reset state
        mIsSelected = false;
        if (mRangeArrayList != null) {
            mRangeArrayList.clear();
        }

        Editable spannableText = getText();
        if (spannableText == null || TextUtils.isEmpty(spannableText.toString())) {
            return;
        }

        CharSequence charSequence = getText();
        int ends = charSequence.length();
        Spannable sp = getText();

        if (sp instanceof SpannableStringBuilder) {
            //find mention string and color it
            int lastMentionIndex = -1;
            String text = spannableText.toString();
            Matcher matcher = mTopicPattern.matcher(text);
            while (matcher.find()) {
                String mentionText = matcher.group();
                int start;
                if (lastMentionIndex != -1) {
                    start = text.indexOf(mentionText, lastMentionIndex);
                } else {
                    start = text.indexOf(mentionText);
                }
                int end = start + mentionText.length();
                lastMentionIndex = end;
                //record all mention-string's position
                mRangeArrayList.add(new Range(start, end));
            }


        } else {
            ClickTopicSpan[] atSpan = sp.getSpans(0, ends, ClickTopicSpan.class);
            for (ClickTopicSpan clickTopicSpan : atSpan) {
                mRangeArrayList.add(new Range(sp.getSpanStart(clickTopicSpan), sp.getSpanEnd(clickTopicSpan)));
            }
        }
        //find mention string and color it
        int lastMentionIndex = -1;
        String text = spannableText.toString();
        Matcher matcher = mPattern.matcher(text);
        while (matcher.find()) {
            String mentionText = matcher.group();
            int start;
            if (lastMentionIndex != -1) {
                start = text.indexOf(mentionText, lastMentionIndex);
            } else {
                start = text.indexOf(mentionText);
            }
            int end = start + mentionText.length();
            lastMentionIndex = end;
            //record all mention-string's position
            mRangeArrayList.add(new Range(start, end));
        }
    }

    protected Range getRangeOfClosestMentionString(int selStart, int selEnd) {
        if (mRangeArrayList == null) {
            return null;
        }
        for (Range range : mRangeArrayList) {
            if (range.contains(selStart, selEnd)) {
                return range;
            }
        }
        return null;
    }

    private Range getRangeOfNearbyMentionString(int selStart, int selEnd) {
        if (mRangeArrayList == null) {
            return null;
        }
        for (Range range : mRangeArrayList) {
            if (range.isWrappedBy(selStart, selEnd)) {
                return range;
            }
        }
        return null;
    }

    //handle the deletion action for mention string, such as '@test'
    private class HackInputConnection extends InputConnectionWrapper {
        private EditText editText;

        public HackInputConnection(InputConnection target, boolean mutable, MentionEditText editText) {
            super(target, mutable);
            this.editText = editText;
        }

        @Override
        public boolean sendKeyEvent(KeyEvent event) {
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
                int selectionStart = editText.getSelectionStart();
                int selectionEnd = editText.getSelectionEnd();
                Range closestRange = getRangeOfClosestMentionString(selectionStart, selectionEnd);
                if (closestRange == null) {
                    mIsSelected = false;
                    return super.sendKeyEvent(event);
                }
                //if mention string has been selected or the cursor is at the beginning of mention string, just use default action(delete)
                if (mIsSelected || selectionStart == closestRange.from) {
                    mIsSelected = false;
                    return super.sendKeyEvent(event);
                } else {
                    //select the mention string
                    mIsSelected = true;
                    mLastSelectedRange = closestRange;
                    setSelection(closestRange.to, closestRange.from);
                }
                return true;
            }
            return super.sendKeyEvent(event);
        }

        @Override
        public boolean deleteSurroundingText(int beforeLength, int afterLength) {
            if (beforeLength == 1 && afterLength == 0) {
                return sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL))
                        && sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL));
            } else if (beforeLength < 0 && afterLength == 0) {
                int selectionStart = editText.getSelectionStart();
                int selectionEnd = editText.getSelectionEnd();
                if (selectionStart == selectionEnd) {
                    setSelection(selectionStart - beforeLength, selectionStart - beforeLength);
                    super.deleteSurroundingText(-beforeLength, afterLength);
                }
            }
            return super.deleteSurroundingText(beforeLength, afterLength);
        }
    }

    //helper class to record the position of mention string in EditText
    private class Range {
        int from;
        int to;

        public Range(int from, int to) {
            this.from = from;
            this.to = to;
        }

        public boolean isWrappedBy(int start, int end) {
            return (start > from && start < to) || (end > from && end < to);
        }

        public boolean contains(int start, int end) {
            return from <= start && to >= end;
        }

        public boolean isEqual(int start, int end) {
            return (from == start && to == end) || (from == end && to == start);
        }

        public int getAnchorPosition(int value) {
            if ((value - from) - (to - value) >= 0) {
                return to;
            } else {
                return from;
            }
        }
    }

    /**
     * Listener for '@' character
     */
    public interface OnMentionInputListener {
        /**
         * call when '@' character is inserted into EditText
         */
        void onMentionCharacterInput();
    }

}
