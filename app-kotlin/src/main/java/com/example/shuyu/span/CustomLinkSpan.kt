package com.example.shuyu.span


import android.content.Context
import android.graphics.Typeface
import android.text.TextPaint

import com.shuyu.textutillib.listener.SpanUrlCallBack
import com.shuyu.textutillib.span.LinkSpan

/**
 * 自定义显示可点击的手机号码和URL
 * Created by shuyu on 2016/11/10.
 */
open class CustomLinkSpan(context: Context, url: String, color: Int, spanUrlCallBack: SpanUrlCallBack) : LinkSpan(context, url, color, spanUrlCallBack) {

    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        ds.isUnderlineText = true
        ds.typeface = Typeface.DEFAULT_BOLD
    }
}