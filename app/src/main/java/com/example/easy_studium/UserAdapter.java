package com.example.easy_studium;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

    public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{
        private  Context context;
        private List<Persona> personaList;

        public UserAdapter(Context context, List<Persona> personaList){
            this.personaList=personaList;
            this.context=context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view=LayoutInflater.from(context).inflate(R.layout.user_row, parent, false);
            return new UserAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Persona persona=personaList.get(position);
            holder.username.setText(persona.getUsername());
            holder.email.setText(persona.getEmail());
        }

        @Override
        public int getItemCount() {
            return personaList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            public TextView username, email;
            public ViewHolder(View itemView){
                super(itemView);
                username=itemView.findViewById(R.id.username_row);
                email=itemView.findViewById(R.id.email_row);
            }
        }
    }
