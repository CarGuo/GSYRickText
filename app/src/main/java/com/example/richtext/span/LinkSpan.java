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
            }

            //用intent启动拨打电话
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(mUrl));
            if (ActivityCompat.checkSelfPermission(widget.getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            widget.getContext().startActivity(intent);

        } else {
            //gotoWebView(widget.getContext(), mUrl);
        }
    }


    @Override
    public void updateDrawState(TextPaint ds) {
        /** 给文字染色 **/
        //ds.setARGB(255, 96, 134, 174);
        ds.setColor(Color.BLUE);
        /** 去掉下划线 ， 默认自带下划线 **/
        ds.setUnderlineText(false);
    }
}