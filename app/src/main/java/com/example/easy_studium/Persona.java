package com.example.easy_studium;

import java.io.Serializable;

public class Persona implements Serializable {



    private String username;
    private String email;
    private String password;
    private String userId;


    private boolean flagAdmin;


    public Persona(String userId, String username, String email, String password) {
        this.userId= userId;
        this.username=(username);
        this.email = email;
        this.password = password;
    }

    public Persona(){
        this.userId="";
        this.username="";
        this.email ="";
        this.password="";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}

