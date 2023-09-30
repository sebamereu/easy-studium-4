package com.example.easy_studium;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {




    public EditText passwordText; //static public perchè serve nell'AdminActivity
    EditText userText;
    Button loginButton;
    TextView errorText, signinButton;

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

        SessionManager sessionManager;

        sessionManager = new SessionManager(getApplicationContext());
        //Firebase.setAndroidContext(this);


        /*se si preme il button "login" porta alla LoginActivity */
        loginButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
/*
                if (!validateUsername() | !validatePassword()){

                } else {
                    checkUser();
                }

 */


                String username, password;
                username=String.valueOf(userText.getText());
                password=String.valueOf(passwordText.getText());

                if(TextUtils.isEmpty(username)){
                    Toast.makeText(LoginActivity.this, "Enter email.",
                            Toast.LENGTH_SHORT).show();
                }

                if(TextUtils.isEmpty(password)){
                    Toast.makeText(LoginActivity.this, "Enter password.",
                            Toast.LENGTH_SHORT).show();
                }

                mAuth.signInWithEmailAndPassword(username, password)
                    .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(LoginActivity.this, "Authentication successed.",
                                        Toast.LENGTH_SHORT).show();
                                sessionManager.createLoginSession(username,password);


                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();





                            }
                            else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


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
        String val = userText.getText().toString();
        if (val.isEmpty()){
            userText.setError("Username cannot be empty");
            return false;
        } else {
            userText.setError(null);
            return true;
        }
    }
    public Boolean validatePassword(){
        String val = passwordText.getText().toString();
        if (val.isEmpty()){
            passwordText.setError("Password cannot be empty");
            return false;
        } else {
            passwordText.setError(null);
            return true;
        }
    }

    public void checkUser(){
        String userUsername = userText.getText().toString().trim();
        String userPassword = passwordText.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user");
        Query checkUserDatabase = reference.orderByChild("username").equalTo(userUsername);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    userText.setError(null);
                    String passwordFromDB = snapshot.child(userUsername).child("password").getValue(String.class);

                    if (passwordFromDB.equals(userPassword)){
                        userText.setError(null);

                        //Pass the data using intent

                        //String nameFromDB = snapshot.child(userUsername).child("name").getValue(String.class);
                        //String emailFromDB = snapshot.child(userUsername).child("email").getValue(String.class);
                        String usernameFromDB = snapshot.child(userUsername).child("username").getValue(String.class);

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                        //intent.putExtra("name", nameFromDB);
                        //intent.putExtra("email", emailFromDB);
                        intent.putExtra("username", usernameFromDB);
                        intent.putExtra("password", passwordFromDB);

                        startActivity(intent);
                    } else {
                        passwordText.setError("Invalid Credentials");
                        passwordText.requestFocus();
                    }
                } else {
                    userText.setError("User does not exist");
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
    /*
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

     */

    private boolean checkAdmin() {
        int errors=0;
        if (passwordText.getText().toString().equals("admin")
                && userText.getText().toString().equals("admin")){errors++;}

        return errors != 0;
    }

/*
    private boolean checkUser(String user, String password){
        for(int i=0; i<SignupActivity.rubrica.size();i++){
            if(SignupActivity.rubrica.get(i).getUsername().equals(user) &&
                    SignupActivity.rubrica.get(i).getPassowrd().equals(password)){
                return true;

            }
        }
        return false;

    }

 */
}