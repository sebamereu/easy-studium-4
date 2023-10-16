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

    /*inizializzazione variabili*/
    public EditText passwordText; //static public perchè serve nell'AdminActivity
    EditText userText;
    Button loginButton;
    TextView signinButton;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    /*onStart verrà attivata all'inizio del ciclo di vita del LoginActivity
    * controllerà se l'utente si era già loggato in precedenza e non ha effettuato il logout,
    * in caso affermativo si passa direttamente al MainActivity*/
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

    /*gestisce tutta la parte di back-end del file activity_login.xml*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*prende il file activity_login presente nella cartella dei layout*/
        setContentView(R.layout.activity_login);


        /*assegnazione dei vari EditText/TextView/Button presenti nel file .xml*/
        userText = findViewById(R.id.attrUser);
        passwordText = findViewById(R.id.attrPassword);
        loginButton = findViewById(R.id.loginButton);
        signinButton = findViewById(R.id.signinButton);
        progressBar=findViewById(R.id.progressBarLogin);

        /*inizializza l'utente loggato in quel momento*/
        mAuth = FirebaseAuth.getInstance();


        /*se si preme il button "login" verranno effettuati i controlli per l'accesso*/
        loginButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                String username, password;
                username = String.valueOf(userText.getText());
                password = String.valueOf(passwordText.getText());

                /*se l'edit text email sarà vuoto allora ricorderemo all'utente di inserire la mail*/
                if (TextUtils.isEmpty(username)) {
                    userText.setError("Enter email");
                    Toast.makeText(LoginActivity.this, "Enter email.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                /*se l'edit text password sarà vuoto allora ricorderemo all'utente di inserire la mail*/
                if (TextUtils.isEmpty(password)) {
                    passwordText.setError("Enter password");
                    Toast.makeText(LoginActivity.this, "Enter password.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                /*questo metodo serve per controllare che le credenziali inserite siano corrette
                * utlizzando il database*/
                login();
            }
        });

        /*se viene cliccato questo TextView si passerà alla SignupActivity*/
        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });
    }

    /*questo metodo serve per controllare che le credenziali inserite siano corrette
     * utlizzando il database*/
    private void login() {
        /*useremo un metodo presente nella documentazione di FireBase pe rcontrollare che le
        * credenziali siano corrette*/
        FirebaseAuth
                .getInstance()
                .signInWithEmailAndPassword(userText.getText().toString().trim()
                        , passwordText.getText().toString())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        /*in caso di successo si passa al MainActivity*/
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                        finish();
                    }
                });
    }
}