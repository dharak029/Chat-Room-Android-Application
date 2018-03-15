/*
Assignment InClass09.
MainActivity.java
Viranchi Deshpande, Dharak Shah
*/

package com.example.viranchi.group11_inclass09;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class MainActivity extends AppCompatActivity {

    EditText edtEmail;
    EditText edtPass;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtEmail = (EditText) findViewById(R.id.editTextEmail);
        edtPass = (EditText) findViewById(R.id.editTextPass);

        final OkHttpClient client = new OkHttpClient();

        findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    RequestBody formBody = new FormBody.Builder()
                            .add("email", edtEmail.getText().toString())
                            .add("password", edtPass.getText().toString())
                            .build();

                    Request request = new Request.Builder()
                            .url("http://ec2-54-164-74-55.compute-1.amazonaws.com/api/login")
                            .post(formBody)
                            .build();


                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String outputJson = response.body().string();
                            if(response.message().equals("Unauthorized")){
                                MainActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(MainActivity.this,"Login Unsuccessful",Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                            Log.d("demo",response.toString());

                            User currentUser = null;
                            try {
                                currentUser = UserDetailsUtil.parseUserDetailsUtil(outputJson);
                                Intent intent = new Intent(MainActivity.this, MsgThread.class);
                                intent.putExtra("UserDetails", currentUser);
                                startActivity(intent);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                }
        });

        findViewById(R.id.btnSignUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignUP.class);
                startActivity(intent);
            }
        });

    }
}
