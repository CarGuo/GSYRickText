

![](https://github.com/CarGuo/RickText/blob/master/Logo.png)


## 支持类似微博的文本效果，表情、@某人、话题、url链接等。DEMO同时演示了MVVM模式的使用。

状态 | 功能
-------- | ---
**支持**|**表情**
**支持**|**#话题**
**支持**|**@某人**
**支持**|**url与数字（可配置）**
**支持**|**点击效果**
**支持**|**自定义span效果**
**支持**|**表情、#话题与@某人编辑时整块删除**
**支持**|**表情、#话题与@某人编辑时选择复制限制整块选择。**
**支持**|**表情大小设置、居中显示**
**支持**|**MVVM（DataBing）。**
**支持**|**kotlin(demo与lib)**


----------------------------------

[![](https://jitpack.io/v/CarGuo/RickText.svg)](https://jitpack.io/#CarGuo/RickText)
[![Build Status](https://travis-ci.org/CarGuo/RickText.svg?branch=master)](https://travis-ci.org/CarGuo/RickText)

## 依赖方式

### 在project下的build.gradle添加
```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

### 在module下的build.gradle添加

```
dependencies {
     compile 'com.github.CarGuo:RickText:v2.1.2'
}

```

----------------------------------

## DEMO效果图

<img src="https://github.com/CarGuo/RickText/blob/master/1.png" width="240px" height="426px"/>


### [旧版简书解析](http://www.jianshu.com/p/cd9e197a5c04)

### [旧版README](https://github.com/CarGuo/RickText/blob/master/OLD_README.md)

### QQ群，有兴趣的可以进来，群里视频项目的人居多，平时多吹水吐槽：174815284 。


----------------------------------

## 使用方式参考demo

### 1、文本模式

#### 1.1、RichTextView
```
richTextView = (RichTextView) findViewById(R.id.rich_text_2);
//直接使用RichTextView
richTextView.setAtColor(Color.RED);
richTextView.setTopicColor(Color.BLUE);
richTextView.setLinkColor(Color.YELLOW);
richTextView.setNeedNumberShow(true);
richTextView.setNeedUrlShow(true);
richTextView.setSpanAtUserCallBackListener(spanAtUserCallBack);
richTextView.setSpanTopicCallBackListener(spanTopicCallBack);
richTextView.setSpanUrlCallBackListener(spanUrlCallBack);
//所有配置完成后才设置text
richTextView.setRichText(content, nameList, topicModels);
```

#### 1.2、普通TextView

```
//url点击回调
SpanUrlCallBack spanUrlCallBack = new SpanUrlCallBack() {
    @Override
    public void phone(String phone) {
        Toast.makeText(MainActivity.this, phone + " 被点击了", Toast.LENGTH_SHORT).show();
        richText.setHighlightColor(Color.TRANSPARENT);
    }

    @Override
    public void url(String url) {
        Toast.makeText(MainActivity.this, url + " 被点击了", Toast.LENGTH_SHORT).show();
        richText.setHighlightColor(Color.TRANSPARENT);
    }
};

//@点击回调
SpanAtUserCallBack spanAtUserCallBack = new SpanAtUserCallBack() {
    @Override
    public void onClick(UserModel userModel1) {
        Toast.makeText(MainActivity.this, userModel1.getUser_name() + " 被点击了", Toast.LENGTH_SHORT).show();
        richText.setHighlightColor(Color.TRANSPARENT);
    }
};

//话题点击回调
SpanTopicCallBack spanTopicCallBack = new SpanTopicCallBack() {
    @Override
    public void onClick(TopicModel topicModel) {
        Toast.makeText(MainActivity.this, topicModel.getTopicName() + " 被点击了", Toast.LENGTH_SHORT).show();
        richText.setHighlightColor(Color.TRANSPARENT);
    }
};

//配置TextView显示文本
RichTextBuilder richTextBuilder = new RichTextBuilder(this);
richTextBuilder.setContent(content)
        .setAtColor(Color.RED)
        .setLinkColor(Color.BLUE)
        .setTopicColor(Color.YELLOW)
        .setListUser(nameList)
        .setListTopic(topicModels)
        .setTextView(richText)
        .setSpanAtUserCallBack(spanAtUserCallBack)
        .setSpanUrlCallBack(spanUrlCallBack)
        .setSpanTopicCallBack(spanTopicCallBack)
        .build();

```

### 2、编辑模式（RichEditText）

```
richEditText = (RichEditText) findViewById(R.id.emoji_edit_text);
emojiLayout.setEditTextSmile(richEditText);
RichEditBuilder richEditBuilder = new RichEditBuilder();
richEditBuilder.setEditText(richEditText)
        .setTopicModels(topicModels)
        .setUserModels(nameList)
        .setColorAtUser("#FF00C0")
        .setColorTopic("#F0F0C0")
        .setEditTextAtUtilJumpListener(new OnEditTextUtilJumpListener() {
            @Override
            public void notifyAt() {
                JumpUtil.goToUserList(MainActivity.this, MainActivity.REQUEST_USER_CODE_INPUT);
            }

            @Override
            public void notifyTopic() {
                JumpUtil.goToTopicList(MainActivity.this, MainActivity.REQUEST_TOPIC_CODE_INPUT);
            }
        })
        .builder();
```

----------------------------------

## 版本更新

#### v2.1.2 （2017-10-20）
* 优化一些问题
* 增加kotlin lib 与 kotlin demo

#### v2.1.1 （2017-09-19）
* 增加表情居中功能的支持。

#### v2.1.0 （2017-09-17）
* 修复了@和#输入存在的问题。


### [历史版本](https://github.com/CarGuo/RickText/blob/master/HISTORY_README.md)


## License

```
MIT
```
