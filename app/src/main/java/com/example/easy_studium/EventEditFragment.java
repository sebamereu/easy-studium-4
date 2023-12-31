package com.example.easy_studium;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class EventEditFragment extends DialogFragment {

    /*inizializzazione variabili*/
    private EditText eventNameET;
    private TextView eventDateTV, errorText, newExamAction;
    private Button saveEventAction, eventTimeTV, eventTimeFinish;
    private String timeNowString;
    private LocalDate localDate;
    public static Spinner spinner, spinnerToDo;
    private TimePicker eventTimeInizio, eventTimeFine;
    public static int hour, minute;
    List<Exam> examList;
    ArrayList<String> examNameList;


    public EventEditFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /*gestisce tutto il fragment_event_edit.xml*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_event_edit, container, false);

        /*assegnazione dei vari EditText/TextView/Button presenti nel file .xml*/
        saveEventAction = view.findViewById(R.id.saveEventAction);
        eventNameET = view.findViewById(R.id.eventNameET);
        eventDateTV = view.findViewById(R.id.eventDateTV);
        eventTimeTV = view.findViewById(R.id.eventTimeTV);
        eventTimeFinish = view.findViewById(R.id.eventTimeFinish);
        spinner = view.findViewById(R.id.spinner1);
        spinnerToDo = view.findViewById(R.id.spinner2);
        errorText = view.findViewById(R.id.errorText);
        newExamAction = view.findViewById(R.id.newExamAction);

        /*metodo per estrarre gli esami dell'utente loggato dal database e assegnarli a spinner*/
        readExam();

        spinner.setVisibility(View.VISIBLE);
        newExamAction.setVisibility(View.VISIBLE);

        /*viene assegnato allo spinnerToDo tre opzion "Teoria", "Laboratorio" o "Appello esame"*/
        ArrayAdapter<CharSequence> examMoodAdapter = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.exams_array, android.R.layout.simple_spinner_item);
        examMoodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerToDo.setAdapter(examMoodAdapter);

        LocalTime timeNow = LocalTime.now(); // il tuo oggetto LocalTime
        timeNowString = timeNow.toString();
        localDate = CalendarUtils.selectedDate;
        eventDateTV.setText("Date: " + CalendarUtils.formattedDate(CalendarUtils.selectedDate));

        /*assegna l'orario di inizio dell'evento*/
        eventTimeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //eventTimeTV.setRawInputType(InputType.TYPE_DATETIME_VARIATION_TIME);
                TimePickerDialog.OnTimeSetListener onTimeSetListener =
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker eventTime, int hour, int minute) {

                                if (eventTime.getMinute() < 10)
                                    eventTimeTV.setText("Time: " + eventTime.getHour() + ":" + eventTime.getMinute() + "0");

                                if (eventTime.getHour() < 10)
                                    eventTimeTV.setText("Time: 0" + eventTime.getHour() + ":" + eventTime.getMinute());

                                if (eventTime.getHour() < 10 && eventTime.getMinute() < 10)
                                    eventTimeTV.setText("Time: 0" + eventTime.getHour() + ":" + eventTime.getMinute() + "0");

                                eventTime.setHour(eventTime.getHour());
                                eventTime.setMinute(eventTime.getMinute());
                                eventTimeInizio = eventTime;
                            }
                        };
                int style = AlertDialog.THEME_DEVICE_DEFAULT_DARK;

                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), style, onTimeSetListener, hour, minute, true);

                timePickerDialog.setTitle("Select time");
                timePickerDialog.show();

            }
        });

        /*assegna l'orario di fine dell'evento*/
        eventTimeFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //eventTimeTV.setRawInputType(InputType.TYPE_DATETIME_VARIATION_TIME);
                TimePickerDialog.OnTimeSetListener onTimeSetListener1 =
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker eventTime, int hour, int minute) {
                                if (eventTime.getMinute() < 10)
                                    eventTimeFinish.setText("Time: " + eventTime.getHour() + ":" + eventTime.getMinute() + "0");

                                if (eventTime.getHour() < 10)
                                    eventTimeFinish.setText("Time: 0" + eventTime.getHour() + ":" + eventTime.getMinute());

                                if (eventTime.getHour() < 10 && eventTime.getMinute() < 10)
                                    eventTimeFinish.setText("Time: 0" + eventTime.getHour() + ":" + eventTime.getMinute() + "0");


                                eventTime.setHour(eventTime.getHour());
                                eventTime.setMinute(eventTime.getMinute());
                                eventTimeFine = eventTime;
                            }
                        };
                int style = AlertDialog.THEME_DEVICE_DEFAULT_DARK;

                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), style, onTimeSetListener1, hour, minute, true);

                timePickerDialog.setTitle("Select time");
                timePickerDialog.show();

            }
        });

        /*si passa al fragment per l'aggiunta di un nuovo esame*/
        newExamAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new ExamFragment());
            }
        });

        /*gestisce tutta la parte di salvataggio di un nuovo evento*/
        saveEventAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInput()) {

                    /*il nome dell'evento sarà composto dall'opzioni selezionate nei due spinner*/
                    String eventName = (spinner.getSelectedItem().toString()
                            + "-" + spinnerToDo.getSelectedItem().toString());

                    /*per capire quante celle orarie bisogna cambiare nel calendario giornaliero si fa una differenza
                    * tra orario di inizio e orario di fine*/
                    TimePicker timePickerInizio = eventTimeInizio;
                    TimePicker timePickerFine = eventTimeFine;
                    int hourInizio = timePickerInizio.getHour();
                    int hourFine = timePickerFine.getHour();
                    int minuteInizio = timePickerInizio.getMinute();
                    int minuteFine = timePickerFine.getMinute();

                    int cont = 1;
                    int max = (hourFine - hourInizio) * 2;

                    if (minuteFine >= 30) max++;
                    if (minuteInizio >= 30) max--;
                    if (hourFine - hourInizio == 0) max = 1;
                    if (hourFine - hourInizio == 0 && minuteFine < 30) max = 0;


                    TimePicker[] timePickers = new TimePicker[max + 1];
                    String[] timePickString = new String[max + 1];
                    for (int i = 0; i < max; i++) {
                        timePickers[i] = new TimePicker(getContext());

                        timePickers[i].setHour(hourInizio+i);
                        timePickers[i].setMinute(minuteInizio);
                        if (i == 0) {
                            timePickers[i].setHour(hourInizio);
                            timePickers[i].setMinute(minuteInizio);
                        } else if (timePickers[i - 1].getMinute() >= 30) {
                            timePickers[i].setHour(hourInizio + cont);
                            timePickers[i].setMinute(minuteInizio - 30);
                            cont++;
                        } else {
                            timePickers[i].setHour(timePickers[i - 1].getHour());
                            timePickers[i].setMinute(minuteInizio + 30);

                        }
                        int hour = timePickers[i].getCurrentHour();
                        int minute = timePickers[i].getCurrentMinute();
                        timePickString[i] = hour + ":" + minute; // convert TimePicker to String

                    }

                    /*la data dell'evento sarà data dal giorno selezionato nel DailyCalendarFragment*/
                    String dateStr = CalendarUtils.selectedDate.toString(); // convert LocalDate to String

                    // Ottieni l'utente attualmente autenticato
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    FirebaseUser user = auth.getCurrentUser();


                    // Ottieni un riferimento al database
                    FirebaseDatabase database = FirebaseDatabase.getInstance();

                    // Crea un riferimento alla cartella "events" nel database
                    DatabaseReference eventReference = database.getReference("events");

                    if (user != null) {
                        Event[] events = new Event[max + 1];

                        for (int i = 0; i < max; i++) {

                            if (i == 0) {
                                /*controlla che tutti gli elementi presenti nella pagina non siano vuoti*/
                                if (spinner.getSelectedItem()==null) {
                                    Toast.makeText(getContext(), "Enter exam.",
                                            Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (spinnerToDo.getSelectedItem()==null) {
                                    Toast.makeText(getContext(), "Enter study mood.",
                                            Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (spinnerToDo.getSelectedItem()==null) {
                                    Toast.makeText(getContext(), "Enter study mood.",
                                            Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (timePickers[i]==null){
                                    Toast.makeText(getContext(), "Enter start time.",
                                            Toast.LENGTH_SHORT).show();
                                    return;

                                }
                                // Crea l'oggetto da assegnare all'utente
                                events[i] = new Event(FirebaseAuth.getInstance().getUid(), eventName,
                                        dateStr, spinner.getSelectedItem(), spinnerToDo.getSelectedItem(), timePickString[i]);

                                // L'utente è autenticato, quindi possiamo procedere con l'assegnazione dell'oggetto
                                /*l'evento sarò inserito nel database*/
                                eventReference.push().setValue(events[i]);

                            /*dentro l'if sarà creato l'evento con il titolo(per la prima cella oraria)
                            , tutte le sucessive celle orario non avranno nome, ma saranno comunque eventi */
                            } else {

                                // Crea l'oggetto da assegnare all'utente
                                events[i] = new Event(FirebaseAuth.getInstance().getUid(), "",
                                        dateStr, spinner.getSelectedItem(), spinnerToDo.getSelectedItem(), timePickString[i]);

                                // L'utente è autenticato, quindi possiamo procedere con l'assegnazione dell'oggetto
                                eventReference.push().setValue(events[i]);

                            }

                        }
                        /*si ritorno al fragment di visualizazzione del calendario giornaliero*/
                        replaceFragment(new DailyCalendarFragment());

                    }
                }
            }
        });
        return view;
    }

    /*metodo per estrarre gli esami dell'utente loggato dal database e assegnarli a spinner*/
    private void readExam() {
        /*si crea il riferimento per inserire/prendere i dati dal database*/
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("exams");

        /*si crea una lista per inserire gli esami creati in precedenza dall'utente*/
        examList = new ArrayList<>();
        examNameList = new ArrayList<>();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                /*puliamo la lista di esami*/
                examList.clear();
                examNameList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Exam exam = snapshot.getValue(Exam.class);
                    /*prendiamo dal database gli elementi Exam che son stati creati dall'utente
                     * loggato*/
                    if (exam != null) {
                        if (exam.getExamId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            /*inseriamoli nelle liste*/
                            examList.add(exam);
                            examNameList.add(exam.getNameExam());
                        }
                    }

                }
                /*adattiamo lo spinner in base agli esami inseriti dall'utente in precedenza*/
                ArrayAdapter<String> examAdapter = new ArrayAdapter<>(EventEditFragment.spinner.getContext(),
                        android.R.layout.simple_spinner_item, examNameList);
                examAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(examAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

/*segnala se ci sono errori nell'inserimento degli input per la creazione di un evento*/
    private boolean checkInput() {
        int errors = 0;

        if (eventTimeInizio == null) {
            errors++;
            eventTimeTV.setError("Inserire orario d'inizio");
        } else eventTimeTV.setError(null);


        if (eventTimeFine == null) {
            errors++;
            eventTimeFinish.setError("Inserire orario d'inizio");
        } else eventTimeFinish.setError(null);


        switch (errors) {
            case 0:
                errorText.setVisibility(View.GONE);
                errorText.setText("");
                break;
            case 1:
                errorText.setVisibility(View.VISIBLE);
                errorText.setText("Si è verificato un errore");
                break;
            default:
                errorText.setVisibility(View.VISIBLE);
                errorText.setText("Si sono verificati " + errors + " errori");
                break;
        }
        return errors == 0;
    }

    /*metodo per passare da un fragment all'altro rimanendo nel MainActiivty*/
    void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    public static String TAG = "EventEditDialog";
}