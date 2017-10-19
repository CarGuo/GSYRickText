package com.shuyu.textutillib.span


import android.content.Context
import android.text.TextPaint
import android.text.TextUtils
import android.text.style.ClickableSpan
import android.view.View

import com.shuyu.textutillib.listener.SpanUrlCallBack

/**
 * 显示可点击的手机号码和URL
 * Created by shuyu on 2016/11/10.
 */
open class LinkSpan(private val context: Context, private val url: String, private val color: Int, private val spanUrlCallBack: SpanUrlCallBack?) : ClickableSpan() {

    override fun onClick(widget: View) {
        if (url.contains("tel:") && TextUtils.isDigitsOnly(url.replace("tel:", "")) || TextUtils.isDigitsOnly(url)) {
            spanUrlCallBack?.phone(widget, url)
        } else {
            spanUrlCallBack?.url(widget, url)
        }
    }


    override fun updateDrawState(ds: TextPaint) {
        ds.color = color
        /** 去掉下划线 ， 默认自带下划线  */
        ds.isUnderlineText = false
    }
}