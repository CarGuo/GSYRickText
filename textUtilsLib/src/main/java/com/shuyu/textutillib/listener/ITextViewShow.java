package com.shuyu.textutillib.listener;

import android.text.method.MovementMethod;

/**
 * textview 显示接口
 * Created by guoshuyu on 2017/8/22.
 */

public interface ITextViewShow {
    void setText(CharSequence charSequence);

    CharSequence getText();

    void setMovementMethod(MovementMethod movementMethod);

    void setAutoLinkMask(int flag);
}
