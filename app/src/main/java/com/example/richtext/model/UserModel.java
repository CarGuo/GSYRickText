package com.example.richtext.model;

import java.io.Serializable;

/**
 * Created by shuyu on 2016/11/10.
 */

public class UserModel implements Serializable {

    private String user_name;

    private String user_id;

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return this.user_name;
    }
}
