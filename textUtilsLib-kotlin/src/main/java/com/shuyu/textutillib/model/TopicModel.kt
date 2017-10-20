package com.shuyu.textutillib.model

import java.io.Serializable

/**
 * 话题model
 * Created by guoshuyu on 2017/8/16.
 */

class TopicModel: Serializable {
    /**
     * 话题名字内部不能有#和空格
     */
    var topicName: String = ""
    var topicId: String = ""

    constructor()

    constructor(topicName: String, topicId: String) {
        this.topicName = topicName
        this.topicId = topicId
    }

    override fun toString(): String = this.topicName
}
