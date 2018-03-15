/*
Assignment InClass09.
TheThread.java
Viranchi Deshpande, Dharak Shah
*/

package com.example.viranchi.group11_inclass09;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Viranchi on 06-11-2017.
 */

public class TheThread implements Serializable {

    String user_fname, user_lname, user_id, id, title, created_at;

    public TheThread(String user_fname, String user_lname, String user_id, String id, String title, String created_at) {
        this.user_fname = user_fname;
        this.user_lname = user_lname;
        this.user_id = user_id;
        this.id = id;
        this.title = title;
        this.created_at = created_at;
    }

    public static TheThread getThreadDetails(JSONObject js) throws JSONException {

        TheThread threadInfo = new TheThread();

        threadInfo.setCreated_at(js.getString("created_at"));
        threadInfo.setId(js.getString("id"));
        threadInfo.setUser_fname(js.getString("user_fname"));
        threadInfo.setUser_lname(js.getString("user_lname"));
        threadInfo.setUser_id(js.getString("user_id"));
        threadInfo.setTitle(js.getString("title"));

        return threadInfo;
    }

    public String getUser_fname() {
        return user_fname;
    }

    public void setUser_fname(String user_fname) {
        this.user_fname = user_fname;
    }

    public String getUser_lname() {
        return user_lname;
    }

    public void setUser_lname(String user_lname) {
        this.user_lname = user_lname;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public TheThread() {
    }
}
