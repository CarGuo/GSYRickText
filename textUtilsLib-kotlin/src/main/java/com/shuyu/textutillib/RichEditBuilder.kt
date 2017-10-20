package com.shuyu.textutillib


import com.shuyu.textutillib.listener.OnEditTextUtilJumpListener
import com.shuyu.textutillib.model.TopicModel
import com.shuyu.textutillib.model.UserModel

/**
 * 富文本设置 话题、at某人，链接识别
 * Created by guoshuyu on 2017/8/18.
 */

class RichEditBuilder {

    private var editText: RichEditText? = null

    private var userModels: MutableList<UserModel>? = null

    private var topicModels: MutableList<TopicModel>? = null

    private var editTextAtUtilJumpListener: OnEditTextUtilJumpListener? = null

    /**
     * At颜色
     */
    private var colorTopic = "#0000FF"
    /**
     * 话题颜色
     */
    private var colorAtUser = "#f77521"

    /**
     * 输入框
     */
    fun setEditText(editText: RichEditText): RichEditBuilder {
        this.editText = editText
        return this
    }

    /**
     * at列表
     */
    fun setUserModels(userModels: MutableList<UserModel>): RichEditBuilder {
        this.userModels = userModels
        return this
    }

    /**
     * 话题列表
     */
    fun setTopicModels(topicModels: MutableList<TopicModel>): RichEditBuilder {
        this.topicModels = topicModels
        return this
    }

    /**
     * 输入监听回调
     */
    fun setEditTextAtUtilJumpListener(editTextAtUtilJumpListener: OnEditTextUtilJumpListener): RichEditBuilder {
        this.editTextAtUtilJumpListener = editTextAtUtilJumpListener
        return this
    }

    /**
     * 话题颜色
     */
    fun setColorTopic(colorTopic: String): RichEditBuilder {
        this.colorTopic = colorTopic
        return this
    }

    /**
     * at颜色
     */
    fun setColorAtUser(colorAtUser: String): RichEditBuilder {
        this.colorAtUser = colorAtUser
        return this
    }

    fun builder(): RichEditText {
        editText?.setEditTextAtUtilJumpListener(editTextAtUtilJumpListener)
        editText?.setModelList(userModels, topicModels)
        editText?.setColorAtUser(colorAtUser)
        editText?.setColorTopic(colorTopic)
        return editText!!
    }

}
