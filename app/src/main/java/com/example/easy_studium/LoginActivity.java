package com.example.easy_studium;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {


    public EditText passwordText; //static public perchè serve nell'AdminActivity
    EditText userText;
    Button loginButton;
    TextView errorText, signinButton;
    FirebaseDatabase database;
    DatabaseReference reference;

    FirebaseAuth mAuth;
// ...
// Initialize Firebase Auth
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        /*assegnazione dei vari EditText/TextView/Button presenti nel file .xml*/
        userText = findViewById(R.id.attrUser);
        passwordText = findViewById(R.id.attrPassword);
        loginButton = findViewById(R.id.loginButton);
        signinButton = findViewById(R.id.signinButton);

        mAuth = FirebaseAuth.getInstance();

        /*se si preme il button "login" porta alla LoginActivity */
        loginButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if(!validateUsername()||!validatePassword()){
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }else {
                    checkUser();
                }
            }
        });

        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Write a message to the database
                 startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });
    }

    public Boolean validateUsername(){
        String val=userText.getText().toString();
        if(val.isEmpty()){
            userText.setError("username non può essere vuoto");
            return false;
        }else{
            userText.setError(null);
            return true;
        }
    }
    public Boolean validatePassword(){
        String val=passwordText.getText().toString();
        if(val.isEmpty()){
            passwordText.setError("password non può essere vuota");
            return false;
        }else{
            passwordText.setError(null);
            return true;
        }
    }
    public void checkUser(){
        String username = userText.getText().toString().trim();
        String password = passwordText.getText().toString().trim();
        DatabaseReference reference1= FirebaseDatabase.getInstance().getReference("user");
        Query checkUserDatabase = reference1.orderByChild("username").equalTo(username);
        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    userText.setError(null);
                    String passwordFromDB = snapshot.child(username).child("password").getValue(String.class);

                    //assert passwordFromDB != null;
                    if(!Objects.equals(passwordFromDB,password)){
                        userText.setError(null);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    }else {
                        passwordText.setError("Credenziali sbagliate");
                        passwordText.requestFocus();
                    }
                }else{
                    userText.setError("User non esistente");
                    userText.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /*metodi per controllare se le credenziali messe son di
    un utente già iscritto*/
    private boolean checkLogin() {
        int errors=0;
        if(userText.getText()==null || !checkUser(userText.getText().toString(), passwordText.getText().toString())) {
            errors++;
            userText.setError("username o password sbagliati");
            passwordText.setError("username o password sbagliati");
        }
        else userText.setError(null);

        return errors==0;

    }

    private boolean checkAdmin() {
        int errors=0;
        if (passwordText.getText().toString().equals("admin")
                && userText.getText().toString().equals("admin")){errors++;}

        return errors != 0;
    }


    private boolean checkUser(String user, String password){
        for(int i=0; i<SignupActivity.rubrica.size();i++){
            if(SignupActivity.rubrica.get(i).getUsername().equals(user) &&
                    SignupActivity.rubrica.get(i).getPassowrd().equals(password)){
                return true;

            }
        }
        return false;

    }
}