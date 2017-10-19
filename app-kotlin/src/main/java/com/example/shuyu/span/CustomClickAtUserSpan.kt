package com.example.shuyu.span

import android.content.Context
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.PathEffect
import android.text.TextPaint

import com.shuyu.textutillib.listener.SpanAtUserCallBack
import com.shuyu.textutillib.model.UserModel
import com.shuyu.textutillib.span.ClickAtUserSpan

/**
 * 自定义显示可点击的@某人
 * Created by shuyu on 2016/11/10.
 */
open class CustomClickAtUserSpan(context: Context, userModel: UserModel, color: Int, spanClickCallBack: SpanAtUserCallBack) : ClickAtUserSpan(context, userModel, color, spanClickCallBack) {

    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        ds.isUnderlineText = true
        //间隔线
        ds.style = Paint.Style.STROKE
        val effects = DashPathEffect(floatArrayOf(1f, 1f), 1f)
        ds.pathEffect = effects
        ds.strokeWidth = 5f
    }

}