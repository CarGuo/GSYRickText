package com.example.shuyu

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.shuyu.span.CustomClickAtUserSpan
import com.example.shuyu.span.CustomClickTopicSpan
import com.example.shuyu.span.CustomLinkSpan
import com.example.shuyu.utils.JumpUtil
import com.example.shuyu.utils.ScreenUtils
import com.shuyu.textutillib.RichEditBuilder
import com.shuyu.textutillib.RichTextBuilder
import com.shuyu.textutillib.SmileUtils
import com.shuyu.textutillib.listener.*
import com.shuyu.textutillib.model.TopicModel
import com.shuyu.textutillib.model.UserModel
import com.shuyu.textutillib.span.ClickAtUserSpan
import com.shuyu.textutillib.span.ClickTopicSpan
import com.shuyu.textutillib.span.LinkSpan
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        val REQUEST_USER_CODE_INPUT = 1111
        val REQUEST_USER_CODE_CLICK = 2222
        val REQUEST_TOPIC_CODE_INPUT = 3333
        val REQUEST_TOPIC_CODE_CLICK = 4444
    }

    private val topicModels = ArrayList<TopicModel>()

    private val nameList = ArrayList<UserModel>()


    private val topicModelsEd = ArrayList<TopicModel>()

    private val nameListEd = ArrayList<UserModel>()


    private val insertContent = "这是测试文本#话题话题#哟 www.baidu.com " +
            " 来@某个人  @22222 @kkk " +
            " 好的,来几个表情[e2][e4][e55]，最后来一个电话 13245685478"


    private val spanCreateListener = object : SpanCreateListener {
        override fun getCustomClickAtUserSpan(context: Context, userModel: UserModel, color: Int, spanClickCallBack: SpanAtUserCallBack): ClickAtUserSpan =
                CustomClickAtUserSpan(context, userModel, color, spanClickCallBack)

        override fun getCustomClickTopicSpan(context: Context, topicModel: TopicModel, color: Int, spanTopicCallBack: SpanTopicCallBack): ClickTopicSpan =
                CustomClickTopicSpan(context, topicModel, color, spanTopicCallBack)

        override fun getCustomLinkSpan(context: Context, url: String, color: Int, spanUrlCallBack: SpanUrlCallBack): LinkSpan =
                CustomLinkSpan(context, url, color, spanUrlCallBack)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        initEmoji()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }


    /**
     * 处理自己的表情
     */
    private fun initEmoji() {
        val data = ArrayList<Int>()
        val strings = ArrayList<String>()
        for (i in 1..63) {
            val resId = resources.getIdentifier("e" + i, "drawable", packageName)
            data.add(resId)
            strings.add("[e$i]")
        }
        /**初始化为自己的**/
        SmileUtils.addPatternAll(SmileUtils.emoticons, strings, data)
    }

    private fun initView() {
        emojiLayout.setEditTextSmile(emojiEditText)
        val richEditBuilder = RichEditBuilder()
        richEditBuilder.setEditText(emojiEditText)
                .setTopicModels(topicModelsEd)
                .setUserModels(nameListEd)
                .setColorAtUser("#FF00C0")
                .setColorTopic("#F0F0C0")
                .setEditTextAtUtilJumpListener(object : OnEditTextUtilJumpListener {
                    override fun notifyAt() {
                        JumpUtil.goToUserList(this@MainActivity, MainActivity.REQUEST_USER_CODE_INPUT)
                    }

                    override fun notifyTopic() {
                        JumpUtil.goToTopicList(this@MainActivity, MainActivity.REQUEST_TOPIC_CODE_INPUT)
                    }
                })
                .builder()
        resolveRichShow()

        emojiShowBottom.setOnClickListener(this)
        emojiShowAt.setOnClickListener(this)
        insertTextBtn.setOnClickListener(this)
        emojiShowTopic.setOnClickListener(this)
        emojiEditText.setOnClickListener(this)
        jumpMvvm.setOnClickListener(this)
    }

    private fun resolveRichShow() {

        initData()

        val content = "这是测试#话题话题#文本哟 www.baidu.com " +
                "\n来@某个人  @22222 @kkk " +
                "\n好的,来几个表情[e2][e4][e55]，最后来一个电话 13245685478"

        val spanUrlCallBack = object : SpanUrlCallBack {
            override fun phone(view: View, phone: String) {
                Toast.makeText(view.context, phone + " 被点击了", Toast.LENGTH_SHORT).show()
                if (view is TextView) {
                    view.highlightColor = Color.TRANSPARENT
                }
            }

            override fun url(view: View, url: String) {
                Toast.makeText(view.context, url + " 被点击了", Toast.LENGTH_SHORT).show()
                if (view is TextView) {
                    view.highlightColor = Color.TRANSPARENT
                }
            }
        }

        val spanAtUserCallBack = object : SpanAtUserCallBack {
            override fun onClick(view: View, userModel1: UserModel) {
                Toast.makeText(view.context, userModel1.user_name + " 被点击了", Toast.LENGTH_SHORT).show()
                if (view is TextView) {
                    view.highlightColor = Color.TRANSPARENT
                }
            }
        }

        val spanTopicCallBack = object : SpanTopicCallBack {
            override fun onClick(view: View, topicModel: TopicModel) {
                Toast.makeText(view.context, topicModel.topicName + " 被点击了", Toast.LENGTH_SHORT).show()
                if (view is TextView) {
                    view.highlightColor = Color.TRANSPARENT
                }
            }
        }
        val richTextBuilder = RichTextBuilder(this)
        richTextBuilder.setContent(content)
                .setAtColor(Color.RED)
                .setLinkColor(Color.BLUE)
                .setTopicColor(Color.YELLOW)
                .setListUser(nameList)
                .setListTopic(topicModels)
                .setTextView(richText)
                .setNeedUrl(true)
                .setNeedNum(true)
                .setEmojiSize(ScreenUtils.dip2px(this, 5f).toInt())
                //.setVerticalAlignment(CenteredImageSpan.ALIGN_CENTER)
                .setSpanAtUserCallBack(spanAtUserCallBack)
                .setSpanUrlCallBack(spanUrlCallBack)
                .setSpanTopicCallBack(spanTopicCallBack)
                //自定义span，如果不需要可不设置
                .setSpanCreateListener(spanCreateListener)
                .build()


        //直接使用RichTextView
        richText2.atColor = Color.RED
        richText2.topicColor = Color.BLUE
        richText2.linkColor = Color.YELLOW
        richText2.isNeedNumberShow = true
        richText2.isNeedUrlShow = true
        richText2.setSpanAtUserCallBackListener(spanAtUserCallBack)
        richText2.setSpanTopicCallBackListener(spanTopicCallBack)
        richText2.setSpanUrlCallBackListener(spanUrlCallBack)
        //所有配置完成后才设置text
        richText2.setRichText(content, nameList, topicModels)
    }

    private fun initData() {
        nameList.clear()
        topicModels.clear()
        val userModel = UserModel("22222", "2222")
        nameList.add(userModel)
        val userModel2 = UserModel("kkk", "23333")
        nameList.add(userModel2)
        val topicModel = TopicModel("话题话题", "333")
        topicModels.add(topicModel)
    }


    override fun onClick(view: View) {
        when (view.id) {
            R.id.emojiShowBottom -> {
                emojiLayout.hideKeyboard()
                if (emojiLayout.visibility == View.VISIBLE) {
                    emojiLayout.visibility = View.GONE
                } else {
                    emojiLayout.visibility = View.VISIBLE
                }
            }
            R.id.emojiShowAt -> JumpUtil.goToUserList(this@MainActivity, MainActivity.REQUEST_USER_CODE_CLICK)
            R.id.insertTextBtn -> {
                nameListEd.clear()
                topicModelsEd.clear()

                //如果是一次性插入的，记得补上@
                var userModel = UserModel("@22222", "2222")
                nameListEd.add(userModel)
                userModel = UserModel("@kkk", "23333")
                nameListEd.add(userModel)
                //如果是一次性插入的，记得补上#和#
                val topicModel = TopicModel("#话题话题#", "333")
                topicModelsEd.add(topicModel)
                emojiEditText.resolveInsertText(this@MainActivity, insertContent, nameListEd, topicModelsEd)
                //获取原始数据可以通过以下获取
                emojiEditText.realUserList
                emojiEditText.realTopicList
                Log.e(this.javaClass.name, emojiEditText.realText)
            }
            R.id.jumpBtn -> {
            }
            R.id.emojiShowTopic -> JumpUtil.goToTopicList(this@MainActivity, MainActivity.REQUEST_TOPIC_CODE_CLICK)
            R.id.jumpMvvm -> {
                startActivity(Intent(this@MainActivity, MVVMActivity::class.java))
            }
            R.id.emojiEditText -> emojiLayout.visibility = View.GONE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_USER_CODE_CLICK -> emojiEditText.resolveAtResult(data.getSerializableExtra(UserListActivity.DATA) as UserModel)
                REQUEST_USER_CODE_INPUT -> emojiEditText.resolveAtResultByEnterAt(data.getSerializableExtra(UserListActivity.DATA) as UserModel)

                REQUEST_TOPIC_CODE_INPUT -> emojiEditText.resolveTopicResultByEnter(data.getSerializableExtra(TopicListActivity.DATA) as TopicModel)
                REQUEST_TOPIC_CODE_CLICK -> emojiEditText.resolveTopicResult(data.getSerializableExtra(TopicListActivity.DATA) as TopicModel)
            }
        }

    }
}
