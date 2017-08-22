package com.example.richtext.model;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.ObservableField;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.shuyu.textutillib.listener.SpanAtUserCallBack;
import com.shuyu.textutillib.listener.SpanTopicCallBack;
import com.shuyu.textutillib.listener.SpanUrlCallBack;
import com.shuyu.textutillib.model.TopicModel;
import com.shuyu.textutillib.model.UserModel;

import java.util.ArrayList;
import java.util.List;

/**
 * view model
 * Created by guoshuyu on 2017/8/22.
 */

public class MVViewModel extends BaseObservable {

    public final ObservableField<String> currentTextString = new ObservableField<>();

    private List<TopicModel> topicModels = new ArrayList<>();

    private List<UserModel> nameList = new ArrayList<>();

    private Context context;

    public MVViewModel(Context context) {
        this.context = context;
        initData();
    }

    private void initData() {
        nameList.clear();
        topicModels.clear();

        UserModel userModel = new UserModel();
        userModel.setUser_name("22222");
        userModel.setUser_id("2222");
        nameList.add(userModel);
        userModel = new UserModel();
        userModel.setUser_name("kkk");
        userModel.setUser_id("23333");
        nameList.add(userModel);

        TopicModel topicModel = new TopicModel();
        topicModel.setTopicId("333");
        topicModel.setTopicName("话题话题");
        topicModels.add(topicModel);

    }

    /**
     * 链接回调
     */
    private SpanUrlCallBack spanUrlCallBack = new SpanUrlCallBack() {
        @Override
        public void phone(View view, String phone) {
            Toast.makeText(view.getContext(), phone + " 被点击了", Toast.LENGTH_SHORT).show();
            if (view instanceof TextView) {
                ((TextView) view).setHighlightColor(Color.TRANSPARENT);
            }
        }

        @Override
        public void url(View view, String url) {
            Toast.makeText(view.getContext(), url + " 被点击了", Toast.LENGTH_SHORT).show();
            if (view instanceof TextView) {
                ((TextView) view).setHighlightColor(Color.TRANSPARENT);
            }
        }
    };

    /**
     * at回调
     */
    private SpanAtUserCallBack spanAtUserCallBack = new SpanAtUserCallBack() {
        @Override
        public void onClick(View view, UserModel userModel1) {
            Toast.makeText(view.getContext(), userModel1.getUser_name() + " 被点击了", Toast.LENGTH_SHORT).show();
            if (view instanceof TextView) {
                ((TextView) view).setHighlightColor(Color.TRANSPARENT);
            }
        }
    };

    /**
     * 话题回调
     */
    private SpanTopicCallBack spanTopicCallBack = new SpanTopicCallBack() {
        @Override
        public void onClick(View view, TopicModel topicModel) {
            Toast.makeText(view.getContext(), topicModel.getTopicName() + " 被点击了", Toast.LENGTH_SHORT).show();
            if (view instanceof TextView) {
                ((TextView) view).setHighlightColor(Color.TRANSPARENT);
            }
        }
    };

    /**
     * 设置显示文本
     *
     * @param text
     */
    public void setCurrentText(String text) {
        currentTextString.set(text);
    }

    /**
     * 插入文本点击
     */
    public void insertTextClick() {
        String content = "这是测试#话题话题#文本哟 www.baidu.com " +
                "\n来@某个人  @22222 @kkk " +
                "\n好的,来几个表情[e2][e4][e55]，最后来一个电话 13245685478";
        setCurrentText(content);
    }
}
