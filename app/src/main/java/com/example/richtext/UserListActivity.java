package com.example.richtext;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import com.shuyu.textutillib.model.UserModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserListActivity extends AppCompatActivity {

    public final static String DATA = "data";

    @BindView(R.id.user_list)
    ListView userList;

    private List<UserModel> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        ButterKnife.bind(this);

        for (int i = 0; i < 50; i++) {
            UserModel userModel = new UserModel();
            userModel.setUser_name("测试名字" + i);
            userModel.setUser_id(i * 30 + "");
            data.add(userModel);
        }

        ArrayAdapter adapter = new ArrayAdapter<UserModel>(this, R.layout.user_list_item, data);
        userList.setAdapter(adapter);
        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra(DATA, data.get(position));
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

    }


}
