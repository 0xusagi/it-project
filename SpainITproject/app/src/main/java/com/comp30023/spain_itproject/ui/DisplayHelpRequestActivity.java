package com.comp30023.spain_itproject.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.comp30023.spain_itproject.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import static com.comp30023.spain_itproject.ui.carerhome.CarerMapsActivity.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;

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
                fragment.setDestination();
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
