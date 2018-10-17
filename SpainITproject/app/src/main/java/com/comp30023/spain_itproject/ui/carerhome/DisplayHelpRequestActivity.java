package com.comp30023.spain_itproject.ui.carerhome;

import android.app.Activity;
import android.content.Context;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.comp30023.spain_itproject.R;
import com.comp30023.spain_itproject.domain.User;
import com.comp30023.spain_itproject.ui.LoginSharedPreference;
import com.comp30023.spain_itproject.ui.maps.TrackerMapFragment;

public class DisplayHelpRequestActivity extends AppCompatActivity {

    public static final String EXTRA_SENDER_ID = "SENDER";
    public static final String EXTRA_SENDER_NAME = "NAME";

    private Button button2;
    private Button button3;

    private Fragment fragment;

    private String senderId;
    private String senderName;

    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_help_request);

        final Context context = this;
        final Activity activity = this;

        fragment = new TrackerMapFragment();

        String currentUserId = LoginSharedPreference.getId(this);

        Intent intent = getIntent();
        senderId = intent.getStringExtra(EXTRA_SENDER_ID);
        senderName = intent.getStringExtra(EXTRA_SENDER_NAME);

        Bundle arguments = new Bundle();
        arguments.putString(TrackerMapFragment.ARGUMENT_TRACK_USER_ID, senderId);
        arguments.putString(TrackerMapFragment.ARGYMENT_TRACK_USER_NAME, senderName);
        arguments.putString(TrackerMapFragment.ARGUMENT_CURRENT_USER, currentUserId);

        fragment.setArguments(arguments);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.displayHelpRequest_mapFrame, fragment);
        transaction.commit();

        button2 = findViewById(R.id.button2);

        button3 = findViewById(R.id.button3);

    }
}
