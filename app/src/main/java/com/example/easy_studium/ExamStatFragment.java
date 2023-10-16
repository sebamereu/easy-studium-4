package com.example.easy_studium;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/*questa classe gestisce il secondo fragment dove si possono visualizzare gli esami e le ore
* di studio dedicato per ognuno di essi*/
public class ExamStatFragment extends Fragment {

    /*inizializzazione variabili*/
    private Button addExam;
    private TextView hourStudy;
    private int hourStudyInt;
    private int countDayStudy;
    List<Exam> examList;
    ArrayList<String> examNameList;
    ExamAdapter adapter;
    ArrayList<String> esamiString = new ArrayList<>();
    ArrayList<Integer> minutiInteger = new ArrayList<>();
    RecyclerView recyclerView;

    public ExamStatFragment() {
        // Required empty public constructor
    }


    /*gestisce tutto il fragment_exam_stat.xml*/
    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view;
        view = inflater.inflate(R.layout.fragment_exam_stat, container, false);

        /*assegnazione dei vari EditText/TextView/Button presenti nel file .xml*/
        addExam = view.findViewById(R.id.addExam);
        hourStudy = view.findViewById(R.id.hourStudy);

        recyclerView=view.findViewById(R.id.recycler_exam);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        examList = new ArrayList<>();
        examNameList = new ArrayList<>();

        /*metodo per estrarre gli esami dell'utente loggato dal database e assegnarli a spinner*/
        readExam();

        /*calcolo delle ore totali di studio fino ad oggi*/
        for (Event event : Event.eventsList) {
            /*per ogni evento presente nell'eventsList si controlla se la data è precedente al giorno
            * odierno, in caso affermativo verrà incrementato il contatore delle ore di studio*/
            LocalDate localDate = LocalDate.parse(event.getDateEvent());
            if (localDate.getDayOfYear() < Calendar.getInstance().get(Calendar.DAY_OF_YEAR)) {
                countDayStudy++;
            }
        }
        hourStudyInt = countDayStudy / 2;
        hourStudy.setText("" + hourStudyInt);

        /*se si clicca questo button si va nel Fragment per aggiungere un nuovo esame*/
        addExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new ExamFragment());

            }
        });

        return view;
    }

    /*metodo per estrarre gli esami dell'utente loggato dal database e aggiungerli all examList*/
    private void readExam() {

        /*si crea il riferimento per inserire/prendere i dati dal database*/
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("exams");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                /*puliamo la lista di esami*/
                examList.clear();
                examNameList.clear();

                String controllo;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Exam exam = snapshot.getValue(Exam.class);
                    if(exam!=null) {
                        /*prendiamo dal database gli elementi Exam che son stati creati dall'utente
                         * loggato*/
                        if (exam.getExamId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            /*inseriamoli nelle liste*/
                            examList.add(exam);
                            examNameList.add(exam.getNameExam());
                        }
                    }

                }
                /*con la classe ExamAdapter si adatta la listview con gli esami già aggiunti in precedenza
                * dall'utente*/
                adapter=new ExamAdapter(getContext(),examList);
                recyclerView.setAdapter(adapter);

                /*si calcola quante ore son state dedicate per ogni esame fino ad oggi, compresa la parte
                * di teoria e la parte di laboratorio*/
                if (Event.eventsList.size() != 0) {
                    String[] esami = new String[examNameList.size()];
                    int[] minuti = new int[examNameList.size()];
                    for (int j = 0; j < examNameList.size(); j++) {
                        int minuto = 0;
                        for (int i = 0; i < Event.eventsList.size(); i++) {
                            controllo = String.valueOf(Event.eventsList.get(i).getExamName());
                            if (controllo.equals(examNameList.get(j)))
                                minuto += 30;

                        }
                        minuti[j] = minuto;
                        minutiInteger.add(minuto);
                    }

                    for (int j = 0; j < examNameList.size(); j++) {

                        for (int i = 0; i < Event.eventsList.size(); i++) {
                            controllo = String.valueOf(Event.eventsList.get(i).getExamName());
                            if (controllo.equals(examNameList.get(j)))
                                esami[j] = controllo;
                            esamiString.add(controllo);
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /*metodo per passare da un fragment all'altro rimanendo nel MainActiivty*/
    void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}