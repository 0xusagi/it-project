package com.comp30023.spain_itproject.ui;

import android.app.Activity;
import android.content.Context;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.comp30023.spain_itproject.R;

public class DisplayHelpRequestActivity extends AppCompatActivity {

    private Button button2;
    private Button button3;

    private NavigationMapsFragment fragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_help_request);


        final Context context = this;
        final Activity activity = this;

        fragment = new NavigationMapsFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.displayHelpRequest_mapFrame, fragment);
        transaction.commit();

        button2 = findViewById(R.id.button2);
        setButton2Listener(this);

        button3 = findViewById(R.id.button3);
        setButton3Listener(this);

    }


    private void setButton2Listener(final Context context) {
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //fragment.setDestination();
            }
        });
    }


    private void setButton3Listener(final Context context) {
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.clearDestination();
            }
        });
    }
}
