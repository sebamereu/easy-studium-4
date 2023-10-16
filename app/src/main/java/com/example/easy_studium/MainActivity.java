package com.example.easy_studium;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.easy_studium.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
@RequiresApi(api = Build.VERSION_CODES.O)

public class MainActivity extends AppCompatActivity {

    /*inizializzazione variabili*/
    public static int HOUR_ALARM=8;
    public static int MINUTE_ALARM=30;
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /*creazione di un allarme che attiverà una notifica ogni giorno alle 8:30 per invitare l'utente
        * ad usufruire dell'applicazione*/
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, HOUR_ALARM);
        calendar.set(Calendar.MINUTE,MINUTE_ALARM);
        calendar.set(Calendar.SECOND,0);
        /*il codice passerà alla classe che gestirà questo servizio*/
        Intent intent = new Intent(this, ReminderBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        /*il tutto verrà gestito usufruendo di un ForegroundService che quindi no ndipenderà dal ciclo
        * di vita del MainActivity o dell'app in generale, ma sarà sempre attivo anche dopo la chiusura
        * dell'App*/
        ContextCompat.startForegroundService(this, intent);

        /*L'app in gnerale è divisa in 3 Fragment principali
        * 1) Un fragment per la visione del calendario giornaliero
        * 2) Un fragment per la gestione degli esami
        * 3) Un fragment per la chat con gli altri utenti dell'app */

        /*all'apertura dell'app verrà visualizzato dall'utente il primo fragment, cioè quello del
        * calendario giornaliero*/
        replaceFragment(new DailyCalendarFragment());

        /*inizializza l'utente loggato in quel momento*/
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            /*viene creato il calendario con gli eventi, già creati dall'utente in precedenza, presi dal database */
            CalendarUtils.selectedDate = LocalDate.now();

            ArrayList<HourEvent> list = new ArrayList<>();
            for (int hour = 0; hour < 24; hour++) {
                LocalTime time = LocalTime.of(hour, 0);
                /*nel metodo eventsForDateAndTime si prendono effettivamente dal databse gli eventi creati in precedenza
                * e si adattano ad ogni data e orario del calendario*/
                ArrayList<Event> events = Event.eventsForDateAndTime(LocalDate.now(), time);
                HourEvent hourEvent = new HourEvent(time, events);
                list.add(hourEvent);

                time = LocalTime.of(hour, 30);
                events = Event.eventsForDateAndTime(LocalDate.now(), time);
                hourEvent = new HourEvent(time, events);
                list.add(hourEvent);
            }
        }

        /*questo è il menù di navigazione presente in basso dove si può scegliere quale dei tre fragment
        * visualizzare*/
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
    /*metodo per passare da un fragment all'altro rimanendo nel MainActiivty*/
    void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}
