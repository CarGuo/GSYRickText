package com.shuyu.textutillib.listener

import android.view.View

/**
 * url被点击的回掉
 * Created by shuyu on 2016/11/10.
 */

open interface SpanUrlCallBack {
    fun phone(view: View, phone: String)

    fun url(view: View, url: String)
}
