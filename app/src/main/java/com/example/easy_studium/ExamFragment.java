package com.example.easy_studium;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExamFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExamFragment extends Fragment {
    private  TextView errorText;
    private EditText examName, examCFU;
    private Button saveExam;
    public static ArrayAdapter<String> adapter;
    public static ArrayAdapter<Exam> adapterExam;

    private Button eventTimeInizioLunediTV, eventTimeFineLunediTV,
            eventTimeInizioMartediTV, eventTimeFineMartediTV,
            eventTimeInizioMercolediTV, eventTimeFineMercolediTV,
            eventTimeInizioGiovediTV, eventTimeFineGiovediTV,
            eventTimeInizioVenerdiTV, eventTimeFineVenerdiTV;

    private TimePicker eventTimeInizioLunedi, eventTimeFineLunedi,
            eventTimeInizioMartedi, eventTimeFineMartedi,
            eventTimeInizioMercoledi, eventTimeFineMercoledi,
            eventTimeInizioGiovedi, eventTimeFineGiovedi,
            eventTimeInizioVenerdi, eventTimeFineVenerdi;

    public static ArrayList<String> arrayList;
    public static ArrayList<Exam> arrayListExam;

    FirebaseDatabase database;

    public static int hour, minute;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ExamFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_exam, container, false);

        examName=view.findViewById(R.id.examName);
        examCFU=view.findViewById(R.id.examCFU);
        saveExam=view.findViewById(R.id.saveExamAction);
        errorText=view.findViewById(R.id.errorText);

        saveExam.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if (checkInput()) {
                    Exam e = new Exam(examName.getText().toString(), examCFU.getText().toString());
                    e.setNameExam(examName.getText().toString());
                    e.setCfu(examCFU.getText().toString());
                    Exam.arrayList1.add(e.getNameExam());
                    Exam.listExam.add(e);

                    // Ottieni l'istanza di FirebaseAuth
                    FirebaseAuth auth = FirebaseAuth.getInstance();

                    // Ottieni l'utente attualmente autenticato
                    FirebaseUser user = auth.getCurrentUser();

                    if (user != null) {
                        // L'utente è autenticato, quindi possiamo procedere con l'assegnazione dell'oggetto

                        // Ottieni un riferimento al database
                        FirebaseDatabase database = FirebaseDatabase.getInstance();

                        // Crea un riferimento all'utente nel database
                        DatabaseReference userRef = database.getReference("user").child(user.getUid());
                        DatabaseReference examRef = userRef.child("exams");


                        // Crea l'oggetto da assegnare all'utente
                         Exam exam = new Exam(examName.getText().toString(), examCFU.getText().toString());

                        // Assegna l'oggetto all'utente nel database
                        examRef.child(e.getNameExam()).setValue(exam);
                    }
                    replaceFragment(new DailyCalendarFragment());
                }
            }
        });

                return view;
    }

    private boolean checkInput() {
        int errors=0;
        int nCfu;
        if (examCFU.length()!=0) {
            nCfu = Integer.parseInt(String.valueOf(examCFU.getText()));
        }else nCfu=0;

        if(examName.length()==0) {
            errors++;
            examName.setError("Inserire il nome dell'esame");
        }else examName.setError(null);

        if(examCFU.length()==0
                || nCfu>30
                || !examCFU.getText().toString().matches("(.*[0-9].*)")
                || examCFU.getText().toString().matches("(.*[a-z].*)")
                || examCFU.getText().toString().matches("(.*[A-Z].*)")
                || examCFU.getText().toString().matches("^(?=.*[_.()$&@]).*$")){
            errors++;
            examCFU.setError("Inserire numero valido");
        }else examCFU.setError(null);


        switch (errors){
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
                errorText.setText("Si sono verificati "+errors+" errori");
                break;
        }

        return errors==0;
    }

    void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}