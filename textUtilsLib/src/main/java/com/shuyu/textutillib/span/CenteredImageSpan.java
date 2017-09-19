package com.shuyu.textutillib.span;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;

/**
 * 带有居中功能的ImageSpan
 * Created by shuyu on 2017/09/19.
 */
public class CenteredImageSpan extends ImageSpan {

    public static final int ALIGN_CENTER = 2;

    public CenteredImageSpan(Drawable drawableRes) {
        super(drawableRes);
    }

    public CenteredImageSpan(Context context, Bitmap b) {
        super(context, b);
    }

    public CenteredImageSpan(Context context, Bitmap b, int verticalAlignment) {
        super(context, b, verticalAlignment);
    }

    public CenteredImageSpan(Drawable d, int verticalAlignment) {
        super(d, verticalAlignment);
    }

    public CenteredImageSpan(Drawable d, String source) {
        super(d, source);
    }

    public CenteredImageSpan(Drawable d, String source, int verticalAlignment) {
        super(d, source, verticalAlignment);
    }

    public CenteredImageSpan(Context context, Uri uri) {
        super(context, uri);
    }

    public CenteredImageSpan(Context context, Uri uri, int verticalAlignment) {
        super(context, uri, verticalAlignment);
    }

    public CenteredImageSpan(Context context, @DrawableRes int resourceId) {
        super(context, resourceId);
    }

    public CenteredImageSpan(Context context, @DrawableRes int resourceId, int verticalAlignment) {
        super(context, resourceId, verticalAlignment);
    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end,
                       Paint.FontMetricsInt fm) {

        if (mVerticalAlignment == DynamicDrawableSpan.ALIGN_BASELINE || mVerticalAlignment == DynamicDrawableSpan.ALIGN_BOTTOM) {
            return super.getSize(paint, text, start, end, fm);
        }

        Drawable d = getDrawable();
        Rect rect = d.getBounds();
        if (fm != null) {
            Paint.FontMetricsInt fmPaint = paint.getFontMetricsInt();
            //获得文字、图片高度
            int fontHeight = fmPaint.bottom - fmPaint.top;
            int drHeight = rect.bottom - rect.top;

            int top = drHeight / 2 - fontHeight / 4;
            int bottom = drHeight / 2 + fontHeight / 4;

            fm.ascent = -bottom;
            fm.top = -bottom;
            fm.bottom = top;
            fm.descent = top;
        }
        return rect.right;
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom,
                     Paint paint) {

        if (mVerticalAlignment == DynamicDrawableSpan.ALIGN_BASELINE || mVerticalAlignment == DynamicDrawableSpan.ALIGN_BOTTOM) {
            super.draw(canvas, text, start, end, x, top, y, bottom, paint);
            return;
        }

        Drawable b = getDrawable();
        canvas.save();
        int transY = 0;
        //获得将要显示的文本高度-图片高度除2等居中位置+top(换行情况)
        transY = ((bottom - top) - b.getBounds().bottom) / 2 + top;
        //偏移画布后开始绘制
        canvas.translate(x, transY);
        b.draw(canvas);
        canvas.restore();
    }
}  