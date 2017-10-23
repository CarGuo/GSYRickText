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

package com.shuyu.textutillib

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.AppCompatEditText
import android.text.Editable
import android.text.InputType
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.view.inputmethod.InputConnectionWrapper
import android.widget.EditText

import com.shuyu.textutillib.span.ClickTopicSpan

import java.util.ArrayList
import java.util.regex.Matcher
import java.util.regex.Pattern


/**
 * 原项目 https://github.com/luckyandyzhang/MentionEditText
 *
 *
 * MentionEditText adds some useful features for mention string(@xxxx), such as highlight,
 * intelligent deletion, intelligent selection and '@' input detection, etc.
 *
 * @author Andy
 */
open class MentionEditText : AppCompatEditText {

    protected var mPattern: Pattern? = null
    protected var mTopicPattern: Pattern? = null

    private var mLastSelectedRange: Range? = null

    private var mRangeArrayList: MutableList<Range>? = null

    private val mOnMentionInputListener: OnMentionInputListener? = null

    private var mIsSelected: Boolean = false

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    override fun onCreateInputConnection(outAttrs: EditorInfo): InputConnection =
            HackInputConnection(super.onCreateInputConnection(outAttrs), true, this)


    override fun onTextChanged(text: CharSequence, start: Int, lengthBefore: Int, lengthAfter: Int) {
        colorMentionString()
    }

    override fun onSelectionChanged(selStart: Int, selEnd: Int) {
        super.onSelectionChanged(selStart, selEnd)
        //avoid infinite recursion after calling setSelection()
        if (mLastSelectedRange != null && mLastSelectedRange!!.isEqual(selStart, selEnd)) {
            return
        }

        //if user cancel a selection of mention string, reset the state of 'mIsSelected'
        val closestRange = getRangeOfClosestMentionString(selStart, selEnd)
        if (closestRange != null && closestRange.to == selEnd) {
            mIsSelected = false
        }

        val nearbyRange = getRangeOfNearbyMentionString(selStart, selEnd) ?: return
        //if there is no mention string nearby the cursor, just skip

        //forbid cursor located in the mention string.
        if (selStart == selEnd) {
            setSelection(nearbyRange.getAnchorPosition(selStart))
        } else {
            if (selEnd < nearbyRange.to) {
                setSelection(selStart, nearbyRange.to)
            }
            if (selStart > nearbyRange.from) {
                setSelection(nearbyRange.from, selEnd)
            }
        }
    }

    private fun init() {
        mRangeArrayList = ArrayList(5)
        mPattern = Pattern.compile(DEFAULT_MENTION_PATTERN)
        mTopicPattern = Pattern.compile(TOPIC_MENTION_PATTERN)
        //disable suggestion
        //setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        //addTextChangedListener(new MentionTextWatcher());
    }

    private fun colorMentionString() {
        //reset state
        mIsSelected = false

        mRangeArrayList?.clear()

        val spannableText = text
        if (spannableText == null || TextUtils.isEmpty(spannableText.toString())) {
            return
        }

        val charSequence = text
        val ends = charSequence.length
        val sp = text

        if (sp is SpannableStringBuilder) {
            //find mention string and color it
            var lastMentionIndex = -1
            val text = spannableText.toString()
            val matcher = mTopicPattern?.matcher(text)
            if(matcher != null) {
                while (matcher.find()) {
                    val mentionText = matcher.group()
                    val start: Int
                    start = if (lastMentionIndex != -1) {
                        text.indexOf(mentionText, lastMentionIndex)
                    } else {
                        text.indexOf(mentionText)
                    }
                    val end = start + mentionText.length
                    lastMentionIndex = end
                    //record all mention-string's position
                    mRangeArrayList?.add(Range(start, end))
                }
            }
        } else {
            val atSpan = sp.getSpans<ClickTopicSpan>(0, ends, ClickTopicSpan::class.java)
            for (clickTopicSpan in atSpan) {
                mRangeArrayList?.add(Range(sp.getSpanStart(clickTopicSpan), sp.getSpanEnd(clickTopicSpan)))
            }
        }
        //find mention string and color it
        var lastMentionIndex = -1
        val text = spannableText.toString()
        val matcher = mPattern?.matcher(text)
        if(matcher != null) {
            while (matcher.find()) {
                val mentionText = matcher.group()
                val start: Int
                start = if (lastMentionIndex != -1) {
                    text.indexOf(mentionText, lastMentionIndex)
                } else {
                    text.indexOf(mentionText)
                }
                val end = start + mentionText.length
                lastMentionIndex = end
                //record all mention-string's position
                mRangeArrayList?.add(Range(start, end))
            }
        }

    }

    fun getRangeOfClosestMentionString(selStart: Int, selEnd: Int): Range? =
            mRangeArrayList?.firstOrNull { it.contains(selStart, selEnd) }

    private fun getRangeOfNearbyMentionString(selStart: Int, selEnd: Int): Range? =
            mRangeArrayList?.firstOrNull { it.isWrappedBy(selStart, selEnd) }

    //handle the deletion action for mention string, such as '@test'
    private inner class HackInputConnection(target: InputConnection, mutable: Boolean, editText: MentionEditText) : InputConnectionWrapper(target, mutable) {
        private val editText: EditText

        init {
            this.editText = editText
        }

        override fun sendKeyEvent(event: KeyEvent): Boolean {
            if (event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_DEL) {
                val selectionStart = editText.selectionStart
                val selectionEnd = editText.selectionEnd
                val closestRange = getRangeOfClosestMentionString(selectionStart, selectionEnd)
                if (closestRange == null) {
                    mIsSelected = false
                    return super.sendKeyEvent(event)
                }
                //if mention string has been selected or the cursor is at the beginning of mention string, just use default action(delete)
                if (mIsSelected || selectionStart == closestRange.from) {
                    mIsSelected = false
                    return super.sendKeyEvent(event)
                } else {
                    //select the mention string
                    mIsSelected = true
                    mLastSelectedRange = closestRange
                    setSelection(closestRange.to, closestRange.from)
                }
                return true
            }
            return super.sendKeyEvent(event)
        }

        override fun deleteSurroundingText(beforeLength: Int, afterLength: Int): Boolean {
            if (beforeLength == 1 && afterLength == 0) {
                return sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL)) && sendKeyEvent(KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL))
            } else if (beforeLength < 0 && afterLength == 0) {
                val selectionStart = editText.selectionStart
                val selectionEnd = editText.selectionEnd
                if (selectionStart == selectionEnd) {
                    setSelection(selectionStart - beforeLength, selectionStart - beforeLength)
                    super.deleteSurroundingText(-beforeLength, afterLength)
                }
            }
            return super.deleteSurroundingText(beforeLength, afterLength)
        }
    }

    //helper class to record the position of mention string in EditText
    inner class Range(internal var from: Int, internal var to: Int) {

        fun isWrappedBy(start: Int, end: Int): Boolean =
                start in (from + 1)..(to - 1) || end in (from + 1)..(to - 1)

        fun contains(start: Int, end: Int): Boolean = from <= start && to >= end

        fun isEqual(start: Int, end: Int): Boolean =
                from == start && to == end || from == end && to == start

        fun getAnchorPosition(value: Int): Int {
            return if (value - from - (to - value) >= 0) {
                to
            } else {
                from
            }
        }
    }

    /**
     * Listener for '@' character
     */
    interface OnMentionInputListener {
        /**
         * call when '@' character is inserted into EditText
         */
        fun onMentionCharacterInput()
    }

    companion object {
        //public static final String DEFAULT_MENTION_PATTERN = "@[\\u4e00-\\u9fa5\\w\\-]+";

        val DEFAULT_MENTION_PATTERN = "@[^(?!@)\\s]+?\\u0008"
        val TOPIC_MENTION_PATTERN = "#[^(?!@)\\s]+?#"
    }

}
