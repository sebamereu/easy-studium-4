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
import java.util.ArrayList;
import java.util.List;

/*questa classe gestisce il terzo fragment, dove si può visualizzare la lista con tutti gli utenti
* e aprire la chat con ognuno di essi*/
public class UserFragment extends Fragment {

    /*inizializzazione variabili*/
    TextView userText;
    Button logoutButton;
    FirebaseAuth auth;
    FirebaseUser user;
    UserAdapter userAdapter;
    FragmentUserBinding fragmentUserBinding;
    List<Persona> personaList;
    RecyclerView recyclerView;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /*gestisce tutto il fragment_user.xml*/
        fragmentUserBinding = FragmentUserBinding.inflate(inflater, container, false);
        View view = inflater.inflate(R.layout.fragment_user,container,false);

        userText=view.findViewById(R.id.confUser);
        logoutButton=view.findViewById(R.id.logoutButton);
        recyclerView= view.findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        personaList= new ArrayList<>();

        /*metodo per estrarre gli users dal database e assegnarli alla personaList*/
        readUsers();

        /*assegnamo ad auth l'utente loggato*/
        auth= FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        userText.setText(user.getEmail());

        /*cliccando questo button si effettuerà il logout dell'utente e si ritornerà al LoginActivity*/
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

    /*metodo per estrarre gli users dal database e assegnarli alla personaList*/
    private void readUsers() {
        /*si crea il riferimento per inserire/prendere i dati dal database*/
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                /*puliamo la lista di utenti*/
                personaList.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    /*prendiamo dal database gli elementi Persona che son stati autenticati in precedenza*/                    Persona persona=snapshot.getValue(Persona.class);
                    /*la lista sarà creata da tutti gli utenti apparte quello loggato*/
                    if(!persona.getUserId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                        personaList.add(persona);
                    }
                }
                /*adattiamo la recyclerView usando la personaList*/
                /*lo userAdapter ci consentirà inoltre di cliccare nel label di un utente e aprire la chatactivity
                * con quell'utente*/
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