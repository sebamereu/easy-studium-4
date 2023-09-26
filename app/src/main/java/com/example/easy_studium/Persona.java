package com.example.easy_studium;

import java.io.Serializable;
import java.util.Calendar;
import java.io.StringReader;

public class Persona implements Serializable {


    private String username;
    private String password;
    private String data;


    private boolean flagAdmin;
    private String citta;


    public Persona(String username, String password, String citta) {

        this.username = username;
        this.password = password;
        this.citta = citta;;
    }

    public Persona(){
        this.username="";
        this.password="";
        this.citta="";;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getCitta() {
        return citta;
    }

    public void setCitta(String citta) {
        this.citta = citta;
    }

}

