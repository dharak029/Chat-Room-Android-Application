/*
Assignment InClass09.
Message.java
Viranchi Deshpande, Dharak Shah
*/

package com.example.viranchi.group11_inclass09;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Viranchi on 06-11-2017.
 */

public class Message implements Serializable{

    String user_fname, user_lname, user_id, id, message, created_at;

    public Message(String user_fname, String user_lname, String user_id, String id, String message, String created_at) {
        this.user_fname = user_fname;
        this.user_lname = user_lname;
        this.user_id = user_id;
        this.id = id;
        this.message = message;
        this.created_at = created_at;
    }

    public Message(){}

    public static Message getMessageDetails(JSONObject js) throws JSONException {

        Message message = new Message();

        message.setCreated_at(js.getString("created_at"));
        message.setId(js.getString("id"));
        message.setUser_fname(js.getString("user_fname"));
        message.setUser_lname(js.getString("user_lname"));
        message.setUser_id(js.getString("user_id"));
        message.setMessage(js.getString("message"));

        return message;
    }


    public String getUser_fname() {
        return user_fname;
    }

    public String getUser_lname() {
        return user_lname;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setUser_fname(String user_fname) {
        this.user_fname = user_fname;
    }

    public void setUser_lname(String user_lname) {
        this.user_lname = user_lname;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
