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

    String username, password, email;

    EditText usernameText, emailText, passwordText;
    public Persona persona;
    TextView errorText, clearButton;
    Button salvaButton, signinButton;
    ProgressBar progressBar;
    public static FirebaseDatabase database;
    DatabaseReference reference;
    int modelvalue = 20;
    static public String PERSONA_EXTRA = "com.example.LoginAdmin.Persona";
    static public ArrayList<Persona> rubrica = new ArrayList<>();

    FirebaseAuth mAuth= FirebaseAuth.getInstance();
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        /*FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent= new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }

         */
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        passwordText = findViewById(R.id.userPassword);
        //passwordConfText = findViewById(R.id.attrPasswordConf);
        usernameText = findViewById(R.id.userName);
        emailText = findViewById(R.id.userEmail);

        //dataText = findViewById(R.id.attrData);
        //cittaText = findViewById(R.id.attrCitta);
        salvaButton = findViewById(R.id.salvaButton);
        clearButton = findViewById(R.id.clearButton);
        errorText = findViewById(R.id.errorText);
        progressBar=findViewById(R.id.progressBar);

        /*inizializzazione di una Persona*/
        persona = new Persona();
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

                if(TextUtils.isEmpty(username)){
                    Toast.makeText(SignupActivity.this, "Enter email",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(SignupActivity.this, "Enter password",Toast.LENGTH_SHORT).show();
                    return;
                }
                signup();

/*
                mAuth.createUserWithEmailAndPassword(username, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);

                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(SignupActivity.this, "Authentication successes.",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(SignupActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

 */



                //database = FirebaseDatabase.getInstance();
                //reference = database.getReference("user");

                //String username = usernameText.getText().toString();
                //String password = passwordText.getText().toString();

                //Persona persona = new Persona(username, password);
                //reference.child(username).setValue(persona);

                Toast.makeText(SignupActivity.this, "You have signup successfully!", Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                //startActivity(intent);

                //database = FirebaseDatabase.getInstance();
                //reference = database.getReference("user");
                //String password = passwordText.getText().toString();
                //String citta = cittaText.getText().toString();
                //Persona persona1 = new Persona(password, password, citta);
                //reference.child(password).setValue(persona1);



                //    aggiornaPersona();

                //intent.putExtra(PERSONA_EXTRA, persona);
                //rubrica.add(persona);
                //Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                //startActivity(intent);
                // }
            }
        });

        /*apre calendario*/

    }
    private void signup() {
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
                        Persona persona= new Persona(FirebaseAuth.getInstance().getCurrentUser().getUid()
                                , username, email, password);

                        reference.child(FirebaseAuth.getInstance().getUid()).setValue(persona);
                        startActivity(new Intent(SignupActivity.this,MainActivity.class));
                        finish();
                    }
                });
    }

    protected void updateValue(int newValue) {
        this.modelvalue = newValue;


    }


    private void aggiornaPersona() {
        String usernameInserito = usernameText.getText().toString();
        this.persona.setUsername(usernameInserito);

        String emailInserito = emailText.getText().toString();
        this.persona.setEmail(emailInserito);

        String passwordInserito = passwordText.getText().toString();
        this.persona.setPassword(passwordInserito);


    }
/*
    public void doPositiveClick(Calendar date) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        dataText.setText(format.format(date.getTime()));
    }*/

    /*true se è andato a buon fine, false altrimenti*/
    /*
    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean checkInput() {
        int errors = 0;

        if (dataText.getText() == null || dataText.getText().length() == 0) {
            errors++;
            dataText.setError("Inserire la data");
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String date = dataText.getText().toString();
            LocalDate newDate = LocalDate.parse(date, formatter);

            Log.d("Anno", "value = " + newDate);
            Period period = Period.between(newDate, LocalDate.now());// con anno 1995 e anno corrente 2021 avrò 26, invece se metto una data nel 2021 avrò 0
            Log.d("Anno", "value = " + period.getYears());

            if (period.getYears() < 18) {

                errors++;
                dataText.setError("Devi essere maggiorenne per iscriverti");
            } else {
                dataText.setError(null);
            }
        }

        for (int i = 0; i < SignupActivity.rubrica.size(); i++) {
            if (usernameText.getText().toString().equals(SignupActivity.rubrica.get(i).getEmail())) {
                errors++;
                usernameText.setError("Username già presente");
                break;
            } else usernameText.setError(null);
        }

        if (passwordText.getText() == null || !passwordConfText.getText().toString().equals(passwordText.getText().toString())) {
            errors++;
            passwordConfText.setError("Le due password non coincidono");
        } else passwordConfText.setError(null);

        if (passwordText.getText() == null
                || passwordText.length() < 8
                || !passwordText.getText().toString().matches("(.*[0-9].*)")
                || !passwordText.getText().toString().matches("(.*[A-Z].*)")
                || !passwordText.getText().toString().matches("^(?=.*[_.()$&@]).*$")) {
            errors++;

            passwordText.setError("La password deve essere formata" +
                    " da almeno 8 caratteri di cui" +
                    " un numero, una lettera maiuscola e un simbolo.");
        } else passwordText.setError(null);


        if (usernameText.getText() == null || usernameText.getText().length() == 0) {
            errors++;
            usernameText.setError("Campo vuoto");
        } else usernameText.setError(null);


        if (usernameText.getText().toString().equals("admin")) {
            errors++;
            usernameText.setError("Username non valido");
        } else usernameText.setError(null);

        if (cittaText.getText() == null || cittaText.length() <= 2) {
            errors++;
            cittaText.setError("Inserire una città");
        } else cittaText.setError(null);

        switch (errors) {
            case 0:
                errorText.setVisibility(View.GONE);
                errorText.setText("");
                break;
            case 1:
                errorText.setVisibility(View.VISIBLE);
                errorText.setText("Si è verificato un errore");
                break;
            default:
                errorText.setVisibility(View.VISIBLE);
                errorText.setText("Si sono verificati " + errors + " errori");
                break;
        }

        return errors == 0;
    }

     */
}