package com.example.shuyu.span

import android.content.Context
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.PathEffect
import android.text.TextPaint

import com.shuyu.textutillib.listener.SpanTopicCallBack
import com.shuyu.textutillib.model.TopicModel
import com.shuyu.textutillib.span.ClickTopicSpan

/**
 * 自定义话题#点击回调
 * Created by guoshuyu on 2017/8/16.
 */

open class CustomClickTopicSpan(context: Context, topicModel: TopicModel, color: Int, spanTopicCallBack: SpanTopicCallBack) : ClickTopicSpan(context, topicModel, color, spanTopicCallBack) {

    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        ds.isUnderlineText = true
        ds.style = Paint.Style.FILL_AND_STROKE
        val effects = DashPathEffect(floatArrayOf(1f, 1f), 1f)
        ds.pathEffect = effects
        ds.strokeWidth = 2f
    }
}
