package com.example.easy_studium;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Person;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easy_studium.databinding.ActivityChatBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    TextView username;
    FirebaseUser fuser;
    DatabaseReference reference;
    MessageAdapter messageAdapter;
    List<Chat> messageList;
    RecyclerView recyclerView;
    Intent intent;
    ImageButton btn_send;
    EditText text_send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
/*
        Toolbar toolbar=findViewById(R.id.toolbar_chat);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

 */

        username=findViewById(R.id.username_chat);
        btn_send=findViewById(R.id.btn_send);
        text_send=findViewById(R.id.text_send);
        
        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        
        intent=getIntent();
        String userId=intent.getStringExtra("userid");
        fuser=FirebaseAuth.getInstance().getCurrentUser();

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    String msg= text_send.getText().toString();
                    if(!msg.equals("")){
                        sendMessage(fuser.getUid(), userId, msg);
                    } else{
                        Toast.makeText(ChatActivity.this, "Non puoi inviare un messaggio vuoto", Toast.LENGTH_SHORT).show();
                    }
                    text_send.setText("");
            }
        });

        reference=FirebaseDatabase.getInstance().getReference("users").child(userId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Persona persona = dataSnapshot.getValue(Persona.class);
                username.setText(persona.getUsername());
                readMessage(fuser.getUid(), userId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void sendMessage(String sender, String receiver, String message){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);

        reference.child("chats").push().setValue(hashMap);
    }

    private void readMessage(String myid, String userid){
        messageList=new ArrayList<>();
        reference=FirebaseDatabase.getInstance().getReference("chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messageList.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Chat chat=snapshot.getValue(Chat.class);
                    if(chat.getReceiver().equals(myid) && chat.getSender().equals(userid) ||
                            chat.getReceiver().equals(userid) && chat.getSender().equals(myid)){
                        messageList.add(chat);
                    }
                    messageAdapter=new MessageAdapter(ChatActivity.this,messageList);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}