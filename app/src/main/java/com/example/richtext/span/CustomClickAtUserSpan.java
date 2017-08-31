package com.example.richtext.span;

import android.content.Context;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.shuyu.textutillib.listener.SpanAtUserCallBack;
import com.shuyu.textutillib.model.UserModel;
import com.shuyu.textutillib.span.ClickAtUserSpan;

/**
 * 自定义显示可点击的@某人
 * Created by shuyu on 2016/11/10.
 */
public class CustomClickAtUserSpan extends ClickAtUserSpan {

    public CustomClickAtUserSpan(Context context, UserModel userModel, int color, SpanAtUserCallBack spanClickCallBack) {
        super(context, userModel, color, spanClickCallBack);
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setUnderlineText(true);
        //间隔线
        ds.setStyle(Paint.Style.STROKE);
        PathEffect effects = new DashPathEffect(new float[]{1, 1}, 1);
        ds.setPathEffect(effects);
        ds.setStrokeWidth(5);
    }

}