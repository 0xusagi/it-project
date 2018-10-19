package com.comp30023.spain_itproject.ui.carerhome;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.comp30023.spain_itproject.R;
import com.comp30023.spain_itproject.ui.LoginSharedPreference;

public class DisplayHelpRequestActivity extends ViewDependentLocationActivity {

    public static final String TITLE = " has requested help!";

    private TextView titleText;

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


    private void setTitleText() {

        titleText = (TextView) findViewById(R.id.displayHelpRequest_title);
        String title = senderName + TITLE;
        titleText.setText(title);
    }



}
