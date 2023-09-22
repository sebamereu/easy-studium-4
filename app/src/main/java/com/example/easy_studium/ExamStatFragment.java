package com.example.easy_studium;

import android.annotation.SuppressLint;
import android.graphics.Color;
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
import android.widget.Spinner;
import android.widget.TextView;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExamStatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@RequiresApi(api = Build.VERSION_CODES.O)
public class ExamStatFragment extends Fragment {

    private Button addExam, editExam;
    private TextView hourStudy;
    private int hourStudyInt;
    private int countDayStudy;
    ListView listView;
    ListViewAdapter adapter;
    ArrayList<String> items;
    AnyChartView anyChartView;
    //String[] months={};
    //int[] values={};
    ArrayList<String> esamiString=new ArrayList<>();
    ArrayList<Integer> minutiInteger=new ArrayList<>();
    String[] esami=new String[Exam.arrayList1.size()];
     int[] minuti=new int[Exam.arrayList1.size()];
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ExamStatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExamStatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExamStatFragment newInstance(String param1, String param2) {
        ExamStatFragment fragment = new ExamStatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view;


        view=inflater.inflate(R.layout.fragment_exam_stat, container, false);


        addExam=view.findViewById(R.id.addExam);
        hourStudy=view.findViewById(R.id.hourStudy);
        listView=view.findViewById(R.id.list);
        anyChartView=view.findViewById(R.id.anyChart);
        editExam=view.findViewById(R.id.editExam);

        String controllo;
        items = new ArrayList<>();
        for (int i = 0; i < Exam.arrayList1.size(); i++) {
            items.add(Exam.arrayList1.get(i));
        }

        adapter = new ListViewAdapter(getActivity(), items);
        listView.setAdapter(adapter);

        PieChartView piechart= view.findViewById(R.id.piechart);

        Float[] percent = new Float[Exam.arrayList1.size()];
        Integer[] colors = new Integer[]{0xffedf8fb, 0xffb2e2e2, 0xff66c2a4, 0xff66c2a4};
       // if (Event.eventsList.size()==0){
            piechart.setVisibility(View.GONE);
       // }

        if (Event.eventsList.size()!=0) {
            for (int j=0;j<Exam.arrayList1.size();j++) {
                String esame;
                int minuto = 0;
                for (int i = 0; i < Event.eventsList.size(); i++) {
                    controllo = String.valueOf(Event.eventsList.get(i).getExam());
                    Log.d("ListViewAdapter", "" + controllo + " / " + Exam.arrayList1.get(j));
                    if (controllo.equals(Exam.arrayList1.get(j)))
                        minuto+=30;

                }
                minuti[j]=minuto;
                minutiInteger.add(minuto);
            }





            for (int j = 0; j < Exam.arrayList1.size(); j++) {

                for (int i = 0; i < Event.eventsList.size(); i++) {
                    controllo = String.valueOf(Event.eventsList.get(i).getExam());
                    if (controllo.equals(Exam.arrayList1.get(j)))
                        esami[j] = controllo;
                    esamiString.add(controllo);
                }
                Log.d("ExamStatFragment", "Minuti[" + j + "]: " + minuti[j]);
            }
        }
        //float calcolo = (minutiTotali/minuti)+100;
        //for (int i=0;i<percent.length;i++){percent[i] =calcolo;}
        /*    piechart.setPercent(Arrays.asList(percent));
        piechart.setSegmentColor(Arrays.asList(colors));

        piechart.setRadius(300);
        piechart.setStrokeColor(Color.BLACK);
        piechart.setStrokeWidth(4);
*/
        //piechart.setSelectedColor(0xff0198E1);
        //piechart.setSelectedWidth(8);

        for (int i=0;i<Event.eventsList.size();i++) {
            if (Event.eventsList.get(i).getDate().getDayOfYear() <Calendar.getInstance().get(Calendar.DAY_OF_YEAR)) {
                countDayStudy++;
            }
        }

        hourStudyInt=countDayStudy/2;
        hourStudy.setText(""+hourStudyInt);

        setupPieChart();


        if (Exam.arrayList1.size()!=0) editExam.setVisibility(View.VISIBLE);
        addExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new ExamFragment());

            }
        });

        editExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new EditExamFragment());
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

    public void setupPieChart(){
        Pie pie= AnyChart.pie();
        List<DataEntry> dataEntries =new ArrayList<>();

        if (esami.length!=0) {
            for (int i = 0; i < esami.length; i++) {
                dataEntries.add(new ValueDataEntry(esami[i], minuti[i]));
            }
        }

        pie.data(dataEntries);
        anyChartView.setChart(pie);
    }


}