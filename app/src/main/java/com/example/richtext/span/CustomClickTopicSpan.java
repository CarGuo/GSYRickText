package com.example.richtext.span;

import android.content.Context;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.view.View;

import com.shuyu.textutillib.listener.SpanTopicCallBack;
import com.shuyu.textutillib.model.TopicModel;
import com.shuyu.textutillib.span.ClickTopicSpan;

/**
 * 自定义话题#点击回调
 * Created by guoshuyu on 2017/8/16.
 */

public class CustomClickTopicSpan extends ClickTopicSpan {

    public CustomClickTopicSpan(Context context, TopicModel topicModel, int color, SpanTopicCallBack spanTopicCallBack) {
        super(context, topicModel, color, spanTopicCallBack);
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setUnderlineText(true);
        ds.setStyle(Paint.Style.FILL_AND_STROKE);
        PathEffect effects = new DashPathEffect(new float[]{1, 1}, 1);
        ds.setPathEffect(effects);
        ds.setStrokeWidth(2);
    }
}
