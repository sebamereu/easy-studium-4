package com.example.easy_studium;

import static com.example.easy_studium.CalendarUtils.selectedDate;


import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import com.example.easy_studium.MainActivity.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DailyCalendarFragment extends Fragment {

    private Button nextDayAction;
    public static Button newEventAction, previousDayAction;
    public static TextView monthDayText;

    public static TextView dayOfWeekTV;
    public static ListView hourListView;
    ArrayList<Event> eventList;


    public DailyCalendarFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daily_calendar, container, false);

        monthDayText = (TextView) view.findViewById(R.id.monthDayText);
        dayOfWeekTV = (TextView) view.findViewById(R.id.dayOfWeekTV);
        hourListView = (ListView) view.findViewById(R.id.hourListView);
        previousDayAction = (Button) view.findViewById(R.id.previousDayAction);
        nextDayAction = (Button) view.findViewById(R.id.nextDayAction);
        newEventAction = (Button) view.findViewById(R.id.newEventAction);

        CalendarUtils.selectedDate = LocalDate.now();

        eventList = new ArrayList<>();


        readEvent();
        //setDayView();

        previousDayAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusDays(1);
                setDayView();

            }
        });

        nextDayAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusDays(1);
                setDayView();
            }
        });

        newEventAction.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus)
                    new EventEditFragment().show(
                            getActivity().getSupportFragmentManager(), EventEditFragment.TAG);
            }
        });

        newEventAction.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {

                replaceFragment(new EventEditFragment());
            }
        });




        // Inflate the layout for this fragment
        return view;
    }

    private void readEvent() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("events");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Event.eventsList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Event event = snapshot.getValue(Event.class);
                    assert event != null;
                    if (event.getEventId().equals(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())) {
                        Event.eventsList.add(event);
                    }
                }
                setDayView();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        setDayView();
    }

    private void setDayView() {
        monthDayText.setText(CalendarUtils.monthDayFromDate(selectedDate));
        String dayOfWeek = selectedDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());
        dayOfWeekTV.setText(dayOfWeek);
        int dateDay = selectedDate.getDayOfYear();
        int day = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);

        if (dateDay < day) {
            newEventAction.setVisibility(View.GONE);
        } else {
            newEventAction.setVisibility(View.VISIBLE);
        }
        setHourAdapter(getContext());
    }

    public void setHourAdapter(Context context) {

            HourAdapter hourAdapter = new HourAdapter(DailyCalendarFragment.newEventAction.getContext(), hourEventList());
            hourListView.setAdapter(hourAdapter);
            hourListView.setSelection(LocalTime.now().getHour());

    }

    private ArrayList<HourEvent> hourEventList() {
        ArrayList<HourEvent> list = new ArrayList<>();
        for (int hour = 0; hour < 24; hour++) {

            LocalTime time = LocalTime.of(hour, 0);

            ArrayList<Event> events = Event.eventsForDateAndTime(selectedDate, time);
            HourEvent hourEvent = new HourEvent(time, events);
            list.add(hourEvent);
/*
            time = LocalTime.of(hour, 30);
            events = Event.eventsForDateAndTime(dateStr, time);
            hourEvent = new HourEvent(time, events);
            list.add(hourEvent);

 */
        }

        return list;
    }
    public void previousDayAction(View view)
    {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusDays(1);
        setDayView();
    }

    public void nextDayAction(View view)
    {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusDays(1);
        setDayView();
    }

    void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}