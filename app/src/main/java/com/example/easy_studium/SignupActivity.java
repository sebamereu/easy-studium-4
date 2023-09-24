package com.example.easy_studium;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;

public class SignupActivity extends AppCompatActivity {


    EditText usernameText, dataText, cittaText, passwordConfText, passwordText;
    public Persona persona;
    TextView errorText, clearButton;
    Button salvaButton, signinButton;
    FirebaseDatabase database;
    DatabaseReference reference;
    int modelvalue = 20;
    static public String PERSONA_EXTRA = "com.example.LoginAdmin.Persona";
    static public ArrayList<Persona> rubrica = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        passwordText = findViewById(R.id.attrPassword);
        passwordConfText = findViewById(R.id.attrPasswordConf);
        usernameText = findViewById(R.id.attrUsername);
        dataText = findViewById(R.id.attrData);
        cittaText = findViewById(R.id.attrCitta);
        salvaButton = findViewById(R.id.salvaButton);
        clearButton = findViewById(R.id.clearButton);
        errorText = findViewById(R.id.errorText);

        /*inizializzazione di una Persona*/
        persona = new Persona();

        /*Button che serve per pulire tutti i campi che son stati compilati*/
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usernameText.getText().clear();
                passwordText.getText().clear();
                passwordConfText.getText().clear();
                dataText.getText().clear();
                cittaText.getText().clear();
            }
        });

        /*Iscrive una nuova persona se tutti i campi son stati compilati correttamente*/
        salvaButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                // if(checkInput()) {
                database = FirebaseDatabase.getInstance();
                reference = database.getReference("user");
                String name = usernameText.getText().toString();
                String password = passwordText.getText().toString();
                String citta = cittaText.getText().toString();
                Persona persona1 = new Persona(name, password, citta);
                reference.child(name).setValue(persona1);

                Toast.makeText(SignupActivity.this,"Registrazione avvenuta con successo", Toast.LENGTH_SHORT).show();
                //    aggiornaPersona();

                //intent.putExtra(PERSONA_EXTRA, persona);
                //rubrica.add(persona);
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
                // }
            }
        });

        /*apre calendario*/
        dataText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    dataText.setRawInputType(InputType.TYPE_NULL);
                    new DatePickerFragment().show(
                            getSupportFragmentManager(), DatePickerFragment.TAG);


                }

            }
        });
    }

    protected void updateValue(int newValue) {
        this.modelvalue = newValue;


    }


    private void aggiornaPersona() {

        String usernameInserito = usernameText.getText().toString();
        this.persona.setUsername(usernameInserito);

        String passwordInserito = passwordText.getText().toString();
        this.persona.setPassowrd(passwordInserito);

        String cittaInserito = cittaText.getText().toString();
        this.persona.setCitta(cittaInserito);

    }

    public void doPositiveClick(Calendar date) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        dataText.setText(format.format(date.getTime()));
    }

    /*true se è andato a buon fine, false altrimenti*/
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
            if (usernameText.getText().toString().equals(SignupActivity.rubrica.get(i).getUsername())) {
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
}