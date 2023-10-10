package com.example.easy_studium;

import static com.example.easy_studium.CalendarUtils.selectedDate;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
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
import com.google.firebase.database.annotations.Nullable;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

@RequiresApi(api = Build.VERSION_CODES.O)

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;





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

            // Create a reference to the user in the database
            /*ExamFragment.arrayList=ExamStatFragment.examNameList;
            ExamFragment.arrayListExam=(ArrayList<Exam>) ExamStatFragment.examList;
            ExamFragment.adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, ExamStatFragment.examNameList);
            ExamFragment.adapterExam = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, ExamStatFragment.examList);

             */

            CalendarUtils.selectedDate = LocalDate.now();
            String dateStr = CalendarUtils.selectedDate.toString(); // convert LocalDate to String

            ArrayList<HourEvent> list = new ArrayList<>();
            for (int hour = 0; hour < 24; hour++) {
                LocalTime time = LocalTime.of(hour, 0);
                ArrayList<Event> events = Event.eventsForDateAndTime(LocalDate.now(), time);
                HourEvent hourEvent = new HourEvent(time, events);
                list.add(hourEvent);

                time = LocalTime.of(hour, 30);
                events = Event.eventsForDateAndTime(LocalDate.now(), time);
                hourEvent = new HourEvent(time, events);
                list.add(hourEvent);
            }
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
