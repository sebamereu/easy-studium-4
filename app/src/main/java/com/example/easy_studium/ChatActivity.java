package com.example.easy_studium;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.example.easy_studium.databinding.ActivityChatBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChatActivity extends AppCompatActivity {
    ActivityChatBinding binding;
    DatabaseReference databaseReference;
    UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
/*
        binding.recycler.setLayoutManager(new LinearLayoutManager(this));
        databaseReference= FirebaseDatabase.getInstance().getReference("users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(UserAdapter.personaList!=null)UserAdapter.personaList.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                        String uid=dataSnapshot.getKey();
                        if(!uid.equals(FirebaseAuth.getInstance().getUid())){
                            Persona persona= dataSnapshot.child(uid).getValue(Persona.class);
                            if(persona!=null) UserAdapter.personaList.add(persona);
                        }
                    }
                }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

 */
    }
}