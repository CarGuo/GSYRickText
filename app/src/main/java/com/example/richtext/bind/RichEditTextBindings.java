package com.example.richtext.bind;

import android.databinding.BindingAdapter;

import com.shuyu.textutillib.RichEditText;
import com.shuyu.textutillib.model.TopicModel;
import com.shuyu.textutillib.model.UserModel;

public class RichEditTextBindings {

    @BindingAdapter("app:atResult")
    public static void setAtResult(RichEditText richEditText, UserModel userModel) {
        if (userModel != null) {
            richEditText.resolveAtResult(userModel);
        }
    }

    @BindingAdapter("app:atResultByEnter")
    public static void setAtResultByEnterAt(RichEditText richEditText, UserModel userModel) {
        if (userModel != null) {
            richEditText.resolveAtResultByEnterAt(userModel);
        }
    }

    @BindingAdapter("app:topicResultByEnter")
    public static void setTopicResultByEnter(RichEditText richEditText, TopicModel topicModel) {
        if (topicModel != null) {
            richEditText.resolveTopicResultByEnter(topicModel);
        }
    }

    @BindingAdapter("app:topicResult")
    public static void setTopicResult(RichEditText richEditText, TopicModel topicModel) {
        if (topicModel != null) {
            richEditText.resolveTopicResult(topicModel);
        }
    }


}
