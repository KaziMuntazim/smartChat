package com.example.smartchat;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatScreeen extends AppCompatActivity {
    String imagerecive , namerecive , idrecive , senderid;
    CircleImageView profile_chat;
    TextView user_chatN;
    EditText chat_edit;
    ImageView send_img;
    FirebaseAuth auth;
    FirebaseDatabase database;
    public static String senderIMG;
    public static String recvieIMG;
    String senderRoom , reciverRoom;
    RecyclerView rc_chat;
    ArrayList<MsgClass> maslist;
    MsgAdepter msgAdepter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat_screeen);

        // Handle window insets for proper UI padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views and Firebase authentication
        profile_chat = findViewById(R.id.profile_chat);
        user_chatN = findViewById(R.id.user_chatN);
        chat_edit = findViewById(R.id.chat_edit);
        send_img = findViewById(R.id.send_img);
        rc_chat = findViewById(R.id.rc_chat);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        // Get data from intent (receiver information)
        namerecive = getIntent().getStringExtra("NAMES");
        imagerecive = getIntent().getStringExtra("IMAGES");
        idrecive = getIntent().getStringExtra("UIDS");

        // Initialize message list and adapter
        maslist = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);  // To scroll from the last message
        rc_chat.setLayoutManager(linearLayoutManager);

        // Set adapter after initializing the message list
        msgAdepter = new MsgAdepter(ChatScreeen.this, maslist);
        rc_chat.setAdapter(msgAdepter);

        // Set receiver's name and profile picture
        Picasso.get().load(imagerecive).into(profile_chat);
        user_chatN.setText(namerecive);

        // Initialize sender and receiver room IDs based on UIDs
        senderid = auth.getUid();  // Current user ID (sender)
        if (senderid != null && idrecive != null) {
            senderRoom = senderid + idrecive;  // Room between sender and receiver
            reciverRoom = idrecive + senderid;  // Room between receiver and sender
        } else {
            Toast.makeText(this, "Error: User ID is null", Toast.LENGTH_SHORT).show();
            return;  // Stop if senderid or idrecive is null to avoid crashes
        }

        // Load sender's profile image and receiver's profile image
        DatabaseReference reference = database.getReference().child("Userinfo").child(senderid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.hasChild("profileP")) {
                    senderIMG = snapshot.child("profileP").getValue(String.class);
                    recvieIMG = imagerecive;  // Receiver's image is passed through intent
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ChatScreeen.this, "Failed to load profile images", Toast.LENGTH_SHORT).show();
            }
        });

        // Load and listen for chat messages between sender and receiver
        DatabaseReference chatref = database.getReference().child("Chat").child(senderRoom).child("massagess");
        chatref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                maslist.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    MsgClass msgClass = dataSnapshot.getValue(MsgClass.class);
                    if (msgClass != null) {
                        maslist.add(msgClass);
                    }
                }
                msgAdepter.notifyDataSetChanged();  // Notify adapter of data changes
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ChatScreeen.this, "Failed to load messages", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle sending messages when the send button is clicked
        send_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String massages = chat_edit.getText().toString();
                if (massages.isEmpty()) {
                    Toast.makeText(ChatScreeen.this, "Enter The Message", Toast.LENGTH_SHORT).show();
                    return;  // Return early if the message is empty
                }

                chat_edit.setText("");  // Clear the input field
                Date date = new Date();
                MsgClass msgClass = new MsgClass(massages, senderid, date.getTime());

                // Save the message to both sender's and receiver's chat rooms
                database.getReference().child("Chat").child(senderRoom).child("massagess")
                        .push().setValue(msgClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                database.getReference().child("Chat").child(reciverRoom).child("massagess")
                                        .push().setValue(msgClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                // Message sent to both sender's and receiver's rooms
                                            }
                                        });
                            }
                        });
            }
        });
    }

}