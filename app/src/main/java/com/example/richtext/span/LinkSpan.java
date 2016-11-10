package com.example.richtext.span;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Toast;

public class LinkSpan extends ClickableSpan {

    private String mUrl;
    private Context mContext;
    public LinkSpan(Context context, String url) {
        mUrl = url;
        mContext = context;
    }

    @Override
    public void onClick(View widget) {
        if ((mUrl.contains("tel:") && TextUtils.isDigitsOnly(mUrl.replace("tel:", ""))) || TextUtils.isDigitsOnly(mUrl)) {
            if (!mUrl.contains("tel:")) {
                mUrl = "tel:" + mUrl;
                Toast.makeText(mContext, "点击了电话", Toast.LENGTH_SHORT).show();
            }

            //用intent启动拨打电话
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(mUrl));
            if (ActivityCompat.checkSelfPermission(widget.getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            widget.getContext().startActivity(intent);

        } else {
            Toast.makeText(mContext, "点击了链接", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(Color.BLUE);
        /** 去掉下划线 ， 默认自带下划线 **/
        ds.setUnderlineText(false);
    }
}