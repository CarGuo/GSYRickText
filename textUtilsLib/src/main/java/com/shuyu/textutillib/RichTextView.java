package com.shuyu.textutillib;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.shuyu.textutillib.listener.SpanAtUserCallBack;
import com.shuyu.textutillib.listener.SpanTopicCallBack;
import com.shuyu.textutillib.listener.SpanUrlCallBack;
import com.shuyu.textutillib.model.TopicModel;
import com.shuyu.textutillib.model.UserModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guoshuyu on 2017/8/28.
 */

public class RichTextView extends TextView {

    private List<TopicModel> topicList = new ArrayList<>();

    private List<UserModel> nameList = new ArrayList<>();

    private int atColor = Color.BLUE;

    private int topicColor = Color.BLUE;

    private int linkColor = Color.BLUE;

    private SpanUrlCallBack spanUrlCallBackListener;

    private SpanAtUserCallBack spanAtUserCallBackListener;

    private SpanTopicCallBack spanTopicCallBackListener;

    private boolean needNumberShow = true;//是否需要数字处理

    public RichTextView(Context context) {
        super(context);
    }

    public RichTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RichTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private SpanUrlCallBack spanUrlCallBack = new SpanUrlCallBack() {
        @Override
        public void phone(View view, String phone) {
            if (spanUrlCallBackListener != null) {
                spanUrlCallBackListener.phone(view, phone);
            }
        }

        @Override
        public void url(View view, String url) {
            if (spanUrlCallBackListener != null) {
                spanUrlCallBackListener.url(view, url);
            }
        }
    };

    private SpanAtUserCallBack spanAtUserCallBack = new SpanAtUserCallBack() {
        @Override
        public void onClick(View view, UserModel userModel1) {
            if (spanAtUserCallBackListener != null) {
                spanAtUserCallBackListener.onClick(view, userModel1);
            }
        }
    };

    private SpanTopicCallBack spanTopicCallBack = new SpanTopicCallBack() {
        @Override
        public void onClick(View view, TopicModel topicModel) {
            if (spanTopicCallBackListener != null) {
                spanTopicCallBackListener.onClick(view, topicModel);
            }
        }
    };

    /**
     * 显示处理文本
     *
     * @param content
     */
    private void resolveRichShow(String content) {
        RichTextBuilder richTextBuilder = new RichTextBuilder(getContext());
        richTextBuilder.setContent(content)
                .setAtColor(atColor)
                .setLinkColor(linkColor)
                .setTopicColor(topicColor)
                .setListUser(nameList)
                .setListTopic(topicList)
                .setNeedNum(needNumberShow)
                .setTextView(this)
                .setSpanAtUserCallBack(spanAtUserCallBack)
                .setSpanUrlCallBack(spanUrlCallBack)
                .setSpanTopicCallBack(spanTopicCallBack)
                .build();

    }

    /**
     * 设置@某人文本
     *
     * @param text     文本
     * @param nameList @人列表
     */
    public void setRichTextUser(String text, List<UserModel> nameList) {
        this.setRichText(text, nameList, topicList);
    }

    /**
     * 设置话题文本
     *
     * @param text      文本
     * @param topicList 话题列表
     */
    public void setRichTextTopic(String text, List<TopicModel> topicList) {
        this.setRichText(text, nameList, topicList);
    }

    /**
     * 设置话题和@文本
     *
     * @param text      文本
     * @param nameList  @人列表
     * @param topicList 话题列表
     */
    public void setRichText(String text, List<UserModel> nameList, List<TopicModel> topicList) {
        if (nameList != null) {
            this.nameList = nameList;
        }
        if (topicList != null) {
            this.topicList = topicList;
        }
        resolveRichShow(text);
    }

    /**
     * 设置话题和@文本
     *
     * @param text 文本
     */
    public void setRichText(String text) {
        setRichText(text, nameList, topicList);
    }

    public boolean isNeedNumberShow() {
        return needNumberShow;
    }

    public List<TopicModel> getTopicList() {
        return topicList;
    }

    /**
     * 设置话题列表
     */
    public void setTopicList(List<TopicModel> topicList) {
        this.topicList = topicList;
    }

    public List<UserModel> getNameList() {
        return nameList;
    }

    /**
     * 设置at列表
     */
    public void setNameList(List<UserModel> nameList) {
        this.nameList = nameList;
    }

    /**
     * 是否需要处理数字
     *
     * @param needNumberShow 是否需要高亮数字和点击
     */
    public void setNeedNumberShow(boolean needNumberShow) {
        this.needNumberShow = needNumberShow;
    }

    /**
     * url点击
     */
    public void setSpanUrlCallBackListener(SpanUrlCallBack spanUrlCallBackListener) {
        this.spanUrlCallBackListener = spanUrlCallBackListener;
    }

    /**
     * at某人点击
     */
    public void setSpanAtUserCallBackListener(SpanAtUserCallBack spanAtUserCallBackListener) {
        this.spanAtUserCallBackListener = spanAtUserCallBackListener;
    }

    /**
     * 话题点击
     */
    public void setSpanTopicCallBackListener(SpanTopicCallBack spanTopicCallBackListener) {
        this.spanTopicCallBackListener = spanTopicCallBackListener;
    }

    public int getAtColor() {
        return atColor;
    }

    /**
     * at某人颜色
     */
    public void setAtColor(int atColor) {
        this.atColor = atColor;
    }

    public int getTopicColor() {
        return topicColor;
    }
    /**
     * 话题颜色
     */
    public void setTopicColor(int topicColor) {
        this.topicColor = topicColor;
    }

    public int getLinkColor() {
        return linkColor;
    }

    /**
     * 链接颜色
     */
    public void setLinkColor(int linkColor) {
        this.linkColor = linkColor;
    }
}
