package com.shuyu.textutillib

import android.app.Activity
import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.support.v4.view.ViewPager
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.LinearLayout


import com.shuyu.textutillib.adapter.ExpressionPagerAdapter
import com.shuyu.textutillib.adapter.SmileImageExpressionAdapter

import java.util.ArrayList


/**
 * 承载表情布局
 * Created by shuyu on 2016/9/2.
 */

class EmojiLayout : LinearLayout {

    private var edittextBarVPager: ViewPager? = null
    var edittextBarViewGroupFace: LinearLayout? = null
        private set
    var edittextBarLlFaceContainer: LinearLayout? = null
        private set
    var edittextBarMore: LinearLayout? = null
        private set

    var editTextEmoji: RichEditText? = null
        private set
    private var reslist: List<String>? = null
    private var imageFaceViews: Array<ImageView?>? = null


    private var focusIndicator: Drawable? = null
    private var unFocusIndicator: Drawable? = null
    private var deleteIconName = "delete_expression"

    private var richMarginBottom: Int = 0
    private var richMarginTop: Int = 0

    private var numColumns = 7

    private var numRows = 3

    private var pageCount = numColumns * numRows - 1


    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }


    private fun init(context: Context, attrs: AttributeSet?) {
        LayoutInflater.from(context).inflate(R.layout.rich_layout_emoji_container, this, true)
        if (isInEditMode)
            return


        edittextBarVPager = findViewById(R.id.edittext_bar_vPager) as ViewPager

        edittextBarViewGroupFace = findViewById(R.id.edittext_bar_viewGroup_face) as LinearLayout

        edittextBarLlFaceContainer = findViewById(R.id.edittext_bar_ll_face_container) as LinearLayout

        edittextBarMore = findViewById(R.id.edittext_bar_more) as LinearLayout

        if (attrs != null) {
            val array = context.obtainStyledAttributes(attrs, R.styleable.EmojiLayout)
            val deleteIconName = array.getString(R.styleable.EmojiLayout_richDeleteIconName)
            if (!TextUtils.isEmpty(deleteIconName)) {
                this.deleteIconName = deleteIconName
            }
            focusIndicator = array.getDrawable(R.styleable.EmojiLayout_richIndicatorFocus)
            unFocusIndicator = array.getDrawable(R.styleable.EmojiLayout_richIndicatorUnFocus)
            richMarginBottom = array.getDimension(R.styleable.EmojiLayout_richMarginBottom, dip2px(getContext(), 8f).toFloat()).toInt()
            richMarginTop = array.getDimension(R.styleable.EmojiLayout_richMarginTop, dip2px(getContext(), 15f).toFloat()).toInt()
            numColumns = array.getInteger(R.styleable.EmojiLayout_richLayoutNumColumns, 7)
            numRows = array.getInteger(R.styleable.EmojiLayout_richLayoutNumRows, 3)
            pageCount = numColumns * numRows - 1
            array.recycle()
        }

        if (focusIndicator == null) {
            focusIndicator = getContext().resources.getDrawable(R.drawable.rich_page_indicator_focused)
        }

        if (unFocusIndicator == null) {
            unFocusIndicator = getContext().resources.getDrawable(R.drawable.rich_page_indicator_unfocused)
        }

        initViews()

    }


    /**
     * 初始化View
     */
    private fun initViews() {

        val size = dip2px(context, 5f)

        val marginSize = dip2px(context, 5f)

        // 表情list
        reslist = SmileUtils.getTextList()

        val viewSize = Math.ceil((reslist!!.size * 1.0f / pageCount).toDouble()).toInt()

        // 初始化表情viewpager
        val views = (0 until viewSize).map { getGridChildView(it + 1) }

        var imageViewFace: ImageView
        imageFaceViews = arrayOfNulls(views.size)
        for (i in views.indices) {
            val margin = LinearLayout.LayoutParams(size, size)
            margin.setMargins(marginSize, 0, 0, 0)
            imageViewFace = ImageView(context)
            imageViewFace.layoutParams = ViewGroup.LayoutParams(size, size)
            imageFaceViews!![i] = imageViewFace
            if (i == 0) {
                imageFaceViews!![i]?.background = focusIndicator
            } else {
                imageFaceViews!![i]?.background = unFocusIndicator
            }
            edittextBarViewGroupFace!!.addView(imageFaceViews!![i], margin)
        }

        edittextBarVPager!!.adapter = ExpressionPagerAdapter(views)
        edittextBarVPager!!.addOnPageChangeListener(GuidePageChangeListener())

    }


    /**
     * 获取表情的gridview的子view
     */
    private fun getGridChildView(i: Int): View {
        val view = View.inflate(context, R.layout.rich_expression_gridview, null)
        val gv = view.findViewById(R.id.gridview) as LockGridView
        gv.numColumns = numColumns
        val layoutParams = gv.layoutParams as LinearLayout.LayoutParams
        layoutParams.setMargins(0, richMarginTop, 0, richMarginBottom)

        val list = ArrayList<String>()

        val startInd = (i - 1) * pageCount
        if (startInd + pageCount >= reslist!!.size) {
            list.addAll(reslist!!.subList(startInd, startInd + (reslist!!.size - startInd)))
        } else {
            list.addAll(reslist!!.subList(startInd, startInd + pageCount))
        }
        list.add(deleteIconName)
        val smileImageExpressionAdapter = SmileImageExpressionAdapter(context, 1, list)
        gv.adapter = smileImageExpressionAdapter
        gv.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val filename = smileImageExpressionAdapter.getItem(position)
            try {
                if (deleteIconName != filename) { // 不是删除键，显示表情
                    editTextEmoji?.insertIcon(filename)
                } else { // 删除文字或者表情
                    if (!TextUtils.isEmpty(editTextEmoji!!.text)) {

                        val selectionStart = editTextEmoji!!.selectionStart// 获取光标的位置
                        if (selectionStart > 0) {
                            val body = editTextEmoji!!.text.toString()
                            val tempStr = body.substring(0, selectionStart)
                            val i = tempStr.lastIndexOf("[")// 获取最后一个表情的位置
                            val end = tempStr.lastIndexOf("]")// 获取最后一个表情的位置
                            if (i != -1 && end == selectionStart - 1) {
                                val cs = tempStr.substring(i, selectionStart)
                                if (SmileUtils.containsKey(cs))
                                    editTextEmoji!!.editableText.delete(i, selectionStart)
                                else
                                    editTextEmoji!!.editableText.delete(selectionStart - 1,
                                            selectionStart)
                            } else {
                                editTextEmoji!!.editableText.delete(selectionStart - 1, selectionStart)
                            }
                        }
                    }

                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return view
    }

    /**
     * dip转为PX
     */
    private fun dip2px(context: Context, dipValue: Float): Int {
        val fontScale = context.resources.displayMetrics.density
        return (dipValue * fontScale + 0.5f).toInt()
    }

    private inner class GuidePageChangeListener : ViewPager.OnPageChangeListener {

        override fun onPageScrollStateChanged(arg0: Int) {}

        override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {

        }

        override fun onPageSelected(arg0: Int) {
            for (i in imageFaceViews!!.indices) {
                imageFaceViews!![arg0]?.background = focusIndicator
                if (arg0 != i) {
                    imageFaceViews!![i]?.background = unFocusIndicator
                }
            }
        }
    }

    /**
     * 隐藏软键盘
     */
    fun hideKeyboard() {
        val context = context as Activity
        if (context.window.attributes.softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (context.currentFocus != null) {
                (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(context.currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            }
        }
    }

    /**
     * 显示键盘
     */
    fun showKeyboard() {
        editTextEmoji!!.requestFocus()
        val inputManager = editTextEmoji!!.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.showSoftInput(editTextEmoji, 0)
    }


    fun getEditTextSmile(): RichEditText? = editTextEmoji

    fun setEditTextSmile(editTextSmile: RichEditText) {
        this.editTextEmoji = editTextSmile
    }
}
