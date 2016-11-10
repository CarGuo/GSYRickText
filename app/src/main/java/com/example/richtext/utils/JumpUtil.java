package com.example.richtext.utils;

import android.app.Activity;
import android.content.Intent;

import com.example.richtext.UserListActivity;

/**
 * Created by shuyu on 2016/11/10.
 */

public class JumpUtil {

    public static void goToUserList(Activity activity, int code) {
        Intent intent = new Intent(activity, UserListActivity.class);
        activity.startActivityForResult(intent, code);
    }
}
