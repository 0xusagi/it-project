package com.comp30023.spain_itproject.ui.calls;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.comp30023.spain_itproject.R;
import com.comp30023.spain_itproject.calls.videoCalls.sinch.SinchClientService;
import com.comp30023.spain_itproject.domain.User;
import com.comp30023.spain_itproject.ui.LoginSharedPreference;
import com.comp30023.spain_itproject.uicontroller.AccountController;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallEndCause;
import com.sinch.android.rtc.video.VideoCallListener;

import java.util.List;

/**
 * New activity when an incomingi internet or voice call is happening
 */
public class IncomingCallActivity extends BaseActivity {
    private String callId;
    private String callerUserId;

    // Views
    private TextView callerName;
    private TextView callType;

    private Button acceptButton;
    private Button declineButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming_call);

        // Get the call id
        callId = getIntent().getStringExtra(SinchClientService.CALL_ID);

        // Get the caller id
        callerUserId = getIntent().getStringExtra(SinchClientService.CALLER_USER_ID);

        // Setup the callerName view
        callerName = findViewById(R.id.incomingVideoCall_callerName);
        setupCallerName();

        // Setup the call type
        callType = findViewById(R.id.incomingVideoCall_callType);

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
            setupCallType(call.getDetails().isVideoOffered());

        } else {
            Log.e(IncomingCallActivity.class.getSimpleName(), "Started with invalid callId, aborting");
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

            // Change the intent to the correct screen
            Intent intent;
            if (call.getDetails().isVideoOffered()) {
                intent = new Intent(this, VideoCallActivity.class);
            }
            else {
                intent = new Intent(this, VoiceCallActivity.class);
            }
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

    /**
     * Listen to an incoming call
     */
    private class SinchCallListener implements VideoCallListener {

        @Override
        public void onVideoTrackAdded(Call call) {

        }

        @Override
        public void onVideoTrackPaused(Call call) {

        }

        @Override
        public void onVideoTrackResumed(Call call) {

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
            Log.d(IncomingCallActivity.class.getSimpleName(), "Call ended: " + cause.toString());
            finish();
        }

        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> list) {
            // No push notifications
        }
    }

    /**
     * Get the name of the caller task
     */
    private class GetCallerNameTask extends AsyncTask<Void, Void, String> {
        private final String SERVER_ERROR_MSG = "Caller name cannot be obtained";
        private Exception exception;

        @Override
        protected String doInBackground(Void... voids) {
            User user;
            // Check if dependent or carer's phone
            // If dependent
            if (LoginSharedPreference.getIsDependent(IncomingCallActivity.this)) {
                try {
                    user = AccountController.getInstance().getCarer(callerUserId);
                    return user.getName();
                } catch (Exception e) {
                    exception = e;
                }
            } else {
                try {
                    user = AccountController.getInstance().getDependent(callerUserId);
                    return user.getName();
                } catch (Exception e) {
                    exception = e;
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String name) {
            // Check if the user is obtained
            if (name == null) {
                callerName.setText(SERVER_ERROR_MSG);
                return;
            }

            // Display the name on the screen
            callerName.setText(name);
        }
    }

    /**
     * Setup the caller name to be displayed
     */
    private void setupCallerName() {
        new GetCallerNameTask().execute();
    }

    /**
     * Setup the type of call which is incoming
     * @param isVideo
     */
    private void setupCallType(boolean isVideo) {
        if (isVideo) {
            callType.setText("Incoming Video Call...");
        }
        else {
            callType.setText("Incoming Voice Call...");
        }
    }
}
