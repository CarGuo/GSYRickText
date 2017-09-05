package com.shuyu.textutillib;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

/**
 * Created by guoshuyu on 2017/9/5.
 */

public class KeyBoardLockLayout extends LinearLayout {

    private final static long DURATION_SWITCH_EMOTION_KEYBOARD = 150L;

    private EmojiLayout emojiLayout;

    private SoftKeyBoardHelper softKeyBoardHelper;

    public KeyBoardLockLayout(Context context) {
        super(context);
        init();
    }

    public KeyBoardLockLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public KeyBoardLockLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        softKeyBoardHelper = new SoftKeyBoardHelper(getContext(), this);
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

    public void setEmojiLayout(EmojiLayout emojiLayout) {
        this.emojiLayout = emojiLayout;
    }

    /**
     * 隐藏emoji键盘，显示软键盘，emojiLayout的高度
     */
    public void hideEmojiKeyLockHeight() {
        ObjectAnimator hideAnimator = ObjectAnimator.ofFloat(emojiLayout, "alpha", 1.0F, 0.0F);
        hideAnimator.setDuration(DURATION_SWITCH_EMOTION_KEYBOARD);
        hideAnimator.setInterpolator(new AccelerateInterpolator());
        hideAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                lockHeight();
                emojiLayout.showKeyboard();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                emojiLayout.setVisibility(View.GONE);
                unlockHeight();
            }
        });
        hideAnimator.start();
    }

    /**
     * 显示 emojo，隐藏键盘(锁定emojiLayout的高度)
     */
    public void showEmojiKeyLockHeight() {

        emojiLayout.setVisibility(View.VISIBLE);
        emojiLayout.getLayoutParams().height = softKeyBoardHelper.getSupportSoftKeyboardHeight();

        ObjectAnimator showAnimator = ObjectAnimator.ofFloat(emojiLayout, "alpha", 0.0F, 1.0F);
        showAnimator.setDuration(DURATION_SWITCH_EMOTION_KEYBOARD);
        showAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        showAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                lockHeight();
                emojiLayout.hideKeyboard();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                unlockHeight();
            }
        });
        showAnimator.start();
    }


}
