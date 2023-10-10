package com.example.easy_studium;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.easy_studium.databinding.FragmentUserBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserFragment extends Fragment {

    TextView userText, passwordText, modificaPassword;
    Button logoutButton, chatButton;
    Persona personaRicevuta;
    FirebaseAuth auth;
    FirebaseUser user;
    UserAdapter userAdapter;
    FragmentUserBinding fragmentUserBinding;
    List<Persona> personaList;
    RecyclerView recyclerView;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragmentUserBinding = FragmentUserBinding.inflate(inflater, container, false);
        View view = inflater.inflate(R.layout.fragment_user,container,false);

        recyclerView= view.findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        personaList= new ArrayList<>();
        readUsers();

        userText=view.findViewById(R.id.confUser);
        logoutButton=view.findViewById(R.id.logoutButton);
        //chatButton=view.findViewById(R.id.chatButton);

        auth= FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        userText.setText(user.getEmail());
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(),  LoginActivity.class));
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    private void readUsers() {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                personaList.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Persona persona=snapshot.getValue(Persona.class);
                    if(!persona.getUserId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                        personaList.add(persona);
                    }
                }
                userAdapter=new UserAdapter(getContext(), personaList);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

    }
}