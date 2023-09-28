package com.example.easy_studium;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;

public class ListViewAdapter extends ArrayAdapter<String> {
    ArrayList<String> list;
    Context context;

    public ListViewAdapter(Context context, ArrayList<String> items) {
        super(context, R.layout.list_row, items);
        this.context = context;
        list = items;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            Button editExam;
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.list_row, null);
            TextView name = convertView.findViewById(R.id.name);
            TextView oreTeoria = convertView.findViewById(R.id.hourTeoria);
            TextView oreLaboratorio = convertView.findViewById(R.id.hourLaboratorio);

            String esame;
            String controllo;
            int ore = 0;
            int oreTeoriaInt = 0;
            int oreLaboratorioInt = 0;

            for (int i = 0; i < Event.eventsList.size(); i++) {
                controllo = String.valueOf(Event.eventsList.get(i).getExamName());
                Log.d("ListViewAdapter", "" + controllo + " / " + Exam.arrayList1.get(position));
                if (controllo.equals(Exam.arrayList1.get(position))) {
                    ore++;
                    if (Event.eventsList.get(i).getExamMode().equals("Teoria")) oreTeoriaInt++;
                    if (Event.eventsList.get(i).getExamMode().equals("Laboratorio"))
                        oreLaboratorioInt++;

                }
            }
            ore = ore / 2;
            oreTeoriaInt = oreTeoriaInt / 2;
            oreLaboratorioInt = oreLaboratorioInt / 2;
            esame = ("" + Exam.arrayList1.get(position) + ": " + ore + " ore.");
            name.setText(esame);

            esame = ("Teoria: " + oreTeoriaInt + " ore.");
            oreTeoria.setText(esame);

            esame = ("Laboratorio: " + oreLaboratorioInt + " ore.");
            oreLaboratorio.setText(esame);


        }
        return convertView;
    }

}
