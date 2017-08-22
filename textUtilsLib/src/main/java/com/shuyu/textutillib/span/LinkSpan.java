package com.shuyu.textutillib.span;


import android.content.Context;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.view.View;

import com.shuyu.textutillib.listener.SpanUrlCallBack;

/**
 * 显示可点击的手机号码和URL
 * Created by shuyu on 2016/11/10.
 */
public class LinkSpan extends ClickableSpan {

    private String url;
    private Context context;
    private SpanUrlCallBack spanUrlCallBack;
    private int color;

    public LinkSpan(Context context, String url, int color, SpanUrlCallBack spanUrlCallBack) {
        this.url = url;
        this.context = context;
        this.spanUrlCallBack = spanUrlCallBack;
        this.color = color;
    }

    @Override
    public void onClick(View widget) {
        if ((url.contains("tel:") && TextUtils.isDigitsOnly(url.replace("tel:", ""))) || TextUtils.isDigitsOnly(url)) {
            if (spanUrlCallBack != null)
                spanUrlCallBack.phone(widget, url);
        } else {
            if (spanUrlCallBack != null)
                spanUrlCallBack.url(widget, url);
        }
    }


    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(color);
        /** 去掉下划线 ， 默认自带下划线 **/
        ds.setUnderlineText(false);
    }
}