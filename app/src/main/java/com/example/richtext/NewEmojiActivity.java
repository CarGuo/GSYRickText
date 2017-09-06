package com.example.richtext;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.shuyu.textutillib.EmojiLayout;
import com.shuyu.textutillib.KeyBoardLockLayout;
import com.shuyu.textutillib.RichEditText;
import com.shuyu.textutillib.SmileUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewEmojiActivity extends AppCompatActivity {


    @BindView(R.id.edit_text)
    RichEditText editText;
    @BindView(R.id.keyboard_layout)
    KeyBoardLockLayout keyboardLayout;
    @BindView(R.id.emoji_layout)
    EmojiLayout emojiLayout;
    @BindView(R.id.emoji_show_bottom)
    ImageView emojiShowBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List<Integer> data = new ArrayList<>();
        List<String> strings = new ArrayList<>();
        data.add(R.drawable.gxx1);
        strings.add("[测试1]");
        data.add(R.drawable.gxx2);
        strings.add("[测试2]");
        data.add(R.drawable.gxx3);
        strings.add("[测试3]");
        data.add(R.drawable.gxx4);
        strings.add("[测试4]");
        data.add(R.drawable.gxx5);
        strings.add("[测试5]");
        data.add(R.drawable.gxx6);
        strings.add("[测试8]");
        /**初始化为自己的**/
        SmileUtils.addPatternAll(SmileUtils.getEmoticons(), strings, data);


        setContentView(R.layout.activity_input);
        ButterKnife.bind(this);

        emojiLayout.setEditTextSmile(editText);
        keyboardLayout.setBottomView(emojiLayout);
        keyboardLayout.setKeyBoardStateListener(new KeyBoardLockLayout.KeyBoardStateListener() {
            @Override
            public void onState(boolean show) {
                if (show)
                    emojiLayout.showKeyboard();
                else
                    emojiLayout.hideKeyboard();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //恢复原本的
        initEmoji();
    }

    /**
     * 处理自己的表情
     */
    private void initEmoji() {
        List<Integer> data = new ArrayList<>();
        List<String> strings = new ArrayList<>();
        for (int i = 1; i < 64; i++) {
            int resId = getResources().getIdentifier("e" + i, "drawable", getPackageName());
            data.add(resId);
            strings.add("[e" + i + "]");
        }
        /**初始化为自己的**/
        SmileUtils.addPatternAll(SmileUtils.getEmoticons(), strings, data);
    }

    @OnClick({R.id.emoji_show_bottom, R.id.edit_text})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.emoji_show_bottom:
                if (emojiLayout.getVisibility() != View.VISIBLE) {
                    keyboardLayout.showBottomViewLockHeight();
                } else {
                    keyboardLayout.hideBottomViewLockHeight();
                }
                break;
            case R.id.edit_text:
                keyboardLayout.hideBottomViewLockHeight();
                break;
        }
    }
}
