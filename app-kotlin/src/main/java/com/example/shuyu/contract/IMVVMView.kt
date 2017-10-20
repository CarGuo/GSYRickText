package com.example.shuyu.contract

import android.content.Context

import com.shuyu.textutillib.listener.SpanAtUserCallBack
import com.shuyu.textutillib.listener.SpanTopicCallBack
import com.shuyu.textutillib.listener.SpanUrlCallBack

/**
 * view 接口
 * Created by guoshuyu on 2017/8/22.
 */

open interface IMVVMView {
    val spanTopicCallBack: SpanTopicCallBack

    val spanAtUserCallBack: SpanAtUserCallBack

    val spanUrlCallBack: SpanUrlCallBack

    val context: Context
}

