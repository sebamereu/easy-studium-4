package com.example.easy_studium;

import java.io.Serializable;
import java.util.Calendar;
import java.io.StringReader;

public class Persona implements Serializable {


    private String username;
    private String passowrd;
    private String data;


    private boolean flagAdmin;
    private String citta;


    public Persona(String username, String passowrd, String citta) {

        this.username = username;
        this.passowrd = passowrd;
        this.citta = citta;;
    }

    public Persona(){
        this.username="";
        this.passowrd="";
        this.citta="";;
    }


    public String getPassowrd() {
        return passowrd;
    }

    public void setPassowrd(String passowrd) {
        this.passowrd = passowrd;
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

