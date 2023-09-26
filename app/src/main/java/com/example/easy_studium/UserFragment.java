package com.example.easy_studium;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;

public class UserFragment extends Fragment {

    TextView userText, passwordText, dataText, cittaText, modificaPassword;
    Button logoutButton, admin;
    Persona personaRicevuta;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_user,container,false);

        logoutButton= (Button) view.findViewById(R.id.logoutButton);

        userText=(TextView) view.findViewById(R.id.confUser);
        passwordText=(TextView) view.findViewById(R.id.confPassword);
        dataText=(TextView) view.findViewById(R.id.confData);
        cittaText=(TextView) view.findViewById(R.id.confCitta);
        logoutButton=(Button) view.findViewById(R.id.logoutButton);
        modificaPassword=(TextView) view.findViewById(R.id.modificaPassword);



        personaRicevuta= SignupActivity.rubrica.get(SignupActivity.rubrica.size()-1);
        userText.setText(personaRicevuta.getUsername());
        passwordText.setText(personaRicevuta.getPassword());
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        cittaText.setText(personaRicevuta.getCitta());


        /*Button per fare logout*/
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });

        /*Button per modificare la password*/
        modificaPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), PasswordActivity.class));
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }






    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
  /*  private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    public UserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserFragment.
     */
 /*   public static UserFragment newInstance(String param1, String param2) {
        UserFragment fragment = new UserFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false);
    }*/
}