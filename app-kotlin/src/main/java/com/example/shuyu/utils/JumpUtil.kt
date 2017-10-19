package com.example.shuyu.utils

import android.app.Activity
import android.content.Intent

import com.example.shuyu.TopicListActivity
import com.example.shuyu.UserListActivity

open class JumpUtil {

    companion object {
        fun goToUserList(activity: Activity, code: Int) {
            val intent = Intent(activity, UserListActivity::class.java)
            activity.startActivityForResult(intent, code)
        }


        fun goToTopicList(activity: Activity, code: Int) {
            val intent = Intent(activity, TopicListActivity::class.java)
            activity.startActivityForResult(intent, code)
        }
    }

}