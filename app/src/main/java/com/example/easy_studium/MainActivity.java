package com.example.easy_studium;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.easy_studium.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(new DailyCalendarFragment());

        // Get an instance of FirebaseAuth
        FirebaseAuth auth = FirebaseAuth.getInstance();

// Get the currently authenticated user
        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            // The user is authenticated, so we can proceed with getting the child object

            // Get a reference to the database
            FirebaseDatabase database = FirebaseDatabase.getInstance();

            // Create a reference to the user in the database
            DatabaseReference reference = database.getReference("user");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        for (DataSnapshot examSnapshot : userSnapshot.getChildren()) {
                            for (DataSnapshot examNameSnapshot : examSnapshot.getChildren()) {

                                String nameExam = (String) examNameSnapshot.child("nameExam").getValue();
                                String cfuExam = (String) examNameSnapshot.child("cfu").getValue();
                                if (nameExam != null) {
                                    Exam e = new Exam(nameExam, cfuExam);
                                    e.setNameExam(nameExam);
                                    e.setCfu(cfuExam);
                                    Exam.arrayList1.add(nameExam);
                                    Exam.listExam.add(e);
                                } else {
                                    System.out.println("exam is null");
                                }
/*
                                String nameEvent = (String) examNameSnapshot.child("NameEvent").getValue();
                                String examMode = (String) examNameSnapshot.child("ExamMode").getValue();
                                String examName = (String) examNameSnapshot.child("ExamName").getValue();
                                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // adjust this to match your date format
                                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS"); // adjust this to match your time format

                                LocalDate dateEvent = LocalDate.parse((Objects.requireNonNull(examNameSnapshot.child("DateEvent").getValue()).toString()), dateFormatter);
                                LocalTime time = LocalTime.parse((String) examNameSnapshot.child("Time").getValue(), timeFormatter);

                                if (nameEvent != null) {
                                    Event event = new Event(nameEvent, dateEvent, time,examName,examMode);
                                    Event.eventsList.add(event);

                                } else {
                                    System.out.println("event is null");
                                }

 */

                            }

                        }

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle error...
                }
            });
            ExamFragment.arrayList=Exam.arrayList1;
            ExamFragment.arrayListExam=Exam.listExam;
            ExamFragment.adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, ExamFragment.arrayList);
            ExamFragment.adapterExam = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, ExamFragment.arrayListExam);

        }


        binding.bottomNavigationView2.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.calendario:
                    replaceFragment(new DailyCalendarFragment());
                    break;
                case R.id.esami:
                    replaceFragment(new ExamStatFragment());
                    break;
                case R.id.profilo:
                    replaceFragment(new UserFragment());
                    break;


            }


            return true;
        });
    }

    void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}