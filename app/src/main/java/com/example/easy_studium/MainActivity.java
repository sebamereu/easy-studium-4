package com.example.easy_studium;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.easy_studium.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    String prova;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(new DailyCalendarFragment());

        binding.bottomNavigationView2.setOnItemSelectedListener(item -> {

            switch (item.getItemId()){
                case R.id.calendario:
                    replaceFragment(new DailyCalendarFragment());
                    break;
                case R.id.esami:
                    replaceFragment(new ExamStatFragment());
                    break;
                case R.id.profilo:
                    replaceFragment(new UserFragment());
                    break;


            }


            return true;
        });
    }
    void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}