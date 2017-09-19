package com.shuyu.textutillib.listener;

import android.content.Context;
import android.text.method.MovementMethod;

import com.shuyu.textutillib.model.TopicModel;
import com.shuyu.textutillib.model.UserModel;
import com.shuyu.textutillib.span.ClickAtUserSpan;
import com.shuyu.textutillib.span.ClickTopicSpan;
import com.shuyu.textutillib.span.LinkSpan;

/**
 * textview 显示接口
 * Created by guoshuyu on 2017/8/22.
 */

public interface ITextViewShow {
    void setText(CharSequence charSequence);

    CharSequence getText();

    void setMovementMethod(MovementMethod movementMethod);

    void setAutoLinkMask(int flag);

    ClickAtUserSpan getCustomClickAtUserSpan(Context context, UserModel userModel, int color, SpanAtUserCallBack spanClickCallBack);

    ClickTopicSpan getCustomClickTopicSpan(Context context, TopicModel topicModel, int color, SpanTopicCallBack spanTopicCallBack);

    LinkSpan getCustomLinkSpan(Context context, String url, int color, SpanUrlCallBack spanUrlCallBack);

    int emojiSize();

    int verticalAlignment();
}
