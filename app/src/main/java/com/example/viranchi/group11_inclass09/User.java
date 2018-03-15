/*
Assignment InClass09.
User.java
Viranchi Deshpande, Dharak Shah
*/

package com.example.viranchi.group11_inclass09;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Viranchi on 06-11-2017.
 */

public class User implements Serializable{
    String token, user_role, user_fname, user_email, user_lname;
    int userId;

    public User(int userId, String token, String user_role, String user_fname, String user_email, String user_lname) {
        this.userId = userId;
        this.token = token;
        this.user_role = user_role;
        this.user_fname = user_fname;
        this.user_email = user_email;
        this.user_lname = user_lname;
    }

    public User() {
    }

    public static User getUserDetails(JSONObject js) throws JSONException {

        User userInfo = new User();

        userInfo.setToken(js.getString("token"));
        userInfo.setUser_email(js.getString("user_email"));
        userInfo.setUser_fname(js.getString("user_fname"));
        userInfo.setUser_lname(js.getString("user_lname"));
        userInfo.setUser_role(js.getString("user_role"));
        userInfo.setUserId(js.getInt("user_id"));

        return userInfo;
    }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUser_role() {
        return user_role;
    }

    public void setUser_role(String user_role) {
        this.user_role = user_role;
    }

    public String getUser_fname() {
        return user_fname;
    }

    public void setUser_fname(String user_fname) {
        this.user_fname = user_fname;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_lname() {
        return user_lname;
    }

    public void setUser_lname(String user_lname) {
        this.user_lname = user_lname;
    }
}
