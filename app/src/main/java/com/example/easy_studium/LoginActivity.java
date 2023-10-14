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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
    TextView signinButton;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent= new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        /*assegnazione dei vari EditText/TextView/Button presenti nel file .xml*/
        userText = findViewById(R.id.attrUser);
        passwordText = findViewById(R.id.attrPassword);
        loginButton = findViewById(R.id.loginButton);
        signinButton = findViewById(R.id.signinButton);
        progressBar=findViewById(R.id.progressBarLogin);

        mAuth = FirebaseAuth.getInstance();


        /*se si preme il button "login" porta alla LoginActivity */
        loginButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                String username, password;
                username = String.valueOf(userText.getText());
                password = String.valueOf(passwordText.getText());

                if (TextUtils.isEmpty(username)) {
                    userText.setError("Enter email");
                    Toast.makeText(LoginActivity.this, "Enter email.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    passwordText.setError("Enter password");
                    Toast.makeText(LoginActivity.this, "Enter password.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                login();
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

    private void login() {
        FirebaseAuth
                .getInstance()
                .signInWithEmailAndPassword(userText.getText().toString().trim()
                        , passwordText.getText().toString())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                        finish();
                    }
                });
    }
}