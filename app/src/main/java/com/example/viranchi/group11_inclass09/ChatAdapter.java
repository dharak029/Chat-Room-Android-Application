/*
Assignment InClass09.
ChatAdapter.java
Viranchi Deshpande, Dharak Shah
*/
package com.example.viranchi.group11_inclass09;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import org.ocpsoft.prettytime.PrettyTime;

import java.io.IOException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Viranchi on 06-11-2017.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    static ArrayList<Message> mData;
    static User user;
    final OkHttpClient client = new OkHttpClient();

    public ChatAdapter(ArrayList<Message> mData,User user) {
        this.mData = mData;
        this.user = user;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtMessage,txtSender,txtTime;
        ImageButton imgBtnDelete;
        Message message;
        public ViewHolder(final View itemView) {
            super(itemView);
            txtMessage = (TextView) itemView.findViewById(R.id.txtMsg);
            txtSender = (TextView)itemView.findViewById(R.id.txtSenderName);
            txtTime = (TextView)itemView.findViewById(R.id.txtTimeStamp);
            imgBtnDelete = (ImageButton)itemView.findViewById(R.id.imgBtnRemoveMsg);
        }
    }

    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout, parent, false);
        ChatAdapter.ViewHolder viewHolder = new ChatAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ChatAdapter.ViewHolder holder, final int position) {

        final Message message = mData.get(position);
        holder.txtMessage.setText(message.getMessage());
        holder.txtSender.setText(message.getUser_fname()+" "+message.getUser_lname());

        PrettyTime prettyTime = new PrettyTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
        long timeMillis = 0;
        try {
            Date date1  = simpleDateFormat.parse(message.getCreated_at());
            timeMillis = date1.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.txtTime.setText(prettyTime.format(new Date(timeMillis - 1000*60*60*5)));
        if(user.getUserId() != Integer.parseInt(message.getUser_id())){
           holder.imgBtnDelete.setVisibility(View.GONE);
        }
        else {
            holder.imgBtnDelete.setVisibility(View.VISIBLE);
        }

        holder.imgBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mData.remove(position);
                notifyDataSetChanged();

                final OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url("http://ec2-54-164-74-55.compute-1.amazonaws.com/api/message/delete/" + message.getId())
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
