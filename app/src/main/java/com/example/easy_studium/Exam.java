package com.example.easy_studium;

import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class Exam {

    public static ArrayList<String> arrayList1=new ArrayList<String>();
    public static ArrayList<Exam> listExam=new ArrayList<>();

    private String name;
    private String cfu;

    public Exam(String name, String cfu){
        this.name=name;
        this.cfu=cfu;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCfu() {
        return cfu;
    }

    public void setCfu(String cfu) {
        this.cfu = cfu;
    }
}
