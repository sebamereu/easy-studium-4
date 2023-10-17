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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ExamAdapter extends RecyclerView.Adapter<ExamAdapter.ViewHolder>{
    private Context context;
    private List<Exam> examNameList;


    public ExamAdapter(Context context, List<Exam> examNameList) {
        this.context = context;
        this.examNameList = examNameList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.list_row, parent, false);
        return new ExamAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExamAdapter.ViewHolder holder, int position) {

        int ore = 0;
        int oreTeoriaInt = 0;
        int oreLaboratorioInt = 0;
        String oreString= "";
        String oreTeoriaString= "";
        String oreLabString= "";

        for (Event event : Event.eventsList) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate eventDate = null;
            try {
                // Convertir la cadena en un objeto LocalDate
                eventDate = LocalDate.parse(event.getDateEvent(), formatter);

                // Ahora 'fecha' contiene la fecha en formato LocalDate
                System.out.println("Fecha: " + eventDate);
            } catch (Exception e) {
                System.out.println("La cadena no pudo ser convertida a LocalDate. Asegúrate de que esté en el formato correcto.");
            }
            if (event.getExamName().equals(examNameList.get(position).getNameExam())
                    && event.getEventId().equals(examNameList.get(position).getExamId())
                    && LocalDate.now().isAfter(eventDate)) {
                ore++;
                if (event.getExamMode().equals("Teoria")) oreTeoriaInt++;
                if (event.getExamMode().equals("Laboratorio")) oreLaboratorioInt++;

            }
        }
        if(ore>0){
            ore = ore / 2;
            oreTeoriaInt = oreTeoriaInt / 2;
            oreLaboratorioInt = oreLaboratorioInt / 2;
            //esame = (examNameList.get(position).getNameExam());
        }

        oreString =""+ore;
        oreTeoriaString =""+oreTeoriaInt;
        oreLabString =""+oreLaboratorioInt;

        holder.oreTot.setText(oreString);
        holder.oreTeoria.setText(oreTeoriaString);
        holder.oreLaboratorio.setText(oreLabString);
        holder.name.setText(examNameList.get(position).getNameExam());
    }

    @Override
    public int getItemCount() {
        return examNameList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView name, oreTeoria, oreLaboratorio, oreTot;
        public ViewHolder(View itemView){
            super(itemView);
            name=itemView.findViewById(R.id.name);
            oreTot=itemView.findViewById(R.id.hour);
            oreTeoria = itemView.findViewById(R.id.hourTeoria);
            oreLaboratorio = itemView.findViewById(R.id.hourLaboratorio);

        }
    }
}
