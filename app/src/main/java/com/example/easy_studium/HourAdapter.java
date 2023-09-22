package com.example.easy_studium;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentController;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class HourAdapter extends ArrayAdapter<HourEvent>
{
    Button deleteEvent;


    public HourAdapter(@NonNull Context context, List<HourEvent> hourEvents)
    {
        super(context, 0, hourEvents);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        HourEvent event = getItem(position);

        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.hour_cell, parent, false);
            deleteEvent=convertView.findViewById(R.id.deleteEvent);

        setHour(convertView, event.time);
        setEvents(convertView, event.events);
        return convertView;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setHour(View convertView, LocalTime time)
    {
        TextView timeTV = convertView.findViewById(R.id.timeTV);
        timeTV.setText(CalendarUtils.formattedShortTime(time));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setEvents(View convertView, ArrayList<Event> events)
    {
        TextView event1 = convertView.findViewById(R.id.event1);

        if(events.size() == 0)
        {
            hideEvent(event1);
        }
        else if(events.size() == 1)
        {
            setEvent(event1, events.get(0));
        }
        else if(events.size() == 2)
        {
            setEvent(event1, events.get(0));
        }
        else if(events.size() == 3)
        {
            setEvent(event1, events.get(0));
        }
        else
        {
            setEvent(event1, events.get(0));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setEvent(TextView textView, Event event)
    {
        textView.setText(event.getName());
        textView.setVisibility(View.VISIBLE);
    }

    private void hideEvent(TextView tv)
    {
        tv.setVisibility(View.INVISIBLE);
    }


}



