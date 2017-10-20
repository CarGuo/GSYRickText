package com.example.shuyu.bind

import android.app.Activity
import android.content.Context
import android.databinding.BindingAdapter
import com.example.shuyu.R

import com.shuyu.textutillib.EmojiLayout
import com.shuyu.textutillib.RichEditText
import com.shuyu.textutillib.model.TopicModel
import com.shuyu.textutillib.model.UserModel


open class RichEditTextBindings{

    companion object {
        @BindingAdapter("atResult")
        fun setAtResult(richEditText: RichEditText, userModel: UserModel?) {
            if (userModel != null) {
                richEditText.resolveAtResult(userModel)
            }
        }

        @BindingAdapter("atResultByEnter")
        fun setAtResultByEnterAt(richEditText: RichEditText, userModel: UserModel?) {
            if (userModel != null) {
                richEditText.resolveAtResultByEnterAt(userModel)
            }
        }

        @BindingAdapter("topicResultByEnter")
        fun setTopicResultByEnter(richEditText: RichEditText, topicModel: TopicModel?) {
            if (topicModel != null) {
                richEditText.resolveTopicResultByEnter(topicModel)
            }
        }

        @BindingAdapter("topicResult")
        fun setTopicResult(richEditText: RichEditText, topicModel: TopicModel?) {
            if (topicModel != null) {
                richEditText.resolveTopicResult(topicModel)
            }
        }

        @BindingAdapter("emojiRichTextView")
        fun setEmojiRichTextView(emojiLayout: EmojiLayout?, context: Context?) {
            if (emojiLayout != null && context != null && context is Activity) {
                val activity = context as Activity?
                emojiLayout.setEditTextSmile(activity!!.findViewById(R.id.mvvm_rich_edit_text) as RichEditText)
            }
        }

    }

}
