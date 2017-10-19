package com.shuyu.textutillib.listener

import android.view.View

import com.shuyu.textutillib.model.UserModel

/**
 * 处理@被某人的回调
 * Created by shuyu on 2016/11/10.
 */

open interface SpanAtUserCallBack {
    fun onClick(view: View, userModel1: UserModel)
}
