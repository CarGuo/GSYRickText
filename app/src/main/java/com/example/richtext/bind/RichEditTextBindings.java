package com.example.richtext.bind;

import android.app.Activity;
import android.content.Context;
import android.databinding.BindingAdapter;

import com.example.richtext.R;
import com.shuyu.textutillib.EmojiLayout;
import com.shuyu.textutillib.RichEditText;
import com.shuyu.textutillib.model.TopicModel;
import com.shuyu.textutillib.model.UserModel;

public class RichEditTextBindings {

    @BindingAdapter("atResult")
    public static void setAtResult(RichEditText richEditText, UserModel userModel) {
        if (userModel != null) {
            richEditText.resolveAtResult(userModel);
        }
    }

    @BindingAdapter("atResultByEnter")
    public static void setAtResultByEnterAt(RichEditText richEditText, UserModel userModel) {
        if (userModel != null) {
            richEditText.resolveAtResultByEnterAt(userModel);
        }
    }

    @BindingAdapter("topicResultByEnter")
    public static void setTopicResultByEnter(RichEditText richEditText, TopicModel topicModel) {
        if (topicModel != null) {
            richEditText.resolveTopicResultByEnter(topicModel);
        }
    }

    @BindingAdapter("topicResult")
    public static void setTopicResult(RichEditText richEditText, TopicModel topicModel) {
        if (topicModel != null) {
            richEditText.resolveTopicResult(topicModel);
        }
    }
    @BindingAdapter("emojiRichTextView")
    public static void setEmojiRichTextView(EmojiLayout emojiLayout, Context context) {
        if (emojiLayout != null && context != null && context instanceof Activity) {
            Activity activity = (Activity) context;
            emojiLayout.setEditTextSmile((RichEditText)
                    activity.findViewById(R.id.mvvm_rich_edit_text));
        }
    }


}
