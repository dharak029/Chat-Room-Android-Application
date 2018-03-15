/*
Assignment InClass09.
SignUP.java
Viranchi Deshpande, Dharak Shah
*/

package com.example.viranchi.group11_inclass09;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignUP extends AppCompatActivity {

    EditText edtEmail;
    EditText edtPass;
    EditText edtFirst;
    EditText edtLast;
    EditText edtConfPass;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edtEmail = (EditText) findViewById(R.id.editSignupEmail);
        edtPass = (EditText) findViewById(R.id.editSignupPassword);
        edtFirst = (EditText) findViewById(R.id.editSignupFname);
        edtLast = (EditText) findViewById(R.id.editSignupLname);
        edtConfPass = (EditText) findViewById(R.id.editRepeatPassword);

        final OkHttpClient client = new OkHttpClient();

        sharedPreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);

        findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUP.this, MainActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btnSignUpRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtConfPass.getText().toString().equals(edtPass.getText().toString()))
                {
                    RequestBody formBody = new FormBody.Builder()
                            .add("email", edtEmail.getText().toString())
                            .add("password", edtPass.getText().toString())
                            .add("fname", edtFirst.getText().toString())
                            .add("lname", edtLast.getText().toString())
                            .build();

                    Request request = new Request.Builder()
                            .url("http://ec2-54-164-74-55.compute-1.amazonaws.com/api/signup")
                            .post(formBody)
                            .build();

                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            //Toast.makeText(SignUP.this, "Some unusual error", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String outputJson = response.body().string();
                            User currentUser = null;
                            try {
                                currentUser = UserDetailsUtil.parseUserDetailsUtil(outputJson);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("token", currentUser.getToken());
                            editor.commit();
                            SignUP.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(SignUP.this, "User created Successfully", Toast.LENGTH_SHORT).show();
                                }
                            });

                            Intent intent = new Intent(SignUP.this, MsgThread.class);
                            intent.putExtra("UserDetails", currentUser);
                            startActivity(intent);
                        }
                    });

                }
                else
                {
                    SignUP.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(SignUP.this, "Password does not match", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }
}
