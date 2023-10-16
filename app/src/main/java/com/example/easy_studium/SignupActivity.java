package com.example.easy_studium;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;

public class SignupActivity extends AppCompatActivity {

    /*inizializzazione variabili*/
    String username, password, email;

    EditText usernameText, emailText, passwordText;
    public Persona persona;
    TextView errorText, clearButton;
    Button salvaButton;
    ProgressBar progressBar;
    public static FirebaseDatabase database;
    DatabaseReference reference;
    static public ArrayList<Persona> rubrica = new ArrayList<>();

    FirebaseAuth mAuth= FirebaseAuth.getInstance();
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        passwordText = findViewById(R.id.userPassword);
        usernameText = findViewById(R.id.userName);
        emailText = findViewById(R.id.userEmail);
        salvaButton = findViewById(R.id.salvaButton);
        clearButton = findViewById(R.id.clearButton);
        errorText = findViewById(R.id.errorText);
        progressBar=findViewById(R.id.progressBar);

        /*inizializzazione di una Persona*/
        persona = new Persona();

        /*indica la posizione nel database a cui si farà riferimento quando si creerà un nuovo utente
        * quindi nella cartella "users"*/
        reference=FirebaseDatabase.getInstance().getReference("users");

        /*Button che serve per pulire tutti i campi che son stati compilati*/
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usernameText.getText().clear();
                emailText.getText().clear();
                passwordText.getText().clear();
            }
        });

        /*Iscrive una nuova persona se tutti i campi son stati compilati correttamente*/
        salvaButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                // if(checkInput()) {
                progressBar.setVisibility(View.VISIBLE);
                username=String.valueOf(usernameText.getText());
                email=String.valueOf(emailText.getText());
                password=String.valueOf(passwordText.getText());

                /*se l'edit text username sarà vuoto allora ricorderemo all'utente di inserire l'username*/
                if(TextUtils.isEmpty(username)){
                    usernameText.setError("inserire username");
                    Toast.makeText(SignupActivity.this, "Enter username",Toast.LENGTH_SHORT).show();
                    return;
                }

                /*se l'edit text email sarà vuoto allora ricorderemo all'utente di inserire la mail*/
                if(TextUtils.isEmpty(email)){
                    emailText.setError("inserire email");
                    Toast.makeText(SignupActivity.this, "Enter email",Toast.LENGTH_SHORT).show();
                    return;
                }

                /*se l'edit text password sarà vuoto allora ricorderemo all'utente di inserire la password*/
                if(TextUtils.isEmpty(password)){
                    passwordText.setError("inserire password");
                    Toast.makeText(SignupActivity.this, "Enter password",Toast.LENGTH_SHORT).show();
                    return;
                }

                /*questo metodo userà le credenziali inserite per creare una nuova Persona e autenticarla
                * inserindela nel database*/
                signup();
            }
        });
    }
    /*questo metodo userà le credenziali inserite per creare una nuova Persona e autenticarla
     * inserindela nel database*/
    private void signup() {
        /*questo metodo  stato preso dalla documentazione di Firebase per l'autenticazione di un nuovo
        * utente e l'inserimento dei suoi dati nel database*/
        FirebaseAuth
                .getInstance()
                .createUserWithEmailAndPassword(email.trim(), password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        UserProfileChangeRequest userProfileChangeRequest=new UserProfileChangeRequest
                                .Builder()
                                .setDisplayName(username).build();
                        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
                        firebaseUser.updateProfile(userProfileChangeRequest);
                        /*verranno prese le credenziali inserite nei vari EditText e verrà creata una
                        * Persona*/
                        Persona persona= new Persona(FirebaseAuth.getInstance().getCurrentUser().getUid()
                                , username, email, password);

                        /*questa persona verrà aggiunta nel reference inizializzato in precedenza*/
                        reference.child(FirebaseAuth.getInstance().getUid()).setValue(persona);
                        /*si passerà di nuovo al LoginActivity*/
                        startActivity(new Intent(SignupActivity.this,LoginActivity.class));
                        finish();
                    }
                });
    }
}