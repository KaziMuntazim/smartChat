package com.example.smartchat;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdepter extends RecyclerView.Adapter<ChatAdepter.myviewholder>{
    Context mainactivity;
    ArrayList<UserInfos> userlist;

    public ChatAdepter(MainActivity mainActivity, ArrayList<UserInfos> userlist) {
        this.mainactivity = mainActivity;
        this.userlist = userlist;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mainactivity).inflate(R.layout.main_page , parent,false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {
        UserInfos userInfos = userlist.get(position);
        holder.user_name.setText(userInfos.username);
        holder.user_status.setText(userInfos.status);
        Picasso.get().load(userInfos.profileP).into(holder.profile_img);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainactivity , ChatScreeen.class);
                intent.putExtra("NAMES" , userInfos.getUsername());
                intent.putExtra("IMAGES", userInfos.getProfileP());
                intent.putExtra("UIDS" , userInfos.getUserid());
                mainactivity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userlist.size();
    }

    class myviewholder extends RecyclerView.ViewHolder {
        CircleImageView profile_img;
        TextView user_name , user_status;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            profile_img = itemView.findViewById(R.id.profile_img);
            user_name = itemView.findViewById(R.id.user_name);
            user_status = itemView.findViewById(R.id.user_status);
        }
    }
}
