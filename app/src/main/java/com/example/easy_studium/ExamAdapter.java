package com.example.easy_studium;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ExamAdapter extends RecyclerView.Adapter<ExamAdapter.ViewHolder>{
    private Context context;
    private List<Exam> examNameList;


    public ExamAdapter(Context context, List<Exam> examNameList) {
        this.context = context;
        this.examNameList = examNameList;
    }
/*
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
            examNameList=new ArrayList<>();
            readExam();
            String esame;
            String controllo;
            int ore = 0;
            int oreTeoriaInt = 0;
            int oreLaboratorioInt = 0;

            for (int i = 0; i < Event.eventsList.size(); i++) {
                controllo = String.valueOf(Event.eventsList.get(i).getExamName());
                if (controllo.equals(examNameList.get(position))) {
                    ore++;
                    if (Event.eventsList.get(i).getExamMode().equals("Teoria")) oreTeoriaInt++;
                    if (Event.eventsList.get(i).getExamMode().equals("Laboratorio"))
                        oreLaboratorioInt++;

                }
            }
            ore = ore / 2;
            oreTeoriaInt = oreTeoriaInt / 2;
            oreLaboratorioInt = oreLaboratorioInt / 2;
            esame = ("" + examNameList.get(position) + ": " + ore + " ore.");
            name.setText(esame);

            esame = ("Teoria: " + oreTeoriaInt + " ore.");
            oreTeoria.setText(esame);

            esame = ("Laboratorio: " + oreLaboratorioInt + " ore.");
            oreLaboratorio.setText(esame);


        }
        return convertView;
    }

 */

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.list_row, parent, false);
        return new ExamAdapter.ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ExamAdapter.ViewHolder holder, int position) {
        Exam exam=examNameList.get(position);

        String esame;
        String controllo;
        int ore = 0;
        int oreTeoriaInt = 0;
        int oreLaboratorioInt = 0;

        for (int i = 0; i < Event.eventsList.size(); i++) {
            controllo = String.valueOf(Event.eventsList.get(i).getExamName());
            if (controllo.equals(examNameList.get(position))) {
                ore++;
                if (Event.eventsList.get(i).getExamMode().equals("Teoria")) oreTeoriaInt++;
                if (Event.eventsList.get(i).getExamMode().equals("Laboratorio"))
                    oreLaboratorioInt++;

            }
        }
        ore = ore / 2;
        oreTeoriaInt = oreTeoriaInt / 2;
        oreLaboratorioInt = oreLaboratorioInt / 2;
        esame = (examNameList.get(position) + ":" + ore + " ore.");
        holder.name.setText(esame);

        esame = ( oreTeoriaInt + " ore.");
        holder.oreTeoria.setText(esame);

        esame = (oreLaboratorioInt + " ore.");
        holder.oreLaboratorio.setText(esame);


        holder.name.setText(exam.getNameExam());
    }

    @Override
    public int getItemCount() {
        return examNameList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView name, oreTeoria, oreLaboratorio;
        public ViewHolder(View itemView){
            super(itemView);
            name=itemView.findViewById(R.id.name);
            oreTeoria = itemView.findViewById(R.id.hourTeoria);
            oreLaboratorio = itemView.findViewById(R.id.hourLaboratorio);

        }
    }
}
