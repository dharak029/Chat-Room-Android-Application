/*
Assignment InClass09.
ThreadAdapter.java
Viranchi Deshpande, Dharak Shah
*/

package com.example.viranchi.group11_inclass09;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * Created by Viranchi on 06-11-2017.
 */

public class ThreadAdapter extends RecyclerView.Adapter<ThreadAdapter.ViewHolder> {

    static ArrayList<TheThread> mData;
    static User user;
    final OkHttpClient client = new OkHttpClient();
    static TheThread theThread;
    static MsgThread activity;

    public ThreadAdapter(ArrayList<TheThread> mData,User user, MsgThread activity) {
        this.mData = mData;
        this.user = user;
        this.activity = activity;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtThread;
        FloatingActionButton btnRemove;
        TheThread theThread;
        public ViewHolder(final View itemView) {
            super(itemView);
            this.theThread = theThread;
            txtThread = (TextView) itemView.findViewById(R.id.txtThreadTitle);
            btnRemove = (FloatingActionButton)itemView.findViewById(R.id.btnDeleteThread);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity,Chatroom.class);
                    intent.putExtra("User_Info", user);
                    intent.putExtra("Thread_Info", theThread);
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.threaditem, parent, false);
        ThreadAdapter.ViewHolder viewHolder = new ThreadAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final TheThread theThread = mData.get(position);
        holder.txtThread.setText(theThread.getTitle());
        holder.theThread = theThread;
        if(user.getUserId() != Integer.parseInt(theThread.getUser_id())){
            holder.btnRemove.setVisibility(View.GONE);
        }
        else {
            holder.btnRemove.setVisibility(View.VISIBLE);
        }

        holder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mData.remove(position);
                notifyDataSetChanged();

                final OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url("http://ec2-54-164-74-55.compute-1.amazonaws.com/api/thread/delete/" + theThread.getId())
                        .addHeader("Authorization", "BEARER " + user.getToken())
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        // Toast.makeText(MsgThread.this, "Some unusual error", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String outputJson = response.body().string();
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
