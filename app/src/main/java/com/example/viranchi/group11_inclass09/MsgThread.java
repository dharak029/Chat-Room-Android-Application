/*
Assignment InClass09.
MsgThread.java
Viranchi Deshpande, Dharak Shah
*/

package com.example.viranchi.group11_inclass09;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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



public class MsgThread extends AppCompatActivity {

    TextView txtUserName;
    RecyclerView mRecyclerView;
    EditText edtThreadName;
    String token;
    ArrayList<TheThread> arrayList;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_thread);

        edtThreadName = (EditText)findViewById(R.id.editAddThread);
        txtUserName = (TextView)findViewById(R.id.txtUserName);

        final OkHttpClient client = new OkHttpClient();

        if (getIntent().getExtras().containsKey("UserDetails"))
        {
            user = (User)getIntent().getSerializableExtra("UserDetails");
            token = user.getToken();
            txtUserName.setText(user.getUser_fname()+" "+user.getUser_lname());

            Request request = new Request.Builder()
                    .url("http://ec2-54-164-74-55.compute-1.amazonaws.com/api/thread")
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
                        arrayList = UserDetailsUtil.parseThreadDetailsUtil(outputJson);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    MsgThread.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mRecyclerView = (RecyclerView)findViewById(R.id.recyclerThread);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MsgThread.this);
                            mRecyclerView.setLayoutManager(mLayoutManager);
                            ThreadAdapter courseItemAdapter = new ThreadAdapter(arrayList,user,MsgThread.this);
                            mRecyclerView.setAdapter(courseItemAdapter);
                        }
                    });

                }
            });

        }

        findViewById(R.id.btnAddThread).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestBody formBody = new FormBody.Builder()
                        .add("title", edtThreadName.getText().toString())
                        .build();

                Request request = new Request.Builder()
                        .url("http://ec2-54-164-74-55.compute-1.amazonaws.com/api/thread/add")
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
                        TheThread theThread = null;
                        try {
                            theThread = UserDetailsUtil.parseThreadObjDetailsUtil(outputJson);
                            arrayList.add(theThread);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        MsgThread.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mRecyclerView = (RecyclerView)findViewById(R.id.recyclerThread);
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MsgThread.this);
                                mRecyclerView.setLayoutManager(mLayoutManager);
                                ThreadAdapter courseItemAdapter = new ThreadAdapter(arrayList,user, MsgThread.this);
                                mRecyclerView.setAdapter(courseItemAdapter);
                            }
                        });

                    }
                });
            }
        });



        findViewById(R.id.imgBtnLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("token");
                editor.commit();

                Intent intent = new Intent(MsgThread.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
