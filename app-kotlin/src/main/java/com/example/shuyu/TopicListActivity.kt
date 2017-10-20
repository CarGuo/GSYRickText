package com.example.shuyu

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView

import com.shuyu.textutillib.model.TopicModel
import kotlinx.android.synthetic.main.activity_user_list.*

import java.util.ArrayList


class TopicListActivity : AppCompatActivity() {

    private val data = ArrayList<TopicModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)

        (0..49).mapTo(data) { TopicModel("测试话题" + it, (it * 30).toString() + "") }

        val adapter = ArrayAdapter(this, R.layout.user_list_item, data)
        userList.adapter = adapter
        userList.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val intent = Intent()
            intent.putExtra(DATA, data[position])
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

    }
    override fun onBackPressed() {
        setResult(RESULT_CANCELED, intent)
        finish()
    }

    companion object {

        val DATA = "data"
    }


}
