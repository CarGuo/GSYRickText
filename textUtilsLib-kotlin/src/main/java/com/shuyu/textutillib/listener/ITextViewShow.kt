package com.shuyu.textutillib.listener

import android.content.Context
import android.text.method.MovementMethod

import com.shuyu.textutillib.model.TopicModel
import com.shuyu.textutillib.model.UserModel
import com.shuyu.textutillib.span.ClickAtUserSpan
import com.shuyu.textutillib.span.ClickTopicSpan
import com.shuyu.textutillib.span.LinkSpan

/**
 * textview 显示接口
 * Created by guoshuyu on 2017/8/22.
 */

open interface ITextViewShow {

    var text: CharSequence

    fun setMovementMethod(movementMethod: MovementMethod)

    fun setAutoLinkMask(flag: Int)

    fun getCustomClickAtUserSpan(context: Context, userModel: UserModel, color: Int, spanClickCallBack: SpanAtUserCallBack): ClickAtUserSpan?

    fun getCustomClickTopicSpan(context: Context, topicModel: TopicModel, color: Int, spanTopicCallBack: SpanTopicCallBack): ClickTopicSpan?

    fun getCustomLinkSpan(context: Context, url: String, color: Int, spanUrlCallBack: SpanUrlCallBack): LinkSpan?

    fun emojiSize(): Int

    fun verticalAlignment(): Int
}
