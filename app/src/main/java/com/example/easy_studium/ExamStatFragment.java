package com.example.easy_studium;

import android.annotation.SuppressLint;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.anychart.AnyChartView;

import java.util.ArrayList;
import java.util.Calendar;
@RequiresApi(api = Build.VERSION_CODES.O)
public class ExamStatFragment extends Fragment {

    private Button addExam;
    private TextView hourStudy;
    private int hourStudyInt;
    private int countDayStudy;
    ListView listView;
    ListViewAdapter adapter;
    ArrayList<String> items;
    ArrayList<String> esamiString=new ArrayList<>();
    ArrayList<Integer> minutiInteger=new ArrayList<>();
    String[] esami=new String[Exam.arrayList1.size()];
     int[] minuti=new int[Exam.arrayList1.size()];

    public ExamStatFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view;
        view=inflater.inflate(R.layout.fragment_exam_stat, container, false);

        items = new ArrayList<>();

        addExam=view.findViewById(R.id.addExam);
        hourStudy=view.findViewById(R.id.hourStudy);
        listView=view.findViewById(R.id.list);

        String controllo;

        items.addAll(Exam.arrayList1);

        adapter = new ListViewAdapter(getActivity(), items);
        listView.setAdapter(adapter);

        if (Event.eventsList.size()!=0) {
            for (int j=0;j<Exam.arrayList1.size();j++) {
                int minuto = 0;
                for (int i = 0; i < Event.eventsList.size(); i++) {
                    controllo = String.valueOf(Event.eventsList.get(i).getExamName());
                    Log.d("ListViewAdapter", "" + controllo + " / " + Exam.arrayList1.get(j));
                    if (controllo.equals(Exam.arrayList1.get(j)))
                        minuto+=30;

                }
                minuti[j]=minuto;
                minutiInteger.add(minuto);
            }

            for (int j = 0; j < Exam.arrayList1.size(); j++) {

                for (int i = 0; i < Event.eventsList.size(); i++) {
                    controllo = String.valueOf(Event.eventsList.get(i).getExamName());
                    if (controllo.equals(Exam.arrayList1.get(j)))
                        esami[j] = controllo;
                    esamiString.add(controllo);
                }
                Log.d("ExamStatFragment", "Minuti[" + j + "]: " + minuti[j]);
            }
        }

        for (int i=0;i<Event.eventsList.size();i++) {
            if (Event.eventsList.get(i).getDateEvent().getDayOfYear() <Calendar.getInstance().get(Calendar.DAY_OF_YEAR)) {
                countDayStudy++;
            }
        }

        hourStudyInt=countDayStudy/2;
        hourStudy.setText(""+hourStudyInt);

        addExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new ExamFragment());

            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}