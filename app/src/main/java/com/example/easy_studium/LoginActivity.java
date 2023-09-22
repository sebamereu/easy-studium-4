package com.example.easy_studium;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {


    static public TextView passwordText; //static public perchè serve nell'AdminActivity
    EditText userText;
    Button loginButton;
    TextView errorText, signinButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        /*assegnazione dei vari EditText/TextView/Button presenti nel file .xml*/
        userText = findViewById(R.id.attrUser);
        passwordText = findViewById(R.id.attrPassword);
        loginButton = findViewById(R.id.loginButton);
        signinButton = findViewById(R.id.signinButton);


        /*se si preme la textView "iscriviti" porta alla SignupActivity */
        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });

        /*se si preme il button "accedi" porta alla LoginActivity */
        loginButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
               // if(checkLogin()){
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                //}
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