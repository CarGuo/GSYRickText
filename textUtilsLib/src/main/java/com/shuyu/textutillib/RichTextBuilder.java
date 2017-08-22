package com.shuyu.textutillib;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.method.MovementMethod;
import android.widget.TextView;

import com.shuyu.textutillib.listener.ITextViewShow;
import com.shuyu.textutillib.listener.SpanAtUserCallBack;
import com.shuyu.textutillib.listener.SpanTopicCallBack;
import com.shuyu.textutillib.listener.SpanUrlCallBack;
import com.shuyu.textutillib.model.TopicModel;
import com.shuyu.textutillib.model.UserModel;

import java.util.List;

/**
 * 富文本设置 话题、at某人，链接识别
 * Created by guoshuyu on 2017/8/17.
 */

public class RichTextBuilder {
    private Context context;
    private String content = "";
    private List<UserModel> listUser;
    private List<TopicModel> listTopic;
    private TextView textView;
    private int atColor = Color.BLUE;
    private int topicColor = Color.BLUE;
    private int linkColor = Color.BLUE;
    private boolean needNum = false;
    private SpanAtUserCallBack spanAtUserCallBack;
    private SpanUrlCallBack spanUrlCallBack;
    private SpanTopicCallBack spanTopicCallBack;

    public RichTextBuilder(Context context) {
        this.context = context;
    }

    /**
     * 文本内容
     */
    public RichTextBuilder setContent(String content) {
        this.content = content;
        return this;
    }

    /**
     * at某人的list
     */
    public RichTextBuilder setListUser(List<UserModel> listUser) {
        this.listUser = listUser;
        return this;
    }

    /**
     * 话题list
     */
    public RichTextBuilder setListTopic(List<TopicModel> listTopic) {
        this.listTopic = listTopic;
        return this;
    }

    /**
     * 显示文本view
     */
    public RichTextBuilder setTextView(TextView textView) {
        this.textView = textView;
        return this;
    }

    /**
     * at某人显示颜色
     */
    public RichTextBuilder setAtColor(int atColor) {
        this.atColor = atColor;
        return this;
    }

    /**
     * 话题显示颜色
     */
    public RichTextBuilder setTopicColor(int topicColor) {
        this.topicColor = topicColor;
        return this;
    }

    /**
     * 链接显示颜色
     */
    public RichTextBuilder setLinkColor(int linkColor) {
        this.linkColor = linkColor;
        return this;
    }

    /**
     * 是否需要识别电话
     */
    public RichTextBuilder setNeedNum(boolean needNum) {
        this.needNum = needNum;
        return this;
    }

    /**
     * at某人点击回调
     */
    public RichTextBuilder setSpanAtUserCallBack(SpanAtUserCallBack spanAtUserCallBack) {
        this.spanAtUserCallBack = spanAtUserCallBack;
        return this;
    }

    /**
     * url点击回调
     */
    public RichTextBuilder setSpanUrlCallBack(SpanUrlCallBack spanUrlCallBack) {
        this.spanUrlCallBack = spanUrlCallBack;
        return this;
    }

    /**
     * 话题点击回调
     */
    public RichTextBuilder setSpanTopicCallBack(SpanTopicCallBack spanTopicCallBack) {
        this.spanTopicCallBack = spanTopicCallBack;
        return this;
    }

    public void build() {

        if (context == null) {
            throw new IllegalStateException("context could not be null.");
        }

        if (textView == null) {
            throw new IllegalStateException("textView could not be null.");
        }

        ITextViewShow iTextViewShow = new ITextViewShow() {
            @Override
            public void setText(CharSequence charSequence) {
                textView.setText(charSequence);
            }

            @Override
            public CharSequence getText() {
                return textView.getText();
            }

            @Override
            public void setMovementMethod(MovementMethod movementMethod) {
                textView.setMovementMethod(movementMethod);
            }

            @Override
            public void setAutoLinkMask(int flag) {
                textView.setAutoLinkMask(flag);
            }
        };

        Spannable spannable = TextCommonUtils.getAllSpanText(
                context,
                content,
                listUser,
                listTopic,
                iTextViewShow,
                atColor,
                linkColor,
                topicColor,
                needNum,
                spanAtUserCallBack,
                spanUrlCallBack,
                spanTopicCallBack);
        textView.setText(spannable);
    }
}
