package com.example.easy_studium;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment {

    private Calendar date;
    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        DatePicker datePicker= new DatePicker(getActivity());
        date=Calendar.getInstance();
        return new AlertDialog.Builder(requireContext())
                .setMessage("Inserisci la data")
                .setView(datePicker)
                .setPositiveButton("OK", (dialog, which) -> {
                    //cosa vogliamo succeda una volta premuto il tasto play
                    date.set(Calendar.YEAR, datePicker.getYear());
                    date.set(Calendar.MONTH, datePicker.getMonth());
                    date.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());

                    ((SignupActivity)getActivity()).doPositiveClick(date);

                } )
                .setNegativeButton("Annulla",
                        ((dialog, which) ->{dismiss();}))
                .create();
    }

    public static String TAG = "DataPickerDialog";
}

