package com.example.easy_studium;

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
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditExamFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditExamFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Spinner spinnerExam;
    String nActualCFU;
    ArrayAdapter<String> adapter1;
    ArrayList<String> arrayList;
    TextView nCFU, errorText;
    EditText newNameExam, newNumCFU;
    Button saveExamChange;

    public EditExamFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditExamFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditExamFragment newInstance(String param1, String param2) {
        EditExamFragment fragment = new EditExamFragment();
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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_edit_exam, container, false);

        spinnerExam=view.findViewById(R.id.spinnerExam);
        nCFU=view.findViewById(R.id.nCFU);
        newNameExam=view.findViewById(R.id.newNameExam);
        saveExamChange=view.findViewById(R.id.saveExamChange);
        newNumCFU=view.findViewById(R.id.newCFUExam);
        errorText=view.findViewById(R.id.errorText);
        arrayList=Exam.arrayList1;
        adapter1=ExamFragment.adapter;

        spinnerExam.setAdapter(adapter1);

        saveExamChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInput()) {
                    for (int i = 0; i < Exam.listExam.size(); i++) {
                        Log.d("EditExamFragment", "0 " + spinnerExam.getSelectedItem());

                        if (newNameExam.getText().length() != 0) {

                            if (Exam.listExam.get(i).getNameExam().equals(spinnerExam.getSelectedItem())) {

                                for (int j = 0; j < Event.eventsList.size(); j++) {
                                    Log.d("EditExamFragment", "1 " + newNameExam.getText());

                                    Log.d("EditExamFragment", "2 " + Event.eventsList.get(j).getExamName() + " - " + Event.eventsList.get(j).getExamMode());
                                    Log.d("EditExamFragment", "3 " + Event.eventsList.get(j).getExamName());
                                    Log.d("EditExamFragment", "4 " + newNameExam.getText().toString());

                                    if (Event.eventsList.get(j).getExamName().equals(spinnerExam.getSelectedItem())) {
                                        Event.eventsList.get(j).setExamName(newNameExam.getText());

                                        Log.d("EditExamFragment", "5 " + Event.eventsList.get(j).getExamName());

                                        Event.eventsList.get(j).setNameEvent("" + Event.eventsList.get(j).getExamName() + " - " + Event.eventsList.get(j).getExamMode());
                                        Log.d("EditExamFragment", "6 " + Event.eventsList.get(j).getExamName() + " - " + Event.eventsList.get(j).getExamMode());

                                        if (j > 0) {
                                            if (Event.eventsList.get(j - 1).getDateEvent() == Event.eventsList.get(j).getDateEvent())
                                            //&& Objects.equals(Event.eventsList.get(j - 1).getName(), Event.eventsList.get(j).getName())
                                            //&& Event.eventsList.get(j - 1).getTimePicker().getHour() - Event.eventsList.get(j).getTimePicker().getHour() == 1
                                            //&& Event.eventsList.get(j - 1).getTimePicker().getMinute() - Event.eventsList.get(j).getTimePicker().getMinute() == 0)
                                            {
                                                Event.eventsList.get(j).setNameEvent("");
                                            }

                                            if (Event.eventsList.get(j - 1).getDateEvent() == Event.eventsList.get(j).getDateEvent())
                                            //&& Objects.equals(Event.eventsList.get(j - 1).getName(), Event.eventsList.get(j).getName())
                                            //&& Event.eventsList.get(j - 1).getTimePicker().getHour() - Event.eventsList.get(j).getTimePicker().getHour() == 0
                                            // && Event.eventsList.get(j - 1).getTimePicker().getMinute() - Event.eventsList.get(j).getTimePicker().getMinute() == 30)
                                            {
                                                Event.eventsList.get(j).setNameEvent("");
                                            }
                                        }
                                    }
                                }

                                arrayList.set(i, newNameExam.getText().toString());

                                Exam.listExam.get(i).setNameExam(newNameExam.getText().toString());
                            }
                        }
                        if (newNumCFU.getText().length() != 0) {
                            Exam.listExam.get(i).setCfu(newNumCFU.getText().toString());
                        }
                        replaceFragment(new ExamStatFragment());


                    }

                }
            }


            });
        // Inflate the layout for this fragment
        return view;
    }

    private boolean checkInput() {
        int errors=0;
        /*
        if() {
            errors++;
            newEventAction.setError("Inserire almeno un esme");
        }else newEventAction.setError(null);

        if(eventTimeInizio==null ) {
            errors++;
            eventTimeTV.setError("Inserire orario d'inizio");
        }else  eventTimeTV.setError(null);


        if(eventTimeFine==null ) {
            errors++;
            eventTimeFinish.setError("Inserire orario d'inizio");
        }else  eventTimeFinish.setError(null);

*/

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