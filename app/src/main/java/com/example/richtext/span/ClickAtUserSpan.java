package com.example.richtext.span;

import android.content.Context;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Toast;

import com.example.richtext.R;


public class ClickAtUserSpan extends ClickableSpan {
    private Context context;
    private String memberName = "";
    private String id;

    public ClickAtUserSpan(Context context, String memberName, String id) {
        this.context = context;
        this.memberName = memberName;
        this.id = id;
    }

    @Override
    public void onClick(View view) {
        /** 跳转 **/
        Toast.makeText(context, "点击了人@名字", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        /** 给文字染色 **/
        //ds.setARGB(255, 96, 134, 174);
        ds.setColor(context.getResources().getColor(R.color.style_color_main));
        /** 去掉下划线 ， 默认自带下划线 **/
        ds.setUnderlineText(false);
    }

}