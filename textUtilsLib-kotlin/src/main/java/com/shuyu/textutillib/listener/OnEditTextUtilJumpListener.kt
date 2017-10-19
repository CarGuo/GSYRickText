package com.shuyu.textutillib.listener

/**
 * 文本框输入了@的跳转
 * Created by shuyu on 2016/11/10.
 */

open interface OnEditTextUtilJumpListener {
    fun notifyAt()
    fun notifyTopic()
}
