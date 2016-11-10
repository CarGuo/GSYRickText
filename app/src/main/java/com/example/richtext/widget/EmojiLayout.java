package com.example.richtext.widget;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.richtext.R;
import com.example.richtext.adapter.ExpressionPagerAdapter;
import com.example.richtext.adapter.SmileImageExpressionAdapter;
import com.example.richtext.utils.ScreenUtils;
import com.shuyu.textutillib.utils.SmileUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by shuyu on 2016/9/2.
 */

public class EmojiLayout extends LinearLayout {


    @BindView(R.id.edittext_bar_vPager)
    ViewPager edittextBarVPager;
    @BindView(R.id.edittext_bar_viewGroup_face)
    LinearLayout edittextBarViewGroupFace;
    @BindView(R.id.edittext_bar_ll_face_container)
    LinearLayout edittextBarLlFaceContainer;
    @BindView(R.id.edittext_bar_more)
    LinearLayout edittextBarMore;

    private EditTextEmoji editTextEmoji;
    private List<String> reslist;
    private ImageView[] imageFaceViews;

    public EmojiLayout(Context context) {
        super(context);
        init(context);
    }

    public EmojiLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EmojiLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_emoji_container, this, true);
        if (isInEditMode())
            return;
        ButterKnife.bind(this, view);
        initViews();

    }


    /**
     * 初始化View
     */
    private void initViews() {
        int size = ScreenUtils.dip2px(getContext(), 5);
        int marginSize = ScreenUtils.dip2px(getContext(), 5);

        // 表情list
        reslist = getExpressionRes(SmileUtils.getEmoticons().size());
        // 初始化表情viewpager
        List<View> views = new ArrayList<View>();
        View gv1 = getGridChildView(1);
        View gv2 = getGridChildView(2);
        View gv3 = getGridChildView(3);
        View gv4 = getGridChildView(4);
        views.add(gv1);
        views.add(gv2);
        views.add(gv3);
        views.add(gv4);

        ImageView imageViewFace;
        imageFaceViews = new ImageView[views.size()];
        for (int i = 0; i < views.size(); i++) {
            LayoutParams margin = new LayoutParams(size, size);
            margin.setMargins(marginSize, 0, 0, 0);
            imageViewFace = new ImageView(getContext());
            imageViewFace.setLayoutParams(new ViewGroup.LayoutParams(size, size));
            imageFaceViews[i] = imageViewFace;
            if (i == 0) {
                imageFaceViews[i].setBackgroundResource(R.drawable.page_indicator_focused);
            } else {
                imageFaceViews[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
            }
            edittextBarViewGroupFace.addView(imageFaceViews[i], margin);
        }

        edittextBarVPager.setAdapter(new ExpressionPagerAdapter(views));
        edittextBarVPager.addOnPageChangeListener(new GuidePageChangeListener());

    }


    public List<String> getExpressionRes(int getSum) {
        List<String> reslist = new ArrayList<String>();
        for (int x = 1; x <= getSum; x++) {
            String filename = "e" + x;
            reslist.add(filename);

        }
        return reslist;

    }

    /**
     * 获取表情的gridview的子view
     */
    private View getGridChildView(int i) {
        View view = View.inflate(getContext(), R.layout.expression_gridview, null);
        LockGridView gv = (LockGridView) view.findViewById(R.id.gridview);
        List<String> list = new ArrayList<String>();

        int startInd = (i - 1) * 20;
        if ((startInd + 20) >= reslist.size()) {
            list.addAll(reslist.subList(startInd, startInd + (reslist.size() - startInd)));
        } else {
            list.addAll(reslist.subList(startInd, startInd + 20));
        }
        list.add("delete_expression");
        final SmileImageExpressionAdapter smileImageExpressionAdapter = new SmileImageExpressionAdapter(getContext(), 1, list);
        gv.setAdapter(smileImageExpressionAdapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String filename = smileImageExpressionAdapter.getItem(position);
                try {
                    if (filename != "delete_expression") { // 不是删除键，显示表情
                        // 这里用的反射，所以混淆的时候不要混淆SmileUtils这个类
                        @SuppressWarnings("rawtypes")
                        Class clz = Class.forName("com.shuyu.textutillib.utils.SmileUtils");
                        Field field = clz.getField(filename);

                        String temp1 = (String) field.get(null);
                        /**暂时屏蔽显示符号*/
                        editTextEmoji.insertIcon(filename, temp1);

                    } else { // 删除文字或者表情
                        if (!TextUtils.isEmpty(editTextEmoji.getText())) {

                            int selectionStart = editTextEmoji.getSelectionStart();// 获取光标的位置
                            if (selectionStart > 0) {
                                String body = editTextEmoji.getText().toString();
                                String tempStr = body.substring(0, selectionStart);
                                int i = tempStr.lastIndexOf("[");// 获取最后一个表情的位置
                                if (i != -1) {
                                    CharSequence cs = tempStr.substring(i, selectionStart);
                                    if (SmileUtils.containsKey(cs.toString()))
                                        editTextEmoji.getEditableText().delete(i, selectionStart);
                                    else
                                        editTextEmoji.getEditableText().delete(selectionStart - 1,
                                                selectionStart);
                                } else {
                                    editTextEmoji.getEditableText().delete(selectionStart - 1, selectionStart);
                                }
                            }
                        }

                    }

                } catch (Exception e) {
                }

            }
        });
        return view;
    }

    class GuidePageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int arg0) {

            for (int i = 0; i < imageFaceViews.length; i++) {
                imageFaceViews[arg0].setBackgroundResource(R.drawable.page_indicator_focused);

                if (arg0 != i) {
                    imageFaceViews[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
                }
            }
        }
    }

    /**
     * 隐藏软键盘
     */
    public void hideKeyboard() {
        Activity context = (Activity) getContext();
        if (context.getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (context.getCurrentFocus() != null) {
                ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).
                        hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * 显示键盘
     */
    public void showKeyboard() {
        editTextEmoji.requestFocus();
        InputMethodManager inputManager =
                (InputMethodManager) editTextEmoji.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(editTextEmoji, 0);
    }


    public EditTextEmoji getEditTextSmile() {
        return editTextEmoji;
    }

    public void setEditTextSmile(EditTextEmoji editTextSmile) {
        this.editTextEmoji = editTextSmile;
        editTextSmile.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setVisibility(GONE);
            }
        });
    }
}
