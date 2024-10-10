package com.example.smartchat;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth auth;
    RecyclerView rc_view;
    ImageView logout_btn , camara_main , chat_main , seting_main;
    ChatAdepter adepter;
    FirebaseDatabase database;
    ArrayList<UserInfos> userlist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        rc_view = findViewById(R.id.rc_view);
        logout_btn = findViewById(R.id.logout_btn);
        camara_main = findViewById(R.id.cmara_main);
        chat_main = findViewById(R.id.chat_main);
        seting_main = findViewById(R.id.seting_main);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference().child("Userinfo");
        userlist = new ArrayList<>();
        rc_view.setLayoutManager(new LinearLayoutManager(this));
        adepter = new ChatAdepter(MainActivity.this , userlist);
        rc_view.setAdapter(adepter);


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userlist.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    UserInfos userInfos = dataSnapshot.getValue(UserInfos.class);
                    userlist.add(userInfos);
                }
                adepter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.logout_diaolg);
                Button yes_btn , no_btn;
                yes_btn = dialog.findViewById(R.id.yes_btn);
                no_btn = dialog.findViewById(R.id.no_btn);
                dialog.setCancelable(false);

                yes_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(MainActivity.this , LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                no_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        if (auth.getCurrentUser() == null){
            Intent intent = new Intent(MainActivity.this , LoginActivity.class);
            startActivity(intent);
        }else {

        }


        seting_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(MainActivity.this , SettingActivity.class);
                startActivity(intent);
            }
        });
        camara_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent , 102);
            }
        });


    }
}