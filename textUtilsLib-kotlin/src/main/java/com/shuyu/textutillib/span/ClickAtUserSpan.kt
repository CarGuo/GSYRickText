package com.shuyu.textutillib.span

import android.content.Context
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View

import com.shuyu.textutillib.listener.SpanAtUserCallBack
import com.shuyu.textutillib.model.UserModel

/**
 * 显示可点击的@某人
 * Created by shuyu on 2016/11/10.
 */
open class ClickAtUserSpan(val context: Context, val userModel: UserModel, val color: Int, val spanClickCallBack: SpanAtUserCallBack?) : ClickableSpan() {

    override fun onClick(view: View) {
        spanClickCallBack?.onClick(view, userModel)
    }

    override fun updateDrawState(ds: TextPaint) {
        /** 给文字染色  */
        ds.color = color
        /** 去掉下划线 ， 默认自带下划线  */
        ds.isUnderlineText = false
    }

}