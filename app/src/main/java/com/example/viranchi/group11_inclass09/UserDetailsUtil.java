/*
Assignment InClass09.
UserDetailsUtil.java
Viranchi Deshpande, Dharak Shah
*/

package com.example.viranchi.group11_inclass09;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Viranchi on 06-11-2017.
 */

public class UserDetailsUtil {

    public static User parseUserDetailsUtil(String input) throws JSONException {
        JSONObject jsonObject = new JSONObject(input);
        return User.getUserDetails(jsonObject);
    }

    public static ArrayList<TheThread> parseThreadDetailsUtil(String input) throws JSONException {
        JSONObject jsonObject = new JSONObject(input);
        JSONArray jsonArray = jsonObject.getJSONArray("threads");
        ArrayList<TheThread> arrayList = new ArrayList<TheThread>();
        for (int i = 0; i < jsonArray.length(); i++)
        {
            arrayList.add(TheThread.getThreadDetails(jsonArray.getJSONObject(i)));
        }
        return arrayList;
    }

    public static TheThread parseThreadObjDetailsUtil(String input) throws JSONException {
        JSONObject jsonObject = new JSONObject(input);
        JSONObject threadObject = jsonObject.getJSONObject("thread");
        return TheThread.getThreadDetails(threadObject);
    }

    public static ArrayList<Message> parseMessageDetailsUtil(String input) throws JSONException {
        JSONObject jsonObject = new JSONObject(input);
        JSONArray jsonArray = jsonObject.getJSONArray("messages");
        ArrayList<Message> arrayList = new ArrayList<Message>();
        for (int i = 0; i < jsonArray.length(); i++)
        {
            arrayList.add(Message.getMessageDetails(jsonArray.getJSONObject(i)));
        }
        return arrayList;
    }

    public static Message parseMessageObjDetailsUtil(String input) throws JSONException {
        JSONObject jsonObject = new JSONObject(input);
        JSONObject threadObject = jsonObject.getJSONObject("message");
        return Message.getMessageDetails(threadObject);
    }
}
