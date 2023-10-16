package com.example.easy_studium;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
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


/*questa classe l'activty di messaggistica tra utenti*/
public class ChatActivity extends AppCompatActivity {

    /*inizializzazione variabili*/
    TextView username;
    FirebaseUser fuser;
    DatabaseReference reference;
    MessageAdapter messageAdapter;
    List<Chat> messageList;
    RecyclerView recyclerView;
    Intent intent;
    ImageButton btn_send;
    EditText text_send;

    /*gestisce tutto il activity_chat.xml*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

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

        /*se viene cliccato il tasto di invio*/
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    /*il messaggio vero e proprio sarà la stringa presente nell'editText*/
                    String msg= text_send.getText().toString();
                    /*viene controllato se la stringa inviata non è vuota*/
                    if(!msg.equals("")){
                        /*il metodo sendMessage serve per inserire il messaggio all'interno del database*/
                        sendMessage(fuser.getUid(), userId, msg);
                    } else{
                        Toast.makeText(ChatActivity.this, "Non puoi inviare un messaggio vuoto", Toast.LENGTH_SHORT).show();
                    }
                    text_send.setText("");
            }
        });
        /*si crea il riferimento per inserire/prendere i dati dal database*/
        reference=FirebaseDatabase.getInstance().getReference("users").child(userId);

        /*metodo che viene chiamato nel momento in cui viene aggiunto/modficato un dato nel database*/
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Persona persona = dataSnapshot.getValue(Persona.class);
                username.setText(persona.getUsername());
                /*il metodo readMessage serve per leggere i messaggi se sono stati inviati da qualche altro
                * utente*/
                readMessage(fuser.getUid(), userId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    /*il metodo sendMessage serve per inserire il messaggio all'interno del database*/
    private void sendMessage(String sender, String receiver, String message){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        /*viene inserito nel database un hashmap con l'ID del mittente e del destinatario e il messaggio
        * da inviare*/
        reference.child("chats").push().setValue(hashMap);
    }

    /*il metodo readMessage serve per leggere i messaggi se sono stati inviati da qualche altro
     * utente*/
    private void readMessage(String myid, String userid){
        messageList=new ArrayList<>();
        /*si crea il riferimento per inserire/prendere i dati dal database*/
        reference=FirebaseDatabase.getInstance().getReference("chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                /*puliamo la lista di messaggi*/
                messageList.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    /*prendiamo dal database gli elementi Chat che son stati creati dall'utente
                     * loggato*/
                    Chat chat=snapshot.getValue(Chat.class);
                    if(chat.getReceiver().equals(myid) && chat.getSender().equals(userid) ||
                            chat.getReceiver().equals(userid) && chat.getSender().equals(myid)){
                        /*inseriamoli nelle liste i messaggi dell'utente loggato*/
                        messageList.add(chat);
                    }
                    /*usiamo la classe MessageAdapter che serve per adattare la schermata della chat
                    * con la chat tra l'utente loggato e l'utente cliccato dalla personaList selezionato
                    * in precedenza*/
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
