package com.example.easy_studium;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
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

@RequiresApi(api = Build.VERSION_CODES.O)
public class ExamStatFragment extends Fragment {

    private Button addExam;
    private TextView hourStudy;
    private int hourStudyInt;
    private int countDayStudy;
    List<Exam> examList;
    ArrayList<String> examNameList;
    ListView listView;
    ExamAdapter adapter;
    ArrayList<String> items;
    ArrayList<String> esamiString = new ArrayList<>();
    ArrayList<Integer> minutiInteger = new ArrayList<>();
    RecyclerView recyclerView;

    public ExamStatFragment() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Exam.arrayList1.clear();
        //Exam.listExam.clear();
        View view;
        view = inflater.inflate(R.layout.fragment_exam_stat, container, false);

        addExam = view.findViewById(R.id.addExam);
        hourStudy = view.findViewById(R.id.hourStudy);
        //listView = view.findViewById(R.id.list);

        recyclerView=view.findViewById(R.id.recycler_exam);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        examList = new ArrayList<>();
        examNameList = new ArrayList<>();

        readExam();


        for (Event event : Event.eventsList) {
            LocalDate localDate = LocalDate.parse(event.getDateEvent());
            if (localDate.getDayOfYear() < Calendar.getInstance().get(Calendar.DAY_OF_YEAR)) {
                countDayStudy++;
            }
        }
        //adapter = new ExamAdapter(getContext(), examNameList);
        //listView.setAdapter(adapter);


        hourStudyInt = countDayStudy / 2;
        hourStudy.setText("" + hourStudyInt);

        addExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new ExamFragment());

            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    private void readExam() {

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("exams");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                examList.clear();
                examNameList.clear();

                String controllo;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Exam exam = snapshot.getValue(Exam.class);
                    if(exam!=null) {
                        if (exam.getExamId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            examList.add(exam);
                            examNameList.add(exam.getNameExam());
                        }
                    }

                }
                adapter=new ExamAdapter(getContext(),examList);
                recyclerView.setAdapter(adapter);
                //Exam.arrayList1 = examNameList;
                //Exam.listExam = (ArrayList<Exam>) examList;


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

    void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}