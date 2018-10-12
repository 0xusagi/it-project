package com.comp30023.spain_itproject.ui.videocalls;

import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.comp30023.spain_itproject.R;
import com.comp30023.spain_itproject.calls.videoCalls.sinch.SinchClientService;
import com.sinch.android.rtc.AudioController;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallEndCause;
import com.sinch.android.rtc.calling.CallState;
import com.sinch.android.rtc.video.VideoCallListener;
import com.sinch.android.rtc.video.VideoController;

import java.util.List;

public class VideoCallActivity extends BaseActivity {

    private String callId;

    // Views
    private TextView callTime;
    private TextView callerName;

    private Button endCallButton;
    private Button flipCameraButton;

    private boolean isAddedListener = false;
    private boolean isAddedVideoViews = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_call);

        // Get the views
        callTime = findViewById(R.id.videoCall_callTime);
        callerName = findViewById(R.id.videoCall_remoteCallerName);

        // Setup the endCall button
        endCallButton = findViewById(R.id.videoCall_endCallButton);
        setupEndCallButton();

        // Setup the flip camera button
        flipCameraButton = findViewById(R.id.videoCall_flipCameraButton);
        setupFlipCameraButton();

        // Get the call id of the user
        callId = getIntent().getStringExtra(SinchClientService.CALL_ID);
    }

    private void setupEndCallButton() {
        endCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endCall();
            }
        });
    }

    @Override
    public void onServiceConnected() {
        Call call = getSinchInterface().getCall(callId);
        if (call != null) {
            if (!isAddedListener) {
                call.addCallListener(new VideoCallListener() {
                    @Override
                    public void onVideoTrackAdded(Call call) {
                        Log.d(VideoCallActivity.class.getSimpleName(), "Video track added");
                        setupVideoViews();
                    }

                    @Override
                    public void onVideoTrackPaused(Call call) {
                        call.pauseVideo();
                    }

                    @Override
                    public void onVideoTrackResumed(Call call) {
                        call.resumeVideo();
                    }

                    @Override
                    public void onCallProgressing(Call call) {
                        Log.d(VideoCallActivity.class.getSimpleName(), "Call progressing");
                    }

                    @Override
                    public void onCallEstablished(Call call) {
                        Log.d(VideoCallActivity.class.getSimpleName(), "Call established");
                        setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);

                        // Enable speaker
                        AudioController audioController = getSinchInterface().getAudioController();
                        audioController.enableSpeaker();

                        // TODO start the timer
                    }

                    @Override
                    public void onCallEnded(Call call) {
                        CallEndCause endCause = call.getDetails().getEndCause();
                        Log.d(VideoCallActivity.class.getSimpleName(), endCause.toString());

                        // Return to default audio
                        setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);

                        // End the call
                        endCall();

                        // Make a toast with the call details
                        Toast.makeText(VideoCallActivity.this, "Ended call: " + call.getDetails().toString(), Toast.LENGTH_SHORT).show();
                    }


                    @Override
                    public void onShouldSendPushNotification(Call call, List<PushPair> list) {
                        // Dont need to send push notifications
                    }
                });
            }
        } else {
            finish();
        }
    }

    /**
     * Update the screen to reflect the new information
     */
    private void updateScreen() {
        // Early exit
        if (getSinchInterface() == null) {
            return;
        }

        // Update the interface
        Call call = getSinchInterface().getCall(callId);
        if (call != null) {
            callerName.setText(call.getRemoteUserId());
            if (call.getState() == CallState.ESTABLISHED) {
                setupVideoViews();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        // TODO cancel the timers
        removeVideoViews();
    }

    @Override
    public void onStart() {
        super.onStart();

        // Make a new timer


        updateScreen();
    }

    @Override
    public void onBackPressed() {
        // Nothing happen on back button press
    }

    /**
     * End the call and exit the activity
     */
    private void endCall() {
        Call call = getSinchInterface().getCall(callId);

        if (call != null) {
            call.hangup();
        }
        finish();
    }

    /**
     * Setup the video views to display the videos in video call
     */
    private void setupVideoViews() {
        // Early exit
        if (isAddedVideoViews || getSinchInterface() == null) {
            return;
        }

        VideoController vc = getSinchInterface().getVideoController();

        // Set the local view to display the video
        if (vc != null) {
            RelativeLayout localView = findViewById(R.id.videoCall_localVideo);

            localView.addView(vc.getLocalView());
        }

        LinearLayout view = findViewById(R.id.videoCall_remoteVideo);
        view.addView(vc.getRemoteView());
        isAddedVideoViews = true;
    }

    /**
     * Remove the video views that are displayed in the video call
     */
    private void removeVideoViews() {
        // Early exit
        if (getSinchInterface() == null) {
            return;
        }

        VideoController vc = getSinchInterface().getVideoController();
        if (vc != null) {
            LinearLayout view = findViewById(R.id.videoCall_remoteVideo);
            view.removeAllViews();

            RelativeLayout localView = findViewById(R.id.videoCall_localVideo);
            localView.removeAllViews();
            isAddedVideoViews = false;
        }
    }

    /**
     * Setup the button to flip the camera
     */
    private void setupFlipCameraButton() {
        getSinchInterface().getVideoController().toggleCaptureDevicePosition();
    }
}
