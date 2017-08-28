package com.example.richtext.model;


import android.databinding.BaseObservable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.graphics.Color;

import com.example.richtext.contract.IMVVMView;
import com.shuyu.textutillib.listener.SpanAtUserCallBack;
import com.shuyu.textutillib.listener.SpanTopicCallBack;
import com.shuyu.textutillib.listener.SpanUrlCallBack;
import com.shuyu.textutillib.model.TopicModel;
import com.shuyu.textutillib.model.UserModel;


/**
 * view model
 * Created by guoshuyu on 2017/8/22.
 */

public class MVViewModel extends BaseObservable {

    public final ObservableField<String> currentTextViewString = new ObservableField<>();

    public final ObservableField<String> currentTextTitle = new ObservableField<>("未输入文本");

    public final ObservableArrayList<TopicModel> topicListOb = new ObservableArrayList<>();

    public final ObservableArrayList<UserModel> nameListOb = new ObservableArrayList<>();

    public final ObservableField<Integer> atColor = new ObservableField<>(Color.YELLOW);

    public final ObservableField<Integer> topicColor = new ObservableField<>(Color.RED);

    public final ObservableField<Integer> linkColor = new ObservableField<>(Color.BLUE);

    public final ObservableField<Boolean> needNumberShow = new ObservableField<>(true);

    public final ObservableField<SpanAtUserCallBack> spanAtUserCallback = new ObservableField<>();

    public final ObservableField<SpanTopicCallBack> spanTopicCallback = new ObservableField<>();

    public final ObservableField<SpanUrlCallBack> spanUrlCallback = new ObservableField<>();

    private IMVVMView imvvmView;

    private int textFlag = 0;

    public MVViewModel(IMVVMView imvvmView) {
        this.imvvmView = imvvmView;
        initData();
    }

    private void initData() {
        nameListOb.clear();
        topicListOb.clear();

        UserModel userModel = new UserModel();
        userModel.setUser_name("22222");
        userModel.setUser_id("2222");
        nameListOb.add(userModel);
        userModel = new UserModel();
        userModel.setUser_name("kkk");
        userModel.setUser_id("23333");
        nameListOb.add(userModel);

        TopicModel topicModel = new TopicModel();
        topicModel.setTopicId("333");
        topicModel.setTopicName("话题话题");
        topicListOb.add(topicModel);

        spanAtUserCallback.set(imvvmView.getSpanAtUserCallBack());
        spanTopicCallback.set(imvvmView.getSpanTopicCallBack());
        spanUrlCallback.set(imvvmView.getSpanUrlCallBack());

    }

    /**
     * 设置显示文本
     *
     * @param text
     */
    private void setCurrentText(String text) {
        currentTextViewString.set(text);
    }

    /**
     * 插入文本点击
     */
    public void insertTextClick() {
        String content = "";
        String title = "";
        switch (textFlag) {
            case 0:
                textFlag = 1;
                content = "这是测试#话题话题#文本哟 www.baidu.com " +
                        "\n来@某个人  @22222 @kkk " +
                        "\n好的,来几个表情[e2][e4][e55]，最后来一个电话 13245685478";
                title = "多种数据类型";
                break;
            case 1:
                textFlag = 2;
                content = "这是普通的测试文本";
                title = "普通文本类型";
                break;
            case 2:
                textFlag = 3;
                content = "这是只有表情[e2][e4][e55]";
                title = "标签文本类型";
                break;
            case 3:
                textFlag = 0;
                content = "这是测试@人的文本 " +
                        "\n来@kkk  @22222 ";
                title = "@人文本类型";
                break;
        }
        setCurrentText(content);
        currentTextTitle.set(title);
    }
}
