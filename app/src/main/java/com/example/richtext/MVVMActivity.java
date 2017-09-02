package com.example.richtext;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.richtext.contract.IMVVMView;
import com.example.richtext.databinding.ActivityMvvmBinding;
import com.example.richtext.model.MVViewModel;
import com.shuyu.textutillib.listener.SpanAtUserCallBack;
import com.shuyu.textutillib.listener.SpanTopicCallBack;
import com.shuyu.textutillib.listener.SpanUrlCallBack;
import com.shuyu.textutillib.model.TopicModel;
import com.shuyu.textutillib.model.UserModel;

public class MVVMActivity extends Activity implements IMVVMView {

    MVViewModel mvViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMvvmBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_mvvm);

        mvViewModel = new MVViewModel(this);
        binding.setViewmodel(mvViewModel);

    }

    @Override
    public SpanTopicCallBack getSpanTopicCallBack() {
        return spanTopicCallBack;
    }

    @Override
    public SpanAtUserCallBack getSpanAtUserCallBack() {
        return spanAtUserCallBack;
    }

    @Override
    public SpanUrlCallBack getSpanUrlCallBack() {
        return spanUrlCallBack;
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mvViewModel.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 链接回调
     */
    private SpanUrlCallBack spanUrlCallBack = new SpanUrlCallBack() {
        @Override
        public void phone(View view, String phone) {
            Toast.makeText(view.getContext(), phone + " 被点击了", Toast.LENGTH_SHORT).show();
            if (view instanceof TextView) {
                ((TextView) view).setHighlightColor(Color.TRANSPARENT);
            }
        }

        @Override
        public void url(View view, String url) {
            Toast.makeText(view.getContext(), url + " 被点击了", Toast.LENGTH_SHORT).show();
            if (view instanceof TextView) {
                ((TextView) view).setHighlightColor(Color.TRANSPARENT);
            }
        }
    };

    /**
     * at回调
     */
    private SpanAtUserCallBack spanAtUserCallBack = new SpanAtUserCallBack() {
        @Override
        public void onClick(View view, UserModel userModel1) {
            Toast.makeText(view.getContext(), userModel1.getUser_name() + " 被点击了", Toast.LENGTH_SHORT).show();
            if (view instanceof TextView) {
                ((TextView) view).setHighlightColor(Color.TRANSPARENT);
            }
        }
    };

    /**
     * 话题回调
     */
    private SpanTopicCallBack spanTopicCallBack = new SpanTopicCallBack() {
        @Override
        public void onClick(View view, TopicModel topicModel) {
            Toast.makeText(view.getContext(), topicModel.getTopicName() + " 被点击了", Toast.LENGTH_SHORT).show();
            if (view instanceof TextView) {
                ((TextView) view).setHighlightColor(Color.TRANSPARENT);
            }
        }
    };
}
