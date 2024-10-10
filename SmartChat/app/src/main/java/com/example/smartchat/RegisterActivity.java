package com.example.smartchat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {
    EditText uname_edit , uemail_edit , upass_edit , cpass_edit;
    TextView logins_btn;
    ImageView register_btn;
    CircleImageView profile_image;
    FirebaseAuth auth;
    Uri imaguri;
    String imageUI;
    FirebaseDatabase database;
    FirebaseStorage storage;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        database= FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        uname_edit = findViewById(R.id.uname_edit);
        uemail_edit = findViewById(R.id.uemail_edit);
        upass_edit = findViewById(R.id.upass_edit);
        cpass_edit = findViewById(R.id.cpass_edit);
        logins_btn = findViewById(R.id.logins_btn);
        register_btn = findViewById(R.id.register_btn);
        profile_image = findViewById(R.id.profile_image);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait....");
        progressDialog.setCancelable(false);

        logins_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this , LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String names = uname_edit.getText().toString();
                String Emails = uemail_edit.getText().toString();
                String Passs = upass_edit.getText().toString();
                String CPass = cpass_edit.getText().toString();
                String stuts = "Hey I am USing App";

                if (names.isEmpty() || Emails.isEmpty() || Passs.isEmpty() || CPass.isEmpty()){
                    progressDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, "Please Enter Fild", Toast.LENGTH_SHORT).show();
                } else if (!Emails.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
                    progressDialog.dismiss();
                    uemail_edit.setError("Enter Valid Email");
                } else if(Passs.length() < 6 || CPass.length() < 6){
                    progressDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, "Password Not Match", Toast.LENGTH_SHORT).show();
                } else if (!Passs.equals(CPass)) {
                    progressDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, "Write Same Password", Toast.LENGTH_SHORT).show();
                } else {
                    auth.createUserWithEmailAndPassword(Emails, Passs).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                           if (task.isSuccessful()){
                               String id = task.getResult().getUser().getUid();
                               DatabaseReference reference = database.getReference().child("Userinfo").child(id);
                               StorageReference storageReference = storage.getReference().child("Uplod").child(id);

                               if (imaguri != null){
                                   progressDialog.show();
                                   storageReference.putFile(imaguri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                       @Override
                                       public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                           if (task.isSuccessful()){
                                               storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                   @Override
                                                   public void onSuccess(Uri uri) {
                                                       imageUI = uri.toString();
                                                       UserInfos userInfos = new UserInfos(Emails, Passs ,imageUI , stuts , id , names);
                                                       reference.setValue(userInfos).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                           @Override
                                                           public void onComplete(@NonNull Task<Void> task) {
                                                               if (task.isSuccessful()) {
                                                                   Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                                                   startActivity(intent);
                                                                   finish();
                                                               }
                                                           }
                                                       });
                                                   }
                                               });
                                           }
                                       }
                                   });
                               }else {
                                   progressDialog.show();
                                   String stuts = "Hey I am USing App";
                                   imageUI = "https://firebasestorage.googleapis.com/v0/b/smartchat-1fb9f.appspot.com/o/profile.png?alt=media&token=e33b8e1e-7cd0-4b97-9852-55bea48551c2";
                                   UserInfos userInfos = new UserInfos(Emails , Passs , imageUI , stuts , id , names);
                                   reference.setValue(userInfos).addOnCompleteListener(new OnCompleteListener<Void>() {
                                       @Override
                                       public void onComplete(@NonNull Task<Void> task) {
                                           if (task.isSuccessful()) {
                                               Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                               startActivity(intent);
                                               finish();
                                           }
                                       }
                                   });
                               }
                           }else {
                               Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                           }
                        }
                    });
                }
            }
        });

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent , "Select Image") , 20);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 20){
            if (data != null){
                imaguri = data.getData();
                profile_image.setImageURI(imaguri);
            }
        }
    }
}