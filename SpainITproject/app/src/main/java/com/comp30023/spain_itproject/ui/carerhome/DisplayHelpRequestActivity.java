package com.comp30023.spain_itproject.ui.carerhome;

import android.arch.lifecycle.Observer;
import android.content.Context;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.comp30023.spain_itproject.R;
import com.comp30023.spain_itproject.domain.Position;
import com.comp30023.spain_itproject.ui.LoginSharedPreference;
import com.comp30023.spain_itproject.ui.chat.ChatActivity;
import com.comp30023.spain_itproject.ui.maps.TrackerMapFragment;

/**
 * Loaded when a user displays a help request
 */
public class DisplayHelpRequestActivity extends AppCompatActivity {

    public static final String EXTRA_SENDER_ID = "SENDER";
    public static final String EXTRA_SENDER_NAME = "NAME";

    public static final String TITLE = " has requested help!";

    private Button callButton;
    private Button messageButton;
    private TextView titleText;
    private TextView timeText;

    private TrackerMapFragment fragment;

    private String currentUserId;
    private String senderId;
    private String senderName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_help_request);

        currentUserId = LoginSharedPreference.getId(this);

        //Get passed in extras from  intent
        Intent intent = getIntent();
        senderId = intent.getStringExtra(EXTRA_SENDER_ID);
        senderName = intent.getStringExtra(EXTRA_SENDER_NAME);

        setTrackerFragment();

        setCallButton();
        setMessageButton();
        setTitleText();
        setTimeText();
        observeTime();
    }

    private void setTrackerFragment() {

        fragment = new TrackerMapFragment();

        Bundle arguments = new Bundle();
        arguments.putString(TrackerMapFragment.ARGUMENT_TRACK_USER_ID, senderId);
        arguments.putString(TrackerMapFragment.ARGYMENT_TRACK_USER_NAME, senderName);
        arguments.putString(TrackerMapFragment.ARGUMENT_CURRENT_USER, currentUserId);

        fragment.setArguments(arguments);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.displayHelpRequest_mapFrame, fragment);
        transaction.commit();
    }

    private void setCallButton() {

        callButton = (Button) findViewById(R.id.displayHelpRequest_callButton);
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Bring up the call options
            }
        });
    }

    private void setMessageButton() {

        final Context context = this;

        messageButton = (Button) findViewById(R.id.displayHelpRequest_messageButton);
        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra(ChatActivity.EXTRA_CHAT_PARTNER_USER_ID, senderId);
                startActivity(intent);
            }
        });
    }

    private void setTitleText() {

        titleText = (TextView) findViewById(R.id.displayHelpRequest_title);
        String title = senderName + TITLE;
        titleText.setText(title);
    }

    private void setTimeText() {

        timeText = (TextView) findViewById(R.id.displayHelpRequest_timeText);
        timeText.setText("");
    }

    private void observeTime() {

        //Create the observer
        Observer<Position> observer = new Observer<Position>() {
            @Override
            public void onChanged(@Nullable Position position) {

                if (position != null) {

                    //Set the time text to the last updated time
                    timeText.setText(position.getTimeStamp());

                }
            }
        };

        //Register to listen
        fragment.addPositionListener(this, observer);
    }
}
