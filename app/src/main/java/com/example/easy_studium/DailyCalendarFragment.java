package com.example.easy_studium;

import static com.example.easy_studium.CalendarUtils.selectedDate;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

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
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

@RequiresApi(api = Build.VERSION_CODES.O)
public class DailyCalendarFragment extends Fragment {

    private Button newEventAction, previousDayAction, nextDayAction, deleteEvent, weeklyAction;
    private TextView monthDayText, event1;
    private TextView dayOfWeekTV;
    private ListView hourListView;
    LocalTime time = LocalTime.now();



    public DailyCalendarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CalendarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DailyCalendarFragment newInstance(String param1, String param2) {
        DailyCalendarFragment fragment = new DailyCalendarFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_daily_calendar, container, false);
        View convertView= inflater.inflate(R.layout.hour_cell, container, false);

        monthDayText = (TextView) view.findViewById(R.id.monthDayText);
        dayOfWeekTV = (TextView) view.findViewById(R.id.dayOfWeekTV);
        hourListView = (ListView) view.findViewById(R.id.hourListView);
        previousDayAction = (Button) view.findViewById(R.id.previousDayAction);
        nextDayAction=(Button) view.findViewById(R.id.nextDayAction);
        newEventAction= (Button) view.findViewById(R.id.newEventAction);
        event1=view.findViewById(R.id.event1);
        deleteEvent=view.findViewById(R.id.deleteEvent);
        weeklyAction=view.findViewById(R.id.weeklyAction);

        CalendarUtils.selectedDate = LocalDate.now();

        setDayView();





        previousDayAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusDays(1);
                setDayView();;
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
            public void onClick(View v) {
                replaceFragment(new EventEditFragment());
            }
        });

        weeklyAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new WeekViewFragment());
            }
        });


        hourListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Fragment fragment = null;

                switch (position) {

                    case 0:
                        fragment = new DeleteEventFragment();

                        break;

                }

                if (fragment != null) {

                    FragmentTransaction ft = getFragmentManager().beginTransaction();

                    ft.replace(R.id.deleteEvent, fragment);

                    ft.addToBackStack(null);

                    ft.commit();
                }

            }
        });



        // Inflate the layout for this fragment
        return  view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        setDayView();
    }

    private void setDayView()
    {
        monthDayText.setText(CalendarUtils.monthDayFromDate(selectedDate));
        String dayOfWeek = selectedDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());
        dayOfWeekTV.setText(dayOfWeek);
        int dateDay=selectedDate.getDayOfYear();
        int day=Calendar.getInstance().get(Calendar.DAY_OF_YEAR);

        int i = (int) (new Date().getTime()/1000);
        if(dateDay<day)
        {
            newEventAction.setVisibility(View.GONE);
        }
        else {
            newEventAction.setVisibility(View.VISIBLE);
        }
        setHourAdapter();
    }

    public void setHourAdapter()
    {
        HourAdapter hourAdapter = new HourAdapter(getActivity().getApplicationContext(), hourEventList());
        hourListView.setAdapter(hourAdapter);
    }

    private ArrayList<HourEvent> hourEventList()
    {
        ArrayList<HourEvent> list = new ArrayList<>();
        for(int hour = 0; hour < 24; hour++) {
            LocalTime time = LocalTime.of(hour, 0);
            ArrayList<Event> events = Event.eventsForDateAndTime(selectedDate, time);
            HourEvent hourEvent = new HourEvent(time, events);
            list.add(hourEvent);

             time = LocalTime.of(hour, 30);
             events = Event.eventsForDateAndTime(selectedDate, time);
             hourEvent = new HourEvent(time, events);
            list.add(hourEvent);

            Log.d("DailyCalendarFragment1", ""+list.size());
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

    void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}