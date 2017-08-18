package com.shuyu.textutillib;

import android.content.Intent;
import android.widget.EditText;

import com.shuyu.textutillib.listener.OnEditTextUtilJumpListener;
import com.shuyu.textutillib.model.TopicModel;
import com.shuyu.textutillib.model.UserModel;

import java.util.List;

/**
 * 富文本设置 话题、at某人，链接识别
 * Created by guoshuyu on 2017/8/18.
 */

public class RichEditBuilder {

    private RichEditText editText;

    private List<UserModel> userModels;

    private List<TopicModel> topicModels;

    private OnEditTextUtilJumpListener editTextAtUtilJumpListener;

    public RichEditBuilder setEditText(RichEditText editText) {
        this.editText = editText;
        return this;
    }

    public RichEditBuilder setUserModels(List<UserModel> userModels) {
        this.userModels = userModels;
        return this;
    }

    public RichEditBuilder setTopicModels(List<TopicModel> topicModels) {
        this.topicModels = topicModels;
        return this;
    }

    public RichEditBuilder setEditTextAtUtilJumpListener(OnEditTextUtilJumpListener editTextAtUtilJumpListener) {
        this.editTextAtUtilJumpListener = editTextAtUtilJumpListener;
        return this;
    }

    public RichEditText builder() {
        editText.setEditTextAtUtilJumpListener(editTextAtUtilJumpListener);
        editText.setModelList(userModels, topicModels);
        return editText;
    }

}
