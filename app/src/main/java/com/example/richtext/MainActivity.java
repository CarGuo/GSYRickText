package com.example.richtext;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.richtext.utils.JumpUtil;
import com.example.richtext.widget.EditTextEmoji;
import com.example.richtext.widget.EmojiLayout;
import com.shuyu.textutillib.EditTextAtUtils;
import com.shuyu.textutillib.TextCommonUtils;
import com.shuyu.textutillib.listener.EditTextAtUtilJumpListener;
import com.shuyu.textutillib.listener.SpanAtUserCallBack;
import com.shuyu.textutillib.listener.SpanTopicCallBack;
import com.shuyu.textutillib.listener.SpanUrlCallBack;
import com.shuyu.textutillib.model.TopicModel;
import com.shuyu.textutillib.model.UserModel;
import com.shuyu.textutillib.SmileUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    public final static int REQUEST_USER_CODE_INPUT = 1111;
    public final static int REQUEST_USER_CODE_CLICK = 2222;
    public final static int REQUEST_TOPIC_CODE_INPUT = 3333;
    public final static int REQUEST_TOPIC_CODE_CLICK = 4444;

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
    @BindView(R.id.rich_text)
    TextView richText;

    EditTextAtUtils editTextAtUtils;

    List<String> editNames = new ArrayList<>();
    List<String> editIds = new ArrayList<>();
    List<TopicModel> topicModels = new ArrayList<>();


    private String insertContent = "这是测试文本#话题话题#哟 www.baidu.com " +
            " 来@某个人  @22222 @kkk " +
            " 好的,来几个表情[e2][e4][e55]，最后来一个电话 13245685478";
    private List<UserModel> nameList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initEmoji();

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initView();
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

    private void initView() {
        emojiLayout.setEditTextSmile(emojiEditText);
        editTextAtUtils = new EditTextAtUtils(emojiEditText, editNames, editIds);
        editTextAtUtils.setEditTextAtUtilJumpListener(new EditTextAtUtilJumpListener() {
            @Override
            public void notifyAt() {
                JumpUtil.goToUserList(MainActivity.this, MainActivity.REQUEST_USER_CODE_INPUT);
            }

            @Override
            public void notifyTopic() {
                JumpUtil.goToTopicList(MainActivity.this, MainActivity.REQUEST_TOPIC_CODE_INPUT);
            }
        });

        resolveRichShow();
    }


    private void resolveRichShow() {
        String content = "这是测试#话题话题#文本哟 www.baidu.com " +
                "\n来@某个人  @22222 @kkk " +
                "\n好的,来几个表情[e2][e4][e55]，最后来一个电话 13245685478";

        UserModel userModel = new UserModel();
        userModel.setUser_name("22222");
        userModel.setUser_id("2222");
        nameList.add(userModel);
        userModel = new UserModel();
        userModel.setUser_name("kkk");
        userModel.setUser_id("23333");
        nameList.add(userModel);

        TopicModel topicModel = new TopicModel();
        topicModel.setTopicId("333");
        topicModel.setTopicName("话题话题");
        topicModels.add(topicModel);

        SpanUrlCallBack spanUrlCallBack = new SpanUrlCallBack() {
            @Override
            public void phone(String phone) {
                Toast.makeText(MainActivity.this, phone + " 被点击了", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void url(String url) {
                Toast.makeText(MainActivity.this, url + " 被点击了", Toast.LENGTH_SHORT).show();
            }
        };

        SpanAtUserCallBack spanAtUserCallBack = new SpanAtUserCallBack() {
            @Override
            public void onClick(UserModel userModel1) {
                Toast.makeText(MainActivity.this, userModel1.getUser_name() + " 被点击了", Toast.LENGTH_SHORT).show();
            }
        };

        SpanTopicCallBack spanTopicCallBack = new SpanTopicCallBack() {
            @Override
            public void onClick(TopicModel topicModel) {
                Toast.makeText(MainActivity.this, topicModel.getTopicName() + " 被点击了", Toast.LENGTH_SHORT).show();
            }
        };

        richText.setText(TextCommonUtils.getUrlSmileText(this, content, nameList, topicModels, richText, Color.BLUE, true, spanAtUserCallBack, spanUrlCallBack, spanTopicCallBack));

    }

    @OnClick({R.id.emoji_show_bottom, R.id.emoji_show_at, R.id.insert_text_btn, R.id.jump_btn, R.id.emoji_show_topic})
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
            case R.id.insert_text_btn:
                editIds.clear();
                editNames.clear();
                for (int i = 0; i < nameList.size(); i++) {
                    editNames.add(nameList.get(i).getUser_name());
                    editIds.add(nameList.get(i).getUser_id());
                }
                for (int i = 0; i < topicModels.size(); i++) {
                    editNames.add(topicModels.get(i).getTopicName());
                    editIds.add(topicModels.get(i).getTopicId());
                }
                EditTextAtUtils.resolveInsertText(MainActivity.this, insertContent, nameList, topicModels, "#f77521", emojiEditText);
                break;
            case R.id.jump_btn:
                Intent intent = new Intent(MainActivity.this, NewEmojiActivity.class);
                startActivity(intent);
                break;
            case R.id.emoji_show_topic:
                JumpUtil.goToTopicList(MainActivity.this, MainActivity.REQUEST_TOPIC_CODE_CLICK);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_USER_CODE_CLICK:
                    EditTextAtUtils.resolveAtResult(editTextAtUtils, "#f77500", (UserModel) data.getSerializableExtra(UserListActivity.DATA));
                    break;
                case REQUEST_USER_CODE_INPUT:
                    EditTextAtUtils.resolveAtResultByEnterAt(emojiEditText, editTextAtUtils, "#f77500", (UserModel) data.getSerializableExtra(UserListActivity.DATA));
                    break;

                case REQUEST_TOPIC_CODE_INPUT:
                    EditTextAtUtils.resolveTopicResultByEnter(emojiEditText, editTextAtUtils, "#f77500", (TopicModel) data.getSerializableExtra(TopicListActivity.DATA));
                    break;
                case REQUEST_TOPIC_CODE_CLICK:
                    EditTextAtUtils.resolveTopicResult(editTextAtUtils, "#f77500", (TopicModel) data.getSerializableExtra(TopicListActivity.DATA));
                    break;
            }
        }

    }
}
