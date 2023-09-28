package com.example.easy_studium;

import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Map;

public class Exam {

    public static ArrayList<String> arrayList1=new ArrayList<>();
    public static ArrayList<Exam> listExam=new ArrayList<>();

    private String nameExam;
    private String cfu;

    public Exam(String nameExam, String cfu){
        this.nameExam=nameExam;
        this.cfu=cfu;
    }
    // Constructor that takes a Map
    public Exam(Map<String, Object> map) {
        this.nameExam = (String) map.get("exam");
        // Initialize other fields...
    }

    public Exam(){}



    public String getNameExam() {
        return nameExam;
    }

    public void setNameExam(String nameExam) {
        this.nameExam = nameExam;
    }

    public String getCfu() {
        return cfu;
    }

    public void setCfu(String cfu) {
        this.cfu = cfu;
    }
}
