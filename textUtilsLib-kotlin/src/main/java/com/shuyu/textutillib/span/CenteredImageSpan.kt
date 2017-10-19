package com.shuyu.textutillib.span

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.net.Uri
import android.support.annotation.DrawableRes
import android.text.style.DynamicDrawableSpan
import android.text.style.ImageSpan

/**
 * 带有居中功能的ImageSpan
 * Created by shuyu on 2017/09/19.
 */
open class CenteredImageSpan : ImageSpan {

    constructor(drawableRes: Drawable) : super(drawableRes) {}

    constructor(context: Context, b: Bitmap) : super(context, b) {}

    constructor(context: Context, b: Bitmap, verticalAlignment: Int) : super(context, b, verticalAlignment) {}

    constructor(d: Drawable, verticalAlignment: Int) : super(d, verticalAlignment) {}

    constructor(d: Drawable, source: String) : super(d, source) {}

    constructor(d: Drawable, source: String, verticalAlignment: Int) : super(d, source, verticalAlignment) {}

    constructor(context: Context, uri: Uri) : super(context, uri) {}

    constructor(context: Context, uri: Uri, verticalAlignment: Int) : super(context, uri, verticalAlignment) {}

    constructor(context: Context, @DrawableRes resourceId: Int) : super(context, resourceId) {}

    constructor(context: Context, @DrawableRes resourceId: Int, verticalAlignment: Int) : super(context, resourceId, verticalAlignment) {}

    override fun getSize(paint: Paint, text: CharSequence, start: Int, end: Int,
                         fm: Paint.FontMetricsInt?): Int {

        if (mVerticalAlignment == DynamicDrawableSpan.ALIGN_BASELINE || mVerticalAlignment == DynamicDrawableSpan.ALIGN_BOTTOM) {
            return super.getSize(paint, text, start, end, fm)
        }

        val d = drawable
        val rect = d.bounds
        if (fm != null) {
            val fmPaint = paint.fontMetricsInt
            //获得文字、图片高度
            val fontHeight = fmPaint.bottom - fmPaint.top
            val drHeight = rect.bottom - rect.top

            val top = drHeight / 2 - fontHeight / 4
            val bottom = drHeight / 2 + fontHeight / 4

            fm.ascent = -bottom
            fm.top = -bottom
            fm.bottom = top
            fm.descent = top
        }
        return rect.right
    }

    override fun draw(canvas: Canvas, text: CharSequence, start: Int, end: Int, x: Float, top: Int, y: Int, bottom: Int,
                      paint: Paint) {

        if (mVerticalAlignment == DynamicDrawableSpan.ALIGN_BASELINE || mVerticalAlignment == DynamicDrawableSpan.ALIGN_BOTTOM) {
            super.draw(canvas, text, start, end, x, top, y, bottom, paint)
            return
        }

        val b = drawable
        canvas.save()
        var transY = 0
        //获得将要显示的文本高度-图片高度除2等居中位置+top(换行情况)
        transY = (bottom - top - b.bounds.bottom) / 2 + top
        //偏移画布后开始绘制
        canvas.translate(x, transY.toFloat())
        b.draw(canvas)
        canvas.restore()
    }

    companion object {

        val ALIGN_CENTER = 2
    }
}  