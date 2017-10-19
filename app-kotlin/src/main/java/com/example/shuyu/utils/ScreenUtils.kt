package com.example.shuyu.utils

import android.content.Context

/**
 * Created by guoshuyu on 2017/10/19.
 */
object ScreenUtils {

    fun dip2px(context: Context, dipValue: Float): Float {
        val fontScale = context.resources.displayMetrics.density
        return (dipValue* fontScale+ 0.5f)
    }

}