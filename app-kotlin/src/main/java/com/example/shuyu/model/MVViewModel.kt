package com.example.shuyu.model


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.databinding.BaseObservable
import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import android.graphics.Color
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager

import com.example.shuyu.TopicListActivity
import com.example.shuyu.UserListActivity
import com.example.shuyu.contract.IMVVMView
import com.example.shuyu.span.CustomClickAtUserSpan
import com.example.shuyu.span.CustomClickTopicSpan
import com.example.shuyu.span.CustomLinkSpan
import com.example.shuyu.utils.JumpUtil
import com.shuyu.textutillib.listener.OnEditTextUtilJumpListener
import com.shuyu.textutillib.listener.SpanAtUserCallBack
import com.shuyu.textutillib.listener.SpanCreateListener
import com.shuyu.textutillib.listener.SpanTopicCallBack
import com.shuyu.textutillib.listener.SpanUrlCallBack
import com.shuyu.textutillib.model.TopicModel
import com.shuyu.textutillib.model.UserModel
import com.shuyu.textutillib.span.ClickAtUserSpan
import com.shuyu.textutillib.span.ClickTopicSpan
import com.shuyu.textutillib.span.LinkSpan


/**
 * view model
 * Created by guoshuyu on 2017/8/22.
 */

open class MVViewModel(private val imvvmView: IMVVMView) : BaseObservable() {

    val currentTextViewString = ObservableField<String>()

    val currentTextTitle = ObservableField("未输入文本")

    val topicListOb = ObservableArrayList<TopicModel>()

    val nameListOb = ObservableArrayList<UserModel>()

    val atColor = ObservableField(Color.YELLOW)

    val topicColor = ObservableField(Color.RED)

    val linkColor = ObservableField(Color.BLUE)

    val textEmojiSize = ObservableField(0)

    val needNumberShow = ObservableField(true)

    val needUrlShow = ObservableField(true)

    val spanAtUserCallback = ObservableField<SpanAtUserCallBack>()

    val spanTopicCallback = ObservableField<SpanTopicCallBack>()

    val spanUrlCallback = ObservableField<SpanUrlCallBack>()

    val spanCreateListener = ObservableField<SpanCreateListener>()

    val textViewShow = ObservableField(true)


    val richMaxLength = ObservableField(2000)

    val colorAtUser = ObservableField("#FA88FF")

    val colorTopic = ObservableField("#9800FF")

    val nameListObEd = ObservableArrayList<UserModel>()

    val topicListObEd = ObservableArrayList<TopicModel>()

    val editJump = ObservableField<OnEditTextUtilJumpListener>()

    val atResult = ObservableField<UserModel>()

    val atResultByEnter = ObservableField<UserModel>()

    val topicResultByEnter = ObservableField<TopicModel>()

    val topicResult = ObservableField<TopicModel>()

    val editTextShow = ObservableField(false)

    val emojiShow = ObservableField(false)

    val curRichTextView = ObservableField<Context>()

    private var textFlag = 0

    //如果不需要可不设置
    private val spanListener = object : SpanCreateListener {
        override fun getCustomClickAtUserSpan(context: Context, userModel: UserModel, color: Int, spanClickCallBack: SpanAtUserCallBack): ClickAtUserSpan =
                CustomClickAtUserSpan(context, userModel, color, spanClickCallBack)

        override fun getCustomClickTopicSpan(context: Context, topicModel: TopicModel, color: Int, spanTopicCallBack: SpanTopicCallBack): ClickTopicSpan =
                CustomClickTopicSpan(context, topicModel, color, spanTopicCallBack)

        override fun getCustomLinkSpan(context: Context, url: String, color: Int, spanUrlCallBack: SpanUrlCallBack): LinkSpan =
                CustomLinkSpan(context, url, color, spanUrlCallBack)
    }

    private val onEditTextUtilJumpListener = object : OnEditTextUtilJumpListener {
        override fun notifyAt() {
            JumpUtil.goToUserList(imvvmView.context as Activity, REQUEST_USER_CODE_INPUT)
        }

        override fun notifyTopic() {
            JumpUtil.goToTopicList(imvvmView.context as Activity, REQUEST_TOPIC_CODE_INPUT)
        }
    }

    init {
        initData()
    }

    private fun initData() {
        nameListOb.clear()
        topicListOb.clear()

        var userModel = UserModel("22222", "2222")
        nameListOb.add(userModel)
        userModel = UserModel("kkk", "23333")
        nameListOb.add(userModel)

        val topicModel = TopicModel("话题话题", "333")
        topicListOb.add(topicModel)

        spanAtUserCallback.set(imvvmView.spanAtUserCallBack)
        spanTopicCallback.set(imvvmView.spanTopicCallBack)
        spanUrlCallback.set(imvvmView.spanUrlCallBack)

        //如果不需要可不设置
        spanCreateListener.set(spanListener)
        curRichTextView.set(imvvmView.context)

    }

    /**
     * 设置显示文本
     *
     * @param text
     */
    private fun setCurrentText(text: String) {
        currentTextViewString.set(text)
    }


    /**
     * 隐藏软键盘
     */
    private fun hideKeyboard() {
        val context = imvvmView.context as Activity
        if (context.window.attributes.softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (context.currentFocus != null) {
                (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(context.currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            }
        }
    }


    /**
     * 插入文本点击
     */
    fun insertTextClick() {
        editTextShow.set(false)
        emojiShow.set(false)
        textViewShow.set(true)
        var content = ""
        var title = ""
        when (textFlag) {
            0 -> {
                textFlag = 1
                content = "这是测试#话题话题#文本哟 www.baidu.com " +
                        "\n来@某个人  @22222 @kkk " +
                        "\n好的,来几个表情[e2][e4][e55]，最后来一个电话 13245685478"
                title = "多种数据类型"
            }
            1 -> {
                textFlag = 2
                content = "这是普通的测试文本"
                title = "普通文本类型"
            }
            2 -> {
                textFlag = 3
                content = "这是只有表情[e2][e4][e55]"
                title = "标签文本类型"
            }
            3 -> {
                textFlag = 0
                content = "这是测试@人的文本 " + "\n来@kkk  @22222 "
                title = "@人文本类型"
            }
        }
        setCurrentText(content)
        currentTextTitle.set(title)
    }

    /**
     * 切换到输入
     */
    fun changeToEdit() {
        editTextShow.set(true)
        textViewShow.set(false)
        emojiShow.set(false)
        currentTextTitle.set("编辑框模式")
        editJump.set(onEditTextUtilJumpListener)

    }

    fun showEmoji() {
        if (emojiShow.get()) {
            emojiShow.set(false)
        } else {
            emojiShow.set(true)
            hideKeyboard()
        }
    }

    fun hideEmojiLayout() {
        emojiShow.set(false)
    }


    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_USER_CODE_CLICK -> atResult.set(data.getSerializableExtra(UserListActivity.DATA) as UserModel)
                REQUEST_USER_CODE_INPUT -> atResultByEnter.set(data.getSerializableExtra(UserListActivity.DATA) as UserModel)

                REQUEST_TOPIC_CODE_INPUT -> topicResultByEnter.set(data.getSerializableExtra(TopicListActivity.DATA) as TopicModel)
                REQUEST_TOPIC_CODE_CLICK -> topicResult.set(data.getSerializableExtra(TopicListActivity.DATA) as TopicModel)
            }
        }

    }

    companion object {


        val REQUEST_USER_CODE_INPUT = 1111

        val REQUEST_USER_CODE_CLICK = 2222

        val REQUEST_TOPIC_CODE_INPUT = 3333

        val REQUEST_TOPIC_CODE_CLICK = 4444
    }
}
