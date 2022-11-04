package com.example.familymapclient.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.example.familymapclient.R;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;


public class MainActivity extends AppCompatActivity {

    private static FragmentManager fragmentManager;
    @SuppressLint("StaticFieldLeak")
    private static LoginFragment loginFragment;
    private static MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Iconify.with(new FontAwesomeModule());
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        loginFragment = (LoginFragment) fragmentManager.findFragmentById(R.id.loginFragID);
        if (loginFragment == null) {
            loginFragment = new LoginFragment();
            fragmentManager.beginTransaction().add(R.id.fragment_container, loginFragment).commit();
        }
    }


    public void switchFragment() {
        mapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = new MapFragment();
            fragmentManager.beginTransaction().replace(R.id.fragment_container, mapFragment).commit();
        }
    }

}