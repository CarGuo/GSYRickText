package com.example.shuyu

import android.content.Context
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.shuyu.span.CustomClickAtUserSpan
import com.example.shuyu.span.CustomClickTopicSpan
import com.example.shuyu.span.CustomLinkSpan
import com.example.shuyu.utils.ScreenUtils
import com.shuyu.textutillib.RichTextBuilder
import com.shuyu.textutillib.SmileUtils
import com.shuyu.textutillib.listener.SpanAtUserCallBack
import com.shuyu.textutillib.listener.SpanCreateListener
import com.shuyu.textutillib.listener.SpanTopicCallBack
import com.shuyu.textutillib.listener.SpanUrlCallBack
import com.shuyu.textutillib.model.TopicModel
import com.shuyu.textutillib.model.UserModel
import com.shuyu.textutillib.span.ClickAtUserSpan
import com.shuyu.textutillib.span.ClickTopicSpan
import com.shuyu.textutillib.span.LinkSpan
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    private val topicModels = ArrayList<TopicModel>()

    private val nameList = ArrayList<UserModel>()


    private val spanCreateListener = object : SpanCreateListener {
        override fun getCustomClickAtUserSpan(context: Context, userModel: UserModel, color: Int, spanClickCallBack: SpanAtUserCallBack): ClickAtUserSpan {
            return CustomClickAtUserSpan(context, userModel, color, spanClickCallBack)
        }

        override fun getCustomClickTopicSpan(context: Context, topicModel: TopicModel, color: Int, spanTopicCallBack: SpanTopicCallBack): ClickTopicSpan {
            return CustomClickTopicSpan(context, topicModel, color, spanTopicCallBack)
        }

        override fun getCustomLinkSpan(context: Context, url: String, color: Int, spanUrlCallBack: SpanUrlCallBack): LinkSpan {
            return CustomLinkSpan(context, url, color, spanUrlCallBack)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initEmoji()
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
        SmileUtils.addPatternAll(SmileUtils.emoticons, strings, data);
    }

    private fun initView() {
        resolveRichShow();
    }

    private fun resolveRichShow() {

        initData();

        val content = "这是测试#话题话题#文本哟 www.baidu.com " +
                "\n来@某个人  @22222 @kkk " +
                "\n好的,来几个表情[e2][e4][e55]，最后来一个电话 13245685478"

        val spanUrlCallBack = object : SpanUrlCallBack {
            override fun phone(view: View, phone: String) {
                Toast.makeText(view.context, phone + " 被点击了", Toast.LENGTH_SHORT).show();
                if (view is TextView) {
                    view.highlightColor = Color.TRANSPARENT
                }
            }

            override fun url(view: View, url: String) {
                Toast.makeText(view.context, url + " 被点击了", Toast.LENGTH_SHORT).show();
                if (view is TextView) {
                    view.highlightColor = Color.TRANSPARENT
                }
            }
        }

        val spanAtUserCallBack = object : SpanAtUserCallBack {
            override fun onClick(view: View, userModel1: UserModel) {
                Toast.makeText(view.context, userModel1.user_name + " 被点击了", Toast.LENGTH_SHORT).show();
                if (view is TextView) {
                    view.highlightColor = Color.TRANSPARENT
                }
            }
        }

        val spanTopicCallBack = object : SpanTopicCallBack {
            override fun onClick(view: View, topicModel: TopicModel) {
                Toast.makeText(view.context, topicModel.topicName + " 被点击了", Toast.LENGTH_SHORT).show();
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
    }

    private fun initData() {
        nameList.clear()
        topicModels.clear()
        val userModel = UserModel("22222", "2222")
        nameList.add(userModel)
        val userModel2 = UserModel("kkk", "23333")
        nameList.add(userModel2)
        val topicModel = TopicModel("333", "话题话题")
        topicModels.add(topicModel)
    }
}
