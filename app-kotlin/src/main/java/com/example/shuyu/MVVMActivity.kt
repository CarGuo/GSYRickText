package com.example.shuyu

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.shuyu.bind.RichEditTextBindingsComponent

import com.example.shuyu.contract.IMVVMView
import com.example.shuyu.databinding.ActivityMvvmBinding
import com.example.shuyu.model.MVViewModel
import com.shuyu.textutillib.listener.SpanAtUserCallBack
import com.shuyu.textutillib.listener.SpanTopicCallBack
import com.shuyu.textutillib.listener.SpanUrlCallBack
import com.shuyu.textutillib.model.TopicModel
import com.shuyu.textutillib.model.UserModel

class MVVMActivity : Activity(), IMVVMView {

    private var mvViewModel: MVViewModel? = null

    override val context: Context
        get() = this

    /**
     * 链接回调
     */
    override val spanUrlCallBack: SpanUrlCallBack = object : SpanUrlCallBack {
        override fun phone(view: View, phone: String) {
            Toast.makeText(view.context, phone + " 被点击了", Toast.LENGTH_SHORT).show()
            if (view is TextView) {
                view.highlightColor = Color.TRANSPARENT
            }
        }

        override fun url(view: View, url: String) {
            Toast.makeText(view.context, url + " 被点击了", Toast.LENGTH_SHORT).show()
            if (view is TextView) {
                view.highlightColor = Color.TRANSPARENT
            }
        }
    }

    /**
     * at回调
     */
    override val spanAtUserCallBack: SpanAtUserCallBack = object : SpanAtUserCallBack {
        override fun onClick(view: View, userModel1: UserModel) {
            Toast.makeText(view.context, userModel1.user_name + " 被点击了", Toast.LENGTH_SHORT).show()
            if (view is TextView) {
                view.highlightColor = Color.TRANSPARENT
            }
        }
    }

    /**
     * 话题回调
     */
    override val spanTopicCallBack: SpanTopicCallBack = object : SpanTopicCallBack {
        override fun onClick(view: View, topicModel: TopicModel) {
            Toast.makeText(view.context, topicModel.topicName + " 被点击了", Toast.LENGTH_SHORT).show()
            if (view is TextView) {
                view.highlightColor = Color.TRANSPARENT
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setDefaultComponent(RichEditTextBindingsComponent())
        val binding = DataBindingUtil.setContentView<ActivityMvvmBinding>(this, R.layout.activity_mvvm)

        mvViewModel = MVViewModel(this)
        binding.viewmodel = mvViewModel

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        mvViewModel?.onActivityResult(requestCode, resultCode, data)
    }
}
