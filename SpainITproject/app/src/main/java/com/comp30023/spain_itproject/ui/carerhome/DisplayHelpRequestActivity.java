package com.comp30023.spain_itproject.ui.carerhome;

import android.app.Activity;
import android.content.Context;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.comp30023.spain_itproject.R;
import com.comp30023.spain_itproject.ui.maps.NavigationMapsFragment;
import com.google.android.gms.maps.SupportMapFragment;

public class DisplayHelpRequestActivity extends AppCompatActivity {

    private Button button2;
    private Button button3;

    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_help_request);


        final Context context = this;
        final Activity activity = this;

        fragment = new SupportMapFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.displayHelpRequest_mapFrame, fragment);
        transaction.commit();

        button2 = findViewById(R.id.button2);

        button3 = findViewById(R.id.button3);

    }
}
