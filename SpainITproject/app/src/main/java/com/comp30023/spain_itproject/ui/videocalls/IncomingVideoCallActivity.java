package com.comp30023.spain_itproject.ui.videocalls;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.comp30023.spain_itproject.R;
import com.comp30023.spain_itproject.calls.videoCalls.sinch.SinchClientService;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallEndCause;
import com.sinch.android.rtc.video.VideoCallListener;

import java.util.List;

public class IncomingVideoCallActivity extends BaseActivity {
    private String callId;

    // Views
    private TextView callerName;

    private Button acceptButton;
    private Button declineButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming_video_call);

        // Get the call id
        callId = getIntent().getStringExtra(SinchClientService.CALL_ID);

        // Setup the buttons
        setupAcceptButton();
        setupDeclineButton();
    }

    @Override
    protected void onServiceConnected() {
        Call call = getSinchInterface().getCall(callId);
        if (call != null) {
            call.addCallListener(new SinchCallListener());
            TextView remoteUser = findViewById(R.id.incomingVideoCall_callerName);
            remoteUser.setText(call.getRemoteUserId());

        } else {
            Log.e(IncomingVideoCallActivity.class.getSimpleName(), "Started with invalid callId, aborting");
            finish();
        }
    }

    private void setupAcceptButton() {
        acceptButton = findViewById(R.id.incomingVideoCall_accept);

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answer();
            }
        });
    }

    /**
     * Accept the call
     */
    private void answer() {
        Call call = getSinchInterface().getCall(callId);

        // Answer the call
        if (call != null) {
            call.answer();

            // Change the intent to the video call screen
            Intent intent = new Intent(this, VideoCallActivity.class);
            intent.putExtra(SinchClientService.CALL_ID, callId);
            startActivity(intent);
        }
        // No accept then return to previous screen
        else {
            finish();
        }
    }

    private void setupDeclineButton() {
        declineButton = findViewById(R.id.incomingVideoCall_decline);

        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decline();
            }
        });
    }

    /**
     * Decline the call
     */
    private void decline() {
        Call call = getSinchInterface().getCall(callId);

        // Decline the call
        if (call != null) {
            call.hangup();
        }
        finish();
    }

    private class SinchCallListener implements VideoCallListener {

        @Override
        public void onVideoTrackAdded(Call call) {

        }

        @Override
        public void onCallProgressing(Call call) {

        }

        @Override
        public void onCallEstablished(Call call) {

        }

        /**
         * End call
         * @param call
         */
        @Override
        public void onCallEnded(Call call) {
            CallEndCause cause = call.getDetails().getEndCause();
            Log.d(IncomingVideoCallActivity.class.getSimpleName(), "Call ended: " + cause.toString());
            finish();
        }

        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> list) {
            // No push notifications
        }
    }
}
