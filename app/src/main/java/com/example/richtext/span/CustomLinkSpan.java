package com.example.richtext.span;


import android.content.Context;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.view.View;

import com.shuyu.textutillib.listener.SpanUrlCallBack;
import com.shuyu.textutillib.span.LinkSpan;

/**
 * 自定义显示可点击的手机号码和URL
 * Created by shuyu on 2016/11/10.
 */
public class CustomLinkSpan extends LinkSpan {

    public CustomLinkSpan(Context context, String url, int color, SpanUrlCallBack spanUrlCallBack) {
        super(context, url, color, spanUrlCallBack);
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setUnderlineText(true);
        ds.setTypeface(Typeface.DEFAULT_BOLD);
    }
}