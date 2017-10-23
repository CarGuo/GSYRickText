package com.shuyu.textutillib

import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.DynamicDrawableSpan
import android.text.style.StyleSpan
import android.text.style.URLSpan
import android.text.util.Linkify


import com.shuyu.textutillib.listener.ITextViewShow
import com.shuyu.textutillib.listener.SpanAtUserCallBack
import com.shuyu.textutillib.listener.SpanTopicCallBack
import com.shuyu.textutillib.listener.SpanUrlCallBack
import com.shuyu.textutillib.model.TopicModel
import com.shuyu.textutillib.model.UserModel
import com.shuyu.textutillib.span.ClickAtUserSpan
import com.shuyu.textutillib.span.ClickTopicSpan
import com.shuyu.textutillib.span.LinkSpan

import java.util.HashMap
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * 通用处理问题的Utisl
 * Created by shuyu on 2016/11/10.
 */

object TextCommonUtils {
    /**
     * 单纯emoji表示
     *
     * @param context 上下文
     * @param text    包含emoji的字符串
     * @param tv      显示的textview
     */
    fun setEmojiText(context: Context, text: String, tv: ITextViewShow) {
        if (TextUtils.isEmpty(text)) {
            tv.text = ""
        }
        val spannable = SmileUtils.unicodeToEmojiName(context, text)
        tv.text = spannable
    }


    /**
     * 单纯获取emoji表示
     *
     * @param context           上下文
     * @param text              需要处理的文本
     * @param size              emoji大小
     * @param verticalAlignment 垂直方式
     * @return 返回显示的spananle
     */
    @JvmOverloads
    fun getEmojiText(context: Context, text: String, size: Int = -1, verticalAlignment: Int = DynamicDrawableSpan.ALIGN_BOTTOM): Spannable {
        return if (TextUtils.isEmpty(text)) {
            SpannableString("")
        } else SmileUtils.unicodeToEmojiName(context, text, size, verticalAlignment)

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
    fun getUrlEmojiText(context: Context, text: String, textView: ITextViewShow, color: Int, needNum: Boolean, spanAtUserCallBack: SpanAtUserCallBack, spanUrlCallBack: SpanUrlCallBack): Spannable {
        return if (!TextUtils.isEmpty(text)) {
            getUrlSmileText(context, text, null, textView, color, 0, needNum, spanAtUserCallBack, spanUrlCallBack)
        } else {
            SpannableString(" ")
        }
    }

    /**
     * 设置带高亮可点击的Url、表情、at某人的textview文本
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
    fun setUrlSmileText(context: Context, string: String, listUser: List<UserModel>, textView: ITextViewShow, color: Int, needNum: Boolean, spanAtUserCallBack: SpanAtUserCallBack, spanUrlCallBack: SpanUrlCallBack) {
        val spannable = getUrlSmileText(context, string, listUser, textView, color, 0, needNum, spanAtUserCallBack, spanUrlCallBack)
        textView.text = spannable
    }

    /**
     * AT某人的跳转
     *
     * @param context            上下文
     * @param listUser           需要显示的AT某人
     * @param content            需要处理的文本
     * @param textView           需要显示的view
     * @param clickable          AT某人是否可以点击
     * @param color              AT需要显示的颜色
     * @param topicColor         topic需要显示的颜色
     * @param spanAtUserCallBack AT某人点击的返回
     * @return 返回显示的spananle
     */
    fun getAtText(context: Context, listUser: List<UserModel>?, listTopic: List<TopicModel>?, content: String, textView: ITextViewShow?, clickable: Boolean,
                  color: Int, topicColor: Int, spanAtUserCallBack: SpanAtUserCallBack, spanTopicCallBack: SpanTopicCallBack?): Spannable {

        var spannable: Spannable? = null

        if (listTopic != null && listTopic.isNotEmpty()) {
            spannable = getTopicText(context, listTopic, content, textView, clickable, topicColor, spanTopicCallBack)
        }

        if ((listUser == null || listUser.isEmpty()) && spannable == null)
            return getEmojiText(context, content, textView!!.emojiSize())

        val spannableString = SpannableString(if (spannable == null) content else spannable)
        var indexStart = 0
        val lenght = content.length
        var hadHighLine = false
        val map = HashMap<String, String>()
        var i = 0
        while (i < listUser!!.size) {
            var index = content.indexOf("@" + listUser[i].user_name, indexStart) + 1
            if (index < 0 && indexStart > 0) {
                index = content.indexOf(listUser[i].user_name)
                if (map.containsKey("" + index)) {
                    val tmpIndexStart = if (indexStart < lenght) Integer.parseInt(map["" + index]) else lenght - 1
                    if (tmpIndexStart != indexStart) {
                        indexStart = tmpIndexStart
                        i--
                        i++
                        continue
                    }
                }
            }
            if (index > 0) {
                map.put(index.toString() + "", index.toString() + "")
                val mathStart = index - 1
                val indexEnd = index + listUser[i].user_name.length
                val hadAt = "@" == content.substring(mathStart, index)
                val matchEnd = indexEnd + 1
                if (hadAt && (matchEnd <= lenght || indexEnd == lenght)) {
                    if (indexEnd == lenght || " " == content.substring(indexEnd, indexEnd + 1) || "\b" == content.substring(indexEnd, indexEnd + 1)) {
                        if (indexEnd > indexStart) {
                            indexStart = indexEnd
                        }
                        hadHighLine = true
                        var clickAtUserSpan: ClickAtUserSpan? = null
                        if (textView != null) {
                            clickAtUserSpan = textView.getCustomClickAtUserSpan(context, listUser[i], color, spanAtUserCallBack)
                        }

                        if (clickAtUserSpan == null) {
                            clickAtUserSpan = ClickAtUserSpan(context, listUser[i], color, spanAtUserCallBack)
                        }

                        spannableString.setSpan(clickAtUserSpan, mathStart, if (indexEnd == lenght) lenght else matchEnd, Spanned.SPAN_MARK_POINT)

                    }
                }
            }
            i++
        }
        SmileUtils.addSmiles(context, textView!!.emojiSize(), textView.verticalAlignment(), spannableString)
        if (clickable && hadHighLine)
            textView.setMovementMethod(LinkMovementMethod.getInstance())
        return spannableString
    }

    /**
     * 话题span
     *
     * @param context           上下文
     * @param listTopic         需要的话题列表
     * @param content           需要处理的文本
     * @param textView          需要显示的view
     * @param clickable         是否可以点击
     * @param color             颜色
     * @param spanTopicCallBack 点击回调
     * @return Spannable
     */
    fun getTopicText(context: Context, listTopic: List<TopicModel>?, content: String, textView: ITextViewShow?, clickable: Boolean,
                     color: Int, spanTopicCallBack: SpanTopicCallBack?): Spannable {

        if (listTopic == null || listTopic.isEmpty())
            return SpannableString(content)
        val spannableString = SpannableString(content)
        var indexStart = 0
        val lenght = content.length
        var hadHighLine = false
        val map = HashMap<String, String>()
        var i = 0
        while (i < listTopic.size) {
            var index = content.indexOf("#" + listTopic[i].topicName + "#", indexStart) + 1
            if (index < 0 && indexStart > 0) {
                index = content.indexOf(listTopic[i].topicName)
                if (map.containsKey("" + index)) {
                    val tmpIndexStart = if (indexStart < lenght) Integer.parseInt(map["" + index]) else lenght - 1
                    if (tmpIndexStart != indexStart) {
                        indexStart = tmpIndexStart
                        i--
                        i++
                        continue
                    }
                }
            }
            if (index > 0) {
                map.put(index.toString() + "", index.toString() + "")
                val mathStart = index - 1
                val indexEnd = index + listTopic[i].topicName.length
                val hadAt = "#" == content.substring(mathStart, index) && "#" == content.substring(indexEnd, indexEnd + 1)
                val matchEnd = indexEnd + 1
                if (hadAt && (matchEnd <= lenght || indexEnd == lenght)) {
                    if (indexEnd > indexStart) {
                        indexStart = indexEnd
                    }
                    hadHighLine = true
                    var clickTopicSpan: ClickTopicSpan? = null
                    if (textView != null) {
                        clickTopicSpan = textView.getCustomClickTopicSpan(context, listTopic[i], color, spanTopicCallBack!!)
                    }
                    if (clickTopicSpan == null) {
                        clickTopicSpan = ClickTopicSpan(context, listTopic[i], color, spanTopicCallBack)
                    }
                    spannableString.setSpan(clickTopicSpan, mathStart, if (indexEnd == lenght) lenght else matchEnd, Spanned.SPAN_MARK_POINT)
                }
            }
            i++
        }
        if (clickable && hadHighLine)
            textView!!.setMovementMethod(LinkMovementMethod.getInstance())
        return spannableString
    }


    /**
     * 设置带高亮可点击的Url、表情的textview文本、AT某人
     *
     * @param context            上下文
     * @param string             需要处理的文本
     * @param listUser           需要显示的AT某人
     * @param textView           需要显示的view
     * @param colorAt            需要显示的颜色
     * @param colorLink          需要显示的颜色
     * @param needNum            是否需要显示号码
     * @param spanAtUserCallBack AT某人点击的返回
     * @param spanUrlCallBack    链接点击的返回
     * @return 返回显示的spananle
     */
    fun getUrlSmileText(context: Context, string: String, listUser: List<UserModel>?, textView: ITextViewShow, colorAt: Int, colorLink: Int, needNum: Boolean, spanAtUserCallBack: SpanAtUserCallBack, spanUrlCallBack: SpanUrlCallBack): Spannable =
            getAllSpanText(context, string, listUser, null, textView, colorAt, colorLink, 0, needNum, true, spanAtUserCallBack, spanUrlCallBack, null)

    /**
     * 设置带高亮可点击的Url、表情的textview文本、AT某人、话题
     *
     * @param context            上下文
     * @param string             需要处理的文本
     * @param listUser           需要显示的AT某人
     * @param listTopic          需要的话题列表
     * @param textView           需要显示的view
     * @param colorAt            需要显示的颜色
     * @param colorLink          需要显示的颜色
     * @param colorTopic         需要显示的颜色
     * @param needNum            是否需要显示号码
     * @param needUrl            是否需要显示url
     * @param spanAtUserCallBack AT某人点击的返回
     * @param spanUrlCallBack    链接点击的返回
     * @param spanTopicCallBack  话题点击的返回
     * @return 返回显示的spananle
     */
    fun getAllSpanText(context: Context, string: String, listUser: List<UserModel>?, listTopic: List<TopicModel>?, textView: ITextViewShow, colorAt: Int, colorLink: Int, colorTopic: Int, needNum: Boolean, needUrl: Boolean, spanAtUserCallBack: SpanAtUserCallBack, spanUrlCallBack: SpanUrlCallBack, spanTopicCallBack: SpanTopicCallBack?): Spannable {
        var stringCur = string
        if (needUrl || needNum) {
            textView.setAutoLinkMask(Linkify.WEB_URLS or Linkify.PHONE_NUMBERS)
        }
        return if (!TextUtils.isEmpty(stringCur)) {
            stringCur = string.replace("\r".toRegex(), "\r\n")
            val spannable = getAtText(context, listUser, listTopic, stringCur, textView, true, colorAt, colorTopic, spanAtUserCallBack, spanTopicCallBack)
            textView.text = spannable
            if (needUrl || needNum) {
                resolveUrlLogic(context, textView, spannable, colorLink, needNum, needUrl, spanUrlCallBack)
            } else {
                spannable
            }
        } else {
            SpannableString(" ")
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
     * @param needUrl         是否需要显示url
     * @param spanUrlCallBack 链接点击的返回
     * @return 返回显示的spananle
     */
    private fun resolveUrlLogic(context: Context, textView: ITextViewShow?, spannable: Spannable, color: Int, needNum: Boolean, needUrl: Boolean, spanUrlCallBack: SpanUrlCallBack): Spannable {
        val charSequence = textView?.text
        if (charSequence is Spannable) {
            val end = charSequence.length
            val sp = textView.text as Spannable
            val urls = sp.getSpans<URLSpan>(0, end, URLSpan::class.java)
            val atSpan = sp.getSpans<ClickAtUserSpan>(0, end, ClickAtUserSpan::class.java)
            if (urls.isNotEmpty()) {
                val style = SpannableStringBuilder(charSequence)
                style.clearSpans()// should clear old spans
                for (url in urls) {
                    val urlString = url.url
                    if (isNumeric(urlString.replace("tel:", ""))) {
                        if (!needNum && !isMobileSimple(urlString.replace("tel:", ""))) {
                            style.setSpan(StyleSpan(Typeface.NORMAL), sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
                        } else {
                            var linkSpan: LinkSpan? = textView.getCustomLinkSpan(context, url.url, color, spanUrlCallBack)
                            if (linkSpan == null) {
                                linkSpan = LinkSpan(context, url.url, color, spanUrlCallBack)
                            }

                            style.setSpan(linkSpan, sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
                        }
                    } else if (needUrl && isTopURL(urlString.toLowerCase())) {
                        var linkSpan: LinkSpan? = textView.getCustomLinkSpan(context, url.url, color, spanUrlCallBack)
                        if (linkSpan == null) {
                            linkSpan = LinkSpan(context, url.url, color, spanUrlCallBack)
                        }
                        style.setSpan(linkSpan, sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
                    } else {
                        style.setSpan(StyleSpan(Typeface.NORMAL), sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
                    }

                }
                for (atUserSpan in atSpan) {
                    //剔除话题和at某人中的link span
                    val removeUrls = style.getSpans<LinkSpan>(sp.getSpanStart(atUserSpan), sp.getSpanEnd(atUserSpan), LinkSpan::class.java)
                    if (removeUrls != null && removeUrls.isNotEmpty()) {
                        for (linkSpan in removeUrls) {
                            style.removeSpan(linkSpan)
                        }
                    }
                    style.setSpan(atUserSpan, sp.getSpanStart(atUserSpan), sp.getSpanEnd(atUserSpan), Spanned.SPAN_MARK_POINT)
                }
                SmileUtils.addSmiles(context, textView.emojiSize(), textView.verticalAlignment(), style)
                textView.setAutoLinkMask(0)
                return style
            } else {
                return spannable
            }
        } else {
            return spannable
        }
    }


    /**
     * 顶级域名判断；如果要忽略大小写，可以直接在传入参数的时候toLowerCase()再做判断
     * 处理1. 2. 3.识别成链接的问题
     *
     * @param str
     * @return 是否符合url
     */
    private fun isTopURL(str: String): Boolean {
        val ss = str.split("\\.".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
        return ss.size >= 3

    }

    /**
     * 是否数字
     *
     * @param str
     * @return 是否数字
     */
    private fun isNumeric(str: String): Boolean {
        val pattern = Pattern.compile("[0-9]*")
        val isNum = pattern.matcher(str)
        return isNum.matches()
    }

    /**
     * @param string 待验证文本
     * @return 是否符合手机号（简单）格式
     */
    private fun isMobileSimple(string: String): Boolean {
        val phone = "^[1]\\d{10}$"
        return !TextUtils.isEmpty(string) && Pattern.matches(phone, string)
    }


}
