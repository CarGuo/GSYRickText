package com.example.richtext.model;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.graphics.Color;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.example.richtext.TopicListActivity;
import com.example.richtext.UserListActivity;
import com.example.richtext.contract.IMVVMView;
import com.example.richtext.span.CustomClickAtUserSpan;
import com.example.richtext.span.CustomClickTopicSpan;
import com.example.richtext.span.CustomLinkSpan;
import com.example.richtext.utils.JumpUtil;
import com.shuyu.textutillib.listener.OnEditTextUtilJumpListener;
import com.shuyu.textutillib.listener.SpanAtUserCallBack;
import com.shuyu.textutillib.listener.SpanCreateListener;
import com.shuyu.textutillib.listener.SpanTopicCallBack;
import com.shuyu.textutillib.listener.SpanUrlCallBack;
import com.shuyu.textutillib.model.TopicModel;
import com.shuyu.textutillib.model.UserModel;
import com.shuyu.textutillib.span.ClickAtUserSpan;
import com.shuyu.textutillib.span.ClickTopicSpan;
import com.shuyu.textutillib.span.LinkSpan;


/**
 * view model
 * Created by guoshuyu on 2017/8/22.
 */

public class MVViewModel extends BaseObservable {


    public final static int REQUEST_USER_CODE_INPUT = 1111;

    public final static int REQUEST_USER_CODE_CLICK = 2222;

    public final static int REQUEST_TOPIC_CODE_INPUT = 3333;

    public final static int REQUEST_TOPIC_CODE_CLICK = 4444;

    public final ObservableField<String> currentTextViewString = new ObservableField<>();

    public final ObservableField<String> currentTextTitle = new ObservableField<>("未输入文本");

    public final ObservableArrayList<TopicModel> topicListOb = new ObservableArrayList<>();

    public final ObservableArrayList<UserModel> nameListOb = new ObservableArrayList<>();

    public final ObservableField<Integer> atColor = new ObservableField<>(Color.YELLOW);

    public final ObservableField<Integer> topicColor = new ObservableField<>(Color.RED);

    public final ObservableField<Integer> linkColor = new ObservableField<>(Color.BLUE);

    public final ObservableField<Integer> textEmojiSize = new ObservableField<>(0);

    public final ObservableField<Boolean> needNumberShow = new ObservableField<>(true);

    public final ObservableField<Boolean> needUrlShow = new ObservableField<>(true);

    public final ObservableField<SpanAtUserCallBack> spanAtUserCallback = new ObservableField<>();

    public final ObservableField<SpanTopicCallBack> spanTopicCallback = new ObservableField<>();

    public final ObservableField<SpanUrlCallBack> spanUrlCallback = new ObservableField<>();

    public final ObservableField<SpanCreateListener> spanCreateListener = new ObservableField<>();

    public final ObservableField<Boolean> textViewShow = new ObservableField<>(true);


    public final ObservableField<Integer> richMaxLength = new ObservableField<>(2000);

    public final ObservableField<String> colorAtUser = new ObservableField<>("#FA88FF");

    public final ObservableField<String> colorTopic = new ObservableField<>("#9800FF");

    public final ObservableArrayList<UserModel> nameListObEd = new ObservableArrayList<>();

    public final ObservableArrayList<TopicModel> topicListObEd = new ObservableArrayList<>();

    public final ObservableField<OnEditTextUtilJumpListener> editJump = new ObservableField<>();

    public final ObservableField<UserModel> atResult = new ObservableField<>();

    public final ObservableField<UserModel> atResultByEnter = new ObservableField<>();

    public final ObservableField<TopicModel> topicResultByEnter = new ObservableField<>();

    public final ObservableField<TopicModel> topicResult = new ObservableField<>();

    public final ObservableField<Boolean> editTextShow = new ObservableField<>(false);

    public final ObservableField<Boolean> emojiShow = new ObservableField<>(false);

    public final ObservableField<Context> curRichTextView = new ObservableField<>();

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

        //如果不需要可不设置
        spanCreateListener.set(spanListener);
        curRichTextView.set(imvvmView.getContext());

    }

    //如果不需要可不设置
    private SpanCreateListener spanListener = new SpanCreateListener() {
        @Override
        public ClickAtUserSpan getCustomClickAtUserSpan(Context context, UserModel userModel, int color, SpanAtUserCallBack spanClickCallBack) {
            return new CustomClickAtUserSpan(context, userModel, color, spanClickCallBack);
        }

        @Override
        public ClickTopicSpan getCustomClickTopicSpan(Context context, TopicModel topicModel, int color, SpanTopicCallBack spanTopicCallBack) {
            return new CustomClickTopicSpan(context, topicModel, color, spanTopicCallBack);
        }

        @Override
        public LinkSpan getCustomLinkSpan(Context context, String url, int color, SpanUrlCallBack spanUrlCallBack) {
            return new CustomLinkSpan(context, url, color, spanUrlCallBack);
        }
    };

    /**
     * 设置显示文本
     *
     * @param text
     */
    private void setCurrentText(String text) {
        currentTextViewString.set(text);
    }


    /**
     * 隐藏软键盘
     */
    private void hideKeyboard() {
        Activity context = (Activity) imvvmView.getContext();
        if (context.getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (context.getCurrentFocus() != null) {
                ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).
                        hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }


    /**
     * 插入文本点击
     */
    public void insertTextClick() {
        editTextShow.set(false);
        emojiShow.set(false);
        textViewShow.set(true);
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

    /**
     * 切换到输入
     */
    public void changeToEdit() {
        editTextShow.set(true);
        textViewShow.set(false);
        emojiShow.set(false);
        currentTextTitle.set("编辑框模式");
        editJump.set(onEditTextUtilJumpListener);

    }

    public void showEmoji() {
        if (emojiShow.get()) {
            emojiShow.set(false);
        } else {
            emojiShow.set(true);
            hideKeyboard();
        }
    }

    public void hideEmojiLayout() {
        emojiShow.set(false);
    }

    private final OnEditTextUtilJumpListener onEditTextUtilJumpListener = new OnEditTextUtilJumpListener() {
        @Override
        public void notifyAt() {
            JumpUtil.goToUserList((Activity) imvvmView.getContext(), REQUEST_USER_CODE_INPUT);
        }

        @Override
        public void notifyTopic() {
            JumpUtil.goToTopicList((Activity) imvvmView.getContext(), REQUEST_TOPIC_CODE_INPUT);
        }
    };


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_USER_CODE_CLICK:
                    atResult.set((UserModel) data.getSerializableExtra(UserListActivity.DATA));
                    break;
                case REQUEST_USER_CODE_INPUT:
                    atResultByEnter.set((UserModel) data.getSerializableExtra(UserListActivity.DATA));
                    break;

                case REQUEST_TOPIC_CODE_INPUT:
                    topicResultByEnter.set((TopicModel) data.getSerializableExtra(TopicListActivity.DATA));
                    break;
                case REQUEST_TOPIC_CODE_CLICK:
                    topicResult.set((TopicModel) data.getSerializableExtra(TopicListActivity.DATA));
                    break;
            }
        }

    }
}
