package com.shuyu.textutillib;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;


/**
 * Created by shuyu on 2017/9/5.
 */
public class SoftKeyBoardHelper {

    private static final String TAG = SoftKeyBoardHelper.class.getName();

    private static final int DEFAULT_SOFT_KEYBOARD_HEIGHT = 240;

    private final static String NAME_PREF_SOFT_KEYBOARD = "name_pref_soft_keyboard";

    private final static String KEY_PREF_SOFT_KEYBOARD_HEIGHT = "key_pref_soft_keyboard_height";

    private Context mActivity;

    private View mView;

    private SharedPreferences mSoftKeyboardSharedPreferences;

    public SoftKeyBoardHelper(Context context, View view) {
        mActivity = context;
        mView = view;
        mSoftKeyboardSharedPreferences =
                context.getSharedPreferences(NAME_PREF_SOFT_KEYBOARD, Context.MODE_PRIVATE);
    }

    // 得到“软键盘”高度
    public int getSupportSoftKeyboardHeight() {

        int softKeyboardHeight = getCurrentSoftInputHeight();

        // 如果当前的键盘高度大于零，赶紧保存下来
        if (softKeyboardHeight > 0) {
            mSoftKeyboardSharedPreferences.edit()
                    .putInt(KEY_PREF_SOFT_KEYBOARD_HEIGHT, softKeyboardHeight)
                    .apply();
        }

        // 如果当前“软键盘”高度等于零，可能是被隐藏了，也可能是我的锅，那就使用本地已经保存键盘高度
        if (softKeyboardHeight == 0) {
            softKeyboardHeight = mSoftKeyboardSharedPreferences.getInt(KEY_PREF_SOFT_KEYBOARD_HEIGHT,
                    dip2px(mActivity, DEFAULT_SOFT_KEYBOARD_HEIGHT));
        }

        return softKeyboardHeight;
    }

    // 软键盘是否显示
    public boolean isSoftKeyboardShown() {
        return getCurrentSoftInputHeight() != 0;
    }

    /**
     * 得到虚拟按键的高度
     *
     * @return 虚拟按键的高度
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private int getNavigationBarHeight() {

        WindowManager windowManager =
                (WindowManager) mActivity.getSystemService(Context.WINDOW_SERVICE);
        // 获取可用的高度
        DisplayMetrics defaultDisplayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(defaultDisplayMetrics);
        int usableHeight = defaultDisplayMetrics.heightPixels;

        // 获取实际的高度
        DisplayMetrics realDisplayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getRealMetrics(realDisplayMetrics);
        int realHeight = realDisplayMetrics.heightPixels;

        return realHeight > usableHeight ? realHeight - usableHeight : 0;
    }

    /**
     * 得到当前软键盘的高度
     *
     * @return 软键盘的高度
     */
    public int getCurrentSoftInputHeight() {

        Rect rect = new Rect();

        mView.getRootView().getWindowVisibleDisplayFrame(rect);

        WindowManager windowManager = (WindowManager) mActivity.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();// 创建了一张白纸
        windowManager.getDefaultDisplay().getMetrics(outMetrics);// 给白纸设置宽高
        int screenHeight = outMetrics.heightPixels;


        int softInputHeight = screenHeight - rect.bottom;

        // Android LOLLIPOP 以上的版本才有"虚拟按键"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //softInputHeight -= getNavigationBarHeight();
        }

        // excuse me?
        if (softInputHeight < 0) {
            Log.e(TAG, "excuse me，键盘高度小于0？");
        }

        return softInputHeight;
    }

    /**
     * dip转为PX
     */
    private int dip2px(Context context, float dipValue) {
        float fontScale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * fontScale + 0.5f);
    }
}
