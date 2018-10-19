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
import com.comp30023.spain_itproject.calls.videoCalls.sinch.SinchClientService;
import com.comp30023.spain_itproject.domain.Position;
import com.comp30023.spain_itproject.ui.LoginSharedPreference;
import com.comp30023.spain_itproject.ui.calls.BaseActivity;
import com.comp30023.spain_itproject.ui.calls.VideoCallActivity;
import com.comp30023.spain_itproject.ui.calls.VoiceCallActivity;
import com.comp30023.spain_itproject.ui.chat.ChatActivity;
import com.comp30023.spain_itproject.ui.maps.TrackerMapFragment;
import com.sinch.android.rtc.calling.Call;

/**
 * Loaded when a user displays a help request
 */
public class ViewDependentLocationActivity extends BaseActivity {

    public static final String EXTRA_SENDER_ID = "SENDER";
    public static final String EXTRA_SENDER_NAME = "NAME";

    private Button callButton;
    private Button messageButton;
    private TextView timeText;

    private TrackerMapFragment fragment;

    private String currentUserId;
    private String senderId;
    private String senderName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_dependent_location);

        currentUserId = LoginSharedPreference.getId(this);

        //Get passed in extras from  intent
        Intent intent = getIntent();
        senderId = intent.getStringExtra(EXTRA_SENDER_ID);
        senderName = intent.getStringExtra(EXTRA_SENDER_NAME);

        setTrackerFragment();

        setCallButton();
        setMessageButton();
        setTimeText();
        observeTime();
    }

    public void setTrackerFragment() {

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

    public void setCallButton() {

        final Context context = this;
        callButton = (Button) findViewById(R.id.displayHelpRequest_callButton);
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Call call = getSinchInterface().callUserVoice(senderId);
                Intent intent = new Intent(context, VoiceCallActivity.class);
                intent.putExtra(SinchClientService.CALL_ID, call.getCallId());

                startActivity(intent);
            }
        });
    }

    public void setMessageButton() {

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

    public void setTimeText() {

        timeText = (TextView) findViewById(R.id.displayHelpRequest_timeText);
        timeText.setText("");
    }

    public void observeTime() {

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
