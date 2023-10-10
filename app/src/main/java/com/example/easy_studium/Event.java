package com.example.easy_studium;

import android.content.Context;
import android.os.Build;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.O)
public class Event {

    private String nameEvent;
    private String dateEvent;
    private String time;
    private Object examName;
    private Object examMode;
    private String evenTimePicker;
    private String eventId;
    private Context context;

    public Event(){}
    public Event(String eventId, String nameEvent, String dateEvent, Object examName, Object examMode, String timePicker) {
        this.eventId=eventId;
        this.nameEvent = nameEvent;
        this.dateEvent = dateEvent;
        this.examName =examName;
        this.examMode=examMode;
        this.evenTimePicker = timePicker;
    }
    public static ArrayList<Event> eventsList = new ArrayList<>();

    public Context getContext(){return context;}
    public void setContext(Context context){this.context = context;}

    public static ArrayList<Event> eventsForDate(LocalDate date) {
        ArrayList<Event> events = new ArrayList<>();


        for (int i=0; i<eventsList.size();i++){
            LocalDate localDate = LocalDate.parse(Event.eventsList.get(i).getDateEvent());

            int eventDate = localDate.getDayOfYear();
            int cellDate = date.getDayOfYear();
            if (eventDate==cellDate &&i==0)
                events.add(eventsList.get(i));

        }

        return events;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static ArrayList<Event> eventsForDateAndTime(LocalDate date, LocalTime time) {
        ArrayList<Event> events = new ArrayList<>();


        for (Event event : eventsList) {

            String[] timeParts = event.getEvenTimePicker().split(":");
            int hour = Integer.parseInt(timeParts[0]);
            int minute = Integer.parseInt(timeParts[1]);

            int eventHour = hour;
            int cellHour = time.getHour();
            if (eventHour == cellHour) {
                if (date.toString().equals(event.getDateEvent()))
                    events.add(event);
            }
        }
            /*LocalDate date = LocalDate.parse(dateStr);

            // Dividi la stringa del tempo in ore e minuti
            String[] timeParts = event.getEvenTimePicker().split(":");
            int hour = Integer.parseInt(timeParts[0]);
            int minute = Integer.parseInt(timeParts[1]);

// Imposta l'ora e i minuti nel TimePicker

            int cellHour = time.getHour();
            int cellMinute = time.getMinute();

             */


            //if (event.getDateEvent().equals(date) && hour == cellHour ) {
      /*          if (minute < 30 && cellMinute == 0) {
                    //event.getTimePicker().setHour(i);
                    events.add(event);
                }
                if (minute >= 30 && cellMinute == 30) {
                    events.add(event);
                }
/*
                    if (eventMinute >= 30) {
                        eventHour++;
                        eventMinute -= 30;
                    } else {
                        eventMinute += 30;
                    }

                    event.getTimePicker().setHour(eventHour);

                    event.getTimePicker().setMinute(eventMinute);
*/


        //}

        return events;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static ArrayList<Event> eventsForDateAndTimePicker(LocalDate date, TimePicker time) {
        ArrayList<Event> events = new ArrayList<>();

        for (Event event : eventsList) {
            String timeStr = event.getEvenTimePicker(); // la tua stringa di tempo
            LocalTime localTimeStr = LocalTime.parse(timeStr);
            int eventHour = localTimeStr.getHour();
            int cellHour = time.getHour();
            if (event.getDateEvent().equals(date) && eventHour == cellHour)
                events.add(event);
        }

        return events;
    }


    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
    public String getNameEvent() {
        return nameEvent;
    }

    public void setNameEvent(String name) {
        this.nameEvent = name;
    }

    public String getDateEvent() {
        return dateEvent;
    }

    public void setDateEvent(String dateEvent) {
        this.dateEvent = dateEvent;
    }



    public String getEvenTimePicker() {
        return evenTimePicker;
    }

    public void setEvenTimePicker(String evenTimePicker) {
        this.evenTimePicker = evenTimePicker;
    }

    public Object getExamName() {
        return examName;
    }

    public void setExamName(Object examName) {
        this.examName = examName;
    }

    public Object getExamMode() {
        return examMode;
    }

    public void setExamMode(Object examMode) {
        this.examMode = examMode;
    }
}