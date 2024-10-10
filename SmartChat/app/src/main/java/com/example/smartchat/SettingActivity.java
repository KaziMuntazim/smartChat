package com.example.smartchat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageRegistrar;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.net.URL;

public class SettingActivity extends AppCompatActivity {

    ImageView seting_img;
    EditText update_name_edit , update_stus_edit;
    Button profile_save;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    Uri setImageUri;
    String email ,password , name , profil , status;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101){
            if (data != null){
                setImageUri = data.getData();
                seting_img.setImageURI(setImageUri);
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_setting);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        seting_img = findViewById(R.id.seting_img);
        update_name_edit = findViewById(R.id.update_name_edit);
        update_stus_edit = findViewById(R.id.update_stus_edit);
        profile_save = findViewById(R.id.save_profile);

        DatabaseReference reference = database.getReference().child("Userinfo").child(auth.getUid());
        StorageReference storageReference = storage.getReference().child("Uplod").child(auth.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                 email = snapshot.child("mail").getValue().toString();
                 password = snapshot.child("password").getValue().toString();
                 name = snapshot.child("username").getValue().toString();
                 profil = snapshot.child("profileP").getValue().toString();
                 status = snapshot.child("status").getValue().toString();
                 update_name_edit.setText(name);
                 update_stus_edit.setText(status);
                 Picasso.get().load(profil).into(seting_img);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        seting_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent , 101);
            }
        });





        profile_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String storename = update_name_edit.getText().toString();
                String storestuts = update_stus_edit.getText().toString();
                if (setImageUri != null){
                    storageReference.putFile(setImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String finalimg = uri.toString();
                                    UserInfos userInfos = new UserInfos(email , password, finalimg ,storestuts , auth.getUid() , storename);
                                    reference.setValue(userInfos).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(SettingActivity.this, "Profile is Save", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(SettingActivity.this , MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }else {
                                                Toast.makeText(SettingActivity.this, "Issue Found", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    });
                }else {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imgfinal = uri.toString();
                            UserInfos userInfos = new UserInfos(email , password , imgfinal , storestuts , auth.getUid() , storename);
                            reference.setValue(userInfos).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(SettingActivity.this, "Profile is Save", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(SettingActivity.this , MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }else {
                                        Toast.makeText(SettingActivity.this, "Issue Found", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
                }

            }
        });
    }
}