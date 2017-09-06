package com.shuyu.textutillib;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.widget.LinearLayout;

/**
 * 键盘高度锁定处理
 * Created by guoshuyu on 2017/9/5.
 */

public class KeyBoardLockLayout extends LinearLayout {

    private static final String TAG = KeyBoardLockLayout.class.getName();

    private final static String NAME_PREF_SOFT_KEYBOARD = TAG + "keyboard_name";

    private final static String KEY_PREF_SOFT_KEYBOARD_HEIGHT = TAG + "keyboard_name_height";

    private final static long DURATION_SWITCH_EMOTION_KEYBOARD = 150L;

    private static final int DEFAULT_SOFT_KEYBOARD_HEIGHT = 240;

    private View bottomView;

    private Context context;

    private SharedPreferences softKeyboardSharedPreferences;

    private KeyBoardStateListener keyBoardStateListener;

    public KeyBoardLockLayout(Context context) {
        super(context);
        init(context);
    }

    public KeyBoardLockLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public KeyBoardLockLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        softKeyboardSharedPreferences =
                context.getSharedPreferences(NAME_PREF_SOFT_KEYBOARD, Context.MODE_PRIVATE);
    }

    /**
     * 锁定 rootLayout 的高度
     */
    private void lockHeight() {
        LinearLayout.LayoutParams layoutParams =
                (LinearLayout.LayoutParams) getLayoutParams();
        layoutParams.height = getHeight();
        layoutParams.weight = 0.0F;
        requestLayout();
    }

    /**
     * 解锁 rootLayout 的高度
     */
    private void unlockHeight() {
        ((LinearLayout.LayoutParams) getLayoutParams()).weight = 1.0F;
        getLayoutParams().height = 0;
        requestLayout();
    }

    /**
     * 得到虚拟按键的高度
     *
     * @return 虚拟按键的高度
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private int getNavigationBarHeight() {

        WindowManager windowManager =
                (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
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
     * dip转为PX
     */
    private int dip2px(Context context, float dipValue) {
        float fontScale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * fontScale + 0.5f);
    }

    /**
     * 得到当前软键盘的高度
     *
     * @return 软键盘的高度
     */
    public int getCurrentSoftInputHeight() {

        Rect rect = new Rect();

        getRootView().getWindowVisibleDisplayFrame(rect);

        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();// 创建了一张白纸
        windowManager.getDefaultDisplay().getMetrics(outMetrics);// 给白纸设置宽高
        int screenHeight = outMetrics.heightPixels;


        int softInputHeight = screenHeight - rect.bottom;

        // Android LOLLIPOP 以上的版本才有"虚拟按键"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //softInputHeight -= getNavigationBarHeight();
        }

        if (softInputHeight < 0) {
        }

        return softInputHeight;
    }

    // 得到“软键盘”高度
    public int getSupportSoftKeyboardHeight() {

        int softKeyboardHeight = getCurrentSoftInputHeight();

        // 如果当前的键盘高度大于零，赶紧保存下来
        if (softKeyboardHeight > 0) {
            softKeyboardSharedPreferences.edit()
                    .putInt(KEY_PREF_SOFT_KEYBOARD_HEIGHT, softKeyboardHeight)
                    .apply();
        }

        // 如果当前“软键盘”高度等于零，可能是被隐藏了，也可能是我的锅，那就使用本地已经保存键盘高度
        if (softKeyboardHeight == 0) {
            softKeyboardHeight = softKeyboardSharedPreferences.getInt(KEY_PREF_SOFT_KEYBOARD_HEIGHT,
                    dip2px(context, DEFAULT_SOFT_KEYBOARD_HEIGHT));
        }

        return softKeyboardHeight;
    }

    // 软键盘是否显示
    public boolean isSoftKeyboardShown() {
        return getCurrentSoftInputHeight() != 0;
    }


    public void setBottomView(View bottomView) {
        this.bottomView = bottomView;
    }

    public void setKeyBoardStateListener(KeyBoardStateListener keyBoardStateListener) {
        this.keyBoardStateListener = keyBoardStateListener;
    }

    /**
     * 隐藏emoji键盘，显示软键盘，emojiLayout的高度
     */
    public void hideBottomViewLockHeight() {
        ObjectAnimator hideAnimator = ObjectAnimator.ofFloat(bottomView, "alpha", 1.0F, 0.0F);
        hideAnimator.setDuration(DURATION_SWITCH_EMOTION_KEYBOARD);
        hideAnimator.setInterpolator(new AccelerateInterpolator());
        hideAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                lockHeight();
                if (keyBoardStateListener != null) {
                    keyBoardStateListener.onState(true);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                bottomView.setVisibility(View.GONE);
                unlockHeight();
            }
        });
        hideAnimator.start();
    }

    /**
     * 显示 emojo，隐藏键盘(锁定emojiLayout的高度)
     */
    public void showBottomViewLockHeight() {

        bottomView.setVisibility(View.VISIBLE);
        bottomView.getLayoutParams().height = getSupportSoftKeyboardHeight();

        ObjectAnimator showAnimator = ObjectAnimator.ofFloat(bottomView, "alpha", 0.0F, 1.0F);
        showAnimator.setDuration(DURATION_SWITCH_EMOTION_KEYBOARD);
        showAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        showAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                lockHeight();
                if (keyBoardStateListener != null) {
                    keyBoardStateListener.onState(false);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                unlockHeight();
            }
        });
        showAnimator.start();
    }

    public interface KeyBoardStateListener {
        void onState(boolean show);
    }

}
