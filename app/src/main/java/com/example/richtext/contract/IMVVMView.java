package com.example.richtext.contract;

import android.content.Context;
import android.text.method.MovementMethod;

import com.shuyu.textutillib.listener.SpanAtUserCallBack;
import com.shuyu.textutillib.listener.SpanTopicCallBack;
import com.shuyu.textutillib.listener.SpanUrlCallBack;

/**
 * view 接口
 * Created by guoshuyu on 2017/8/22.
 */

public interface IMVVMView {
    SpanTopicCallBack getSpanTopicCallBack();

    SpanAtUserCallBack getSpanAtUserCallBack();

    SpanUrlCallBack getSpanUrlCallBack();

    Context getContext();
}
