package com.shuyu.textutillib

/**
 * 表情工具类
 * Created by shuyu on 2016/11/14.
 */

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.CharacterStyle
import android.text.style.ForegroundColorSpan
import android.widget.EditText


import com.shuyu.textutillib.span.CenteredImageSpan

import java.util.ArrayList
import java.util.HashMap
import java.util.regex.Matcher
import java.util.regex.Pattern

import android.text.style.DynamicDrawableSpan.ALIGN_BOTTOM
import com.shuyu.textutillib.SmileUtils.Companion.getSmiledText

class SmileUtils {

    val emotions: HashMap<Pattern, Int>
        get() = emoticons

    companion object {

        private val spannableFactory = Spannable.Factory.getInstance()

        val emoticons: HashMap<Pattern, Int> = HashMap()

        private val textList = ArrayList<String>()

        /**
         * 添加到map
         *
         * @param map      map
         * @param smile    文本
         * @param resource 显示图片表情列表
         */
        private fun addPattern(map: HashMap<Pattern, Int>, smile: String,
                               resource: Int) {
            map.put(Pattern.compile(Pattern.quote(smile)), resource)
        }

        /**
         * 使用你自己的map
         *
         * @param map      map
         * @param smile    文本列表
         * @param resource 显示图片表情列表
         */
        fun addPatternAll(map: MutableMap<Pattern, Int>, smile: List<String>,
                          resource: List<Int>) {

            map.clear()
            textList.clear()
            if (smile.size != resource.size) {
                try {
                    throw Exception("**********文本与图片list不相等")
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                return
            }
            textList.addAll(smile)
            for (i in smile.indices) {
                map.put(Pattern.compile(Pattern.quote(smile[i])), resource[i])
            }
        }

        /***
         * 文本对应的资源
         *
         * @param string 需要转化文本
         * @return
         */
        fun getRedId(string: String): Int {
            for ((key, value) in emoticons) {
                val matcher = key.matcher(string)
                while (matcher.find()) {
                    return value
                }
            }
            return -1
        }

        /**
         * 文本转化表情处理
         *
         * @param editText  要显示的EditText
         * @param maxLength 最长高度
         * @param size      显示大小
         * @param name      需要转化的文本
         */
        fun insertIcon(editText: EditText, maxLength: Int, size: Int, name: String) {

            val curString = editText.toString()
            if (curString.length + name.length > maxLength) {
                return
            }

            val resId = SmileUtils.getRedId(name)

            val drawable = editText.resources.getDrawable(resId) ?: return

            drawable.setBounds(0, 0, size, size)//这里设置图片的大小
            val centeredImageSpan = CenteredImageSpan(drawable)
            val spannableString = SpannableString(name)
            spannableString.setSpan(centeredImageSpan, 0, spannableString.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)


            val index = Math.max(editText.selectionStart, 0)
            val spannableStringBuilder = SpannableStringBuilder(editText.text)
            spannableStringBuilder.insert(index, spannableString)

            editText.setText(spannableStringBuilder.toString())
            editText.setSelection(index + spannableString.length)
        }


        /**
         * replace existing spannable with smiles
         *
         * @param context   上下文
         * @param spannable 显示的span
         * @return 是否添加
         */
        fun addSmiles(context: Context, spannable: Spannable): Boolean =
                addSmiles(context, -1, spannable)

        /**
         * replace existing spannable with smiles
         *
         * @param context   上下文
         * @param size      大小
         * @param spannable 显示的span
         * @return 是否添加
         */
        fun addSmiles(context: Context, size: Int, spannable: Spannable): Boolean =
                addSmiles(context, size, ALIGN_BOTTOM, spannable)


        /**
         * replace existing spannable with smiles
         *
         * @param context           上下文
         * @param size              大小
         * @param spannable         显示的span
         * @param verticalAlignment 垂直方向
         * @return 是否添加
         */
        fun addSmiles(context: Context, size: Int, verticalAlignment: Int, spannable: Spannable): Boolean {
            var hasChanges = false
            for ((key, value) in emoticons) {
                val matcher = key.matcher(spannable)
                while (matcher.find()) {
                    var set = true
                    for (span in spannable.getSpans<CenteredImageSpan>(matcher.start(),
                            matcher.end(), CenteredImageSpan::class.java))
                        if (spannable.getSpanStart(span) >= matcher.start() && spannable.getSpanEnd(span) <= matcher.end())
                            spannable.removeSpan(span)
                        else {
                            set = false
                            break
                        }
                    if (set) {
                        hasChanges = true
                        if (size <= 0) {
                            spannable.setSpan(CenteredImageSpan(context, value, verticalAlignment),
                                    matcher.start(), matcher.end(),
                                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                        } else {
                            val drawable = context.resources.getDrawable(value)
                            if (drawable != null) {
                                drawable.setBounds(0, 0, size, size)//这里设置图片的大小
                                val centeredImageSpan = CenteredImageSpan(drawable, verticalAlignment)
                                spannable.setSpan(centeredImageSpan,
                                        matcher.start(), matcher.end(),
                                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                            }
                        }
                    }
                }
            }
            return hasChanges
        }

        @JvmOverloads
        fun getSmiledText(context: Context, text: CharSequence, size: Int = -1, verticalAlignment: Int = ALIGN_BOTTOM): Spannable {
            val spannable = spannableFactory.newSpannable(text)
            addSmiles(context, size, verticalAlignment, spannable)
            return spannable
        }

        fun containsKey(key: String): Boolean {
            var b = false
            for ((key1) in emoticons) {
                val matcher = key1.matcher(key)
                if (matcher.find()) {
                    b = true
                    break
                }
            }

            return b
        }

        fun stringToUnicode(string: String): String {

            val unicode = StringBuffer()

            (0 until string.length)
                    .map {
                        // 取出每一个字符
                        string[it]

                        // 转换为unicode
                    }
                    .forEach { unicode.append("\\u" + String.format("%04", Integer.toHexString(it.toInt()))) }

            return "[" + unicode.toString() + "]"
        }

        fun unicode2String(unicode: String): String {

            val string = StringBuffer()

            val hex = unicode.split("\\\\u".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()

            (1 until hex.size)
                    .map {
                        // 转换出每一个代码点
                        Integer.parseInt(hex[it], 16)

                        // 追加成string
                    }.forEach { string.append(it.toChar()) }

            return string.toString()
        }

        var specials = arrayOf("\\", "\\/", "*", ".", "?", "+", "$", "^", "[", "]", "(", ")", "{", "}", "|")

        fun highlight(text: String, targetT: String): SpannableStringBuilder {
            var target = targetT
            val spannable = SpannableStringBuilder(text)
            var span: CharacterStyle? = null
            specials.indices
                    .asSequence()
                    .filter { target.contains(specials[it]) }
                    .forEach { target = target.replace(specials[it], "\\" + specials[it]) }
            val p = Pattern.compile(target.toLowerCase())
            val m = p.matcher(text.toLowerCase())
            while (m.find()) {
                span = ForegroundColorSpan(Color.rgb(253, 113, 34))// 需要重复！
                spannable.setSpan(span, m.start(), m.end(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            return spannable
        }

        fun highlight(text: Spannable, target: String): SpannableStringBuilder {
            val spannable = SpannableStringBuilder(text)
            var span: CharacterStyle? = null
            val p = Pattern.compile(target)
            val m = p.matcher(text)
            while (m.find()) {
                span = ForegroundColorSpan(Color.rgb(253, 113, 34))// 需要重复！
                spannable.setSpan(span, m.start(), m.end(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            return spannable
        }

        fun highlight(text: String): SpannableStringBuilder {
            val spannable = SpannableStringBuilder(text)
            var span: CharacterStyle? = null
            span = ForegroundColorSpan(Color.rgb(253, 113, 34))// 需要重复！
            spannable.setSpan(span, 0, text.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            return spannable
        }

        fun unicodeToEmojiName(context: Context, content: String, size: Int, verticalAlignment: Int): Spannable =
                getSmiledText(context, content, size, verticalAlignment)

        fun unicodeToEmojiName(context: Context, content: String, size: Int): Spannable =
                getSmiledText(context, content, size)

        fun unicodeToEmojiName(context: Context, content: String): Spannable =
                getSmiledText(context, content, -1)

        fun getTextList(): List<String> = textList
    }
}
