package com.example.easy_studium;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

public class PasswordActivity extends AppCompatActivity {

    EditText vecchiaPass, nuovaPass, nuovaPassConf;
    String password;
    TextView errorText;
    ImageView imageView;
    Button cambiaPass;
    Persona personaRicevuta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        vecchiaPass=findViewById(R.id.passVecchia);
        nuovaPass=findViewById(R.id.passNuova);
        nuovaPassConf=findViewById(R.id.passNuovaConf);
        cambiaPass=findViewById(R.id.passwordChange);
        errorText=findViewById(R.id.errorText);
        imageView=findViewById(R.id.imageView);

        personaRicevuta= SignupActivity.rubrica.get(SignupActivity.rubrica.size()-1);

        /*button per modificare le password se i campi son stati riempiti correttamente*/
        cambiaPass.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                if(checkOldPassword()){
                    password=nuovaPass.getText().toString();
                    personaRicevuta.setPassowrd(password);
                    startActivity(new Intent(PasswordActivity.this, UserFragment.class));

                }
            }

        });

        /*Immagine che porta alla pagina precedente*/
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PasswordActivity.this, UserFragment.class));
            }
        });

    }

    /*Metodo per verificare la correttezza della vecchia e della nuova password*/





    private boolean checkOldPassword() {
        int errors=0;

        if(vecchiaPass.getText() ==null ||vecchiaPass.getText().length()==0){
            errors++;
            vecchiaPass.setError("Campo vuoto");
        }
        else vecchiaPass.setError(null);

        if(nuovaPass.getText() ==null||nuovaPass.getText().length()==0){
            errors++;
            nuovaPass.setError("Campo vuoto");
        }
        else nuovaPass.setError(null);

        if(nuovaPassConf.getText() ==null ||nuovaPassConf.getText().length()==0){
            errors++;
            nuovaPassConf.setError("Campo vuoto");
        }
        else nuovaPassConf.setError(null);



        if(!vecchiaPass.getText().toString().equals(personaRicevuta.getPassowrd())){
            errors++;
            vecchiaPass.setError("Password vecchia sbagliata");
        }

        if(nuovaPass.getText().toString().equals(vecchiaPass.getText().toString())){
            errors++;
            nuovaPass.setError("La password vecchia è uguale a quella nuova");
        }

        if(!nuovaPass.getText().toString().equals(nuovaPassConf.getText().toString())){
            errors++;
            nuovaPassConf.setError("Le due password nuove non coincidono");
        }


        if (nuovaPass.getText()==null
                || nuovaPass.length()<8
                || !nuovaPass.getText().toString().matches("(.*[0-9].*)")
                || !nuovaPass.getText().toString().matches("(.*[A-Z].*)")
                || !nuovaPass.getText().toString().matches("^(?=.*[_.()$&@]).*$")){
            errors++;
            nuovaPass.setError("La password deve essere formata" +
                    " da almeno 8 caratteri di cui almeno: " +
                    " un numero, una lettera maiuscola e un simbolo.");
        }

        if(nuovaPass.getText().toString().equals("admin")){
            errors++;
            nuovaPass.setError("L'admin non può cambiare la password");
        }

        if(vecchiaPass.getText().toString().equals(nuovaPass.getText().toString())){
            errors++;
            nuovaPass.setError("La nuova password non può essere uguale alla vecchia");
        }


        switch (errors){
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
                errorText.setText("Si sono verificati "+errors+" errori");
                break;
        }

        return errors == 0;
    }
}