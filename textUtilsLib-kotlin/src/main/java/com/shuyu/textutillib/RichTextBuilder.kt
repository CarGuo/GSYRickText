package com.shuyu.textutillib

import android.content.Context
import android.graphics.Color
import android.text.Spannable
import android.text.method.MovementMethod
import android.text.style.DynamicDrawableSpan
import android.widget.TextView

import com.shuyu.textutillib.listener.ITextViewShow
import com.shuyu.textutillib.listener.SpanAtUserCallBack
import com.shuyu.textutillib.listener.SpanCreateListener
import com.shuyu.textutillib.listener.SpanTopicCallBack
import com.shuyu.textutillib.listener.SpanUrlCallBack
import com.shuyu.textutillib.model.TopicModel
import com.shuyu.textutillib.model.UserModel
import com.shuyu.textutillib.span.ClickAtUserSpan
import com.shuyu.textutillib.span.ClickTopicSpan
import com.shuyu.textutillib.span.LinkSpan

/**
 * 富文本设置 话题、at某人，链接识别
 * Created by guoshuyu on 2017/8/17.
 */

class RichTextBuilder(private val context: Context?) {
    private var content = ""
    private var listUser: List<UserModel>? = ArrayList()
    private var listTopic: List<TopicModel>? = ArrayList()
    private var textView: TextView? = null
    private var spanAtUserCallBack: SpanAtUserCallBack? = null
    private var spanUrlCallBack: SpanUrlCallBack? = null
    private var spanTopicCallBack: SpanTopicCallBack? = null
    private var spanCreateListener: SpanCreateListener? = null
    private var atColor = Color.BLUE
    private var topicColor = Color.BLUE
    private var linkColor = Color.BLUE
    private var emojiSize = 0
    private var verticalAlignment = DynamicDrawableSpan.ALIGN_BOTTOM
    private var needNum = false
    private var needUrl = false

    /**
     * 文本内容
     */
    fun setContent(content: String?): RichTextBuilder {
        if (content != null) {
            this.content = content
        }
        return this
    }

    /**
     * at某人的list
     */
    fun setListUser(listUser: List<UserModel>?): RichTextBuilder {
        this.listUser = listUser
        return this
    }

    /**
     * 话题list
     */
    fun setListTopic(listTopic: List<TopicModel>?): RichTextBuilder {
        this.listTopic = listTopic
        return this
    }

    /**
     * 显示文本view
     */
    fun setTextView(textView: TextView): RichTextBuilder {
        this.textView = textView
        return this
    }

    /**
     * at某人显示颜色
     */
    fun setAtColor(atColor: Int): RichTextBuilder {
        this.atColor = atColor
        return this
    }

    /**
     * 话题显示颜色
     */
    fun setTopicColor(topicColor: Int): RichTextBuilder {
        this.topicColor = topicColor
        return this
    }

    /**
     * 链接显示颜色
     */
    fun setLinkColor(linkColor: Int): RichTextBuilder {
        this.linkColor = linkColor
        return this
    }

    /**
     * 是否需要识别电话
     */
    fun setNeedNum(needNum: Boolean): RichTextBuilder {
        this.needNum = needNum
        return this
    }

    fun setNeedUrl(needUrl: Boolean): RichTextBuilder {
        this.needUrl = needUrl
        return this
    }

    /**
     * at某人点击回调
     */
    fun setSpanAtUserCallBack(spanAtUserCallBack: SpanAtUserCallBack): RichTextBuilder {
        this.spanAtUserCallBack = spanAtUserCallBack
        return this
    }

    /**
     * url点击回调
     */
    fun setSpanUrlCallBack(spanUrlCallBack: SpanUrlCallBack): RichTextBuilder {
        this.spanUrlCallBack = spanUrlCallBack
        return this
    }

    /**
     * 话题点击回调
     */
    fun setSpanTopicCallBack(spanTopicCallBack: SpanTopicCallBack): RichTextBuilder {
        this.spanTopicCallBack = spanTopicCallBack
        return this
    }

    /**
     * emoji大小，不设置默认图片大小
     */
    fun setEmojiSize(emojiSize: Int): RichTextBuilder {
        this.emojiSize = emojiSize
        return this
    }

    /**
     * emoji垂直
     */
    fun setVerticalAlignment(verticalAlignment: Int): RichTextBuilder {
        this.verticalAlignment = verticalAlignment
        return this
    }

    /**
     * 自定义span回调，如果不需要可不设置
     */
    fun setSpanCreateListener(spanCreateListener: SpanCreateListener?): RichTextBuilder {
        this.spanCreateListener = spanCreateListener
        return this
    }

    fun buildSpan(iTextViewShow: ITextViewShow): Spannable {
        if (context == null) {
            throw IllegalStateException("context could not be null.")
        }

        return TextCommonUtils.getAllSpanText(
                context,
                content,
                listUser,
                listTopic,
                iTextViewShow,
                atColor,
                linkColor,
                topicColor,
                needNum,
                needUrl,
                spanAtUserCallBack!!,
                spanUrlCallBack!!,
                spanTopicCallBack)
    }


    fun build() {

        if (context == null) {
            throw IllegalStateException("context could not be null.")
        }

        if (textView == null) {
            throw IllegalStateException("textView could not be null.")
        }

        val iTextViewShow = object : ITextViewShow {

            override var text: CharSequence
                get() = textView!!.text
                set(charSequence) {
                    textView!!.text = charSequence
                }

            override fun setMovementMethod(movementMethod: MovementMethod) {
                textView?.movementMethod = movementMethod
            }

            override fun setAutoLinkMask(flag: Int) {
                textView?.autoLinkMask = flag
            }


            override fun getCustomClickAtUserSpan(context: Context, userModel: UserModel, color: Int, spanClickCallBack: SpanAtUserCallBack): ClickAtUserSpan? =
                    spanCreateListener?.getCustomClickAtUserSpan(context, userModel, color, spanClickCallBack)

            override fun getCustomClickTopicSpan(context: Context, topicModel: TopicModel, color: Int, spanTopicCallBack: SpanTopicCallBack): ClickTopicSpan? =
                    spanCreateListener?.getCustomClickTopicSpan(context, topicModel, color, spanTopicCallBack)

            override fun getCustomLinkSpan(context: Context, url: String, color: Int, spanUrlCallBack: SpanUrlCallBack): LinkSpan? =
                    spanCreateListener?.getCustomLinkSpan(context, url, color, spanUrlCallBack)

            override fun emojiSize(): Int = emojiSize

            override fun verticalAlignment(): Int = verticalAlignment
        }

        val spannable = TextCommonUtils.getAllSpanText(
                context,
                content,
                listUser,
                listTopic,
                iTextViewShow,
                atColor,
                linkColor,
                topicColor,
                needNum,
                needUrl,
                spanAtUserCallBack!!,
                spanUrlCallBack!!,
                spanTopicCallBack)
        textView?.text = spannable
    }
}
