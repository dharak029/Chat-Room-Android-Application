/*
Assignment InClass09.
Chatroom.java
Viranchi Deshpande, Dharak Shah
*/
package com.example.viranchi.group11_inclass09;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Chatroom extends AppCompatActivity {

    User user;
    TheThread theThread;
    TextView txtThreadName;
    EditText editMsg;
    ImageButton imgHome,imgSend;
    RecyclerView mRecyclerView;
    String token;
    ArrayList<Message> arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);

        txtThreadName = (TextView)findViewById(R.id.txtThreadName);
        editMsg = (EditText)findViewById(R.id.editAddMessage);
        imgHome = (ImageButton)findViewById(R.id.imgBtnHome);
        imgSend = (ImageButton)findViewById(R.id.imgBtnSendMsg);
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerChatRoom);


        final OkHttpClient client = new OkHttpClient();
        if (getIntent().getExtras().containsKey("User_Info"))
        {
            user = (User)getIntent().getSerializableExtra("User_Info");
            theThread = (TheThread)getIntent().getSerializableExtra("Thread_Info");
            token = user.getToken();
            txtThreadName.setText(theThread.getTitle());

            Request request = new Request.Builder()
                    .url("http://ec2-54-164-74-55.compute-1.amazonaws.com/api/messages/"+theThread.getId())
                    .addHeader("Authorization", "BEARER " + token)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override public void onFailure(Call call, IOException e) {
                    //Toast.makeText(MsgThread.this, "Failure due to unusual error", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

                @Override public void onResponse(Call call, Response response) throws IOException {
                    String outputJson = response.body().string();
                    try {
                        arrayList = UserDetailsUtil.parseMessageDetailsUtil(outputJson);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Chatroom.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mRecyclerView = (RecyclerView)findViewById(R.id.recyclerChatRoom);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Chatroom.this);
                            mRecyclerView.setLayoutManager(mLayoutManager);
                            ChatAdapter chatAdapter = new ChatAdapter(arrayList,user);
                            mRecyclerView.setAdapter(chatAdapter);
                        }
                    });

                }
            });

        }

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Chatroom.this,MsgThread.class);
                intent.putExtra("UserDetails",user);
                startActivity(intent);
            }
        });

        imgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestBody formBody = new FormBody.Builder()
                        .add("message", editMsg.getText().toString())
                        .add("thread_id",theThread.getId())
                        .build();

                Request request = new Request.Builder()
                        .url("http://ec2-54-164-74-55.compute-1.amazonaws.com/api/message/add")
                        .post(formBody)
                        .addHeader("Authorization", "BEARER " + token)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        // Toast.makeText(MsgThread.this, "Some unusual error", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String outputJson = response.body().string();
                        Message message = null;
                        try {
                            message = UserDetailsUtil.parseMessageObjDetailsUtil(outputJson);
                            arrayList.add(message);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Chatroom.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mRecyclerView = (RecyclerView)findViewById(R.id.recyclerChatRoom);
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Chatroom.this);
                                mRecyclerView.setLayoutManager(mLayoutManager);
                                ChatAdapter chatAdapter = new ChatAdapter(arrayList,user);
                                mRecyclerView.setAdapter(chatAdapter);
                            }
                        });

                    }
                });
            }
        });

    }
}
