package com.shuyu.textutillib.span

import android.content.Context
import android.view.View

import com.shuyu.textutillib.listener.SpanTopicCallBack
import com.shuyu.textutillib.model.TopicModel
import com.shuyu.textutillib.model.UserModel

/**
 * 话题#点击回调
 * Created by guoshuyu on 2017/8/16.
 */

open class ClickTopicSpan(context: Context, private val topicModel: TopicModel, color: Int, private val spanTopicCallBack: SpanTopicCallBack?) : ClickAtUserSpan(context, UserModel(), color, null) {

    override fun onClick(view: View) {
        super.onClick(view)
        spanTopicCallBack?.onClick(view, topicModel)
    }
}
