package com.example.easy_studium;

import java.io.Serializable;
import java.util.Calendar;
import java.io.StringReader;

public class Persona implements Serializable {


    private String username;
    private String passowrd;
    private Calendar data;


    private boolean flagAdmin;
    private String citta;


    public Persona(String username, String passowrd,  Calendar data, String citta, boolean flagAdmin) {

        this.username = username;
        this.passowrd = passowrd;
        this.data = data;
        this.citta = citta;
        this.flagAdmin = flagAdmin;
    }

    public Persona(){
        this.username="";
        this.passowrd="";
        this.citta="";
        this.flagAdmin=false;
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

    public Calendar getData() {
        return data;
    }

    public void setData(Calendar data) {
        this.data = data;
    }


    public String getCitta() {
        return citta;
    }

    public void setCitta(String citta) {
        this.citta = citta;
    }

    public boolean isFlagAdmin() {
        return flagAdmin;
    }

    public String isAdminString(){
        if(this.flagAdmin) return "true";
        else return "false";
    }

    public void setFlagAdmin(boolean flagAdmin) {
        this.flagAdmin = flagAdmin;
    }
}

