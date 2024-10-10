package com.example.smartchat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText email_edit , pass_edit;
    TextView register_btn;
    ImageView login_btn;
    FirebaseAuth auth;
    String Emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait....");
        progressDialog.setCancelable(false);

        auth = FirebaseAuth.getInstance();
        email_edit = findViewById(R.id.email_edit);
        pass_edit = findViewById(R.id.pass_edit);
        register_btn = findViewById(R.id.register_btn);
        login_btn = findViewById(R.id.login_btn);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = email_edit.getText().toString();
                String Pass = pass_edit.getText().toString();

                if (Email.isEmpty() && Pass.isEmpty()){
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Please Enter The Email And Password", Toast.LENGTH_SHORT).show();
                } else if (Email.isEmpty()) {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Please Enter the Email", Toast.LENGTH_SHORT).show();
                } else if (Pass.isEmpty()) {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Plese Enter The Password", Toast.LENGTH_SHORT).show();
                } else if (!Email.matches(Emailpattern)) {
                    progressDialog.dismiss();
                    email_edit.setError("Write proper Email");
                } else if (Pass.length() < 6) {
                    progressDialog.dismiss();
                    pass_edit.setError("Please Enter minimum 6 to 8 latter password");
                }else {
                    auth.signInWithEmailAndPassword(Email , Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                progressDialog.show();
                                try {
                                    Intent intent = new Intent(LoginActivity.this , MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }catch (Exception e){
                                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });




        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this , RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}