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

    /*inizializzazione variabili*/
    private Button nextDayAction;
    public static Button newEventAction, previousDayAction;
    public static TextView monthDayText;

    public static TextView dayOfWeekTV;
    public static ListView hourListView;
    ArrayList<Event> eventList;


    public DailyCalendarFragment() {
        // Required empty public constructor
    }

    /*gestisce tutto il fragment_daily_calendar.xml*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daily_calendar, container, false);

        /*assegnazione dei vari EditText/TextView/Button presenti nel file .xml*/
        monthDayText = (TextView) view.findViewById(R.id.monthDayText);
        dayOfWeekTV = (TextView) view.findViewById(R.id.dayOfWeekTV);
        hourListView = (ListView) view.findViewById(R.id.hourListView);
        previousDayAction = (Button) view.findViewById(R.id.previousDayAction);
        nextDayAction = (Button) view.findViewById(R.id.nextDayAction);
        newEventAction = (Button) view.findViewById(R.id.newEventAction);

        /*assegna la data visualizzata dall'utente in quel momento*/
        CalendarUtils.selectedDate = LocalDate.now();

        /*creazione di una lista per assegnare gli eventi creati in precedenza da quel preciso utente*/
        eventList = new ArrayList<>();

        /*metodo per estrarre gli eventi dell'utente loggato dal database*/
        readEvent();

        /*se si clicca questo button si va alla giornata precedente*/
        previousDayAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusDays(1);
                setDayView();

            }
        });

        /*se si clicca questo button si va alla giornata successiva*/
        nextDayAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusDays(1);
                setDayView();
            }
        });

        /*se si clicca questo button si passa al fragment di creazione di eventi*/
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

    /*metodo per assegnare gli eventi dell'utente loggato ad eventList*/
    private void readEvent() {
        /*si crea il riferimento per inserire/prendere i dati dal database*/
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("events");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                /*puliamo la lista di eveti*/
                Event.eventsList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    /*prendiamo dal database gli elementi Event che son stati creati dall'utente
                    * loggato*/
                    Event event = snapshot.getValue(Event.class);
                    assert event != null;
                    if (event.getEventId().equals(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())) {
                        /*inseriamoli nella eventlist*/
                        Event.eventsList.add(event);
                    }
                }
                /*metodo per visualizzare il calendario giornaliero considerando gli eventi inserito
                * per quel giorno*/
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

    /*metodo per visualizzare il calendario giornaliero considerando gli eventi inserito
     * per quel giorno*/
    private void setDayView() {
        /*si inizializza il mese e il giorno*/
        monthDayText.setText(CalendarUtils.monthDayFromDate(selectedDate));
        String dayOfWeek = selectedDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());
        dayOfWeekTV.setText(dayOfWeek);
        int dateDay = selectedDate.getDayOfYear();
        int day = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);

        /*se il giorno visualizzato è precedente al giorno odierno non si visualizzerà il button
        * per entrare nel fragment di creazione nuovo evento*/
        if (dateDay < day) {
            newEventAction.setVisibility(View.GONE);
        } else {
            newEventAction.setVisibility(View.VISIBLE);
        }
        /*metodo per adattare ad ogni ora l'eventuale evento creato in precedenza dall'utente*/
        setHourAdapter(getContext());
    }

    /*metodo per adattare ad ogni ora l'eventuale evento creato in precedenza dall'utente*/
    public void setHourAdapter(Context context) {
            /*si inizializza un nuovo HourAdapter in base agli eventi creati dall'utente in precedenza*/
            HourAdapter hourAdapter = new HourAdapter(DailyCalendarFragment.newEventAction.getContext(), hourEventList());
            hourListView.setAdapter(hourAdapter);
            hourListView.setSelection(LocalTime.now().getHour());

    }

    /*si analizza ogni ora del giorno e si assegna un label se in quell'orario è presente un evento*/
    private ArrayList<HourEvent> hourEventList() {
        ArrayList<HourEvent> list = new ArrayList<>();
        for (int hour = 0; hour < 24; hour++) {

            LocalTime time = LocalTime.of(hour, 0);

            ArrayList<Event> events = Event.eventsForDateAndTime(selectedDate, time);
            HourEvent hourEvent = new HourEvent(time, events);
            list.add(hourEvent);

            time = LocalTime.of(hour, 30);
            events = Event.eventsForDateAndTime(selectedDate, time);
            hourEvent = new HourEvent(time, events);
            list.add(hourEvent);
        }

        return list;
    }

    /*metodo per passare da un fragment all'altro rimanendo nel MainActiivty*/
    void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}