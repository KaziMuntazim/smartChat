package com.example.smartchat;

import static com.example.smartchat.ChatScreeen.recvieIMG;
import static com.example.smartchat.ChatScreeen.senderIMG;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MsgAdepter extends RecyclerView.Adapter{
    Context context;
    ArrayList<MsgClass> msg_show_list;
    int ITEM_SEND = 1;
    int ITEM_RECIVE = 2;

    public MsgAdepter(Context context,  ArrayList<MsgClass> msg_show_list) {
        this.context = context;
        this.msg_show_list = msg_show_list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_SEND){
            View view = LayoutInflater.from(context).inflate(R.layout.sender_layout , parent , false);
            return new MysendHolder(view);
        }else {
            View view = LayoutInflater.from(context).inflate(R.layout.reciver_layout , parent, false);
            return new MyrecivHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MsgClass msgClass = msg_show_list.get(position);
        if (holder.getClass() == MysendHolder.class){
            MysendHolder viewhol = (MysendHolder) holder;
            viewhol.msgsendertyp.setText(msgClass.getMassage());
            Picasso.get().load(senderIMG).into(viewhol.profilerggg);
        }else {
            MyrecivHolder viewrc = (MyrecivHolder) holder;
            viewrc.recivertextset.setText(msgClass.getMassage());
            Picasso.get().load(recvieIMG).into(viewrc.pro);
        }
    }

    @Override
    public int getItemCount() {
        return  msg_show_list.size();
    }

    @Override
    public int getItemViewType(int position) {
        MsgClass msgClass = msg_show_list.get(position);
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(msgClass.getSenderUid())){
            return ITEM_SEND;
        }else {
            return ITEM_RECIVE;
        }
    }

    public class MysendHolder extends RecyclerView.ViewHolder {
        CircleImageView profilerggg;
        TextView msgsendertyp;

        public MysendHolder(@NonNull View itemView) {
            super(itemView);
            profilerggg = itemView.findViewById(R.id.profilerggg);
            msgsendertyp = itemView.findViewById(R.id.msgsendertyp);
        }
    }

    public class MyrecivHolder extends RecyclerView.ViewHolder {
        CircleImageView pro;
        TextView recivertextset;
        public MyrecivHolder(@NonNull View itemView) {
            super(itemView);
            pro = itemView.findViewById(R.id.pro);
            recivertextset = itemView.findViewById(R.id.recivertextset);
        }
    }
}
