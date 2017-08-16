package com.shuyu.textutillib.model;

import java.io.Serializable;

/**
 * 话题model
 * Created by guoshuyu on 2017/8/16.
 */

public class TopicModel implements Serializable {
    private String topicName;
    private String topicId;

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    @Override
    public String toString() {
        return this.topicName;
    }
}
