package com.shuyu.textutillib

import android.content.Context
import android.graphics.Color
import android.text.DynamicLayout
import android.text.StaticLayout
import android.text.style.DynamicDrawableSpan
import android.util.AttributeSet
import android.view.View
import android.widget.TextView

import com.shuyu.textutillib.listener.SpanAtUserCallBack
import com.shuyu.textutillib.listener.SpanCreateListener
import com.shuyu.textutillib.listener.SpanTopicCallBack
import com.shuyu.textutillib.listener.SpanUrlCallBack
import com.shuyu.textutillib.model.TopicModel
import com.shuyu.textutillib.model.UserModel
import java.lang.reflect.Field

import java.util.ArrayList

/**
 * Created by guoshuyu on 2017/8/28.
 */

class RichTextView : TextView {

    /**
     * 设置话题列表
     */
    var topicList: List<TopicModel> = ArrayList()

    /**
     * 设置at列表
     */
    var nameList: List<UserModel> = ArrayList()

    /**
     * at某人颜色
     */
    var atColor = Color.BLUE

    /**
     * 话题颜色
     */
    var topicColor = Color.BLUE

    /**
     * 链接颜色
     */
    var linkColor = Color.BLUE

    private var emojiSize = 0

    private var spanUrlCallBackListener: SpanUrlCallBack? = null

    private var spanAtUserCallBackListener: SpanAtUserCallBack? = null

    private var spanTopicCallBackListener: SpanTopicCallBack? = null

    private var spanCreateListener: SpanCreateListener? = null

    /**
     * 是否需要处理数字
     *
     * @param needNumberShow 是否需要高亮数字和点击
     */
    var isNeedNumberShow = true//是否需要数字处理

    /**
     * 是否需要处理数字
     *
     * @param needUrlShow 是否需要高亮url和点击
     */
    var isNeedUrlShow = true//是否需要url处理

    /**
     * emoji垂直
     */
    var emojiVerticalAlignment = DynamicDrawableSpan.ALIGN_BOTTOM//垂直方式

    private val spanUrlCallBack = object : SpanUrlCallBack {
        override fun phone(view: View, phone: String) {
            spanUrlCallBackListener?.phone(view, phone)
        }

        override fun url(view: View, url: String) {
            spanUrlCallBackListener?.url(view, url)
        }
    }

    private val spanAtUserCallBack = object : SpanAtUserCallBack{
        override fun onClick(view: View, userModel1: UserModel) {
            spanAtUserCallBackListener?.onClick(view, userModel1)
        }
    }


    private val spanTopicCallBack = object : SpanTopicCallBack{
        override fun onClick(view: View, topicModel: TopicModel) {
            spanTopicCallBackListener?.onClick(view, topicModel)
        }
    }

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        if (isInEditMode)
            return

        if (attrs != null) {
            val array = context.obtainStyledAttributes(attrs, R.styleable.RichTextView)
            isNeedNumberShow = array.getBoolean(R.styleable.RichTextView_needNumberShow, false)
            isNeedUrlShow = array.getBoolean(R.styleable.RichTextView_needUrlShow, false)
            atColor = array.getColor(R.styleable.RichTextView_atColor, Color.BLUE)
            topicColor = array.getColor(R.styleable.RichTextView_topicColor, Color.BLUE)
            linkColor = array.getColor(R.styleable.RichTextView_linkColor, Color.BLUE)
            emojiSize = array.getInteger(R.styleable.RichTextView_emojiSize, 0)
            emojiVerticalAlignment = array.getInteger(R.styleable.RichTextView_emojiVerticalAlignment, DynamicDrawableSpan.ALIGN_BOTTOM)
            array.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var layout: StaticLayout? = null
        var field: Field? = null
        try {
            val staticField = DynamicLayout::class.java.getDeclaredField("sStaticLayout")
            staticField.isAccessible = true
            layout = staticField.get(DynamicLayout::class.java) as StaticLayout
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }

        if (layout != null) {
            try {
                field = StaticLayout::class.java.getDeclaredField("mMaximumVisibleLineCount")
                field!!.isAccessible = true
                field.setInt(layout, maxLines)
            } catch (e: NoSuchFieldException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }

        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (layout != null && field != null) {
            try {
                field.setInt(layout, Integer.MAX_VALUE)
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }

        }
    }

    /**
     * 显示处理文本
     *
     * @param content
     */
    private fun resolveRichShow(content: String?) {
        val richTextBuilder = RichTextBuilder(context)
        richTextBuilder.setContent(content)
                .setAtColor(atColor)
                .setLinkColor(linkColor)
                .setTopicColor(topicColor)
                .setListUser(nameList)
                .setListTopic(topicList)
                .setNeedNum(isNeedNumberShow)
                .setNeedUrl(isNeedUrlShow)
                .setTextView(this)
                .setEmojiSize(emojiSize)
                .setSpanAtUserCallBack(spanAtUserCallBack)
                .setSpanUrlCallBack(spanUrlCallBack)
                .setSpanTopicCallBack(spanTopicCallBack)
                .setVerticalAlignment(emojiVerticalAlignment)
                .setSpanCreateListener(spanCreateListener)
                .build()

    }

    /**
     * 设置@某人文本
     *
     * @param text     文本
     * @param nameList @人列表
     */
    fun setRichTextUser(text: String, nameList: List<UserModel>) {
        this.setRichText(text, nameList, topicList)
    }

    /**
     * 设置话题文本
     *
     * @param text      文本
     * @param topicList 话题列表
     */
    fun setRichTextTopic(text: String, topicList: List<TopicModel>) {
        this.setRichText(text, nameList, topicList)
    }

    /**
     * 设置话题和@文本
     *
     * @param text      文本
     * @param nameList  @人列表
     * @param topicList 话题列表
     */
    @JvmOverloads
    fun setRichText(text: String?, nameList: List<UserModel>? = null, topicList: List<TopicModel>? = null) {
        if (nameList != null) {
            this.nameList = nameList
        }
        if (topicList != null) {
            this.topicList = topicList
        }
        resolveRichShow(text)
    }

    /**
     * url点击
     */
    fun setSpanUrlCallBackListener(spanUrlCallBackListener: SpanUrlCallBack) {
        this.spanUrlCallBackListener = spanUrlCallBackListener
    }

    /**
     * at某人点击
     */
    fun setSpanAtUserCallBackListener(spanAtUserCallBackListener: SpanAtUserCallBack) {
        this.spanAtUserCallBackListener = spanAtUserCallBackListener
    }

    /**
     * 设置自定义span回调
     */
    fun setSpanCreateListener(spanCreateListener: SpanCreateListener) {
        this.spanCreateListener = spanCreateListener
    }

    /**
     * 话题点击
     */
    fun setSpanTopicCallBackListener(spanTopicCallBackListener: SpanTopicCallBack) {
        this.spanTopicCallBackListener = spanTopicCallBackListener
    }


    fun setEmojiSize(emojiSize: Int) {
        this.emojiSize = emojiSize
    }

}
