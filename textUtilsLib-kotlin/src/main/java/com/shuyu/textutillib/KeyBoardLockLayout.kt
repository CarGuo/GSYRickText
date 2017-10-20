package com.shuyu.textutillib

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.annotation.TargetApi
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Rect
import android.os.Build
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.widget.LinearLayout

/**
 * 键盘高度锁定处理
 * Created by guoshuyu on 2017/9/5.
 */

class KeyBoardLockLayout : LinearLayout {

    private var bottomView: View? = null

    private var softKeyboardSharedPreferences: SharedPreferences? = null

    private var keyBoardStateListener: KeyBoardStateListener? = null

    /**
     * 得到虚拟按键的高度
     *
     * @return 虚拟按键的高度
     */
    private// 获取可用的高度
            // 获取实际的高度
    val navigationBarHeight: Int
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
        get() {

            val windowManager = context!!.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val defaultDisplayMetrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(defaultDisplayMetrics)
            val usableHeight = defaultDisplayMetrics.heightPixels
            val realDisplayMetrics = DisplayMetrics()
            windowManager.defaultDisplay.getRealMetrics(realDisplayMetrics)
            val realHeight = realDisplayMetrics.heightPixels

            return if (realHeight > usableHeight) realHeight - usableHeight else 0
        }

    /**
     * 得到当前软键盘的高度
     *
     * @return 软键盘的高度
     */
    // 创建了一张白纸
    // 给白纸设置宽高
    // Android LOLLIPOP 以上的版本才有"虚拟按键"
    //softInputHeight -= getNavigationBarHeight();
    val currentSoftInputHeight: Int
        get() {

            val rect = Rect()

            rootView.getWindowVisibleDisplayFrame(rect)

            val windowManager = context!!.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val outMetrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(outMetrics)
            val screenHeight = outMetrics.heightPixels


            val softInputHeight = screenHeight - rect.bottom
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            }

            if (softInputHeight < 0) {
            }

            return softInputHeight
        }

    // 得到“软键盘”高度
    // 如果当前的键盘高度大于零，赶紧保存下来
    // 如果当前“软键盘”高度等于零，可能是被隐藏了，也可能是我的锅，那就使用本地已经保存键盘高度
    val supportSoftKeyboardHeight: Int
        get() {

            var softKeyboardHeight = currentSoftInputHeight
            if (softKeyboardHeight > 0) {
                softKeyboardSharedPreferences!!.edit()
                        .putInt(KEY_PREF_SOFT_KEYBOARD_HEIGHT, softKeyboardHeight)
                        .apply()
            }
            if (softKeyboardHeight == 0) {
                softKeyboardHeight = softKeyboardSharedPreferences!!.getInt(KEY_PREF_SOFT_KEYBOARD_HEIGHT,
                        dip2px(context, DEFAULT_SOFT_KEYBOARD_HEIGHT.toFloat()))
            }

            return softKeyboardHeight
        }

    // 软键盘是否显示
    val isSoftKeyboardShown: Boolean
        get() = currentSoftInputHeight != 0

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    private fun init(context: Context) {
        softKeyboardSharedPreferences = context.getSharedPreferences(NAME_PREF_SOFT_KEYBOARD, Context.MODE_PRIVATE)
    }

    /**
     * 锁定 rootLayout 的高度
     */
    private fun lockHeight() {
        val layoutParams = layoutParams as LinearLayout.LayoutParams
        layoutParams.height = height
        layoutParams.weight = 0.0f
        requestLayout()
    }

    /**
     * 解锁 rootLayout 的高度
     */
    private fun unlockHeight() {
        (layoutParams as LinearLayout.LayoutParams).weight = 1.0f
        layoutParams.height = 0
        requestLayout()
    }


    /**
     * dip转为PX
     */
    private fun dip2px(context: Context?, dipValue: Float): Int {
        val fontScale = context!!.resources.displayMetrics.density
        return (dipValue * fontScale + 0.5f).toInt()
    }


    fun setBottomView(bottomView: View) {
        this.bottomView = bottomView
    }

    fun setKeyBoardStateListener(keyBoardStateListener: KeyBoardStateListener) {
        this.keyBoardStateListener = keyBoardStateListener
    }

    /**
     * 隐藏emoji键盘，显示软键盘，emojiLayout的高度
     */
    fun hideBottomViewLockHeight() {
        val hideAnimator = ObjectAnimator.ofFloat(bottomView, "alpha", 1.0f, 0.0f)
        hideAnimator.duration = DURATION_SWITCH_EMOTION_KEYBOARD
        hideAnimator.interpolator = AccelerateInterpolator()
        hideAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                lockHeight()
                if (keyBoardStateListener != null) {
                    keyBoardStateListener!!.onState(true)
                }
            }

            override fun onAnimationEnd(animation: Animator) {
                bottomView!!.visibility = View.GONE
                unlockHeight()
            }
        })
        hideAnimator.start()
    }

    /**
     * 显示 emojo，隐藏键盘(锁定emojiLayout的高度)
     */
    fun showBottomViewLockHeight() {

        bottomView!!.visibility = View.VISIBLE
        bottomView!!.layoutParams.height = supportSoftKeyboardHeight

        val showAnimator = ObjectAnimator.ofFloat(bottomView, "alpha", 0.0f, 1.0f)
        showAnimator.duration = DURATION_SWITCH_EMOTION_KEYBOARD
        showAnimator.interpolator = AccelerateDecelerateInterpolator()
        showAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                lockHeight()
                if (keyBoardStateListener != null) {
                    keyBoardStateListener!!.onState(false)
                }
            }

            override fun onAnimationEnd(animation: Animator) {
                unlockHeight()
            }
        })
        showAnimator.start()
    }

    interface KeyBoardStateListener {
        fun onState(show: Boolean)
    }

    companion object {

        private val TAG = KeyBoardLockLayout::class.java.name

        private val NAME_PREF_SOFT_KEYBOARD = TAG + "keyboard_name"

        private val KEY_PREF_SOFT_KEYBOARD_HEIGHT = TAG + "keyboard_name_height"

        private val DURATION_SWITCH_EMOTION_KEYBOARD = 150L

        private val DEFAULT_SOFT_KEYBOARD_HEIGHT = 240
    }

}
