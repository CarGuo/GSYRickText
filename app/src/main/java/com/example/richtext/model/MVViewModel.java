package com.example.richtext.model;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.ObservableField;
import android.graphics.Color;
import android.text.Spannable;
import android.text.method.MovementMethod;

import com.example.richtext.contract.IMVVMView;
import com.shuyu.textutillib.RichTextBuilder;
import com.shuyu.textutillib.listener.ITextViewShow;
import com.shuyu.textutillib.model.TopicModel;
import com.shuyu.textutillib.model.UserModel;

import java.util.ArrayList;
import java.util.List;

/**
 * view model
 * Created by guoshuyu on 2017/8/22.
 */

public class MVViewModel extends BaseObservable {

    public final ObservableField<CharSequence> currentTextString = new ObservableField<>();

    public final ObservableField<String> currentTextTitle= new ObservableField<>("未输入文本");

    public final ObservableField<Integer> linkFlag = new ObservableField<>(0);

    private List<TopicModel> topicModels = new ArrayList<>();

    private List<UserModel> nameList = new ArrayList<>();

    private Context context;

    private IMVVMView imvvmView;

    private int textFlag = 0;

    public MVViewModel(Context context, IMVVMView imvvmView) {
        this.context = context;
        this.imvvmView = imvvmView;
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

    private ITextViewShow iTextViewShow = new ITextViewShow() {
        @Override
        public void setText(CharSequence charSequence) {
            imvvmView.setText(charSequence);
        }

        @Override
        public CharSequence getText() {
            return imvvmView.getText();
        }

        @Override
        public void setMovementMethod(MovementMethod movementMethod) {
            imvvmView.setMovementMethod(movementMethod);
        }

        @Override
        public void setAutoLinkMask(int flag) {
            imvvmView.setAutoLinkMask(flag);
        }
    };

    /**
     * 设置显示文本
     *
     * @param text
     */
    private void setCurrentText(String text) {
        RichTextBuilder richTextBuilder = new RichTextBuilder(context);
        Spannable spannable = richTextBuilder.setContent(text)
                .setAtColor(Color.RED)
                .setLinkColor(Color.BLUE)
                .setTopicColor(Color.YELLOW)
                .setListUser(nameList)
                .setListTopic(topicModels)
                .setSpanAtUserCallBack(imvvmView.getSpanAtUserCallBack())
                .setSpanUrlCallBack(imvvmView.getSpanUrlCallBack())
                .setSpanTopicCallBack(imvvmView.getSpanTopicCallBack())
                .buildSpan(iTextViewShow);
        setLocalCurrentTextString(spannable);
    }

    private void setLocalCurrentTextString(CharSequence charSequence) {
        currentTextString.set(charSequence);
    }

    private CharSequence getLocalCurrentTextString() {
        return currentTextString.get();
    }

    private void setForLinkFlag(int flag) {
        linkFlag.set(flag);
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
