package com.example.richtext;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.richtext.utils.EditTextAtUtils;
import com.example.richtext.utils.JumpUtil;
import com.example.richtext.widget.EditTextEmoji;
import com.example.richtext.widget.EmojiLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    public final static int REQUEST_USER_CODE_INPUT = 1111;
    public final static int REQUEST_USER_CODE_CLICK = 2222;

    @BindView(R.id.emoji_edit_text)
    EditTextEmoji emojiEditText;
    @BindView(R.id.emoji_show_bottom)
    ImageView emojiShowBottom;
    @BindView(R.id.emojiLayout)
    EmojiLayout emojiLayout;
    @BindView(R.id.activity_main)
    RelativeLayout activityMain;
    @BindView(R.id.emoji_show_at)
    ImageView emojiShowAt;

    EditTextAtUtils editTextAtUtils;

    List<String> userNames = new ArrayList<>();
    List<String> userIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        emojiLayout.setEditTextSmile(emojiEditText);
        editTextAtUtils = new EditTextAtUtils(this, emojiEditText, userNames, userIds);
    }

    @OnClick({R.id.emoji_show_bottom, R.id.emoji_show_at})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.emoji_show_bottom:
                emojiLayout.hideKeyboard();
                if (emojiLayout.getVisibility() == View.VISIBLE) {
                    emojiLayout.setVisibility(View.GONE);
                } else {
                    emojiLayout.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.emoji_show_at:
                JumpUtil.goToUserList(MainActivity.this, MainActivity.REQUEST_USER_CODE_CLICK);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_USER_CODE_CLICK:
                    EditTextAtUtils.resolveAtResult(data, emojiEditText, this, editTextAtUtils);
                    break;
                case REQUEST_USER_CODE_INPUT:
                    EditTextAtUtils.resolveAtResultByEnterAt(data, emojiEditText, this, editTextAtUtils);
                    break;
            }
        }

    }
}
